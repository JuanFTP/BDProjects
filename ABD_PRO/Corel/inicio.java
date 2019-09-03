import javax.swing.UIManager.*;
import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

class inicio extends JFrame implements KeyListener
{
	private Dimension pantalla, ventana;
	public static enlace conector;
	public static Connection fix;
	public static Statement consulta=null;
	public static ResultSet resultado=null;
	public static usuariosEmergente="";

	/*ELEMENTOS DEL FRAME*/
	public JTextField campoLoggin;
	public static JComboBox comboLogin;
	public JPasswordField campoPassword;

	public inicio()
	{
		new JFrame();
		setTitle("Bienvenido");
		setLayout(null);
		getContentPane().setBackground(Color.WHITE);

		JLabel etiquetaOpciones=new JLabel("F2: LUsers   F3: Opciones   F4: Salir");
		etiquetaOpciones.setForeground(new Color(137, 135, 132));
		etiquetaOpciones.setBounds(75, 0, 200, 20);
		add(etiquetaOpciones);

		JLabel etiquetaICON=new JLabel(new ImageIcon("data/admin.png"));
		etiquetaICON.setBounds(79, 40, 192, 192);
		add(etiquetaICON);

		JLabel etiquetaUsuario=new JLabel("Usuario");
		etiquetaUsuario.setBounds(152, 240, 45, 20);
		add(etiquetaUsuario);

		JLabel etiquetaPassword=new JLabel("Password");
		etiquetaPassword.setBounds(145, 300, 60, 20);
		add(etiquetaPassword);

		campoLoggin=new JTextField();
		campoLoggin.setBounds(75, 260, 200, 30);
		campoLoggin.setHorizontalAlignment(JTextField.CENTER);
		add(campoLoggin);

		campoPassword=new JPasswordField();
		campoPassword.setBounds(75, 320, 200, 30);
		campoPassword.setHorizontalAlignment(JPasswordField.CENTER);
		add(campoPassword);

		/*Enventos de inicio de sesión*/
		campoLoggin.addKeyListener(this);
		campoPassword.addKeyListener(this);

		/*Zona de dibujo*/
		JLabel barner=new JLabel(new ImageIcon("data/fondo.png"));
		barner.setBounds(0, 380, 350, 50);
		add(barner);
		/*Zona de dibujos*/

		setVisible(true);
		setResizable(false);
	    setSize(350, 450);
	    ventana=getSize();
	    pantalla=Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void keyPressed(KeyEvent keyEvent)
	{
    	int tipo = keyEvent.getExtendedKeyCode();
    	if(tipo==113)//F2 lista de logins
    	{
    		try
    		{    			
				conector = new enlace(); fix=conector.getConnection();
				consulta = fix.createStatement();
				resultado = consulta.executeQuery("select login from manejador;");
				String logins="";

				while(resultado.next())
				{
					logins+="\n"+resultado.getString("login");
				}
				JOptionPane.showMessageDialog(this, "Usuarios:"+logins, "Lista de Logins", 1);
				fix.close(); consulta.close(); resultado.close();
    		}
    		catch(Exception ex)
    		{System.out.println("Error al acceder");ex.printStackTrace();}
    	}
    	else if(tipo==114)//F3
    	{JOptionPane.showMessageDialog(this, "Opciones", "Mensaje", 1);}
    	else if(tipo==115)//F4
    	{
    		if(JOptionPane.showConfirmDialog(this, "Desea salir?", "Advertencia", JOptionPane.YES_NO_OPTION)==0)
    		{System.exit(0);}
    	}
    	else if(tipo==10)//ENTER
    	{
			try
			{
				conector = new enlace(); fix = conector.getConnection();
				consulta=fix.createStatement();
				resultado=consulta.executeQuery("select * from manejador where login='"+campoLoggin.getText()+"';");
				if(resultado.next())
				{
					if(campoLoggin.getText().equalsIgnoreCase(resultado.getString("login")))
					{
						if((campoPassword.getText().equals(resultado.getString("password"))) && resultado.getInt("permisos")==1111)/*Si la contraseña es igual*/
    					{
    						JOptionPane.showMessageDialog(this, "Bienvenido: "+" "+resultado.getString("login")+"\n"+resultado.getString("nombreM")+" "+resultado.getString("apellidos"), "Bienvenido", 1);
    						administrador abd=new administrador(campoLoggin.getText()); /*Pantalla admin*/
    						setVisible(false);
    					}
    					else if(campoPassword.getText().equals(resultado.getString("password")))
    					{
    						JOptionPane.showMessageDialog(this, "Bienvenido: "+" "+resultado.getString("login")+"\n"+resultado.getString("nombreM")+" "+resultado.getString("apellidos"), "Bienvenido", 1);
    						usuario ubd = new usuario(campoLoggin.getText());/*Usuario no administrador*/
    						setVisible(false);
    					}
    					else
    					{JOptionPane.showMessageDialog(this, "Error en los datos, verifica\ntu informacion.", "Warning", 0);}
    				}
    				else
    				{JOptionPane.showMessageDialog(this, "Error en los datos, verifica\ntu informacion.", "Warning", 0);}
				}
				else
				{JOptionPane.showMessageDialog(this, "Error en los datos, verifica\ntu informacion.", "Warning", 0);}
				fix.close(); consulta.close(); resultado.close();
			}
			catch(Exception ex)
			{System.out.println("Error al acceder"); ex.printStackTrace();}
    	}
		else
		{/*Nada*/}
    }

    /*Combo de Logins*/
    public void agregarLogin()
    {
    	try
    	{
    		comboLogin.removeAllItems();/*Se eliminan todos los usuarios*/
    		conector = new enlace(); fix=conector.getConnection();
    		consulta = fix.createStatement();
    		resultado = consulta.executeQuery("select count(*), login, password from manejador;");
    		while(resultado.next)
    		{

    		}
    		fix.close(); consulta.close(); resultado.close();
    	}
    	catch(SQLException ex)
    	{JOptionPane.showMessageDialog(this, "Error al anadir los Logins al Combo", "Error al Vaciar", 0);}
    }

    public void keyReleased(KeyEvent keyEvent)
    {}

    public void keyTyped(KeyEvent keyEvent)
    {}

	public static void main(String...Args) throws SQLException
	{
		new inicio();
	}
}