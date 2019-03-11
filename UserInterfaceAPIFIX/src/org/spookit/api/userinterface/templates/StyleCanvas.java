package org.spookit.api.userinterface.templates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.inventory.InventoryType;
import org.spookit.api.userinterface.ItemSlot;
import org.spookit.api.userinterface.ItemUtil;

public class StyleCanvas {

	ItemUtil util = new ItemUtil();
	List<ItemSlot> slot;
	ItemSlot next;
	ItemSlot prev;
	InventoryType type;
	int size;
	public StyleCanvas(InventoryType type,List<ItemSlot> slots,ItemSlot nextPage,ItemSlot prevPage) {
		slot = slots; next = nextPage; prev = prevPage; this.type = type;
		//detectUnusedSlots();
		rebuild();
	}
	public StyleCanvas(int si,List<ItemSlot> slots,ItemSlot nextPage,ItemSlot prevPage) {
		slot = slots; next = nextPage; prev = prevPage;size = si;
		//detectUnusedSlots();
		rebuild();
	}
	public ItemSlot getNext() {
		return next;
	}
	public ItemSlot getPrev() {
		return prev;
	}
	public InventoryType getType() {
		return type;
	}
	public int getSize() {
		return type == null ? size : type.getDefaultSize();
	}
	public void detectUnusedSlots() {
		for (int i = 0;i<(type == null ? size : type.getDefaultSize());i++) {
			unusedSlots.add(i);
		}
		for (ItemSlot item : new ArrayList<>(slot)) {
			if (item != null && item.getAmount() > 0 && util.isVisibleItem(item.getType())) {
				Integer slot = Integer.valueOf(item.getSlot());
				if (unusedSlots.contains(slot)) unusedSlots.remove(slot);
			}
		}
		if (next != null) unusedSlots.remove(Integer.valueOf(next.getSlot()));
		if (prev != null) unusedSlots.remove(Integer.valueOf(prev.getSlot()));
	}
	public void rebuild() {
		unusedSlots.clear();
		detectUnusedSlots();
	}
	ArrayList<Integer> unusedSlots = new ArrayList<>();
	public void setUnusedSlots(int...ints) {
		unusedSlots.clear();
		for (int i : ints) {
			Integer in = Integer.valueOf(i);
			if (!unusedSlots.contains(in)) unusedSlots.add(in);
		}
	}
	public ArrayList<Integer> getUnusedSlots() {
		return unusedSlots;
	}
}
