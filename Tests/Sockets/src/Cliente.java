/* Importante la API de Java
 * https://docs.oracle.com/javase/8/docs/api/
 */

import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


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
class EnvioOnline extends WindowAdapter{

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

		ip.addItem("Usuario 1");

		ip.addItem("Usuario 2");

		ip.addItem("Usuario 3");

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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket servidor_cliente=new ServerSocket(9090);
			Socket cliente;

			PaqueteEnvio paqueteRecibido;

			while(true){
				
				cliente=servidor_cliente.accept();

				ObjectInputStream flujoEntrada= new ObjectInputStream(cliente.getInputStream());

				paqueteRecibido=(PaqueteEnvio) flujoEntrada.readObject();

				campoChat.append("\n"+paqueteRecibido.getNick()+": "+paqueteRecibido.getMensaje());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
}


class PaqueteEnvio implements Serializable{

	//Es importante serializar el paquete de datos ya que se enviará un error de tipo serial, debido a la falta de la implementación de esta inferfaz
	/*
	 * ¿Qué es serialización?
	 * La serialización le dice a un objeto que debe de convertirse en una serie de bytes para ser enviados por la red
	 */

	private String nick,ip,mensaje;

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