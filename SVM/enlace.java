import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class enlace {
	static String servidor = "jdbc:mysql://localhost:3306/ventas";
	static String usuario = "root";
	static String password="5826";

	public static Connection conexion=null;  
    /*Obtencion de la conexion*/
    public static Connection getConnection()
    {
        conexion=null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conexion= DriverManager.getConnection(servidor, usuario, password);
        }
        catch(ClassNotFoundException ex)
        {JOptionPane.showMessageDialog(null, ex, "Error 1 en la Conexion con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE); conexion=null;}
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error 2 en la Conexion con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE); conexion=null;
        }
        catch(Exception ex)
        {JOptionPane.showMessageDialog(null, ex, "Error 3 en la Conexion con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE); conexion=null;}
        finally
        {return conexion;}
    }	
}