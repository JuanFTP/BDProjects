/**
 * @(#)BaseDeDatos.java
 *
 *
 * @author 
 * @version 1.00 2015/11/21
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.sql.*;
import java.util.logging.*;
public class BaseDeDatos {
	static Visualizar visualizar=null;
	static Dimension pant,vent;
	static ResultSet registro=null;
	static Statement comando=null;
	static String[] val={"-Seleccionar-","CURP","Nombre","Apellido","Sexo","Edad","Estatura","Peso"};
	public static MySQL db = new MySQL();
    static void mostrarVentana(){
		final JFrame frame=new JFrame("Base De Datos De Personas");
		frame.setLayout(null);
		final JButton boton=new JButton("Visualizar");
		final JButton boton2=new JButton("Buscar");
		final JButton boton3=new JButton("Insertar");
		final JButton boton4=new JButton("Actualizar");
		final JButton boton5=new JButton("Eliminar");
		final JButton boton6=new JButton("Salir");
		final JButton imagen=new JButton(new ImageIcon("Usuario.png"));
		boton.setBounds(20,50,100,30);
		boton2.setBounds(150,50,100,30);
		boton3.setBounds(280,50,100,30);
		boton4.setBounds(20,100,100,30);
		boton5.setBounds(150,100,100,30);
		boton6.setBounds(280,100,100,30);
		imagen.setBounds(150,150,100,100);
		frame.getContentPane().add(boton);
		frame.getContentPane().add(boton2);
		frame.getContentPane().add(boton3);
		frame.getContentPane().add(boton4);
		frame.getContentPane().add(boton5);
		frame.getContentPane().add(boton6);
		frame.getContentPane().add(imagen);
		frame.getContentPane().setBackground(Color.BLUE);
		frame.setSize(420,300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		vent=frame.getSize();
		pant=Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((pant.width-vent.width)/2,(pant.height-vent.height)/2);
		boton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(getRegistro("*",";")==null){
					JOptionPane.showMessageDialog(null,"No hay Registros");
				}else{
					frame.setVisible(false);
					visualizar = new Visualizar("*",";");
					visualizar.pack();
					visualizar.setVisible(true);
				}
			}
		});
		boton2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(getRegistro("*",";")==null){
					JOptionPane.showMessageDialog(null,"No hay Registros");
				}else{
					frame.setVisible(false);
					Buscar.mostrarVentana();
				}
			}
		});
		boton3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				frame.setVisible(false);
				Insertar.mostrarVentana();
			}
		});
		boton4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(getRegistro("*",";")==null){
					JOptionPane.showMessageDialog(null,"No hay Registros");
				}else{
					frame.setVisible(false);
					Actualizar.mostrarVentana();
				}
			}
		});
		boton5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(getRegistro("*",";")==null){
					JOptionPane.showMessageDialog(null,"No hay Registros");
				}else{
					frame.setVisible(false);
					Eliminar.mostrarVentana();
				}
			}
		});
		boton6.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				System.exit(0);
			}
		});
	}
	public static ResultSet getRegistro(String con,String con2){
		try{
			comando=db.getConnection().createStatement();
			System.out.println("SELECT "+con+" FROM Persona "+con2);
			return registro=comando.executeQuery("SELECT "+con+" FROM Persona "+con2);
		}catch(Exception ex){
			Logger.getLogger(Principal.class.getName()).log(Level.SEVERE,null,ex);
		}
		return null;
	}
	public static void main (String[]Args){
		BaseDeDatos.mostrarVentana();
	}
}