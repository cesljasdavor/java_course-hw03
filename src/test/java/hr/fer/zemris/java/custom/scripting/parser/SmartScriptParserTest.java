package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SmartScriptParserTest {

	// Testovi koji ne izazivaju exceptione (od lakših prema težima)
	@Test
	public void testiranjePrazanUnos() {
		String docBody = "";
		SmartScriptParser parser = new SmartScriptParser(docBody);
		assertEquals(docBody, parser.getDocumentNode().toString());
	}
	
	
	@Test
	public void testiranjeSamoTekst() {
		String docBody = loader("document2.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		assertEquals(docBody, parser.getDocumentNode().toString());
	}

	@Test
	public void testiranjeMaloSlozenijiPrimjer() {
		String docBody = loader("document3.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		assertEquals(docBody, parser.getDocumentNode().toString());
	}
	
	@Test
	public void testiranjeDokumentPocinjeSTagom() {
		String docBody = loader("document4.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		assertEquals(docBody, parser.getDocumentNode().toString());
	}

	@Test
	public void testiranjePrimjeraIzZadatka() {
		String docBody = loader("document1.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		assertEquals(docBody, parser.getDocumentNode().toString());
	}
	
	@Test
	public void testiranjeUlancaniFor() {
		String docBody = loader("document0.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		assertEquals(docBody, parser.getDocumentNode().toString());
	}
	
	@Test
	public void testiranjePrimjerSaSvimMogucimPravilima() {
		String docBody = loader("document8.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		assertEquals(docBody, parser.getDocumentNode().toString());
	}
	
	
	// Testovi koji izazivaju exceptione (od lakših prema težima)
	
	@Test(expected = SmartScriptParserException.class)
	public void testiranjePredanJeNull() {
		new SmartScriptParser(null);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testiranjeKrivoImeTaga() {
		String docBody = loader("document5.txt");
		new SmartScriptParser(docBody);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testiranjeTagNijeZatvoren() {
		String docBody = loader("document6.txt");
		new SmartScriptParser(docBody);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testiranjeNePostojiEndZaForTag() {
		String docBody = loader("document7.txt");
		new SmartScriptParser(docBody);
	}
	
	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			while (true) {
				int read = is.read(buffer);
				if (read < 1)
					break;
				bos.write(buffer, 0, read);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			return null;
		}
	}

}
