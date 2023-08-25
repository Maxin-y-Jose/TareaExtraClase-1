import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;

public class ClienteLocal {

    public static void main(String[] args) {
        MarcoCliente mimarco=new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class MarcoCliente extends JFrame {

    public MarcoCliente(){
        setBounds(600, 300, 280, 350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
        addWindowListener(new EnvioOnline()); // Permite enviar la IP al servidor
    }
}

class EnvioOnline extends WindowAdapter {

    public void windowOpened(WindowEvent e){
        try {
            Socket miSocket2 = new Socket("localhost", 9999); // Cambio a "localhost"
            PaqueteEnvio datos = new PaqueteEnvio();
            datos.setMensaje(" online");
            ObjectOutputStream paqueteDatos = new ObjectOutputStream(miSocket2.getOutputStream());
            paqueteDatos.writeObject(datos);
            miSocket2.close();
        } catch(Exception e1){}
    }
}

class LaminaMarcoCliente extends JPanel implements Runnable {

    public LaminaMarcoCliente(){
        String nickUsuario = JOptionPane.showInputDialog("Nick: ");
        JLabel nNick = new JLabel("Nick: ");
        add(nNick);
        nick = new JLabel();
        nick.setText(nickUsuario);
        add(nick);
        JLabel texto1= new JLabel("Puerto: ");
        add(texto1);
        puertoComboBox=new JComboBox<>();
        puertoComboBox.addItem(9999);
        puertoComboBox.addItem(9090);
        puertoComboBox.addItem(9080);
        add(puertoComboBox);
        campoChat = new JTextArea(12, 20);
        add(campoChat);
        campo1 = new JTextField(20);
        add(campo1);
        miboton = new JButton("Enviar");
        EnviaTexto miEvento = new EnviaTexto();
        miboton.addActionListener(miEvento);
        add(miboton);
        Thread miHilo = new Thread(this);
        miHilo.start();
    }
    
    private class EnviaTexto implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            campoChat.append("\n" + campo1.getText());
            try {
                int puerto=(int) puertoComboBox.getSelectedItem();
                Socket miSocket = new Socket("localhost", puerto);
                PaqueteEnvio datos = new PaqueteEnvio();
                datos.setNick(nick.getText());
                datos.setIp("192.168.100.7");
                datos.setMensaje(campo1.getText());
                ObjectOutputStream paqueteDatos = new ObjectOutputStream(miSocket.getOutputStream());
                paqueteDatos.writeObject(datos);
                miSocket.close();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            } catch(NumberFormatException ex){
                System.out.println("Error: Ingrese un número válido de puerto");
            }
        }
    }

    private JTextField campo1;
    private JComboBox<Integer> puertoComboBox;
    private JLabel nick;
    private JTextArea campoChat;
    private JButton miboton;

    @Override
    public void run() {
        try {
            try (ServerSocket servidor_cliente = new ServerSocket(9090)) {
                Socket cliente;
                PaqueteEnvio paqueteRecibido;
                while(true) {
                    cliente = servidor_cliente.accept();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
                    paqueteRecibido = (PaqueteEnvio) flujoEntrada.readObject();

                    if (!paqueteRecibido.getMensaje().equals(" online")) {
                        campoChat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());
                    } else {
                        
                        System.out.println("Hola");
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
