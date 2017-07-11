package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Razred koji predstavlja leksički analizator koji se koristi prilikom
 * parsiranja primjerkom razreda{@link SmartScriptParser} . Ovaj analizator
 * ulazni niz znakova analizira na sljedeća dva načina:
 * <ol>
 * <li>ukoliko je analizator u stanju {@link SmartLexerState#TEXT} ulazni niz
 * nastoji se pretvoriti u jedan token tipa {@link SmartTokenType#TEXT}. Ukoliko
 * u ovom stanju naiđemo na znak '{' koji predhodno nije escapean leksički
 * analizator prelazi u stanje {@link SmartLexerState#TEXT}</li>
 * <li>ukoliko je analizator u stanju {@link SmartLexerState#TAG} ulazni niz
 * nastoji se pretvoriti u jedan od tagova FOR END ili = (ECHO). Za više detalja
 * vidjeti {@link SmartTokenType}(sve osim {@link SmartTokenType#TEXT}).
 * Prilikom nailaska na znak sekvencu "$}" leksički analizator prelazi u stanje
 * {@link SmartLexerState#TEXT}</li>
 * </ol>
 * 
 * @see SmartTokenType
 * 
 * @author Davor Češljaš
 */
public class SmartScriptLexer {

	/** Konstanta koja predstavlja znak '{' */
	private static final char OPENED_CURLY_BRACKETS = '{';

	/** Konstanta koja predstavlja znak '}' */
	private static final char CLOSED_CURLY_BRACKETS = '}';

	/** Konstanta koja predstavlja znak '\\' */
	private static final char ESCAPE = '\\';

	/** Konstanta koja predstavlja znak '$' */
	private static final char DOLLAR_SIGN = '$';

	/** Konstanta koja predstavlja znak '=' */
	private static final char ECHO = '=';

	/** Konstanta koja predstavlja znak " */
	private static final char QUOUTE = '"';

	/** Konstanta koja predstavlja znak '@' */
	private static final char AT_SIGN = '@';

	/** Konstanta koja predstavlja znak '-' */
	private static final char MINUS = '-';

	/** Konstanta koja predstavlja znak '.' */
	private static final char DOT = '.';

	/** Konstanta koja predstavlja znak '_' */
	private static final char UNDERSCORE = '_';

	/** Predstavlja token koji predstavlja otvoreni tag */
	private static final SmartToken OPEN_TAG = new SmartToken(SmartTokenType.TAG_OPEN, "{$");

	/** Predstavlja token koji predstavlja zatvoreni tag */
	private static final SmartToken CLOSE_TAG = new SmartToken(SmartTokenType.TAG_CLOSE, "$}");

	/** Konstanta koja predstavlja nize znakova "FOR" */
	public static final String FOR = "FOR";

	/** Konstanta koja predstavlja nize znakova "END" */
	public static final String END = "END";

	/**
	 * Stanje u kojem se nalazi leksiči analizator. Moguća stanju su
	 * {@link SmartLexerState#TEXT} i {@link SmartLexerState#TAG}
	 */
	private SmartLexerState state;

	/** Trenutni pozicija u ulaznom nizu znakova */
	private int currentIndex;

	/** Ulazni niz znakova koji se leksički analizira */
	private char[] data;

	/** Zadnje izvađeni token */
	private SmartToken currentToken;

	/**
	 * Konstruktor koji iz ulaznog teksta inicijalizira ulazni niz znakova te
	 * postavlja trenutno stanje na {@link SmartLexerState#TEXT}
	 *
	 * @param text
	 *            ulazni tekst koji je potrebno leksički analizirati
	 * @throws SmartLexerException
	 *             ukoliko je kao <b>input</b> predan <b>null</b>
	 * 
	 */
	public SmartScriptLexer(String input) {
		if (input == null) {
			throw new SmartLexerException("Ulazni niz u leksički analizator ne smije biti null");
		}

		data = input.toCharArray();
		// inicijalno stanje
		state = SmartLexerState.TEXT;
	}

	/**
	 * Metoda koja dohvaća trenutno stanje leksičkog analizatora
	 *
	 * @return trenutno stanje leksičkog analizatora
	 */
	public SmartLexerState getState() {
		return state;
	}

	/**
	 * Metoda koja dohvaća zadnje izvađeni token. Može vratiti <b>null</b>
	 * ukoliko analiza nije započela, odnosno nikada nije pozvana metoda
	 * {@link #nextToken()}
	 *
	 * @return zadnje izvađeni token ili <b>null</b>
	 */
	public SmartToken getCurrentToken() {
		return currentToken;
	}

	/**
	 * Analizator iz predanog ulaznog teksta pokušava izvaditi sljedeći token.
	 * Način vađenja sljedećeg tokena ovisi o stanju leksičkog analizatora.
	 * Metoda ujedino ažurira trenutni token. Izvađeni token sada je ponovo
	 * moguće dohvatiti pozivom metode {@link #getToken()}
	 *
	 * @return sljedeći token iz ulaznog niza
	 * 
	 * @throws SmartLexerException
	 *             ukoliko sljedeći token nije moguće izvaditi, jer znakovi u
	 *             ulaznom nizu ne odgovaraju niti jednom tipu tokena
	 */
	public SmartToken nextToken() {
		extractToken();
		return currentToken;
	}

	/**
	 * Pomoćna metoda koja ovisno o stanju vrši vađenje sljedećeg tokena . Ako
	 * uspije izvađeni token će biti postavljen kao trenutni token
	 * 
	 * @throws SmartLexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void extractToken() {
		if (currentToken != null && currentToken.getType() == SmartTokenType.EOF) {
			throw new SmartLexerException("Nemam više tokena!");
		}

		if (isEOF()) {
			extractEOFToken();
			return;
		}

		// promjena stanja
		if (data[currentIndex] == OPENED_CURLY_BRACKETS) {
			state = SmartLexerState.TAG;
		}

		if (state == SmartLexerState.TEXT) {
			nextTextToken();
		} else {
			nextTagToken();
		}
	}

	/**
	 * Pomoćna metoda koja postavlja trenutni token na token tipa
	 * {@link SmartTokenType#EOF}
	 */
	private void extractEOFToken() {
		currentToken = new SmartToken(SmartTokenType.EOF, null);
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena ukoliko je leksički
	 * analizator u stanju {@link SmartLexerState#TEXT}
	 * 
	 * @throws SmartLexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void nextTextToken() {
		StringBuilder sb = new StringBuilder();

		char c;
		while (!isEOF() && (c = data[currentIndex]) != OPENED_CURLY_BRACKETS) {
			if (c == ESCAPE) {
				// može puknuti SmartLexerException
				extractEscapeSequence(sb, OPENED_CURLY_BRACKETS);
				continue;
			}
			// normalan režim rada
			sb.append(c);
			currentIndex++;
		}

		currentToken = new SmartToken(SmartTokenType.TEXT, sb.toString());
	}

	// Metode do extractDigitsLettersAndUnderscores() koriste se isključivo u
	// svrhe vađenja tokena unutar taga

	/**
	 * Pomoćna metoda koja vrši pripremu ulaznog niza ukoliko je leksički
	 * analizator u stanju {@link SmartLexerState#TAG}
	 * 
	 * @throws SmartLexerException
	 *             ukoliko {@link #extractTagToken()} baci
	 *             {@link SmartLexerException}
	 */
	private void nextTagToken() {
		// u textu ima razmaka dok ovdje nema
		skipWhitespaces();

		// sada je moguće da su ostale upravo praznine na kraju pa vraćamo EOF
		if (isEOF()) {
			extractEOFToken();
			return;
		}

		extractTagToken();
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena ukoliko je leksički
	 * analizator u stanju {@link SmartLexerState#TAG}. Koji se token stvara
	 * ovisi o ulaznom nizu
	 * 
	 * @throws SmartLexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void extractTagToken() {
		char c = data[currentIndex];
		if (c == OPENED_CURLY_BRACKETS) {
			currentIndex++;
			extractOpenTag();
		} else if (c == DOLLAR_SIGN) {
			currentIndex++;
			extractCloseTag();
		} else if (Character.isLetter(c)) {
			// počinje ili vađenje fora ili vađenje varijable
			extractVariableOrTagName();
		} else if (c == ECHO) {
			currentToken = new SmartToken(SmartTokenType.TAG_NAME, ECHO);
			currentIndex++;
		} else if (Character.isDigit(c) || (c == MINUS && Character.isDigit(data[currentIndex + 1]))) {
			extractNumber();
		} else if (isOperator(c)) {
			currentToken = new SmartToken(SmartTokenType.OPERATOR, c);
			currentIndex++;
		} else if (c == QUOUTE) {
			// izbaci navodnike
			currentIndex++;
			extractString();
		} else if (c == AT_SIGN) {
			extractFunction();
		} else {
			throw new SmartLexerException("Znak '" + c + "' ne mogu smjestiti u token");
		}
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena tipa
	 * {@link SmartTokenType#TAG_OPEN}
	 * 
	 * @throws SmartLexerException
	 *             ukoliko se krivo otvori tag(oznaka)
	 */
	private void extractOpenTag() {
		if (isEOF() || data[currentIndex] != DOLLAR_SIGN) {
			throw new SmartLexerException(
					"Krivo otvaranje oznake (taga). Vi ste unijeli '{" + data[currentIndex] + "'");
		}

		currentIndex++;
		// vidi jel ga bolje izbaciti u konstantu
		currentToken = OPEN_TAG;
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena tipa
	 * {@link SmartTokenType#TAG_CLOSE}
	 * 
	 * @throws SmartLexerException
	 *             ukoliko se krivo zatvori tag(oznaka)
	 */
	private void extractCloseTag() {
		if (isEOF() || data[currentIndex] != CLOSED_CURLY_BRACKETS) {
			throw new SmartLexerException("Znak '$' smije doći isključivo ispred '}'!");
		}

		currentIndex++;
		state = SmartLexerState.TEXT;
		currentToken = CLOSE_TAG;
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena tipa
	 * {@link SmartTokenType#VARIABLE} ili {@link SmartTokenType#TAG_NAME}.
	 * Nizovi "FOR" i "END" uvijek se tumaće kao naziv taga
	 * 
	 * @throws SmartLexerException
	 *             ukoliko varijabla ne započinje sa slovom
	 */
	private void extractVariableOrTagName() {
		String result = null;
		try {
			result = extractDigitsLettersAndUnderscores();
		} catch (SmartLexerException e) {
			throw new SmartLexerException("Varijabla mora sadržavati slovo na početku");
		}
		currentToken = new SmartToken(result.equalsIgnoreCase(FOR) || result.equalsIgnoreCase(END)
				? SmartTokenType.TAG_NAME : SmartTokenType.VARIABLE, result);
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena tipa
	 * {@link SmartTokenType#CONSTANT_DOUBLE} ili
	 * {@link SmartTokenType#CONSTANT_INTEGER}
	 * 
	 * @throws SmartLexerException
	 *             ukoliko se ulazni niz ne može parsirati u {@link Integer} ili
	 *             {@link Double}
	 */
	private void extractNumber() {
		StringBuilder sb = new StringBuilder();

		char c;
		boolean isDouble = false;
		while (!isEOF()) {
			if (!Character.isDigit((c = data[currentIndex])) && c != DOT && c != MINUS) {
				break;
			}
			if (c == DOT && !isDouble) {
				isDouble = true;
			}
			sb.append(c);
			currentIndex++;
		}

		String potencialNumber = sb.toString();
		try {
			currentToken = isDouble
					? new SmartToken(SmartTokenType.CONSTANT_DOUBLE, Double.parseDouble(potencialNumber))
					: new SmartToken(SmartTokenType.CONSTANT_INTEGER, Integer.parseInt(potencialNumber));
		} catch (NumberFormatException nfe) {
			throw new SmartLexerException("Format broja '" + potencialNumber + "' nije ispravan");
		}
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena tipa
	 * {@link SmartTokenType#STRING}
	 * 
	 * @throws SmartLexerException
	 *             ukoliko se string nikada ne zatvori ili se naiđe na krivu
	 *             escape sekvencu
	 */
	private void extractString() {
		StringBuilder sb = new StringBuilder();

		char c;
		while (true) {
			if (isEOF()) {
				throw new SmartLexerException("String '" + sb.toString() + "' nikada nije zatvoren");
			}
			if ((c = data[currentIndex]) == ESCAPE) {
				extractEscapeSequence(sb, QUOUTE);
				continue;
			}
			currentIndex++;
			// navodnici se ne upisuju u string
			if (c == QUOUTE) {
				break;
			}
			sb.append(c);
		}

		currentToken = new SmartToken(SmartTokenType.STRING, sb.toString());
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena tipa
	 * {@link SmartTokenType#FUNCTION}
	 * 
	 * @throws SmartLexerException
	 *             ukoliko niz ne započinje sa @ i nakon toga nekim slovom
	 */
	private void extractFunction() {
		StringBuilder sb = new StringBuilder();
		// prije je provjereno da je trenutni znak @ ovdje ga dodajemo
		sb.append(data[currentIndex++]);

		String result = null;
		try {
			result = extractDigitsLettersAndUnderscores();
		} catch (SmartLexerException e) {
			throw new SmartLexerException("Neisprvano ime funkcije.Funkcija mora započinjati sa @(neko slovo)");
		}

		currentToken = new SmartToken(SmartTokenType.FUNCTION, sb.append(result).toString());
	}

	/**
	 * Provjerava je li znak operator
	 * 
	 * @see SmartTokenType#OPERATOR
	 *
	 * @param c
	 *            znak koji provjeravamo
	 * @return <b>true</b> ukoliko je <b>c</b> operator, <b>false</b> inače
	 */
	private boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
	}

	// Daljnje metode koriste se u raznim dijelovima lexera stoga su izdvojene
	// na ovom mjestu

	/**
	 * Pomoćna metoda koja iz niza vadi brojeve, slova i znakove '_' i spaja ih
	 * i vraća kao {@link String}
	 *
	 * @return niz brojeva, slova i znakova '_'
	 * 
	 * @throws SmartLexerException
	 *             ukoliko niz ne započinje slovom
	 */
	private String extractDigitsLettersAndUnderscores() {
		// prilikom poziva ove metode provjerili smo je li prva znamenka slovo
		StringBuilder sb = new StringBuilder();

		char c;
		if (isEOF() || !Character.isLetter((c = data[currentIndex]))) {
			// u ovo trenutnku ne znamo tko je pozivatelj metode stoga ne možemo
			// znati točan opis pogreške, ali ćemo je i dalje izazvati
			throw new SmartLexerException();
		}

		while (!isEOF() && (Character.isLetterOrDigit(c = data[currentIndex]) || c == UNDERSCORE)) {
			sb.append(c);
			currentIndex++;
		}

		return sb.toString();
	}

	/**
	 * Pomoćna metoda koja vadi escape sekvencu. Escape sekvenca je ili znak '\'
	 * ili znak '\' nakon kojeg dolazi <b>specialChar</b>
	 *
	 * @param sb
	 *            primjerak razreda {@link StringBuilder} kojem se nadodaje znak
	 * @param specialChar
	 *            specijalni znak koji se escapea
	 *            
	 * @throws SmartLexerException ako znak nakon '\' nije jednak <b>specialChar</b>
	 */
	private void extractEscapeSequence(StringBuilder sb, char specialChar) {
		// ovdje znamo da je u c '\\'
		char c = data[currentIndex];
		currentIndex++;
		// sada je data[currentIndex] sljedeći char ali ne smijemo mu pristupati
		// jer ne znamo je li dokument došao do kraja
		if (isEOF() || isWhitespace(data[currentIndex])) {
			sb.append(c);
		} else if (data[currentIndex] == specialChar) {
			sb.append(data[currentIndex++]);
		} else {
			throw new SmartLexerException("Netočna escape sekvenca '\\" + data[currentIndex] + "'.");
		}
	}

	/**
	 * Pomoćna metoda koja ispituje jesmo li došli do kraja ulaznog niza
	 *
	 * @return <b>true</b> ukoliko smo došli do kraj niza <b>false</b> inače
	 */
	private boolean isEOF() {
		return currentIndex >= data.length;
	}

	/**
	 * Pomoćna metoda koja se koristi za preskakanje praznina u ulaznom nizu
	 */
	private void skipWhitespaces() {
		while (!isEOF() && isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
	}

	/**
	 * Pomoćna metoda koja ispituje je li predani znak praznina. Kao praznine se
	 * podrazumjevaju znakovi :
	 * <ul>
	 * <li>'\t'</li>
	 * <li>'\r'</li>
	 * <li>'\n'</li>
	 * <li>' '</li>
	 * </ul>
	 * 
	 *
	 * @param c
	 *            znak koji se provjerava
	 * @return <b>true </b> ukoliko je <b>c</b> praznina, inače vraća
	 *         <b>false</b>
	 */
	private boolean isWhitespace(char c) {
		return c == '\n' || c == '\t' || c == '\r' || c == ' ';
	}
}
