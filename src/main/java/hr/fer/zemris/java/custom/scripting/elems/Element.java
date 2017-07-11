package hr.fer.zemris.java.custom.scripting.elems;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Razred koji predstavlja općeniti nepromjenjivi element parsiranja primjerkom razreda
 * {@link SmartScriptParser}. Formalno, svaki razred koji nasljeđuje ovaj razred
 * predstavlja završni znak gramatike spomenutog parsera
 * 
 * @author Davor Češljaš
 */
public class Element {

	/**
	 * Metoda predstavlja tekstualnu reprezentaciju objekta ovog razreda.
	 *
	 * @return tekstualnu reprezentaciju objekta ovog razreda
	 */
	public String asText() {
		return "";
	}

	@Override
	public String toString() {
		// metodu će naslijediti sve vrste elemenata ali im asText() neće biti
		// isti
		return asText();
	}
}
