/*Pag 821 de la biblia de java*/
import java.lang.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

class TablaModelo extends AbstractTableModel{
		ResultSet registro = null;
		final String[] columnNames = Principal.cad;
		//Object[][] data = null;
		final Object[][] data = {
			 {"--","--", "--", "--","--", "--", "--"}
		 };
		 //retorna el numero de columnas
		 public int getColumnCount(){
		 	return columnNames.length;
		 }
		 //retorna el numero de elementos
		 public int getRowCount() {
		 	return data.length;
		 }
		 //retornamos el elemento indicado
		 public String getColumnName(int col) {
		 	return columnNames[col];
		 }
		 //y lo mismo para las celdas
		 public Object getValueAt(int row, int col) {
		 	return data[row][col];
		 }
		 
		 //Determina si las celdas se pueden editar
		 public boolean isCellEditable(int row, int col) {
		 	return false;
		 }
		 /*
		 * No tienes que implementar este método a menos que
		 * los datos de tu tabla cambien
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
}