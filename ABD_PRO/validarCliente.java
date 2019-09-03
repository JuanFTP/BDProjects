import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

class validarCliente
{
	enlace conector;
	Connection fix;
	Statement consulta;
	ResultSet resultado;
	static boolean respuesta=false;
	static String mensajeSalida="";
	JFrame window;
/*Inserccion*/
	/*Retorna true si la validacion es correcta*/
	public boolean validarParaInsercion(JFrame window, String nombre, String apellidos, String direccion, String telefono, String userUpper)
	{
		this.window = window;
		respuesta=false;
		if(nombre.equals("") || apellidos.equals("") || direccion.equals("") || telefono.equals(""))
		{JOptionPane.showMessageDialog(window, "Por favor completa todos los campos.", "Mensaje", 1); respuesta=false;}
		else
		{
			if(esCadena(nombre) && esCadena(apellidos) && esDireccion(direccion) && esNumeroTelefono(telefono))
			{
				if(!esDuplicado(nombre, apellidos, telefono))
				{
					String query = "insert into usuario (nombre, apellidos, direccion, telefono, login, fecha) values ('"+nombre+"', '"+apellidos+"', '"+direccion+"', '"+telefono+"', '"+userUpper+"', curdate());";
					System.out.println("Insert into Usuario: "+query);
					if(JOptionPane.showConfirmDialog(window, "Deseas continuar?", "Continuar", JOptionPane.YES_NO_OPTION)==0)
					{
						try
						{
							conector = new enlace(); fix = conector.getConnection();
							consulta = fix.createStatement();
							consulta.executeUpdate(query);
							JOptionPane.showMessageDialog(window, "Registro insertado correctamente.", "Mensaje", 1);
							respuesta=true;
						}
						catch(SQLException ex)
						{JOptionPane.showMessageDialog(window, "Error en la insercion de un nuevo cliente.  "+ex.getMessage(), "Error", 0);}
					}
				}
			}
			else
			{JOptionPane.showMessageDialog(window, "Error en los siguientes datos:\n"+mensajeSalida, "Error", 0); respuesta=false; mensajeSalida="";}
		}
		return respuesta;
	}

	public boolean esCadena(String cadena)
	{
		boolean r=false;
		for(int k=0; k<cadena.length(); k++)
		{
			if((cadena.charAt(k)>=65 && cadena.charAt(k)<=92) || cadena.charAt(k)>=97 && cadena.charAt(k)<=122 || cadena.charAt(k)==32)
			{r=true;}
			else
			{r=false; mensajeSalida+=": Nombre o Apellidos (Solo Letras):"; break;}
		}
		return r;
	}

	public boolean esNumeroTelefono(String cadena)
	{
		boolean r=false;
		try
		{
			if(cadena.length()==10)
			{Double.parseDouble(cadena); r=true;}
			else
			{mensajeSalida+=": Telefono (10 Digitos) :"; r=false;}
		}
		catch(Exception ex){mensajeSalida+=": Telefono (10 Digitos) :"; r=false;}
		return r;
	}

	public boolean esDireccion(String cadena)
	{
		boolean r=false;
		try
		{Integer.parseInt(cadena); mensajeSalida+=": Direccion (Letras y Numeros) :"; r=false;}
		catch(Exception ex)
		{r=true;}
		return r;
	}

	/*Retorna false si no hay duplicado y true si lo hay*/
	public boolean esDuplicado(String nombre, String apellidos, String telefono)
	{
		boolean r=false;
		try
		{
			conector = new enlace(); fix= conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery("select count(*) from usuario where nombre='"+nombre+"' and apellidos='"+apellidos+"';");
			if(resultado.next())
			{
				if(resultado.getInt("count(*)")==1)
				{JOptionPane.showMessageDialog(window, "El cliente ya existe, verifica los datos\nque estas agregando.", "Datos Duplicados", 0); r=true;}
				else
				{
					if(nombre.equals("modificar")) /*Para que extraiga todo menos el mismo telefono del usuario si en caso este lo deja igual*/
					{resultado = consulta.executeQuery("select count(*) from usuario where telefono = '"+telefono+"' and id<>"+apellidos+";");}//para modificar
					else
					{resultado = consulta.executeQuery("select count(*) from usuario where telefono = '"+telefono+"';");}//Para uno nuevo
					if(resultado.next())
					{
						if(resultado.getInt("count(*)")==1)
						{
							JOptionPane.showMessageDialog(window, "El telefono ya existe, verificalo.", "Telefono Duplicado", 0);
							r=true;;
						}
					}
				}
			}
			fix.close(); consulta.close(); resultado.close();
		}
		catch(SQLException ex)
		{JOptionPane.showMessageDialog(window, "Error al consultar esDuplicado: "+ex.getMessage(), "Error", 0);}
		return r;
	}
/*Insercion*/
/*Modificacion*/
	public ResultSet existe(String identificador)
	{
		ResultSet retorno=null;
		try
		{
			conector = new enlace(); fix = conector.getConnection();
			consulta = fix.createStatement();
			resultado = consulta.executeQuery("select count(*) from usuario where id="+identificador+";");
			if(resultado.next())
			{
				if(resultado.getInt("count(*)")==0)
				{retorno = null; JOptionPane.showMessageDialog(window, "Usuario no existe, verifica su ID.", "Mensaje", 0);}
				else if(resultado.getInt("count(*)")==1)
				{
					retorno = consulta.executeQuery("select * from usuario where id="+identificador+";");
				}
			}
			else
			{retorno = null; JOptionPane.showMessageDialog(window, "Error al consultar ID.", "Mensaje", 0);	}

		}
		catch(SQLException ex)
		{retorno = null; JOptionPane.showMessageDialog(window, "Error al consultar.", "Mensaje", 0);}
		return retorno;
	}
/*Modificacion*/
}