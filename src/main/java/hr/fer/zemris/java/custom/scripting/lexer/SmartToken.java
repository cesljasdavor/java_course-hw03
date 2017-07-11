package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Pomoćni razred koji se koristi prilikom leksičke analize ulaznog niza
 * primjerkom razreda {@link SmartScriptLexer}. Razred predstavlja jedan token
 * (leksičku jedinku). Primjerci ovog razreda su nepromjenjivi.
 * 
 * @author Davor Češljaš
 */
public class SmartToken {

	/** Tip leksičke jedinke */
	private SmartTokenType type;

	/** Vrijednost leksičke jedinke */
	private Object value;

	/**
	 * Konstruktor koji inicijalizira atribute leksičke jedinke
	 *
	 * @param type
	 *            tip leksičke jedinke
	 * @param value
	 *            vrijednost leksičke jedinke
	 */
	public SmartToken(SmartTokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Dohvaća tip leksičke jedinke
	 *
	 * @return tip leksičke jedinke
	 */
	public SmartTokenType getType() {
		return type;
	}

	/**
	 * Dohvaća vrijednost leksičke jedinke
	 *
	 * @return vrijednost leksičke jedinke
	 */
	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Token type is: " + type + " and token value is " + value.toString();
	}
}
