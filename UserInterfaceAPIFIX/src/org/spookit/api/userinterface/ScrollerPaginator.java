package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScrollerPaginator<Type> {

	final Collection<? extends Type> typed;
	final int a;
	public ScrollerPaginator(Collection<? extends Type> types,int max) {
		typed = types;a =max;
		length = types.size();
	}
	public List<Type> getFullPage() {
		return new ArrayList<>(typed);
	}
	public List<Type> scroll(int page) {
		try {
			List<Type> subbed = new ArrayList<>(typed);
			subbed = subbed.subList(Math.max(0, page),Math.min(subbed.size(), Math.max(0, page)+a));
			return subbed;
		} catch (Throwable t) {
			return new ArrayList<>();
		}
	}
	public boolean isValidScroll(int page) {
		return page >= 0 && scroll(page).size() >= a;
	}
	public int size() {
		return typed.size();
	}
	public int length() {
		return size();
	}
	public final int length;
	
}
