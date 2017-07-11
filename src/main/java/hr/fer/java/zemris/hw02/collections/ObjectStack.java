package hr.fer.java.zemris.hw02.collections;

/**
 * Razred koji predstavlja implementaciju LIFO(engl. Last In First Out) stoga
 * objekata razreda {@link Object}. Razred sadrži klasične metode za rad sa
 * stogom. Stog <code><b>može sadržavati</b></code> duplikate, ali
 * <code><b>ne može sadržavat null</b></code> vrijednosti. Popis metoda:
 * <ul>
 * <li><code>{@link #isEmpty()}</code></li>
 * <li><code>{@link #size()} </code></li>
 * <li><code>{@link #push(Object)} </code></li>
 * <li><code>{@link #pop()}</code></li>
 * <li><code>{@link #peek()} </code></li>
 * <li><code>{@link #clear()}</code></li>
 * </ul>
 * 
 * Razred sadrži samo jedan konstruktor:
 * <code>{@link #ObjectStack()} </code>
 * 
 *  @author Davor Češljaš
 * 
 */
public class ObjectStack {

	/**
	 * Kolekcija koju koristimo kao spremište podataka za ovaj stog Kolekcija je
	 * primjerak razreda {@link ArrayIndexedCollection} te se metode ovog stoga
	 * oslanjaju na metode te kolekcije
	 */
	private ArrayIndexedCollection collection = new ArrayIndexedCollection();

	/**
	 * Konstruktor koji inicijalizira stog. Korisnik može biti siguran da je
	 * nakon poziva konstruktora stog prazan ,odnosno metoda {@link #isEmpty()}
	 * vraća vrijednost <code><b>true</b></code>
	 */
	public ObjectStack() {
		this.collection = new ArrayIndexedCollection();
	}

	/**
	 * Vraća vrijednost <code><b>true</b></code> ukoliko stog ne sadrži niti
	 * jedan objekt. U suprotnom vraća <code><b>false</b></code>
	 *
	 * @return <code><b>true</b></code> ako je stog prazna,
	 *         <code><b>false</b></code> inače
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	/**
	 * Vraća broj trenutno pospremljenih objekata na stogu
	 *
	 * @return broj objekata u kolekciji
	 */
	public int size() {
		return collection.size();
	}

	/**
	 * Dodaje objekt <code><b>value</b></code> na vrh stoga.
	 *
	 * @param value
	 *            objekt koji je potrebno dodati na stog
	 * 
	 * @throws IllegalArgumentException
	 *             - ukoliko se preda vrijednost null
	 */
	public void push(Object value) {
		collection.add(value);
	}

	/**
	 * Skida zadnje dodani objekt sa stoga i vraća ga kroz povratnu vrijednost
	 *
	 * @return zadnje dodani objekt na stogu
	 * 
	 * @throws EmptyStackException
	 *             ukoliko je stog prazan
	 */
	public Object pop() {
		// dohvati zadnji
		Object element = peek();

		// izbriši ga iz kolekcije
		collection.remove(size() - 1);
		return element;
	}

	/**
	 * Analogno metodi {@link #pop()}. Jedina rezlika je što stog ostaje
	 * nepromijenje, odnosno vraćeni objekt se nakon poziva ove metode i dalje
	 * nalazi na stogu
	 *
	 * @return zadnje dodani objekt na stogu
	 *
	 * @throws EmptyStackException
	 *             ukoliko je stog prazan
	 */
	public Object peek() {
		// ukoliko je stog prazan baci EmptyStackException
		if (size() == 0) {
			throw new EmptyStackException("Stog je prazan");
		}
		// dohvati zadnji
		return collection.get(collection.size() - 1);

	}

	/**
	 * Miče sve elemente sa stoga. Pozivom metoda {@link #peek()} ili
	 * {@link #pop()} nakon poziva ove metode izazvati će
	 * {@link EmptyStackException}
	 */
	public void clear() {
		collection.clear();
	}

}
