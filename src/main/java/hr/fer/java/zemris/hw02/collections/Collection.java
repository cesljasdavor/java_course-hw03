package hr.fer.java.zemris.hw02.collections;

/**
 * Razred predstavlja kolekciju objekata razreda {@link Object}(ili bilo kojeg
 * drugog razreda, budući da svaki razred nasljeđuje razred Object) Sadrži
 * sljedeće metode:
 * <ul>
 * <li><code>{@link #isEmpty()}</code></li>
 * <li><code>{@link #size()} </code></li>
 * <li><code>{@link #add(Object)} </code></li>
 * <li><code>{@link #contains(Object)}</code></li>
 * <li><code>{@link #remove(Object)} </code></li>
 * <li><code>{@link #toArray()}</code></li>
 * <li><code>{@link #forEach(Processor)} </code></li>
 * <li><code>{@link #addAll(Collection)}</code></li>
 * <li><code>{@link #clear()} </code></li>
 * </ul>
 * 
 * @author Davor Češljaš
 * 
 */
public class Collection {

	/**
	 * Defaultni zaštićeni konstruktor
	 */
	protected Collection() {
		// TODO Method body
	}

	/**
	 * Vraća vrijednost <code><b>true</b></code> ukoliko kolekcija ne sadrži
	 * niti jedan objekt. U suprotnom vraća <code><b>false</b></code>
	 *
	 * @return <code><b>true</b></code> ako je kolekcija prazna,
	 *         <code><b>false</b></code> inače
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Vraća broj trenutno pospremljenih objekata u kolekciji
	 *
	 * @return broj objekata u kolekciji
	 */
	public int size() {
		return 0;
	}

	/**
	 * Dodaje predani objekt u kolekciju.
	 *
	 * @param value
	 *            objekt koji je potrebno dodati
	 */
	public void add(Object value) {
		// TODO Method body
	}

	/**
	 * Vraća <code><b>true</b></code> samo ako kolekcja sadrži zadanu
	 * <code><b>vrijednost</b></code>. Za odlučivanje sadrži li kolekcija zadanu
	 * vrijednost koristi se metoda {@link #equals(Object)}.
	 *
	 * @param value
	 *            vrijednost kojoj ispitujemo postojanje u kolekciji
	 * @return <code><b>true</b></code>, ako i samo ako kolekcija sadrži predanu
	 *         vrijednost
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Vraća <code><b>true</b></code> samo ako kolekcja sadrži zadanu
	 * <code><b>vrijednost</b></code>. Za odlučivanje sadrži li kolekcija zadanu
	 * vrijednost koristi se metoda {@link #equals(Object)}. Prije nego što
	 * vrati vrijednost <code><b>true</b></code> briše prvo pojavljivanje
	 * elementa u kolekciji
	 *
	 * @param value
	 *            vrijednost koju je potrebno izbrisati ako postoji u kolkeciju
	 * @return true, je li vrijednost postojala (Budući da kolekcija može imati
	 *         duplikate moguće je da i dalje postoji)
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Alocira novo polje, puni ga elementima kolekcije i vraća kao povratnu
	 * vrijednost. Ova metoda nikada neće vratiti <code><b>null</b></code>.
	 *
	 * @return novo polje objekata koje sadrži kolekcija
	 * @throws UnsupportedOperationException
	 *             ako metoda nije implementirana
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Metoda poziva metodu primjerka razreda {@link Processor#process(Object)}
	 * nad svakim elementom ove kolekcije. Nije određen redoslijed slanja
	 * elemenata
	 *
	 * @param processor
	 *            the processor
	 */
	public void forEach(Processor processor) {
		// TODO Method body
	}

	/**
	 * Metoda ovoj kolekciji dodaje sve elemente predane kolekcije. Prilikom
	 * dodavanje predana kolekcija ostaje nepromjenjena. Metodi se može predati
	 * i argument <code><b>null</b></code>. Time metoda neće ništa napraviti nad
	 * ovom kolekcijom
	 *
	 * @param other
	 *            kolekcija iz koje se kopiraju elementi
	 */
	public void addAll(Collection other) {
		// jedan od mogućih ishoda
		if (other == null) {
			return;
		}
		class AddAllProcessor extends Processor {
			@Override
			public void process(Object value) {
				Collection.this.add(value);
			}
		}
		Processor processor = new AddAllProcessor();
		other.forEach(processor);
	}

	/**
	 * Miče sve elemente iz ove kolekcije
	 */
	public void clear() {
		// TODO Method body
	}
}
