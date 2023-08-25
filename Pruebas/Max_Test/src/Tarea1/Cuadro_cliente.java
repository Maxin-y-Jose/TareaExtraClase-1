package Tarea1;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
  
public class Cuadro_cliente extends JFrame implements ActionListener{

	JButton boton_enviar; //esto hace que l boton se pueda acceder desde afera del constructor
	JTextField mensaje;
	JTextField ip;
	JButton ok;
	
	Cuadro_cliente(){
		
		this.setTitle("Chat ;)");//nombre del cuadro
		
		this.setSize(500,600); // tama;o del this
		
		this.setVisible(true); //hace ell this visible en la pantalla
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//para poder cerrarlo con lla x
		
		this.setResizable(false);// para que el tamaño sea fijo
		
		this.getContentPane().setBackground(new Color (50,50,50));//para el color del fondo
		
		boton_enviar = new JButton();//es el boton de enviar
		
		boton_enviar.setBounds(325, 505, 150, 50);//son las dimennsiones y la posicion del boton
		
		boton_enviar.setText("Enviar");// el texto en el boton
		
		boton_enviar.setFocusable(false);//para que el texto este en el centro
		
		boton_enviar.setBackground(new Color(160,50,100));//para el color del botton
		
		boton_enviar.setFont(new Font("Comic Sans",Font.BOLD,22));//para el tipo de letra, el tamaño y  el estilo del texto del boton
		
		boton_enviar.setForeground(new Color(0,0,0));//para el color de la letra del boton
		
		boton_enviar.setBorder(BorderFactory.createLoweredSoftBevelBorder());//para el borde del boton
		
		this.add(boton_enviar); //para que se agrugue al cuadro el boton
		
		mensaje = new JTextField();
		
		mensaje.setFont(new Font("Comic Sans",Font.PLAIN,15));
		
		mensaje.setBounds(5, 505, 300, 50);
		
		mensaje.setForeground(new Color(0,0,0));
		
		this.add(mensaje);
		
		ip = new JTextField();
		
		ip.setFont(new Font("Comic Sans",Font.PLAIN,15));
		
		ip.setBounds(10, 10, 200, 20);
		
		ip.setForeground(new Color(0,0,0));
		
		this.add(ip);
		
		ok = new JButton();//es el boton de enviar
		
		ok.setBounds(220, 8, 50, 23);//son las dimennsiones y la posicion del boton
		
		ok.setText("Ok");// el texto en el boton
		
		ok.setFocusable(false);//para que el texto este en el centro
		
		ok.setBackground(new Color(160,50,100));//para el color del botton
		
		ok.setFont(new Font("Comic Sans",Font.BOLD,19));//para el tipo de letra, el tamaño y  el estilo del texto del boton
		
		ok.setForeground(new Color(0,0,0));//para el color de la letra del boton
		
		ok.setBorder(BorderFactory.createLoweredSoftBevelBorder());//para el borde del boton
		
		this.add(ok);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//aqui va el evento de los sockets
	}
		
}
