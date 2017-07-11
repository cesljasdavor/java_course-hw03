package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Razred čiji primjerci predstavljaju niz znakova (string). Razred nasljeđuje razred
 * {@link Element}.
 * 
 * @author Davor Češljaš
 */
public class ElementString extends Element {

	/** Niz znakova (string) */
	private String value;

	/**
	 * Konstruktor koji inicijalizira vrijednost niza znakova
	 *
	 * @param value
	 *            vrijednost niza znakova
	 */
	public ElementString(String value) {
		this.value = Objects.requireNonNull(value);;
	}
	
	/**
	 * Dohvaća vrijednost niza znakova
	 *
	 * @return vrijednost niza znakova
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		//vrati nazad escapeove
		value = value.replaceAll("\\\"","\\\\\"");
		return "\"" + value + "\"";
	}
	
	
}
