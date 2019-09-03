import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
class usuario extends JFrame implements KeyListener
{
	private Dimension pantalla, ventana;
	/*Checar conexion de enlace*/
	public static enlace conexion = new enlace();
	public static Statement consulta=null;
	public static ResultSet resultado=null;
	public static String userUpper;
	public static String permisosUpper;
	/*Variables de posicionamiento*/
	int posX, posY, tamX, tamY, tamVX, tamVY;
	
	public usuario(String userUpper, String permisosUpper)
	{
		this.userUpper=userUpper;
		this.permisosUpper=permisosUpper;
		
		new JFrame();
		setTitle("Menu Usuario");
		setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		/*Area de botones y Funciones dif=41*/
		/*Administrar Usuarios*/
		/*Dimensionadores*/
		posX=30; posY=85; tamX=tamY=80;

		JButton administrarClientes=new JButton(new ImageIcon("data/mAD/adCLI.png"));
		administrarClientes.setBounds(posX, posY, tamX, tamY);
		administrarClientes.setBackground(Color.WHITE);
		administrarClientes.setBorderPainted(false);
		administrarClientes.addKeyListener(this);
		add(administrarClientes);

		JTextArea etiquetaAdminUsu=new JTextArea("Administrar\n  Clientes");
		etiquetaAdminUsu.setBounds(posX, posY+tamY+10, tamX, tamY-20);
		add(etiquetaAdminUsu);

		posX+=tamY+40;

		administrarClientes.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(!userUpper.equals("INDEX"))
				{
					adminClientes adU=new adminClientes(userUpper, permisosUpper);
					setVisible(false);/*ocultar pantalla usuario*/
				}
				else
				{JOptionPane.showMessageDialog(getContentPane(), "Crea una cuenta de administrador para continuar.", "Database: Empty", 0);}
			}
		}
		);

		/*Administrar Contratos*/
		JButton administrarContratos=new JButton(new ImageIcon("data/mAD/adCONT.png"));
		administrarContratos.setBounds(posX, posY, tamX, tamY);
		administrarContratos.setBackground(Color.WHITE);
		administrarContratos.setBorderPainted(false);
		administrarContratos.addKeyListener(this);
		add(administrarContratos);

		JTextArea etiquetaAdminCon=new JTextArea("Administrar\n Contratos");
		etiquetaAdminCon.setBounds(posX, posY+tamY+10, tamX, tamY-20);
		add(etiquetaAdminCon);

		posX+=tamY+40;

		administrarContratos.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(!userUpper.equals("INDEX"))
				{
					adminContratos adC=new adminContratos(userUpper, permisosUpper);
					setVisible(false);/*ocultar pantalla usuario*/
				}
				else
				{JOptionPane.showMessageDialog(getContentPane(), "Crea una cuenta de administrador para continuar", "Database: Empty", 0);}
			}
		}
		);

		/*Reporte de Pagos*/
		JButton puntoPago=new JButton(new ImageIcon("data/mAD/rePAG.png"));
		puntoPago.setBounds(posX, posY, tamX, tamY);
		puntoPago.setBackground(Color.WHITE);
		puntoPago.setBorderPainted(false);
		puntoPago.addKeyListener(this);
		add(puntoPago);

		JTextArea etiquetaRPagos=new JTextArea("Punto - cobro\nPagos");
		etiquetaRPagos.setBounds(posX, posY+tamY+10, tamX, tamY-20);
		add(etiquetaRPagos);

		posX+=tamY+30;
		posY+=(tamY+10)+(tamY-20)+20;
		tamVX=posX;
		tamVY=posY;

		puntoPago.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(!userUpper.equals("INDEX"))
				{
					setVisible(false);
					new puntoCobro(userUpper, permisosUpper);
				}
				else
				{JOptionPane.showMessageDialog(getContentPane(), "Crea una cuenta de administrador para continuar", "Database: Empty", 0);}
			}
		}
		);

		posY=0;
		/*Header*/
		JButton barraSuperior=new JButton("");
		barraSuperior.setBounds(0, posY, tamVX, 10);
		barraSuperior.setBackground(new Color(0, 195, 135));
		barraSuperior.setBorderPainted(false);
		barraSuperior.addKeyListener(this);
		add(barraSuperior);
		posY+=20;
		/*Header*/
		/*Titulo*/
		/*Datos Usuario*/
		JLabel datosUsuario=new JLabel(userUpper+"         Salir(ESC)");
		datosUsuario.setHorizontalAlignment(JLabel.RIGHT);
		datosUsuario.setFont(new Font("Roboto", Font.PLAIN, 15));
		datosUsuario.setBounds(150, posY, tamVX-170, 30);
		add(datosUsuario);
		/*Datos Usuario*/
		
		/*Titulo*/
		JLabel tituloMenu=new JLabel("Usuario");
		tituloMenu.setFont(new Font("Roboto", Font.PLAIN, 30));
		tituloMenu.setBounds(10, posY, 140, 30);
		tituloMenu.setHorizontalAlignment(JLabel.LEFT);
		add(tituloMenu);
		posY+=45;
		/*Idicador*/
		JLabel indicadorGeneral=new JLabel("Selecciona una accion");
		indicadorGeneral.setFont(new Font("Roboto", Font.PLAIN, 15));
		indicadorGeneral.setBounds(10, 50, 600, 30);
		add(indicadorGeneral);
		/*Idicador*/
	
		/*Caracteristicas de la pantalla*/
		setVisible(true);
		setResizable(false);
	    setSize(tamVX, tamVY);
	    ventana=getSize();
	    pantalla=Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
	    //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		int tipo = keyEvent.getExtendedKeyCode();
		if(tipo==27)//esc salir
		{
			/*Salida del sistema*/
			if(JOptionPane.showConfirmDialog(getContentPane(), "Cerrar sesion?", "Advertencia", JOptionPane.YES_NO_OPTION)==0)
    		{
    			this.setVisible(false);
				Home adm=new Home();
    		}
		}
		System.out.println("keyPressed: tipo: "+tipo);
	}

    public void keyReleased(KeyEvent keyEvent)
    {}

    public void keyTyped(KeyEvent keyEvent)
    {}
}