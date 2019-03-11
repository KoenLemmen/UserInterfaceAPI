package org.spookit.api.userinterface.templates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.spookit.api.userinterface.Interface;
import org.spookit.api.userinterface.InterfaceBuilder;
import org.spookit.api.userinterface.InterfaceUtil;
import org.spookit.api.userinterface.ItemEvent;
import org.spookit.api.userinterface.ItemSlot;
import org.spookit.api.userinterface.Paginator;
import org.spookit.api.userinterface.ScrollerPaginator;

public class CategorizedGUIPaginator {

	public static CategorizedGUIPaginator open(String title,int categorypage,int subpage,Player p,Map<? extends ItemStack,? extends Collection<? extends ItemSlot>> categories,ItemSlot back) {
		CategorizedGUIPaginator paged = new CategorizedGUIPaginator();
		open(title,paged,categorypage,subpage,0,categories,back);
		paged.current.open(p);
		return paged;
	}
	public static int[] border = {
			0,1,2,3,4,5,6,7,8,
			9,              17,
			18,19,20,21,22,23,24,25,26,
			27,             35,
			36,             44,
			45,46,47,48,49,50,51,52,53
			};
	public static int[] categorynavigation = {9,17};
	public static int[] subnavigation = {47,51};
	public static int[] unusedCategorySlots = {10,11,12,13,14,15,16};
	public static int[] unusedSubSlots = {28,29,30,31,32,33,34,37,38,39,40,41,42,43};
	public static int backSlot = 49;
	private static void open(String title,CategorizedGUIPaginator paged,int a,int b,int scroll,Map<? extends ItemStack,? extends Collection<? extends ItemSlot>> categories,ItemSlot back) {
		ArrayList<ItemSlot> slots = new ArrayList<>();
		
		ArrayList<Entry<? extends ItemStack,? extends Collection<? extends ItemSlot>>> entries = new ArrayList<>(categories.entrySet());
		ScrollerPaginator<Entry<? extends ItemStack,? extends Collection<? extends ItemSlot>>> pager = new ScrollerPaginator<>(entries,unusedCategorySlots.length);
		ArrayList<Entry<? extends ItemStack,?extends Collection<? extends ItemSlot>>> entries2 = new ArrayList<>(pager.scroll(scroll));
		//slots.add(new ItemSlot(Material.REDSTONE).name("&dCurrent Category").setSlot(center));
		for (int i = 0;i<unusedCategorySlots.length && i < entries.size();i++) {
			ItemSlot item = new ItemSlot(entries2.get(i).getKey()).setSlot(unusedCategorySlots[i]);
			final int ac = i;
			item.setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					paged.openPage((Player)e.getWhoClicked(), ac, 0,scroll);
					return true;
				}
			});
			slots.add(item);
		}
		ArrayList<ItemSlot> items = a>=0 && a<entries.size() ? new ArrayList<>(entries.get(a).getValue()) : new ArrayList<>();
		Paginator<ItemSlot> subpager = new Paginator<>(items,unusedSubSlots.length);
		items = new ArrayList<>(subpager.paginate(b));
		for (int i = 0;i<unusedSubSlots.length && i<items.size();i++) {
			ItemSlot item = items.get(i).clone().setSlot(unusedSubSlots[i]);
			slots.add(item);
		}
		for (int i = 0;i<border.length;i++) {
			slots.add(InterfaceUtil.BLACK_BORDER.clone().setSlot(border[i]));
		} 
		Interface inter = new InterfaceBuilder().title(title).size(54).addAll(slots).fillEmpty(InterfaceUtil.GRAY_BORDER.clone()).build();
		if (back != null) {
			inter.replaceSlot(back.clone().setSlot(backSlot));
		}
		if (pager.isValidScroll(scroll+1)) {
			inter.replaceSlot(new ItemSlot(Material.ARROW).name("&aNext Category").setSlot(categorynavigation[1]).setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					paged.openPage((Player)e.getWhoClicked(), a, b,scroll+1);
					return true;
				}
			}));
		}
		if (pager.isValidScroll(scroll-1)) {
			inter.replaceSlot(new ItemSlot(Material.ARROW).name("&aPrevious Category").setSlot(categorynavigation[0]).setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					paged.openPage((Player)e.getWhoClicked(), a, b,scroll-1);
					return true;
				}
			}));
		}
		if (subpager.isValidPage(b+1)) {
			inter.replaceSlot(new ItemSlot(Material.ARROW).name("&aNext Page").setSlot(subnavigation[1]).setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					paged.openPage((Player)e.getWhoClicked(), a, b+1,scroll);
					return true;
				}
			}));
		}
		if (subpager.isValidPage(b-1)) {
			inter.replaceSlot(new ItemSlot(Material.ARROW).name("&aPrevious Page").setSlot(subnavigation[0]).setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					paged.openPage((Player)e.getWhoClicked(), a, b-1,scroll);
					return true;
				}
			}));
		}
		paged.current = inter;
		paged.currentCategory = a;
		paged.currentSubPage = b;
		paged.maxCategory = pager.getFullPage().size();
		paged.maxSubPage = subpager.totalPage();
		paged.f = new TriFunction<Integer, Integer,Integer, Interface>() {
			
			@Override
			public Interface apply(Integer arg0, Integer arg1,Integer arg2) {
				open(title,paged,arg0,arg1,arg2,categories,back);
				return paged.current;
			}
		};
	}
	
	interface TriFunction<A,B,C,D> {
		D apply(A arg0,B arg1,C arg2);
	}
	
	int currentCategory;
	int currentSubPage;
	int maxCategory;
	int maxSubPage;
	Interface current;
	TriFunction<Integer,Integer,Integer,Interface> f;
	public int getCurrentCategoryPage() {
		return currentCategory;
	}
	public int getCurrentSubPage() {
		return currentSubPage;
	}
	public int getMaxCategoryPage() {
		return maxCategory;
	}
	public int getMaxSubPage() {
		return maxSubPage;
	}
	public Interface getCurrentInterface() {
		return current;
	}
	public Interface getPage(int page,int cp,int scroll) {
		return f.apply(page, cp,scroll);
	}
	public void openPage(Player p,int page,int cp,int scroll) {
		getPage(page,cp,scroll).open(p);
	}
}
