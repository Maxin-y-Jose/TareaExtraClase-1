/* Importante la API de Java
 * https://docs.oracle.com/javase/8/docs/api/
 */

import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;

/**
 * Esta clase Cliente permite la creación de la ventana JFrame, además de que permite cerrar la ventana
 * @author Max Stradi
 * @version 20/08/2023
 */
public class ClienteIP {

	public static void main(String[] args) {
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}

/**
 * La clase MarcoCliente va a delimitar las dimensiones de la ventana, además de que sirve para el envío de la IP del cliente que ejecute por primera vez el programa
 * @author José Barquero y Max Stradi
 * @version 25/08/2023
 */
class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);

		addWindowListener(new EnvioOnline()); //Permite enviar la IP al servidor

		}	
	
}

//--------Envio Señal Online-------
/**
 * La clase EnvioOnline hereda una clase adaptador de ventana, la cual es parte del java.awt.event, esto permite que genere un evento de al iniciar la ventana de cliente
 * que envíe un texto " online" al servidor, que permitirá reconocer que se ha conectado por primera vez un nuevo cliente al chat, para así podes envíar su IP y añadirla
 * a una ArrayList con los IPs presentes en el chat.
 * @author José Barquero
 * @version 24/08/2023
 */
class EnvioOnline extends WindowAdapter{

	/**
	 * Constructor para el evento de cuando se abra por primera vez el programa del cliente
	 * @param e Este parámetro es el que genera el evento que envía la IP al servidor
	 */
	public void windowOpened(WindowEvent e){
		
		try{
			Socket miSocket2= new Socket("192.168.100.7",9999);

			PaqueteEnvio datos=new PaqueteEnvio();

			datos.setMensaje(" online");

			ObjectOutputStream paqueteDatos=new ObjectOutputStream(miSocket2.getOutputStream());

			paqueteDatos.writeObject(datos);

			miSocket2.close();

		}catch(Exception e1){}

	}
}
//-------------------------------------

/**
 * Esta clase es dónde se van a añadir todos los aspectos gráficos que va a visulizar el usuario
 * @author Max Stradi
 * @version 20/08/2023
 */
class LaminaMarcoCliente extends JPanel implements Runnable{
	
	public LaminaMarcoCliente(){

		String nickUsuario=JOptionPane.showInputDialog("Nick: "); //Genera una ventana emergente para añadir un nombre de usuario

		JLabel nNick=new JLabel("Nick: ");

		add(nNick);
	
		nick=new JLabel();

		nick.setText(nickUsuario);

		add(nick);

		JLabel texto=new JLabel("Online: ");
		
		add(texto);

		ip=new JComboBox();

		/*ip.addItem("Usuario 1");

		ip.addItem("Usuario 2");

		ip.addItem("Usuario 3");*/

		//ip.addItem("192.168.100.7");

		add(ip);
		
		campoChat = new JTextArea(12,20);

		add(campoChat);
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");

		EnviaTexto miEvento = new EnviaTexto();
		miboton.addActionListener(miEvento);
		
		add(miboton);	

		Thread miHilo= new Thread(this);

		miHilo.start();
	}
	
	/**
	 * Esta clase consiste en la creación del "canal" (socket) que permite comunicar al cliente con el servidor para el envío de datos
	 * @author José Barquero
	 * @version 24/08/2023
	 */
	private class EnviaTexto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println(campo1.getText());

			campoChat.append("\n"+campo1.getText());

			try {
				Socket miSocket = new Socket("192.168.100.7",9999);
				
				//Objeto como paquete de los datos que se quieren enviar
				PaqueteEnvio datos = new PaqueteEnvio(); //Se quiere empaquetar los datos en el cliente (ip, nickname, mensaje)

				datos.setNick(nick.getText());
				
				datos.setIp(ip.getSelectedItem().toString());

				datos.setMensaje(campo1.getText());

				//Creación de un flujo para enviar al destinatario
				ObjectOutputStream paqueteDatos=new ObjectOutputStream(miSocket.getOutputStream()); //Flujo de salida para enviar el objeto por la red

				//Se requiere escribir en nuestro flujo de datos nuestro paquete de datos

				paqueteDatos.writeObject(datos);

				miSocket.close();

				/*DataOutputStream flujo_salida= new DataOutputStream(miSocket.getOutputStream());

				flujo_salida.writeUTF(campo1.getText()); //En el flujo de datos de salida va a viajar el texto del campo1, escribe en el flujo lo que hay en el stream

				flujo_salida.close(); //Se cierra para que no quede abierto*/

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				//e1.printStackTrace();
				System.out.println(e1.getMessage());
			}
		}
		
	}
		
	private JTextField campo1;

	private JComboBox ip;

	private JLabel nick;
	
	private JTextArea campoChat;

	private JButton miboton;

	/**
	 * El método run permite que los clientes puedan ver el IP de los usuarios que se van conectando al chat con el uso de la lista desplegable (JComboBox)
	 * @author José Barquero
	 * @version 25/08/2023
	 */
	@Override
	public void run() {
		try {
			try (ServerSocket servidor_cliente = new ServerSocket(9090)) {
				Socket cliente;

				PaqueteEnvio paqueteRecibido;

				while(true){
					
					cliente=servidor_cliente.accept();

					ObjectInputStream flujoEntrada= new ObjectInputStream(cliente.getInputStream());

					paqueteRecibido=(PaqueteEnvio) flujoEntrada.readObject();

					//Bloque if/else que evita que se repitan mensajes de IPs que se conectan en el chat del usuario

					if(!paqueteRecibido.getMensaje().equals(" online")){

						campoChat.append("\n"+paqueteRecibido.getNick()+": "+paqueteRecibido.getMensaje()); //Extrae el nombre y mensaje

					}
					else{

						//campoChat.append("\n"+paqueteRecibido.getIps()); //Extrae el ArrayList

						ArrayList <String> ipsMenu = new ArrayList<String>(); //Se crea un ArrayList para añadir los IP al menú

						ipsMenu=paqueteRecibido.getIps(); //Se almacenan las IPs enviadas por el servidor

						ip.removeAllItems(); //Cada vez que un cliente se conecta se elimina todo lo que haya en el JCombo todo lo que estba antes

						for(String z:ipsMenu){

							ip.addItem(z);

						}

					}

				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}

/**
 * Esta clase permite la creación de un paquete de datos que va a ser serializado para ser enviado al servidor y así el tenga los datos y los comunique a los demás clientes presentes en el chat
 * @author José Barquero
 * @version 25/08/2023
 */
class PaqueteEnvio implements Serializable{

	//Es importante serializar el paquete de datos ya que se enviará un error de tipo serial, debido a la falta de la implementación de esta inferfaz
	/*
	 * ¿Qué es serialización?
	 * La serialización le dice a un objeto que debe de convertirse en una serie de bytes para ser enviados por la red
	 */

	private String nick,ip,mensaje;

	private ArrayList<String> ips;

	public ArrayList<String> getIps() {
		return ips;
	}

	public void setIps(ArrayList<String> ips) {
		this.ips = ips;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}