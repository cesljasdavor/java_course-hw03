package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Razred čiji primjerci predstavljaju funkcije. Razred nasljeđuje razred
 * {@link Element}.
 * 
 * @author Davor Češljaš
 */
public class ElementFunction extends Element {

	/** Naziv funkcije */
	private String name;

	/**
	 * Konstruktor koji inicijalizira  naziv funkcije
	 *
	 * @param name
	 *            naziv funkcije
	 */
	public ElementFunction(String name) {
		this.name = Objects.requireNonNull(name);
	}

	/**
	 * Dohvaća naziv funkcije
	 *
	 * @return naziv funkcije
	 */
	public String getName() {
		return name;
	}

	@Override
	public String asText() {
		return name;
	}
}
