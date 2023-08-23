package Tarea1;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.JFrame;

public class Cuadro_cliente extends JFrame implements ActionListener{

	JButton boton_enviar; //esto hace que l boton se pueda acceder desde afera del constructor
	
	Cuadro_cliente(){
		
		this.setTitle("Chat ;)");
		
		this.setSize(500,600); // tama;o del this
		
		this.setVisible(true); //hace ell this visible en la pantalla
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setResizable(false);
		
		this.getContentPane().setBackground(new Color (30,30,30));
		
		boton_enviar = new JButton();
		
		boton_enviar.setBounds(170, 270, 150, 50);
		
		boton_enviar.setText("Enviar");
		
		boton_enviar.setFocusable(false);
		
		boton_enviar.setBackground(new Color(160,50,100));
		
		boton_enviar.setFont(new Font("Comic Sans",Font.BOLD,22));
		
		boton_enviar.setForeground(new Color(0,0,0));
		
		boton_enviar.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		
		this.add(boton_enviar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//aqui va el evento de los sockets
		
		
	}
}
