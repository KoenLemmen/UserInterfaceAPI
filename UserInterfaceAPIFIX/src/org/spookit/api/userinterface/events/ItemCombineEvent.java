package org.spookit.api.userinterface.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCombineEvent extends CancellableEvent {

	Player pl;
	ItemStack a,b;
	public ItemCombineEvent(Player p,ItemStack item1,ItemStack item2) {
		pl = p; a = item1; b = item2;
	}
	public Player getPlayer() {
		return pl;
	}
	public ItemStack getFirstItem() {
		return a;
	}
	public ItemStack getSecondItem() {
		return b;
	}
}
