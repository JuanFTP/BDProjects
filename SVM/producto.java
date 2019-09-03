import java.io.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

class producto extends JFrame implements KeyListener {

	/*Posicionadores de la Ventana*/
	static Dimension pantalla, ventana;
	
	/*Etiquetas de la ventana*/
	static JLabel titulo, funciones, imagen, codigo, nombre, descripcion, existencia, precio;
	static JTextField eCod, eNombre, eDescripcion, eExistencia, ePrecio;
	/*Elementos de la tabla*/
	static JScrollPane scroll;
	static JTable tablaBase;
	static DefaultTableModel tabla;
	static String [] cabecera = {"Codigo", "Nombre", "Descripcion", "Existencia", "Precio"};
	static Object [][] datos = {{"0", "N/D", "N/D", "N/D", "0.00"}};

	/*Datos de conexion*/
	static enlace conector;
	static Statement consulta;
	static ResultSet resultado;

	/*Nueva Barra de Busqueda*/
	static JTextField cBuscar;
	static JButton bBuscar, bVer, bVolver;
	
	/*Posicionadores*/
	int posX = 0, posY = 0;
	
	/*Objeto de validaciones*/
	static util val = new util();

	/*Controlador de modificacion*/
	/*
	0 = Inicia modificacion
	1 = Ya se tiene un codigo a modificar
	*/
	static int iMod = 0;

	/*Almacena datos para validar*/
	String sCodigo, sNombre, sDescripcion, sExistencia, sPrecio;

	producto(){
		new JFrame("Administracion de Productos"); setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		setSize(680, 540);
		
		titulo = new JLabel("Administracion de Productos"); titulo.setBounds(posX, posY, 700, 30);
		titulo.setFont(new Font("Arial", Font.PLAIN, 25));
		titulo.setHorizontalAlignment(JLabel.CENTER); add(titulo); posY+=30;
		
		funciones = new JLabel("F2: Agregar        F3: Modificar       F4: Eliminar"); funciones.setBounds(posX, posY, 700, 20);
		funciones.setHorizontalAlignment(JLabel.CENTER); add(funciones); posY+=35;
		
		posX+=20;
		imagen = new JLabel(new ImageIcon("data/icon/producto.png")); imagen.setBounds(posX, posY, 70, 70);
		add(imagen); posX+=75;
			/*Etiquetas*/
			codigo = new JLabel("COD"); codigo.setBounds(posX, posY, 90, 20); codigo.setHorizontalAlignment(JLabel.CENTER); add(codigo); posX+=95;
			nombre = new JLabel("Nombre"); nombre.setBounds(posX, posY, 120, 20); nombre.setHorizontalAlignment(JLabel.CENTER); add(nombre); posX+=125;
			descripcion = new JLabel("Descripcion"); descripcion.setBounds(posX, posY, 165, 20); descripcion.setHorizontalAlignment(JLabel.CENTER); add(descripcion); posX+=170;
			existencia = new JLabel("Existencia"); existencia.setBounds(posX, posY, 65, 20); existencia.setHorizontalAlignment(JLabel.CENTER); add(existencia); posX+=70;
			precio = new JLabel("Precio"); precio.setBounds(posX, posY, 95, 20); precio.setHorizontalAlignment(JLabel.CENTER); add(precio);
		posX=95; posY+=25;
			eCod = new JTextField(); eCod.setBounds(posX, posY, 90, 30); eCod.setHorizontalAlignment(JTextField.CENTER); eCod.addKeyListener(this); add(eCod); posX+=95;
			eNombre = new JTextField(); eNombre.setBounds(posX, posY, 120, 30); eNombre.setHorizontalAlignment(JTextField.CENTER); eNombre.addKeyListener(this); add(eNombre); posX+=125;
			eDescripcion = new JTextField(); eDescripcion.setBounds(posX, posY, 165, 30); eDescripcion.setHorizontalAlignment(JTextField.CENTER); eDescripcion.addKeyListener(this); add(eDescripcion); posX+=170;
			eExistencia =  new JTextField(); eExistencia.setBounds(posX, posY, 65, 30); eExistencia.setHorizontalAlignment(JTextField.CENTER); eExistencia.addKeyListener(this); add(eExistencia); posX+=70;
			ePrecio = new JTextField(); ePrecio.setBounds(posX, posY, 95, 30); ePrecio.setHorizontalAlignment(JTextField.CENTER); ePrecio.addKeyListener(this); add(ePrecio);
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
		tablaBase.getColumnModel().getColumn(0).setPreferredWidth(100);
		tablaBase.getColumnModel().getColumn(1).setPreferredWidth(150);
		tablaBase.getColumnModel().getColumn(2).setPreferredWidth(170);
		tablaBase.getColumnModel().getColumn(3).setPreferredWidth(80);
		tablaBase.getColumnModel().getColumn(4).setPreferredWidth(130);

		scroll = new JScrollPane(tablaBase);
		scroll.setBounds(posX, posY, 630, 300);
		add(scroll); posX=530; posY+=310;

		setData("select * from producto;");

		//Listener del Boton
			bBuscar.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					setData("select * from producto where codigo like'%"+cBuscar.getText()+"%' or nombre like'%"+cBuscar.getText()+"%' or descripcion like'%"+cBuscar.getText()+"%' or precio like'%"+cBuscar.getText()+"%' or existencia like'%"+cBuscar.getText()+"%';");
				}
			});

			bVer.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					setData("select * from producto;");
				}
			});

			bVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					setVisible(false);
					new credito();
				}
			});

		/*Configuracion de la ventana*/
			ventana=getSize();
			pantalla=Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((pantalla.width-ventana.width)/2, ((pantalla.height-ventana.height)/2)-50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);  setVisible(true);
	}

	/*Clasificacion de Teclas*/
	public void keyPressed(KeyEvent keyEvent){
		int tipo = keyEvent.getExtendedKeyCode();
		/*Obtener string*/
		sCodigo = eCod.getText();
		sNombre = eNombre.getText();
		sDescripcion = eDescripcion.getText();
		sPrecio = ePrecio.getText();
		sExistencia = eExistencia.getText();

		System.out.println("Type: "+tipo);

		if(tipo==113)/*F2*/{
			if(iMod==0){
				/*Agregar un nuevo producto*/
				if(sCodigo.isEmpty() || sNombre.isEmpty() || sDescripcion.isEmpty() || sExistencia.isEmpty() || sPrecio.isEmpty()){
					JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Completar Campos", 2);
				} else {
					/*Verificar Tipo de dato*/
					if(val.esEntero(sCodigo, 6) && val.esCadena(sNombre) && val.esCadena(sDescripcion) && val.esDoble(sExistencia) && val.esDoble(sPrecio)){
						/*Consulta de Existencia*/
						try {
							/*Obtener la cantidad de datos*/
							conector = new enlace();
							consulta = enlace.getConnection().createStatement();
							resultado =  consulta.executeQuery("select count(*) from producto where codigo = '"+sCodigo+"';");
							if(resultado.next()){
								if(resultado.getInt("count(*)")>0){
									JOptionPane.showMessageDialog(this, "El codigo de producto, ya existe.", "Advertencia", 2);
								} else {
									/*Insertar*/
									if(JOptionPane.showConfirmDialog(this, "Agregar producto: "+sNombre+"?", "Agregar Producto", JOptionPane.YES_NO_OPTION)==0)
									{
										consulta.execute("insert into producto values ('"+sCodigo+"', '"+sNombre+"', '"+sDescripcion+"', "+sPrecio+", "+sExistencia+");");
										JOptionPane.showMessageDialog(this, "Producto agregado correctamente", "Mensaje", 1);
										setData("select * from producto;");
										setEmpty();
									}
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
						JOptionPane.showMessageDialog(this, "Completa los datos de manera correcta:\nCodigo: Entero (6)\nNombre: Texto\nDescripcion: Texto\nExistencia: Entero (1)\nPrecio: Decimal", "Error", 0);
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Deshabilita la opcion de modificar para usar esta funcion.", "Advertencia", 2);
			}
		}
		else if(tipo==114)/*F3*/{
			/*Modificar un registro*/
			if(iMod==0){
				modificar();
			} else if(iMod==1){
				/*Guardar cambios de la modificacion*/
				try {
					/*Validar vacios*/
					if(sNombre.isEmpty() || sDescripcion.isEmpty() || sExistencia.isEmpty() || sPrecio.isEmpty()){
					JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Completar Campos", 2);
					} else {
						/*Verificar Tipo de dato*/
						if(val.esCadena(sNombre) && val.esCadena(sDescripcion) && val.esDoble(sExistencia) && val.esDoble(sPrecio)){
							conector = new enlace();
							consulta = enlace.getConnection().createStatement();
								if(JOptionPane.showConfirmDialog(this, "Modificar producto: "+sCodigo+"?", "Guardar cambios", JOptionPane.YES_NO_OPTION)==0){
									consulta.execute("update producto set nombre = '"+sNombre+"', descripcion='"+sDescripcion+"', precio="+sPrecio+", existencia="+sExistencia+" where codigo='"+sCodigo+"';");
									JOptionPane.showMessageDialog(this, "Producto modificado correctamente.", "Mensaje", 1);
									setEmpty();
									setData("select * from producto;");
								}
							consulta.close();
							resultado.close();
						} else {
							JOptionPane.showMessageDialog(this, "Completa los datos de manera correcta:\nNombre: Texto\nDescripcion: Texto\nExistencia: Entero (1)\nPrecio: Decimal", "Error", 0);
						}
					}
				}catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "ERROR: Guardar cambios.\n"+ex.getMessage(), "Error", 0);
				}
			}
		} else if(tipo==115)/*F4*/{
			/*Eliminar*/
			if(sCodigo.isEmpty()){
				JOptionPane.showMessageDialog(this, "Se requiere un CODIGO para eliminar un producto.", "Advertencia", 0);
			} else {
				if(val.esEntero(sCodigo, 6)){
					/*Confirmar para eliminar*/
					try {
						/*Obtener la cantidad de datos*/
						conector = new enlace();
						consulta = enlace.getConnection().createStatement();
						resultado =  consulta.executeQuery("select count(*) from producto where codigo = '"+sCodigo+"';");
						if(resultado.next()){
							if(resultado.getInt("count(*)")==1){
								if(JOptionPane.showConfirmDialog(this, "Eliminar producto: "+sCodigo+"?", "Eliminar Producto", JOptionPane.YES_NO_OPTION)==0)
								{
									consulta.execute("delete from producto where codigo = '"+sCodigo+"';");
									JOptionPane.showMessageDialog(this, "Producto eliminado correctamente", "Mensaje", 1);
									setData("select * from producto;");
									setEmpty();
								}
							} else {
								JOptionPane.showMessageDialog(this, "El codigo de producto, NO existe.\nIngresa uno valido.", "Advertencia", 2);
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
		} else if(tipo == 116){/*F5*/
			if(iMod==1){/*Opcion de cancelar la modificacion*/
				setEmpty();
			}
		}
	}

	/*Metodo para vaciar los campos*/
	public void setEmpty(){
		eCod.setText("");
		eCod.setEditable(true);
		eCod.requestFocus();
		funciones.setText("F2: Agregar        F3: Modificar       F4: Eliminar");
		iMod=0;
		eNombre.setText(""); eDescripcion.setText("");
		ePrecio.setText(""); eExistencia.setText("");
	}

	/*Accion Buscar*/
	public void modificar(){
		if(iMod == 0){/*Inicia la modificacion*/
			if(sCodigo.isEmpty()){
				JOptionPane.showMessageDialog(this, "Se requiere el codigo del producto a modificar.", "Advertencia", 1);
			} else {
				if(val.esEntero(sCodigo, 6)){
					try {
						/*Consulta de existencia*/
						conector = new enlace();
						consulta = enlace.getConnection().createStatement();
						resultado =  consulta.executeQuery("select  *, count(*) from producto where codigo = '"+sCodigo+"';");
						if(resultado.next()){
							if(resultado.getInt("count(*)")>0){
								eNombre.setText(resultado.getString("nombre"));
								eDescripcion.setText(resultado.getString("descripcion"));
								eExistencia.setText(resultado.getString("existencia"));
								ePrecio.setText(resultado.getString("precio"));
								/*Verificar la existencia del codigo a moficar*/
								funciones.setText("F2: Agregar        F3: Guardar       F4: Eliminar        F5: Cancelar");
								eCod.setEditable(false);
								iMod=1;
							} else {
								JOptionPane.showMessageDialog(this, "No existen el producto con codigo:\n"+sCodigo, "Advertencia", 1);
							}
						} else {
							JOptionPane.showMessageDialog(this, "ERROR: Consulta de codio: "+sCodigo, "Error", 0);
						}
						consulta.close();
						resultado.close();
					}catch(Exception ex){
						JOptionPane.showMessageDialog(this, "ERROR: Consulta de existencia.\n"+ex.getMessage(), "Error", 0);
					}

					/*Validar existenncia*/
				} else {
					JOptionPane.showMessageDialog(this, "Formato de codigo no valido:\n123456", "Error", 0);
				}
			}
		}
	}

	/*Metodos por defecto*/
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
						resultado.getInt("codigo"),
						resultado.getString("nombre"),
						resultado.getString("descripcion"),
						resultado.getInt("existencia"),
						resultado.getDouble("precio")
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
}