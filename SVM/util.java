import java.util.*;

class util {

	/*Evalua si una cadena es entera y si esta tiene una longitud determinada
	el 0 indica que no se requiere comprobar la  longitud del numero*/
	public boolean esEntero(String value, int dim){
		try {
			if(dim==0){
				Integer.parseInt(value);
				return true;
			} else {
				Integer.parseInt(value);
				if(dim!=value.length()){
					return false;
				} else {
					return true;
				}
			}
		} catch(Exception ex){
			return false;
		}
	}

	/*Determina si una cadena es doble*/
	public boolean esDoble(String value){
		try{
				if(value.isEmpty()){
					return false;
				} else {
					Double.parseDouble(value);
					return true;
				}
			} catch(Exception ex){
				return false;
			}
	}

	/*Determina si la cadena contiene solo texto*/
	public boolean esCadena(String value){
		boolean band = true;
		for (int x=0; x<value.length(); x++) {
			if(!((value.charAt(x)>=65 && value.charAt(x)<=90) || (value.charAt(x)>=97 && value.charAt(x)<=122) || (value.charAt(x)==32))){
				band = false;
			}
		}
		return band;
	}
}