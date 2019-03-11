package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
public final class StringFactory implements Cloneable {

	public static String valueOf(Object o) {
		if (o instanceof Object[]) {
			return new StringFactory((Object[])o).toJSONGroup();
		}
		return String.valueOf(o);
	}
	public static final char BUKKIT_COLOURIZER = '§';
	public static final String[] COLORIZER = new String[] {
		"§0","§1","§2","§3","§4","§5","§6","§7","§8","§9","§a","§b","§c","§d","§e","§f","§k","§l","§m","§n","§o",
		"§A","§B","§C","§D","§E","§F","§K","§L","§M","§N","§O"
	};
	public static final TreeMap<String,String> VARIABLES = null;
	public static final double CLASS_VERSION = 1.5;
	private final ArrayList<String> strings = new ArrayList<String>();
	public StringFactory(String s) {
		strings.add(s);
	}
	public StringFactory clone() {
		try {
			return (StringFactory) super.clone();
		} catch (CloneNotSupportedException e) {
			return new StringFactory(strings);
		}
	}
	public void logToConsole() {
		Bukkit.getConsoleSender().sendMessage(toString());
	}
	public static void logToConsole(String s) {
		Bukkit.getConsoleSender().sendMessage(s);
	}
	public StringFactory colourize() {
		replace("&", Character.toString(BUKKIT_COLOURIZER));
		return this;
	}
	public StringFactory(List<String> list) {
		strings.addAll(list);
	}
	public StringFactory(String... string) {
		strings.addAll(Arrays.asList(string));
	}
	public String limitToLength(int length) {
		if (toString().length() <= length) {
			return toString();
		}
		return toString().substring(0, length)+"...";
	}
	public StringFactory uncolor() {
		for (String s : new ArrayList<>(strings)) {
			strings.remove(s);
			s = uncolor(s);
			strings.add(s);
		}
		return this;
	}
	public StringFactory(boolean b) {
		strings.add(new Boolean(b).toString());
	}
	public StringFactory(int i) {
		strings.add(new Integer(i).toString());
	}
	public StringFactory(Object o) {
		strings.add(o.toString());
	}
	public StringFactory(Object...objects) {
		for (Object o : objects) strings.add(String.valueOf(o));
	}
	public Object toObject() {
		return strings;
	}
	public StringFactory append(Object o) {
		strings.add(o.toString());
		return this;
	}
	public static String color(String s) {
		if (s == null) return "";
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	private String defaultSeparator = " ";
	public String getDefaultSeparator() {
		return defaultSeparator;
	}
	public void setDefaultSeparator(String separator) {
		defaultSeparator = separator;
	}
	public String toString() {
		return toString(getDefaultSeparator());
	}
	public StringFactory reggex(String reggex) {
		for (String s : new ArrayList<String>(strings)) {
			strings.remove(s);
			Matcher m = Pattern.compile(reggex).matcher(s);
			while (m.find()) {
				s = m.group(1);
			}
			strings.add(s);
		}
		return this;
	}
	public static String reggex(String reggex, String matcher) {
		return new StringFactory(matcher).reggex(reggex).toString();
	}
	public String toString(String separator) {
		String append = "";
		boolean doIt = false;
		for (String s : strings) {
			doIt = true;
			append = append+separator+s;
		}
		if (doIt) append = append.replaceFirst(separator, "");
		return append;
	}
	public String toJSONGroup() {
		return "["+toString(", ")+"]";
	}
	String toJSONGroupNoFirst() {
		return toString(",")+"]";
	}
	public List<String> toList() {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(strings);
		return list;
	}
	public void reset(StringFactory fac) {
		strings.clear();
		strings.addAll(fac.strings);
	}

	public static boolean isInteger(String a) {
		try {
			Integer.parseInt(a);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
	public static List<String> lines(String s, int perChar) {
		ArrayList<String> a = new ArrayList<>();
		String append = new String();
		int i = 0;
		int p = 0;
		while (true) {
			if (i < s.length()) {
				if (p < perChar) {
					append = append+s.toCharArray()[i];
					p++;
				} else {
					if (i < s.length()) {
						append = append+s.toCharArray()[i];
					}
					p = 0;
					a.add(append);
					append = new String();
				}
			} else {
				if (p < perChar) {
					if (i < s.length()) {
						append = append+s.toCharArray()[i];
					}
					p++;
				} else {
					p = 0;
					a.add(append);
					append = new String();
					break;
				}
			}
			i++;
		}
		return a;
	}
	public static boolean isNumeric(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
	public boolean isNumeric() {
		try {
			Integer.parseInt(toString());
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
	public boolean isInteger() {
	    if (toString() == null) {
	        return false;
	    }
	    int length = toString().length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (toString().charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    } else if (toString().charAt(0) == '+') {
	    	if (length == 1) {
	    		return false;
	    	}
	    	i = 1;
	    }
	    for (; i < length; i++) {
	        char c = toString().charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
	@Deprecated
	public StringFactory setBukkitVisibility(boolean arg0) {
		if (arg0) {
			ArrayList<String> newest = new ArrayList<>();
			for (String s : new ArrayList<>(strings)) {
				String append = new String();
				char[] a = s.toCharArray();
				for (int i = 0; i < a.length ; i++) {
					append = append+BUKKIT_COLOURIZER+a[i];
				}
				newest.add(append);
			}
			strings.clear();
			strings.addAll(newest);
		} else {
			replace(String.valueOf(BUKKIT_COLOURIZER),"");
		}
		return this;
	}
	public Integer toInteger() {
		if (!isInteger()) return null;
		return Integer.parseInt(toString());
	}
	public int toInt() {
		if (!isInteger()) return 0;
		return Integer.parseInt(toString());
	}
	public boolean isBoolean() {
		if ((toString().equalsIgnoreCase("true")) || (toString().equalsIgnoreCase("false"))) return true;
		return false;		
	}
	public Boolean toBoolean() {
		if (!isBoolean()) throw new NullPointerException("String \""+toString()+"\" is not a boolean");
		return Boolean.parseBoolean(toString());
	}
	public String[] toArray() {
		return (String[]) strings.toArray(new String[strings.size()]);
	}
	public StringFactory replace(String old, String New) {
		for (String s : new ArrayList<String>(strings)) {
			strings.remove(s);
			strings.add(s.replace(old, New));
		}
		return this;
	}
	public StringFactory format(Object... format) {
		for (String s : strings) {
			replace(s, formatString(s,format).toString());
		}
		return this;
	}
	public static StringFactory generate(Object arg0) {
		return new StringFactory(arg0);
	}
	public static StringFactory generate(List<Object> arg0) {
		return new StringFactory(arg0);
	}
	public static String uncolor(String s) {
		s.replace(((Character)BUKKIT_COLOURIZER).toString(),"&"); 
		return s;
	}
	public static StringFactory formatString(String string, Object... format) {
		int now = 0;
		while (string.contains(now+"")) {
			if (now < format.length) {
				string = string.replace("{"+now+"}", format[now].toString());
			}
			now += 1;
		}
		StringFactory fuck = new StringFactory(string);
		
		return fuck;
	}
	public StringFactory capitalize() {
		for (String s : strings) {
			replace(s, s.substring(0, 1)+s.substring(1));
		}
		return this;
	}
}
