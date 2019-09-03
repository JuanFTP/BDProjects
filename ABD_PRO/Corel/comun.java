/*BETA - CORREGIR*/
import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
class comun extends JFrame implements KeyListener
{
	private Dimension pantalla, ventana;
	/*Checar conexion de MySQL*/
	public static MySQL enlace = new MySQL();
	public static Statement consulta=null;
	public static ResultSet resultado=null;
	
	public comun()
	{
		new JFrame("Menu User");
		setLayout(null);
		getContentPane().setBackground(Color.WHITE);

		/*Header*/
		JButton barraSuperior=new JButton("");
		barraSuperior.setBounds(0, 0, 600, 10);
		barraSuperior.setBackground(new Color(0, 195, 135));
		barraSuperior.setBorderPainted(false);
		barraSuperior.setEnabled(false);
		add(barraSuperior);
		/*Header*/
		/*Titulo*/
		JLabel tituloMenu=new JLabel("Comun");
		tituloMenu.setFont(new Font("Roboto", Font.PLAIN, 30));
		tituloMenu.setBounds(10, 15, 200, 30);
		add(tituloMenu);
		/*Titulo*/
		/*Datos Usuario*/
		JLabel datosUsuario=new JLabel("Se mostraran los datos del Usuario");
		datosUsuario.setFont(new Font("Roboto", Font.PLAIN, 15));
		datosUsuario.setBounds(10, 40, 600, 30);
		add(datosUsuario);
		/*Datos Usuario*/
		/*Idicador*/
		JLabel indicadorGeneral=new JLabel("Operaciones Administrativas");
		indicadorGeneral.setFont(new Font("Roboto", Font.PLAIN, 15));
		indicadorGeneral.setBounds(10, 70, 600, 30);
		add(indicadorGeneral);
		/*Idicador*/

		/*Area de botones y Funciones dif=41*/
		/*Administrar Usuarios*/
		int vAlto=100;
		JButton administrarUsuarios=new JButton(new ImageIcon("data/mAD/adUSU.png"));
		administrarUsuarios.setBounds(41, vAlto, 70, 70);
		administrarUsuarios.setBackground(Color.WHITE);
		administrarUsuarios.setBorderPainted(false);
		add(administrarUsuarios);

		JTextArea etiquetaAdminUsu=new JTextArea("Administrar\n  Usuarios");
		etiquetaAdminUsu.setBounds(41, vAlto+70, 70, 40);
		add(etiquetaAdminUsu);

		administrarUsuarios.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				adminUsuarios adU=new adminUsuarios();
				setVisible(false);/*ocultar pantalla comun*/
			}
		}
		);

		/*Administrar Contratos*/
		JButton administrarContratos=new JButton(new ImageIcon("data/mAD/adCONT.png"));
		administrarContratos.setBounds(152, vAlto, 70, 70);
		administrarContratos.setBackground(Color.WHITE);
		administrarContratos.setBorderPainted(false);
		add(administrarContratos);

		JTextArea etiquetaAdminCon=new JTextArea("Administrar\n Contratos");
		etiquetaAdminCon.setBounds(152, vAlto+70, 70, 40);
		add(etiquetaAdminCon);

		administrarContratos.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				adminContratos adC=new adminContratos();
				setVisible(false);/*ocultar pantalla comun*/
			}
		}
		);

		/*Reporte de Pagos*/
		JButton reportePagos=new JButton(new ImageIcon("data/mAD/rePAG.png"));
		reportePagos.setBounds(263, vAlto, 70, 70);
		reportePagos.setBackground(Color.WHITE);
		reportePagos.setBorderPainted(false);
		add(reportePagos);

		JTextArea etiquetaRPagos=new JTextArea("Reporte\nde Pagos");
		etiquetaRPagos.setBounds(263, vAlto+70, 70, 40);
		add(etiquetaRPagos);

		reportePagos.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				reportPagos reP=new reportPagos();
				setVisible(false);/*ocultar pantalla comun*/
			}
		}
		);

		/*Informe de ingresos*/
		JButton informeIngresos=new JButton(new ImageIcon("data/mAD/reING.png"));
		informeIngresos.setBounds(374, vAlto, 70, 70);
		informeIngresos.setBackground(Color.WHITE);
		informeIngresos.setBorderPainted(false);
		add(informeIngresos);

		JTextArea etiquetaIngresos=new JTextArea("Informe de\nIngresos");
		etiquetaIngresos.setBounds(374, vAlto+70, 70, 40);
		add(etiquetaIngresos);

		informeIngresos.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				infoIngresos iIn=new infoIngresos();
				setVisible(false);/*ocultar pantalla comun*/
			}
		}
		);

		/*Informe de egresos*/
		JButton informeEgresos=new JButton(new ImageIcon("data/mAD/reEGR.png"));
		informeEgresos.setBounds(485, vAlto, 70, 70);
		informeEgresos.setBackground(Color.WHITE);
		informeEgresos.setBorderPainted(false);
		add(informeEgresos);

		JTextArea etiquetaEgresos=new JTextArea("Informe de\nEgresos");
		etiquetaEgresos.setBounds(485, vAlto+70, 70, 40);
		add(etiquetaEgresos);

		informeEgresos.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				infoEgresos iEg=new infoEgresos();
				setVisible(false);/*ocultar pantalla comun*/
			}
		}
		);

		/*Idicador Cuenta*/
		JLabel indicadorCuenta=new JLabel("Cuenta");
		indicadorCuenta.setFont(new Font("Roboto", Font.PLAIN, 15));
		indicadorCuenta.setBounds(10, 220, 600, 30);
		add(indicadorCuenta);
		/*Idicador Cuenta*/
		vAlto=260;

		/*Administrar Usuarios del Sistema*/
		JButton adminUSistema=new JButton(new ImageIcon("data/mAD/adUSIS.png"));
		adminUSistema.setBounds(153, vAlto, 70, 70);
		adminUSistema.setBackground(Color.WHITE);
		adminUSistema.setBorderPainted(false);
		add(adminUSistema);

		JTextArea etiquetaUSistema=new JTextArea("Administrar\nUsuarios del\nSistema");
		etiquetaUSistema.setBounds(153, vAlto+70, 70, 50);
		add(etiquetaUSistema);

		adminUSistema.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				adminUSistema adUS=new adminUSistema();
				setVisible(false);/*ocultar pantalla comun*/
			}
		}
		);

		/*Configuracion de la cuenta*/
		JButton configCuenta=new JButton(new ImageIcon("data/mAD/confCUEN.png"));
		configCuenta.setBounds(376, vAlto, 70, 70);
		configCuenta.setBackground(Color.WHITE);
		configCuenta.setBorderPainted(false);
		add(configCuenta);

		JTextArea etiquetaCCuenta=new JTextArea("Configurar\nCuenta");
		etiquetaCCuenta.setBounds(376, vAlto+70, 70, 50);
		add(etiquetaCCuenta);

		configCuenta.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				confiCuenta coCu=new confiCuenta();
				setVisible(false);/*ocultar pantalla comun*/
			}
		}
		);

		/*Boton Salir*/
		JButton botonSalir=new JButton("Salir");
		botonSalir.setBounds(530, 390, 70, 30);
		botonSalir.setBackground(Color.WHITE);
		botonSalir.setBorderPainted(false);
		add(botonSalir);

		botonSalir.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				/*Salida del sistema*/
				/*Se oculta la ventana del admin y se habre el inicio de sesion*/
				if(JOptionPane.showConfirmDialog(getContentPane(), "Cerrar sesion?", "Advertencia", JOptionPane.YES_NO_OPTION)==0)
	    		{
	    			JOptionPane.showMessageDialog(getContentPane(), "Desconectado --\nOK !!!");
	    			setVisible(false);
					inicio ini=new inicio();
	    		}
			}
		}
		);
		/*Boton Salir*/

		/*Caracteristicas de la pantalla*/
		setVisible(true);
		setResizable(false);
	    setSize(600, 450);
	    ventana=getSize();
	    pantalla=Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void keyPressed(KeyEvent keyEvent)
	{}

    public void keyReleased(KeyEvent keyEvent)
    {}

    public void keyTyped(KeyEvent keyEvent)
    {}

	public static void main(String...Args) throws SQLException
	{new comun();}
}