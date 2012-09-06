package tagcomp.text;

import java.util.Iterator;
import java.util.List;

/**
 * Clase utilitaria para manejo de texto
 * @author Juan Peralta
 *
 */
public class TextUtils
{
	/**
	 * Funcion que retorna el dígito verificador de un rut chileno
	 * @param rut
	 * @return digito verificador
	 * @throws NumberFormatException
	 * @TODO cambiar el retorno de la funcion a 'char'
	 */
	public static String dvRut(String rut) throws NumberFormatException
	{
		int m = 0, s = 1, t = Integer.parseInt(rut);

		for( ; t != 0; t /= 10)
		{
			s = (s + t % 10 * (9 - m++ % 6)) % 11;
		}
		return String.valueOf((char)(s != 0 ? s + 47 : 75));
	}
	
	/**
	 * Permite obtener un String separados por el parametro <b>separator<b> a partir de una lista
	 * @param listOfElements lista de elementos
	 * @param separator separador a utilizar para delimitar elementos en el resultado 
	 * @return un string que contiene la lista separada por el separador seleccionado
	 */
	public static String getStringFromList(List<?> listOfElements, String separator)
	{
		StringBuilder sb = new StringBuilder();
		for (Iterator<?> iterator = listOfElements.iterator(); iterator.hasNext();)
		{
			if(sb.length() != 0)
				sb.append(separator);
			sb.append(String.valueOf(iterator.next()));
		}
		return sb.toString();
	}
}
