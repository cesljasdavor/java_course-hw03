package hr.fer.zemris.java.hw03.prob1;

/**
 * Razred koji predstavlja leksički analizator. Ovaj analizator ulazni niz
 * znakova analizira na sljedeća dva načina:
 * <ol>
 * <li>ukoliko je analizator u stanju {@link LexerState#BASIC} u ulaznom nizu
 * zanemaruju se sve praznine.
 * <ul>
 * <li>Ukoliko je sljedeći znak u nizu slovo ili znak '\' analizator će pokušati
 * izvući token tipa {@link TokenType#WORD}. Ukoliko je pročitan znak '\'
 * znamenka koja predstoji shvaća se kao dio riječi</li>
 * <li>Ukoliko je sljedeći znak brojka analizator će pokušati izvući token tipa
 * {@link TokenType#NUMBER}</li>
 * <li>U suprotnom će analizator ulazni znak tumačiti kao token
 * tipa{@link TokenType#SYMBOL}</li>
 * </ul>
 * </li>
 * <li>ukoliko je analizator u stanju {@link LexerState#EXTENDED} svi znakovi do
 * znaka '#' tumaće se kao {@link TokenType#WORD}. Ovdje se praznine ne
 * zanemarujuli>
 * </ol>
 * 
 * @author Davor Češljaš
 */
public class Lexer {

	/** Konstanta koja predstavlja zna '\' */
	private static final char ESCAPE = '\\';

	/** Konstanta koja predstavlja znak '#' */
	private static final char HASH = '#';

	/** Ulazni niz znakova koji se leksički analizira */
	private char[] data;

	/** Zadnje izvađeni token */
	private Token token;

	/** Trenutni pozicija u ulaznom nizu znakova */
	private int currentIndex;

	/**
	 * Stanje u kojem se nalazi leksiči analizator. Moguća stanju su
	 * {@link LexerState#BASIC} i {@link LexerState#EXTENDED}
	 */
	private LexerState state;

	/**
	 * Konstruktor koji iz ulaznog teksta inicijalizira ulazni niz znakova te
	 * postavlja trenutno stanje na {@link LexerState#BASIC}
	 *
	 * @param text
	 *            ulazni tekst koji je potrebno leksički analizirati
	 * @throws IllegalArgumentException
	 *             ukoliko je kao <b>text</b> predan <b>null</b>
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Tekst ne smije biti null!");
		}
		this.data = text.toCharArray();
		this.state = LexerState.BASIC;
	}

	/**
	 * Dohvaća zadnje izvađeni token. Može vratiti <b>null</b> ukoliko analiza
	 * nije započela, odnosno nikada nije pozvana metoda {@link #nextToken()}
	 *
	 * @return zadnje izvađeni token ili <b>null</b>
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Analizator iz predanog ulaznog teksta pokušava izvaditi sljedeći token.
	 * Način vađenja sljedećeg tokena ovisi o stanju leksičkog analizatora.
	 * Metoda ujedino ažurira trenutni token. Izvađeni token sada je ponovo
	 * moguće dohvatiti pozivom metode {@link #getToken()}
	 *
	 * @return sljedeći token iz ulaznog niza
	 * 
	 * @throws LexerException
	 *             ukoliko sljedeći token nije moguće izvaditi, jer znakovi u
	 *             ulaznom nizu ne odgovaraju niti jednom tipu tokena
	 */
	public Token nextToken() {
		extractToken();
		return token;
	}

	/**
	 * Metoda postavlja stanje leksičkog analizatora. Moguća stanju su
	 * {@link LexerState#BASIC} i {@link LexerState#EXTENDED}.
	 *
	 * @param state
	 *            {@link LexerState#BASIC} ili {@link LexerState#EXTENDED}
	 * 
	 * @throws IllegalArgumentException
	 *             ukoliko se preda <b>null</b>
	 */
	public void setState(LexerState state) {
		if (state == null) {
			throw new IllegalArgumentException("Stanje leksičkog analizatora ne smije biti null!");
		}
		this.state = state;
	}

	/**
	 * Pomoćna metoda koja ovisno o stanju vrši vađenje sljedećeg tokena . Ako
	 * uspije izvađeni token će biti postavljen kao trenutni token
	 * 
	 * @throws LexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void extractToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Nemam više tokena!");
		}

		// prije nego krenomo uzimati znakove potrebno je očistiti sve
		// razmake("\t","\n", "\r", " ")
		skipWhitespaces();

		// jesmo li na kraju
		if (isEOF()) {
			token = new Token(TokenType.EOF, null);
			return;
		}

		if (state == LexerState.BASIC)
			basicInterpretation();
		else
			extendedInterpretation();
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena ukoliko je leksički
	 * analizator u stanju {@link LexerState#EXTENDED}
	 * 
	 * @throws LexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void extendedInterpretation() {
		if (data[currentIndex] == HASH) {
			token = new Token(TokenType.SYMBOL, data[currentIndex++]);
		} else {
			extractWordExtended();
		}
	}

	/**
	 * Pomoćna metoda koja pokušava izvaditi sljedeći token tipa
	 * {@link TokenType#WORD}. Prilikom vađenja tokena ne izostavljaju se
	 * praznine
	 * 
	 * @throws LexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void extractWordExtended() {
		StringBuilder sb = new StringBuilder();

		char c;
		while (!isEOF() && !isWhitespace((c = data[currentIndex])) && c != HASH) {
			sb.append(c);
			currentIndex++;
		}

		token = new Token(TokenType.WORD, sb.toString());
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena ukoliko je leksički
	 * analizator u stanju {@link LexerState#BASIC}
	 * 
	 * @throws LexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void basicInterpretation() {
		char c = data[currentIndex];
		// svaki od tipova tokena počinje na specifičan način , ispitujemo ih
		// sve ovdje
		if (Character.isLetter(c) || c == ESCAPE) {
			extractWord();
		} else if (Character.isDigit(c)) {
			extractNumber();
		} else {
			// sve ostalo su simboli
			token = new Token(TokenType.SYMBOL, c); // autoboxing
			currentIndex++;
		}
	}

	/**
	 * Pomoćna metoda koja pokušava izvaditi sljedeći token tipa
	 * {@link TokenType#WORD}. Prilikom vađenja tokena izostavljaju se praznine
	 * 
	 * @throws LexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void extractWord() {
		StringBuilder sb = new StringBuilder();

		while (!isEOF()) {
			char c = data[currentIndex];
			if (Character.isLetter(c)) {
				sb.append(c);
				currentIndex++;
			} else if (c == ESCAPE) {
				currentIndex++;
				extractEscapeSequence(sb);
			} else {
				break;
			}
		}

		token = new Token(TokenType.WORD, sb.toString());
	}

	/**
	 * Pomoćna metoda koja se poziva ukoliko se prilikom čitanje ulaznog niza
	 * naišlo na znak {@link Lexer#ESCAPE}. Ako je sljedeći znak znamenka ili
	 * znak '\' do sada izvađenom nizu znakova spremljenom u <b>sb</b> nadodaje
	 * se pročitana znamenka ili '\'
	 *
	 * @param sb
	 *            primjerak razreda {@link StringBuilder} kojem se nadodaje
	 *            znamenka ili znak '\
	 * 
	 * @throws ukoliko
	 *             sljedeći znak nije znamenka ili znak '\''
	 */
	private void extractEscapeSequence(StringBuilder sb) {
		if (isEOF()) {
			throw new LexerException("Escape sekvenca je prazna");
		}

		char c = data[currentIndex++];
		if (!Character.isDigit(c) && c != ESCAPE) {
			throw new LexerException(String.format("Nedopuštena escape sekvenca '+\\" + c + "'."));
		}

		sb.append(c);
	}

	/**
	 * Pomoćna metoda koja pokušava izvaditi sljedeći token tipa
	 * {@link TokenType#NUMBER}. Prilikom vađenja tokena izostavljaju se
	 * praznine
	 * 
	 * @throws LexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void extractNumber() {
		StringBuilder sb = new StringBuilder();

		char c;
		while (!isEOF() && Character.isDigit((c = data[currentIndex]))) {
			sb.append(c);
			currentIndex++;
		}

		token = new Token(TokenType.NUMBER, extractLongValue(sb.toString()));
	}

	/**
	 * Pomoćna metoda koja parsira predani ulaz <b>input</b> u cijeli broj tipa
	 * {@link Long}
	 *
	 * @param input
	 *            ulazni niz koji se pokušava parsirati u {@link Long}
	 *            vrijednost
	 * @return {@link Long} vrijednost predanog ulaza <b>input</b>
	 * 
	 * @throws LexerException
	 *             ukoliko parsiranje nije uspjelo
	 */
	private Long extractLongValue(String input) {
		Long value = null;
		try {
			value = Long.parseLong(input);
		} catch (NumberFormatException nfe) {
			throw new LexerException(String.format("Broj '%s' je prevelik! Valjani brojevi su od %d do %d", input,
					Long.MIN_VALUE, Long.MAX_VALUE));
		}

		return value;
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
		return c == '\t' || c == '\n' || c == '\r' || c == ' ';
	}

	/**
	 * Pomoćna metoda koja ispituje jesmo li došli do kraja ulaznog niza
	 *
	 * @return <b>true</b> ukoliko smo došli do kraj niza <b>false</b> inače
	 */
	private boolean isEOF() {
		return currentIndex >= data.length;
	}
}
