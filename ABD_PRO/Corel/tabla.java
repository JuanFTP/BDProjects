import javax.swing.*;
import java.util.logging.Logger;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import javax.swing.event.*;
import javax.swing.table.*;

class tabla extends AbstractTableModel
{
	public static Statement consulta=null;
	public static ResultSet resultado=null;
	public static enlace conectar = new enlace();
	
	public static String [] colNa;
	public static Object [][] dat;

	public void visaulizarDatos(String tablaI, String query)
	{
		if(tablaI.equals("manejador"))
		{manejadorT(query);}//Tabla para manejador
	}

	 //retorna el numero de columnas
	 public int getColumnCount()
	 {
	 	return colNa.length;
	 }
	 //retorna el numero de elementos
	 public int getRowCount()
	 {
	 	return dat.length;
	 }
	 //retornamos el elemento indicado
	 public String getColumnName(int col)
	 {
	 	return colNa[col];
	 }
	 //y lo mismo para las celdas
	 public Object getValueAt(int row, int col)
	 {
	 	return dat[row][col];
	 }
	 
	 //Determina si las celdas se pueden editar
	 public boolean isCellEditable(int row, int col)
	 {
	 	return false;
	 }
	 /*
	 * No tienes que implementar este método a menos que
	 * los datos de tu tabla cambien
	 */
	public void setValueAt(Object value, int row, int col)
	{
		dat[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	/*Datos de acuerdo a cada tabla*/
	public static void manejadorT(String query)
	{
		String [] columnNames = {"Login","User","Nombre","Apellidos","Direccion","Permisos","Fecha"};//Encabezados
		Object [][] datos = {{"---", "---", "---", "---", "---", "---", "---"}};//Linea default
		colNa = columnNames; //Globalizar Datos
		dat = datos; //Globalizar Datos

		Connection conexion = conectar.getConnection();//Obtener enlace
		consulta=null;
		resultado=null;

		try
		{
			consulta = conexion.createStatement(); //Abrir el flujo de consulta
			resultado = consulta.executeQuery(query);//consulta
			if(resultado.next())//si hay resultados
			{
				
			}
			else
			{

			}
			conexion.close();
			consulta.close();
		}
		catch(Exception ex)
		{/*Logger.getLogger(Principal.class.getName()).log(Level.SEVERE,null,ex);*/}
	}
}