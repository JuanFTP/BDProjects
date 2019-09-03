import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import javax.swing.event.*;

class puntoCobro extends JFrame implements KeyListener
{
	public Dimension pantallaF, ventanaF;
	/*Variables para consulta*/
	enlace conector;
	Connection fix;
	Statement consulta;
	ResultSet resultado;
	/*Variables para cosulta*/
	static int tamVX, tamVY, posX, posY, tamX, tamY, estadoInicial=0, ventanaAct=0;/*Estado 0 es en espera, 1 se busca por folio, 2 se busca por nombre*//*Ventana 0 para la principal 1 para la busqueda*/
	/*Botones y Etiquetas*/
	static JLabel etiquetaOpciones, eTitulo, eFolio, eNombre, eApellidos;
	static JTextField iBusqueda, iFolio, iNombre, iApellidos;
	static JTextArea aFolio, salidaGeneral;
	static JButton buscarFolio, buscarNombre, limpiar;
	static String [] columnas = {"Folio Pago", "A_o Pagado", "Importe", "Fecha Pago", "Folio Contrato", "ID_Cliente", "Login"};
	static Object [][] lineaDefault = {{"---", "---", "---", "---", "---", "---", "---"}};
	static JScrollPane barra, barra2;
	static DefaultTableModel tabla, tabla2;
	static JTable tablaC, tablaC2;
	/*Elementos para la parte derecha*/
	static JLabel eTipo, eEstado, eFecha, eA_o, ePagoUni, eImporte, eNumPagos;
	static JTextField cTipo, cEstado, cFecha, cA_o, cPagoUni, cImporte, cNumPagos; 
	static JComboBox cFolios;
	static JButton seleccionar, ejecutar, cambiar;
	static JFrame wPago;
	/*Permisos*/
	public static String userUpper;
	public static String permisosUpper;
	/*Frame*/
	puntoCobro(String userUpper, String permisosUpper)
	{
		/*Usuario del sistema*/
		this.userUpper=userUpper;
		this.permisosUpper=permisosUpper;
		tamVX=1000; tamVY=700; getContentPane().setBackground(Color.WHITE);
		new JFrame(); setTitle("Punto de Cobro"); setLayout(null);
		/*Titulo*/
		tamX=100; tamY=20; posX=0; posY=0;
		etiquetaOpciones=new JLabel("F2: Buscar Pagos            Usuario: "+userUpper+"            (ESC) Salir");
		etiquetaOpciones.setForeground(new Color(137, 135, 132)); etiquetaOpciones.setBounds(0, 5, tamVX, tamY); add(etiquetaOpciones);
		etiquetaOpciones.setHorizontalAlignment(JLabel.CENTER);
		posY+=tamY+5;
		eTitulo = new JLabel("Punto de Cobro"); eTitulo.setBounds(0, posY, tamVX, tamY*2); add(eTitulo); eTitulo.setHorizontalAlignment(JLabel.CENTER);
		eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(200, 34, 67));
		posY+=tamY+10;
		/*Titulo*/

		posX = 20; tamX=150;
		eFolio = new JLabel("Folio"); eFolio.setBounds(posX, posY, 50, tamY); add(eFolio); eFolio.setHorizontalAlignment(JLabel.CENTER); posX+=50+10;
		eNombre = new JLabel("Nombre"); eNombre.setBounds(posX, posY, tamX, tamY); add(eNombre); eNombre.setHorizontalAlignment(JLabel.CENTER); posX+=tamX+10;
		eApellidos = new JLabel("Apellidos"); eApellidos.setBounds(posX, posY, tamX, tamY); add(eApellidos); eApellidos.setHorizontalAlignment(JLabel.CENTER); posX=20;
		posY+=tamY+5;
		iFolio = new JTextField(); iFolio.setBounds(posX, posY, 50, tamY); add(iFolio); iFolio.setHorizontalAlignment(JTextField.CENTER); posX+=50+10; iFolio.addKeyListener(this);
		iNombre = new JTextField(); iNombre.setBounds(posX, posY, tamX, tamY); add(iNombre); iNombre.setHorizontalAlignment(JTextField.CENTER); posX+=tamX+10; iNombre.addKeyListener(this);
		iApellidos = new JTextField(); iApellidos.setBounds(posX, posY, tamX, tamY); add(iApellidos); iApellidos.setHorizontalAlignment(JTextField.CENTER); posX+=tamX; iApellidos.addKeyListener(this);
		posY+=tamY+10;
		/*Botones*/
		buscarFolio = new JButton(new ImageIcon("data/oper/search.png")); buscarFolio.setText(" BFolio"); buscarFolio.setBounds(20, posY, posX/4, tamY); buscarFolio.setBackground(Color.WHITE); add(buscarFolio);
		buscarNombre = new JButton(new ImageIcon("data/oper/search_2.png")); buscarNombre.setText(" BNombre"); buscarNombre.setBounds(posX/4+25, posY, posX/4+20, tamY); buscarNombre.setBackground(Color.WHITE); add(buscarNombre);
		limpiar = new JButton(new ImageIcon("data/oper/clean.png")); limpiar.setText(" Limpiar"); limpiar.setBounds(posX/2+50, posY, posX/2-50, tamY); limpiar.setBackground(Color.WHITE); add(limpiar); posY+=tamY+5;
		/*Botones*/
		salidaGeneral = new JTextArea(); salidaGeneral.setBounds(20, posY, posX-20, tamY*4); salidaGeneral.setBackground(new Color(235, 235, 235)); add(salidaGeneral); salidaGeneral.setEditable(false); salidaGeneral.addKeyListener(this);
		salidaGeneral.setText("Introduce el Folio del contrato a pagar o bien ingresa el nombre\ndel usuario para conocer sus contratos, y asi elegir cual contrato\npagar.");
		
		/*Estructura derecha del pago -- SOBREESCRIBIENDO LOS DATOS*/int respY = posY; int respX = posX + 40;
		posY = 80; posX+=40; tamX=145;
		eFolio = new JLabel("Folio"); eFolio.setBounds(posX, posY, tamX-50, tamY); add(eFolio); eFolio.setHorizontalAlignment(JLabel.CENTER);
			cFolios = new JComboBox(); cFolios.setBounds(posX, posY+tamY, tamX-50, tamY); cFolios.setBackground(Color.WHITE); add(cFolios); cFolios.addItem("FContrato"); cFolios.setEnabled(false);
		posX+=(tamX-50)+10;
		eTipo = new JLabel("Tipo"); eTipo.setBounds(posX, posY, tamX, tamY); eTipo.setHorizontalAlignment(JLabel.CENTER); add(eTipo);
			cTipo = new JTextField(); cTipo.setBounds(posX, posY+tamY, tamX, tamY); cTipo.setHorizontalAlignment(JTextField.CENTER); add(cTipo); cTipo.setEditable(false);
		posX+=tamX+5;
		eEstado = new JLabel("Estado"); eEstado.setBounds(posX, posY, tamX, tamY); eEstado.setHorizontalAlignment(JLabel.CENTER); add(eEstado);
			cEstado = new JTextField(); cEstado.setBounds(posX, posY+tamY, tamX, tamY); cEstado.setHorizontalAlignment(JTextField.CENTER); add(cEstado); cEstado.setEditable(false);
		posX+=tamX+5;
		eFecha = new JLabel("Fecha"); eFecha.setBounds(posX, posY, tamX, tamY); eFecha.setHorizontalAlignment(JLabel.CENTER); add(eFecha);
			cFecha = new JTextField(); cFecha.setBounds(posX, posY+tamY, tamX, tamY); cFecha.setHorizontalAlignment(JTextField.CENTER); add(cFecha); cFecha.setEditable(false);
		
		posX=respX; posY+=(tamY*2)+10; tamX = 133;
		eA_o = new JLabel("A_o a Pagar"); eA_o.setBounds(posX, posY, tamX, tamY); eA_o.setHorizontalAlignment(JLabel.CENTER); add(eA_o);
			cA_o = new JTextField(); cA_o.setBounds(posX, posY+tamY, tamX, tamY); cA_o.setHorizontalAlignment(JTextField.CENTER); add(cA_o); cA_o.setEditable(false);
		posX+=tamX+5;
		ePagoUni = new JLabel("Pago Unitario"); ePagoUni.setBounds(posX, posY, tamX, tamY); ePagoUni.setHorizontalAlignment(JLabel.CENTER); add(ePagoUni);
			cPagoUni = new JTextField(); cPagoUni.setBounds(posX, posY+tamY, tamX, tamY); cPagoUni.setHorizontalAlignment(JTextField.CENTER); add(cPagoUni); cPagoUni.setEditable(false);
		posX+=tamX+5;
		eNumPagos = new JLabel("Numero Pagos"); eNumPagos.setBounds(posX, posY, tamX, tamY); eNumPagos.setHorizontalAlignment(JLabel.CENTER); add(eNumPagos);
			cNumPagos = new JTextField(); cNumPagos.setBounds(posX, posY+tamY, tamX, tamY); cNumPagos.setHorizontalAlignment(JTextField.CENTER); add(cNumPagos); cNumPagos.setEditable(false);
		posX+=tamX+5;
		eImporte = new JLabel("Importe"); eImporte.setBounds(posX, posY, tamX, tamY); eImporte.setHorizontalAlignment(JLabel.CENTER); add(eImporte);
			cImporte = new JTextField(); cImporte.setBounds(posX, posY+tamY, tamX, tamY); cImporte.setHorizontalAlignment(JTextField.CENTER); add(cImporte); cImporte.setEditable(false);

		posX=respX; posY+=(tamY*2)+10; tamX = 133;
		seleccionar = new JButton(new ImageIcon("data/oper/seleccionar.png")); seleccionar.setText(" Seleccionar"); seleccionar.setBackground(Color.WHITE); seleccionar.setBounds(posX, posY, tamX, tamY); add(seleccionar); seleccionar.setHorizontalAlignment(JButton.CENTER);
		seleccionar.setEnabled(false); posX+=tamX+10;
		ejecutar = new JButton(new ImageIcon("data/oper/save_3.png")); ejecutar.setText(" Generar"); ejecutar.setBackground(Color.WHITE); ejecutar.setBounds(posX, posY, tamX, tamY); add(ejecutar); ejecutar.setHorizontalAlignment(JButton.CENTER);
		ejecutar.setEnabled(false); posX+=tamX+10;
		cambiar = new JButton(new ImageIcon("data/oper/cambiar.png")); cambiar.setText(" Cambiar"); cambiar.setBackground(Color.WHITE); cambiar.setBounds(posX, posY, tamX, tamY); add(cambiar); cambiar.setHorizontalAlignment(JButton.CENTER);
		cambiar.setEnabled(false);
		cTipo.addKeyListener(this); cEstado.addKeyListener(this); cFecha.addKeyListener(this); cA_o.addKeyListener(this); cPagoUni.addKeyListener(this); cImporte.addKeyListener(this); cNumPagos.addKeyListener(this); cFolios.addKeyListener(this);
		/*Estructura derecha del pago*/

		/*Tabla*/ posY = respY; tamX=100;
		posX=10; posY+=(tamY*4)+10;
		eTitulo = new JLabel("Historia General de Todos los Pagos Realizados"); eTitulo.setBounds(0, posY, tamVX, tamY*2); add(eTitulo); eTitulo.setHorizontalAlignment(JLabel.CENTER);
		eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(200, 34, 67)); posY+=tamY*2;
		tabla = new DefaultTableModel(lineaDefault, columnas);
		tablaC =  new JTable(tabla); barra = new JScrollPane(tablaC); tablaC.addKeyListener(this);
		barra.setBounds(posX, posY, tamVX-25, 395); add(barra);	
		mostrarResultados();
		/*Tabla*/
		/*Buscar contrato por folio*/
		buscarFolio.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(!iFolio.getText().isEmpty())/*Si el campo folio está vacio*/
				{
					if(estadoInicial==0)//No se ha iniciado ninguna busqueda
					{
						try
						{
							Integer.parseInt(iFolio.getText());
							/*Validar existencia del folio*/
							try
							{
								conector = new enlace(); fix = conector.getConnection();
								consulta = fix.createStatement();
								resultado = consulta.executeQuery("select count(*), folio, tipoContrato, estado, fecha from contrato where folio="+iFolio.getText()+";");
								if(resultado.next())
								{
									if(resultado.getInt("count(*)")==1)
									{
										cFolios.removeAllItems(); cFolios.addItem(resultado.getString("folio"));
										cTipo.setText(resultado.getString("tipoContrato")); cEstado.setText(resultado.getString("estado"));
										cFecha.setText(resultado.getString("fecha"));
										cA_o.setEditable(true); cNumPagos.setText("1");;
										estadoInicial=1;/*Se buscara por folio*/
										/*Calculo del pago*/
										String pago = resultado.getString("tipoContrato");
										if(pago.equals("Local"))
										{cPagoUni.setText("50");}
										else if(pago.equals("Externo"))
										{cPagoUni.setText("300");}
										else if(pago.equals("Superior"))
										{cPagoUni.setText("600");}
										/*Calculo del pago*/
										limpiar.setBackground(new Color(49, 238, 238));
										iFolio.setEditable(false); iNombre.setEditable(false); iApellidos.setEditable(false); buscarNombre.setEnabled(false); buscarFolio.setEnabled(false);
									}
									else
									{JOptionPane.showMessageDialog(getContentPane(), "Folio de Contrato NO EXISTE", "Folio Inexistente", 0);}
								}
								else
								{JOptionPane.showMessageDialog(getContentPane(), "Error al buscar el Folio: count(*)", "SQL Folio Error", 0);}
								fix.close(); consulta.close(); resultado.close();
							}
							catch(SQLException ex)
							{JOptionPane.showMessageDialog(getContentPane(), "Error al buscar el Folio\nError: "+ex.getMessage(), "SQL Folio Error", 0);}
						}
						catch(Exception ex){JOptionPane.showMessageDialog(getContentPane(), "El folio es un numero Entero\nError: "+ex.getMessage(), "Folio no valido", 2);}
					}
				}
				else{JOptionPane.showMessageDialog(getContentPane(), "Ingresa un folio para iniciar la busqueda", "Ingresa un Folio", 2);}
			}
		}
		);
		/*Buscar por nombre y apellidos del usuario*/
		buscarNombre.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(!iNombre.getText().isEmpty() && !iApellidos.getText().isEmpty())/*Si el campo nombre está vacio*/
				{
					if(estadoInicial==0)//Si no se ha habilitado ningun tipo de busqueda
					{
						try
						{
							conector = new enlace(); fix = conector.getConnection();
							consulta = fix.createStatement();
							resultado = consulta.executeQuery("select count(id), id from usuario where nombre='"+iNombre.getText()+"' and apellidos='"+iApellidos.getText()+"';");//Se busca el id del usuario
							if(resultado.next())//Si hay algun resultado
							{
								if(resultado.getInt("count(id)")==1)//Si existe el usuario
								{
									resultado = consulta.executeQuery("select count(folio), folio, id from contrato where id="+resultado.getString("id")+";");
									if(resultado.next())//Si hay resultado
									{
										if(resultado.getInt("count(folio)")>0)
										{
											if(resultado.getInt("count(folio)")==1)//Si solo tiene un contrato
											{
												resultado = consulta.executeQuery("select folio, tipoContrato, estado, fecha from contrato where folio="+resultado.getString("folio")+";");
												if(resultado.next())
												{
													cFolios.removeAllItems(); cFolios.addItem(resultado.getString("folio")); ejecutar.setEnabled(true);
													cTipo.setText(resultado.getString("tipoContrato")); cEstado.setText(resultado.getString("estado"));
													cFecha.setText(resultado.getString("fecha"));
													cA_o.setEditable(true); cNumPagos.setText("1"); cNumPagos.setEditable(false);
													/*Calculo del pago*/
													String pago = resultado.getString("tipoContrato");
													if(pago.equals("Local")){cPagoUni.setText("50");}
													else if(pago.equals("Externo")){cPagoUni.setText("300");}
													else if(pago.equals("Superior")){cPagoUni.setText("600");}
													/*Calculo del pago*/
													limpiar.setBackground(new Color(49, 238, 238));
													iFolio.setEditable(false); iNombre.setEditable(false); iApellidos.setEditable(false); buscarNombre.setEnabled(false); buscarFolio.setEnabled(false);
												}
												else
												{JOptionPane.showMessageDialog(getContentPane(), "Error al obtener los datos de un solo contrato cliente.", "Error en nombre.consulta.Uno", 0);}
											}
											else//si hay mas de uno
											{
												resultado = consulta.executeQuery("select folio from contrato where id="+resultado.getString("id")+";");
												if(resultado.next())//Si hay algun resultado
												{
													cFolios.removeAllItems();
													do{cFolios.addItem(resultado.getString("folio"));}while(resultado.next());
													seleccionar.setEnabled(true);
													cFolios.setEnabled(true); limpiar.setBackground(new Color(49, 238, 238));
												}
												else
												{JOptionPane.showMessageDialog(getContentPane(), "Error al tratar de anadir mas contratos en la lista del cliente.", "Error en mas de un contrato select", 2);}
											}
											estadoInicial=2;//Estado en busqueda de nombre
											iFolio.setEditable(false); iNombre.setEditable(false); iApellidos.setEditable(false); buscarNombre.setEnabled(false); buscarFolio.setEnabled(false);		
										}
										else
										{JOptionPane.showMessageDialog(getContentPane(), "No hay Contratos Activos para este Cliente.", "No hay Contratos", 2);}
									}
									else
									{JOptionPane.showMessageDialog(getContentPane(), "Error al obtener los contratos del cliente", "Error en consulta Folios", 0);}
								}
								else
								{JOptionPane.showMessageDialog(getContentPane(), "El Cliente NO EXISTE", "Cliente no EXISTE", 0);}
							}
							else
							{JOptionPane.showMessageDialog(getContentPane(), "Error al buscar por Nombre\nError: Select id", "SQL Nombre Select", 0);}
							fix.close(); consulta.close(); resultado.close();
						}
						catch(SQLException ex)
						{JOptionPane.showMessageDialog(getContentPane(), "Error al buscar por Nombre\nError: "+ex.getMessage(), "SQL Nombre Error", 0);}
					}
				}
				else{JOptionPane.showMessageDialog(getContentPane(), "Completa los campos: Nombre y Apellidos para iniciar la busqueda.", "Completa los campos", 2);}
			}
		}
		);
		/*Limpia los campos*/
		limpiar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				iFolio.setText(""); iNombre.setText(""); iApellidos.setText(""); estadoInicial=0;
				iFolio.setEditable(true); iNombre.setEditable(true); iApellidos.setEditable(true);
				buscarFolio.setEnabled(true); buscarNombre.setEnabled(true);
				cA_o.setEditable(false);
				cFolios.setEnabled(false); seleccionar.setEnabled(false); ejecutar.setEnabled(false); cambiar.setEnabled(false);
				cTipo.setText("");cEstado.setText("");cFecha.setText("");cA_o.setText("");cPagoUni.setText("");cNumPagos.setText("");cImporte.setText("");
				cFolios.removeAllItems();
				cFolios.addItem("FContrato");
				limpiar.setBackground(Color.WHITE);
			}
		}
		);

		/*Botones extras*/
		seleccionar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(estadoInicial==2)//Se busco por nombre
				{
					try//Genera la extraccion de los datos en consulta
					{
						conector = new enlace(); fix = conector.getConnection();
						consulta = fix.createStatement();
						resultado = consulta.executeQuery("select tipoContrato, estado, fecha, id from contrato where folio = "+cFolios.getSelectedItem()+";");
						if(resultado.next())//Si hay algun resultado de la consulta del contrato respecto al folio
						{
							String tipoC = resultado.getString("tipoContrato");
							if(tipoC.equals("Local"))
							{cPagoUni.setText("50");}
							if(tipoC.equals("Externo"))
							{cPagoUni.setText("300");}
							if(tipoC.equals("Superior"))
							{cPagoUni.setText("600");}
							seleccionar.setEnabled(false); cambiar.setEnabled(true); cFolios.setEnabled(false);
							cA_o.setEditable(true); cNumPagos.setText("1"); cNumPagos.setEditable(false);
							cTipo.setText(resultado.getString("tipoContrato")); cEstado.setText(resultado.getString("estado")); cFecha.setText(resultado.getString("fecha"));
						}
						fix.close(); consulta.close(); resultado.close();
					}
					catch(SQLException ex)
					{JOptionPane.showMessageDialog(getContentPane(), "Error en la consulta de algun Folio de contrato.", "SQL Error: Seleccionar", 2);}
				}
			}
		}
		);

		ejecutar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				/*Se ejecutará el cobro de un contrato*/
				if(!cA_o.getText().isEmpty() && !cNumPagos.getText().isEmpty())//Verifica si no estan vacios los campos
				{
					/*Se ha de verificar todo lo que se ha de hacer para agregar*/
					try
					{
						conector = new enlace(); fix = conector.getConnection();
						consulta = fix.createStatement();
						resultado = consulta.executeQuery("select id  from contrato where folio="+cFolios.getSelectedItem());
						if(resultado.next())
						{
							/*Se guarda la insercion pero primero se verifica que el pago no haya sido relizado ya.*/
							String insert = "insert into pago (descripcion, importe, fechaP, folioC, id, login) values('"+cA_o.getText()+"', "+cImporte.getText()+", curdate(), "+cFolios.getSelectedItem()+", "+resultado.getString("id")+", '"+userUpper+"');";
							resultado = consulta.executeQuery("select count(*) from pago where descripcion='"+cA_o.getText()+"' and folioC="+String.valueOf(cFolios.getSelectedItem())+" and id = "+resultado.getString("id")+";");
							if(resultado.next())
							{
								if(resultado.getInt("count(*)")>0)
								{/*Ya existe el año de pago*/JOptionPane.showMessageDialog(getContentPane(), "El a_o (year): "+cA_o.getText()+"\nYa fue pagado para este contrato.", "A_o Pagado", 1);}
								else
								{
									if(JOptionPane.showConfirmDialog(getContentPane(), "Deseas contunar?", "Continuar", JOptionPane.YES_NO_OPTION)==0)/*Si se desea continuar*/
									{
										consulta.executeUpdate(insert);
										/*Insertar Ingreso*/
										/*insert into ingreso (fecha, descripcion, importe, login) values (curdate(), "Pago Contrato: 2", 600, "usuario");*/
										String insertIngreso = "insert into ingreso (fecha, descripcion, importe, login) values (curdate(), 'Pago Contrato: "+String.valueOf(cFolios.getSelectedItem())+"', "+cImporte.getText()+", '"+userUpper+"');";
										consulta.executeUpdate(insertIngreso);/*Se registra el Ingreso*/
										mostrarResultados();
										JOptionPane.showMessageDialog(getContentPane(),"Pago realizado correctamente. :)", "Pago Completado", 1);
										cA_o.setText(""); ejecutar.setEnabled(false); cImporte.setText("");
									}
								}
							}
							else
							{JOptionPane.showMessageDialog(getContentPane(), "Error al generar el Insert:\n"+insert, "Error en el Insert", 2);}
						}
						else
						{JOptionPane.showMessageDialog(getContentPane(), "Error al obtener el ID del usuario respecto al folio.", "Error en getID()", 2);}
						fix.close(); consulta.close(); resultado.close();
					}
					catch(SQLException ex)
					{JOptionPane.showMessageDialog(getContentPane(), "Error al tratar de agregar algun Pago de contrato.\nError: "+ex.getMessage(), "SQL Error Agregar", 2);}
				}
				else
				{JOptionPane.showMessageDialog(getContentPane(), "Completa los campos A_o, Numero de Pagos", "Campos Requeridos", 2);}
			}
		}
		);

		cambiar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				/*Ya que solo se habilita en caso de haber mas de un folio, pues no se considera ningun cambio extra*/
				cA_o.setEditable(false); cNumPagos.setEditable(false);
				cFolios.setEnabled(true); seleccionar.setEnabled(true); ejecutar.setEnabled(false); cambiar.setEnabled(false);
				cTipo.setText("");cEstado.setText("");cFecha.setText("");cA_o.setText("");cPagoUni.setText("");cNumPagos.setText("");cImporte.setText("");
			}
		}
		);

		/*Posicionadores y Operacion*/
		setSize(tamVX, tamVY); setVisible(true); setResizable(false);
		ventanaF=getSize(); pantallaF=Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((pantallaF.width-ventanaF.width)/2, 0);
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addKeyListener(this);//Escucha eventos de todo el frame
	}
	
	/*Pantalla de buscar Pago*/
	public void buscarPago()
	{
		tamVX = 1000; tamVY = 500; tamX = 100; tamY = 30; posX = 0; posY = 5;
		wPago = new JFrame("Buscar Pago"); wPago.getContentPane().setBackground(Color.WHITE); wPago.setLayout(null); 
		eTitulo = new JLabel("Buscador de Pagos Relizados"); eTitulo.setFont(new Font("Roboto", Font.PLAIN, 20)); eTitulo.setForeground(new Color(200, 34, 67)); eTitulo.setHorizontalAlignment(JLabel.CENTER);
		eTitulo.setBounds(posX, posY, tamVX, tamY);
		posY+=tamY; tamY=20; posX=10;
		etiquetaOpciones = new JLabel("Ingresa tu busqueda");
		etiquetaOpciones.setBounds(posX, posY, tamX*4, tamY);
		salidaGeneral = new JTextArea("Usuario: "+userUpper+"\nPresiona ENTER para buscar, presiona ESC para salir"); salidaGeneral.setEditable(false); salidaGeneral.setBackground(new Color(200, 200, 200));
		salidaGeneral.setBounds((posX*2)+(tamX*4), posY, tamX*6-40, tamY*2);
		posY+=tamY;	
		iBusqueda = new JTextField(); iBusqueda.setHorizontalAlignment(JTextField.CENTER);
		iBusqueda.setBounds(posX, posY, tamX*4, tamY);
		posY+=tamY+5;
		/*Tabla*/
		tabla2 = new DefaultTableModel(lineaDefault, columnas);
		tablaC2 = new JTable(tabla2);
		barra2 = new JScrollPane(tablaC2);
		barra2.setBounds(posX, posY, tamVX-(posX+15), tamVY-(posY+tamY*2));
		/*Tabla*/
		wPago.add(eTitulo); wPago.add(etiquetaOpciones); wPago.add(iBusqueda); wPago.add(barra2); wPago.add(salidaGeneral);
		iBusqueda.addKeyListener(this); tablaC2.addKeyListener(this);
		buscar();
		/*Posicionadores y Operacion*/
		wPago.setSize(tamVX, tamVY); wPago.setVisible(true); wPago.setResizable(false);
		ventanaF=getSize(); pantallaF=Toolkit.getDefaultToolkit().getScreenSize();
	    wPago.setLocation((pantallaF.width-ventanaF.width)/2, 0);
	    wPago.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	/*Dibujar Lineas que dividiran el contenido*/
	public void paint(Graphics g)
	{
		/*Cuadro para la primera parte*/
		super.paint(g);
		g.setColor(new Color(128, 128, 128));
		g.drawLine(15, 75, 400, 75); g.drawLine(400, 75, 400, 250); g.drawLine(15, 250, 400, 250); g.drawLine(15, 75, 15, 250);
	}

	public void mostrarResultados()
	{
		try
		{	
			/*Borrado de todos los registros de la tabla para poder actualizarlos de nuevo*/
			int count = tabla.getRowCount() - 1;
			while(count>=0)
			{
				tabla.removeRow(count);
				count--;
			}

			//System.out.println(numRows);
			conector = new enlace(); fix = conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery("select count(*) from pago;");
			if(resultado.next())
			{
				if(resultado.getInt("count(*)")>0)/*Si hay algun registro*/
				{
					/*Punto de retorno*/
					resultado=null;
					resultado = consulta.executeQuery("select * from pago;");
					//tabla.removeRow(0); /*Borra la fila por defecto*/
					while(resultado.next())
					{
						Object [] row = {resultado.getString("folio"), resultado.getString("descripcion"), resultado.getString("importe"), resultado.getString("fechaP"),  resultado.getString("folioC"), resultado.getString("id"), resultado.getString("login")};
						tabla.addRow(row);/*Añade nueva fila*/
					}
				}
			}
			fix.close(); consulta.close(); resultado.close();
		}
		catch(Exception ex)
		{JOptionPane.showMessageDialog(this, "Error al Vaciar los resultados\n"+"Error: "+ex.getMessage(), "Error", 0);}
	}

	/*Metodo para buscar los pagos*/
	public void buscar()
	{
		try
		{	
			/*Borrado de todos los registros de la tabla para poder actualizarlos de nuevo*/
			int count = tabla2.getRowCount() - 1;
			while(count>=0)
			{
				tabla2.removeRow(count);
				count--;
			}

			String inputBuscar = iBusqueda.getText();
			String query = "select * from pago where ";			
			query+="folio like'%"+inputBuscar+"%' or descripcion like'%"+inputBuscar+"%' or ";
			query+="importe like'%"+inputBuscar+"%' or fechaP like'%"+inputBuscar+"%' or ";
			query+="folioC like'%"+inputBuscar+"%' or id like'%"+inputBuscar+"%' or login like'%"+inputBuscar+"%';";
			conector = new enlace(); fix = conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery("select count(*) from pago;");/*Se ejecuta la busqueda*/
			if(resultado.next())
			{
				if(resultado.getInt("count(*)")>0)/*Si hay algun registro*/
				{
					/*Punto de retorno*/
					resultado=null;
					resultado = consulta.executeQuery(query);
					while(resultado.next())
					{
						Object [] row = {resultado.getString("folio"), resultado.getString("descripcion"), resultado.getString("importe"), resultado.getString("fechaP"),  resultado.getString("folioC"), resultado.getString("id"), resultado.getString("login")};
						tabla2.addRow(row);/*Añade nueva fila*/
					}
				}
			}
			fix.close(); consulta.close(); resultado.close();
		}
		catch(Exception ex)
		{JOptionPane.showMessageDialog(this, "Error al Vaciar los resultados\n"+"Error: "+ex.getMessage(), "Error", 0);}
	}


	//Calcula el importe por tecleo del contrato
	public void calImporte()
	{
		try
		{
			if(!cA_o.getText().isEmpty())//Si no esta vacio el num pago
			{
				if(cA_o.getText().length()==4)//El a_o es de tipo AAAA debe de 
				{
					Integer.parseInt(cA_o.getText());
					cImporte.setText("");//Reinicio del campo
					cImporte.setText(Integer.parseInt(cPagoUni.getText())*1+"");
					ejecutar.setEnabled(true);
				}
				else
				{cImporte.setText("A_o = AAAA"); ejecutar.setEnabled(false);}
			}
			else
			{cImporte.setText(""); ejecutar.setEnabled(false);}
		}
		catch(Exception ex)
		{cImporte.setText("A_o no Valido."); ejecutar.setEnabled(false);}
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		int tipo = keyEvent.getExtendedKeyCode();
		if(tipo == 113)//F2 Buscar pago
		{
			buscarPago();
			ventanaAct = 1; /*Se activo la ventana de buscar pago*/
		}
		else if(tipo==27 && ventanaAct == 0)//ESC, para salir
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
		else if(tipo == 27 && ventanaAct == 1)/*Ventana de busqueda*/
		{
			wPago.setVisible(false);
			ventanaAct=0;
		}
		else if(ventanaAct==1)/*Para buscar en la tabla*/
		{buscar();}
		else/*Calculo de importe por tecleo*/
		{
			/*Calculo de importe por tecleo*/
			if(estadoInicial>0){calImporte();}
		}
	}
	public void keyReleased(KeyEvent keyEvent)
	{
		if(estadoInicial>0){calImporte();}
		if(ventanaAct==1){buscar();}
	}
    public void keyTyped(KeyEvent keyEvent)
    {
    	if(estadoInicial>0){calImporte();}
		if(ventanaAct==1){buscar();}
    }
}