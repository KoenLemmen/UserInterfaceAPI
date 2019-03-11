package org.spookit.api.userinterface;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.spookit.api.userinterface.Interface.Row;
import org.spookit.externalcore.Reflection;

public class InterfaceBuilder {

	String title;
	int size;
	boolean interact = false;
	InventoryType type;
	CloseEvent event;
	ItemEvent global;
	ItemSlot empty;
	ArrayList<ItemSlot> a = new ArrayList<>();
	ItemSlot row;

	public InterfaceBuilder title(String title) {
		this.title = title;
		return this;
	}

	public InterfaceBuilder closeEvent(CloseEvent e) {
		event = e;
		return this;
	}
	public InterfaceBuilder clickEvent(ItemEvent e) {
		global = e;
		return this;
	}

	public InterfaceBuilder size(int size) {
		type = null;
		this.size = size;
		return this;
	}

	public InterfaceBuilder type(InventoryType type) {
		this.type = type;
		size = type.getDefaultSize();
		return this;
	}

	public InterfaceBuilder abstractSize() {
		size = -1;
		return this;
	}

	public InterfaceBuilder allowInteract() {
		interact = true;
		return this;
	}
	
	public InterfaceBuilder fillEmpty(ItemSlot s) {
		empty = s;
		return this;
	}

	public InterfaceBuilder add(ItemSlot s) {
		a.add(s);
		return this;
	}

	public InterfaceBuilder addAll(Collection<? extends ItemSlot> s) {
		a.addAll(s);
		return this;
	}

	public ArrayList<ItemSlot> getSlots() {
		return a;
	}
	
	public InterfaceBuilder setRow(ItemSlot s) {
		row = s;
		return this;
	}

	public Interface build() {
		Interface inter = new Interface();
		inter.ce = event;
		inter.interact = interact;
		inter.slots = a;
		inter.empty = empty;
		if (size < 0) {
			size = 0;
			for (int i = 0;i<a.size();i+=9) {
				size++;
			}
			size*=9;
		}
		if (title == null) title = "";
		inter.global = global;
		if (type == null) {
			inter.inv = Bukkit.createInventory(inter, size, ChatColor.translateAlternateColorCodes('&', title));
		} else {
			inter.inv = Bukkit.createInventory(inter, type, ChatColor.translateAlternateColorCodes('&', title));
		}
		if (!(inter.inv.getHolder() instanceof Interface)) {
			/*
			 * Seems the server doesn't hold the inventory holder
			 */
			try {
				Class<?> craftInventoryCustom = Reflection.getCraftBukkitClass("inventory.CraftInventoryCustom");
				Constructor<?> constr = craftInventoryCustom.getConstructor(InventoryHolder.class,InventoryType.class,String.class);
				inter.inv = (Inventory)constr.newInstance(inter,type,ChatColor.translateAlternateColorCodes('&', title));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (row != null) inter.setRow(Row.valueOf(row.slot), row);
		inter.build();
		return inter;
	}
}
