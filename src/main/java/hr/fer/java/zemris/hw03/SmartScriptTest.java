package hr.fer.java.zemris.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Program koji ilustrira korištenje {@link SmartScriptParser} razreda prilikom
 * parsiranja texta s dodatnim značajkama.
 * 
 * @author Davor Češljaš
 */
public class SmartScriptTest {

	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            <b>args[0]</b> predstavlja naziv datoteke koja se parsira
	 * @throws IOException
	 *             ukoliko nije moguće otvoriti dokument
	 */
	public static void main(String[] args) throws IOException {
		String docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody);

		System.out
				.println("-------------------------------------------------------------------------------------------");

		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		String originalDocumentBody2 = createOriginalDocumentBody(document2);
		System.out.println(originalDocumentBody2);
		System.out.println(
				"Jesu li ispisi jednaki? " + (originalDocumentBody.equals(originalDocumentBody2) ? "Da" : "Ne"));
	}

	/**
	 * Metoda koja se korisit za dobivanje inicijalnog sadržaja datoteke. Zbog
	 * parsiranja moguće je da se sadržaj promijenio iako je semantički ostao
	 * isti. Dakle moguće je da metoda {@link String#equals(Object)} usporedbom
	 * inicijalnog teksta i povratne vrijednosti ove metode vrati
	 * <code><b>false</b></code>
	 *
	 * @param document
	 *            primjerak razreda {@link DocumentNode} iz kojeg dobivamo
	 *            inicijalni sadržaj
	 * @return inicijalni sadržaj uz moguće izmjene
	 */
	private static String createOriginalDocumentBody(DocumentNode document) {
		// dokument se zna ispisati
		return document.toString();
	}
}
