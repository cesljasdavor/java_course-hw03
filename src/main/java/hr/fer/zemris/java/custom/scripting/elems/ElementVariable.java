package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;


/**
 * Razred čiji primjerci predstavljaju varijablu. Razred nasljeđuje razred
 * {@link Element}.
 * 
 * @author Davor Češljaš
 */
public class ElementVariable  extends Element{
	
	/** Naziv varijable */
	private String name;
	
	/**
	 * Konstruktor koji inicijalizira naziv varijable
	 *
	 * @param name
	 *            naziv varijable
	 */
	public ElementVariable(String name) {
		this.name = Objects.requireNonNull(name);
	}

	/**
	 * Dohvaća naziv varijable
	 *
	 * @return naziv varijable
	 */
	public String getName() {
		return name;
	}

	@Override
	public String asText() {
		return name;
	}
}
