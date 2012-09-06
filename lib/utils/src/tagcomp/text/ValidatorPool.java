package tagcomp.text;

import java.util.HashMap;
import java.util.Map;

import tagcomp.text.Validator;


/**
 * Pool de validadores. Busca resolver la problemática de manejar variados tipos de campos con validaciones específicas.
 * Esta clase agrupa un conjunto de tipos de campos agrupados por su nombre, y su validador asociado. La idea 
 * es que cada proyecto genere sus validaciones particulares extendiendo de aquí y armando el mapa en el constructor especifico.
 * @author Juan Peralta
 *
 */
public class ValidatorPool
{
	private Map<String, Validator> pool;
	
	/**
	 * Constructor. Requerido para inicializar pool de validaciones.
	 */
	public ValidatorPool()
	{
		pool = new HashMap<String, Validator>();
	}
	
	/**
	 * Valida el texto con la validacion definida para la clave dada.
	 * @param key clave por la cual se validara el texto
	 * @param textToValidate texto a validar
	 * @return true si el texto aprueba la validacion, false si no
	 */
	public boolean validate(String key, String textToValidate)
	{
		return pool.get(key).validate(textToValidate);
	}
	
	/**
	 * Agrega un par key-validador al pool de validaciones.
	 * @param key clave de validacion
	 * @param validator validacion
	 * @return el validador previo para ese key o null si no se tenia un valor previo
	 */
	protected Validator put(String key, Validator validator)
	{
		return pool.put(key, validator);
	}
	
	public boolean contains(String key)
	{
		return pool.containsKey(key);
	}
}
