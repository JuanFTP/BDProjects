import java.sql.*;
import javax.swing.*;
public class MySQL
{
	static String server="jdbc:mysql://localhost:3306/agua";
	static String user="root";
	static String password="5826";

    public static Connection conexion=null;  
    /*Obtencion de la conexion*/
    public static Connection getConnection()
    {
        conexion=null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conexion= DriverManager.getConnection(server, user, password);
        }
        catch(ClassNotFoundException ex)
        {JOptionPane.showMessageDialog(null, ex, "Error1 en la Conexion con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE); conexion=null;}
        catch(SQLException ex)
        {JOptionPane.showMessageDialog(null, ex, "Error2 en la Conexion con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE); conexion=null;}
        catch(Exception ex)
        {JOptionPane.showMessageDialog(null, ex, "Error3 en la Conexion con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE); conexion=null;}
        finally
        {return conexion;}
    }
}