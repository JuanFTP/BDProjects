import java.io.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

class credito extends JFrame implements KeyListener {

	/*Posicionadores de la Ventana*/
	static Dimension pantalla, ventana;
	
	/*Etiquetas de la ventana*/
	static JLabel titulo, funcionesSup, funcionesInf, imagen;

		/*Datos del cliente*/
		static JLabel id, nombre, apellido1, apellido2;
		static JTextField eId, eNombre, eApellido1, eApellido2;

		/*Datos del producto*/
		static JLabel codigo, nombreP, precio, cantidad;
		static JTextField eCod, eNombreP, ePrecio, eCantidad;

		/*Datos de salida*/
		static JLabel salida, imgIZQ, imgDER;
		static JTextField eSalida, lineColor;

	/*Botones*/
	static JButton bBuscar, bCambiar, bAgregar;

	/*Datos de conexion*/
	static enlace conector;
	static Statement consulta;
	static ResultSet resultado;

	/*Elementos de la tabla*/
	static JScrollPane scroll;
	static JTable tablaBase;
	static JButton bVolver;
	static DefaultTableModel tabla;
	static String [] cabecera = {"Codigo", "Producto", "Precio U.", "Cantidad", "Importe"};
	static String [] rowDefault = {"0", "N/D", "N/D", "N/D", "0.00"};
	static String [][] datos = {rowDefault};
	
	/*Posicionadores*/
	int posX = 0, posY = 0;
	
	/*Indicador de Estado*/
	static int state = 0, stateSearch = 0, nCantidad = 0;
	/* state
	 * 0 indica que no se ha seleccionado un cliente
	 * 1 indica que ya se selecciono un cliente y ahora se cargar? productos
	 * stateSearch
	 * 0 indica que no se ha buscado un producto
	 * 1 indica que ya se tiene fijado un producto
	 */	

	/*Objeto de validaciones*/
	static util val = new util();

	/*Vector de Datos de Cliente*/
	static String [] dataClient = new String[4];

	/*Formato para los numeros*/
	DecimalFormat dF = new DecimalFormat("######.##");

	/*Para la ventana de vCobro*/
	static JFrame vCobro;
	static JTextField cSubTotal;
	static JTextField cEfectivo;
	static JTextField cTotal;
	static JTextField cCambio;
	static JButton bCobrar;


	credito(){
		new JFrame("Administracion de Productos"); setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		setSize(680, 600);

		/*Imagenes IZQ y DER*/
		imgIZQ = new JLabel(new ImageIcon("data/wall/titulo.png")); imgIZQ.setBounds(10, 10, 150, 100); add(imgIZQ);
		imgDER = new JLabel(new ImageIcon("data/wall/sedesol.png")); imgDER.setBounds(500, 10, 150, 100); add(imgDER);
		posX=155;
		
		titulo = new JLabel("Sistema de Credito"); titulo.setBounds(posX, posY, 350, 30);
		titulo.setFont(new Font("Arial", Font.PLAIN, 25));
		titulo.setHorizontalAlignment(JLabel.CENTER); add(titulo); posY+=30;
		
		funcionesSup = new JLabel("F2: Administrar Clientes        F3: Adminstrar Productos"); funcionesSup.setBounds(posX, posY, 350, 20);
		funcionesSup.setHorizontalAlignment(JLabel.CENTER); add(funcionesSup); posY+=20;

		funcionesInf = new JLabel("\nF4: Seleccionar Cliente"); funcionesInf.setBounds(posX, posY, 350, 20);
		funcionesInf.setHorizontalAlignment(JLabel.CENTER); add(funcionesInf); posY+=40;
		
		posX+=20;
		titulo = new JLabel("Datos del Cliente"); titulo.setBounds(posX, posY, 350, 25);
		titulo.setForeground(new Color(217, 0, 0));
		titulo.setHorizontalAlignment(JLabel.CENTER);
		titulo.setFont(new Font("Arial", Font.PLAIN, 17));
		add(titulo); posY+=25;

		posX=20;
		imagen = new JLabel(new ImageIcon("data/icon/cliente.png")); imagen.setBounds(posX, posY, 70, 70);
		add(imagen); posX+=75;
		/*Seccion de Cliente*/

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
		posX=20; posY+=50;

		/*Seccion de producto*/
		titulo = new JLabel("Datos del Producto"); titulo.setBounds(posX, posY, 680, 25);
		titulo.setForeground(new Color(217, 0, 0));
		titulo.setHorizontalAlignment(JLabel.CENTER);
		titulo.setFont(new Font("Arial", Font.PLAIN, 17));
		add(titulo); posY+=25;

		imagen = new JLabel(new ImageIcon("data/icon/producto.png")); imagen.setBounds(posX, posY, 70, 70);
		add(imagen); posX+=75;

			/*Etiquetas*/
			codigo = new JLabel("COD"); codigo.setBounds(posX, posY, 110, 20); codigo.setHorizontalAlignment(JLabel.CENTER); add(codigo); posX+=115;
			nombreP = new JLabel("Nombre"); nombreP.setBounds(posX, posY, 135, 20); nombreP.setHorizontalAlignment(JLabel.CENTER); add(nombreP); posX+=140;
			precio = new JLabel("Precio"); precio.setBounds(posX, posY, 95, 20); precio.setHorizontalAlignment(JLabel.CENTER); add(precio); posX+=100;
			cantidad = new JLabel("Cantidad"); cantidad.setBounds(posX, posY, 90, 20); cantidad.setHorizontalAlignment(JLabel.CENTER); add(cantidad); posX+=95;
		posX=95; posY+=25;
			eCod = new JTextField(); eCod.setBounds(posX, posY, 110, 30); eCod.setHorizontalAlignment(JTextField.CENTER); eCod.addKeyListener(this); add(eCod); posX+=115;
			eNombreP = new JTextField(); eNombreP.setBounds(posX, posY, 135, 30); eNombreP.setHorizontalAlignment(JTextField.CENTER); eNombreP.addKeyListener(this); add(eNombreP); posX+=140;
			ePrecio = new JTextField(); ePrecio.setBounds(posX, posY, 95, 30); ePrecio.setHorizontalAlignment(JTextField.CENTER); ePrecio.addKeyListener(this); add(ePrecio); posX+=100;
			eCantidad =  new JTextField(); eCantidad.setBounds(posX, posY, 90, 30); eCantidad.setHorizontalAlignment(JTextField.CENTER);  eCantidad.addKeyListener(this);add(eCantidad); posX+=95;
			bBuscar = new JButton(new ImageIcon("data/icon/buscar.png")); bBuscar.setBackground(Color.WHITE); bBuscar.setBounds(posX, posY, 30, 30); add(bBuscar); posX+=35;
			bCambiar = new JButton(new ImageIcon("data/icon/cancelar.png")); bCambiar.setBackground(Color.WHITE); bCambiar.setBounds(posX, posY, 30, 30); add(bCambiar); posX+=35;
			bAgregar = new JButton(new ImageIcon("data/icon/nuevo.png")); bAgregar.setBackground(Color.WHITE); bAgregar.setBounds(posX, posY, 30, 30); add(bAgregar); posX+=35;
		posX=20; posY+=60;

		/*Zona de tabla*/
		tabla = new DefaultTableModel(datos, cabecera);
		tablaBase = new JTable(tabla); tablaBase.getTableHeader().setReorderingAllowed(false);
		/*Ancho de Columna*/
		tablaBase.getColumnModel().getColumn(0).setPreferredWidth(100);
		tablaBase.getColumnModel().getColumn(1).setPreferredWidth(200);
		tablaBase.getColumnModel().getColumn(2).setPreferredWidth(120);
		tablaBase.getColumnModel().getColumn(3).setPreferredWidth(80);
		tablaBase.getColumnModel().getColumn(4).setPreferredWidth(130);

		scroll = new JScrollPane(tablaBase);
		scroll.setBounds(posX, posY, 630, 200);
		add(scroll);

		posX=20; posY+=210;

		/*Boton de volver*/
		bVolver = new JButton(new ImageIcon("data/icon/salir.png"));
		bVolver.setBackground(Color.WHITE);
		bVolver.setText(" Salir"); bVolver.setBounds(posX, posY, 120, 50); add(bVolver);
		posX+=320;	

	 	salida = new JLabel("Total:"); salida.setHorizontalAlignment(JLabel.CENTER); salida.setBounds(posX, posY, 100, 50);
	 	salida.setFont(new Font("Arial", Font.PLAIN, 25));
	 	add(salida); posX+=110;

	 	eSalida = new JTextField("00.00"); eSalida.setHorizontalAlignment(JTextField.CENTER); eSalida.setBounds(posX, posY, 200, 50);
	 	eSalida.setEditable(false); eSalida.setBackground(Color.WHITE);
	 	eSalida.setFont(new Font("Arial", Font.PLAIN, 30)); eSalida.setForeground(new Color(0, 0, 128));
	 	add(eSalida); posX=0; posY+=60;

	 	lineColor = new JTextField(); lineColor.setBackground(Color.GREEN); lineColor.setBounds(posX, posY, 680, 40); 
	 	lineColor.setEditable(false);
	 	add(lineColor);

	 	/*Estado por defecto*/
	 	setProductEnabled(false, 1);

	 	/*Listener*/
		 	bVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if(JOptionPane.showConfirmDialog(getContentPane(), "Deseas Salir?", "Confirmar", JOptionPane.YES_NO_OPTION)==0){
						System.exit(0);
					}
				}
			});

			bBuscar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae){
					/*Acciones de busqueda de un producto*/		
					String codigo = eCod.getText();
					if(codigo.isEmpty()){
						JOptionPane.showMessageDialog(getContentPane(), "Se requiere un codigo de un producto para buscar.", "Advertencia", 2);
					} else {
						if(val.esEntero(codigo, 6)){
							/*Buscar producto*/
							try{
								conector = new enlace();
								consulta = conector.getConnection().createStatement();
								resultado = consulta.executeQuery("select *, count(*) from producto where codigo='"+codigo+"';");
								if(resultado.next()){
									if(resultado.getInt("count(*)")>0){
										/*Verificar existencia*/
										if(resultado.getInt("existencia")>0){
											/*Obtener datos*/
											nCantidad = resultado.getInt("existencia");
											eNombreP.setText(resultado.getString("nombre"));
											ePrecio.setText(resultado.getString("precio"));
											eCod.setEditable(false);
											bBuscar.setEnabled(false);
											bCambiar.setEnabled(true);
											bAgregar.setEnabled(true);
											eCantidad.requestFocus(true);
											stateSearch=1;
										} else {
											JOptionPane.showMessageDialog(getContentPane(), "Este producto se ha agotado", "Advertencia", 2);
										}
									} else {
										JOptionPane.showMessageDialog(getContentPane(), "El producto: "+codigo+" no existe.", "Advertencia", 2);
									}
								} else{
									JOptionPane.showMessageDialog(getContentPane(), "Sin resultados: SQL Query Search", "Mensaje", 1);
								}
								consulta.close();
								resultado.close();
							}catch(Exception ex){
								JOptionPane.showMessageDialog(getContentPane(), "ERROR: SQL Consult - Producto\n"+ex.getMessage(), "Error", 0);
							}
						} else {
							JOptionPane.showMessageDialog(getContentPane(), "Formato de codigo incorrecto\nEntero (6)", "Advertencia", 2);
						}
					}
				}
			});

			bCambiar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae){
					setProductEnabled(true, 2);
				}
			});
			
			bAgregar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae){
					String codigo = eCod.getText(), nombre = eNombreP.getText(), precio = ePrecio.getText(), cantidad = eCantidad.getText();
					if(cantidad.isEmpty()){
						JOptionPane.showMessageDialog(getContentPane(), "Se requiere una cantidad.", "Advertencia", 2);
					} else {
						if(val.esEntero(cantidad, 0)){
							if(Integer.parseInt(cantidad)>0){
								if(Integer.parseInt(cantidad)<nCantidad || Integer.parseInt(cantidad)==nCantidad){
									if((Integer.parseInt(cantidad)<(nCantidad - duplicadoAcum(codigo))) || (Integer.parseInt(cantidad)==(nCantidad - duplicadoAcum(codigo)))){
										/*Agregar producto*/
										Object [] newRow = {codigo, nombre, precio, cantidad, dF.format(Double.parseDouble(precio) * Double.parseDouble(cantidad))};
										tabla.addRow(newRow);
										setTotal();
										setProductEnabled(true, 2);
										eCod.requestFocus(true);
									} else {
										JOptionPane.showMessageDialog(getContentPane(), "Existencia limitada: Quedan = "+(nCantidad - duplicadoAcum(codigo))+" Solicita: "+cantidad, "Advertencia", 2);	
									}
								} else {
									JOptionPane.showMessageDialog(getContentPane(), "Existencia limitada: Hay = "+nCantidad+" Solicita: "+cantidad, "Advertencia", 2);
								}
							} else {
								JOptionPane.showMessageDialog(getContentPane(), "La cantidad debe ser mayor a 0", "Advertencia", 2);
							}
						} else {
							JOptionPane.showMessageDialog(getContentPane(), "Formato de Cantidad no conecta:\nCantidad (Entero).", "Advertencia", 2);
						}
					}
				}
			});

		/*Configuracion de la ventana*/
			ventana=getSize();
			pantalla=Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((pantalla.width-ventana.width)/2, ((pantalla.height-ventana.height)/2));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);  setVisible(true);
	}

	public void keyReleased(KeyEvent keyEvent){}
	public void keyPressed(KeyEvent keyEvent){
		int tipo = keyEvent.getExtendedKeyCode();
		/*Obtener datos de Cliente*/
		dataClient[0] = eId.getText();
		dataClient[1] = eNombre.getText();
		dataClient[2] = eApellido1.getText();
		dataClient[3] = eApellido2.getText();

		System.out.println("Type: "+tipo);
		if(tipo==27)/*ESC*/{
			if(state==0){
				if(JOptionPane.showConfirmDialog(this, "Salir del sistema?", "Confirmar", JOptionPane.YES_NO_OPTION)==0){
					System.exit(0);
				}
			} else {
				if(JOptionPane.showConfirmDialog(this, "Cancelar venta y Salir?", "Confirmar", JOptionPane.YES_NO_OPTION)==0){
					setEmpty();
					System.exit(0);
				}
			}
		} else if(tipo==113)/*F2: Administrar Clientes*/{
			if(state==0){
				setVisible(false); new cliente();
			} else {
				if(JOptionPane.showConfirmDialog(this, "Cancelar venta?", "Confirmar", JOptionPane.YES_NO_OPTION)==0){
					setEmpty();
					setVisible(false); new cliente();
				}
			}
		} else if(tipo==114)/*F3: Administrar Productos*/{
			if(state==0){
				setVisible(false); new producto();
			} else{
				if(JOptionPane.showConfirmDialog(this, "Cancelar venta?", "Confirmar", JOptionPane.YES_NO_OPTION)==0){
					setEmpty();
					setVisible(false); new producto();
				}
			}
		}else if(tipo==115)/*F4*: Seleccionar cliente*/{
			if(state==0){/*Si no se ha seleccionado un cliente*/
				if(!dataClient[0].isEmpty()){/*Caso seleccion por id*/
					/*Verificar Tipo de Dato*/
					if(val.esEntero(dataClient[0], 0)){
						/*Busqueda del Cliente por ID*/
						seleccionarCliente(1);
					} else {
						JOptionPane.showMessageDialog(this, "Tipo de Dato no admitido en ID (Entero)", "Advertencia", 2);
					}
				} else if(!(dataClient[1].isEmpty() || dataClient[2].isEmpty() || dataClient[3].isEmpty())){/*Caso seleccion por Nombre*/
					/*Validar tipos*/
					if(val.esCadena(dataClient[1]) && val.esCadena(dataClient[2]) && val.esCadena(dataClient[3])){
						/*Buscar Cliente por nombre y apellidos*/
						seleccionarCliente(2);
					} else {
						JOptionPane.showMessageDialog(this, "Tipo de Dato no admitido en\nNombre (s): (Texto)\nApellido Paterno: (Texto)\nApellido Materno: (Texto)", "Advertencia", 2);	
					}
				} else {
					JOptionPane.showMessageDialog(this, "Se requiere el ID del cliente o el\nNombre, ApellidoP y ApellidoM para\npoder seleccionarlo.", "Advertencia", 2);
				}
			}
		}
		else if(tipo==116)/*F5: Guardar Una venta*/{
			if(state==1){
				if(Double.parseDouble(eSalida.getText())>0){
					vCobro = new JFrame("Cobrar"); vCobro.setSize(450, 345);
					vCobro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); vCobro.setResizable(false);
					vCobro.getContentPane().setBackground(Color.WHITE); vCobro.setLayout(null);

					posX=20; posY=20;

					JLabel head = new JLabel("Completar Venta");
					head.setFont(new Font("Arial", Font.PLAIN, 25));
					head.setBounds(posX, posY, 410, 30);
					head.setHorizontalAlignment(JLabel.CENTER); vCobro.add(head);
					posY+=45;

					JLabel subTotal = new JLabel("Importe:");
					subTotal.setHorizontalAlignment(JLabel.CENTER);
					subTotal.setBounds(posX, posY, 100, 40);
					vCobro.add(subTotal);
					posY+=45;

					JLabel efectivo = new JLabel("Efectivo:");
					efectivo.setHorizontalAlignment(JLabel.CENTER);
					efectivo.setBounds(posX, posY, 100, 40);
					vCobro.add(efectivo);
					posY+=45;

					JLabel total = new JLabel("Total:");
					total.setHorizontalAlignment(JLabel.CENTER);
					total.setBounds(posX, posY, 100, 40);
					vCobro.add(total);
					posY+=45;

					JLabel cambio = new JLabel("Cambio:");
					cambio.setHorizontalAlignment(JLabel.CENTER);
					cambio.setBounds(posX, posY, 100, 40);
					vCobro.add(cambio);
					posY=55;
					posX+=110;

					cSubTotal = new JTextField(eSalida.getText());
					cSubTotal.setHorizontalAlignment(JLabel.CENTER);
					cSubTotal.setEditable(false);
					cSubTotal.setFont(new Font("Arial", Font.PLAIN, 30)); cSubTotal.setForeground(new Color(0, 0, 128));
					cSubTotal.setBounds(posX, posY, 300, 40);
					vCobro.add(cSubTotal);
					posY+=45;

					cEfectivo = new JTextField("0.00");
					cEfectivo.setHorizontalAlignment(JTextField.CENTER);
					cEfectivo.setFont(new Font("Arial", Font.PLAIN, 30));
					cEfectivo.setForeground(new Color(0, 0, 128));
					cEfectivo.setBounds(posX, posY, 300, 40);
					vCobro.add(cEfectivo);
					posY+=45;

					cTotal = new JTextField(eSalida.getText());
					cTotal.setEditable(false);
					cTotal.setFont(new Font("Arial", Font.PLAIN, 30));
					cTotal.setForeground(new Color(0, 0, 128));
					cTotal.setHorizontalAlignment(JTextField.CENTER);
					cTotal.setBounds(posX, posY, 300, 40);
					vCobro.add(cTotal);
					posY+=45;

					cCambio = new JTextField("0.00");
					cCambio.setEditable(false);
					cCambio.setFont(new Font("Arial", Font.PLAIN, 30));
					cCambio.setForeground(new Color(0, 0, 128));
					cCambio.setHorizontalAlignment(JTextField.CENTER);
					cCambio.setBounds(posX, posY, 300, 40);
					vCobro.add(cCambio);
					posY+=50;
					posX=20;

					bCobrar = new JButton(new ImageIcon("data/icon/cobrar.png"));
					bCobrar.setBackground(Color.WHITE);
					bCobrar.setBounds(posX, posY, 410, 50);
					bCobrar.setText(" Cobrar");
					vCobro.add(bCobrar);

					vCobro.setLocation(((pantalla.width-ventana.width)/2)+125, ((pantalla.height-ventana.height)/2)+100);
					vCobro.setVisible(true);

					cEfectivo.requestFocus(true);

					cEfectivo.addKeyListener(new KeyListener(){
						public void keyPressed(KeyEvent kE){}
						public void keyReleased(KeyEvent kE){
							if(!cEfectivo.getText().isEmpty()){
								if(val.esDoble(cEfectivo.getText())) {
									double sub = Double.parseDouble(cSubTotal.getText());
									double efe = Double.parseDouble(cEfectivo.getText());
									double sT = 0;
									if(efe<sub){
										sT = sub-efe;
										cTotal.setText(dF.format(sT));
										cCambio.setText("0.00");
										bCobrar.setEnabled(true);
									} else if(efe>sub){
										cTotal.setText("0.00");
										sT = efe-sub;
										cCambio.setText(dF.format(sT));
										bCobrar.setEnabled(true);
									}
								} else {
									bCobrar.setEnabled(false);
								}
							} else {
								bCobrar.setEnabled(false);
							}
						}
						public void keyTyped(KeyEvent kE){}
					});

					bCobrar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae){
							if(Double.parseDouble(cTotal.getText())==0){/*Actualizar productos sin cambio en cliente*/
								try{
									conector = new enlace();
									consulta = conector.getConnection().createStatement();
									int nRow = tabla.getRowCount()-1, cProduct=0;
									double cCredito = 0;
										while(nRow>=0) {
											resultado = consulta.executeQuery("select * from producto where codigo='"+tabla.getValueAt(nRow, 0).toString()+"'");
											if(resultado.next()){
												cProduct = resultado.getInt("existencia");
												consulta.execute("update producto set existencia='"+(cProduct - (Integer.parseInt(tabla.getValueAt(nRow, 3).toString())))+"' where codigo='"+tabla.getValueAt(nRow, 0)+"';");
											} else {
												JOptionPane.showMessageDialog(vCobro.getContentPane(), "ERROR: SQL Consult Producto.", "ERROR", 0);
											}
											//Decrementar fila
											nRow--;
										}
									consulta.close();
									resultado.close();

									//Confirmacion
									JOptionPane.showMessageDialog(vCobro.getContentPane(), "Compra completada Correctamente!!", "Mensaje", 1);
									vCobro.setVisible(false);
									setEmpty();
								} catch(Exception ex){
									JOptionPane.showMessageDialog(vCobro.getContentPane(), "ERROR: SQL End Venta:\n"+ex.getMessage(), "ERROR", 2);
								}
							} else {/*Actualizar ambos tanto productos como cliente*/
								try{
									conector = new enlace();
									consulta = conector.getConnection().createStatement();
									int nRow = tabla.getRowCount()-1, cProduct=0;
									double cCredito = 0;
										while(nRow>=0) {
											resultado = consulta.executeQuery("select * from producto where codigo='"+tabla.getValueAt(nRow, 0).toString()+"'");
											if(resultado.next()){
												cProduct = resultado.getInt("existencia");
												consulta.execute("update producto set existencia='"+(cProduct - (Integer.parseInt(tabla.getValueAt(nRow, 3).toString())))+"' where codigo='"+tabla.getValueAt(nRow, 0)+"';");
											} else {
												JOptionPane.showMessageDialog(vCobro.getContentPane(), "ERROR: SQL Consult Producto.", "ERROR", 0);
											}
											//Decrementar fila
											nRow--;
										}
										//Actualizar valor del cliente en credito

										resultado = consulta.executeQuery("select * from cliente where id='"+eId.getText()+"';");
										if(resultado.next()){
											cCredito = resultado.getDouble("monto");
											consulta.execute("update cliente set monto='"+(cCredito + (Double.parseDouble(cTotal.getText())))+"' where id='"+eId.getText()+"';");
										} else {
											JOptionPane.showMessageDialog(vCobro.getContentPane(), "No se ha encontrado el cliente.", "Advertencia", 2);
										}
									consulta.close();
									resultado.close();

									//Confirmacion
									JOptionPane.showMessageDialog(vCobro.getContentPane(), "Compra completada Correctamente!!", "Mensaje", 1);
									vCobro.setVisible(false);
									setEmpty();
								} catch(Exception ex){
									JOptionPane.showMessageDialog(vCobro.getContentPane(), "ERROR: SQL End Venta:\n"+ex.getMessage(), "ERROR", 2);
								}
							}
						}
					});
				} else {
					JOptionPane.showMessageDialog(this, "Agrega al menos un producto para completar la venta.", "Advertencia", 2);
				}
			}
		}
		else if (tipo==117) /*F6: Cancelar Venta*/{
			if(state==1){
				if(JOptionPane.showConfirmDialog(this, "Cancelar venta?", "Confirmar", JOptionPane.YES_NO_OPTION)==0){
					setEmpty();
				}
			}
		}
	}
	public void keyTyped(KeyEvent keyEvent){}

	/*Buscar duplicado en tabla*/
	public int duplicadoAcum(String cod){
		int ex = 0;
		int nRow = tabla.getRowCount()-1;
		while(nRow>=0) {
			if(tabla.getValueAt(nRow, 0).equals(cod)){
				ex+= Integer.parseInt(tabla.getValueAt(nRow, 3).toString());
			}
			nRow--;
		}
		return ex;
	}

	/*Generar un total*/
	public void setTotal(){
		double ex = 0;
		int nRow = tabla.getRowCount()-1;
		while(nRow>=0) {
			ex+= Double.parseDouble(tabla.getValueAt(nRow, 4).toString());
			nRow--;
		}
		eSalida.setText(dF.format(ex));
		System.out.println(ex+"");
	}

	/*Metodo para buscar un cliente*/
	public void seleccionarCliente(int modo){
		try{
			conector = new enlace();
			consulta = conector.getConnection().createStatement();
			resultado=null;
			if(modo==1){/*Buscar por ID*/
				resultado = consulta.executeQuery("select *, count(*) from cliente where id='"+dataClient[0]+"';");
			} else{/*Buscar por Nombre*/
				resultado = consulta.executeQuery("select *, count(*) from cliente where nombre='"+dataClient[1]+"' and apellidoP='"+dataClient[2]+"' and apellidoM='"+dataClient[3]+"';");
			}
			/*Obtener datos*/
			if(resultado.next()){
				if(resultado.getInt("count(*)")>0){
					/*Habilitar Funcion de acuerdo al resultado*/
					eId.setText(resultado.getString("id"));
					eNombre.setText(resultado.getString("nombre"));
					eApellido1.setText(resultado.getString("apellidoP"));
					eApellido2.setText(resultado.getString("apellidoM"));
					setClientEnabled(false, 2);
					setProductEnabled(true, 2);
					eCod.requestFocus(true);
					removeData();
					funcionesInf.setText("\nF5: Guardar Venta        F6: Cancelar Venta");;
					state=1;/*Ya se ha seleccionado un cliente*/
				} else{
					JOptionPane.showMessageDialog(this, "El cliente no Existe.", "Advertencia", 2);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Sin Resultados: SQL Consult", "Advertencia", 2);
			}
			consulta.close();
			resultado.close();
		} catch(Exception ex){
			JOptionPane.showMessageDialog(this, "ERROR: SQL Consult\n"+ex.getMessage(), "ERROR", 0);
		}
	}

	/*Reinicia todos los datos*/
	public void setEmpty(){
		funcionesInf.setText("\nF4: Seleccionar Cliente");;
		state=0;
		stateSearch=0;
		eSalida.setText("00.00");
		setClientEnabled(true, 1);
		setProductEnabled(false, 1);
		eId.requestFocus(true);
		/*Limpiar tabla*/
		removeData();
		tabla.addRow((Object[])rowDefault);
	}

	/*Para habilitar campos del cliente*/
	public void setClientEnabled(boolean stateLocal, int modo){
		/* Modo =1: Significa que esta deshabilitado por completo
		 * Modo =2: Significa que ya se valido al cliente y se procede a agregar productos 
		 */

		if(modo==1){
			eId.setText(""); eNombre.setText(""); eApellido1.setText(""); eApellido2.setText("");
			eId.setEditable(stateLocal); eNombre.setEditable(stateLocal); eApellido1.setEditable(stateLocal); eApellido2.setEditable(stateLocal);
		} else if(modo==2){
			eId.setEditable(stateLocal); eNombre.setEditable(stateLocal); eApellido1.setEditable(stateLocal); eApellido2.setEditable(stateLocal);
		}
	}

	
	/*Metodos de habilitacion y deshabilitacion de operaciones*/
	public void setProductEnabled(boolean stateLocal, int modo){
		/* Modo = 1: Significa que esta deshabilitado por completo.
		 * Modo = 2: Significa que esta habilitado para agregar productos.
		 */
		if(modo==1){
			eCod.setEditable(stateLocal); 
			eNombreP.setEditable(stateLocal);
			ePrecio.setEditable(stateLocal);
			eCantidad.setEditable(stateLocal);
			bBuscar.setEnabled(stateLocal);
			bCambiar.setEnabled(stateLocal);
			bAgregar.setEnabled(stateLocal);
			setEmptyProduct();
		} else if(modo==2){
			eCod.setEditable(stateLocal);
			eCantidad.setEditable(stateLocal);
			bBuscar.setEnabled(stateLocal);
			setEmptyProduct();
			stateSearch=0;
			bCambiar.setEnabled(false);
			bAgregar.setEnabled(false);
		}
	}

	public void setEmptyProduct(){
		eCod.setText("");
		eNombreP.setText("");
		ePrecio.setText("");
		eCantidad.setText("");
	}	

	/*Metodo de eliminacion de contenido de la tabla*/
	public void removeData(){
		int nRow = tabla.getRowCount()-1;
		while(nRow>=0) {
			tabla.removeRow(nRow);
			nRow--;
		}
	}
	
	public static void main (String[] args) {
		new credito();
	}
}