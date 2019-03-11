package org.spookit.api.userinterface;

import java.util.Iterator;

public class Interator implements Iterable<Integer> {

	public static void main(String[]args) {
		Interator it = new Interator(100);
		for (int i : it) {
			System.out.println(i);
		}
	}
	public Interator(int size) {
		total =size;
	}
	public int size() {
		return total;
	}
	int total;
	@Override
	public Iterator<Integer> iterator() {
		return new IntIterator(total);
	}
	
	class IntIterator implements Iterator<Integer> {

		int m;
		IntIterator(int max) {
			m = max;
		}
		int now = 0;
		@Override
		public boolean hasNext() {
			return now < m;
		}

		public Integer now() {
			return m;
		}
		@Override
		public Integer next() {
			int old = now;
			now++;
			return old;
		}
		
	}

}
