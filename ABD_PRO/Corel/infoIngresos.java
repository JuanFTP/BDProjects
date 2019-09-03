import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
class infoIngresos extends JFrame implements KeyListener
{
	private Dimension pantalla, ventana;
	/*Checar conexion de MySQL*/
	public static MySQL enlace = new MySQL();
	public static Statement consulta=null;
	public static ResultSet resultado=null;
	/*Variables Basicas*/
	public String [] titulos = {"IDIng", "Fecha", "Concepto", "Importe", "Usuario"};
	public String [] orden = {"IDIng", "Fecha", "Importe", "Usuario"};
	public JTable tabla;
	public JScrollPane desplazar;
	/*Estilos Generales*/
	JButton color;
	
	public infoIngresos()
	{
		new JFrame("Informe de Ingresos");
		setLayout(null);
		getContentPane().setBackground(new Color(180, 231, 252));
		/*Estilo General*/
		color=new JButton("");
		color.setBounds(0, 0, 800, 10);
		color.setBackground(new Color(41, 182, 246));
		color.setBorderPainted(false);
		add(color);
		/*Estilo General*/
		/*Titulo*/
		JLabel tituloMenu=new JLabel("Informe de Ingresos");
		tituloMenu.setFont(new Font("Roboto", Font.PLAIN, 20));
		tituloMenu.setBounds(10, 8, 600, 30);
		add(tituloMenu);
		/*Titulo*/
		/*Seccion de operaciones*/
		JButton botonAgregar=new JButton(new ImageIcon("data/oper/nuevo.gif"));
		botonAgregar.setText("Agregar");
		botonAgregar.setBounds(10, 40, 100, 20);
		botonAgregar.setBorderPainted(false);
		botonAgregar.setBackground(new Color(180, 231, 252));
		add(botonAgregar);

		JButton botonModificar=new JButton(new ImageIcon("data/oper/editar.gif"));
		botonModificar.setText("Editar");
		botonModificar.setBounds(110, 40, 90, 20);
		botonModificar.setBorderPainted(false);
		botonModificar.setBackground(new Color(180, 231, 252));
		add(botonModificar);

		JButton botonBorrar=new JButton(new ImageIcon("data/oper/borrar.gif"));
		botonBorrar.setText("Borrar");
		botonBorrar.setBounds(200, 40, 90, 20);
		botonBorrar.setBorderPainted(false);
		botonBorrar.setBackground(new Color(180, 231, 252));
		add(botonBorrar);

		JButton botonOrdenar=new JButton(new ImageIcon("data/oper/orden.gif"));
		botonOrdenar.setText("Ordenar:");
		botonOrdenar.setBounds(290, 40, 100, 20);
		botonOrdenar.setBackground(new Color(180, 231, 252));
		add(botonOrdenar);
		
		JComboBox comboOrden = new JComboBox(orden);
		comboOrden.setBackground(Color.WHITE);
		comboOrden.setBounds(390, 40, 100, 20);
		add(comboOrden);
		/*Seccion de operaciones*/

		/*Boton Salir*/
		JButton botonSalir=new JButton(new ImageIcon("data/oper/salir.gif"));
		botonSalir.setText("Salir");
		botonSalir.setBounds(710, 40, 90, 20);
		botonSalir.setBackground(new Color(180, 231, 252));
		botonSalir.setBorderPainted(false);
		add(botonSalir);

		botonSalir.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				administrador admin=new administrador();
				setVisible(false);/*ocultar pantalla administrador*/
			}
		}
		);
		/*Boton Salir*/

		/*Area de tabla*/
		tabla= new JTable(null);
		tabla.setPreferredScrollableViewportSize(new Dimension(700, 500));
		tabla.getTableHeader().setReorderingAllowed(false);
		desplazar = new JScrollPane(tabla);
		desplazar.setBounds(25, 90, 750, 460);
		add(desplazar);
		/*Area de tabla*/

		/*Caracteristicas de la pantalla*/
		setVisible(true);
		setResizable(false);
	    setSize(800, 600);
	    ventana=getSize();
	    pantalla=Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
	    //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void keyPressed(KeyEvent keyEvent)
	{}

    public void keyReleased(KeyEvent keyEvent)
    {}

    public void keyTyped(KeyEvent keyEvent)
    {}

	public static void main(String...Args) throws SQLException
	{new infoIngresos();}
}