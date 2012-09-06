package tagcomp.text;

/**
 * Clase que almacena validadores comunmente utilizados como una instancia única.
 * @author Daniel Langdon
 * TODO Testear expresiones regulares.
 * TODO Múltiples llamadas a estos validadores pueden ser lentas. String.matches() no precompila la expresión regular. Puede evaluarse el uso de java.util.regex.Pattern and java.util.regex.Matcher.
 */
public enum CommonValidator implements Validator
{
	/**
	 * De puro buena onda acepta cualquier texto.
	 * Se usa primordialmente en casos donde separar casos con o sin un validador nulo puede ser una molestia. 
	 */
	PASS {
		@Override
		public boolean validate(String str)
		{
			return true;
		}
	},
	
	/**
	 * Solo acepta ruts válidos.
	 */
	RUT {
		@Override
		public boolean validate(String str)
		{
			try
			{
				if(str.startsWith("-") || str.startsWith(".") || str.startsWith(","))
				{
					return false;
				}
				
				str = str.replace(".", "").replace(",", "").replace("-", "").replace("k", "K");
				String rutNoDv = str.substring(0, str.length() - 1);
				String dv = str.substring(str.length() - 1);
				if(INTEGER.validate(rutNoDv) && TextUtils.dvRut(rutNoDv).equals(dv))
				{
					return true;
				}
			}
			catch(Exception e)
			{
			}
			return false;
		}
	},
	
	/**
	 * Solo acepta números enteros positivos o 0.
	 */
	INTEGER {
		@Override
		public boolean validate(String str)
		{
			return str.matches("[0-9]+");
		}
	}, 
	
	/**
	 * Sólo acepta caracteres alfanuméricos.
	 */
	ALPHA{
		@Override
		public boolean validate(String str)
		{
			return str.matches("[a-zA-Z0-9áéíóú�?É�?ÓÚñÑüÜ ]+");
		}
	}, 
	
	/**
	 * Solo acepta fechas en el formato DD/MM/YYYY
	 */
	DATE{
		@Override
		public boolean validate(String str)
		{
			return str.matches("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/(19|20)[0-9][0-9]");
		}
	}, 
	
	/**
	 * Acepta cualquier nomenclatura para número decimal aceptada por Java.
	 * @see Double.parseDouble(str)
	 */
	DOUBLE{
		@Override
		public boolean validate(String str)
		{
			try
			{
				Double.parseDouble(str);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}
	}, 
	
	/**
	 * Acepta dirección de recaudación o pago: 'R' o 'P'
	 * TODO Esta definicion está solo por compatibilidad inversa. 
	 */
	@Deprecated
	DIRECCION{
		@Override
		public boolean validate(String str)
		{
			return str.equals("P") || str.equals("R");
		}
	}, 
	
	/** Acepta textos simples (alfanuméricos más espacios, puntuación y paréntesis). Por sobre todo, no acepta comillas de ningún tipo. */
	TEXT{
		@Override
		public boolean validate(String str)
		{
			return str.matches("[a-zA-Záéíóú�?É�?ÓÚñÑüÜ0-9;: _,\\.\\(\\)\\-]+");
		}
	}, 
	
	/**
	 * Acepta emails simples
	 */
	EMAIL{
		@Override
		public boolean validate(String str)
		{
			return str.matches("([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,4})");
		}
	};

	
	/**
	 * Método que setea la validación de un string
	 * @param str string a ser validado
	 * @return true si la validación es correcta
	 */
	public abstract boolean validate(String str);
	
	/**
	 * Método estático para evaluar listas, que a su vez pueden tener elementos de un tipo conocido.
	 */
	public static boolean validateList(String data, Validator validator, String separator)
	{
		for(String s : data.split(separator))
		{
			if(!validator.validate(s))
				return false;
		}
		return true;
	}
}

