package org.spookit.api.userinterface.templates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.spookit.api.userinterface.Interface;
import org.spookit.api.userinterface.InterfaceBuilder;
import org.spookit.api.userinterface.ItemEvent;
import org.spookit.api.userinterface.ItemSlot;
import org.spookit.api.userinterface.Paginator;
import org.spookit.api.userinterface.XMaterial;

public class InterfacePaginator {

	public static InterfacePaginator create(String title,List<ItemSlot> item) {
		InterfacePaginator page = new InterfacePaginator();
		Paginator<ItemSlot> paginator = new Paginator<>(item, 45);
		int pageId = 1;
		List<InterfaceBuilder> builders = new ArrayList<>();
		for (List<ItemSlot> pagg : paginator.paginates()) {
			InterfaceBuilder builder = new InterfaceBuilder().title(title.replace("$page", pageId+""));
			builder.size(54);
			//builder.addAll(pagg);
			int lastIndex = 0;
			for (ItemSlot i : pagg) {
				builder.add(i.setSlot(lastIndex));
				lastIndex++;
			}
			pageId++;
			for (int i = 45;i<52;i++) {
				builder.add(new ItemSlot(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).setSlot(i).setExecutor(new ItemEvent() {
					
					@Override
					public boolean run(InventoryClickEvent e) {
						return true;
					}
				}));
			}
			
			builders.add(builder);
		}
		page.builders.addAll(builders);
		page.title = title;
		page.refreshInterfaces();
		return page;
	}
	String title;
	List<Interface> pages = new ArrayList<>();
	List<InterfaceBuilder> builders = new ArrayList<>();
	Interface empty;
	ItemStack nextPage;
	ItemStack prevPage;
	{
		nextPage = new ItemSlot(XMaterial.MINECART.parseItem()).name("&aNext").lore("&7Click here to go to the next page.");
		prevPage = new ItemSlot(XMaterial.CHEST_MINECART.parseItem()).name("&aPrevious").lore("&7Click here to go to the previous page.");
	}
	public void setPrevItem(ItemStack prev) {
		prevPage = prev;
		refreshInterfaces();
	}
	public void setNextItem(ItemStack next) {
		nextPage = next;
		refreshInterfaces();
	}
	public Interface getPage(int page) {
		if (page < 0 || page >= pages.size()) {
			return empty;
		}
		return pages.get(page);
	}
	public void refreshInterfaces() {
		int page = 0;
		pages.clear();
		for (InterfaceBuilder i : new ArrayList<>(builders)) {
			final int pag = page;
			if (page + 1< builders.size()) {
				i.add(new ItemSlot(nextPage).setSlot(53).setExecutor(new ItemEvent() {
					
					@Override
					public boolean run(InventoryClickEvent e) {
						Interface inter = pages.get(pag+1);
						if (inter != null) {
							inter.open((Player)e.getWhoClicked());
						}
						return true;
					}
				}));
			} else i.add(new ItemSlot(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).name("&7").setSlot(53));
			if (0 < page){ 
				i.add(new ItemSlot(prevPage).setSlot(52).setExecutor(new ItemEvent() {
					
					@Override
					public boolean run(InventoryClickEvent e) {
						Interface inter = pages.get(pag-1);
						if (inter != null) {
							inter.open((Player)e.getWhoClicked());
						}
						return true;
					}
				}));
			} else i.add(new ItemSlot(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).name("&7").setSlot(52));
			page++;
			pages.add(i.build());
		}
		InterfaceBuilder builder = new InterfaceBuilder().title(title).size(54);
		for (int i = 45;i<54;i++) builder.add(new ItemSlot(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).name("&7").setSlot(i));
		empty = builder.build();
	}
	public void open(Player p) {
		if (pages.isEmpty()) {
			if (empty != null) empty.open(p);
			return;
		}
		pages.get(0).open(p);
	}
	public void open(int i,Player p) {
		if (i >= pages.size()) {
			if (empty != null) empty.open(p);
			return;
		}
		pages.get(i).open(p);
	}
}
