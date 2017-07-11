package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred čiji primjerci predstavljaju cjelobrojnu brojčanu vrijednost. Razred
 * nasljeđuje razred {@link Element}.
 * 
 * @author Davor Češljaš
 */
public class ElementConstantInteger extends Element {

	/** Cjelobrojna konstanta */
	private int value;

	/**
	 * Konstruktor koji inicijalizira vrijednost cjelobrojne konstante
	 *
	 * @param value
	 *            vrijednost cjelobrojne konstante
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Dohvaća vrijednost cjelobrojne konstante
	 *
	 * @return vrijednost cjelobrojne konstante
	 */
	public int getValue() {
		return value;
	}

	@Override
	public String asText() {
		return String.valueOf(value);
	}
	
}
