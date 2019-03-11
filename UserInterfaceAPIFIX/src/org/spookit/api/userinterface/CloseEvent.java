package org.spookit.api.userinterface;

import org.bukkit.event.inventory.InventoryCloseEvent;

public interface CloseEvent {

	public abstract void onClose(InventoryCloseEvent e);
}
