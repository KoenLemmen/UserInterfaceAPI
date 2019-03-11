package org.spookit.api.userinterface.templates;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.spookit.api.userinterface.Interface;
import org.spookit.api.userinterface.InterfaceBuilder;
import org.spookit.api.userinterface.ItemEvent;
import org.spookit.api.userinterface.ItemSlot;
import org.spookit.api.userinterface.Paginator;

public class StyledPaginator {

	StyledPaginator() {}
	public static StyledPaginator create(String title,StyleCanvas canvas,List<ItemSlot> slots) {
		StyledPaginator pag = new StyledPaginator();
		pag.canvas= canvas;
		pag.title = title;
		if (slots != null) pag.slots.addAll(slots);
		pag.build();
		return pag;
	}
	
	StyleCanvas canvas;
	List<ItemSlot> slots = new ArrayList<>();
	String title;
	LinkedList<Interface> pages = new LinkedList<>();
	Interface empty;
	void build() {
		Paginator<ItemSlot> pager = new Paginator<>(slots);
		int total = canvas.unusedSlots.size();
		int pos = 0;
		List<List<ItemSlot>> inventories;
		for (List<ItemSlot> inv : inventories=pager.paginates(total)) {
			LinkedList<ItemSlot> sorted = new LinkedList<>(inv);
			LinkedList<Integer> sortedInt = new LinkedList<>(canvas.unusedSlots);
			InterfaceBuilder builder = new InterfaceBuilder().title(title);
			if (canvas.type != null) {
				builder.type(canvas.type);
			} else {
				builder.size(canvas.size);
			}
			for (ItemSlot sl : canvas.slot) {
				builder.add(sl.clone());
			}
			for (int i = 0;i<sorted.size() && i<sortedInt.size();i++) {
				ItemSlot slot = sorted.get(i).clone();
				Integer inte = sortedInt.get(i);
				slot.setSlot(inte);
			}
			final int a = pos;
			if (pos + 1 < inventories.size()) builder.add(canvas.next.clone().setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					open(a + 1,(Player)e.getWhoClicked());
					return true;
				}
			}));
			if (pos > 1) builder.add(canvas.prev.clone().setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					open(a-1,(Player)e.getWhoClicked());
					return true;
				}
			}));
			pos++;
			pages.addLast(builder.build());
		}
		{
			InterfaceBuilder builder = new InterfaceBuilder().title(title);
			if (canvas.type != null) {
				builder.type(canvas.type);
			} else {
				builder.size(canvas.size);
			}
			for (ItemSlot sl : canvas.slot) {
				builder.add(sl.clone());
			}
			empty = builder.build();
		}
	}
	public Interface get(int page) {
		if (page>=pages.size() || page < 0)return empty;
		return pages.get(page);
	}
	public void open(int page,Player p) {
		get(page).open(p);
	}
	public void open(Player p) {
		open(0,p);
	}
}
