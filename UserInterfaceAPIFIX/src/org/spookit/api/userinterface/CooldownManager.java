package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CooldownManager<T> {

	Map<T,Long> cooldowns = new HashMap<>();
	public void put(T cd,long cooldownSeconds) {
		cooldowns.remove(cd);
		cooldowns.put(cd, cooldownSeconds*1000);
	}
	
	public boolean hasDelay(T t) {
		Long get = cooldowns.get(t);
		if (get != null && get-System.currentTimeMillis() >= 0) {
			cooldowns.remove(t);
			return false;
		}
		return get != null;
	}
	
	public void remove(T cd) {
		cooldowns.remove(cd);
	}
	
	public long getDelayRemaining(T t) {
		Long get = cooldowns.get(t);
		if (get == null) return 0;
		return get-System.currentTimeMillis();
	}
	
	public void clear() {
		cooldowns.clear();
	}
	
	public Set<T> getCooldowned() {
		return cooldowns.keySet();
	}
	
	public T getLongestDelay() {
		Map<T,Long> delays = new LinkedHashMap<>();
		delays = MapUtil.reverse(MapUtil.sortByValue(delays));
		return delays.isEmpty() ? null : new ArrayList<>(delays.keySet()).get(0); 
	}
	
	public T getShortestDelay() {
		Map<T,Long> delays = new LinkedHashMap<>();
		delays = MapUtil.sortByValue(delays);
		return delays.isEmpty() ? null : new ArrayList<>(delays.keySet()).get(0); 
	}
	
}
