package org.spookit.api.userinterface;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.spookit.api.userinterface.events.CancellableEvent;
import org.spookit.api.userinterface.events.ItemCombineEvent;

public class InventoryListener implements Listener {

	@EventHandler(priority=EventPriority.HIGHEST)
	public void a(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player)e.getWhoClicked();
			ItemStack first = e.getCursor();
			ItemStack second = e.getCurrentItem();
			if (first != null && second != null) {
				e.setCancelled(CancellableEvent.callEvent(new ItemCombineEvent(p,first,second)));
			}
		}
	}
}
