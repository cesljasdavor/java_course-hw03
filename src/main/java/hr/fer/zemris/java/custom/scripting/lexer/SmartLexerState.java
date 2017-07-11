package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeracija predstavlja stanje primjerka razreda {@link SmartScriptLexer}.
 * 
 * @author Davor Češljaš
 */
public enum SmartLexerState {

	/**
	 * Stanje u kojem se ulazni niz znakova čita kao običan tekst.
	 */
	TEXT,

	/** Stanje u kojem se ulazni niz tumači kao tag i njegovi elementi */
	TAG
}
