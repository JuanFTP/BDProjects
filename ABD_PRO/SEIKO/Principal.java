import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.sql.*;
class Principal {
	public static MySQL db = new MySQL();
	public TablaModelo tm = null;
	private static Statement comando = null;
	private static ResultSet registro = null;
	private Dimension pant,vent;
	private JFrame ventana;
	private JPanel panel,bordo;
	private JScrollPane barra;
	private JLabel[] etiq;
	private JTextField campo[];
	private JCheckBox[] caja;
	private JComboBox lista;
	private JButton aceptar,salir,opcion;
	private JTable tab;
	//private KeyListener keyListener;
	private String[] cad = {"CURP","Nombre","Apellido","Sexo","Edad"},list = {"-Opción-","Insertar","Buscar","Modificar","Borrar"};
	private int a=15,b=60,c=0,d=0,elim=0,z=0;
	private boolean ban=false;
	public Principal(){		
		ventana = new JFrame("Registro");
		ventana.setSize(680,400);
		vent = ventana.getSize();
		pant = Toolkit.getDefaultToolkit().getScreenSize();
		ventana.setLocation((pant.width-vent.width)/2,(pant.height-vent.height)/2);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.getContentPane().setLayout(null);
		
		tm= new TablaModelo();
		tm.visaulizarDatos("*",";");
		tab = new JTable(tm);
		tab.setPreferredScrollableViewportSize(new Dimension(350,70));
		tab.getTableHeader().setReorderingAllowed(false);
		barra = new JScrollPane(tab);
		barra.setBounds(20,140,610,200);
		ventana.getContentPane().add(barra);
		/*bordo = new JPanel();
		barra.setViewportView(bordo);
		bordo.setLayout(new BorderLayout(0,0));*/
		
		lista = new JComboBox(list);
		lista.setBounds(460,15,90,25);
		aceptar = new JButton("Aceptar");
		aceptar.setBounds(570,15,80,25);
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try{
					getRegistro("*",";");
					if(validarCombox(lista.getSelectedIndex())){
						if(!registro.next() && lista.getSelectedIndex()>1){
							JOptionPane.showMessageDialog(null,"No Hay Registros");
							cambiar(false,false,String.valueOf(lista.getSelectedItem()));
						}else{
							cambiar(true,true,String.valueOf(lista.getSelectedItem()));
						}
					}else
						validarError();
					if(lista.getSelectedIndex()==4){
						JOptionPane.showMessageDialog(null,"Seleccione (el/los) Registro(s) a Eliminar","Eliminación de Registros",JOptionPane.INFORMATION_MESSAGE);
						teclas();
					}
					if(lista.getSelectedIndex()==3)
						JOptionPane.showMessageDialog(null,"Seleccione el Registro a Modificar","Modificación de Registros",JOptionPane.INFORMATION_MESSAGE);
					if(lista.getSelectedIndex()==2){
						teclas();
					}
				}catch(Exception e){
				}
			}
		});
		opcion = new JButton(" -- ");
		opcion.setBounds(175,95,90,25);
		opcion.setEnabled(false);
		opcion.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				switch(lista.getSelectedIndex()){
					case 1: insertar();
						break;
					case 2: 
						break;
					case 3: modificar();
						break;
					case 4: eliminar();
						break;
				}
			}
		});
		etiq = new JLabel[cad.length];
		campo = new JTextField[cad.length];
		boolean band=true;
			for(int x=0;x<cad.length;x++){
				etiq[x] = new JLabel(cad[x]+((x==1 || x==2)?"(s): ":": "));
				etiq[x].setBounds((x>2)?a+c+30+d:((x>1)?a+c:a),b,70,25);
				ventana.getContentPane().add(etiq[x]);
				campo[x] = new JTextField();
				campo[x].setEditable(false);
				campo[x].setBounds((x>1)?a+70+c+d:a+40+c,b,90,25);
				ventana.getContentPane().add(campo[x]);
				a+=140;	c=30;
				if(x>2 && band){
					a=15;
					b=95;
					c=0;
					d=-30;
					band=false;
				}
			}
		salir = new JButton("Salir");
		salir.setBounds(530,95,90,25);
		salir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				int resp = JOptionPane.showConfirmDialog(null,"¿Desea Salir del Sistema?","Salir",JOptionPane.YES_NO_OPTION);
				if(resp == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		tab.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent ke){
				elim = tab.getSelectedRow();
				if(elim!=-1 && lista.getSelectedIndex()==3){
					for(int x=0;x<cad.length;x++)
						campo[x].setText(String.valueOf(tm.getValueAt(elim,x)));
				}
			}
		});
		tab.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
				elim = tab.getSelectedRow();
				if(elim!=-1 && lista.getSelectedIndex()==3){
					for(int x=0;x<cad.length;x++)
						campo[x].setText(String.valueOf(tm.getValueAt(elim,x)));
				}
			}
		});
		//addKeyListener(this);
		ventana.getContentPane().add(lista);
		ventana.getContentPane().add(opcion);
		ventana.getContentPane().add(aceptar);
		ventana.getContentPane().add(salir);
		ventana.setVisible(true);
	}/*
	public void keyPressed(KeyEvent keyEvent) {
		buscar(z);
	}
	public void keyReleased(KeyEvent keyEvent) {
		buscar(z);
	}
	public void keyTyped(KeyEvent keyEvent) {
		buscar(z);
	}*/
	public void insertar(){
		try{
			ban=true;
			comando=db.getConnection().createStatement();
			String cadena= "INSERT INTO Cliente VALUES(";
			for(int x=0;x<cad.length-1;x++)
				cadena+="'"+campo[x].getText()+"',";
			cadena+=campo[cad.length-1].getText()+");";
				getRegistro("*","WHERE CURP='"+campo[0].getText()+"';");
				if(!registro.next()){
					comando.execute(cadena);
					tm.visaulizarDatos("*",";");
					barra.setViewportView(tab);
					//tm.updateUI();
					for(int x=0;x<cad.length;x++)
						campo[x].setText("");
				}else{
					JOptionPane.showMessageDialog(null,"Imposible de Insertar. Claves Duplicadas","Registro Duplicado",JOptionPane.ERROR_MESSAGE);
					ban=false;
				}
		}catch(Exception ex){
			Logger.getLogger(Principal.class.getName()).log(Level.SEVERE,null,ex);
		}finally{
			if(ban)
				JOptionPane.showMessageDialog(null,"Datos Guardados Correctamente","Insercción de Registros",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void buscar(){
		try{
			comando=db.getConnection().createStatement();
			String cadena="WHERE ";
			for(int y=0,z=0;z<cad.length;z++){
				if(campo[z].getText().length()>0){
					cadena+=(y>0)?" AND "+cad[z]+" LIKE '%"+campo[z].getText()+"%'":" "+cad[z]+" LIKE '%"+campo[z].getText()+"%'";
					y++;
				}
			}
			tm.visaulizarDatos("*",((cadena.length()>6)?cadena+";":";"));
					barra.setViewportView(tab);
		}catch(Exception ex){
			Logger.getLogger(Principal.class.getName()).log(Level.SEVERE,null,ex);
		}
	}
	public void teclas(){
		KeyListener keyListener= new KeyListener(){
			public void keyPressed(KeyEvent keyEvent) {
        		buscar();
      		}
      		public void keyReleased(KeyEvent keyEvent) {
				buscar();
      		}
      		public void keyTyped(KeyEvent keyEvent) {
	        	buscar();
      		}
		};
		for(z=0;z<cad.length;z++)
			campo[z].addKeyListener(keyListener);
	}
	public void modificar(){
		try{
			ban=true;
			int resp = JOptionPane.showConfirmDialog(null,"Se Modificará el Registro. ¿Desea Continuar? ","Confirmación",JOptionPane.YES_NO_OPTION);
				if(resp == JOptionPane.YES_OPTION){
					comando=db.getConnection().createStatement();
						String cadena= "UPDATE Cliente SET ";
						for(int x=1;x<cad.length-1;x++)
							cadena+=cad[x]+"='"+campo[x].getText()+"', ";
						cadena+=cad[cad.length-1]+"="+campo[cad.length-1].getText()+" WHERE CURP='"+tm.getValueAt(elim,0)+"0';";
						comando.execute(cadena);
					cambiar(true,true,String.valueOf(lista.getSelectedItem()));
					tm.visaulizarDatos("*",";");
					barra.setViewportView(tab);
				}
		}catch(Exception ex){
			Logger.getLogger(Principal.class.getName()).log(Level.SEVERE,null,ex);
		}finally{
			if(ban)
				JOptionPane.showMessageDialog(null,"Datos Guardados Correctamente","Modificación de Registros",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void eliminar(){
		try{
			int[] elim = tab.getSelectedRows();
			int resp = JOptionPane.showConfirmDialog(null,"Se Eliminaran Registros. ¿Desea Continuar? ","Confirmación",JOptionPane.YES_NO_OPTION);
				if(resp == JOptionPane.YES_OPTION){
					comando=db.getConnection().createStatement();
					for(int x:elim){
						comando.execute("DELETE FROM Cliente WHERE CURP='"+tm.getValueAt(x,0)+"'");
					}
					tm.visaulizarDatos("*",";");
					barra.setViewportView(tab);
				}
		}catch(Exception ex){
			Logger.getLogger(Principal.class.getName()).log(Level.SEVERE,null,ex);
		}
	}
	public static ResultSet getRegistro(String con,String con2){
		try{
			comando=db.getConnection().createStatement();
			return registro=comando.executeQuery("SELECT "+con+" FROM Cliente "+con2);
		}catch(Exception ex){
			Logger.getLogger(Principal.class.getName()).log(Level.SEVERE,null,ex);
		}
		return null;
	}
	public void cambiar(boolean op,boolean op0,String op1){
		for(int x=0;x<cad.length;x++){
			campo[x].setEditable(op);
			campo[x].setText("");
		}
		opcion.setEnabled(op0);
		opcion.setText(op1);
	}
	public boolean validarCombox(int i){
		return (i!=0)?true:false;
	}
	public void validarError(){
		if(!validarCombox(lista.getSelectedIndex())){
			JOptionPane.showMessageDialog(null,"Seleccione una Operación","Opción Invalida",JOptionPane.ERROR_MESSAGE);
			cambiar(false,false,"--");
		}
	}	
	public static void main(String...Args){
		new Principal();
	}
}
class TablaModelo extends AbstractTableModel{
		ResultSet registro = null;
		final String[] columnNames = {"CURP", "Nombre", "Apellidos", "Sexo", "Edad"};
		Object[][] data = null;
		//JCheckBox[] caja = null;
		public void visaulizarDatos(String b,String d){
			Vector<String> vec= new Vector<String>();
			try{
				//registro = Principal.comando.executeQuery("SELECT "+b+" FROM Cliente"+d);
				registro = Principal.getRegistro(b,d);
				while(registro.next()){
					for(int x=1;x<=getColumnCount();x++)
						vec.add(registro.getString(x));
				}
				data = new Object[vec.size()/getColumnCount()][getColumnCount()];
				for(int f=0,x=0;f<(vec.size()/getColumnCount());f++){
					for(int c=0;c<getColumnCount();c++,x++)
							data[f][c]= vec.get(x);
				}
			}catch(Exception ex){
				Logger.getLogger(Principal.class.getName()).log(Level.SEVERE,null,ex);
			}
		 }
		 //retorna el numero de columnas
		 public int getColumnCount(){
		 	return columnNames.length;
		 }
		 //retorna el numero de elementos
		 public int getRowCount() {
		 	return data.length;
		 }
		 //retornamos el elemento indicado
		 public String getColumnName(int col) {
		 	return columnNames[col];
		 }
		 //y lo mismo para las celdas
		 public Object getValueAt(int row, int col) {
		 	return data[row][col];
		 }
		 
		 //Determina si las celdas se pueden editar
		 public boolean isCellEditable(int row, int col) {
		 	return false;
		 }
		 /*
		 * No tienes que implementar este método a menos que
		 * los datos de tu tabla cambien
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
}