import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
class administrador extends JFrame implements KeyListener
{
	private Dimension pantalla, ventana;
	/*Checar conexion de enlace*/
	public static enlace conexion = new enlace();
	public static Statement consulta=null;
	public static ResultSet resultado=null;
	public static String userUpper;
	public static String permisosUpper;
	/*Posicionadores*/
	int posX, posY, tamX, tamY, tamVX, tamVY;
	
	public administrador(String userUpr, String permisosUpper)
	{
		this.userUpper=userUpr;
		this.permisosUpper=permisosUpper;
		new JFrame();
		setTitle("Menu (Administrador)");
		setLayout(null);
		getContentPane().setBackground(Color.WHITE);

		/*Inicalizacion de posicionadores*/
		posX=20; posY=85; tamX=tamY=85;

		/*Area de botones y Funciones dif=41*/
		/*Administrar Usuarios*/
		JButton administrarUsuarios=new JButton(new ImageIcon("data/mAD/adUSU.png"));
		administrarUsuarios.setBounds(posX, posY, tamX, tamY);
		administrarUsuarios.setBackground(Color.WHITE);
		administrarUsuarios.setBorderPainted(false);
		administrarUsuarios.addKeyListener(this);
		add(administrarUsuarios);

		JTextArea etiquetaAdminUsu=new JTextArea("Administrar\n  Usuarios");
		etiquetaAdminUsu.setBounds(posX, posY+tamY, tamX, tamY-20);
		add(etiquetaAdminUsu);

		posX+=tamY+40;

		administrarUsuarios.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(userUpper.equals("INDEX"))
				{JOptionPane.showMessageDialog(getContentPane(), "Primero debes agregar una cuenta de administrador\n para poder acceder a los demas servicios.", "Cuidado", 2);}
				adminUsuarios adU=new adminUsuarios(userUpper, permisosUpper);
				setVisible(false);/*ocultar pantalla administrador*/
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
		etiquetaAdminCon.setBounds(posX, posY+tamY, tamX, tamY-20);
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
				{JOptionPane.showMessageDialog(getContentPane(), "Crea primero una cuenta de Administrador para continuar", "Dabase: Empty", 0);}
			}
		}
		);

		/*Reporte de Pagos*/
		JButton adminisRecursos=new JButton(new ImageIcon("data/mAD/rePAG.png"));
		adminisRecursos.setBounds(posX, posY, tamX, tamY);
		adminisRecursos.setBackground(Color.WHITE);
		adminisRecursos.setBorderPainted(false);
		adminisRecursos.addKeyListener(this);
		add(adminisRecursos);

		JTextArea etiquetaRPagos=new JTextArea("Administrar\nRecursos");
		etiquetaRPagos.setBounds(posX, posY+tamY, tamX, tamY-20);
		add(etiquetaRPagos);

		posX+=tamY+40;

		adminisRecursos.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(!userUpper.equals("INDEX"))
				{
					new adminRecursos(userUpper, permisosUpper);
					setVisible(false);
				}
				else
				{JOptionPane.showMessageDialog(getContentPane(), "Crea primero una cuenta de Administrador para continuar", "Dabase: Empty", 0);}
			}
		}
		);

		/*Informe de ingresos*/
		JButton administrarClientes=new JButton(new ImageIcon("data/mAD/adCLI.png"));
		administrarClientes.setBounds(posX, posY, tamX, tamY);
		administrarClientes.setBackground(Color.WHITE);
		administrarClientes.setBorderPainted(false);
		administrarClientes.addKeyListener(this);
		add(administrarClientes);

		JTextArea etiquetaIngresos=new JTextArea("Administrar\n  Clientes");
		etiquetaIngresos.setBounds(posX, posY+tamY, tamX, tamY-20);
		add(etiquetaIngresos);

		posX+=tamY+30;
		posY+=(tamY+10)+(tamY-20)+20;
		posY+=30;
		tamVX=posX;
		tamVY=posY;
		System.out.println(posX+" "+posY+" "+tamVX+" "+tamVY);
		/*Footer*/
		JButton barraInferior=new JButton("");
		barraInferior.setBounds(0, posY-45, tamVX, 20);
		barraInferior.setBackground(new Color(0, 91, 91));
		barraInferior.setBorderPainted(false);
		barraInferior.addKeyListener(this);
		add(barraInferior);
		repaint();
		/*Footer*/
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
		posX=0; posY=0;
		/*Area inicial de Indicadores*/
		/*Header*/
		JButton barraSuperior=new JButton("");
		barraSuperior.setBounds(posX, posY, tamVX, 10);
		barraSuperior.setBackground(new Color(0, 0, 64));
		barraSuperior.setBorderPainted(false);
		barraSuperior.addKeyListener(this);
		add(barraSuperior);
		/*Header*/
		posX=10; posY+=20; tamX=200; tamY=30;
		/*Titulo*/
		JLabel tituloMenu=new JLabel("Administrador");
		tituloMenu.setFont(new Font("Roboto", Font.PLAIN, 30));
		tituloMenu.setBounds(posX, posY, tamX, tamY);
		tituloMenu.setHorizontalAlignment(JLabel.CENTER);
		add(tituloMenu);
		/*Titulo*/
		posX+=tamX+10; tamX=tamVX-(posX+20);
		/*Datos Usuario*/
		JLabel datosUsuario=new JLabel("Usuario: "+userUpper+"         Salir(ESC)");
		datosUsuario.setHorizontalAlignment(JLabel.RIGHT);
		datosUsuario.setFont(new Font("Roboto", Font.PLAIN, 15));
		datosUsuario.setBounds(posX, posY, tamX, tamY);
		add(datosUsuario);
		/*Datos Usuario*/
		posX=10;
		posY+=tamY;
		tamX=300;
		/*Idicador*/
		JLabel indicadorGeneral=new JLabel("Operaciones Administrativas");
		indicadorGeneral.setFont(new Font("Roboto", Font.PLAIN, 15));
		indicadorGeneral.setBounds(posX, posY, tamVX, tamY);
		indicadorGeneral.setHorizontalAlignment(JLabel.LEFT);
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
	}

    public void keyReleased(KeyEvent keyEvent)
    {}

    public void keyTyped(KeyEvent keyEvent)
    {}
}