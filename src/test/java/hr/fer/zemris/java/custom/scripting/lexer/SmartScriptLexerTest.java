package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmartScriptLexerTest {

	// Testovi za stanje legera SmartLexerState.TAG
	@Test
	public void testiranjeZaPrazanNiz() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		assertNotNull("Token was expected but null was returned.", lexer.nextToken());
	}

	@Test(expected = SmartLexerException.class)
	public void testiranjeZaNizNull() {
		// must throw!
		new SmartScriptLexer(null);
	}

	@Test
	public void testiranjeTipaTokenaPrazanNiz() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		assertEquals("Empty input must generate only EOF token.", SmartTokenType.EOF, lexer.nextToken().getType());
	}

	@Test(expected = SmartLexerException.class)
	public void testiranjeRadaNakonEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		lexer.nextToken();
	}

	@Test
	public void testiranjeSamoPraznine() {
		SmartScriptLexer lexer = new SmartScriptLexer(" \t\n\r  \n");

		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeJednaRijec() {
		SmartScriptLexer lexer = new SmartScriptLexer("Java");

		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeKombinacijaRijeciIPraznina() {
		SmartScriptLexer lexer = new SmartScriptLexer("\nJava Oracle\n");

		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
	}

	@Test(expected = SmartLexerException.class)
	public void testiranjeTextNeispravan() {
		SmartScriptLexer lexer = new SmartScriptLexer("Java \\8");
		lexer.nextToken();
	}

	@Test
	public void testiranjeEscapea() {
		SmartScriptLexer lexer = new SmartScriptLexer("\nJava \\ Oracle\\{\n");

		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
	}

	// Testovi za stanje legera SmartLexerState.TAG

	@Test
	public void testiranjeOtvorenogTagaIspravno() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$");

		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
	}

	@Test(expected = SmartLexerException.class)
	public void testiranjeOtvorenogTagaNeispravno() {
		SmartScriptLexer lexer = new SmartScriptLexer("{X");

		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeZatvorenogTagaIspravno() {
		// predpostavka je da je test za otvoreni tag prosao
		SmartScriptLexer lexer = new SmartScriptLexer("{$$}");

		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test(expected = SmartLexerException.class)
	public void testiranjeZatvorenogTagaNeispravno() {
		// predpostavka je da je test za otvoreni tag prosao
		SmartScriptLexer lexer = new SmartScriptLexer("{$X}");

		// otvoreni tag
		lexer.nextToken();

		// varijabla (tako se tumači X)
		lexer.nextToken();

		// error
		lexer.nextToken();
	}

	@Test
	public void testiranjeVarijable() {
		// predpostavka je da je test za otvoreni tag prosao
		SmartScriptLexer lexer = new SmartScriptLexer("{$ X x_c X25_cd C_ $}");

		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeForTaga() {
		// predpostavka je da je test za otvoreni tag prosao
		SmartScriptLexer lexer = new SmartScriptLexer("{$FOR For for fOr foR FoR$}");

		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		for (int i = 0; i < 6; i++) {
			SmartToken token = lexer.nextToken();
			assertEquals(SmartTokenType.TAG_NAME, token.getType());
			assertEquals("FOR", token.getValue().toString().toUpperCase());
		}
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeEndTaga() {
		// predpostavka je da je test za otvoreni tag prosao
		SmartScriptLexer lexer = new SmartScriptLexer("{$END End end eNd enD EnD$}");

		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		for (int i = 0; i < 6; i++) {
			SmartToken token = lexer.nextToken();
			assertEquals(SmartTokenType.TAG_NAME, token.getType());
			assertEquals("END", token.getValue().toString().toUpperCase());
		}
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeEchoTaga() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= $}");
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		SmartToken token = lexer.nextToken();
		assertEquals(SmartTokenType.TAG_NAME, token.getType());
		assertEquals('=', token.getValue());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeBrojeva() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ 1 -10 25.86 -3.25 2. $}");
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_INTEGER, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_INTEGER, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_DOUBLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_DOUBLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_DOUBLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test(expected = SmartLexerException.class)
	public void neispravanFormatBrojaPreviseTocaka() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ 1.0.0. $}");
		// otvaranje taga
		lexer.nextToken();
		// broj neispravan
		lexer.nextToken();

	}
	
	@Test(expected = SmartLexerException.class)
	public void neispravanFormatBrojaPreviseMinusa() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ -1-0.0. $}");
		// otvaranje taga
		lexer.nextToken();
		// broj neispravan
		lexer.nextToken();

	}

	@Test
	public void testiranjeOperatora() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ + - * / ^ $}");
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		for (int i = 0; i < 5; i++) {
			assertEquals(SmartTokenType.OPERATOR, lexer.nextToken().getType());
		}
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeMinusIliNegativanBroj() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$- 10 -10$}");
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.OPERATOR, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_INTEGER, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_INTEGER, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeStringa() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ \"java\"$}");
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.STRING, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test
	public void testiranjeStringaEscapeIspravan() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ \"java\\ \\\"\"$}");
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.STRING, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}

	@Test(expected = SmartLexerException.class)
	public void testiranjeStringaEscapeNeispravan() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ \"java\\a\"$}");
		lexer.nextToken().getType();
		lexer.nextToken().getType();
	}

	@Test(expected = SmartLexerException.class)
	public void testiranjeStringaKojiNijeZatvoren() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ \"java\\ $}");
		lexer.nextToken().getType();
		lexer.nextToken().getType();
	}
	
	@Test
	public void testiranjeImenaFunkcijeIspravno() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$@ba3_$}");
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.FUNCTION, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}
	
	@Test(expected = SmartLexerException.class)
	public void testiranjeImenaFunkcijeNeispravno() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ @2a3_ $}");
		lexer.nextToken().getType();
		lexer.nextToken().getType();
	}
	
	//slozeniji primjeri
	//oba primjera spojena su u Demo.java u ovom paketu
	//primjere sam namjerno razdvojio da bi koliko toliko povečao čitljivost
	
	@Test
	public void testiranjeSlozenogPrimjera1() throws Exception {
		String input = 				
			"This is sample text.\n"+
			"{$ FOR i \"1\"-10 1 $}\n"+
				"\tThis is {$= i $}-th time this message is generated.\n"+
			"{$END$}\n";
		SmartScriptLexer lexer = new SmartScriptLexer(input);
		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_NAME, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.STRING, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_INTEGER, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_INTEGER, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_NAME, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_NAME, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
	}
	

	@Test
	public void testiranjeSlozenogPrimjera2() throws Exception {
		String input = 				
				"{$FOR i 0 10 2 $}\n" +
					"\tsin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n"+
				"{$END$}";
		SmartScriptLexer lexer = new SmartScriptLexer(input);
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_NAME, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_INTEGER, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_INTEGER, lexer.nextToken().getType());
		assertEquals(SmartTokenType.CONSTANT_INTEGER, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_NAME, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_NAME, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.OPERATOR, lexer.nextToken().getType());
		assertEquals(SmartTokenType.FUNCTION, lexer.nextToken().getType());
		assertEquals(SmartTokenType.STRING, lexer.nextToken().getType());
		assertEquals(SmartTokenType.FUNCTION, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_OPEN, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_NAME, lexer.nextToken().getType());
		assertEquals(SmartTokenType.TAG_CLOSE, lexer.nextToken().getType());
	}
}
