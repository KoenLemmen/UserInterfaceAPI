package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Interface implements InventoryHolder {

	public final Map<String,Object> signature = new HashMap<>();
	ArrayList<Player> viewers = new ArrayList<>();

	public void open(Player p) {
		viewers.add(p);
		p.openInventory(getInventory());
	}

	List<ItemSlot> slots = new ArrayList<>();
	boolean interact;
	CloseEvent ce;
	ItemEvent global;
	ItemSlot empty;

	ArrayList<Integer> emptied = new ArrayList<>();
	public List<ItemSlot> getSlots() {
		return slots;
	}
	
	public Interface changeTitle(String title) {
		if (inv != null) {
			InventoryType type = inv.getType();
			List<HumanEntity> viewers = inv.getViewers();
			if (type == null) {
				inv = Bukkit.createInventory(this, type,Str.c(title));
			} else {
				inv = Bukkit.createInventory(this, inv.getSize(),Str.c(title));
			}
			build();
			for (HumanEntity en : new ArrayList<>(viewers)) en.openInventory(inv);
		}
		return this;
	}

	void build() {
		inv.clear();
		emptied.clear();
		for (ItemSlot s : slots) {
			if (s != null) {
				s.onPut(inv);
				if (s.slot >= 0 && s.slot < inv.getSize()) {
					inv.setItem(s.slot, s);
				} else {
					inv.addItem(s);
				}
			}
		}
		if (empty != null) {
			int fe;
			while ((fe = inv.firstEmpty()) > -1) {
				empty.onPut(inv);
				emptied.add(fe);
				inv.setItem(fe, empty);
			}
		}
	}
	public void updateInventory() {
		for (HumanEntity he : new ArrayList<>(inv.getViewers())) {
			he.openInventory(inv);
		}
	}
	Inventory inv;

	public static enum Row {
		FIRST,SECOND,THIRD,FOURTH,FIFTH,SIXTH;
		List<Integer> s() {
			int a = 9 * ordinal();
			ArrayList<Integer> in = new ArrayList<>();
			for (int i : new Interator(a)) in.add(i);
			return in;
		}
		public static Row valueOf(int i) {
			for (Row rows : values()) {
				if (rows.s().contains(i)) return rows;
			}
			return null;
		}
	}
	public void setRow(Row r,ItemSlot s) {
		List<Integer> slots = r.s();
		for (int a : slots) {
			ItemSlot clo;
			(clo = s.clone()).slot = a;
			replaceSlot(clo);
		}
	}
	@Override
	public Inventory getInventory() {
		return inv;
	}

	public ItemSlot get(int slot) {
		for (ItemSlot s : slots) {
			if (s.slot == slot)
				return s;
		}
		if (emptied.contains((Integer)slot)) return empty;
		return null;
	}
	
	public void setSlots(List<ItemSlot> s) {
		slots = s;
		build();
	}
	public void replaceSlot(ItemSlot s) {
		boolean edit = false;
		for (ItemSlot sl : new ArrayList<>(slots)) {
			if (s.slot == sl.slot) {
				edit = true;
				slots.remove(sl);
				s.onPut(inv);
				slots.add(s);
			}
		}
		if (!edit) {
			s.onPut(inv);
			slots.add(s);
		}
		build();
	}

}
