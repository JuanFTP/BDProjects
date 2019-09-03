import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

class adminContratos extends JFrame implements KeyListener
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
	//validarContrato objValidar = new validarContrato(); //Objeto de validacion
	/*Campos globales*/
	static JLabel titulo, linea;/*Para fines de estilo*/
	static JLabel eFolio, eFecha, eDescripcion, eTipo, eEstado, eID, eNombre, eLogin, eFechaM; /*Etiquetas para salidas de datos*/
	static JTextField iFolio, iId; /*Campos de entrada de datos*/
	static JTextField aDescripcion;
	static JLabel buscar, agregar, editar, eliminar, cerrar; /*Etiquetas para instrucciones*/
	static JButton activar, volver;
	static JComboBox menuTipo;
	static JComboBox menuEstado;
	static JTextField cBuscar; /*Campo para buscar*/
	static String s1="", s2="", s3="", s4="", s5="", s6="", s7="", s8="", s9="";/*Salida para texto en campos*/
	/*Modo modificar*/
	static int estadoInicial=0, segCiclo=0; //Hay que buscar
	/*Para tabla*/
	static String [] columnas = {"Folio", "Fecha Inicio", "Descripcion", "Tipo", "Estado", "ID", "Nombre : Apellidos", "Login", "Fecha Modificado"};
	static Object [][] lineaDefault = {{"---", "---", "---", "---", "---", "---", "---", "---", "---"}};
	static JScrollPane barra;
	static DefaultTableModel tabla;
	static JTable tablaC;
	/*Para Tabla*/
	/*Permisos*/
	public static String userUpper;
	public static String permisosUpper;

	adminContratos(String userUpper, String permisosUpper)
	{
		/*Usuario del sistema en curso*/
		this.userUpper=userUpper;
		this.permisosUpper=permisosUpper;
		new JFrame(); setTitle("Administrar Contratos"); setLayout(null); getContentPane().setBackground(Color.WHITE);
		tamVX=1000; tamVY=700; posY=5; posX=0; tamX=100; tamY=20;
		titulo = new JLabel("Administrar Contratos   Usuario: "+userUpper); titulo.setBounds(0, posY, tamVX, 30); add(titulo); titulo.setHorizontalAlignment(JLabel.CENTER);
		posY+=(tamY*2); posX=10;
		/*Seccion de operaciones*/
		buscar = new JLabel(new ImageIcon("data/oper/busca.png")); buscar.setText("Buscar"); buscar.setBounds(posX, posY, 80, tamY); buscar.setHorizontalAlignment(JLabel.CENTER); add(buscar); posX+=80;
		cBuscar = new JTextField(); cBuscar.setBounds(posX, posY, tamX+20, tamY); add(cBuscar); cBuscar.addKeyListener(this); 
		JLabel etiNotifi = new JLabel("FOLIO o ID"); etiNotifi.setBounds(posX, posY-20, tamX+20, tamY); etiNotifi.setHorizontalAlignment(JLabel.CENTER); add(etiNotifi); posX+=140;
		agregar = new JLabel(new ImageIcon("data/oper/nuevo.gif")); agregar.setText(" Nuevo (F2)"); agregar.setHorizontalAlignment(JLabel.CENTER); agregar.setBounds(posX, posY, tamX, tamY); add(agregar);
		posX+=120;
		/*Operaciones para administrador*/
		if(permisosUpper.equals("administrador")){editar = new JLabel(new ImageIcon("data/oper/editar.gif")); editar.setText(" Editar (F3)"); editar.setHorizontalAlignment(JLabel.CENTER); editar.setBounds(posX, posY, tamX, tamY); add(editar);}
		posX+=120;
		if(permisosUpper.equals("administrador")){eliminar = new JLabel(new ImageIcon("data/oper/borrar.gif")); eliminar.setText(" Borrar (F4)"); eliminar.setHorizontalAlignment(JLabel.CENTER); eliminar.setBounds(posX, posY, tamX, tamY); add(eliminar);}
		posX+=120;
		cerrar = new JLabel(new ImageIcon("data/oper/salir.gif")); cerrar.setText(" Salir [esc]"); cerrar.setBounds(tamVX-130, posY, 100, tamY); cerrar.setHorizontalAlignment(JLabel.RIGHT); add(cerrar);
		/*Tabla de Resultados*/
		posY+=tamY+20; posX=10; tamX=tamVX-25; tamY=tamVY-(posY+40);
		tabla = new DefaultTableModel(lineaDefault, columnas);
		tablaC = new JTable(tabla);
		barra = new JScrollPane(tablaC);
		barra.setBounds(posX, posY, tamX, tamY);
		System.out.println(posX+" "+posY+" "+tamX+" "+tamY);
		/*Propiedad de Mover columnas, todo se aplica sobre el JTable*/
		tablaC.getTableHeader().setReorderingAllowed(false);/*Para que no se muevan las columnas*/
		/*Tamaño para las columnas*/
		tablaC.getColumnModel().getColumn(0).setPreferredWidth(50);
		tablaC.getColumnModel().getColumn(1).setPreferredWidth(90);
		tablaC.getColumnModel().getColumn(2).setPreferredWidth(215);
		tablaC.getColumnModel().getColumn(3).setPreferredWidth(65);
		tablaC.getColumnModel().getColumn(4).setPreferredWidth(60);
		tablaC.getColumnModel().getColumn(5).setPreferredWidth(45);
		tablaC.getColumnModel().getColumn(6).setPreferredWidth(260);
		tablaC.getColumnModel().getColumn(7).setPreferredWidth(70);
		tablaC.getColumnModel().getColumn(8).setPreferredWidth(120);
		/*Tamaño para las columnas*/
		add(barra);
		/*Tabla de Resultados*/


		query = "select folio, contrato.fecha, descripcion, tipoContrato, estado, contrato.id, nombre, apellidos, contrato.login, fechaM from usuario, contrato where contrato.id = usuario.id;";
		mostrarResultado();
		/*Propiedades de la ventana*/
		setSize(tamVX, tamVY); setVisible(true); setResizable(false);
		//setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana = getSize(); pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((pantalla.width-ventana.width)/2 ,0);
	}

	/*Mostrar Resultados*/
	public void mostrarResultado()
	{
		/*Vacia los datos de la tabla*/
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
			while(resultado.next())
			{
				Object [] newRow = {resultado.getString("folio"), resultado.getString("fecha"), resultado.getString("descripcion"), resultado.getString("tipoContrato"), resultado.getString("estado"), resultado.getString("id"), resultado.getString("nombre")+" "+resultado.getString("apellidos"), resultado.getString("login"), resultado.getString("fechaM")};
				tabla.addRow(newRow);
			}
			fix.close(); consulta.close(); resultado.close();
		}
		catch(Exception ex)
		{JOptionPane.showMessageDialog(this, "Error: Mostrar resultado.\nConsulta: "+query+" :\n"+ex.getMessage(), "Error", 0); ex.printStackTrace();}
	}

	/*Nuevo Contrato*/
	static JComboBox clientesList;
	public void nuevoContrato()
	{
		JFrame nuevoContrato = new JFrame("Nuevo Contrato"); tamVX=500; tamVY=200; tamX=100; tamY=20; posX=10; posY=40;
		nuevoContrato.setLayout(null); nuevoContrato.getContentPane().setBackground(Color.WHITE);
		titulo = new JLabel("Nuevo Contrato : "+userUpper); titulo.setBounds(0, 10, tamVX, tamY); titulo.setHorizontalAlignment(JLabel.CENTER); nuevoContrato.getContentPane().add(titulo);
		/*Etiquetas*/
		eFolio = new JLabel("Folio"); eFolio.setBounds(posX, posY, 40, tamY); nuevoContrato.getContentPane().add(eFolio);
			iFolio = new JTextField(); iFolio.setBounds(posX, posY+tamY, 40, tamY); nuevoContrato.getContentPane().add(iFolio); iFolio.setHorizontalAlignment(JTextField.CENTER); iFolio.setEditable(false);
		posX+=50;
		eDescripcion = new JLabel("Descripcion"); eDescripcion.setBounds(posX, posY, 200, tamY); nuevoContrato.getContentPane().add(eDescripcion);
			aDescripcion = new JTextField(); aDescripcion.setBackground(new Color(226, 226, 226)); aDescripcion.setBounds(posX, posY+tamY, 200, 60); nuevoContrato.getContentPane().add(aDescripcion);
		posX+=210;
		eEstado = new JLabel("Estado"); eEstado.setBounds(posX, posY, 100, tamY); nuevoContrato.getContentPane().add(eEstado);
			menuEstado = new JComboBox(); menuEstado.addItem("Selecciona"); menuEstado.setBounds(posX, posY+tamY, 100, tamY); menuEstado.setBackground(Color.WHITE); nuevoContrato.getContentPane().add(menuEstado);
			menuEstado.addItem("Inactivo"); menuEstado.addItem("Activo"); menuEstado.addItem("Pendiente");
		eID = new JLabel("ID Usuario"); eID.setBounds(posX, posY+60, 100, tamY); eID.setHorizontalAlignment(JLabel.CENTER); nuevoContrato.getContentPane().add(eID);
			clientesList = new JComboBox(); clientesList.setBounds(posX+110, posY+60, 100, tamY); nuevoContrato.getContentPane().add(clientesList);
			clientesList.addItem("Selecciona"); clientesList.setBackground(Color.WHITE);
			clientesListRell();

		posX+=110;
		eTipo = new JLabel("Tipo"); eTipo.setBounds(posX, posY, 100, tamY); nuevoContrato.getContentPane().add(eTipo);
			menuTipo = new JComboBox(); menuTipo.addItem("Selecciona"); menuTipo.setBounds(posX, posY+tamY, 100, tamY); menuTipo.setBackground(Color.WHITE); nuevoContrato.getContentPane().add(menuTipo);
			menuTipo.addItem("Local"); menuTipo.addItem("Externo"); menuTipo.addItem("Superior");
		posY+=100;
		/*Botones*/
		activar = new JButton(new ImageIcon("data/oper/aceptar.gif")); activar.setText(" Aceptar"); activar.setHorizontalAlignment(JButton.CENTER); activar.setBounds(10, posY, 240, 20); activar.setBackground(Color.WHITE); nuevoContrato.getContentPane().add(activar);
		volver = new JButton(new ImageIcon("data/oper/atras.gif")); volver.setText(" Volver"); volver.setHorizontalAlignment(JButton.CENTER); volver.setBounds(260, posY, 220, 20); volver.setBackground(Color.WHITE); nuevoContrato.getContentPane().add(volver);

		/*Calcular siguiente ID*/
		try
		{
			conector = new enlace(); fix = conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery("select max(folio) from contrato;");
			if(resultado.next())
			{int dta = resultado.getInt("max(folio)"); dta+=1; iFolio.setText(dta+"");}
			fix.close(); consulta.close(); resultado.close();
		}
		catch(Exception ex)
		{JOptionPane.showMessageDialog(nuevoContrato, "Error al intentar agregar", "Notificion", 0);}
		
		activar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(aDescripcion.getText().isEmpty() || menuEstado.getSelectedIndex()==0 || menuTipo.getSelectedIndex()==0 || clientesList.getSelectedIndex()==0)
				{JOptionPane.showMessageDialog(nuevoContrato, "Por favor, completa todos los campos.", "Campos Requeridos", 0);}
				else
				{
					/*Se obtienen todos los datos a registrar*/	
					String des = aDescripcion.getText();
					String estado = String.valueOf(menuEstado.getSelectedItem());
					String tipo = String.valueOf(menuTipo.getSelectedItem());
					try
					{
						try
						{
							int id_alt = Integer.parseInt(String.valueOf(clientesList.getSelectedItem())); //Convierte y sirve como validador ya que si se genera una exception entonces se cancela
							conector = new enlace(); fix = conector.getConnection();
							consulta = fix.createStatement();
							resultado = consulta.executeQuery("select count(*) from usuario where id="+id_alt+";");
							if(resultado.next())//Si hay retorno
							{
								if(resultado.getInt("count(*)")==1)//Si el usuario existe
								{
									resultado = consulta.executeQuery("select count(*) from contrato where id="+id_alt);//Verifica el numero de contratos
									if(resultado.next())//Si hay respuesta
									{
										if(resultado.getInt("count(*)")==3)//contratos maximos
										{JOptionPane.showMessageDialog(nuevoContrato, "Limite de contratos exedidos para este usuario: "+id_alt, "No se puede Agregar mas contratos", 0);}
										else//Aun se pueda agregar contratos
										{
											resultado = consulta.executeQuery("select count(*) from contrato where id = "+id_alt+" and tipoContrato ='"+tipo+"';");//Se busca el tipo de contrato
											if(resultado.next())//Si hay retorno
											{
												if(resultado.getInt("count(*)")==1)//Si ya existe el contrato
												{JOptionPane.showMessageDialog(nuevoContrato, "Tipo de contrato ya existente.", "Duplicidad de contrato", 0);}
												else//Si no existe se ha de agregar el contrato
												{
													String queryInsert = "insert into contrato(fecha, descripcion, tipoContrato, estado, id, login, fechaM) values ";
													queryInsert += "(curdate(), '"+des+"', '"+tipo+"', '"+estado+"', "+id_alt+", '"+userUpper+"', curdate());";
													if(JOptionPane.showConfirmDialog(nuevoContrato, "Deseas agregar el contrato?", "Continuar", JOptionPane.YES_NO_OPTION)==0)//Si desea continuar
													{
														consulta.executeUpdate(queryInsert);
														JOptionPane.showMessageDialog(nuevoContrato, "Contrato agregado correctamente.", "Operacion exitosa", 1);
														mostrarResultado();
														/*Reseteo de valores*/
														iFolio.setText((Integer.parseInt(iFolio.getText())+1)+"");
														aDescripcion.setText("");
														menuEstado.setSelectedIndex(0); menuTipo.setSelectedIndex(0); clientesList.setSelectedIndex(0);
													}
												}
											}
										}
									}
								}
								else//Si no existe el usuario
								{JOptionPane.showMessageDialog(nuevoContrato, "El usuario no existe verificalo.", "Usuario Inexistente", 0);}
							}
							fix.close(); consulta.close(); resultado.close();
						}
						catch(Exception ex)
						{JOptionPane.showMessageDialog(nuevoContrato, "El ID debe ser un Numero entero. "+ex.getMessage(), "Se Requiere Entero", 0);}
					}
					catch(Exception ex)
					{JOptionPane.showMessageDialog(nuevoContrato, "Error al tratar de agregar un contrato.", "Error en insercion", 0);}
				}
			}
		}
		);

		volver.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{nuevoContrato.setVisible(false);}
		}
		);

		/*Botones*/
		nuevoContrato.setSize(tamVX, tamVY); nuevoContrato.setVisible(true); nuevoContrato.setResizable(false); nuevoContrato.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ventana = getSize(); pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		nuevoContrato.setLocation((pantalla.width-ventana.width)/2 ,0);
	}

	/*Rellenar clientes List*/
	public void clientesListRell()
	{
		try
		{
			conector =  new enlace(); fix = conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery("select count(*) from usuario;");
			if(resultado.next())
			{
				if(resultado.getInt("count(*)")>0)
				{
					clientesList.removeAllItems();
					clientesList.addItem("Selecciona");
					resultado = consulta.executeQuery("select id from usuario order by id;");
					while(resultado.next())
					{
						clientesList.addItem(resultado.getString("id"));
					}	
				}
			}
			else
			{JOptionPane.showMessageDialog(this, "Error en agregar clientes", "Error", 0);}
			fix.close(); consulta.close(); resultado.close();
		}
		catch(SQLException ex)
		{JOptionPane.showMessageDialog(this, "Error en clientesListRell:\n"+ex.getMessage(), "Mensaje", 0);}
	}

	/*Modificar contrado*/
	public void modificarContrato()
	{
		estadoInicial=0;
		JFrame modiContrato = new JFrame("Modificar Contrato"); tamVX=500; tamVY=200; tamX=100; tamY=20; posX=10; posY=40;
		modiContrato.setLayout(null); modiContrato.getContentPane().setBackground(Color.WHITE);
		titulo = new JLabel("Modificar Contrato : "+userUpper); titulo.setBounds(0, 10, tamVX, tamY); titulo.setHorizontalAlignment(JLabel.CENTER); modiContrato.getContentPane().add(titulo);
		/*Etiquetas*/
		eFolio = new JLabel("Folio"); eFolio.setBounds(posX, posY, 40, tamY); modiContrato.getContentPane().add(eFolio);
			iFolio = new JTextField(); iFolio.setBounds(posX, posY+tamY, 40, tamY); modiContrato.getContentPane().add(iFolio); iFolio.setHorizontalAlignment(JTextField.CENTER);
		posX+=50;
		eDescripcion = new JLabel("Descripcion"); eDescripcion.setBounds(posX, posY, 200, tamY); modiContrato.getContentPane().add(eDescripcion);
			aDescripcion = new JTextField(); aDescripcion.setBackground(new Color(226, 226, 226)); aDescripcion.setBounds(posX, posY+tamY, 200, 60); modiContrato.getContentPane().add(aDescripcion);
		posX+=210;
		eEstado = new JLabel("Estado"); eEstado.setBounds(posX, posY, 100, tamY); modiContrato.getContentPane().add(eEstado);
			menuEstado = new JComboBox(); menuEstado.setBounds(posX, posY+tamY, 100, tamY); menuEstado.setBackground(Color.WHITE); modiContrato.getContentPane().add(menuEstado);
			menuEstado.addItem("Inactivo"); menuEstado.addItem("Activo"); menuEstado.addItem("Pendiente");
		eID = new JLabel("ID Usuario"); eID.setBounds(posX, posY+60, 100, tamY); eID.setHorizontalAlignment(JLabel.CENTER); modiContrato.getContentPane().add(eID);
			iId = new JTextField(); iId.setBounds(posX+110, posY+60, 100, tamY); modiContrato.getContentPane().add(iId); iId.setEditable(false);

		posX+=110;
		eTipo = new JLabel("Tipo"); eTipo.setBounds(posX, posY, 100, tamY); modiContrato.getContentPane().add(eTipo);
			menuTipo = new JComboBox(); menuTipo.setBounds(posX, posY+tamY, 100, tamY); menuTipo.setBackground(Color.WHITE); modiContrato.getContentPane().add(menuTipo);
			menuTipo.addItem("Local"); menuTipo.addItem("Externo"); menuTipo.addItem("Superior");
		posY+=100;
		/*Botones*/
		activar = new JButton(new ImageIcon("data/oper/aceptar.gif")); activar.setText(" Buscar"); activar.setHorizontalAlignment(JButton.CENTER); activar.setBounds(10, posY, 240, 20); activar.setBackground(Color.WHITE); modiContrato.getContentPane().add(activar);
		volver = new JButton(new ImageIcon("data/oper/atras.gif")); volver.setText(" Volver"); volver.setHorizontalAlignment(JButton.CENTER); volver.setBounds(260, posY, 220, 20); volver.setBackground(Color.WHITE); modiContrato.getContentPane().add(volver);
		activar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(estadoInicial==0)/*Se precionó buscar*/
				{
					if(iFolio.getText().isEmpty())
					{JOptionPane.showMessageDialog(modiContrato, "Folio de contrato Requerido.", "Folio Requerido", 0);}
					else
					{
						try
						{
							Integer.parseInt(iFolio.getText());/*Convierte el folio a entero y verifica si es un numero entero*/
							try/*Obtencion de valores para el folio*/
							{
								conector = new enlace(); fix = conector.getConnection();
								consulta = fix.createStatement();
								resultado = consulta.executeQuery("select count(*) from contrato where folio="+iFolio.getText()+";");
								if(resultado.next())
								{
									if(resultado.getInt("count(*)")==1)/*Existe el contrato*/
									{
										resultado = consulta.executeQuery("select * from contrato where folio="+iFolio.getText()+";");
										if(resultado.next())/*Verica la existencia del resultado*/
										{
											activar.setText(" Guardar");
											iFolio.setEditable(false); aDescripcion.setText(resultado.getString("descripcion"));
											aDescripcion.setEditable(true); iId.setText(resultado.getInt("id")+"");
											String tipo = resultado.getString("tipoContrato"); String estado = resultado.getString("estado");
											if(tipo.equalsIgnoreCase("Local")){menuTipo.setSelectedIndex(0);}
											else if(tipo.equalsIgnoreCase("Externo")){menuTipo.setSelectedIndex(1);}
											else if(tipo.equalsIgnoreCase("Superior")){menuTipo.setSelectedIndex(2);}
											if(estado.equalsIgnoreCase("Inactivo")){menuEstado.setSelectedIndex(0);}
											else if(estado.equalsIgnoreCase("Activo")){menuEstado.setSelectedIndex(1);}
											else if(estado.equalsIgnoreCase("Pendiente")){menuEstado.setSelectedIndex(2);}
											estadoInicial=1;
										}
									}
									else
									{JOptionPane.showMessageDialog(modiContrato, "Folio Inexistente, verificalo.", "Folio no existe", 2);}
								}
								fix.close(); consulta.close(); resultado.close();
							}
							catch(SQLException ex)
							{JOptionPane.showMessageDialog(modiContrato, "Error al buscar Folio.\n"+ex.getMessage(), "Error", 0);}
						}
						catch(Exception ex)
						{JOptionPane.showMessageDialog(modiContrato, "El folio es un numero Entero.", "Folio de tipo Entero", 0);}
					}
				}
				else if(estadoInicial==1)/*Ya se activo el buscar*/
				{
					try
					{
						if(aDescripcion.getText().isEmpty())
						{JOptionPane.showMessageDialog(modiContrato, "Campo de descripcion vacio.", "Descripcion Requerida", 2);}
						else
						{
							conector = new enlace(); fix = conector.getConnection();
							consulta = fix.createStatement();
							resultado = consulta.executeQuery("select * from contrato where folio = "+iFolio.getText()+";");
							if(resultado.next())//Si hay resultado
							{
								String tipoC = resultado.getString("tipoContrato");
								if(tipoC.equalsIgnoreCase(String.valueOf(menuTipo.getSelectedItem())))//Se obtiene el item y se ve si es lo mismo
								{
									if(JOptionPane.showConfirmDialog(modiContrato, "Deseas continuar?", "Continuar?", JOptionPane.YES_NO_OPTION)==0)
									{
										consulta.executeUpdate("update contrato set descripcion = '"+aDescripcion.getText()+"', estado = '"+String.valueOf(menuEstado.getSelectedItem())+"', login = '"+userUpper+"', fechaM = curdate() where folio = '"+iFolio.getText()+"';");
										JOptionPane.showMessageDialog(modiContrato, "Actualizacion completada\ncorrectamente.", "Finalizado", 1);
										estadoInicial=0;
										iFolio.setText(""); iFolio.setEditable(true); aDescripcion.setText(""); menuTipo.setSelectedIndex(0); menuEstado.setSelectedIndex(0); iId.setText("");
										mostrarResultado(); activar.setText(" Buscar");
									}
								}
								else/*Hay un cambio en el tipo*/
								{
									resultado = consulta.executeQuery("select count(*) from contrato where id="+iId.getText()+" and tipoContrato = '"+String.valueOf(menuTipo.getSelectedItem())+"';");
									if(resultado.next())//Ya existe el tipo
									{
										if(resultado.getInt("count(*)")==1)
										{
											JOptionPane.showMessageDialog(modiContrato, "El tipo de contrato ya esta habilitado.", "Duplicidad de contrato", 0);
											System.out.println("Estado: Duplicidad "+estadoInicial);
										}
										else
										{
											if(JOptionPane.showConfirmDialog(modiContrato, "[0] Deseas continuar?", "Continuar?", JOptionPane.YES_NO_OPTION)==0)
											{
												consulta.executeUpdate("update contrato set descripcion = '"+aDescripcion.getText()+"', tipoContrato = '"+String.valueOf(menuTipo.getSelectedItem())+"', estado = '"+String.valueOf(menuEstado.getSelectedItem())+"', login = '"+userUpper+"', fechaM = curdate() where folio = '"+iFolio.getText()+"';");
												JOptionPane.showMessageDialog(modiContrato, "Actualizacion completada\ncorrectamente.", "Finalizado", 1);
												iFolio.setText(""); iFolio.setEditable(true); aDescripcion.setText(""); menuTipo.setSelectedIndex(0); menuEstado.setSelectedIndex(0); iId.setText("");
												mostrarResultado(); activar.setText(" Buscar");
												estadoInicial=0;
											}
										}
									}
								}
							}
							fix.close(); consulta.close(); resultado.close();
						}
					}
					catch(SQLException  ex)
					{JOptionPane.showMessageDialog(modiContrato, "Error al tratar de modificar.\n"+ex.getMessage(), "Error", 0);}
					estadoInicial=0;/*Reinicio de todo*/
				}
			}
		}
		);

		volver.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{modiContrato.setVisible(false);}
		}
		);

		/*Botones*/
		modiContrato.setSize(tamVX, tamVY); modiContrato.setVisible(true); modiContrato.setResizable(false); modiContrato.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ventana = getSize(); pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		modiContrato.setLocation((pantalla.width-ventana.width)/2 ,0);
	}	

	/*Eliminar contrato*/
	public void eliminarContrato()
	{
		estadoInicial=0;
		JFrame eliminarContrato = new JFrame("Eliminar Contrato"); tamVX=500; tamVY=200; tamX=100; tamY=20; posX=10; posY=40;
		eliminarContrato.setLayout(null); eliminarContrato.getContentPane().setBackground(Color.WHITE);
		titulo = new JLabel("Eliminar Contrato : "+userUpper); titulo.setBounds(0, 10, tamVX, tamY); titulo.setHorizontalAlignment(JLabel.CENTER); eliminarContrato.getContentPane().add(titulo);
		/*Etiquetas*/
		eFolio = new JLabel("Folio"); eFolio.setBounds(posX, posY, 40, tamY); eliminarContrato.getContentPane().add(eFolio);
			iFolio = new JTextField(); iFolio.setBounds(posX, posY+tamY, 40, tamY); eliminarContrato.getContentPane().add(iFolio); iFolio.setHorizontalAlignment(JTextField.CENTER);
		posX+=50;
		eDescripcion = new JLabel("Descripcion"); eDescripcion.setBounds(posX, posY, 200, tamY); eliminarContrato.getContentPane().add(eDescripcion);
			aDescripcion = new JTextField(); aDescripcion.setBackground(new Color(226, 226, 226)); aDescripcion.setBounds(posX, posY+tamY, 200, 60); eliminarContrato.getContentPane().add(aDescripcion);
		posX+=210;
		eEstado = new JLabel("Estado"); eEstado.setBounds(posX, posY, 100, tamY); eliminarContrato.getContentPane().add(eEstado);
			menuEstado = new JComboBox(); menuEstado.setBounds(posX, posY+tamY, 100, tamY); menuEstado.setBackground(Color.WHITE); eliminarContrato.getContentPane().add(menuEstado);
			menuEstado.addItem("Inactivo"); menuEstado.addItem("Activo"); menuEstado.addItem("Pendiente");
		eID = new JLabel("ID Usuario"); eID.setBounds(posX, posY+60, 100, tamY); eID.setHorizontalAlignment(JLabel.CENTER); eliminarContrato.getContentPane().add(eID);
			iId = new JTextField(); iId.setBounds(posX+110, posY+60, 100, tamY); eliminarContrato.getContentPane().add(iId);

		posX+=110;
		eTipo = new JLabel("Tipo"); eTipo.setBounds(posX, posY, 100, tamY); eliminarContrato.getContentPane().add(eTipo);
			menuTipo = new JComboBox(); menuTipo.setBounds(posX, posY+tamY, 100, tamY); menuTipo.setBackground(Color.WHITE); eliminarContrato.getContentPane().add(menuTipo);
			menuTipo.addItem("Local"); menuTipo.addItem("Externo"); menuTipo.addItem("Superior");
		posY+=100;
		aDescripcion.setEditable(false); iId.setEditable(false); menuEstado.setEnabled(false); menuTipo.setEnabled(false); 
		/*Botones*/
		activar = new JButton(new ImageIcon("data/oper/aceptar.gif")); activar.setText(" Buscar"); activar.setHorizontalAlignment(JButton.CENTER); activar.setBounds(10, posY, 240, 20); activar.setBackground(Color.WHITE); eliminarContrato.getContentPane().add(activar);
		volver = new JButton(new ImageIcon("data/oper/atras.gif")); volver.setText(" Volver"); volver.setHorizontalAlignment(JButton.CENTER); volver.setBounds(260, posY, 220, 20); volver.setBackground(Color.WHITE); eliminarContrato.getContentPane().add(volver);

		activar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(estadoInicial==0)/*Es la primera busqueda*/
				{
					if(iFolio.getText().isEmpty())
					{JOptionPane.showMessageDialog(eliminarContrato, "Se requiere el folio del contrato\npara poder eliminarlo.", "Folio Requerido", 0);}
					else
					{
						try
						{
							int folioI = Integer.parseInt(iFolio.getText());//Folio valido
							try
							{
								conector = new enlace(); fix = conector.getConnection();
								consulta = fix.createStatement();
								resultado = consulta.executeQuery("select count(*) from contrato where folio="+iFolio.getText());
								if(resultado.next())
								{
									if(resultado.getInt("count(*)")==1)
									{
										resultado = consulta.executeQuery("select * from contrato where folio = "+iFolio.getText());
										if(resultado.next())
										{
											iFolio.setEditable(false);  aDescripcion.setText(resultado.getString("descripcion"));
											menuTipo.setSelectedItem(resultado.getString("tipoContrato"));
											menuEstado.setSelectedItem(resultado.getString("estado"));
											iId.setText(resultado.getInt("id")+"");
											activar.setText(" Eliminar"); estadoInicial=1;
										}
									}
									else
									{JOptionPane.showMessageDialog(eliminarContrato, "El Folio no existe, verificalo.", "Folio Inexistente", 0);}
								}
								fix.close(); consulta.close(); resultado.close();
							}
							catch(SQLException ex)
							{JOptionPane.showMessageDialog(eliminarContrato, "Error en la consulta General", "Eliminar imposible", 0);}

						}
						catch(Exception ex)
						{JOptionPane.showMessageDialog(eliminarContrato, "El folio es de tipo numerico.", "Folio Entero", 0);}
					}
				}
				else if(estadoInicial==1)
				{
					if(JOptionPane.showConfirmDialog(eliminarContrato, "Deseas continuar?\nNo se podra revertir los cambios", "Eliminar", JOptionPane.YES_NO_OPTION)==0)
					{
						try
						{
							conector = new enlace(); fix = conector.getConnection();
							consulta = fix.createStatement();
							resultado = consulta.executeQuery("select estado from contrato where folio ="+iFolio.getText());
							if(resultado.next())
							{
								if(resultado.getString("estado").equalsIgnoreCase("Activo"))//Contrato activo
								{
									JOptionPane.showMessageDialog(eliminarContrato, "El contrato esta ACTIVO\nno es posible eliminar.", "Contrato Activo", 0);
									activar.setText(" Buscar");
									iFolio.setEditable(true); iFolio.setText(""); aDescripcion.setText(""); menuTipo.setSelectedIndex(0);
									menuEstado.setSelectedIndex(0); iId.setText("");
									estadoInicial=0;
								}
								else//Contrato inactivo
								{
									consulta.executeUpdate("delete from contrato where folio = "+iFolio.getText());
									JOptionPane.showMessageDialog(eliminarContrato, "Operacion completada exitosamente.", "Operacion completada", 1);
									activar.setText(" Buscar");
									iFolio.setEditable(true); iFolio.setText(""); aDescripcion.setText(""); menuTipo.setSelectedIndex(0);
									menuEstado.setSelectedIndex(0); iId.setText("");
									estadoInicial=0;
									mostrarResultado();
								}
							}
							fix.close(); consulta.close(); resultado.close();
						}	
						catch(SQLException ex)
						{JOptionPane.showMessageDialog(eliminarContrato, "El contrato ya tiene transacciones sobre el,\nNO es posible eliminarlo.", "Error", 0);}
					}
				}
			}
		}
		);

		volver.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{eliminarContrato.setVisible(false);}
		}
		);

		/*Botones*/
		eliminarContrato.setSize(tamVX, tamVY); eliminarContrato.setVisible(true); eliminarContrato.setResizable(false); eliminarContrato.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ventana = getSize(); pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		eliminarContrato.setLocation((pantalla.width-ventana.width)/2 ,0);
	}

	/*Tecleo para busqueda*/
	public void keyPressed(KeyEvent keyEvent)
	{
		int tipo=keyEvent.getExtendedKeyCode();
		if(tipo==113)//F2
		{nuevoContrato();}
		else if(tipo==114 && permisosUpper.equals("administrador"))//F3
		{modificarContrato();}
		else if(tipo==115 && permisosUpper.equals("administrador"))//F4
		{eliminarContrato();}
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
			try
			{
				int di = Integer.parseInt(cBuscar.getText());
				query = "select folio, contrato.fecha, descripcion, tipoContrato, estado, contrato.id, contrato.login, fechaM, nombre, apellidos from contrato, usuario where contrato.id=usuario.id and (folio="+di+" or usuario.id="+di+");";
				mostrarResultado();
			}
			catch(Exception ex)
			{
				query = "select folio, contrato.fecha, descripcion, tipoContrato, estado, contrato.id, contrato.login, fechaM, nombre, apellidos from contrato, usuario where contrato.id=usuario.id;";
				mostrarResultado();
			}
		}
	}
    public void keyReleased(KeyEvent keyEvent)
    {
    	mostrarResultado();
    }
    public void keyTyped(KeyEvent keyEvent)
    {
    	mostrarResultado();
    }
}