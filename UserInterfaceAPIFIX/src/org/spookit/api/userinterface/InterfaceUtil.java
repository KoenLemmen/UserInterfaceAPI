package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class InterfaceUtil {

	ItemUtil util = new ItemUtil();
	public Interface merge(String title,Interface... in) {
		return merge(title,Arrays.asList(in));
	}
	
	public static final ItemSlot BLACK_BORDER = border(15);
	public static final ItemSlot GRAY_BORDER = border(DyeColor.GRAY);
	public static final ItemSlot border(int data) {
		return new ItemSlot(XMaterial.fromString("STAINED_GLASS_PANE:"+data).parseItem()	).name("&7");
	}
	@SuppressWarnings("deprecation")
	public static final ItemSlot border(DyeColor color) {
		return new ItemSlot(XMaterial.fromString("STAINED_GLASS_PANE:"+color.getWoolData()).parseItem()).name("&7");
	}
	
	public Interface merge(String title,List<Interface> in) {
		InterfaceBuilder builder = new InterfaceBuilder().title(title);
		InventoryType type = null;
		ArrayList<CloseEvent> cevents = new ArrayList<>();
		ArrayList<ItemEvent> ievents = new ArrayList<>();
		for (Interface inter : in) {
			if (type != null && !type.equals(inter.getInventory().getType())) throw new UnsupportedOperationException("Different type of inventory");
			type = inter.getInventory().getType();
			if (builder.size < inter.getInventory().getSize()) builder.size(inter.getInventory().getSize());
			for (ItemSlot sl : inter.getSlots()) {
				for (ItemSlot slo : new ArrayList<>(builder.getSlots())) {
					if (slo.slot == sl.slot) builder.getSlots().remove(slo);
				}
				builder.add(sl);
			}
			if (inter.global != null) ievents.add(inter.global);
			if (inter.ce != null) cevents.add(inter.ce);
		}
		builder.closeEvent(new CloseEvent() {
			
			@Override
			public void onClose(InventoryCloseEvent e) {
				for (CloseEvent ce : cevents) {
					ce.onClose(e);
				}
			}
		});
		builder.clickEvent(new ItemEvent() {
			
			@Override
			public boolean run(InventoryClickEvent e) {
				boolean mustTrue = false;
				for (ItemEvent g : ievents) {
					if (g.run(e)) mustTrue = true;
				}
				return mustTrue;
			}
		});
		return builder.build();
	}
}
