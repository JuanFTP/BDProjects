import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

class adminClientes extends JFrame implements KeyListener
{
	/*Manejo del sistema*/
	public Dimension pantalla, ventana;
	static int tamVX, tamVY, posX, posY, tamX, tamY;
	/*Variables de conexion*/
	enlace conector;
	Connection fix;
	Statement consulta;
	ResultSet resultado;
	String query;
	validarCliente objValidar = new validarCliente(); //Objeto de validacion
	/*Campos globales*/
	static JLabel titulo, linea;/*Para fines de estilo*/
	static JLabel eId, eNombre, eApellidos, eDireccion, eTelefono, eLogin, eFecha; /*Etiquetas para salidas de datos*/
	static JTextField iId, iNombre, iApellidos, iDireccion, iTelefono; /*Campos de entrada de datos*/
	static JLabel buscar, agregar, editar, eliminar, cerrar; /*Etiquetas para instrucciones*/
	static JTextField cBuscar; /*Campo para buscar*/
	/*Botones*/
	JButton aceptar, volver;
	/*Modo modificar*/
	static int estadoInicial=0; //Hay que buscar

	/*Para tabla*/
	static String [] columnas = {"ID", "Nombre", "Apellidos", "Direccion", "Telefono", "Login", "Fecha"};
	static Object [][] lineaDefault = {{"---", "---", "---", "---", "---", "---", "---"}};
	static JScrollPane barra;
	static DefaultTableModel tabla;
	static JTable tablaC;
	/*Para Tabla*/
	/*Permisos*/
	public static String userUpper;
	public static String permisosUpper;

	adminClientes(String userUpper, String permisosUpper)
	{
		/*Usuario del sistema en curso*/
		this.userUpper=userUpper;
		this.permisosUpper=permisosUpper;
		new JFrame(); setTitle("Administrar Clientes"); setLayout(null); getContentPane().setBackground(Color.WHITE);
		tamVX=1000; tamVY=700; /*Ajustar Tamaño de la ventana*/
		posX=10; posY=5; /*Ajustar posicion de los elementos*/
		tamX=90; tamY=20; /*Tamaño de alto de las etiquetas y campos*/
		titulo = new JLabel("Administrar Clientes   Usuario: "+userUpper); titulo.setBounds(0, posY, tamVX, 30); add(titulo); titulo.setHorizontalAlignment(JLabel.CENTER);
		posY=40;
		/*Seccion de operaciones*/
		buscar = new JLabel(new ImageIcon("data/oper/orden.gif")); buscar.setText("Buscar: "); buscar.setBounds(posX, posY, tamX, tamY); buscar.setHorizontalAlignment(JLabel.CENTER); add(buscar);
		posX+=tamX;
		cBuscar = new JTextField(); cBuscar.setBounds(posX, posY, tamX+20, tamY); add(cBuscar); cBuscar.addKeyListener(this);
		posX+=tamX+40;/*CampoBuscar*/
		agregar = new JLabel(new ImageIcon("data/oper/nuevo.gif")); agregar.setText("Agregar (F2)"); agregar.setBounds(posX, posY, tamX, tamY); agregar.setHorizontalAlignment(JLabel.CENTER); add(agregar);
		posX+=tamX+20;
		/*Operaciones para adminstrador*/
		if(permisosUpper.equals("administrador")){editar = new JLabel(new ImageIcon("data/oper/editar.gif")); editar.setText("Editar (F3)"); editar.setBounds(posX, posY, tamX, tamY); editar.setHorizontalAlignment(JLabel.CENTER); add(editar);}
		posX+=tamX+20;
		if(permisosUpper.equals("administrador")){eliminar = new JLabel(new ImageIcon("data/oper/borrar.gif")); eliminar.setText("Eliminar (F4)"); eliminar.setBounds(posX, posY, tamX, tamY); eliminar.setHorizontalAlignment(JLabel.CENTER); add(eliminar);}
		posX+=tamX+20;
		cerrar = new JLabel(new ImageIcon("data/oper/salir.gif")); cerrar.setText("Salir (esc)"); cerrar.setBounds(tamVX-(tamX+20), posY, tamX, tamY); cerrar.setHorizontalAlignment(JLabel.RIGHT); add(cerrar);
		/*Desplazamiento de la linea de salida*/
		posY+=tamY+20; posX=10; tamX=tamVX-25; tamY=posY+30; tamY=tamVY-(posY+30);
		/*Tabla de Resultados*/
		tabla = new DefaultTableModel(lineaDefault, columnas);
		tablaC = new JTable(tabla);
		barra = new JScrollPane(tablaC);
		barra.setBounds(posX, posY, tamX, tamY);
		/*Propiedad de Mover columnas, todo se aplica sobre el JTable*/
		tablaC.getTableHeader().setReorderingAllowed(false);/*Para que no se muevan las columnas*/
		/*Tamaño para las columnas*/
		tablaC.getColumnModel().getColumn(0).setPreferredWidth(55);
		tablaC.getColumnModel().getColumn(1).setPreferredWidth(200);
		tablaC.getColumnModel().getColumn(2).setPreferredWidth(200);
		tablaC.getColumnModel().getColumn(3).setPreferredWidth(250);
		tablaC.getColumnModel().getColumn(4).setPreferredWidth(95);
		tablaC.getColumnModel().getColumn(5).setPreferredWidth(75);
		tablaC.getColumnModel().getColumn(6).setPreferredWidth(100);
		/*Tamaño para las columnas*/
		add(barra);
		/*Tabla de Resultados*/
		/*Consulta por default*/
		query="select * from usuario;"; mostrarResultado();

		setSize(tamVX, tamVY); setVisible(true); setResizable(false); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana = getSize(); pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((pantalla.width-ventana.width)/2, 0);
	}

	/*Agregar un nuevo cliente*/
	public void agregarCliente()
	{
		tamVX=tamVY=130; posX=10; posY=5; tamX=150; tamY=20;
		JFrame vAgregar = new JFrame("Agregar Cliente"); vAgregar.setLayout(null);
		vAgregar.getContentPane().setBackground(Color.WHITE);
		posY+=tamY;
		eNombre=new JLabel("Nombre"); eNombre.setBounds(posX, posY, tamX, tamY); vAgregar.getContentPane().add(eNombre); 
		iNombre=new JTextField(); iNombre.setBounds(posX, posY+tamY, tamX, tamY); vAgregar.getContentPane().add(iNombre);
		posX=posX+tamX+10;
		eApellidos=new JLabel("Apellidos"); eApellidos.setBounds(posX, posY, tamX, tamY); vAgregar.getContentPane().add(eApellidos);
		iApellidos=new JTextField(); iApellidos.setBounds(posX, posY+tamY, tamX, tamY); vAgregar.getContentPane().add(iApellidos);
		posX=10+(tamX+10)*2;
		eDireccion=new JLabel("Direccion"); eDireccion.setBounds(posX, posY, tamX, tamY); vAgregar.getContentPane().add(eDireccion); 
		iDireccion=new JTextField(); iDireccion.setBounds(posX, posY+tamY, tamX, tamY); vAgregar.getContentPane().add(iDireccion);
		posX=10+(tamX+10)*3;
		eTelefono=new JLabel("Telefono"); eTelefono.setBounds(posX, posY, tamX, tamY); vAgregar.getContentPane().add(eTelefono);
		iTelefono=new JTextField(); iTelefono.setBounds(posX, posY+tamY, tamX, tamY); vAgregar.getContentPane().add(iTelefono);
		/*Botones*/
		aceptar=new JButton(new ImageIcon("data/oper/aceptar.gif")); aceptar.setText("Aceptar"); aceptar.setBounds(5, tamVY-55, (posX+tamX+15)/2-5, tamY); vAgregar.getContentPane().add(aceptar); aceptar.setBackground(Color.WHITE);
		volver=new JButton(new ImageIcon("data/oper/atras.gif")); volver.setText("Volver"); volver.setBounds((posX+tamX+15)/2+5, tamVY-55, (posX+tamX+15)/2-15, tamY); vAgregar.getContentPane().add(volver); volver.setBackground(Color.WHITE);
				
		aceptar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(objValidar.validarParaInsercion(vAgregar, iNombre.getText(), iApellidos.getText(), iDireccion.getText(), iTelefono.getText(), userUpper))
				{
					iNombre.setText(""); iApellidos.setText(""); iDireccion.setText(""); iTelefono.setText("");
					query="Select * from usuario;";
					mostrarResultado();
				}				
			}
		}
		);	

		volver.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent ae) {vAgregar.setVisible(false);}});
		titulo=new JLabel("Agregar Cliente : "+userUpper); titulo.setBounds(0, 5, posX+tamX+15, 20); titulo.setHorizontalAlignment(JLabel.CENTER); vAgregar.getContentPane().add(titulo);
		vAgregar.setSize(posX+tamX+15, tamVY); vAgregar.setVisible(true); vAgregar.setResizable(false); vAgregar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		vAgregar.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
	}

	/*Modificar un cliente*/
	public void modificarCliente()
	{
		tamVX=465; tamVY=160; posX=10; posY=40; tamX=50; tamY=20;
		JFrame vModificar = new JFrame("Modificar Registro");
		vModificar.setLayout(null);
		eId = new JLabel("ID"); eId.setBounds(10, posY, 50, tamY); vModificar.add(eId);
			iId = new JTextField(); iId.setBounds(10, posY+tamY, 50, tamY); vModificar.add(iId);
		eNombre = new JLabel("Nombre"); eNombre.setBounds(65, posY, 125, tamY); vModificar.add(eNombre); 
			iNombre = new JTextField(); iNombre.setBounds(65, posY+tamY, 125, tamY); vModificar.add(iNombre); tamX=100;
		eApellidos = new JLabel("Apellidos"); eApellidos.setBounds(195, posY, 150, tamY); vModificar.add(eApellidos); 
			iApellidos = new JTextField(); iApellidos.setBounds(195, posY+tamY, 150, tamY); vModificar.add(iApellidos);
		eTelefono = new JLabel("Telefono"); eTelefono.setBounds(350, posY, 100, tamY); vModificar.add(eTelefono); 
			iTelefono = new JTextField(); iTelefono.setBounds(350, posY+tamY, 100, tamY); vModificar.add(iTelefono);
		eDireccion = new JLabel("Direccion"); eDireccion.setBounds(10, 80, 200, tamY); vModificar.add(eDireccion); 
			iDireccion = new JTextField(); iDireccion.setBounds(10, 100, 200, tamY); vModificar.add(iDireccion);
		aceptar = new JButton(new ImageIcon("data/oper/save.png")); aceptar.setText("Buscar"); aceptar.setBounds(215, 100, 235, 20); aceptar.setBackground(Color.WHITE); vModificar.add(aceptar);
		/*Estado default de las entradas de modificar*/
		iNombre.setEditable(false); iApellidos.setEditable(false); iDireccion.setEditable(false); iTelefono.setEditable(false);
		aceptar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				try
				{
					if(!iId.getText().isEmpty() && estadoInicial==0)// Si no esta vacio y no se ha activado el buscar
					{
						Integer.parseInt(iId.getText());//Se generará un a excepcion si no es un numero entero
						try
						{
							resultado = objValidar.existe(iId.getText());
							if(resultado!=null)//si hay conicidencias
							{
								resultado.next();
								iId.setEditable(false);
								iNombre.setText(resultado.getString("nombre")); iApellidos.setText(resultado.getString("apellidos")); iDireccion.setText(resultado.getString("direccion"));
								iTelefono.setText(resultado.getString("telefono"));
								estadoInicial=1;/*Ya se oprimio buscar ahora se debe guardar*/
								iTelefono.setEditable(true); iDireccion.setEditable(true); aceptar.setText("Guardar");
							}
							else //si existe el id
							{/**/}
						}
						catch(SQLException ex)
						{JOptionPane.showMessageDialog(vModificar, "Error al tratar de modificar."+ex.getMessage(), "Error", 0);}
					}
					else if(!iId.getText().isEmpty() && estadoInicial==1)/*ya se activo el buscar y ahora se ha de agregar las modificaciones*/
					{
						if(iTelefono.getText().isEmpty() || iDireccion.getText().isEmpty())/*Si alguno de los dos campos esta vacio*/
						{JOptionPane.showMessageDialog(vModificar, "Por favor llena los campos antes de continuar.", "Mensaje", 2);;}
						else if(objValidar.esNumeroTelefono(iTelefono.getText()) && objValidar.esDireccion(iDireccion.getText()))/*Si telefono y direccion son correctos*/
						{
							if(!objValidar.esDuplicado("modificar", iId.getText(), iTelefono.getText()))//Modo para reconsiderar el telefono
							{
								if(JOptionPane.showConfirmDialog(vModificar, "Deseas continuar?", "Continuar", JOptionPane.YES_NO_OPTION)==0)
								{
									try
									{
										conector = new enlace(); fix = conector.getConnection();
										consulta = fix.createStatement();
										query = "update usuario set direccion='"+iDireccion.getText()+"', telefono='"+iTelefono.getText()+"', login='"+userUpper+"', fecha=curdate() where id="+iId.getText()+";";
										System.out.println(query);
										consulta.executeUpdate(query);
										JOptionPane.showMessageDialog(vModificar, "Registro modificado correctamente.", "Mensaje", 1);
										estadoInicial=0;
										iId.setText(""); iNombre.setText(""); iApellidos.setText(""); iDireccion.setText("");
										iTelefono.setText(""); aceptar.setText("Buscar");
										iId.setEditable(true); iDireccion.setEditable(false); iTelefono.setEditable(false);
										query="select * from usuario;";
										mostrarResultado();
										fix.close(); consulta.close();
									}	
									catch(Exception ex)
									{JOptionPane.showMessageDialog(vModificar, "Error al tratar de modificar.", "Error", 0);}
								}
							}
						}

					}
					else
					{JOptionPane.showMessageDialog(vModificar, "Campo ID necesario para poder buscar al Cliente.", "ID requerido", 1);}
				}
				catch(Exception ex)
				{JOptionPane.showMessageDialog(vModificar, "Erro en el ID, es un numero, no letras.", "Advertencia", 1);}
			}
		}
		);
		vModificar.getContentPane().setBackground(Color.WHITE);	
		titulo=new JLabel("Modificar Cliente : "+userUpper); titulo.setBounds(0, 5, tamVX, 20); titulo.setHorizontalAlignment(JLabel.CENTER); vModificar.getContentPane().add(titulo);
		vModificar.setSize(tamVX, tamVY); vModificar.setVisible(true); vModificar.setResizable(false); vModificar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		vModificar.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);	
	}

	/*Eliminar un cliente*/
	public void eliminarCliente()
	{
		tamVX=465; tamVY=160; posX=10; posY=40; tamX=50; tamY=20;
		JFrame vModificar = new JFrame("Eliminar Cliente");
		vModificar.setLayout(null);
		estadoInicial=0;
		eId = new JLabel("ID"); eId.setBounds(10, posY, 50, tamY); vModificar.add(eId);
			iId = new JTextField(); iId.setBounds(10, posY+tamY, 50, tamY); vModificar.add(iId);
		eNombre = new JLabel("Nombre"); eNombre.setBounds(65, posY, 125, tamY); vModificar.add(eNombre); 
			iNombre = new JTextField(); iNombre.setBounds(65, posY+tamY, 125, tamY); vModificar.add(iNombre); tamX=100;
		eApellidos = new JLabel("Apellidos"); eApellidos.setBounds(195, posY, 150, tamY); vModificar.add(eApellidos); 
			iApellidos = new JTextField(); iApellidos.setBounds(195, posY+tamY, 150, tamY); vModificar.add(iApellidos);
		eTelefono = new JLabel("Telefono"); eTelefono.setBounds(350, posY, 100, tamY); vModificar.add(eTelefono); 
			iTelefono = new JTextField(); iTelefono.setBounds(350, posY+tamY, 100, tamY); vModificar.add(iTelefono);
		eDireccion = new JLabel("Direccion"); eDireccion.setBounds(10, 80, 200, tamY); vModificar.add(eDireccion); 
			iDireccion = new JTextField(); iDireccion.setBounds(10, 100, 200, tamY); vModificar.add(iDireccion);
		aceptar = new JButton(new ImageIcon("data/oper/search.png")); aceptar.setText("Buscar"); aceptar.setBounds(215, 100, 235, 20); aceptar.setBackground(Color.WHITE); vModificar.add(aceptar);
		/*Estado default de las entradas de modificar*/
		iNombre.setEditable(false); iApellidos.setEditable(false); iDireccion.setEditable(false); iTelefono.setEditable(false);
		aceptar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				try
				{
					if(!iId.getText().isEmpty() && estadoInicial==0)// Si no esta vacio y no se ha activado el buscar
					{
						Integer.parseInt(iId.getText());//Se generará un a excepcion si no es un numero entero
						try
						{
							resultado = objValidar.existe(iId.getText());
							if(resultado!=null)//si hay conicidencias
							{
								resultado.next();
								iId.setEditable(false);
								iNombre.setText(resultado.getString("nombre")); iApellidos.setText(resultado.getString("apellidos")); iDireccion.setText(resultado.getString("direccion"));
								iTelefono.setText(resultado.getString("telefono"));
								estadoInicial=1;/*Ya se oprimio buscar ahora se debe guardar*/
								aceptar.setIcon(new ImageIcon("data/oper/borrar.gif"));
								aceptar.setText("Borrar");
							}
							else //si existe el id
							{/**/}
						}
						catch(SQLException ex)
						{JOptionPane.showMessageDialog(vModificar, "Error al tratar de eliminar."+ex.getMessage(), "Error", 0);}
					}
					else if(!iId.getText().isEmpty() && estadoInicial==1)/*ya se activo el buscar y ahora se ha de eliminar*/
					{
						try
						{
							conector = new enlace(); fix = conector.getConnection();
							consulta = fix.createStatement();
							resultado = consulta.executeQuery("select count(*) from contrato where contrato.id="+iId.getText()+";");//Verifica que aun no se haya ocupado el cliente en los contratos
							if(resultado.next())
							{
								if(resultado.getInt("count(*)")>0)
								{
									JOptionPane.showMessageDialog(vModificar, "Error el Cliente esta activo en uno o mas contratos,\npor favor Verifica su estado.\nEl cliente no debe tener ningun contrato para poder\nser eliminado.", "Imposible eliminar Cliente (En uso)", 0);
									iId.setText(""); iNombre.setText(""); iApellidos.setText(""); iDireccion.setText(""); iTelefono.setText("");  aceptar.setIcon(new ImageIcon("data/oper/search.png"));
									iId.setEditable(true);
									estadoInicial=0;
								}
								else
								{
									if(JOptionPane.showConfirmDialog(vModificar, "Deseas continuar?\n(El proceso es irreversible)", "Eliminar Cliente", JOptionPane.YES_NO_OPTION)==0)
									{
										query = "delete from usuario where id="+iId.getText()+";";
										consulta.execute(query);
										JOptionPane.showMessageDialog(vModificar, "Cliente eliminado correctamente.", "Mensaje", 1);
										estadoInicial=0;
										iId.setText(""); iNombre.setText(""); iApellidos.setText(""); iDireccion.setText("");
										iTelefono.setText(""); aceptar.setText("Buscar");
										iId.setEditable(true);
										query="select * from usuario;";
										mostrarResultado();
										fix.close(); consulta.close();
									}
								}
							}
						}	
						catch(Exception ex)
						{JOptionPane.showMessageDialog(vModificar, "Error al tratar de modificar.", "Error", 0);}
					}
					else
					{JOptionPane.showMessageDialog(vModificar, "Campo ID necesario para poder buscar al Cliente.", "ID requerido", 1);}
				}
				catch(Exception ex)
				{JOptionPane.showMessageDialog(vModificar, "Erro en el ID, es un numero, no letras.", "Advertencia", 1);}
			}
		}
		);
		vModificar.getContentPane().setBackground(Color.WHITE);	
		titulo=new JLabel("Eliminar Cliente : "+userUpper); titulo.setBounds(0, 5, tamVX, 20); titulo.setHorizontalAlignment(JLabel.CENTER); vModificar.getContentPane().add(titulo);
		vModificar.setSize(tamVX, tamVY); vModificar.setVisible(true); vModificar.setResizable(false); vModificar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		vModificar.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);	
	}

	/*Mostrar Resultados*/
	public void mostrarResultado()
	{
		/*Se limpia la Tabla*/
		int count = tabla.getRowCount() - 1;
		while(count>=0)
		{
			tabla.removeRow(count);
			count--;
		}

		try
		{
			conector = new enlace(); fix = conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery(query);
			/*Si hay algun resultado*/
			while(resultado.next())
			{
				/*Se añade cada registro a la tabla*/
				Object [] newRow = {resultado.getString("id"), resultado.getString("nombre"), resultado.getString("apellidos"), resultado.getString("direccion"), resultado.getString("telefono"), resultado.getString("login"), resultado.getString("fecha")};
				tabla.addRow(newRow);
			}
			fix.close(); consulta.close(); resultado.close();
		}
		catch(SQLException ex)
		{JOptionPane.showMessageDialog(this, "Error: Mostrar resultado.\nConsulta: "+query+ex.getMessage(), "Error", 0); ex.printStackTrace();}
	}

	/*Tecleo para busqueda*/
	public void keyPressed(KeyEvent keyEvent)
	{
		int tipo=keyEvent.getExtendedKeyCode();
		if(tipo==113)//F2
		{agregarCliente();}
		else if(tipo==114 && permisosUpper.equals("administrador"))//F3
		{modificarCliente();}
		else if(tipo==115 && permisosUpper.equals("administrador"))
		{eliminarCliente();}
		else if(tipo==27)//tESC
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
		else//Buscar
		{
			String cB=cBuscar.getText();
			query="select * from usuario where id like'%"+cB+"%' or nombre like'%"+cB+"%' ";
			query+="or apellidos like'%"+cB+"%' or direccion like'%"+cB+"%' ";
			query+="or telefono like'%"+cB+"%' or login like '&"+cB+"&' ";;
			query+="or fecha like'%"+cB+"%';";
			mostrarResultado();
		}
	}
    public void keyReleased(KeyEvent keyEvent)
    {mostrarResultado();}
    public void keyTyped(KeyEvent keyEvent)
    {mostrarResultado();}

	/*public static void main(String...Args)
   	{new adminClientes("JuanFTP10");}*/
}