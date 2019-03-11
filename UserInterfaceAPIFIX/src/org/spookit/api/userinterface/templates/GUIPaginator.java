package org.spookit.api.userinterface.templates;

import java.util.List;

import org.bukkit.entity.Player;
import org.spookit.api.userinterface.Interface;
import org.spookit.api.userinterface.ItemSlot;

public class GUIPaginator {

	public static GUIPaginator create(String title, List<ItemSlot> slot) {
		
		GUIPaginator pagi = new GUIPaginator();
		pagi.paginator = InterfacePaginator.create(title, slot);
		return pagi;
	}
	
	

	public void open(int page,Player p) {
		paginator.open(page, p);
	}
	public Interface getPage(int page) {
		return paginator.getPage(page);
	}
	GUIPaginator() {}
	InterfacePaginator paginator;
}
