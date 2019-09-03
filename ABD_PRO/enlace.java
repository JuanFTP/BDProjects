import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class enlace
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
        {JOptionPane.showMessageDialog(null, ex, "Error 1 en la Conexion con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE); conexion=null;}
        catch(SQLException ex)
        {
            //JOptionPane.showMessageDialog(null, ex, "Error 2 en la Conexion con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE); conexion=null;
            if(JOptionPane.showConfirmDialog(null, "La base de datos no existe, deseas crearla?", "Crear: Base de datos : AGUA", JOptionPane.YES_NO_OPTION)==0)
            {
                newDataBase("data/dataBase.sql");
                JOptionPane.showMessageDialog(null, "Base de datos creada CORRECTAMENTE\nVUELVE A INICIAR el sistema para continuar.", "Finalizado", 1);
                System.exit(0);
            }
            else
            {
               JOptionPane.showMessageDialog(null, "No se puede continuar hasta que se cree la BD", "Imposible Continuar", 0); conexion=null;
               System.exit(0);
            }
        }
        catch(Exception ex)
        {JOptionPane.showMessageDialog(null, ex, "Error 3 en la Conexion con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE); conexion=null;}
        finally
        {return conexion;}
    }

    public static void newDataBase(String dataMain)
    {
        try
        {
            /*Cambio de server*/
            server ="jdbc:mysql://localhost:3306/";
            Connection fix = getConnection();
            Statement consulta = fix.createStatement();

            File archivo=new File(dataMain); //Creacion del fichero-objeto
            String lineaTemp=""; //Linea de lectura temporal
            BufferedReader flujoArchivo=new BufferedReader(new FileReader(archivo)); //Flujo de lectura para el archivo

            lineaTemp=flujoArchivo.readLine(); //primera lectura
            int line = 1;
            while(lineaTemp!=null) //mientras el archivo no este vacio
            {
                if(lineaTemp!=null) //para evitar el error de linea vacia
                {
                    System.out.println(line+" : "+lineaTemp);
                    consulta.execute(lineaTemp);
                    lineaTemp=flujoArchivo.readLine(); //lectura de una nueva linea
                    line++;
                }
            }

            flujoArchivo.close(); //se cierra el flujo de lectura
            fix.close(); consulta.close();
            //JOptionPane.showMessageDialog(null, "Base de datos creada CORRECTAMENTE\nVUELVE A INICIAR el sistema para continuar.", "Finalizado", 1);
            //System.exit(0);
        }
        catch(IOException ex)
        {JOptionPane.showMessageDialog(null, "Error fatal en la lectura del ARCHIVO"+ex.getMessage(), "END", 0);}
        catch(SQLException ex)
        {JOptionPane.showMessageDialog(null, "Error SQLException\nError: "+ex.getMessage(), "END", 0);}
    }

    public static void newBackupDatabase(String location, int tipo)
    {
        try
        {/*escritura.write(newLine); escritura.newLine();*/
            /*Query*/
            Connection fix = getConnection();
            Statement consulta = fix.createStatement();
            ResultSet resultado=null;
            /*Query*/
            /*Obtener date time*/
            if(tipo==0)/*Respaldo Local*/
            {
                resultado = consulta.executeQuery("select curdate() as 'fecha', curtime() as 'hora'");
                if(resultado.next())
                {
                    StringTokenizer token=new StringTokenizer(resultado.getString("hora"));
                    String hora = "";
                    while(token.hasMoreTokens())
                    {
                        hora+=token.nextToken(":").toString()+"-";
                    }
                    location+=resultado.getString("fecha")+"_"+hora.substring(0, hora.length()-1)+".ARM";
                }
            }
            else if(tipo==1)/*Respaldo externo*/
            {
                location+=".ARM";
            }
            /*Consulta index*/
            String query = "select (select count(*) from manejador) as 'M', ";
            query+= "(select count(*) from usuario) as 'U', ";
            query+="(select count(*) from contrato) as 'C', ";
            query+="(select count(*) from pago) as 'P', ";
            query+="(select count(*) from ingreso) as 'I', ";
            query+="(select count(*) from egreso) as 'E';";
            String newLine="";
            resultado = consulta.executeQuery(query);/*Resultados*/
            /*Archivo*/
            File archivo =  new File(location);
            BufferedWriter escritura=new BufferedWriter(new FileWriter(archivo));
            /*Archivo*/
            if(resultado.next())
            {
                /*El caracter '^' separa los tokens*/
                int [] vecRes = new int [6];
                vecRes[0] = resultado.getInt("M"); vecRes[1] = resultado.getInt("U"); vecRes[2] = resultado.getInt("C"); vecRes[3] = resultado.getInt("P"); vecRes[4] = resultado.getInt("I"); vecRes[5] = resultado.getInt("E");
                if(vecRes[0]>0)/*Si hay Manejadores*/
                {
                    /*Cabecera de Manejador*/
                    escritura.write("manejador^"); escritura.newLine();
                    resultado = consulta.executeQuery("select * from manejador;");
                    while(resultado.next())
                    {
                        newLine = "'"+resultado.getString("login")+"'^";
                        newLine+= "'"+resultado.getString("password")+"'^";
                        newLine+= "'"+resultado.getString("nombreM")+"'^";
                        newLine+= "'"+resultado.getString("apellidos")+"'^";
                        newLine+= "'"+resultado.getString("direccion")+"'^";
                        newLine+= "'"+resultado.getString("permisos")+"'^";
                        newLine+= "'"+resultado.getString("fecha")+"'";
                        escritura.write(newLine); escritura.newLine();
                    }
                }
                if(vecRes[1]>0)/*Si hay Usuarios*/
                {
                    /*Cabecera de Manejador*/
                    escritura.write("usuario^"); escritura.newLine();
                    resultado = consulta.executeQuery("select * from usuario;");
                    while(resultado.next())
                    {
                        newLine = resultado.getString("id")+"^";
                        newLine+= "'"+resultado.getString("nombre")+"'^";
                        newLine+= "'"+resultado.getString("apellidos")+"'^";
                        newLine+= "'"+resultado.getString("direccion")+"'^";
                        newLine+= "'"+resultado.getString("telefono")+"'^";
                        newLine+= "'"+resultado.getString("login")+"'^";
                        newLine+= "'"+resultado.getString("fecha")+"'";
                        escritura.write(newLine); escritura.newLine();
                    }
                }
                if(vecRes[2]>0)/*Si hay Contratos*/
                {
                    /*Cabecera de Contrato*/
                    escritura.write("contrato^"); escritura.newLine();
                    resultado = consulta.executeQuery("select * from contrato;");
                    while(resultado.next())
                    {
                        newLine = resultado.getString("folio")+"^";
                        newLine+= "'"+resultado.getString("fecha")+"'^";
                        newLine+= "'"+resultado.getString("descripcion")+"'^";
                        newLine+= "'"+resultado.getString("tipoContrato")+"'^";
                        newLine+= "'"+resultado.getString("estado")+"'^";
                        newLine+= resultado.getString("id")+"^";
                        newLine+= "'"+resultado.getString("login")+"'^";
                        newLine+= "'"+resultado.getString("fechaM")+"'";
                        escritura.write(newLine); escritura.newLine();
                    }
                }
                if(vecRes[3]>0)/*Si hay Pagos*/
                {
                    /*Cabecera de Pago*/
                    escritura.write("pago^"); escritura.newLine();
                    resultado = consulta.executeQuery("select * from pago;");
                    while(resultado.next())
                    {
                        newLine = resultado.getString("folio")+"^";
                        newLine+= "'"+resultado.getString("descripcion")+"'^";
                        newLine+= resultado.getString("importe")+"^";
                        newLine+= "'"+resultado.getString("fechaP")+"'^";
                        newLine+= resultado.getString("folioC")+"^";
                        newLine+= resultado.getString("id")+"^";
                        newLine+= "'"+resultado.getString("login")+"'";
                        escritura.write(newLine); escritura.newLine();
                    }
                }
                if(vecRes[4]>0)/*Si hay Ingresos*/
                {
                    /*Cabecera de ingreso*/
                    escritura.write("ingreso^"); escritura.newLine();
                    resultado = consulta.executeQuery("select * from ingreso;");
                    while(resultado.next())
                    {
                        newLine = resultado.getString("id")+"^";
                        newLine+= "'"+resultado.getString("fecha")+"'^";
                        newLine+= "'"+resultado.getString("descripcion")+"'^";
                        newLine+= resultado.getString("importe")+"^";
                        newLine+= "'"+resultado.getString("login")+"'";
                        escritura.write(newLine); escritura.newLine();
                    }
                }
                if(vecRes[5]>0)/*Si hay Egresos*/
                {
                    /*Cabecera de egreso*/
                    escritura.write("egreso^"); escritura.newLine();
                    resultado = consulta.executeQuery("select * from egreso;");
                    while(resultado.next())
                    {
                        newLine = resultado.getString("id")+"^";
                        newLine+= "'"+resultado.getString("fecha")+"'^";
                        newLine+= "'"+resultado.getString("descripcion")+"'^";
                        newLine+= resultado.getString("importe")+"^";
                        newLine+= "'"+resultado.getString("login")+"'";
                        escritura.write(newLine); escritura.newLine();
                    }
                }
                JOptionPane.showMessageDialog(null, "Respaldo Completado: CORRECTAMENTE\nGuardado en: "+location, "Mensaje", 1);
            }
            /*Cierra Archivo*/
            fix.close();
            consulta.close();
            resultado.close();
            escritura.close();
            /*Cierra Archivo*/
        }
        catch(IOException ex)
        {JOptionPane.showMessageDialog(null, "Error en newBackupDatabase: [Archivo : "+location+"] :\n"+ex.getMessage(), "Error", 0);}
        catch(SQLException sx)
        {JOptionPane.showMessageDialog(null, "Error SQLException:\n"+sx.getMessage(), "Error SQLException", 0);}
    }

    /*
        File archivo = new File("Intermedio.cnotI");
        BufferedWriter escritura = new BufferedWriter(new FileWriter (archivo));
        escritura.close();
    */

    /*Restauracion*/
    public static void restore(String source)
    {
        try
        {
        	System.out.println(source);
        	backupAndCreateDatabase();/*crea una copia de seguridad, borra la base de datos y la crea de nuevo*/
        	/*CHECAR LA ELIMINACION DE LA BASE DE DATOS*/
            /*Query*/
            server ="jdbc:mysql://localhost:3306/agua";/*Se seleccciona la base de datos*/
            Connection fix = getConnection();
            Statement consulta = fix.createStatement();
            /*ResultSet resultado = null;*/
            /*Query*/
            /*Archivo*/
            File archivo=new File(source); //Creacion del fichero-objeto
            BufferedReader lectura=new BufferedReader(new FileReader(archivo)); //Flujo de lectura para el archivo
            String linea = ""; //Linea de lectura temporal
            linea=lectura.readLine(); //primera lectura
            int index = 0;/*indicara que tabla es la que se tiene en el token de 1*/
            /*Indicador de que tabla se leyó
             *1 = manejador
             *2 = usuario
             *3 = contrato
             *4 = pago
             *5 = ingreso
             *6 = egreso
             */
            /*Ciclo de lectura*/
            while(linea!=null) //mientras el archivo no este vacio
            {
                if(linea!=null) //para evitar el error de linea vacia
                {
                	StringTokenizer stk = new StringTokenizer(linea, "^");/*Para Optener los tokens*/
                	if(stk.countTokens()==1)/*Es una tabla*/
                	{
                		String tabla = stk.nextToken().toString();/*extraccion de la tabla*/
                		//System.out.println(tabla);/*el que indicara en que tabla se está insertando*/
                		if(tabla.equals("manejador")){index=1;}
                		else if(tabla.equals("usuario")){index=2;}
                		else if(tabla.equals("contrato")){index=3;}
                		else if(tabla.equals("pago")){index=4;}
                		else if(tabla.equals("ingreso")){index=5;}
                		else if(tabla.equals("egreso")){index=6;}
                	}
                	else
                	{
                		String insert = "";/*Query para insertar*/
                		/*Cuando no sea una tabla*/
                		if(index==1)/*es la tabla manejador*/
                		{
                			insert = "insert into manejador values (";
                			insert += stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken();
                			insert +=");";
                			consulta.executeUpdate(insert);
                			System.out.println("Query Manejador: "+insert);
                		}
                		else if(index==2)/*tabla usuario*/
                		{
                			insert = "insert into usuario values (";
                			insert += stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken();
                			insert +=");";
                			consulta.executeUpdate(insert);
                			System.out.println("Query Usuario: "+insert);
                		}
                		else if(index==3)/*tabla contrato*/
                		{
                			insert = "insert into contrato values (";
                			insert += stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken();
                			insert +=");";
                			consulta.executeUpdate(insert);
                			System.out.println("Query Contrato: "+insert);
                		}
                		else if(index==4)/*tabla pago*/
                		{
                			insert = "insert into pago values (";
                			insert += stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken();
                			insert +=");";
                			consulta.executeUpdate(insert);
                			System.out.println("Query Pago: "+insert);	
                		}
                		else if(index==5)/*tabla ingreso*/
                		{
                			insert = "insert into ingreso values (";
                			insert += stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken();
                			insert +=");";
                			consulta.executeUpdate(insert);
                			System.out.println("Query Ingreso: "+insert);
                		}
                		else if(index==6)/*tabla egreso*/
                		{
                			insert = "insert into egreso values (";
                			insert += stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken()+", "+stk.nextToken();
                			insert +=");";
                			consulta.executeUpdate(insert);
                			System.out.println("Query Egreso: "+insert);
                		}
                	}
                    //System.out.println(line+" : "+linea);
                    linea=lectura.readLine(); //lectura de una nueva linea
                }
            }
            lectura.close(); //se cierra el flujo de lectura
            /*Cierra Archivo*/
            fix.close();
            consulta.close();
            /*Cierra Archivo*/
            JOptionPane.showMessageDialog(null, "Restauracion Completada : "+source+"\nEl sistema se cerrara, vuelve a iniciar.", "Restauracion completada", 1);
            System.exit(0);
        }
        catch(IOException ex)
        {JOptionPane.showMessageDialog(null, "Error en newBackupDatabase: [Archivo : "+source+"] :\n"+ex.getMessage(), "Error", 0);}
        catch(SQLException ex)
        {JOptionPane.showMessageDialog(null, "Error en newBackupDatabase: [SQL : \n"+ex.getMessage(), "Error", 0);}
    }

    /*Se crea una nueva copia de seguridad antes de eliminar la base de datos*/
    public static void backupAndCreateDatabase()
    {
    	try
    	{
    		/*se crea una copia se seguridad antes de eliminar*/
    		newBackupDatabase("data/backup/", 0);/*Respaldo Local*/
    		server ="jdbc:mysql://localhost:3306/";
            Connection fix = getConnection();
            Statement consulta = fix.createStatement();
            consulta.execute("drop database agua;");
            newDataBase("data/dataBase.sql");/*se crea la base de datos*/
            fix.close();
            consulta.close();
    	}
    	catch(SQLException ex)
    	{JOptionPane.showMessageDialog(null, "Error en backupAndCreateDatabase", "SQLException", 0);}
    }

    /*public static void main(String...Args)
    {
    	new enlace().restore("data/backup/2016-05-26_21-41-43");
    }*/
    
    public static void listFiles(String location)
    {
    	JFrame wRecuperar = new JFrame("Restaurar");
    	wRecuperar.setLayout(null); wRecuperar.getContentPane().setBackground(Color.WHITE);
    	JComboBox menuRecuperar = new JComboBox();
    	menuRecuperar.addItem("Selecciona un punto de restauracion");
    	menuRecuperar.setBounds(10, 20, 300, 30);
    	menuRecuperar.setBackground(Color.WHITE);
	    wRecuperar.add(menuRecuperar);
	    /*Lista de archivos que existen*/
    	File beta = new File(location);
        String [] a = beta.list();
        for(int k=0; k<a.length; k++)
        {menuRecuperar.addItem(a[k]);}
	    
        wRecuperar.setSize(400, 300);
        wRecuperar.setVisible(true);
        wRecuperar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}