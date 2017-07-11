package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Iterator;

import hr.fer.java.zemris.hw02.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Razred predstavlja nepromjenjivi čvor generativnog stabla prilikom parsiranja
 * primjerkom razreda {@link SmartScriptParser}. Formalno, svaki razred koji
 * nasljeđuje ovaj razred predstavlja nezavršni znak gramatike spomenutog
 * parsera. Razred također implementira sučelje {@link Iterable} kako bi se
 * moglo iterirati po čvoru koji sadrži djecu
 * 
 * @author Davor Češljaš
 */
public class Node implements Iterable<Object> {

	/** Čvorovi djeca ovog čvora. */
	private ArrayIndexedCollection children;

	/**
	 * Dodaje čvor u listu čvorova djece
	 *
	 * @param child
	 *            čvor dijete koji je potrebno dodati
	 */
	public void addChildNode(Node child) {
		if (children == null) {
			children = new ArrayIndexedCollection();
		}

		children.add(child);
	}

	/**
	 * Dohvaća broj djece ovog čvora
	 *
	 * @return broj djece ovog čvora
	 */
	public int numberOfChildren() {
		if (children == null) {
			return 0;
		}

		return children.size();
	}

	/**
	 * Dohvaća dijete na poziciji <b>index</b> unutar liste čvorova djece.
	 *
	 * @param index
	 *            pozicija u listi čvorova djece s koje se dohvaća referenca na
	 *            čvor dijete
	 * @return referencu na čvor dijete koje se nalazi na poziciji <b>index</b>
	 *         u listi čvorova djece
	 * 
	 * @throws IndexOutOfBoundsException
	 *             - ukoliko index nije unutar granica
	 */
	public Node getChild(int index) {
		return (Node) children.get(index);
	}

	@Override
	public Iterator<Object> iterator() {
		return new NodeIterator();
	}

	/**
	 * Pomoćni razred koji implementira sučelje {@link Iterator}.
	 * 
	 * @author Davor Češljaš
	 */
	private class NodeIterator implements Iterator<Object> {

		/** Trenutna pozicija elementa u listi čvorova djece */
		private int currentIndex;

		@Override
		public boolean hasNext() {
			return children != null && currentIndex < children.size();
		}

		/**
		 * @throws UnsupportedOperationException
		 *             ukoliko čvor nema djece
		 */
		@Override
		public Object next() {
			if (children == null) {
				throw new UnsupportedOperationException();
			}
			return children.get(currentIndex++);
		}
	}

}
