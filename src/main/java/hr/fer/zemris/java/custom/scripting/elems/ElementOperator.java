package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Razred čiji primjerci predstavljaju operatore. Razred nasljeđuje razred
 * {@link Element}.
 * 
 * @author Davor Češljaš
 */
public class ElementOperator extends Element {

	/** Simbol operatora */
	private String symbol;

	/**
	 * Konstruktor koji inicijalizira vrijednost simbola operatora
	 *
	 * @param symbol
	 *            vrijednost simbola operatora
	 */
	public ElementOperator(String symbol) {
		this.symbol = Objects.requireNonNull(symbol);;
	}
	
	/**
	 * Dohvaća simbol operatora
	 *
	 * @return simbol operatora
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
}
