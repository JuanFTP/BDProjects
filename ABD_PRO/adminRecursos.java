import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

class adminRecursos extends JFrame implements KeyListener
{
	public Dimension pantalla, ventana;
	static enlace conector;
	static Connection fix;
	static Statement consulta;
	static ResultSet resultado;

	/*Para tabla*/
	static String [] columnas = {"ID", "Fecha", "Descripcion", "Importe", "Login"};
	static Object [][] lineaDefault = {{"---", "---", "---", "---", "---"}};
	static JScrollPane barraIn, barraEg, barraBusqueda;
	static DefaultTableModel tablaIn, tablaEg, tablaBusqueda;
	static JTable tablaCIn, tablaCEg, tablaCBusqued;

	/*Elementos*/
	static JLabel eTitulo, eInstrucciones;
	static JLabel eSalIn, eSalEg, eTotal, eDescripcion, eImporte;
	static JTextField cSalIn, cSalEg, cTotal, cDescripcion, cImporte;
	static JButton botonGenerar;/*Para Agregar el Ingreso o egreso según sea*/
	/*Posicionadores*/
	static int posX, posY, tamX, tamY, tamVX, tamVY, indiOperacion; /*Indicador de operacion de las F, F2=2, F3=3, f4=4, 1 es que esta en el frame normal*/
	static String queryIngreso, queryEgreso;/*Cadenas para consultas de las tablas*/
	static JFrame wOpciones; /*Frame para busquedas y ese relajo*/
	/*Permisos*/
	public static String userUpper;
	public static String permisosUpper;

	adminRecursos(String userUpper, String permisosUpper)
	{
		this.userUpper = userUpper;
		this.permisosUpper=permisosUpper;
		indiOperacion=1;
		new JFrame(); setTitle("Recursos (Admin)"); setLayout(null); getContentPane().setBackground(Color.WHITE);
		posX=0; posY=5; tamX=100; tamY=20; tamVX=1300; tamVY=700;
		eTitulo = new JLabel("Administracion de Recursos                 Usuario: "+userUpper); eTitulo.setBounds(0, posY, tamVX, 20); add(eTitulo); eTitulo.setHorizontalAlignment(JLabel.CENTER);
		eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(200, 34, 67));
		posY+=25;
		eInstrucciones = new JLabel("F2: Registrar Ingreso                   F3: Registrar Egreso                   F4: Buscar                   (ESC) Salir"); eInstrucciones.setBounds(0, posY, tamVX, tamY); eInstrucciones.setHorizontalAlignment(JLabel.CENTER); add(eInstrucciones); eInstrucciones.setForeground(new Color(128, 128, 128));
		posY+=tamY+5; posX=385;
		int posYY=posY; /*Para guardar la posicion*/
		eTitulo = new JLabel("Historial de Ingresos"); eTitulo.setBounds(posX, posY, 900, 25); add(eTitulo); eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(0, 0, 128));
		posY+=tamY+10; tamY+=250;
		/*Tabla Ingresos*/
		tablaIn = new DefaultTableModel(lineaDefault, columnas);
		tablaCIn = new JTable(tablaIn);
		tablaCIn.getTableHeader().setReorderingAllowed(false);/*Para que no se muevan las columnas*/
		/*Tamaño para las columnas*/
		tablaCIn.getColumnModel().getColumn(0).setPreferredWidth(50);
		tablaCIn.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaCIn.getColumnModel().getColumn(2).setPreferredWidth(500);
		tablaCIn.getColumnModel().getColumn(3).setPreferredWidth(150);
		tablaCIn.getColumnModel().getColumn(4).setPreferredWidth(100);
		/*Tamaño para las columnas*/
		barraIn = new JScrollPane(tablaCIn); barraIn.setBounds(posX, posY, 900, tamY); add(barraIn); tablaCIn.addKeyListener(this);
		posY+=tamY+5;
		/*Tabla Ingresos*/
		eTitulo = new JLabel("Historia de Egresos"); eTitulo.setBounds(posX, posY, 900, 25); add(eTitulo); eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(13, 191, 84));		
		posY+=30;
		/*Tabla Egresos*/
		tablaEg = new DefaultTableModel(lineaDefault, columnas);
		tablaCEg = new JTable(tablaEg);
		tablaCEg.getTableHeader().setReorderingAllowed(false);/*Para que no se muevan las columnas*/
		/*Tamaño para las columnas*/
		tablaCEg.getColumnModel().getColumn(0).setPreferredWidth(50);
		tablaCEg.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaCEg.getColumnModel().getColumn(2).setPreferredWidth(500);
		tablaCEg.getColumnModel().getColumn(3).setPreferredWidth(150);
		tablaCEg.getColumnModel().getColumn(4).setPreferredWidth(100);
		/*Tamaño para las columnas*/
		barraEg = new JScrollPane(tablaCEg); barraEg.setBounds(posX, posY, 900, tamY); add(barraEg); tablaCEg.addKeyListener(this);
		posY+=tamY+5;
		/*Tabla Egresos*/
		/*Esquemas Generales*/
		/*posYY Servirá para poder colocar las etiquetas*/
		posX=20; tamX=345; tamY=25;
		eSalIn = new JLabel("Total Ingresos"); eSalIn.setBounds(posX, posYY, tamX, tamY); add(eSalIn); eSalIn.setFont(new Font("Roboto", Font.PLAIN, 20)); eSalIn.setForeground(new Color(255, 128, 64)); eSalIn.setHorizontalAlignment(JLabel.CENTER);
			posYY+=tamY+5;
			cSalIn = new JTextField("$"); cSalIn.setBounds(posX, posYY, tamX, tamY*6); add(cSalIn); cSalIn.setHorizontalAlignment(JTextField.CENTER); cSalIn.setEditable(false); cSalIn.setFont(new Font("Roboto", Font.PLAIN, 50)); cSalIn.setForeground(new Color(0, 0, 128));
			cSalIn.setBackground(Color.WHITE); cSalIn.addKeyListener(this);
			posYY+=(tamY*5)+45;
		eSalEg = new JLabel("Total Egresos"); eSalEg.setBounds(posX, posYY, tamX, tamY); add(eSalEg); eSalEg.setFont(new Font("Roboto", Font.PLAIN, 20)); eSalEg.setForeground(new Color(255, 128, 64)); eSalEg.setHorizontalAlignment(JLabel.CENTER);
			posYY+=tamY+5;
			cSalEg = new JTextField("$"); cSalEg.setBounds(posX, posYY, tamX, tamY*6); add(cSalEg); cSalEg.setHorizontalAlignment(JTextField.CENTER); cSalEg.setEditable(false); cSalEg.setFont(new Font("Roboto", Font.PLAIN, 50)); cSalEg.setForeground(new Color(13, 191, 84));
			cSalEg.setBackground(Color.WHITE); cSalEg.addKeyListener(this);
			posYY+=(tamY*5)+45;
		eTotal = new JLabel("En Caja"); eTotal.setBounds(posX, posYY, tamX, tamY); add(eTotal); eTotal.setFont(new Font("Roboto", Font.PLAIN, 20)); eTotal.setForeground(new Color(255, 128, 64)); eTotal.setHorizontalAlignment(JLabel.CENTER);
			posYY+=tamY+5;
			cTotal = new JTextField("$"); cTotal.setBounds(posX, posYY, tamX, tamY*6); add(cTotal); cTotal.setHorizontalAlignment(JTextField.CENTER); cTotal.setEditable(false); cTotal.setFont(new Font("Roboto", Font.PLAIN, 50)); cTotal.setForeground(new Color(128, 128, 128));
			cTotal.setBackground(Color.WHITE); cTotal.addKeyListener(this);
		/*Esquemas Generales*/
		/*Primera invocacion*/
		queryIngreso = "select * from ingreso;";
		queryEgreso = "select * from egreso;";
		mostrarTodo(queryIngreso, queryEgreso);
		/*Primera invocacion*/
		/*Funciones de Operacion*/
		setVisible(true); setResizable(false); setSize(tamVX, tamVY);
		ventana = getSize();
		pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((pantalla.width-ventana.width)/2 ,0);
		//setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);
	}

	public void operacion()
	{
		wOpciones = new JFrame(); wOpciones.setLayout(null); wOpciones.getContentPane().setBackground(Color.WHITE);
		tamVX=500; tamVY=500; posX=0; posY=5; tamX=100; tamY=25;
		
		/*Campos de Entrada a Registrar*/
		posX=10; posY+=tamY+5; tamX=400;
		if(indiOperacion==2)/*Nuevo Ingreso*/
		{eDescripcion = new JLabel("Descripcion del Ingreso");}
		else if(indiOperacion==3)/*Nuevo Egreso*/
		{eDescripcion = new JLabel("Descripcion del Egreso");}
		
		if(indiOperacion==2 || indiOperacion==3)/*Que no sea busqueda*/
		{
			eDescripcion.setBounds(posX, posY, tamX, tamY); wOpciones.add(eDescripcion); eDescripcion.setHorizontalAlignment(JLabel.CENTER);
				cDescripcion = new JTextField(); cDescripcion.setBounds(posX, posY+tamY, tamX, tamY); cDescripcion.setHorizontalAlignment(JTextField.CENTER);
			posX+=tamX+03; 
			tamX=100;
			eImporte = new JLabel("Importe Total"); eImporte.setBounds(posX, posY, tamX, tamY); wOpciones.add(eImporte); eImporte.setHorizontalAlignment(JLabel.CENTER);
				cImporte = new JTextField(); cImporte.setBounds(posX, posY+tamY, tamX, tamY); cImporte.setHorizontalAlignment(JTextField.CENTER);
			posY+=tamY+30;
			/*Fijar Ajuste de la Ventana para Agregar Ingreso o Egreso*/
			tamVX=posX+tamX+15;
			botonGenerar = new JButton(new ImageIcon("data/oper/save.png")); botonGenerar.setBounds(10, posY, tamVX-25, tamY*2); botonGenerar.setHorizontalAlignment(JButton.CENTER);
			botonGenerar.setBackground(Color.WHITE); botonGenerar.setEnabled(false);
			if(indiOperacion==2)/*Ingreso*/
			{botonGenerar.setText("Registrar Ingreso");}
			else if(indiOperacion==3)/*Egreso*/
			{botonGenerar.setText("Registrar Egreso");}
			posY+=(tamY*2)+10;
			tamVY=posY+30;
			wOpciones.add(eDescripcion); wOpciones.add(eImporte); wOpciones.add(cDescripcion); wOpciones.add(cImporte); wOpciones.add(botonGenerar);
			cDescripcion.addKeyListener(this); cImporte.addKeyListener(this);
		}
		else if(indiOperacion==4)/*Para Busqueda*/
		{
			tamVX=700;/*TamVentana para Busqueda*/
			eDescripcion = new JLabel(new ImageIcon("data/oper/search.png")); eDescripcion.setText("Ingresa tu busqueda [Cualquer Aspecto] (Ingreso/Egreso)"); eDescripcion.setBounds(posX, posY, tamVX, tamY); wOpciones.add(eDescripcion);
			eDescripcion.setHorizontalAlignment(JLabel.CENTER);
			posY+=tamY+5;
			cDescripcion = new JTextField(); cDescripcion.setBounds(posX, posY, tamVX-25, tamY); wOpciones.add(cDescripcion); cDescripcion.setHorizontalAlignment(JTextField.CENTER);
			posY+=tamY+10;
			/*Tabla Ingresos*/
			eTitulo = new JLabel("Ingresos"); eTitulo.setBounds(posX, posY, tamVX-25, 25); wOpciones.add(eTitulo); eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(13, 191, 84));		
			posY+=30;
			tablaIn = new DefaultTableModel(lineaDefault, columnas);
			tablaCIn = new JTable(tablaIn);
			tablaCIn.getTableHeader().setReorderingAllowed(false);/*Para que no se muevan las columnas*/
			/*Tamaño para las columnas*/
			tablaCIn.getColumnModel().getColumn(0).setPreferredWidth(40);
			tablaCIn.getColumnModel().getColumn(1).setPreferredWidth(80);
			tablaCIn.getColumnModel().getColumn(2).setPreferredWidth(205);
			tablaCIn.getColumnModel().getColumn(3).setPreferredWidth(80);
			tablaCIn.getColumnModel().getColumn(4).setPreferredWidth(60);
			/*Tamaño para las columnas*/
			int tamTablaB = 250;
			barraIn = new JScrollPane(tablaCIn); barraIn.setBounds(posX, posY, tamVX-25, tamTablaB); wOpciones.add(barraIn); tablaCIn.addKeyListener(this);
			posY+=tamTablaB+5;
			/*Tabla Ingresos*/
			eTitulo = new JLabel("Egresos"); eTitulo.setBounds(posX, posY, tamVX-25, 25); wOpciones.add(eTitulo); eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(13, 191, 84));		
			posY+=30;
			/*Tabla Egresos*/
			tablaEg = new DefaultTableModel(lineaDefault, columnas);
			tablaCEg = new JTable(tablaEg);
			tablaCEg.getTableHeader().setReorderingAllowed(false);/*Para que no se muevan las columnas*/
			/*Tamaño para las columnas*/
			tablaCEg.getColumnModel().getColumn(0).setPreferredWidth(40);
			tablaCEg.getColumnModel().getColumn(1).setPreferredWidth(80);
			tablaCEg.getColumnModel().getColumn(2).setPreferredWidth(205);
			tablaCEg.getColumnModel().getColumn(3).setPreferredWidth(80);
			tablaCEg.getColumnModel().getColumn(4).setPreferredWidth(60);
			/*Tamaño para las columnas*/
			barraEg = new JScrollPane(tablaCEg); barraEg.setBounds(posX, posY, tamVX-25, tamTablaB); wOpciones.add(barraEg); tablaCEg.addKeyListener(this);
			posY+=tamTablaB+35;
			tamVY=posY;
			/*Tabla Egresos*/
			/*Eventos*/
			cDescripcion.addKeyListener(this); tablaCIn.addKeyListener(this); tablaCEg.addKeyListener(this);
		}
		/*Campos de Entrada a Registrar*/
		/*Titulo de la ventana*/
		if(indiOperacion==2)/*Operacion de agregar ingreso*/
		{
			wOpciones.setTitle("Registrar Nuevo Ingreso"); 
			eTitulo = new JLabel("Nuevo Ingreso            Salir(ESC)            Usuario: "+userUpper);
		}
		else if(indiOperacion==3)/*F3 agregar nuevo egreso*/
		{
			wOpciones.setTitle("Registrar Nuevo Egreso"); 
			eTitulo = new JLabel("Nuevo Egreso            Salir(ESC)            Usuario: "+userUpper);
		}
		else if(indiOperacion==4)/*F4 Buscar*/
		{
			wOpciones.setTitle("Buscar Registro"); 
			eTitulo = new JLabel("Buscar Registro            Salir(ESC)            Usuario: "+userUpper);
		}
		eTitulo.setBounds(0, 5, tamVX, tamY); add(eTitulo); eTitulo.setHorizontalAlignment(JLabel.CENTER);
		eTitulo.setFont(new Font("Roboto", Font.PLAIN, 15)); eTitulo.setForeground(new Color(200, 34, 67));
		wOpciones.add(eTitulo);
		/*Titulo de la ventana*/
		if(indiOperacion==2 || indiOperacion==3)/*Solo se va a agregar cuando las opciones sean 2 o 3*/
		{
			/*Boton de ejecutar*/
			botonGenerar.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					/*Operacion de acuerdo a cada Opcion*/
					int proceso = 0;/*Indicará si se relizo alguna operacion correcta de agregacion.*/
					if (indiOperacion==2 || indiOperacion==3)/*Para que este dentro de las operaciones*/
					{
						try
						{
							conector = new enlace(); fix = conector.getConnection();
							consulta =  fix.createStatement();
							String insert = indiOperacion==2 ? "insert into ingreso ": "insert into egreso ";/*Agregar cabecera*/
							insert+="(fecha, descripcion, importe, login) values(curdate(), '"+cDescripcion.getText()+"',"+cImporte.getText()+", '"+userUpper+"');";
							if(indiOperacion==2)/*Agregar Ingreso*/
							{
								if(JOptionPane.showConfirmDialog(wOpciones, "El Registro es correcto?", "Agregar Ingreso", JOptionPane.YES_NO_OPTION)==0)
								{
									consulta.executeUpdate(insert);
									JOptionPane.showMessageDialog(wOpciones, "Registro agregado correctamente :)", "Operacion completada", 1);
									proceso=1;/*Se generó algun cambio*/
								}
							}
							else if(indiOperacion==3)/*Agregar Egreso*/
							{
								/*Verificacion lo que hay en caja*/
								resultado = consulta.executeQuery("select (select sum(importe) from ingreso)-(select sum(importe) from egreso) as 'caja';");
								if(resultado.next())
								{
									System.out.println(resultado.getString("caja"));
									if(resultado.getObject("caja")==null)/*No hubo resultados*/
									{
										resultado = consulta.executeQuery("select sum(importe) as 'caja' from ingreso;");
										if(resultado.next())
										{
											if(resultado.getDouble("caja")>=Double.parseDouble(cImporte.getText()))
											{
												if(JOptionPane.showConfirmDialog(wOpciones, "Agregar Egreso?", "Continuar: Datos correctos?", JOptionPane.YES_NO_OPTION)==0)
												{
													consulta.executeUpdate(insert);
													JOptionPane.showMessageDialog(wOpciones, "Registro agregado correctamente :)", "Operacion completada", 1);
													proceso=1;/*Se generó algun cambio*/
												}
											}
											else
											{
												double faltaIMP = (-1)*(Double.parseDouble(resultado.getString("caja")) - Double.parseDouble(cImporte.getText()));
												JOptionPane.showMessageDialog(wOpciones, "Fondos insuficientes\nNO SE PUEDE PAGAR EL EGRESO.\nFaltan:"+faltaIMP, "Fondos insuficientes: Caja", 2);
											}
										}
										else
										{JOptionPane.showMessageDialog(wOpciones, "No se obtuvo correctamente el ingreso", "Mensaje", 0);}
									}
									else
									{
										if(resultado.getDouble("caja")>=Double.parseDouble(cImporte.getText()))
										{
											if(JOptionPane.showConfirmDialog(wOpciones, "Agregar Egreso?", "Continuar: Datos correctos?", JOptionPane.YES_NO_OPTION)==0)
											{
												consulta.executeUpdate(insert);
												JOptionPane.showMessageDialog(wOpciones, "Registro agregado correctamente :)", "Operacion completada", 1);
												proceso=1;/*Se generó algun cambio*/
											}
										}
										else
										{
											double falta = (-1)*(Double.parseDouble(resultado.getString("caja")) - Double.parseDouble(cImporte.getText()));
											JOptionPane.showMessageDialog(wOpciones, "Fondos insuficientes\nNO SE PUEDE PAGAR EL EGRESO.\nFaltan:"+falta, "Fondos insuficientes: Caja", 2);
										}
									}
								}
								else
								{JOptionPane.showMessageDialog(wOpciones, "Error al obtener lo que hay en caja", "Error: Caja", 0);}
								resultado.close();
							}
							fix.close(); consulta.close();
							/*Si se generó algun cambio, actualizar tablas*/
							if(proceso==1)/*Se generó algún cambio y reinicio*/
							{
								queryIngreso="select * from ingreso;";
								queryEgreso="select * from egreso;";
								mostrarTodo(queryIngreso, queryEgreso);
								cDescripcion.setText(""); cImporte.setText("");
								proceso=0;/*Reset del indicador*/
							}
						}
						catch(Exception ex)
						{JOptionPane.showMessageDialog(wOpciones, "Error al intentar Agregar (Ingreso/Egreso)\nError:\n"+ex.getMessage());}
					}
				}
			}
			);
			/*Boton de ejecutar*/
		}
		/*Funciones de Operacion*/
		wOpciones.setVisible(true); wOpciones.setResizable(false); wOpciones.setSize(tamVX, tamVY);
		ventana = getSize();
		pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		wOpciones.setLocation((pantalla.width-ventana.width)/2 ,0);
		wOpciones.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		wOpciones.getContentPane().addKeyListener(this);
	}

	public void mostrarTodo(String query, String querydos)
	{
		try
		{
			int count = 0;
			conector = new enlace(); fix = conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery("select count(*) from ingreso;");
			if(resultado.next())
			{
				if(resultado.getInt("count(*)")>0)/*Hay pagos de Ingreso*/
				{
					/*Borrado de los registros de la tabla*/
					count = tablaIn.getRowCount()-1;
					while(count>=0)
					{
						tablaIn.removeRow(count);
						count--;
					}
					resultado = consulta.executeQuery(query);
					while(resultado.next())/*Se agregan los datos a la tabla de ingresos*/
					{
						Object [] newRow = {resultado.getString("id"), resultado.getString("fecha"), resultado.getString("descripcion"), resultado.getString("importe"), resultado.getString("login")};
						tablaIn.addRow(newRow);
					}
					
					if(indiOperacion!=4)/*Calcular Ingreso cuando no este en busqueda*/
					{
						resultado = consulta.executeQuery("select sum(importe) from ingreso;");
						if(resultado.next())/*Para importe*/
						{cSalIn.setText("$"+Double.parseDouble(resultado.getString("sum(importe)")));}
						else
						{JOptionPane.showMessageDialog(this, "Error al tratar de agregar el total de importe de los pagos de Ingreso", "Error, en Ingreso", 0);}
					}
				}
				else
				{cSalIn.setText(""); cSalIn.setText("$0.0");}
				/*Propiedad de Edicion*/
			}
			else
			{JOptionPane.showMessageDialog(this, "Error en select count(*) from ingreso;", "Error", 0);}
			fix.close(); consulta.close(); resultado.close();

			conector = new enlace(); fix = conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery("select count(*) from egreso;");
			if(resultado.next())
			{
				if(resultado.getInt("count(*)")>0)/*Hay pagos de Ingreso*/
				{
					/*Borrado de los registros de la tabla*/
					count = tablaEg.getRowCount()-1;
					while(count>=0)
					{
						tablaEg.removeRow(count);
						count--;
					}
					resultado = consulta.executeQuery(querydos);
					while(resultado.next())/*Se agregan los datos a la tabla de ingresos*/
					{
						Object [] newRow = {resultado.getString("id"), resultado.getString("fecha"), resultado.getString("descripcion"), resultado.getString("importe"), resultado.getString("login")};
						tablaEg.addRow(newRow);

					}
					
					if(indiOperacion!=4)/*Calcular el la suma total del egreso, cuando no este en busqueda*/
					{
						resultado = consulta.executeQuery("select sum(importe) from egreso;");
						if(resultado.next())/*Para importe*/
						{cSalEg.setText("$"+Double.parseDouble(resultado.getString("sum(importe)")));}
						else
						{JOptionPane.showMessageDialog(this, "Error al tratar de agregar el total de importe de los pagos de Egreso", "Error, en Egreso", 0);}
					}
				}
				else
				{cSalEg.setText(""); cSalEg.setText("$0.0");}
			}
			else
			{JOptionPane.showMessageDialog(this, "Error en select count(*) from egreso;", "Error", 0);}
			fix.close(); consulta.close(); resultado.close();
			/*Generacion de lo que hay en Caja*/
			cTotal.setText("$"+(Double.parseDouble(cSalIn.getText().substring(1, cSalIn.getText().length())) - Double.parseDouble(cSalEg.getText().substring(1, cSalEg.getText().length()))));
		}
		catch(SQLException ex)
		{
			JOptionPane.showMessageDialog(this, "Error al tratar de mostrarTodo, Error: "+ex.getMessage(), "Error en mostrarTodo", 0);
		}
	}
	
	/*Valida el ingreso del registro de ingreso y egreso*/
	public void validarIE()
	{
		try/*Validar campos de Descripcion y de Importe de Ingreso y Egreso*/
		{
			if(!cDescripcion.getText().isEmpty() && !cImporte.getText().isEmpty())/*Si ambos campos no estan vacios*/
			{
				String desc = cDescripcion.getText();
				double impe = Double.parseDouble(cImporte.getText());/*Comvertir el importe*/
				desc.toLowerCase();/*A minusculas*/
				/*Verifica si contiene las palabras Pago y Contrato de acuerdo a cada tipo de operacion*/
				if(indiOperacion==2)/*Agregar un Ingreso*/
				{
					if(desc.contains("contrato") || desc.contains("pago contrato") || desc.contains("pagocontrato") || desc.contains("pago contrato: "))
					{botonGenerar.setEnabled(false);}
					else if(desc.contains("contrato folio:") || desc.contains("cont fol") || desc.contains("contratofolio:") || desc.contains("contrato folio"))
					{botonGenerar.setEnabled(false);}
					else/*Para cuando no lo contenga*/
					{botonGenerar.setEnabled(true);}
				}
				else if(indiOperacion==3)/*Agregar un egreso*/
				{
					if(desc.contains("pago contrato") || desc.contains("pagocontrato") || desc.contains("pago contrato: "))
					{botonGenerar.setEnabled(false);}
					else if(desc.contains("contrato folio:") || desc.contains("cont fol") || desc.contains("contratofolio:") || desc.contains("contrato folio"))
					{botonGenerar.setEnabled(false);}
					else if(desc.contains("ingreso") || desc.contains("ingreso:") || desc.contains("ingre") || desc.contains("ingre:") || desc.contains("ingresito"))
					{botonGenerar.setEnabled(false);}
					else/*Para cuando no lo contenga*/
					{botonGenerar.setEnabled(true);}
				}
			}
			else
			{botonGenerar.setEnabled(false);}
		}
		catch(Exception ex)/*No es correcto el ingreso o Egreso*/
		{botonGenerar.setEnabled(false);}
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		int tipo = keyEvent.getExtendedKeyCode();
		if(tipo==27 && indiOperacion==1)/*ESC*/
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
		else if(tipo==113)/*F2*/
		{indiOperacion=2; operacion();}
		else if(tipo==114)/*F3*/
		{indiOperacion=3; operacion();}
		else if(tipo==115)/*F4*/
		{indiOperacion=4; operacion();}
		else if(tipo==27 && (indiOperacion==2 || indiOperacion==3 || indiOperacion==4))/*Cerrar ventana de operaciones*/
		{wOpciones.setVisible(false); indiOperacion=1;}
		
		if(indiOperacion==2 || indiOperacion==3)/*Habilitar guardado de acuerdo a los valores a registrar sea ingreso o egreso*/
		{validarIE();}
		else if(indiOperacion==4)/*Para cuando se haya se hacer una busqueda*/
		{
			String cBus = cDescripcion.getText();
			queryIngreso = "select * from ingreso where ";
			queryEgreso = "select * from egreso where ";
			String criterios = "id like'%"+cBus+"%' or fecha like'%"+cBus+"%' or descripcion like'%"+cBus+"%' or importe like'%"+cBus+"%' or login like'%"+cBus+"%';";
			queryIngreso+=criterios;
			queryEgreso+=criterios;
			mostrarTodo(queryIngreso, queryEgreso);
		}
	}
	public void keyReleased(KeyEvent keyEvent)
	{
		if(indiOperacion==2 || indiOperacion==3)/*Habilitar guardado de acuerdo a los valores a registrar sea ingreso o egreso*/
		{validarIE();}
		else if(indiOperacion==4)
		{mostrarTodo(queryIngreso, queryEgreso);}
	}
	public void keyTyped(KeyEvent keyEvent)
	{
		if(indiOperacion==2 || indiOperacion==3)/*Habilitar guardado de acuerdo a los valores a registrar sea ingreso o egreso*/
		{validarIE();}
		else if(indiOperacion==4)
		{mostrarTodo(queryIngreso, queryEgreso);}
	}
}