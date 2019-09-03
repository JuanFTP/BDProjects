import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
class confiCuenta extends JFrame implements KeyListener
{
	private Dimension pantalla, ventana;
	/*Checar conexion de MySQL*/
	public static MySQL enlace = new MySQL();
	public static Statement consulta=null;
	public static ResultSet resultado=null;
	/*Estilos Generales*/
	JButton color;	
	
	public confiCuenta()
	{
		new JFrame("Configurar Cuenta");
		setLayout(null);
		getContentPane().setBackground(new Color(182, 223, 243));
		/*Estilo General*/
		color=new JButton("");
		color.setBounds(0, 0, 800, 10);
		color.setBackground(new Color(34, 158, 219));
		color.setBorderPainted(false);
		add(color);
		/*Estilo General*/
		/*Titulo*/
		JLabel tituloMenu=new JLabel("Configurar Cuenta");
		tituloMenu.setFont(new Font("Roboto", Font.PLAIN, 20));
		tituloMenu.setBounds(10, 8, 600, 30);
		add(tituloMenu);
		/*Titulo*/

		/*Boton Salir*/
		JButton botonSalir=new JButton(new ImageIcon("data/oper/salir.gif"));
		botonSalir.setText("Salir");
		botonSalir.setBounds(310, 14, 90, 20);
		botonSalir.setBackground(new Color(182, 223, 243));
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

		/*Caracteristicas de la pantalla*/
		setVisible(true);
		setResizable(false);
	    setSize(400, 300);
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
	{new confiCuenta();}
}