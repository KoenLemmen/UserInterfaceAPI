package org.spookit.api.userinterface;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ItemEvent {

	public abstract boolean run(InventoryClickEvent e);

}
