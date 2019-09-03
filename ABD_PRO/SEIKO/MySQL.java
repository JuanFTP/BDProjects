import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
class MySQL{
	static Connection conexion = null;
	public MySQL(){
		String servidor="jdbc:mysql://localhost/Proyecto";
		try{
			Class.forName("com.mysql.jdbc.Driver");
			//conexion=DriverManager.getConnection(servidor,"root","oscar");
			conexion=DriverManager.getConnection(servidor,"root","5826");
		}catch(ClassNotFoundException ex){
			JOptionPane.showMessageDialog(null,ex,"Error en la Conexión a la Base de Datos"+ex.getMessage(),JOptionPane.ERROR_MESSAGE);
			conexion=null;
		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null,ex,"Error en la Conexión a la Base de Datos"+ex.getMessage(),JOptionPane.ERROR_MESSAGE);
			conexion=null;
		}catch(Exception ex){
			JOptionPane.showMessageDialog(null,ex,"Error en la Conexión a la Base de Datos"+ex.getMessage(),JOptionPane.ERROR_MESSAGE);
			conexion=null;
		}finally{
			JOptionPane.showMessageDialog(null,"Conexión Exitosa");
		}
	}
	public static Connection getConnection(){
		return conexion;
	}
}