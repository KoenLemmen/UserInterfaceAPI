package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Paginator<Type> {

	public static void main(String[] args) {
		Paginator<String> s = new Paginator<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g"), 45);
		System.out.println(s.paginates()+" "+s.totalPage());
	}

	List<Type> list;
	int tot;

	public Paginator(List<Type> lists, int totalitem) {
		list = lists;
		tot = totalitem;
	}
	

	public String toString() {
		return list.toString();
	}
	
	public boolean isValidPage(int npage) {
		return npage >= 0 && npage < totalPage();
	}
	public boolean isValidPage(int npage,int maxitempp) {
		return npage >= 0 && npage < totalPage(maxitempp);
	}

	public Paginator(List<Type> lists) {
		list = lists;
		tot = 10;
	}

	public List<List<Type>> paginates() {
		return paginates(tot);
	}
	public List<List<Type>> paginates(int itemPerPage) {
		if (itemPerPage <= 0) throw new IllegalArgumentException("Invalid item per page : "+itemPerPage);
		ArrayList<List<Type>> paged = new ArrayList<>();
		int totalPage = 0;
		for (int i = 0; i < list.size(); i += itemPerPage)
			totalPage++;
		int page = 0;
		while (page < totalPage) {
			paged.add(paginate(page,itemPerPage));
			page++;
		}
		return paged;
	}
	
	public int totalPage() {
		return totalPage(tot);
	}

	public int totalPage(int itemPerPage) {
		if (itemPerPage < 0) return 0;
		int totalPage = 0;
		for (int i = 0; i < list.size(); i += itemPerPage)
			totalPage++;
		return totalPage++;
	}

	public List<Type> paginate(int page) {
		int fromIndex = (page) * tot;
		if (list == null) {
			return new ArrayList<>();
		}
		fromIndex = Math.max(fromIndex, 0);
		List<Type> subbed = list.subList(fromIndex, Math.min(fromIndex + tot, list.size()));
		return subbed;
	}

	public List<Type> paginate(int page, int itemperpage) {
		int fromIndex = (page) * itemperpage;
		if (list == null) {
			return new ArrayList<>();
		}
		fromIndex = Math.max(fromIndex, 0);
		List<Type> subbed = list.subList(fromIndex, Math.min(fromIndex + itemperpage, list.size()));
		return subbed;
	}
}
