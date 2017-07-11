package hr.fer.java.zemris.hw02.collections;

import java.util.Arrays;

/**
 * Razred koji nasljeđuje razred {@link Collection}. Razred predstavlja
 * promijenjivu kolekciju poduprtu sa poljem objekata. Važno: kolekcija <b>može
 * spremati</b> duplikate, ali <b>ne može spremati</b> vrijednost
 * <code><b>null</b></code>. Metode dodane u ovaj razred:
 * <ul>
 * <li><code>{@link #get(int)}</code></li>
 * <li><code>{@link #insert(Object, int)} </code></li>
 * <li><code>{@link #indexOf(Object)} </code></li>
 * <li><code>{@link #remove(int)}</code></li>
 * </ul>
 * 
 * Moguće implementacije konstruktora:
 * <ul>
 * <li><code>{@link #ArrayIndexedCollection(Collection, int)}</code></li>
 * <li><code>{@link #ArrayIndexedCollection(Collection)} </code></li>
 * <li><code>{@link #ArrayIndexedCollection(int)} </code></li>
 * <li><code>{@link #ArrayIndexedCollection()}</code></li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public class ArrayIndexedCollection extends Collection {

	/** Defaultni kapacitet, ukoliko isti nije zadan */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * Status koji se koristi kod metode {@link #indexOf(Object)} ukoliko index
	 * ne postoji
	 */
	private static final int DOESNT_CONTAIN = -1;

	/** Trenutna veličina kolekcije. */
	private int size;

	/** Trenutni kapacitet polja u koje se spremaju objekti {@link #elements} */
	private int capacity;

	/**
	 * Polje objekata razreda {@link Object} u koji su spremljeni u kolekciju
	 */
	private Object[] elements;

	/**
	 * Konstruktor koji prima referencu na primjerak razreda {@link Collection}
	 * čiji elementi moraju biti kopirani u novi primjerak razreda
	 * {@link ArrayIndexedCollection}. Drugi agument predstavlja inicijalnu
	 * veličinu polja potrebnu za spremanje elemenata ove kolekcije.
	 *
	 * @param other
	 *            primjerak razreda {@link Collection} čiji se elementi moraju
	 *            kopirati
	 * @param initialCapacity
	 *            inicijalni kapacitet polja koje koristi ova kolekcija
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		this.capacity = initialCapacity;
		this.elements = new Object[this.capacity];
		
		if (other != null) {
			addAll(other);
		}
		
		// ako je predan null ništa se neće dogoditi
		
	}

	/**
	 * Konstruktor koji prima samo inicijalnu veličinu polja potrebnu za
	 * spremanje elemenata ove kolekcije
	 *
	 * @param initialCapacity
	 *            inicijalni kapacitet polja koje koristi ova kolekcija
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		this(null, initialCapacity);
	}

	/**
	 * Konstruktor koji prima referencu na primjerak razreda {@link Collection}
	 * čiji elementi moraju biti kopirani u novi primjerak razreda
	 * {@link ArrayIndexedCollection}.
	 * 
	 * @param other
	 *            primjerak razreda {@link Collection} čiji se elementi moraju
	 *            kopirati
	 * 
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, DEFAULT_CAPACITY);
	}

	/**
	 * Konstruktor koji ne prima niti jedan argument. Inicijalna veličina polja
	 * postavlja se na 16 elemenata
	 */
	public ArrayIndexedCollection() {
		this(null);
	}

	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Dodaje predani objekt u kolekciju na prvo prazno mjesto u polju
	 * elemenata. Metoda neće dodati vrijednost <code><b>null</b></code>.
	 * 
	 * @param value
	 *            objekt koji je potrebno dodati
	 * @throws IllegalArgumentException
	 *             ukoliko se preda vrijednost null
	 */
	@Override
	public void add(Object value) {
		insert(value, size);
	}

	/**
	 * Miče sve elemente iz ove kolekcije. Alocirano polje neće promijeniti svoj
	 * kapacitet
	 */
	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}
		this.size = 0;
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index == DOESNT_CONTAIN) {
			return false;
		}

		remove(index);
		return true;
	}

	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
	}

	@Override
	public boolean contains(Object value) {
		return indexOf(value) != DOESNT_CONTAIN;
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(this.elements, this.size);
	}

	/**
	 * Provjerava je li <b>index</b> unutar granica
	 *
	 * @param index
	 *            predana vrijednost koja se provjerava
	 * @throws IndexOutOfBoundsException
	 *             ukoliko <b>index</b> nije unutar granica
	 */
	private void indexInRange(int index) {
		if (index < 0 || index > (size - 1)) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Vraća objekt koji je spremljen u polju elemenata ove kolekcije na
	 * poziciji <b>index</b>. Važeće pozicjie su iz intervala <b>[0, size
	 * -1]</b>
	 *
	 * @param index
	 *            pozicija traženog elementa u polju
	 * @return traženi element iz polja
	 * 
	 * @throws IndexOutOfBoundsException
	 *             ukoliko <b>index</b> nije unutar granica
	 */
	public Object get(int index) {
		indexInRange(index);
		return this.elements[index];
	}

	/**
	 * Metoda koja služi za realociranje polja elemenata ove kolekcije
	 */
	private void reallocateCollection() {
		this.capacity *= 2;
		// stvaramo novo polje sa kopijama referenci ali dvostruko većeg
		// kapaciteta
		this.elements = Arrays.copyOf(this.elements, this.capacity);
	}

	/**
	 * Ubacuje vrijednost <b>value</b> na predanu poziciju <b>position</b>. Ova
	 * metoda neće prebrisati element na toj poziciji već će sve elemente na
	 * pozicijima većim od predane poziciji pomaknuti za jedno mjesto u desno.
	 * Važeće pozicjie su iz intervala <b>[0, size]</b>.
	 *
	 * @param value
	 *            vrijednost koju je potrebno ubaciti na predanu poziciju
	 * @param position
	 *            pozicija na koju je potrebno ubaciti predanu vrijednost
	 * 
	 * @throws IllegalArgumentException
	 *             ukoliko je vrijednost <b>null</b> ili pozicija nije u važećem
	 *             rasponu
	 */
	public void insert(Object value, int position) {
		// jesu li agrumenti ispravni
		if (position < 0 || position > size || value == null) {
			throw new IllegalArgumentException();
		}

		// je li kapacitet dovoljno velik
		if (size == capacity) {
			reallocateCollection();
		}
		// pomakni sve od positiona za jedan u desno
		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		elements[position] = value;
		size++;
	}

	/**
	 * Pretražuje kolekciju i vraća prvu poziciju na kojoj je našla predanu
	 * vrijednost ili -1 ukoliko predana vrijednosti ne postoji u ovoj kolekciji
	 *
	 * @param value
	 *            vrijednost koja se pretražuje
	 * @return pozicija na kojoj je prvi puta pronađena vrijednost ili -1
	 *         ukoliko vrijednost nije pronađena
	 */
	public int indexOf(Object value) {
		for (int i = 0; i < size; i++) {
			if (elements[i].equals(value)) {
				return i;
			}
		}
		return DOESNT_CONTAIN;
	}

	/**
	 * Miče element sa predane pozicije <b>index</b>. sve elemente desno od
	 * predane pozicije pomiče za jedno mjesto u lijevo u polju. Važeće pozicjie
	 * su iz intervala <b>[0, size-1]</b>.
	 *
	 * @param index
	 *            pozicija sa koje je potrebno maknuti element
	 * 
	 * @throws IndexOutOfBoundsException
	 *             ukoliko <b>index</b> nije unutar granica
	 */
	public void remove(int index) {
		indexInRange(index);
		
		// da ne bi size išao u negativno
		if (size == 0) {
			return;
		}

		for (int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}
		
		// oslobodi element
		elements[--size] = null;
	}

}
