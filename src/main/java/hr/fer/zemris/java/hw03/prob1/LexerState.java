package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeracija koja predstavlja stanje leksičkog analizatora koji je primjerak
 * razreda {@link Lexer}
 *
 * @author Davor Češljaš
 */
public enum LexerState {

	/**
	 * Osnovno stanje rada. Lexer zanemaruje sve praznine te prihvaća tokene
	 * tipa:
	 * <ul>
	 * <li>{@link TokenType#WORD}</li>
	 * <li>{@link TokenType#NUMBER}</li>
	 * <li>{@link TokenType#SYMBOL}</li>
	 * </ul>
	 */
	BASIC,

	/**
	 * Prošireno stanje. Lexer čita ulaz kao jednu riječ (token tipa
	 * {@link TokenType#WORD}) sve do pojave znaka '#'
	 */
	EXTENDED
}
