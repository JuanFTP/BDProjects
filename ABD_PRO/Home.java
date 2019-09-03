import javax.swing.UIManager.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

class Home extends JFrame implements KeyListener
{
	public Dimension pantalla, ventana;
	static enlace conector;
	static Connection fix;
	static Statement consulta;
	static ResultSet resultado;
	static String listUsuarios;
	/*Elementos del Frame*/
	static JLabel eTitulo, eImagen, eBarner, eOpciones, eLogin, ePassword;
	static JComboBox comboLogin;
	static JPasswordField cPassword;
	static int posX, posY, tamVX, tamVY;

	Home()
	{
		new JFrame(); setTitle("Bienvenido"); setLayout(null); getContentPane().setBackground(Color.WHITE);
		posX=0; posY=5; tamVX=500; tamVY=300;
		/*Imagen*/
		eOpciones = new JLabel("F2: LUsers   F3: Opciones   ESC: Salir"); eOpciones.setBounds(posX, posY, tamVX, 20); add(eOpciones); eOpciones.setForeground(new Color(137, 135, 132)); eOpciones.setHorizontalAlignment(JLabel.CENTER); posY+=25;
		eTitulo = new JLabel("Servicio de Administracion de Pagos (AGUAP)"); eTitulo.setBounds(posX, posY, tamVX, 25); add(eTitulo); eTitulo.setHorizontalAlignment(JLabel.CENTER); posY+=30; posX+=5;
		eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(0, 0, 128));
		eImagen = new JLabel(new ImageIcon("data/admin.png")); eImagen.setBounds(posX, posY, 200, 200); add(eImagen);
		posX+=230;
		/*Imagen*/
		eLogin = new JLabel("Selecciona tu Login"); eLogin.setBounds(posX, posY, 200, 25); add(eLogin);
		posY+=25;
		comboLogin = new JComboBox(); comboLogin.setBounds(posX, posY, 200, 25); add(comboLogin); comboLogin.setBackground(Color.WHITE);
		posY+=40;
		ePassword = new JLabel("Ingresa tu Password"); ePassword.setBounds(posX, posY, 200, 25); add(ePassword);
		posY+=25;
		cPassword = new JPasswordField(); cPassword.setBounds(posX, posY, 200, 25); cPassword.setBackground(Color.WHITE); add(cPassword);
		posY+=70;
		agregarLogin();/*Se vacian los usuarios en el combo*/
		comboLogin.addKeyListener(this);
		cPassword.addKeyListener(this);
		/*Funciones de Operacion*/
		setVisible(true); setResizable(false); setSize(500, 300);
		ventana = getSize();
		pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((pantalla.width-ventana.width)/2 , (pantalla.height-ventana.height)/2);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void agregarLogin()
	{
		try
		{
			comboLogin.removeAllItems();/*Se Borra todos los items*/
			listUsuarios = "Usuarios:\n";/*para consulta de usuarios*/
			conector = new enlace(); fix = conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery("select count(*) from manejador;");
			if(resultado.next())
			{
				if(resultado.getInt("count(*)")!=0)/*Si hay resultados*/
				{
					resultado = consulta.executeQuery("select login from manejador;");
					while(resultado.next())
					{comboLogin.addItem(resultado.getString("login")); listUsuarios+=resultado.getString("login")+"\n";}
				}
				else
				{comboLogin.addItem("Sin Usuarios"); listUsuarios = "No Hay Usuarios";}
			}
			else
			{JOptionPane.showMessageDialog(this, "Error en Count(*) Combo", "Error", 0);}
			fix.close(); consulta.close(); resultado.close();
		}
		catch(SQLException ex)
		{JOptionPane.showMessageDialog(this, "Error al Consultar los Login: "+ex.getMessage(), "Error", 0);}
	}

	static JFrame wOpciones;
	static JButton botonNuevoExterno, botonNuevoLocal;
	static JButton botonRestaurarExterno, botonRestaurarLocal;
	static int tamX, tamY;
	static JComboBox menuRecuperar;
	static JFileChooser explorador;
	static JLabel tipoOper;
	static String autentica = "";
	public void opciones()
	{
		wOpciones = new JFrame("Opciones del Sistema");
		wOpciones.getContentPane().setBackground(Color.WHITE);
		wOpciones.setLayout(null);
		tamVX=300; tamVY=303; posX=10; posY=15; tamX=70; tamY=25;
		eTitulo = new JLabel("Opciones de Recuperacion"); eTitulo.setBounds(posX, posY, tamVX-25, 25); wOpciones.add(eTitulo); eTitulo.setHorizontalAlignment(JLabel.CENTER);
		eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(0, 0, 128));
		posY+=35; posX=10;
		tipoOper = new JLabel("Operaciones Externas"); tipoOper.setBounds(posX, posY, tamVX, tamY-5); wOpciones.add(tipoOper);
		posY+=25;
		/*Botones de Operacion*/
		botonNuevoExterno = new JButton(new ImageIcon("data/backup.png")); botonNuevoExterno.setText("Respaldo EXTERNO"); botonNuevoExterno.setBackground(Color.WHITE); botonNuevoExterno.setBorderPainted(false); botonNuevoExterno.setBounds(posX, posY, tamVX-25, tamY); wOpciones.add(botonNuevoExterno);
		posY+=30;
		botonRestaurarExterno = new JButton(new ImageIcon("data/restore.png")); botonRestaurarExterno.setText("Restaurar [EXTERNO]"); botonRestaurarExterno.setBackground(Color.WHITE); botonRestaurarExterno.setBorderPainted(false); botonRestaurarExterno.setBounds(posX, posY, tamVX-25, tamY); wOpciones.add(botonRestaurarExterno);
		posY+=tamY+10;
		tipoOper = new JLabel("Operaciones Locales"); tipoOper.setBounds(posX, posY, tamVX, tamY-5); wOpciones.add(tipoOper);
		posY+=25;
		botonNuevoLocal = new JButton(new ImageIcon("data/backup.png")); botonNuevoLocal.setText("Respaldo LOCAL"); botonNuevoLocal.setBackground(Color.WHITE); botonNuevoLocal.setBorderPainted(false); botonNuevoLocal.setBounds(posX, posY, tamVX-25, tamY); wOpciones.add(botonNuevoLocal);
		posY+=30;
		botonRestaurarLocal = new JButton(new ImageIcon("data/restore.png")); botonRestaurarLocal.setText("Restaurar [LOCAL]"); botonRestaurarLocal.setBackground(Color.WHITE); botonRestaurarLocal.setBorderPainted(false); botonRestaurarLocal.setBounds(posX, posY, tamVX-25, tamY); wOpciones.add(botonRestaurarLocal);
		posY+=tamY+10;
		/*Menu de recuperacion*/
		menuRecuperar = new JComboBox();
    	menuRecuperar.setBounds(posX, posY, tamVX-40, 30);
    	menuRecuperar.setBackground(Color.WHITE);
    	list("data/backup/");/*Obtener ficheros*/
	    wOpciones.add(menuRecuperar);
	    /*Menu de recuperacion*/
		/*JFileChooser*/
		explorador = new JFileChooser();
		explorador.setMultiSelectionEnabled(false);
		explorador.setFileSelectionMode(JFileChooser.FILES_ONLY);
		/*JFileChooser*/
		/*Respaldo Externo*/
		botonNuevoExterno.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				autentica = JOptionPane.showInputDialog(wOpciones, "Ingresa la contraseña de administrador\npara conrtinuar.", "Autenticacion Requerida [ADMINISTRADOR]", 1);
				if(autentica != null && !autentica.isEmpty())//Si se ingreso una contraseña
				{
					try
					{
						explorador.setDialogTitle("Guardar en: Archive Restore Main : ARM");
						conector = new enlace(); fix = enlace.getConnection();
						consulta = fix.createStatement();
						resultado = consulta.executeQuery("select count(*) from manejador where permisos='administrador';");
						if(resultado.next())
						{
							if(resultado.getInt("count(*)")==0)
							{
								if(explorador.showSaveDialog(getContentPane())==JFileChooser.APPROVE_OPTION)
								{
									String directorio = explorador.getCurrentDirectory()+"\\"+explorador.getSelectedFile().getName();
									new enlace().newBackupDatabase(directorio, 1);/*Un respaldo externo sin respaldo local previo*/
								}
							}
							else
							{
								resultado = consulta.executeQuery("select password from manejador where permisos='administrador';");
								if(resultado.next())
								{
									if(autentica.equals(resultado.getString("password")))
									{
										if(explorador.showSaveDialog(getContentPane())==JFileChooser.APPROVE_OPTION)
										{
											String directorio = explorador.getCurrentDirectory()+"\\"+explorador.getSelectedFile().getName();
											new enlace().newBackupDatabase(directorio, 1);/*Un respaldo externo sin respaldo local previo*/
										}
									}
									else
									{JOptionPane.showMessageDialog(wOpciones, "Error: Password Incorrecto", "Autenticacion Erronea", 0);}
								}
								else{JOptionPane.showMessageDialog(wOpciones, "Error al obtener el password", "Error", 0);}
							}
						}
						else{JOptionPane.showMessageDialog(wOpciones, "Error al obtener el password del administrador.", "Error", 0);}
						fix.close();
						consulta.close();
						resultado.close();
					}
					catch(SQLException ex)
					{JOptionPane.showMessageDialog(wOpciones, "Error al obtener el password de administrador: "+ex.getMessage(), "Error de Autenticacion", 0);}
				}
				else
				{JOptionPane.showMessageDialog(wOpciones, "Se requerie un password", "No se proporciono un Password", 0);}
			}
		}
		);
		/*Respaldo local*/
		botonNuevoLocal.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				autentica = JOptionPane.showInputDialog(wOpciones, "Ingresa la contraseña de administrador\npara conrtinuar.", "Autenticacion Requerida [ADMINISTRADOR]", 1);
				if(autentica != null && !autentica.isEmpty())//Si se ingreso una contraseña
				{
					try
					{
						conector = new enlace(); fix = enlace.getConnection();
						consulta = fix.createStatement();
						resultado = consulta.executeQuery("select count(*) from manejador where permisos='administrador';");
						if(resultado.next())
						{
							if(resultado.getInt("count(*)")==0)
							{
								if(JOptionPane.showConfirmDialog(wOpciones, "Deseas crear un nuevo Respaldo Local?", "Continuar?", JOptionPane.YES_NO_OPTION)==0)
								{new enlace().newBackupDatabase("data/backup/", 0);}/*El 0 indica que es local*/
								list("data/backup/");
							}
							else
							{
								resultado = consulta.executeQuery("select password from manejador where permisos='administrador';");
								if(resultado.next())
								{
									if(autentica.equals(resultado.getString("password")))
									{
										if(JOptionPane.showConfirmDialog(wOpciones, "Deseas crear un nuevo Respaldo Local?", "Continuar?", JOptionPane.YES_NO_OPTION)==0)
										{new enlace().newBackupDatabase("data/backup/", 0);}/*El 0 indica que es local*/
										list("data/backup/");
									}
									else
									{JOptionPane.showMessageDialog(wOpciones, "Error: Password Incorrecto", "Autenticacion Erronea", 0);}
								}
								else{JOptionPane.showMessageDialog(wOpciones, "Error al obtener el password", "Error", 0);}
							}
						}
						else{JOptionPane.showMessageDialog(wOpciones, "Error al obtener el password del administrador.", "Error", 0);}
						fix.close();
						consulta.close();
						resultado.close();
					}
					catch(SQLException ex)
					{JOptionPane.showMessageDialog(wOpciones, "Error al obtener el password de administrador: "+ex.getMessage(), "Error de Autenticacion", 0);}
				}
				else
				{JOptionPane.showMessageDialog(wOpciones, "Se requerie un password", "No se proporciono un Password", 0);}
			}
		}
		);

	    /*Restauracion Local*/
		botonRestaurarLocal.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(menuRecuperar.getSelectedIndex()==0)
				{JOptionPane.showMessageDialog(wOpciones, "Selecciona por favor un punto de\nrestauracion.", "Selecciona un punto", 1);}
				else
				{
					String passConfirm = JOptionPane.showInputDialog(wOpciones, "Ingresa el Password [ADMINISTRADOR]\n-- Requerido -- ", "Autenticacion requerida", 1);
					//if(!passConfirm.equals(""))/*Si se igreso alguna contraseÃ±a*/
					//{
					try
					{
						conector = new enlace(); fix = conector.getConnection();
						consulta = fix.createStatement();
						resultado = consulta.executeQuery("select count(*) from manejador;");
						if(resultado.next())
						{
							if(resultado.getInt("count(*)")==0)/*no hay un admin*/
							{
								new enlace().restore("data/backup/"+String.valueOf(menuRecuperar.getSelectedItem()));
							}
							else/*Hay un admin y se tiene que corroborar*/
							{
								resultado = consulta.executeQuery("select password from manejador where permisos='administrador';");
								if(resultado.next())
								{
									if(passConfirm.equals(resultado.getString("password")))
									{
										if(JOptionPane.showConfirmDialog(wOpciones, "Deseas continuar?\n[Recuerda que el proceso es irreversible y\npuede que el password cambie de acuerdo a lo anterior\nten cuidado]", "Continuar?", JOptionPane.YES_NO_OPTION)==0)
										{new enlace().restore("data/backup/"+String.valueOf(menuRecuperar.getSelectedItem()));}
									}
									else
									{JOptionPane.showMessageDialog(wOpciones, "La contraseña no coincide, verificala.", "Error de autenticacion", 0);}
								}
							}
						}
					}
					catch(Exception ex)
					{JOptionPane.showMessageDialog(wOpciones, "Error en la confirmacion del password admin.", "Error", 0);}
					//}
					//else
					//{JOptionPane.showMessageDialog(wOpciones, "Debes ingresar el password de ADMINISTRADOR", "Autenticacion requerida", 0);}
				}
			}
		}
		);
		
		/*Restauracion Externa*/
		botonRestaurarExterno.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				explorador.setDialogTitle("Abrir Archive Restore Main : ARM");
				if(explorador.showOpenDialog(getContentPane())==JFileChooser.APPROVE_OPTION)
				{
					String passConfirm = JOptionPane.showInputDialog(wOpciones, "Ingresa el Password [ADMINISTRADOR]\n-- Requerido -- ", "Autenticacion requerida", 1);
					try
					{
						conector = new enlace(); fix = conector.getConnection();
						consulta = fix.createStatement();
						resultado = consulta.executeQuery("select count(*) from manejador where password='administrador';");
						if(resultado.next())
						{
							if(resultado.getInt("count(*)")==0)/*no hay un admin*/
							{
								String directorio = explorador.getCurrentDirectory()+"\\"+explorador.getSelectedFile().getName();
								//new enlace().restore(directorio);/*Un respaldo externo sin respaldo local previo*/
								if(directorio.substring(directorio.length()-4, directorio.length()).equalsIgnoreCase(".ARM"))
								{
									//System.out.println("Archivo de restauracion: "+directorio);
									new enlace().restore(directorio);/*Restauracion local*/
								}
								else
								{
									JOptionPane.showMessageDialog(wOpciones, "Archivo de restauracion NO VALIDO.", "Archivo no ARM : Archive Restore Main", 0);
									System.out.println("Tipo de Archivo: "+directorio.substring(directorio.length()-4, directorio.length()));
								}
							}
							else if(resultado.getInt("count(*)")>0)/*Hay un admin y se tiene que corroborar*/
							{
								resultado = consulta.executeQuery("select password from manejador where permisos='administrador';");
								if(resultado.next())
								{
									if(passConfirm.equals(resultado.getString("password")))
									{
										if(JOptionPane.showConfirmDialog(wOpciones, "Deseas continuar?\n[Recuerda que el proceso es irreversible y\npuede que el password cambie de acuerdo a lo anterior\nten cuidado]", "Continuar?", JOptionPane.YES_NO_OPTION)==0)
										{
											String directorio = explorador.getCurrentDirectory()+"\\"+explorador.getSelectedFile().getName();
											if(directorio.substring(directorio.length()-4, directorio.length()).equalsIgnoreCase(".ARM"))
											{
												new enlace().restore(directorio);/*Restauracion de entorno externo*/
												//System.out.println("Archivo de restauracion: "+directorio);
											}
											else
											{
												JOptionPane.showMessageDialog(wOpciones, "Archivo de restauracion NO VALIDO.", "Archivo no ARM : Archive Restore Main", 0);
												System.out.println("Tipo de Archivo: "+directorio.substring(directorio.length()-4, directorio.length()));
											}
										}
									}
									else
									{JOptionPane.showMessageDialog(wOpciones, "La contraseña no coincide, verificala.", "Error de autenticacion", 0);}
								}
							}
						}
						else
						{JOptionPane.showMessageDialog(wOpciones, "No hay administrador", "Error", 0);}
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(wOpciones, "Error en la confirmacion del password admin [ADD].\n"+ex.getMessage(), "Error", 0);
						ex.printStackTrace();
					}
				}
			}
		}
		);
		/*Restauracion Externo*/
		/*Botones de Operacion*/
		wOpciones.setLocation(((pantalla.width-ventana.width)/2)+491, (pantalla.height-ventana.height)/2);
		wOpciones.setVisible(true);
		wOpciones.setSize(tamVX, tamVY);
		wOpciones.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}	

	/*obtiene la lista de ficheros existentes*/
	public void list(String location)
	{
		menuRecuperar.removeAllItems();
		menuRecuperar.addItem("Selecciona un punto de restauracion");
		File beta = new File(location);
        String [] a = beta.list();
        for(int k=0; k<a.length; k++)
        {menuRecuperar.addItem(a[k]);}
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		int tipo = keyEvent.getExtendedKeyCode();/*Numero de tecla pulsada*/
		if(tipo==113)//F2 Lista de usuarios
		{JOptionPane.showMessageDialog(this, listUsuarios, "Lista de Usuarios", 1);}
		else if(tipo==114)//F3 Opciones
		{opciones();}
		else if(tipo==27)//F4 Salir
		{if(JOptionPane.showConfirmDialog(this, "Deseas salir?", "Continuar", JOptionPane.YES_NO_OPTION)==0){System.exit(0);}}
		else if(tipo==10)//Enter Validar para entrar
		{
			/*Validar para acceder*/
			if(String.valueOf(comboLogin.getSelectedItem()).equals("Sin Usuarios"))/*si no hay usuarios agregados*/
			{
				JOptionPane.showMessageDialog(this, "ACCESO UNICO para agregar al ADMINISTRADOR\nTEN CUIDADO", "Atencion", 2);
				setVisible(false);
				new administrador("INDEX", "administrador");
			}
			else
			{
				if(!cPassword.getText().isEmpty())//Si no esta vacio el password
				{
					try
					{
						conector = new enlace(); fix = conector.getConnection();
						consulta = fix.createStatement();
						resultado = consulta.executeQuery("select password, permisos from manejador where login='"+String.valueOf(comboLogin.getSelectedItem())+"';");
						if(resultado.next())
						{
							if(cPassword.getText().equals(resultado.getString("password")))//Considen los password
							{
								if(resultado.getString("permisos").equals("administrador"))/*Es un admin*/
								{
									JOptionPane.showMessageDialog(this, "Bienvenido (ADMIN): "+String.valueOf(comboLogin.getSelectedItem()), "Bienvenido", 1);
									setVisible(false);
									new administrador(String.valueOf(comboLogin.getSelectedItem()), resultado.getString("permisos"));
								}
								else//No es un admin
								{
									JOptionPane.showMessageDialog(this, "Bienvenido (USER): "+String.valueOf(comboLogin.getSelectedItem()), "Bienvenido", 1);
									setVisible(false);
									new usuario(String.valueOf(comboLogin.getSelectedItem()), resultado.getString("permisos"));
								}
							}
							else
							{JOptionPane.showMessageDialog(this, "Contrase_a erronea, por favor\nverificala", "Password Incorrecto", 0);}
						}
						else
						{JOptionPane.showMessageDialog(this, "Error al obtener password y permisos", "Error", 0);}
						fix.close(); consulta.close(); resultado.close();
					}
					catch(SQLException ex)
					{JOptionPane.showMessageDialog(this, "Error al entrar: "+ex.getMessage(), "SQLException INTRO", 1);}
				}
				else
				{JOptionPane.showMessageDialog(this, "Ingresa el Password : Contrase_a", "Contrase_a requerida", 2);}
			}
		}
	}
	public void keyReleased(KeyEvent keyEvent){}
	public void keyTyped(KeyEvent keyEvent){}

	/*Para pintar y colorear el frame*/
	public void paint(Graphics g)
	{
		super.paint(g);
		/*Pintar Lineas de Colores*/
		g.setColor(new Color(79, 185, 223));
		for(int c=0; c<=100; c+=3)
		{
			g.drawLine(0, c, c, 0);
		}
		/*Squares*/
		g.drawRect(210, 210, 50, 50);
		g.drawRect(235, 235, 50, 50);
		g.drawRect(285, 210, 100, 50);
		g.drawRect(335, 235, 100, 50);
		g.drawRect(410, 210, 50, 50);
		g.drawString("Hello", 225, 225);
		g.drawString("Bienvenido", 355, 275);
	}

	public static void main(String...Args)
	{new Home();}
}