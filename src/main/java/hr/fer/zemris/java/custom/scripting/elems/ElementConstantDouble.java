package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred čiji primjerci predstavljaju realnu brojčanu vrijednost. Razred
 * nasljeđuje razred {@link Element}.
 * 
 * @author Davor Češljaš
 */
public class ElementConstantDouble extends Element {

	/** Realna konstanta  */
	private double value;

	/**
	 * Konstruktor koji inicijalizira vrijednost realne konstante
	 *
	 * @param value
	 *            vrijednost realne konstante
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	/**
	 * Dohvaća vrijednost realne konstante
	 *
	 * @return vrijednost realne konstante
	 */
	public double getValue() {
		return value;
	}

	@Override
	public String asText() {
		return String.valueOf(value);
	}

}
