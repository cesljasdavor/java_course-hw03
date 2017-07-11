package hr.fer.zemris.java.custom.scripting.nodes;


/**
 * Razred predstavlja vršni čvor u generativnom stablu. Razred nasljeđuje razred {@link Node}
 * 
 * @author Davor Češljaš
 */
public class DocumentNode extends Node {

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Object obj : this) {
			sb.append(obj);
		}
		return sb.toString();
	} 
}
