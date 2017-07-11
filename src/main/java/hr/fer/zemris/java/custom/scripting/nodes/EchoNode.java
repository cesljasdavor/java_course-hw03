package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Razred predstavlja čvor oznake ispisa (engl. Echo Tag). Razred nasljeđuje razred {@link Node}
 * 
 * @author Davor Češljaš
 */
public class EchoNode extends Node {

	/** Elementi oznake ispisa */
	private Element[] elements;

	/**
	 * Konstruktor inicijalizira elemente oznake ispisa. Elementi su primjerci razreda {@link Element}
	 *
	 * @param elements oznake ispisa
	 */
	public EchoNode(Element... elements) {
		this.elements = elements;
	}
	
	/**
	 * Dohvaća elemente oznake ispisa
	 *
	 * @return elemente oznake ispisa. Elementi su primjerci razreda {@link Element}
	 */
	public Element[] getElements() {
		return elements;
	}

	@Override
	public String toString() {
		StringBuilder sb =  new StringBuilder();
		sb.append("{$= ");
		for(Element element : elements) {
			sb.append(element).append(" ");
		}
		sb.append("$}");
		return sb.toString();
	}
		
}
