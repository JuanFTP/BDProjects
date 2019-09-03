import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

class adminUsuarios extends JFrame implements KeyListener
{
	public Dimension pantalla, ventana;
	/*Checar conector de enlace*/
	public static enlace conector;
	public static Connection fix;
	public static Statement consulta=null;
	public static ResultSet resultado=null;
	/*Estilos Generales*/
	JLabel color;
	/*Para resultados*/
	static String query, bTxT;
	static String salida;
	/*Campos para buscar*/
	static JTextField campoBuscar;
	/*Objeto de validacion*/
	validar objValidar = new validar();
	static int botonBuscarHabilitado=0;

	/*Para tabla*/
	static String [] columnas = {"Login", "Password", "Nombre", "Apellidos", "Direccion", "Permisos", "Fecha"};
	static Object [][] lineaDefault = {{"---", "---", "---", "---", "---", "---", "---"}};
	static JScrollPane barra;
	static DefaultTableModel tabla;
	static JTable tablaC;
	/*Para Tabla*/
	static JLabel tituloMenu;
	/*GLobal indicadores*/
	public static String userUpper;
	public static String permisosUpper;

	public adminUsuarios(String userUpper, String permisosUpper)
	{
		new JFrame();
		setTitle("Administrar Usuarios");
		setLayout(null);
		this.userUpper=userUpper;
		this.permisosUpper=permisosUpper;
		getContentPane().setBackground(Color.WHITE);
		/*Estilo General*/
		color=new JLabel();
		color.setBounds(0, 0, 800, 10);
		add(color);
		/*Estilo General*/
		/*Titulo*/
		tituloMenu=new JLabel("Administrar Usuarios        Usuario: "+userUpper);
		tituloMenu.setFont(new Font("Roboto", Font.PLAIN, 20));
		tituloMenu.setBounds(10, 8, 600, 30);
		add(tituloMenu);
		/*Titulo*/

		/*Barra de lujo*/
		JLabel barraLujo=new JLabel(new ImageIcon("data/mUS/barner.png"));
		barraLujo.setBounds(0, 70, 1000, 20);
		barraLujo.setBackground(Color.WHITE);
		add(barraLujo);

		/*Seccion de operaciones*/
		JLabel etiquetaBuscar=new JLabel(new ImageIcon("data/oper/orden.gif"));
		etiquetaBuscar.setText("Buscar:");
		etiquetaBuscar.setBounds(10, 40, 60, 20);
		etiquetaBuscar.setBackground(Color.WHITE);
		add(etiquetaBuscar);

		campoBuscar = new JTextField("");
		campoBuscar.setBackground(Color.WHITE);
		campoBuscar.setBounds(80, 40, 120, 20);
		campoBuscar.setHorizontalAlignment(JTextField.LEFT);
		campoBuscar.addKeyListener(this);//Escucha la busqueda
		add(campoBuscar);

		JLabel etiAgregar=new JLabel(new ImageIcon("data/oper/nuevo.gif"));
		etiAgregar.setText("Agregar (F2)");
		etiAgregar.setBounds(210, 40, 90, 20);
		etiAgregar.setBackground(Color.WHITE);
		add(etiAgregar);

		JLabel etiModificar=new JLabel(new ImageIcon("data/oper/editar.gif"));
		etiModificar.setText("Editar (F3)");
		etiModificar.setBounds(330, 40, 80, 20);
		etiModificar.setBackground(Color.WHITE);
		add(etiModificar);

		JLabel etiBorrar=new JLabel(new ImageIcon("data/oper/borrar.gif"));
		etiBorrar.setText("Borrar (F4)");
		etiBorrar.setBounds(450, 40, 80, 20);
		etiBorrar.setBackground(Color.WHITE);
		add(etiBorrar);
		
		JLabel etiSalir=new JLabel(new ImageIcon("data/oper/salir.gif"));
		etiSalir.setText("Salir (ESC)");
		etiSalir.setHorizontalAlignment(JLabel.CENTER);
		etiSalir.setBounds(880, 40, 100, 20);
		add(etiSalir);

		/*Tabla de datos*/
		tabla = new DefaultTableModel(lineaDefault, columnas);
		tablaC = new JTable(tabla);
		barra = new JScrollPane(tablaC);
		barra.setBounds(10, 100, 975, 460);
		add(barra);
		/*Propiedad de Mover columnas, todo se aplica sobre el JTable*/
		tablaC.getTableHeader().setReorderingAllowed(false);/*Para que no se muevan las columnas*/
		/*Tamaño para las columnas*/
		tablaC.getColumnModel().getColumn(0).setPreferredWidth(70);
		tablaC.getColumnModel().getColumn(1).setPreferredWidth(70);
		tablaC.getColumnModel().getColumn(2).setPreferredWidth(150);
		tablaC.getColumnModel().getColumn(3).setPreferredWidth(250);
		tablaC.getColumnModel().getColumn(4).setPreferredWidth(250);
		tablaC.getColumnModel().getColumn(5).setPreferredWidth(85);
		tablaC.getColumnModel().getColumn(6).setPreferredWidth(100);
		/*Tamaño para las columnas*/
		/*Tabla de datos*/

		mostrarResultados("select * from manejador;");

		/*Caracteristicas de la pantalla*/
		setVisible(true);
		setResizable(false);
	    setSize(1000, 600);
	    ventana=getSize();
	    pantalla=Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
	    //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void mostrarResultados(String con)
	{
		int count = tabla.getRowCount() - 1;
		while(count>=0)
		{
			tabla.removeRow(count);
			count--;
		}

		query = con;//consulta
		try
		{
			conector = new enlace(); fix = conector.getConnection();
			consulta= fix.createStatement();
			resultado = consulta.executeQuery(query);
			while(resultado.next())
			{
				Object [] newRow = {resultado.getString("login"), resultado.getString("password"), resultado.getString("nombreM"), resultado.getString("apellidos"), resultado.getString("direccion"), resultado.getString("permisos"), resultado.getString("fecha")};
				tabla.addRow(newRow);
			}
			/*Cerrar conector*/
			fix.close(); consulta.close(); resultado.close();
		}
		catch(Exception ex)
		{System.out.println("Error en la consulta de adminUsuarios");}
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		int tipo = keyEvent.getExtendedKeyCode();
		if(tipo==113)//F2 agregar usuario
		{agregar(userUpper);}
		else if(tipo==114)//F3 Modificar Usuario
		{editar(userUpper);}
		else if(tipo==115)//F4 Borrar
		{borrar(userUpper);}
		else if(tipo==27)//esc salir
		{
			setVisible(false);
			try
			{
				if(permisosUpper.equals("administrador"))//Administrador
				{administrador admin = new administrador(userUpper, permisosUpper);}
				else
				{usuario user = new usuario(userUpper, permisosUpper);}
			}
			catch(Exception ex)
			{JOptionPane.showMessageDialog(this, "Error al retornar a ADMIN.", "ADMIN ERROR", 0);}
		}
		else//Buscar cualquier cosa
		{
			bTxT=campoBuscar.getText();//extraccion del texto
			query="select * from manejador where login like'%"+bTxT+"%' or password like'%"+bTxT+"%' or nombreM like'%"+bTxT+"%' or apellidos like'%"+bTxT+"%' or direccion like'%"+bTxT+"%' or permisos like'%"+bTxT+"%' or fecha like'%"+bTxT+"%';";
			mostrarResultados(query);
		}
		//System.out.println("keyPressed: tipo: "+tipo);
	}

    public void keyReleased(KeyEvent keyEvent)
    {
    	mostrarResultados(query);
    	//System.out.println("keyReleased");
    }

    public void keyTyped(KeyEvent keyEvent)
    {
    	mostrarResultados(query);
    	//System.out.println("keyTyped");
    }

    public void agregar(String userAgrega)//F2
    {
    	JFrame ventanaAgregar = new JFrame("Nuevo Usuario");
    	ventanaAgregar.setLayout(null);
    	ventanaAgregar.getContentPane().setBackground(Color.WHITE);

    	JLabel titulo = new JLabel("Nuevo Usuario");
    	titulo.setBounds(0, 5, 300, 25);
    	titulo.setHorizontalAlignment(JLabel.CENTER);
    	ventanaAgregar.getContentPane().add(titulo);

    	JLabel eLogin = new JLabel("Login:");
    	eLogin.setBounds(10, 30, 60, 20);
    	eLogin.setHorizontalAlignment(JLabel.CENTER);
		ventanaAgregar.getContentPane().add(eLogin);
		JTextField login = new JTextField();
		login.setBounds(10, 50, 60, 20);	
		ventanaAgregar.getContentPane().add(login);

		JLabel ePassword = new JLabel("Password");
		ePassword.setBounds(80, 30, 200, 20);
		ePassword.setHorizontalAlignment(JLabel.CENTER);
		ventanaAgregar.getContentPane().add(ePassword);
		JTextField password = new JTextField();
		password.setBounds(90, 50, 90, 20);
		password.setHorizontalAlignment(JTextField.CENTER);
		ventanaAgregar.getContentPane().add(password);
		JPasswordField passwordConfirm = new JPasswordField();
		passwordConfirm.setBounds(190, 50, 90, 20);
		passwordConfirm.setHorizontalAlignment(JPasswordField.CENTER);
		ventanaAgregar.getContentPane().add(passwordConfirm);

		JLabel eNombre = new JLabel("Nombre:");
		eNombre.setBounds(10, 90, 60, 20);
		eNombre.setHorizontalAlignment(JLabel.CENTER);
		ventanaAgregar.getContentPane().add(eNombre);
		JTextField nombre = new JTextField();
		nombre.setBounds(80, 90, 200, 20);
		ventanaAgregar.getContentPane().add(nombre);

		JLabel eApellidos = new JLabel("Apellidos:");
		eApellidos.setBounds(10, 120, 60, 20);
		eApellidos.setHorizontalAlignment(JLabel.CENTER);
		ventanaAgregar.getContentPane().add(eApellidos);
		JTextField apellidos = new JTextField();
		apellidos.setBounds(80, 120, 200, 20);
		ventanaAgregar.getContentPane().add(apellidos);

		JLabel eDireccion = new JLabel("Direccion:");
		eDireccion.setBounds(10, 150, 60, 20);
		eDireccion.setHorizontalAlignment(JLabel.CENTER);
		ventanaAgregar.getContentPane().add(eDireccion);
		JTextField direccion = new JTextField();
		direccion.setBackground(new Color(224, 224, 224));
		direccion.setBounds(80, 150, 200, 40);
		ventanaAgregar.getContentPane().add(direccion);

		JLabel ePermisos = new JLabel("Permisos:");
		ePermisos.setBounds(10, 200, 280, 20);
		ePermisos.setHorizontalAlignment(JLabel.LEFT);
		ventanaAgregar.getContentPane().add(ePermisos);
		
		JCheckBox r = new JCheckBox();
		r.setBounds(10, 220, 20, 20);
		r.setSelected(true);//permiso por default lectura
		r.setBorderPaintedFlat(true);
		r.setEnabled(false);
		ventanaAgregar.getContentPane().add(r);
		JLabel eR = new JLabel("Leer");
		eR.setBounds(30, 220, 30, 20);
		eR.setHorizontalAlignment(JLabel.LEFT);
		ventanaAgregar.getContentPane().add(eR);

		JCheckBox w = new JCheckBox();
		w.setBounds(60, 220, 20, 20);
		ventanaAgregar.getContentPane().add(w);
		JLabel eW = new JLabel("Escribir");
		eW.setBounds(80, 220, 50, 20);
		eW.setHorizontalAlignment(JLabel.LEFT);
		ventanaAgregar.getContentPane().add(eW);

		JCheckBox u = new JCheckBox();
		u.setBounds(130, 220, 20, 20);
		ventanaAgregar.getContentPane().add(u);
		JLabel eU = new JLabel("Modificar");
		eU.setBounds(150, 220, 60, 20);
		eU.setHorizontalAlignment(JLabel.LEFT);
		ventanaAgregar.getContentPane().add(eU);

		JCheckBox d = new JCheckBox();
		d.setBounds(210, 220, 20, 20);
		ventanaAgregar.getContentPane().add(d);
		JLabel eD = new JLabel("Eliminar");
		eD.setBounds(230, 220, 90, 20);
		eD.setHorizontalAlignment(JLabel.LEFT);
		ventanaAgregar.getContentPane().add(eD);

		JButton botonAceptar = new JButton(new ImageIcon("data/oper/save.png"));
		botonAceptar.setText("Guardar");
		botonAceptar.setBounds(5, 260, 285, 25);
		botonAceptar.setBackground(Color.WHITE);
		ventanaAgregar.getContentPane().add(botonAceptar);

		if(userAgrega.equals("INDEX"))/*Para crear el administrador*/
		{
			w.setSelected(true); w.setEnabled(false);
			u.setSelected(true); u.setEnabled(false);
			d.setSelected(true); d.setEnabled(false);
		}
		else /*Para crear mas usuarios*/
		{
			w.setSelected(true); w.setEnabled(false);
			u.setSelected(false); u.setEnabled(false);
			d.setSelected(false); d.setEnabled(false);
		}

		botonAceptar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(objValidar.esLogin(ventanaAgregar, login.getText()))
				{
					if(objValidar.verificaPassword(ventanaAgregar, password.getText(), passwordConfirm.getText()))
					{
						if(objValidar.esDuplicado(ventanaAgregar, nombre.getText(), apellidos.getText(), direccion.getText()))//checa si no hay un registro similar
						{
							String perCheck="1";//permiso de lectura por default
							/*Permisos*/
							
							if(w.isSelected())//escritura
							{perCheck+="1";}	
							else
							{perCheck+="0";}
							
							if(u.isSelected())//actualizacion
							{perCheck+="1";}
							else
							{perCheck+="0";}

							if(d.isSelected())//Eliminacion
							{perCheck+="1";}
							else
							{perCheck+="0";}
							if(perCheck.equals("1111"))
							{
								perCheck="administrador";
							}
							else
							{
								perCheck="operador";
							}
							/*Agregar usuario valido*/
							query="insert into manejador values ('"+login.getText()+"', '"+password.getText()+"', '"+nombre.getText()+"', '"+apellidos.getText()+"', '"+direccion.getText()+"', '"+perCheck+"', curdate());";
							if(JOptionPane.showConfirmDialog(ventanaAgregar, "Deseas agregar al usuario: "+login.getText()+"?", "Mensaje", JOptionPane.YES_NO_OPTION)==0)//Agregar?
							{
								String newUsuario = login.getText();
								agregarUsuario(ventanaAgregar);
								if(userAgrega.equals("INDEX"))
								{
									/*Actualizacion del usuario*/
									userUpper = newUsuario;
									tituloMenu.setText("Administrar Usuarios        Usuario: "+userUpper);
									repaint();
									ventanaAgregar.setVisible(false);
								}
								mostrarResultados("select * from manejador;");
								//Reincio de los campos
								login.setText(""); password.setText(""); passwordConfirm.setText(""); nombre.setText(""); apellidos.setText(""); direccion.setText(""); w.setSelected(true); u.setSelected(false); d.setSelected(false);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(ventanaAgregar, "Datos no validos en Nombre o Apellido\nsolo se deben poner texto.", "Datos no Validos", 0);
						}
					}
				}
			}
		}
		);		

    	/*Caracteristicas de la pantalla*/
		ventanaAgregar.setVisible(true);
		ventanaAgregar.setResizable(false);
	    ventanaAgregar.setSize(300, 320);
	    //Posicion
	    ventana=getSize();
	    pantalla=Toolkit.getDefaultToolkit().getScreenSize();
	    ventanaAgregar.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
	    ventanaAgregar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void editar(String userUpper)//F3
    {
    	JFrame ventanaEditar = new JFrame("Modificar Usuario");
    	ventanaEditar.setLayout(null);
    	ventanaEditar.getContentPane().setBackground(Color.WHITE);

    	JLabel titulo = new JLabel("----------------- Editar Usuario -----------------");
    	titulo.setBounds(0, 5, 300, 25);
    	titulo.setHorizontalAlignment(JLabel.CENTER);
    	ventanaEditar.getContentPane().add(titulo);

    	JLabel eLogin = new JLabel("Login:");
    	eLogin.setBounds(10, 30, 60, 20);
    	eLogin.setHorizontalAlignment(JLabel.CENTER);
		ventanaEditar.getContentPane().add(eLogin);
		JTextField login = new JTextField();
		login.setBounds(10, 50, 60, 20);
		ventanaEditar.getContentPane().add(login);

		JLabel ePassword = new JLabel("Password");
		ePassword.setBounds(80, 30, 200, 20);
		ePassword.setHorizontalAlignment(JLabel.CENTER);
		ventanaEditar.getContentPane().add(ePassword);
		JTextField password = new JTextField();
		password.setBounds(90, 50, 90, 20);
		password.setHorizontalAlignment(JTextField.CENTER);
		password.setEditable(false);
		ventanaEditar.getContentPane().add(password);
		JPasswordField passwordConfirm = new JPasswordField();
		passwordConfirm.setBounds(190, 50, 90, 20);
		passwordConfirm.setHorizontalAlignment(JPasswordField.CENTER);
		passwordConfirm.setEditable(false);
		ventanaEditar.getContentPane().add(passwordConfirm);

		JLabel eNombre = new JLabel("Nombre:");
		eNombre.setBounds(10, 90, 60, 20);
		eNombre.setHorizontalAlignment(JLabel.CENTER);
		ventanaEditar.getContentPane().add(eNombre);
		JTextField nombre = new JTextField();
		nombre.setEditable(false);
		nombre.setBounds(80, 90, 200, 20);
		ventanaEditar.getContentPane().add(nombre);

		JLabel eApellidos = new JLabel("Apellidos:");
		eApellidos.setBounds(10, 120, 60, 20);
		eApellidos.setHorizontalAlignment(JLabel.CENTER);
		ventanaEditar.getContentPane().add(eApellidos);
		JTextField apellidos = new JTextField();
		apellidos.setEditable(false);
		apellidos.setBounds(80, 120, 200, 20);
		ventanaEditar.getContentPane().add(apellidos);

		JLabel eDireccion = new JLabel("Direccion:");
		eDireccion.setBounds(10, 150, 60, 20);
		eDireccion.setHorizontalAlignment(JLabel.CENTER);
		ventanaEditar.getContentPane().add(eDireccion);
		JTextField direccion = new JTextField();
		direccion.setBackground(new Color(224, 224, 224));
		direccion.setEditable(false);
		direccion.setBounds(80, 150, 200, 40);
		ventanaEditar.getContentPane().add(direccion);

		JLabel ePermisos = new JLabel("Permisos:");
		ePermisos.setBounds(10, 200, 280, 20);
		ePermisos.setHorizontalAlignment(JLabel.LEFT);
		ventanaEditar.getContentPane().add(ePermisos);
		
		JCheckBox r = new JCheckBox();
		r.setBounds(10, 220, 20, 20);
		r.setSelected(true);//permiso por default lectura
		r.setEnabled(false);
		ventanaEditar.getContentPane().add(r);
		JLabel eR = new JLabel("Leer");
		eR.setBounds(30, 220, 30, 20);
		eR.setHorizontalAlignment(JLabel.LEFT);
		ventanaEditar.getContentPane().add(eR);

		JCheckBox w = new JCheckBox();
		w.setBounds(60, 220, 20, 20);
		w.setEnabled(false);
		ventanaEditar.getContentPane().add(w);
		JLabel eW = new JLabel("Escribir");
		eW.setBounds(80, 220, 50, 20);
		eW.setHorizontalAlignment(JLabel.LEFT);
		ventanaEditar.getContentPane().add(eW);

		JCheckBox u = new JCheckBox();
		u.setBounds(130, 220, 20, 20);
		u.setEnabled(false);
		ventanaEditar.getContentPane().add(u);
		JLabel eU = new JLabel("Modificar");
		eU.setBounds(150, 220, 60, 20);
		eU.setHorizontalAlignment(JLabel.LEFT);
		ventanaEditar.getContentPane().add(eU);

		JCheckBox d = new JCheckBox();
		d.setBounds(210, 220, 20, 20);
		d.setEnabled(false);
		ventanaEditar.getContentPane().add(d);
		JLabel eD = new JLabel("Eliminar");
		eD.setBounds(230, 220, 90, 20);
		eD.setHorizontalAlignment(JLabel.LEFT);
		ventanaEditar.getContentPane().add(eD);

		JButton botonGuardar = new JButton(new ImageIcon("data/oper/save.png"));
		botonGuardar.setText("Buscar");
		botonGuardar.setBounds(5, 260, 285, 25);
		botonGuardar.setBackground(Color.WHITE);
		ventanaEditar.getContentPane().add(botonGuardar);

		botonGuardar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(objValidar.esLoginMOD(ventanaEditar, login.getText()) && botonBuscarHabilitado==0)//El login existe y se extraen los datos
				{
					botonBuscarHabilitado=1;//pasa para guardar
					botonGuardar.setText("Guardar");
					try
			    	{
			    		conector = new enlace();
    					Connection fix = conector.getConnection();	
			    		consulta=fix.createStatement();
			    		resultado = consulta.executeQuery("select * from manejador where login='"+login.getText()+"';");
			    		if(resultado.next())
			    		{
			    			login.setEditable(false);
			    			direccion.setBackground(Color.WHITE);
			    			password.setEditable(true);
			    			password.setText(resultado.getString("password"));
			    			passwordConfirm.setEditable(true);
			    			passwordConfirm.setText(resultado.getString("password"));
			    			nombre.setText(resultado.getString("nombreM"));
			    			apellidos.setText(resultado.getString("apellidos"));
			    			direccion.setEditable(true);
			    			direccion.setText(resultado.getString("direccion"));
			    			String level = resultado.getString("permisos");
			    			if(level.equals("administrador"))
			    			{
			    				w.setSelected(true);
			    				u.setSelected(true);
			    				d.setSelected(true);
			    			}
			    			else if(level.equals("operador"))
			    			{
			    				w.setSelected(true);
			    			}  			
							fix.close(); consulta.close(); resultado.close();
			    		}
					}
					catch(SQLException ex)
					{System.out.println("Error en la extraccion de los datos de usuario.");}
				}
				else if(objValidar.esLoginMOD(ventanaEditar, login.getText()) && botonBuscarHabilitado==1)
				{
					if(objValidar.verificaPassword(ventanaEditar, password.getText(), passwordConfirm.getText()))
					{
						String prmss="1";
						if(w.isSelected()){prmss+="1";} else {prmss+="0";}
						if(u.isSelected()){prmss+="1";} else {prmss+="0";}
						if(d.isSelected()){prmss+="1";} else {prmss+="0";}
						if(prmss.equals("1111"))
						{
							prmss="administrador";
						}
						else
						{
							prmss="operador";
						}
						query = "update manejador set password='"+password.getText()+"', direccion='"+direccion.getText()+"', permisos='"+prmss+"', fecha=curdate() where login='"+login.getText()+"';";
						if(JOptionPane.showConfirmDialog(ventanaEditar, "Deseas continuar?", "Mensaje", JOptionPane.YES_NO_OPTION)==0)
						{
							modificarUsuario(ventanaEditar);
							mostrarResultados("select * from manejador;");
							botonBuscarHabilitado=0;						
							login.setText(""); password.setText(""); passwordConfirm.setText(""); nombre.setText(""); apellidos.setText(""); direccion.setText(""); w.setSelected(true); u.setSelected(false); d.setSelected(false);
							botonGuardar.setText("Buscar");
							password.setEditable(false); passwordConfirm.setEditable(false); nombre.setEditable(false); apellidos.setEditable(false); direccion.setEditable(false);
							w.setEnabled(true); u.setEnabled(false); d.setEnabled(false);
							login.setEditable(true);
							direccion.setBackground(new Color(224, 224, 224));
						}							
					}
				}
			}
		}
		);		

    	/*Caracteristicas de la pantalla*/
		ventanaEditar.setVisible(true);
		ventanaEditar.setResizable(false);
	    ventanaEditar.setSize(300, 320);
	    //Posicion
	    ventana=getSize();
	    pantalla=Toolkit.getDefaultToolkit().getScreenSize();
	    ventanaEditar.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
	    ventanaEditar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void borrar(String userUpper)//F4
    {
    	JFrame ventanaBorrar = new JFrame("Borrar Usuario");
    	ventanaBorrar.setLayout(null);
    	ventanaBorrar.getContentPane().setBackground(Color.WHITE);

    	JLabel titulo = new JLabel("----------------- Eliminar Usuario -----------------");
    	titulo.setBounds(0, 5, 400, 25);
    	titulo.setHorizontalAlignment(JLabel.CENTER);
    	ventanaBorrar.getContentPane().add(titulo);
    	
    	JLabel instruccion = new JLabel("Ingresa el Login del Usuario a Eliminar:");
    	instruccion.setBounds(10, 35, 380, 20);
    	ventanaBorrar.getContentPane().add(instruccion);
    	JTextField loginB = new JTextField();
    	loginB.setBounds(10, 60, 380, 30);
    	ventanaBorrar.getContentPane().add(loginB);

    	JButton botonBorrar = new JButton(new ImageIcon("data/oper/borrar.gif"));
		botonBorrar.setText("Eliminar");
		botonBorrar.setBounds(10, 100, 380, 16);
		botonBorrar.setBackground(Color.WHITE);
		botonBorrar.setBorderPainted(false);
		ventanaBorrar.getContentPane().add(botonBorrar);

		/*Boton borrar*/
		botonBorrar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(loginB.getText().isEmpty())
				{JOptionPane.showMessageDialog(ventanaBorrar, "Por favor introduce el login del usuario\na eliminar.", "Mensaje", 2);}
				else
				{
					if(objValidar.esLoginMOD(ventanaBorrar, loginB.getText()))//busca que el login exista.
					{
						if(JOptionPane.showConfirmDialog(ventanaBorrar, "Deseas eliminar la cuenta con Login: "+loginB.getText()+"?", "Advertencia", JOptionPane.YES_NO_OPTION)==0)
						{
							query="delete from manejador where login='"+loginB.getText()+"';";
							eliminarUsuario(ventanaBorrar, loginB.getText());
							mostrarResultados("select * from manejador;");
							loginB.setText("");
						}
					}
				}
			}
		}
		);	

    	/*Caracteristicas de la pantalla*/
		ventanaBorrar.setVisible(true);
		ventanaBorrar.setResizable(false);
	    ventanaBorrar.setSize(400, 160);
	    //Posicion
	    ventana=getSize();
	    pantalla=Toolkit.getDefaultToolkit().getScreenSize();
	    ventanaBorrar.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
	    ventanaBorrar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    //Ejecuta la operacion de agregado
    public void agregarUsuario(JFrame w)//Agregar un usuario
    {
    	try
    	{
    		conector = new enlace();
    		fix = conector.getConnection();
    		consulta=fix.createStatement();
	    		consulta.executeUpdate(query);
	    		JOptionPane.showMessageDialog(w, "El registro se agrego correctamente.", "Mensaje", 1);
    		fix.close(); consulta.close();
    	}
    	catch(Exception ex)
    	{System.out.println("Resgistro Fallo");}
    }

    //Ejecuta la actualizacion
    public void modificarUsuario(JFrame w)
    {
    	
    	try
    	{
    		conector = new enlace();
    		fix = conector.getConnection();
    		consulta = fix.createStatement();
	    		consulta.executeUpdate(query);
	    		JOptionPane.showMessageDialog(w, "Actualizacion completada correctamente.", "Mensaje", 1);
    		fix.close(); consulta.close();
    	}
    	catch(Exception ex)
    	{System.out.println("Resgistro Fallo");}
    }

    //Ejecuta la operacion
    public void eliminarUsuario(JFrame w, String userD)
    {
    	try
    	{
    		conector = new enlace();
    		fix = conector.getConnection();
    		consulta=fix.createStatement();
	    		consulta.executeUpdate(query);
	    		JOptionPane.showMessageDialog(w, "Registro eliminado correctamente.", "Mensaje", 1);
	    	fix.close(); consulta.close();
    	}
    	catch(Exception ex)
    	{JOptionPane.showMessageDialog(w, "No se puede eliminar al usuario: "+userD+"\nya que esta el usuario esta activo.", "Mensaje", 1);}
    }
}