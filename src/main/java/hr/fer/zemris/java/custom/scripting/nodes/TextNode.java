package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Razred predstavlja čvor normalnog teksta(ne taga). Razred nasljeđuje razred
 * {@link Node}
 * 
 * @author Davor Češljaš
 */
public class TextNode extends Node {

	/** Vrijednost teksta */
	private String text;

	/**
	 * Konstruktor inicijalizira vrijednost teksta.
	 *
	 * @param text  vrijednost teksta
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Dohvaća vrijednost teksta
	 *
	 * @return vrijednost teksta
	 */
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		//vraćanje escape sekvenci
		text = text.replaceAll("\\{","\\\\{");
		return text;
	}
}
