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
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println("Estoy a la escucha");

		try {
			ServerSocket servidor= new ServerSocket(9999); //Este a la escucha

			while (true) { //No hay problema con este ciclo, ya que el hilo ejecuta en segundo plano este bucle

				Socket miSocket = servidor.accept(); //Acepte el servidor

				DataInputStream flujoEntrada= new DataInputStream(miSocket.getInputStream());

				String mensajeTexto=flujoEntrada.readUTF();

				areatexto.append(mensajeTexto+"\n");

				miSocket.close();
				
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
