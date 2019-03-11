package org.spookit.api.userinterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

import org.bukkit.ChatColor;

public class Str implements Iterable<String>,CharSequence,Serializable,Comparable<Str> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<String> strings = new ArrayList<>();
	public Str(Collection<? extends String> a) {
		strings.addAll(a);
	}
	public Str(Str another) {
		this(another.strings);
	}
	public Str() {
	}
	public Str(String...strings) {
		this.strings.addAll(Arrays.asList(strings));
	}
	public Str append(String arg0) {
		strings.add(arg0);
		return this;
	}
	public Str clone() {
		return new Str(strings);
	}
	public Collection<? extends String> asList() {
		return new ArrayList<>(strings);
	}
	public String toString(String delimiter) {
		boolean first = true;
		String appender =new String();
		for (String s : clone().strings) {
			if (first) {
				first = false;
				appender+=s;
			} else {
				appender+=delimiter+s;
			}
		}
		return appender;
	}
	public String toString(String delimiter,String endDelimiter) {
		boolean first = true;
		String appender =new String();
		int index = 0;
		for (String s : clone().strings) {
			if (first) {
				first = false;
				appender+=s;
			} else if (index == clone().strings.size()-1) {
				appender+=endDelimiter+s;
			} else {
				appender+=delimiter+s;
			}
			index++;
		}
		return appender;
	}
	public String toString(char c) {
		return toString(String.valueOf(c));
	}
	public String toString() {
		return toString("");
	}
	public Str clone(Consumer<Str> a) {
		Str str = clone();
		a.accept(str);
		return str;
	}
	public Str replaceFirst(String regex,String target) {
		return clone(a->{
			a.strings.replaceAll(b->{
				return b.replaceFirst(regex, target);
			});
		});
	}
	public Str replaceAll(String regex,String target) {
		return clone(a->{
			a.strings.replaceAll(b->{
				return b.replaceAll(regex, target);
			});
		});
	}
	public Str replace(String arg0,String target) {
		return clone(a->{
			a.strings.replaceAll(b->{
				return b.replace(arg0, target);
			});
		});
	}
	public Str replace(char c,char t) {
		return clone(a->{
			a.strings.replaceAll(b->{
				return b.replace(c, t);
			});
		});
	}
	public Str colorize() {
		return clone(a->{
			a.strings.replaceAll(b->{
				return ChatColor.translateAlternateColorCodes('&', b);
			});
		});
	}
	public Str stripColor() {
		return clone(a->{
			a.strings.replaceAll(b->{
				return ChatColor.stripColor(b);
			});
		});
	}
	public Str forceColor() {
		return replace('&','\u00A7');
	}
	public Str forceStripColor() {
		return replace('\u00A7','&');
	}
	public boolean contains(String arg0) {
		for (String s : clone().strings) {
			if (s.contains(arg0)) return true;
		}
		return false;
	}
	public Str concat(String... a) {
		return clone(b->{
			b.strings.addAll(Arrays.asList(a));
		});
	}
	public char[] toCharArray() {
		return toString().toCharArray();
	}
	public Str split(String delimiter,int limit) {
		ArrayList<String> splitment = new ArrayList<>();
		for (String s : this) {
			splitment.addAll(Arrays.asList(s.split(delimiter,limit)));
		}
		return new Str(splitment);
	}
	public Str split(String delimiter) {
		ArrayList<String> splitment = new ArrayList<>();
		for (String s : this) {
			splitment.addAll(Arrays.asList(s.split(delimiter)));
		}
		return new Str(splitment);
	}
	public Str concat(Str a) {
		return clone(b->{
			b.strings.addAll(a.strings);
		});
	}
	public Str concat(Collection<? extends String> a) {
		return clone(b->{
			b.strings.addAll(a);
		});
	}
	public void finalize() {
	}
	public boolean startsWith(String a) {
		return !isEmpty() && strings.get(0).startsWith(a);
	}
	public boolean endsWith(String a) {
		return !isEmpty() && strings.get(strings.size()-1).endsWith(a);
	}
	public boolean equals(Object o) {
		if (o instanceof String) {
			return toString().equals(o);
		}
		if (o instanceof Str) {
			return ((Str)o).toString().equals(toString());
		}
		return super.equals(o);
	}
	public boolean equalsIgnoreCase(Object o) {
		if (o instanceof String) {
			return toString().equalsIgnoreCase((String)o);
		}
		if (o instanceof Str) {
			return toString().equalsIgnoreCase(((Str)o).toString());
		}
		return equals(o);
	}
	public boolean isEmpty() {
		for (String s : this) {
			if (!s.isEmpty()) return false;
		}
		return true;
	}
	public Str substr(int start) {
		return clone(a->a.strings.subList(start, a.strings.size()));
	}
	public Str substr(int start,int end) {
		return clone(a->a.strings.subList(start, end));
	}
	@Override
	public Iterator<String> iterator() {
		return strings.iterator();
	}
	@Override
	public char charAt(int index) {
		return toString().charAt(index);
	}
	@Override
	public int length() {
		return toString().length();
	}
	@Override
	public CharSequence subSequence(int start, int end) {
		return toString().subSequence(start, end);
	}
	public int size() {
		return strings.size();
	}
	public static Str valueOf(Object o) {
		return new Str(String.valueOf(o));
	}
	public String[] toArray() {
		return strings.toArray(new String[strings.size()]);
	}
	public Str center() {
		return clone(a->{
			ArrayList<String> centered = new ArrayList<>();
			for (String s : new ArrayList<>(a.strings)) {
				centered.addAll(Center.multicenter(s));
			}
			a.strings = centered;
		});
	}
	public static String c(String a) {
		return ChatColor.translateAlternateColorCodes('&',a);
	}
	@Override
	public int compareTo(Str arg0) {
		int arg1 = this.strings.size();
		int arg2 = arg0.strings.size();
		int arg3 = Math.min(arg1, arg2);
		String[] arg4 = this.strings.toArray(new String[arg1]);
		String[] arg5 = arg0.strings.toArray(new String[arg2]);

		for (int arg6 = 0; arg6 < arg3; ++arg6) {
			String arg7 = arg4[arg6];
			String arg8 = arg5[arg6];
			if (arg7 != arg8) {
				return arg7.compareTo(arg8);
			}
		}

		return arg1 - arg2;
	}
	public int compareToIgnoreCase(Str arg0) {
		int arg1 = this.strings.size();
		int arg2 = arg0.strings.size();
		int arg3 = Math.min(arg1, arg2);
		String[] arg4 = this.strings.toArray(new String[arg1]);
		String[] arg5 = arg0.strings.toArray(new String[arg2]);

		for (int arg6 = 0; arg6 < arg3; ++arg6) {
			String arg7 = arg4[arg6].toLowerCase();
			String arg8 = arg5[arg6].toLowerCase();
			if (arg7 != arg8) {
				return arg7.compareTo(arg8);
			}
		}

		return arg1 - arg2;
	}
}