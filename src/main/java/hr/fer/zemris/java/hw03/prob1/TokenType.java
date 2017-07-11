package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeracija koja predstavlja moguće tipove tokena prilikom leksičke analize
 * primjerkom razreda {@link Lexer}
 * 
 * @author Davor  Češljaš
 */
public enum TokenType {

	/** Kraj predanog dokumenta */
	EOF,

	/** Tip tokena koji se tumači kao riječ. */
	WORD,

	/** Tip tokena koji se tumači kao brojčana vrijednost */
	NUMBER,

	/** Tip tokena koji se tumači kao simbol */
	SYMBOL
}
