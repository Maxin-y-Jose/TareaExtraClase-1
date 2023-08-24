/* Importante la API de Java
 * https://docs.oracle.com/javase/8/docs/api/
 * https://docs.oracle.com/javase/8/docs/api/
 * https://docs.oracle.com/javase/8/docs/api/
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

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

	@Override
	public void run() { //Se ejecuta en segundo plano
		// TODO Auto-generated method stub
		//System.out.println("Estoy a la escucha");

		try {
			ServerSocket servidor= new ServerSocket(9999); //Esté a la escucha

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

				/*DataInputStream flujoEntrada= new DataInputStream(miSocket.getInputStream());

				String mensajeTexto=flujoEntrada.readUTF();

				areatexto.append(mensajeTexto+"\n");*/

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

			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


