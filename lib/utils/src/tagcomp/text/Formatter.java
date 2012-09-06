package tagcomp.text;

/**
 * Objetos que implementan esta interfaz son capaces de recibir un texto cualquiera y convertirlo a un formato particular.
 * Esta conversión podría requerir de ciertos supuestos sobre el texto recibido, los cuales deben estar correctamente explicados en la documentación de dichos Formatters. Como concepto general, en lo posible un formatter que detecta una anomalía debiera retornar el texto tal como lo recibiera, sin embargo, la validez del texto recibido debiera darse por contrato.
 * @author Daniel Langdon
 */
public interface Formatter
{
	/**
	 * Método que setea la validación de un string
	 * @param str String a ser formateado.
	 * @return String corregido de acuerdo al formato pedido.
	 */
	public abstract String format(String str);
}