package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Init {

	public static <T> List<T> list() {
		return (List<T>)(Collections.<T>emptyList());
	}
	public static <T> ArrayList<T> arrayList() {
		return (ArrayList<T>) (new ArrayList<T>());
	}
	public static <T> Set<T> set() {
		return (Set<T>)(new HashSet<T>());
	}
	public static <T> Collection<T> collection() {
		return (Collection<T>)(Collections.<T>emptyList());
	}
	public static <K,V> Map<K,V> map() {
		return (Map<K,V>)(Collections.<K,V>emptyMap());
	}
	public static void main(String[]args) {
		System.out.println(map());
	}
}
