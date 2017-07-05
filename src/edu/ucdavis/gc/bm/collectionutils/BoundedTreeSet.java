package edu.ucdavis.gc.bm.collectionutils;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class BoundedTreeSet<E> extends TreeSet<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int limit;
	
	public BoundedTreeSet(final int limit){
		super();
		this.limit = limit; 
	}
	
	@Override
	public boolean add(E e){
		super.add(e);
		while (this.size() > limit) {
			Iterator<E> it = this.iterator();
			if (it.hasNext()){
				this.remove(it.next());
			}
		}
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c){
		super.addAll(c);
		if (this.size() > limit){
			Iterator<E> it = this.iterator();
			while (this.size() > limit && it.hasNext()) {
				this.remove(it.next());
			}
		}
		return true;
	}
}
