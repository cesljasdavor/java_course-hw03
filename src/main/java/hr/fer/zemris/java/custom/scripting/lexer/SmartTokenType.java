package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeracija koja predstavlja tip tokena koji je primjerak razreda
 * {@link SmartToken}. Moguće vrijednosti:
 * <ul>
 * <li>{@link SmartTokenType#CONSTANT_DOUBLE}</li>
 * <li>{@link SmartTokenType#CONSTANT_INTEGER}</li>
 * <li>{@link SmartTokenType#EOF}</li>
 * <li>{@link SmartTokenType#FUNCTION}</li>
 * <li>{@link SmartTokenType#OPERATOR}</li>
 * <li>{@link SmartTokenType#STRING}</li>
 * <li>{@link SmartTokenType#TAG_CLOSE}</li>
 * <li>{@link SmartTokenType#TAG_NAME}</li>
 * <li>{@link SmartTokenType#TAG_OPEN}</li>
 * <li>{@link SmartTokenType#TEXT}</li>
 * <li>{@link SmartTokenType#VARIABLE}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public enum SmartTokenType {

	/**
	 * Predstavlja običan tekst. Unutar teksta moguće escape sekvence su '\\'
	 * koji predstavlja '\' i '\{' koji predstavlja '{' te se koristi kako se
	 * ulaz ne bi tumačio kao početak taga
	 */
	TEXT,

	/**
	 * Predstavlja varijablu unutar taga. Varijabla je niz znakova koja
	 * započinje slovom i nakon toga može sadržavati proizvoljan broj slova,
	 * brojki ili znakova '_'
	 */
	VARIABLE,

	/**
	 * Predstavlja cjelobrojnu konstantu. Cjelobrojna konstanta je sve što se
	 * može tumačiti kao {@link Integer}
	 */
	CONSTANT_INTEGER,

	/**
	 * Predstavlja realnu konstantu. Realna konstanta je sve što se može
	 * tumačiti kao {@link Double}
	 */
	CONSTANT_DOUBLE,

	/**
	 * Predstavlja tekst unutar taga. Tekst mora započeti i završiti sa ".
	 * Unutar teksta moguće escape sekvence su '\\' koji predstavlja '\' i '\"'
	 * koji predstavlja '"' kako se znak " ne bi tumačio kao kraj teksta
	 */
	STRING,

	/**
	 * redstavlja naziv funkcije. Funkcija započinje znakom '@' nakon kojeg
	 * slijedi slovo. Nakon toga naziv funkcije može sadržavati proizvoljni broj
	 * slova, brojki ili znakova '_'
	 */
	FUNCTION,

	/**
	 * Jedan od zakova '+', '-', '*', '/' ili '^'. Da bi se '-' tumačio kao
	 * operator on ne smije bit neposredno ispred brojke (npr. -2 je broj, ali -
	 * 2 je operator pa broj)
	 */
	OPERATOR,

	/** Predstavlja otvoreni tag. To je niz znakova "{$" */
	TAG_OPEN,

	/** The tag name. */
	TAG_NAME,

	/** Predstavlja zatvoreni tag. To je niz znakova "$}" */
	TAG_CLOSE,

	/** Predstavlja oznaku kraja datoteke */
	EOF
}
