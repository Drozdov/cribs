package ru.cribs.helpers;

import java.util.Iterator;

public class ConcatIterable<T> implements Iterable<T> {

	private final Iterable<T> base;
	private final T newElement;
	
	public ConcatIterable(Iterable<T> base, T newElement) {
		this.base = base;
		this.newElement = newElement;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private final Iterator<T> baseIterator = base.iterator();
			private boolean newElementUsed;
			
			@Override
			public boolean hasNext() {
				return baseIterator.hasNext() || !newElementUsed;
			}

			@Override
			public T next() {
				if (baseIterator.hasNext())
					return baseIterator.next();
				newElementUsed = true;
				return newElement;
			}
		};
	}
	
}
