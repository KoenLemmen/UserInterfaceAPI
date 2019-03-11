package org.spookit.api.userinterface.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.spookit.api.userinterface.Asker;
import org.spookit.api.userinterface.Interface;
import org.spookit.api.userinterface.InterfaceBuilder;
import org.spookit.api.userinterface.ItemEvent;
import org.spookit.api.userinterface.ItemSlot;
import org.spookit.api.userinterface.Paginator;
import org.spookit.api.userinterface.Str;
import org.spookit.api.userinterface.XMaterial;

public class BorderedGUIPaginator {

	public static PaginatorInfo open(int page,Player p,List<ItemSlot> slots,String title,ItemSlot backbutton) {
		int usedSlots[] = {0,0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
		int unusedSlots[] = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
		Paginator<ItemSlot> pages = new Paginator<>(slots,unusedSlots.length);
		List<ItemSlot> paged = pages.paginate(page);
		InterfaceBuilder builder = new InterfaceBuilder().title(title).size(54);
		ArrayList<ItemSlot> slotss = new ArrayList<>();
		for (int i = 0;i<paged.size() && i < unusedSlots.length;i++) {
			slotss.add(paged.get(i).setSlot(unusedSlots[i]));
		}
		for (int i : usedSlots) slotss.add(new ItemSlot(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).setSlot(i).name("&7"));
		builder.fillEmpty(new ItemSlot(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).name("&7"));
		builder.addAll(slotss);
		Interface inter = builder.build();
		if (page > 0) {
			inter.replaceSlot(new ItemSlot(XMaterial.CHEST_MINECART.parseItem()).name("&a&m<--- &aPrevious Page").setSlot(47).setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					open(page-1,p,slots,title,backbutton);
					return true;
				}
			}));
		}
		if (page + 1 < pages.totalPage()) {
			inter.replaceSlot(new ItemSlot(Material.MINECART).name("&aNext Page &m--->").setSlot(51).setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					open(page+1,p,slots,title,backbutton);
					return true;
				}
			}));
		}
		
		if (backbutton != null) inter.replaceSlot(backbutton.setSlot(45));
		inter.replaceSlot(new ItemSlot(Material.NAME_TAG).name("&aSearch &7&m--&fO").setSlot(53).setExecutor(new ItemEvent() {
			
			@Override
			public boolean run(InventoryClickEvent e) {
				Asker.ask(p, new Consumer<Str>() {
					
					@Override
					public void accept(Str arg0) {
						ArrayList<ItemSlot> mod = new ArrayList<>();
						for (ItemSlot it : new ArrayList<>(slots)) {
							if (it.hasItemMeta()) {
								String lol = it.getItemMeta().hasDisplayName() ? it.getItemMeta().getDisplayName() : "";
								if (it.getItemMeta().hasLore()) for (String lore : it.getItemMeta().getLore()) lol+=lore;
								lol=ChatColor.stripColor(lol.toLowerCase());
								if (lol.contains(arg0.toString().toLowerCase())) {
									mod.add(it);
								}
							}
						}
						openOld(page,p,slots,mod,title,backbutton);
					}
				}, "&1Search Keyword", "");
				return true;
			}
		}));
		inter.open(p);
		PaginatorInfo info = new PaginatorInfo();
		info.cp = page;
		info.mp = pages.totalPage();
		info.current = inter;
		return info;
	}
	static PaginatorInfo openOld(int page,Player p,List<ItemSlot> original,List<ItemSlot> slots,String title,ItemSlot backbutton) {
		int usedSlots[] = {0,0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
		int unusedSlots[] = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
		Paginator<ItemSlot> pages = new Paginator<>(slots,unusedSlots.length);
		List<ItemSlot> paged = pages.paginate(page);
		InterfaceBuilder builder = new InterfaceBuilder().title(title).size(54);
		ArrayList<ItemSlot> slotss = new ArrayList<>();
		for (int i = 0;i<paged.size() && i < unusedSlots.length;i++) {
			slotss.add(paged.get(i).setSlot(unusedSlots[i]));
		}
		for (int i : usedSlots) slotss.add(new ItemSlot(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).setSlot(i).name("&7"));
		builder.fillEmpty(new ItemSlot(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).name("&7"));
		builder.addAll(slotss);
		Interface inter = builder.build();
		if (page > 0) {
			inter.replaceSlot(new ItemSlot(XMaterial.CHEST_MINECART.parseItem()).name("&a&m<--- &aPrevious Page").setSlot(47).setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					open(page-1,p,slots,title,backbutton);
					return true;
				}
			}));
		}
		if (page + 1 < pages.totalPage()) {
			inter.replaceSlot(new ItemSlot(Material.MINECART).name("&aNext Page &m--->").setSlot(51).setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					open(page+1,p,slots,title,backbutton);
					return true;
				}
			}));
		}
		
		if (backbutton != null) inter.replaceSlot(backbutton.setSlot(45));
		inter.replaceSlot(new ItemSlot(Material.REDSTONE).name("&cReset Search Result").setSlot(53).setExecutor(new ItemEvent() {
			
			@Override
			public boolean run(InventoryClickEvent e) {
				open(page,p,original,title,backbutton);
				return true;
			}
		}));
		inter.open(p);
		PaginatorInfo info = new PaginatorInfo();
		info.cp = page;
		info.mp = pages.totalPage();
		info.current = inter;
		return info;
	}
	public static class PaginatorInfo {
		int cp;
		int mp;
		Interface current;
		public int getCurrentPage() {
			return cp;
		}
		public int getMaxPage() {
			return mp;
		}
		public Interface getInterface() {
			return current;
		}
	}
}
