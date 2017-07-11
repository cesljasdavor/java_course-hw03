package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Program koji predstavlja demonstraciju rada leksičkog analizatora
 * {@link SmartScriptLexer}.
 * 
 * @author Davor Češljaš
 */
public class Demo {

	/**
	 * Metoda od koje započinje izvođenje programa
	 *
	 * @param args
	 *            u ovom programu se ne koristi
	 */
	public static void main(String[] args) {
		String input = "This is sample text.\n" + "{$ FOR i 1 10 1 $}\n"
				+ "\tThis is {$= i $}-th time this message is generated.\n" + "{$END$}\n" + "{$FOR i 0 10 2 $}\n"
				+ "\tsin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n" + "{$END$}";

		SmartScriptLexer lexer = new SmartScriptLexer(input);
		SmartToken token = null;
		while ((token = lexer.nextToken()).getType() != SmartTokenType.EOF) {
			System.out.println(token);
		}
	}
}
