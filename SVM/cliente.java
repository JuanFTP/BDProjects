import java.io.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

class cliente extends JFrame implements KeyListener {
	
	/*Posicionadores de la Ventana*/
	static Dimension pantalla, ventana;

	/*Etiquetas de la ventana*/
	static JLabel titulo, funciones, imagen, id, nombre, apellido1, apellido2;
	static JTextField eId, eNombre, eApellido1, eApellido2;
	/*Elementos de la tabla*/
	static JScrollPane scroll;
	static JTable tablaBase;
	static DefaultTableModel tabla;
	static String [] cabecera = {"ID", "Nombre", "Apellido Paterno", "Apellido Materno", "Saldo Actual"};
	static String [][] datos = {{"0", "N/D", "N/D", "N/D", "0.00"}};

	/*Nueva Barra de Busqueda*/
	static JTextField cBuscar;
	static JButton bBuscar, bVer, bVolver;
	
	/*Posicionadores*/
	int posX = 0, posY = 0;
	/*Controlador de modificacion*/

	/*Objeto de validaciones*/
	static util val = new util();
	/*
	0 = Inicia modificacion
	1 = Ya se tiene un codigo a modificar
	*/
	static int iMod = 0;

	/*Datos de conexion*/
	static enlace conector;
	static Statement consulta;
	static ResultSet resultado;
	
	/*Cadenas de datos de cada campo*/
	String sId, sNombre, sApellido1, sApellido2;

	cliente(){
		new JFrame("Administracion de Clientes"); setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		setSize(680, 540);
		
		titulo = new JLabel("Administracion de Clientes"); titulo.setBounds(posX, posY, 700, 30);
		titulo.setFont(new Font("Arial", Font.PLAIN, 25));
		titulo.setHorizontalAlignment(JLabel.CENTER); add(titulo); posY+=30;
		
		funciones = new JLabel("F1: Abonar        F2: Agregar        F3: Modificar       F4: Eliminar"); funciones.setBounds(posX, posY, 700, 20);
		funciones.setHorizontalAlignment(JLabel.CENTER); add(funciones); posY+=35;
		
		posX+=20;
		imagen = new JLabel(new ImageIcon("data/icon/cliente.png")); imagen.setBounds(posX, posY, 70, 70);
		add(imagen); posX+=75;
			/*Etiquetas*/
			id = new JLabel("ID"); id.setBounds(posX, posY, 50, 20); id.setHorizontalAlignment(JLabel.CENTER); add(id); posX+=55;
			nombre = new JLabel("Nombre(s)"); nombre.setBounds(posX, posY, 160, 20); nombre.setHorizontalAlignment(JLabel.CENTER); add(nombre); posX+=165;
			apellido1 = new JLabel("Apellido Paterno"); apellido1.setBounds(posX, posY, 165, 20); apellido1.setHorizontalAlignment(JLabel.CENTER); add(apellido1); posX+=170;
			apellido2 = new JLabel("Apellido Materno"); apellido2.setBounds(posX, posY, 165, 20); apellido2.setHorizontalAlignment(JLabel.CENTER); add(apellido2);
		posX=95; posY+=25;
			eId = new JTextField(); eId.setBounds(posX, posY, 50, 30); eId.setHorizontalAlignment(JTextField.CENTER); eId.addKeyListener(this); add(eId); posX+=55;
			eNombre = new JTextField(); eNombre.setBounds(posX, posY, 160, 30); eNombre.setHorizontalAlignment(JTextField.CENTER); eNombre.addKeyListener(this); add(eNombre); posX+=165;
			eApellido1 = new JTextField(); eApellido1.setBounds(posX, posY, 165, 30); eApellido1.setHorizontalAlignment(JTextField.CENTER); eApellido1.addKeyListener(this); add(eApellido1); posX+=170;
			eApellido2 = new JTextField(); eApellido2.setBounds(posX, posY, 165, 30); eApellido2.setHorizontalAlignment(JTextField.CENTER); eApellido2.addKeyListener(this); add(eApellido2);
		posX=20; posY+=30;

		/*Seccion de Busqueda*/
		titulo = new JLabel("Busqueda"); titulo.setBounds(posX, posY, 640, 25); titulo.setHorizontalAlignment(JLabel.CENTER); add(titulo);
		posY+=30;
		cBuscar = new JTextField("Ingresa tu Busqueda"); cBuscar.setBounds(posX, posY, 280, 30); cBuscar.setHorizontalAlignment(JTextField.CENTER); cBuscar.addKeyListener(this); add(cBuscar);
		posX+=290;
		bBuscar = new JButton(new ImageIcon("data/icon/buscar.png")); bBuscar.setText(" Buscar"); bBuscar.setBounds(posX, posY,100, 30); bBuscar.setBackground(Color.WHITE); add(bBuscar);
		posX+=110;
		bVer = new JButton(new ImageIcon("data/icon/verTodos.png")); bVer.setText(" Ver Todos"); bVer.setBounds(posX, posY, 120, 30); bVer.setBackground(Color.WHITE); add(bVer);
		posX+=130;
		bVolver = new JButton(new ImageIcon("data/icon/volver.png")); bVolver.setText(" Volver"); bVolver.setBounds(posX, posY, 100, 30); bVolver.setBackground(Color.WHITE); add(bVolver);
		posX=20; posY+=40;

		/*Zona de tabla*/
		tabla = new DefaultTableModel(datos, cabecera);
		tablaBase = new JTable(tabla); tablaBase.getTableHeader().setReorderingAllowed(false);
		/*Ancho de Columna*/
		tablaBase.getColumnModel().getColumn(0).setPreferredWidth(50);
		tablaBase.getColumnModel().getColumn(1).setPreferredWidth(150);
		tablaBase.getColumnModel().getColumn(2).setPreferredWidth(170);
		tablaBase.getColumnModel().getColumn(3).setPreferredWidth(170);
		tablaBase.getColumnModel().getColumn(4).setPreferredWidth(90);

		scroll = new JScrollPane(tablaBase);
		scroll.setBounds(posX, posY, 630, 300);
		add(scroll); posX=530; posY+=310;

		setData("select * from cliente;");

		//Listener del Boton
			bBuscar.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					setData("select * from cliente where id like'%"+cBuscar.getText()+"%' or nombre like'%"+cBuscar.getText()+"%' or apellidoP like'%"+cBuscar.getText()+"%' or apellidoM like'%"+cBuscar.getText()+"%';");
				}
			});

			bVer.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					setData("select * from cliente;");
				}
			});

			bVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					setVisible(false);
					new credito();
				}
			});

		/*Configuracion de la ventana*/
			ventana = getSize();
			pantalla = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((pantalla.width-ventana.width)/2, ((pantalla.height-ventana.height)/2)-50);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);  setVisible(true);
	}

	public void keyPressed(KeyEvent keyEvent){
		int tipo = keyEvent.getExtendedKeyCode();
		System.out.println("Key: "+tipo);
		/*Obtener string*/
		sId = eId.getText();
		sNombre = eNombre.getText();
		sApellido1 = eApellido1.getText();
		sApellido2 = eApellido2.getText();
		if(tipo==112){/*F1: Abonar*/
			if(iMod==0){
				if(sId.isEmpty() && sNombre.isEmpty() && sApellido1.isEmpty() && sApellido2.isEmpty()){
					JOptionPane.showMessageDialog(this, "Completa el campo ID o Nombre, ApellidoP y ApellidoM para poder Abonar.", "Advertencia", 2);
				} else {
					/*Llamar a la funcion de ABonar*/
					int abonoState = 0;
					/* 0: No se Abona Nada
					 * 1: Se Abona con el ID
					 * 2: Se Abona con el Nombre
					 */
					if(!sId.isEmpty()){
						if(val.esEntero(sId, 0)){
							/*Llamar a la funcion*/
							abonoState=1;
						} else {
							JOptionPane.showMessageDialog(this, "Formato de ID no valido.\nID: Entero", "Advertencia", 2);
						}
					} else if(!sNombre.isEmpty() && !sApellido1.isEmpty() && !sApellido2.isEmpty()){
						if(val.esCadena(sNombre) && val.esCadena(sApellido1) && val.esCadena(sApellido2)){
							abonoState=2;
						} else {
							JOptionPane.showMessageDialog(this, "Tipo de Dato No Valido: \nNombre: Texto\nApellidoP: Texto\nApellidoM: Texto", "Advertencia", 2);
						}
					} else {
						JOptionPane.showMessageDialog(this, "Ingresa el ID o en su defecto el:\nNombre, ApellidoP y ApellidoM del cliente\npara poder Abonar", "Advertencia", 2);
					}

					/*Consultar dato de Cliente*/
					if(abonoState!=0){
						try{
							conector = new enlace();
							consulta = conector.getConnection().createStatement();
							resultado = null;

								if(abonoState==1){/*Abono con ID*/
									resultado = consulta.executeQuery("select *, count(*) from cliente where id='"+sId+"';");
								} else if(abonoState==2){/*Abono con Nombre*/
									resultado = consulta.executeQuery("select *, count(*) from cliente where nombre='"+sNombre+"' and apellidoP='"+sApellido1+"' and apellidoM='"+sApellido2+"';");
								}

								if(resultado.next()){
									if(resultado.getInt("count(*)")>0){
										String valueAbonoS = JOptionPane.showInputDialog(this, "Ingresa el monto del Abono:");
										if(valueAbonoS==null){
											valueAbonoS="";
										}
										if(valueAbonoS.isEmpty()){
											JOptionPane.showMessageDialog(this, "Se requiere un Monto.", "Advertencia", 2);
										} else {
											if(val.esDoble(valueAbonoS)){
												double abono = Double.parseDouble(valueAbonoS);
												if(abono>0){
													/*Actualizar abono*/
													if(abono>resultado.getDouble("monto")){
														JOptionPane.showMessageDialog(this, "No se puede Abonar mas de lo que el cliente debe.", "Advertencia", 2);
													} else {
														String idC = resultado.getString("id");
														double montoA = resultado.getDouble("monto");
														consulta.execute("update cliente set monto='"+(montoA-abono)+"' where id='"+idC+"';");
														JOptionPane.showMessageDialog(this, "Abono Registrado Correctamente.", "Mensaje", 1);
														setEmpty();
														setData("select * from cliente;");
													}
												} else {
													JOptionPane.showMessageDialog(this, "No se puede Abonar 0.", "Advertencia", 2);
												}
											} else{
												JOptionPane.showMessageDialog(this, "Tipo de Dato no Compatible: Decimal", "Avertencia", 2);
											}
										}
									} else {
										JOptionPane.showMessageDialog(this, "El Cliente no Existe.", "Advertencia", 2);
									}
								} else {
									JOptionPane.showMessageDialog(this, "Sin Resultados: SQL Consult Abono.", "Advertencia", 2);
								}

							consulta.close();
							resultado.close();
						} catch(Exception ex){
							JOptionPane.showMessageDialog(this, "ERROR: SQL Consult : Abono\n"+ex.getMessage(), "ERROR", 0);
						}
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Deshabilita la Opcion de Modificar antes\npara poder Usar esta Funcion.", "Advertencia", 2);
			}
		} else if(tipo==113){/*F2: Opcion de Agregar*/
			if(iMod==0){
				/*Validar Vacios*/
				if(sNombre.isEmpty() || sApellido1.isEmpty() || sApellido2.isEmpty()){
					JOptionPane.showMessageDialog(this, "Completa los campos de Nombre, Apellido Paterno y Apellido Materno", "Advertencia", 2);
				} else {
					if(val.esCadena(sNombre) && val.esCadena(sApellido1) && val.esCadena(sApellido2)){
						try{
							conector = new enlace();
							consulta = enlace.getConnection().createStatement();
							resultado = consulta.executeQuery("select count(*) from cliente where nombre='"+sNombre+"' and apellidoP='"+sApellido1+"' and apellidoM='"+sApellido2+"';");
							if(resultado.next()){
								if(resultado.getInt("count(*)")>0){
									JOptionPane.showMessageDialog(this, "Este Cliente ya se Encuentra Registrado.", "Advertencia", 2);
								} else {
									/*Confirmar Insercion*/
									if(JOptionPane.showConfirmDialog(this, "Agregar cliente: "+sNombre+"?", "Agregar Cliente", JOptionPane.YES_NO_OPTION)==0)
									{	
										consulta.execute("insert into cliente values(null, '"+sNombre+"', '"+sApellido1+"', '"+sApellido2+"', 0);");
										JOptionPane.showMessageDialog(this, "Cliente agregado correctamente", "Mensaje", 1);
										setData("select * from cliente;");
										eId.requestFocus();
										setEmpty();
									}
								}
							} else {
								JOptionPane.showMessageDialog(this, "Sin Resultados :D", "Mensaje", 1);
							}
							consulta.close();
							resultado.close();
						}catch(Exception ex){
							JOptionPane.showMessageDialog(this, "ERROR: SQL Insert: "+ex.getMessage(), "Error", 0);
						}
					} else{
						JOptionPane.showMessageDialog(this, "Tipo de Dato no Permitido:\nNombre: Texto\nApellidoP: Texto\nApellidoM: Texto", "Advertencia", 2);
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Deshabilita la Opcion de Modificar antes\npara poder Usar esta Funcion.", "Advertencia", 2);
			}
		} else if(tipo==114){/*Opcion de Modificar*/
			if(iMod==0){
				/*Activar funcion de modificar*/
				if(sId.isEmpty()){
					JOptionPane.showMessageDialog(this, "Se requiere un ID para poder Modificar un Cliente.", "Advertencia", 2);
				} else {
					/*Obtener datos del cliente*/
					if(val.esEntero(sId, 0)){
						try{
							conector = new enlace();
							consulta =  conector.getConnection().createStatement();
							resultado = consulta.executeQuery("select  *, count(*) from cliente where id = '"+sId+"';");

							if(resultado.next()){
								if(resultado.getInt("count(*)")>0){
									eId.setEditable(false); eId.setEnabled(false);
									eNombre.setText(resultado.getString("nombre"));
									eApellido1.setText(resultado.getString("apellidoP"));
									eApellido2.setText(resultado.getString("apellidoM"));
									iMod=1;
									funciones.setText("F1: Abonar        F2: Agregar        F3: Guardar       F4: Eliminar        F5: Cancelar");
								}else {
									JOptionPane.showMessageDialog(this, "No existen un cliente con la ID:\n"+sId, "Advertencia", 1);								
								}
							} else {
								JOptionPane.showMessageDialog(this, "ERROR: Consulta de ID: "+sId, "Error", 0);
							}
							consulta.close();
							resultado.close();
						} catch(Exception ex){
							JOptionPane.showMessageDialog(this, "Error al recuperar el Cliente: \n"+ex.getMessage(), "Error", 0);
						}
					} else {
						JOptionPane.showMessageDialog(this, "Formato de Identificardor no valido.\nID: Entero", "Error", 0);
					}
				}
			} else if(iMod==1){
				/*Guardar cambios*/
				if(sNombre.isEmpty() || sApellido1.isEmpty() || sApellido2.isEmpty()){
					JOptionPane.showMessageDialog(this, "Completa los campos: Nombre, Apellido Paterno y Apellido Materno.", "Advertencia", 2);
				} else {
					/*Revisar tipos de datos*/
					if(val.esCadena(sNombre) && val.esCadena(sApellido1) && val.esCadena(sApellido2)){
						/*Revisar existencia para actualizar*/
						try{
							conector = new enlace();
							consulta = conector.getConnection().createStatement();
							resultado = consulta.executeQuery("select count(*) from cliente where nombre='"+sNombre+"' and apellidoP='"+sApellido1+"' and apellidoM='"+sApellido2+"' and id<>'"+sId+"';");
							if(resultado.next()){
								if(resultado.getInt("count(*)")>0){
									JOptionPane.showMessageDialog(this, "Este cliente ya se encuentra Registrado.", "Advertencia", 2);
								} else{
									/*Ejecutar Actualizacion*/
									if(JOptionPane.showConfirmDialog(this, "Actualizar Cliente?", "Confirmar", JOptionPane.YES_NO_OPTION)==0){
										consulta.execute("update cliente set nombre='"+sNombre+"', apellidoP='"+sApellido1+"', apellidoM='"+sApellido2+"' where id='"+sId+"';");
										JOptionPane.showMessageDialog(this, "Actulizacion Completa", "Mensaje", 1);
										setData("select * from cliente;");
										setEmpty();
									}
								}
							} else {
								JOptionPane.showMessageDialog(this, "Error: Consulta de Existencia de Cliente.", "Advertencia", 2);
							}
							consulta.close();
							resultado.close();
						}catch(Exception ex){
							JOptionPane.showMessageDialog(this, "ERROR: SQL Update: "+ex.getMessage(), "Error", 0);
						}
					} else {
						JOptionPane.showMessageDialog(this, "Tipo de Dato no Permitido:\nNombre: Texto\nApellidoP: Texto\nApellidoM: Texto", "Advertencia", 2);
					}

				}
			}
		} else if(tipo==115){/*F4 : Funcion de eliminar*/
			/*Eliminar y considerar el Modificar*/
			if(sId.isEmpty()){
				JOptionPane.showMessageDialog(this, "Se requiere un ID para eliminar un Cliente.", "Advertencia", 0);
			} else {
				if(val.esEntero(sId, 0)){
					/*Confirmar para eliminar*/
					try {
						/*Obtener la cantidad de datos*/
						conector = new enlace();
						consulta = enlace.getConnection().createStatement();
						resultado =  consulta.executeQuery("select *, count(*) from cliente where id = '"+sId+"';");
						if(resultado.next()){
							if(resultado.getInt("count(*)")==1){
								if(resultado.getDouble("monto")>0){
									JOptionPane.showMessageDialog(this, "ERROR: El Cliente no se puede eliminar ya que\naun tiene un monto de credito pendiente.", "ERROR", 0);
								} else {
									if(JOptionPane.showConfirmDialog(this, "Eliminar cliente: "+sId+"?", "Eliminar cliente", JOptionPane.YES_NO_OPTION)==0)
									{
										consulta.execute("delete from cliente where id = '"+sId+"';");
										JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente", "Mensaje", 1);
										setData("select * from cliente;");
										setEmpty();
									}
								}
							} else {
								JOptionPane.showMessageDialog(this, "El ID del cliente, NO existe.\nIngresa uno valido.", "Advertencia", 2);
							}
						} else {
							JOptionPane.showMessageDialog(this, "Sin Resultados :D", "Mensaje", 1);
						}
						consulta.close();
						resultado.close();
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(this, "ERROR: SQL Insert: "+ex.getMessage(), "Error", 0);
					}
				} else {
					JOptionPane.showMessageDialog(this, "Formato de Codigo Incorrecto\n123456", "Advertencia", 0);
				}
			}
		} else if(tipo==116){/*F5 : Funcion de cancelar Modificacion*/
			if(iMod==1){
				setEmpty();
			}
		}
		
	}

	/*Funcion con GUI para Abonar
	public double getAbono(String nombre){
		valueAbono=-10;
		JFrame abonoF = new JFrame("Abonar");
		abonoF.getContentPane().setBackground(Color.WHITE);
		abonoF.setLayout(null);
		abonoF.setSize(300, 180);
		abonoF.setResizable(false);
		abonoF.setLocation(((pantalla.width-ventana.width)/2)+125, ((pantalla.height-ventana.height)/2)+100);
		abonoF.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JLabel title = new JLabel("Abonar a: "+nombre);
		title.setBounds(0, 10, 300, 20);
		title.setHorizontalAlignment(JLabel.CENTER);
		abonoF.add(title);

		JTextField cValor = new JTextField();
		cValor.setBounds(20, 35, 260, 50);
		cValor.setFont(new Font("Arial", Font.PLAIN, 30));
		cValor.setForeground(new Color(0, 0, 128));
		cValor.setHorizontalAlignment(JTextField.CENTER);
		abonoF.add(cValor);

		JButton bAbonar = new JButton("Abonar");
		bAbonar.setBackground(Color.WHITE);
		bAbonar.setBounds(20, 90, 120, 30);
		abonoF.add(bAbonar);

		JButton bCancelar = new JButton("Cancelar");
		bCancelar.setBackground(Color.WHITE);
		bCancelar.setBounds(160, 90, 120, 30);
		abonoF.add(bCancelar);

		abonoF.setVisible(true);

		//Acciones de los botones
		bAbonar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String sValor = cValor.getText();
				if(sValor.isEmpty()){
					JOptionPane.showMessageDialog(getContentPane(), "Se requiere un valor.", "Advertencia", 2);
				} else {
					if(val.esDoble(sValor)){
						valueAbono = Double.parseDouble(sValor);
						abonoF.setVisible(false);
					} else {
						JOptionPane.showMessageDialog(getContentPane(), "Formato no compatible: Valor Numerico Decimal", "Advertencia", 2);
					}
				}
			}
		});

		bCancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				valueAbono=-1;
				abonoF.setVisible(false);
			}
		});
		return valueAbono;
	}*/

	/*Metodo para vaciar los campos*/
	public void setEmpty(){
		funciones.setText("F1: Abonar        F2: Agregar        F3: Modificar       F4: Eliminar");
		iMod=0;
		eId.setEditable(true); eId.setEnabled(true); eId.requestFocus();
		eId.setText(""); eNombre.setText("");
		eApellido1.setText(""); eApellido2.setText("");
		
	}

	public void keyReleased(KeyEvent keyEvent){}
	public void keyTyped(KeyEvent keyEvent){}

	/*Mostrar datos en la tabla se acuerdo a una consulta especifica*/
	public void setData(String query){
		try {
			/*Obtener la cantidad de datos*/
			conector = new enlace();
			consulta = enlace.getConnection().createStatement();
			resultado =  consulta.executeQuery(query);
			
			/*Llenar la tabla con los datos*/
			removeData(); /*Limpiar tabla*/
				while(resultado.next()){
					Object newRow [] = {
						resultado.getInt("id"),
						resultado.getString("nombre"),
						resultado.getString("apellidoP"),
						resultado.getString("apellidoM"),
						resultado.getDouble("monto")
					};
					tabla.addRow(newRow);
				}
			consulta.close();
			resultado.close();
		} catch(Exception ex){ JOptionPane.showMessageDialog(this, "Error: "+query+"\n"+ex.getMessage(), "Error", 0); }
	}

	/*Metodo de eliminacion de contenido de la tabla*/
	public void removeData(){
		int nRow = tabla.getRowCount()-1;
		while(nRow>=0) {
			tabla.removeRow(nRow);
			nRow--;
		}
	}

	public static void main(String[] args) {
		new cliente();
	}
}