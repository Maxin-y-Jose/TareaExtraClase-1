import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Esta clase es la que va a generar la ventana que puede ser cerrada para el servidor
 * @author Max Stradi
 * @version 20/08/2023
 */
public class Servidor  {

	public static void main(String[] args) {
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

/**
 * Clase que añade a la interfaz gráfica lo necesario que se necesita ver en el servidor, como las IPs conectadas, etc...
 * @author Max Stradi
 * @version 24/08/2023
 */
class MarcoServidor extends JFrame implements Runnable {
	
	public MarcoServidor(){
		
		setBounds(1200,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);

		Thread miHilo = new Thread(this);

		miHilo.start();
		
		}
	
	private	JTextArea areatexto;

	/**
	 * Dentro del método run se tiene toda la lógica de los sockets a utilizar.
	 * Se crean sockets que permiten la escucha hasta recibir una conexión cliente-servidor, en donde se genera un flujo de datos para los clientes conectados.
	 * Este flujo de datos permite enviar paquetes con los nicks, IPs y mensajes que cada usuario envíe. En donde, estos datos pasan primeramente por el servidor para luego ser enviados al destinatario.
	 */
	@Override
	public void run() { //Se ejecuta en segundo plano
		//System.out.println("Estoy a la escucha");

		try {
			try (ServerSocket servidor = new ServerSocket(9999)) {
				String nick,ip,mensaje;

				PaqueteEnvio paqueteRecibido; //Se instancia este objeto para recibir los datos desmenuzados

				while (true) { //No hay problema con este ciclo, ya que el hilo ejecuta en segundo plano este bucle

					Socket miSocket = servidor.accept(); //Acepte el servidor


					ObjectInputStream paqueteDatos=new ObjectInputStream(miSocket.getInputStream()); //Se crea el flujo de datos de entrada

					paqueteRecibido=(PaqueteEnvio) paqueteDatos.readObject(); //Se recibe el paquete y se guarda

					//Se accede a la información presente en el paquete para que se pueda visualizar

					nick=paqueteRecibido.getNick();

					ip=paqueteRecibido.getIp();

					mensaje=paqueteRecibido.getMensaje();

					//Creación de un ArrayList para almacenar las IP de los clientes presentes para añadirlos a la lista del JCombo
					ArrayList <String> listaIP= new ArrayList<String>();

					/*DataInputStream flujoEntrada= new DataInputStream(miSocket.getInputStream());

					String mensajeTexto=flujoEntrada.readUTF();

					areatexto.append(mensajeTexto+"\n");*/

					if (!mensaje.equals(" online")){ //Cuando no es la primera vez que se conecta el usuario

						areatexto.append("\n"+nick+": "+mensaje+" para "+ip); //Se tiene al servidor de por medio para la revisión de mensajes de un usuario

						Socket enviaDestinatario = new Socket(ip,9090); //Se crea un nuevo socket en donde va a viajar la información del servidor al nuevo cliente

						//Flujo de datos vacío
						ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

						//Se escribe en el flujo los datos recibidos
						paqueteReenvio.writeObject(paqueteRecibido);
						//Con esto se llevan los datos al otro lado
						paqueteReenvio.close();
						
						enviaDestinatario.close();

						miSocket.close();
					}
					else{ //Si es la primera vez que se conecta el usuario
					//-------Detecta Online------

						InetAddress localizacion=miSocket.getInetAddress(); //Se almacena la dirección del cliente en formato InetAddres

						String ipRemota=localizacion.getHostAddress(); //Se almacena la dirección IP del usuario
						System.out.println("Online "+ipRemota);

						listaIP.add(ipRemota); //Cada que se conecta un cliente se añade la IP en el ArrayList

						paqueteRecibido.setIps(listaIP); //Se añade un elemento más a nuestro paquete de datos

						for (String z: listaIP) {
							
							Socket enviaDestinatario = new Socket(z,9090); //Se crea un nuevo socket en donde va a viajar la información del servidor al nuevo cliente

							//Flujo de datos vacío
							ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

							//Se escribe en el flujo los datos recibidos
							paqueteReenvio.writeObject(paqueteRecibido);
							//Con esto se llevan los datos al otro lado
							paqueteReenvio.close();
							
							enviaDestinatario.close();

							miSocket.close();
						}

					//-----------------------------
					}
					
				}
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
