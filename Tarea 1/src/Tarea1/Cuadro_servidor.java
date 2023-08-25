package Tarea1;

import javax.swing.*;

import java.awt.*;

public class Cuadro_servidor extends JFrame {
	
	JTextArea chat;
	
	Cuadro_servidor(){
		
		JFrame server = new JFrame(); //nombre del cuadro
		
		this.setTitle("Server");
		
		this.setSize(500,600); // tama;o del cuadro
		
		this.setVisible(true); //hace ell cuadro visible en la pantalla
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setResizable(false);
		
		this.getContentPane().setBackground(new Color (30,30,30));
		
		chat = new JTextArea();
		
		chat.setBounds(0, 0, 500, 600);
		
		chat.setFont(new Font("Comic Sans",Font.PLAIN,20));
		
		chat.setBackground(new Color(30,30,30));
		
		chat.setForeground(new Color(255,255,255));
		
		this.add(chat);
		
	}
		
}
	
