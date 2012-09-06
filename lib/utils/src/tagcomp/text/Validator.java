package tagcomp.text;

/**
 * Objetos que implementan esta interfaz son capaces de recibir un texto cualquiera y validar si cumple un formato concreto.
 * @author Daniel Langdon
 */
public interface Validator
{
	/**
	 * Método que setea la validación de un string
	 * @param str string a ser validado
	 * @return true si la validación es correcta
	 */
	public abstract boolean validate(String str);
}
