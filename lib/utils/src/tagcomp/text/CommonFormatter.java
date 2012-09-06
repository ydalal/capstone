package tagcomp.text;


/**
 * Clase que almacena formateadores comunmente utilizados como una instancia única.
 * TODO Esta clase se mantiene por usos históricos. A futuro (internalización, múltiples monedas) puede evaluarse usar com.google.gwt.i18n.client.NumberFormat
 * @author Daniel Langdon
 */
public enum CommonFormatter implements Formatter
{
	/**
	 * Deja pasar el texto tal como lo recibe. Es útil en ciertos casos para evitar validación constante con validadores nulos.
	 */
	PASS {
		@Override
		public String format(String str)
		{
			return str;
		}
	},
	
	/**
	 * Limpia un string de espacios y otros caracteres (,;-) •
	 */
	CLEAN_ALPHA {
		@Override
		public String format(String str)
		{
			StringBuilder ret = new StringBuilder();
			for(int i=0; i<str.length() ; i++)
			{
				if(Character.isLetterOrDigit(str.charAt(i)))
					ret.append(str.charAt(i));
			}
			return ret.toString();
		}
	},
	
	/**
	 * Reemplaza los caracteres por '•', de manera de ocultar la contraseña.
	 */
	PASSWORD {
		@Override
		public String format(String str)
		{
			StringBuilder ret = new StringBuilder();
			for(int i=0; i<str.length() ; i++)
				ret.append("•");
			
			return ret.toString();
		}
	},
	
	/**
	 * Limpia un string todo lo que no sean dígitos, a excepción de coma ','.
	 * Retorna un valor decimal en formato latino, por ejemplo: '1.234.567,89'. 
	 */
	LATIN_NUMBER {
		@Override
		public String format(String str)
		{
			// Mantengo los decimales
			int coma = str.indexOf(',');
			if(coma < 0)
				coma = str.length();
			
			// Extraigo todos los dígitos presentes
			StringBuilder ret = new StringBuilder();
			for(int i=0; i<coma ; i++)
			{
				if(Character.isDigit(str.charAt(i)))
					ret.append(str.charAt(i));
			}
			
			// Agrego los puntos.
			//ret = ret.reverse(); <-- No aceptado por GWT al 24/01/2011
			for(int i=ret.length()%3; i<ret.length(); i+=4)
			{
				ret.insert(i, '.');
			}
			if(ret.charAt(0) == '.')
				ret.deleteCharAt(0);
			
			// Agrego los dígitos decimales
			for(int i=coma; i<str.length() ; i++)
			{
				if(Character.isDigit(str.charAt(i)) || str.charAt(i) == ',')
					ret.append(str.charAt(i));
			}
			
			return ret.toString();
		}
	},
	
	/**
	 * Retorna un valor equivalente a LATIN_NUMBER, pero agregando el signo '$' al inicio.
	 */
	MONEY {
		@Override
		public String format(String str)
		{
			return '$' + LATIN_NUMBER.format(str);
		}
	},
	
	/**
	 * Retorna un rut, usando LATIN_NUMBER para el formato inicial y agregando '-' + dígito verificador.
	 * El dígito verificador debe venir en el string de origen, en caso de 'k' se transforma a 'K'.
	 */
	RUT {
		@Override
		public String format(String str)
		{
			return LATIN_NUMBER.format(str.substring(0, str.length()-1)) + '-' + Character.toUpperCase(str.charAt(str.length()-1));
		}
	};
	
	/**
	 * Método que setea la validación de un string
	 * @param str string a ser validado
	 * @return true si la validación es correcta
	 */
	public abstract String format(String str);
}

