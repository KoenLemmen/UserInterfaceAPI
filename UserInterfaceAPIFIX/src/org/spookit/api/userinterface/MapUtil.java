package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class MapUtil {
	public static void main(String[]args) {
		Map<Integer,String> map = new HashMap<>();
		map.put(12, "BlueObsidian");
		map.put(10, "T_A_G");
		map.put(109, "Slyme");
		System.out.println(reverse(sortByKey(map)));
		System.out.println(reverse(sort(Arrays.asList(3,5,1,7,8,1,8,9,4,9,0,2,46,8))));
	}
	public static <K,V> Map<K,V> reverse(Map<K,V> map) {
		List<Entry<K,V>> list = new ArrayList<>(map.entrySet());
		Collections.reverse(list);
		Map<K,V> result = new LinkedHashMap<>();
		for (Entry<K,V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	public static <T> List<T> reverse(List<T> col) {
		Collections.reverse(col);
		return col;
	}
	public static <T extends Comparable<? super T>> List<? extends T> sort(List<? extends T> col) {
		Collections.sort(col, new Comparator<T>() {

			@Override
			public int compare(T p0, T p1) {
				return p0.compareTo(p1);
			}
			
		});
		return col;
	}
	public static <K extends Comparable<? super K>,V> Map<K,V> sortByKey(Map<K,V> map) {
		List<Entry<K,V>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByKey());
		Map<K,V> result = new LinkedHashMap<>();
		for (Entry<K,V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}