package com.north25.prometheus;

import java.util.Iterator;

class ImmutableIterator<T> implements Iterator<T> {
	
	private final Iterator<T> delegate;
	
	public ImmutableIterator(Iterator<T> delegate) {
		this.delegate = delegate;
	}
	
	public void remove() {
		throw new UnsupportedOperationException("Cannot delete underlying element through an immutable iterator");
	}

	public boolean hasNext() {
		return this.delegate.hasNext();
	}

	public T next() {
		return this.delegate.next();
	}
	

}
