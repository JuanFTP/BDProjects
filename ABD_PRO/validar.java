import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

class validar
{
	boolean result;
	static enlace conexion;
	static Statement consulta;
	static ResultSet resultado;

	public boolean esCadena(String cadena)// retorna true si es cadena
	{
		boolean res=false;
		for(int k=0; k<cadena.length(); k++)
		{
			if((cadena.charAt(k)>=65  && cadena.charAt(k)<=90) || (cadena.charAt(k)>=97  && cadena.charAt(k)<=122) || cadena.charAt(k)==32) //mayusculas
			{res=true;}
			else
			{
				res=false;
				break;
			}
		}
		return res;
	}

	public boolean esEntero(String cadena)//Retorna True si es un numero
	{
		try
		{Integer.parseInt(cadena);}
		catch(Exception ex)
		{result = false;}
		return result;
	}

	public boolean esLogin(JFrame window, String cadena)//Verifica las reglas para el Login
	{
		result=true;
		conexion = new enlace();
		Connection fix = conexion.getConnection();
		
		//Longitud del login
		if(cadena.length()>=7 && cadena.length()<=10)
		{
			if(esEntero(cadena))
			{JOptionPane.showMessageDialog(window, "Login no valido, no puede ser solo\nnumeros.", "Login No Valido", 0); result=false;}
			else
			{
				result=true;//como no es entero entonces es correcto hasta este punto
				try
				{
					/*Checa la existencia del login*/
					consulta = fix.createStatement();
					resultado = consulta.executeQuery("select count(*) from manejador where login = '"+cadena+"';");
					if(resultado.next())//Si hay un login, ya
					{
						System.out.println("Resultado del login: "+cadena+" resultado: "+resultado.getInt("count(*)"));
						if(resultado.getInt("count(*)")!=0)//si hay ya un login
						{JOptionPane.showMessageDialog(window, "Login ya existente. Ingresa un\nLogin distinto.", "Login Duplicado", 0); result=false;}
					}
				}
				catch(SQLException ex)
				{System.out.println("Error en la consulta del login.");}
			}
		}
		else
		{JOptionPane.showMessageDialog(window, "Login no valido, debe ser\nde 7 a 10 caracteres.", "Login No Valido", 0); result=false;}
		return result;
	}

	public boolean esLoginMOD(JFrame window, String cadena)//Verifica las reglas para el Login
	{
		result=true;
		conexion = new enlace();
		Connection fix = conexion.getConnection();
		
		//Longitud del login
		if(cadena.length()>=7 && cadena.length()<=10)
		{
			if(esEntero(cadena))
			{JOptionPane.showMessageDialog(window, "Login no valido, verificalo.", "Login No Valido", 0); result=false;}
			else
			{
				result=true;//como no es entero entonces es correcto hasta este punto
				try
				{
					/*Checa la existencia del login*/
					consulta = fix.createStatement();
					resultado = consulta.executeQuery("select count(*) from manejador where login = '"+cadena+"';");
					if(resultado.next())//Si hay un login, ya
					{
						System.out.println("Resultado del login: "+cadena+" resultado: "+resultado.getInt("count(*)"));
						if(resultado.getInt("count(*)")==0)//si hay ya un login
						{JOptionPane.showMessageDialog(window, "El login no existe. Ingresa un\nLogin valido.", "Login no existe", 0); result=false;}
					}
				}
				catch(SQLException ex)
				{System.out.println("Error en la consulta del login.");}
			}
		}
		else
		{JOptionPane.showMessageDialog(window, "Login no valido, debe ser\nde 7 a 10 caracteres.", "Login No Valido", 0); result=false;}
		return result;
	}

	public boolean verificaPassword(JFrame window, String cadena1, String cadena2)//comparacion de dos Password
	{
		if(cadena1.isEmpty() || cadena2.isEmpty())//que ningun campo este vacio de las del password
		{
			if(cadena1.isEmpty())
			{JOptionPane.showMessageDialog(window, "Se requiere un password para tu cuenta.", "Password requerido", 0); result=false;}
			else
			{
				if(cadena2.isEmpty())
				{JOptionPane.showMessageDialog(window, "Por favor confirma el tu Password.", "Comprobacion requerida", 0); result=false;}
			}
		}
		else
		{
			if((cadena1.length()>=5 && cadena1.length()<=20) && cadena2.length()>=5 && cadena2.length()<=20)
			{
				if(!cadena1.equals(cadena2))//si los password no son iguales
				{JOptionPane.showMessageDialog(window, "Los Passwords no coinciden\n rectificalos por favor.", "Password No Valido", 0); result=false;}
			}
			else
			{JOptionPane.showMessageDialog(window, "Password no valido, debe ser\nde 5 a 20 caracteres.", "Password No Valido", 0); result=false;}
		}
		return result;
	}

	public boolean esDuplicado(JFrame window, String cad1, String cad2, String cad3)//Verifica la duplicidad de un registro
	{
		if(cad1.isEmpty() || cad2.isEmpty() || cad3.isEmpty())//si alguno de los campos nombre, apellido y direccion estan vacios
		{
			JOptionPane.showMessageDialog(window, "Por favor completa los campos:\nNombre, Apellidos y Direccion.", "Campos Vacios", 0); result=false;
		}
		else
		{
			conexion = new enlace();
			Connection fix = conexion.getConnection();
			try
			{
				consulta = fix.createStatement();
				if(esCadena(cad1) && esCadena(cad2))/*si nombre y apellido son cadenas*/
				{
					resultado = consulta.executeQuery("select count(*) from manejador where nombreM = '"+cad1+"' and apellidos = '"+cad2+"';");
					if(resultado.next())
					{
						if(resultado.getInt("count(*)")==1)//registro duplicado
						{JOptionPane.showMessageDialog(window, "Este Usuario ya tiene una cuenta,\nverifica los datos que estas agregando.", "Registro Duplicado", 0); result=false;}
						else
						{
							resultado = consulta.executeQuery("select count(*) from manejador where nombreM = '"+cad1+"' and apellidos = '"+cad2+"' and direccion = '"+cad3+"';");
							if(resultado.next())//segunda consulta para los 3 campos
							{
								if(resultado.getInt("count(*)")==1)//Duplicidad en 3 campos
								{JOptionPane.showMessageDialog(window, "Propietario de la cuenta, duplicado\nverifica los datos que estas agregando.", "Registro Duplicado", 0); result=false;}
							}
						}
					}
				}
				else
				{result=false;}
			}
			catch(SQLException ex)
			{JOptionPane.showMessageDialog(window, "Error registro duplicado, verifica\nlos datos que estas agregando.", "Datos Duplicados", 0); result = false;}
		}
		return result;
	}
}