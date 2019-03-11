package org.spookit.api.userinterface;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;

public final class InterfaceListener implements Listener {

	InterfaceListener() {
		//RegisteredListener listen = new RegisteredListener(this, (l,k)->run(k), EventPriority.LOWEST, UserInterface.getInstance(), false);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void run(InventoryClickEvent e) {
		final InventoryView view = e.getView();
		if (view != null) {
			final Inventory top = view.getTopInventory();
			if (top != null && top.getHolder() instanceof Interface) {
				Interface inter = (Interface) top.getHolder();
				e.setCancelled(!inter.interact);
				if (top == e.getClickedInventory()) {
					if (inter.global != null) new BukkitRunnable() {
						public void run() {
							inter.global.run(e);
						}
					}.runTask(UserInterface.getInstance());
					inter.signature.put("onClick", true);
					for (ItemSlot sl : new ArrayList<>(inter.slots)) {
						if (sl.getSlot() == e.getRawSlot()) {
							if (sl != null && sl.e != null) {
								new BukkitRunnable() {
									public void run() {
										e.setCancelled(sl.e.run(e));
									}
								}.runTask(UserInterface.getInstance());
							}
						}
					}
					inter.signature.remove("onClick");
				}
			}
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void run(InventoryDragEvent e) {
		Inventory top = e.getView().getTopInventory();
		Inventory bottom = e.getView().getBottomInventory();
		if (e.getInventory().getHolder() instanceof Interface) {
			e.setCancelled(!((Interface)e.getInventory().getHolder()).interact);
		}
		if (top != null && top.getHolder() instanceof Interface) {
			Interface inter = (Interface) top.getHolder();
			e.setCancelled(!inter.interact);
		} 
		if (bottom != null && bottom.getHolder() instanceof Interface) {
			Interface inter = (Interface) bottom.getHolder();
			e.setCancelled(!inter.interact);
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void run(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getHolder() instanceof Interface) {
			Interface inter = (Interface) inv.getHolder();
			if (inter.ce != null && !inter.signature.containsKey("onClick")) {
				new BukkitRunnable() {
					public void run() {
						inter.ce.onClose(e);
					}
				}.runTaskLater(UserInterface.getInstance(), 2L);
			}
		}
	}

}
