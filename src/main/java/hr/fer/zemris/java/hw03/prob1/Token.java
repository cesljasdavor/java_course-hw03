package hr.fer.zemris.java.hw03.prob1;

/**
 * Pomoćni razred koji se koristi prilikom leksičke analize ulaznog niza
 * primjerkom razreda {@link Lexer}. Razred predstavlja jedan token (leksičku
 * jedinku). Primjerci ovog razreda su nepromjenjivi.
 * 
 * @author Davor Češljaš
 */
public class Token {

	/** Tip leksičke jedinke */
	private TokenType type;

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
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Dohvaća tip leksičke jedinke
	 *
	 * @return tip leksičke jedinke
	 */
	public TokenType getType() {
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

}
