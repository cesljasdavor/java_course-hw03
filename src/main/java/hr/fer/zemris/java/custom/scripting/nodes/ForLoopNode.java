package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Razred predstavlja čvor for petlje. Razred nasljeđuje razred {@link Node}
 * 
 * @author Davor Češljaš
 */
public class ForLoopNode extends Node {

	/** Varijabla u for petlji */
	private ElementVariable variable;

	/** Početna vrijednost */
	private Element startExpression;

	/** Konačna vrijednost */
	private Element endExpression;

	/** Korak za koji se varijabla uvećava (smanjuje) */
	private Element stepExpression;

	/**
	 * Konstruktor inicijalizira atribute for petlje. Svaka for petlja mora se
	 * sastojati od:
	 * <ol>
	 * <li>varijable (Primjerak razreda {@link ElementVariable})</li>
	 * <li>početne vrijednosti. (Primjerak razreda {@link Element})</li>
	 * <li>konačne vrijednosti. (Primjerak razreda {@link Element})</li>
	 * <li>(opcionalno) koraka. (Primjerak razreda {@link Element})</li>
	 * </ol>
	 *
	 * @param elements
	 *            polje referenci na objekate razreda {@link Element}
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko broj predanih elemenata nije unuata <b>[3,4]</b> ili
	 *             ako prvi element nije primjerak razreda
	 *             {@link ElementVariable}
	 */
	public ForLoopNode(Element[] elements) {
		Objects.requireNonNull(elements);
		if (elements.length < 3 || elements.length > 4) {
			throw new SmartScriptParserException("Netočan broj elemenata for petlje: " + elements.length);
		}
		if (!(elements[0] instanceof ElementVariable)) {
			throw new SmartScriptParserException("Prvi element for petlje mora biti varijabla");
		}

		this.variable = (ElementVariable) elements[0];
		this.startExpression = elements[1];
		this.endExpression = elements[2];
		if (elements.length == 4) {
			this.stepExpression = elements[3];
		}
	}

	/**
	 * Dohvaća varijablu
	 *
	 * @return varijablu
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Dohvaća početnu vrijednost
	 *
	 * @return početnu vrijednost
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Dohvaća konačnu vrijednost
	 *
	 * @return konačnu vrijednost
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Dohvaća korak za koji se varijabla uvećava (smanjuje) 
	 *
	 * @return korak za koji se varijabla uvećava (smanjuje) 
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$ FOR " + variable + " " + startExpression + " " + endExpression + " ");
		sb.append(stepExpression != null ? stepExpression : "").append("$}");
		// nadodaj svu djecu
		for (Object obj : this) {
			sb.append(obj);
		}
		sb.append("{$ END $}");
		return sb.toString();
	}

}
