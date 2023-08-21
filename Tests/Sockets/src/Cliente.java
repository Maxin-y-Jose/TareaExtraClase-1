/* Importante la API de Java
 * https://docs.oracle.com/javase/8/docs/api/
 * https://docs.oracle.com/javase/8/docs/api/
 * https://docs.oracle.com/javase/8/docs/api/
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
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
		}	
	
}

class LaminaMarcoCliente extends JPanel{
	
	public LaminaMarcoCliente(){
	
		JLabel texto=new JLabel("CLIENTE");
		
		add(texto);
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");

		EnviaTexto miEvento = new EnviaTexto();
		miboton.addActionListener(miEvento);
		
		add(miboton);	
		
	}
	
	
	private class EnviaTexto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println(campo1.getText());

			try {
				Socket miSocket = new Socket("192.168.100.13",9999);

				DataOutputStream flujo_salida= new DataOutputStream(miSocket.getOutputStream());

				flujo_salida.writeUTF(campo1.getText()); //En el flujo de datos de salida va a viajar el texto del campo1, escribe en el flujo lo que hay en el stream

				flujo_salida.close(); //Se cierra para que no quede abierto

				


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
	
	private JButton miboton;
	
}