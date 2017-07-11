package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.java.zemris.hw02.collections.ArrayIndexedCollection;
import hr.fer.java.zemris.hw02.collections.Collection;
import hr.fer.java.zemris.hw02.collections.EmptyStackException;
import hr.fer.java.zemris.hw02.collections.ObjectStack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

import hr.fer.zemris.java.custom.scripting.lexer.SmartLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.SmartLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartToken;
import hr.fer.zemris.java.custom.scripting.lexer.SmartTokenType;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Razred koji predstavlja sintaksni analizator. Razred sadrži primjerak razreda
 * {@link SmartScriptLexer} te nad njim poziva
 * {@link SmartScriptLexer#nextToken()} i gradi generativno stablo. Razred
 * prilikom gradnje stabla koristi razred {@link Node} i razrede izvedene iz
 * njega kao čvorove (nezavršni znakovi gramatike). Razred također koristi
 * razred {@link Element} i razrede izvedene iz njega kao listove generativnog
 * stabla (završni znakovi gramatike). Razred započinje parsiranje prilikom
 * inicijalizacije (poziva konstruktora). Rezultat je moguće dohvatiti pomoću
 * metode {@link #getDocumentNode()}.
 * 
 * @see SmartScriptLexer
 * 
 * @author Davor Češljaš
 */
public class SmartScriptParser {

	/** Primjerak leksičkog analizatora koji se koristi prilikom parsiranja. */
	private SmartScriptLexer lexer;

	/** Vršni čvor generativnog stabla */
	private DocumentNode documentNode;

	/** Pomoćni stog koji se koristi prilikom izgradnje stabla */
	private ObjectStack nodeStack;

	/**
	 * Konstruktor koji stvara primjerak razreda {@link SmartScriptLexer} i
	 * predaje mu predani tekst <b>documentBody</b>. Nakon uspješnog stvaranja
	 * leksičkog analizatora kreće sintaksna analiza unutar koje se gradi
	 * generativno stablo.
	 *
	 * @param documentBody
	 *            tekst koji je potrebno parsirati u generativno stablo
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko iz predanog teksta <b>documentBody</b> nije moguće
	 *             stvoriti generativno stablo
	 */
	public SmartScriptParser(String documentBody) {
		if (documentBody == null) {
			throw new SmartScriptParserException("Predani dokument je null");
		}
		lexer = new SmartScriptLexer(documentBody);
		nodeStack = new ObjectStack();
		documentNode = new DocumentNode();
		parseDocument();
	}

	/**
	 * Dohvaća vršni čvor generativnog stabla
	 *
	 * @return vršni čvor generativnog stabla
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}

	/**
	 * Pomoćna metoda u kojoj se inicijalizira početno "stanje" parsera i
	 * započinje parsiranje.
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko parsiranje nije uspjelo
	 */
	private void parseDocument() {
		// inicijalno na stogu se nalazi samo dokument
		nodeStack.push(documentNode);
		parseInput();
		if (nodeStack.size() != 1) {
			throw new SmartScriptParserException(
					"Po završetku parsiranja na stogu smije biti samo jedan element. Broj elemenata je: "
							+ nodeStack.size());
		}
	}

	/**
	 * Pomoćna metoda koja ovisno o stanju leksičkog analizatora stvara ili novi
	 * primjerak razreda {@link TextNode} ili neki od primjeraka koji
	 * predstavljaju tag:
	 * <ul>
	 * <li>{@link ForLoopNode} ili tag koji ga zatvara</li>
	 * <li>{@link EchoNode}</li>
	 * </ul>
	 */
	private void parseInput() {
		// riješi da ovo ne puca
		while (getNextToken().getType() != SmartTokenType.EOF) {
			if (lexer.getState() == SmartLexerState.TEXT) {
				addTextNode();
			} else {
				parseTag();
			}
		}
	}

	/**
	 * Pomoćna metoda koja primjerku razreda {@link Node} koji je na vrhu stoga
	 * dodaje primjerak razreda {@link TextNode}
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko leksički analizator dojavi da je nastala pogreška
	 */
	private void addTextNode() {
		Node parent = requireParent();
		parent.addChildNode(new TextNode(lexer.getCurrentToken().getValue().toString()));
	}

	/**
	 * Pomoćna metoda koja pokreće parsiranje sljedećeg taga. Metoda se poziva
	 * kroz metodu {@link #parseInput()} ukoliko je pročitan token tipa
	 * {@link SmartTokenType#TAG_OPEN}.
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko sljedeći token nije tipa
	 *             {@link SmartTokenType#TAG_NAME}.
	 */
	private void parseTag() {
		SmartToken token = getNextToken();
		if (!isTokenOfType(SmartTokenType.TAG_NAME)) {
			throw new SmartScriptParserException(
					"Nakon otvorenog taga mora doći neki od mogućih imena tagova (for, end ili =)");
		}
		String tagName = token.getValue().toString();
		if (tagName.equalsIgnoreCase(SmartScriptLexer.FOR)) {
			addForLoopNode();
		} else if (tagName.equalsIgnoreCase(SmartScriptLexer.END)) {
			closeTag();
		} else {
			addEchoNode();
		}
	}

	/**
	 * Pomoćna metoda koja primjerku razreda {@link Node} koji je na vrhu stoga
	 * dodaje primjerak razreda {@link ForLoopNode}
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko je konstruktoru razreda {@link ForLoopNode} predan
	 *             krivi broj elemenata ili elementi krivog tipa. Također je
	 *             moguće da leksički analizator dojavi da je nastala pogreška
	 */
	private void addForLoopNode() {
		Node parent = requireParent();
		ForLoopNode node = new ForLoopNode(requireTagElements());
		parent.addChildNode(node);
		// for loop može biti i parrent od drugih nodeova
		nodeStack.push(node);
	}

	/**
	 * Pomoćna metoda koja skida trenutni vrh stoga ukoliko uspije parsiranje
	 * END oznake
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko parsiranje END oznake nije uspjelo
	 */
	private void closeTag() {
		/*
		 * close tag će sa stoga skinuti bilo koji node (for ili document), ako
		 * skine dokument program će sigurno puknuti prilikom dodavanja bilo
		 * kojeg djeteta ili prilikom finalne provjere nalazi li se na stogu
		 * točno jedan node
		 */
		getNextToken();
		if (!isTokenOfType(SmartTokenType.TAG_CLOSE)) {
			throw new SmartScriptParserException("Niste zatvorili end tag");
		}
		requireParent(); // baca exception ako nema roditelja
		nodeStack.pop();
	}

	/**
	 * Pomoćna metoda koja primjerku razreda {@link Node} koji je na vrhu stoga
	 * dodaje primjerak razreda {@link EchoNode}
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko leksički analizator dojavi da je nastala pogreška
	 */
	private void addEchoNode() {
		Node parent = requireParent();
		parent.addChildNode(new EchoNode(requireTagElements()));
	}

	/**
	 * Pomoćna metoda koja izlaz leksičkog analizatora pretvara u primjerke
	 * razreda {@link Element} ili razreda koji ga nasljeđuju (ovisno o tokenu).
	 *
	 * @return polje elemenata koji predstavljaju elemente jednog od čvorova
	 *         {@link ForLoopNode} ili {@link EchoNode}
	 * 
	 * @throws ukoliko
	 *             dokument završi prije zatvaranja taga
	 */
	private Element[] requireTagElements() {
		// token koji predstavlja ime taga nam ne treba
		getNextToken();
		Collection elements = new ArrayIndexedCollection();
		while (!isTokenOfType(SmartTokenType.TAG_CLOSE)) {
			if (isTokenOfType(SmartTokenType.EOF)) {
				throw new SmartScriptParserException("Dokument došao do kraja prije zatvaranja taga!");
			}
			elements.add(createNewElement());
			getNextToken();
		}
		return convertToElementArray(elements);
	}

	/**
	 * Pomoćna metoda koja članove predane kolekcije <b>collection</b> pokušava
	 * pretvoriti u polje tipa {@link Element}[]
	 *
	 * @param collection
	 *            predana kolekcija koju pokušavamo pretvoriti u polje tipa
	 *            {@link Element}[]
	 * @return polje tipa {@link Element}[]
	 */
	private Element[] convertToElementArray(Collection collection) {
		Object[] objects = collection.toArray();
		Element[] elements = new Element[objects.length];
		for (int i = 0; i < objects.length; i++) {
			elements[i] = (Element) objects[i];
		}
		return elements;
	}

	/**
	 * Pomoćna metoda koja ovisno o trenutnom tokenu leksičkog analizatora
	 * stvara primjerak određenog razreda koji nasljeđuje razred {@link Element}
	 *
	 * @return primjerak određenog razreda koji nasljeđuje razred
	 *         {@link Element}
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko se trenutni token leksičkog analizatora ne može
	 *             protumačiti kao primjerak razreda {@link Element} ili njemu
	 *             izvedenog razreda
	 */
	private Element createNewElement() {
		SmartToken currentToken = lexer.getCurrentToken();
		switch (currentToken.getType()) {
		case CONSTANT_DOUBLE:
			return new ElementConstantDouble((Double) currentToken.getValue());
		case CONSTANT_INTEGER:
			return new ElementConstantInteger((Integer) currentToken.getValue());
		case FUNCTION:
			return new ElementFunction(currentToken.getValue().toString());
		case OPERATOR:
			return new ElementOperator(currentToken.getValue().toString());
		case STRING:
			return new ElementString(currentToken.getValue().toString());
		case VARIABLE:
			return new ElementVariable(currentToken.getValue().toString());
		default:
			throw new SmartScriptParserException("Za token tipa" + currentToken.getType() + " ne postoji element");
		}
	}

	/**
	 * Pomoćna metoda koja provjerava je li tip trenutnog tokena jednak predanom
	 * tipu <b>type</b>
	 *
	 * @param type
	 *            enumeracija tipa {@link SmartTokenType}
	 * @return <b>true</b> ako su tipovi jednaki, <b>false</b> inače
	 */
	private boolean isTokenOfType(SmartTokenType type) {
		return lexer.getCurrentToken().getType() == type;
	}

	/**
	 * Pomoćna metoda koja dohvaća trenutni vrh stoga.
	 *
	 * @return trenutni vrh stoga
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko nema čvorova na stogu.
	 */
	private Node requireParent() {
		try {
			return (Node) nodeStack.peek();
		} catch (EmptyStackException e) {
			throw new SmartScriptParserException("Stog roditeljskih čvorova je prazan");
		}
	}

	/**
	 * Pomoćna metoda koja poziva metodu {@link SmartScriptLexer#nextToken()}
	 * nad primjerkom razreda {@link SmartScriptLexer}(atribut ovog razreda).
	 * Metoda nikada neće baciti iznimku zbog učitavanja kraja niza već će
	 * vratiti token tipa {@link SmartTokenType#EOF} ukoliko je leksički
	 * analizator završio s radom
	 *
	 * @return sljedeći token
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko parser baci {@link SmartLexerException}
	 */
	private SmartToken getNextToken() {
		try {
			if (lexer.getCurrentToken() != null && isTokenOfType(SmartTokenType.EOF)) {
				return lexer.getCurrentToken();
			}
			return lexer.nextToken();
		} catch (SmartLexerException e) {
			// u zadatku je zadano da parser smije vratiti samo
			// SmartScriptParserException
			throw new SmartScriptParserException(
					"Leksički analizator(u stanje:  " + lexer.getState() + "): " + e.getMessage());
		}
	}
}
