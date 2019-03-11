package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class InventoryGrabber implements InventoryHolder {

	Inventory inv;
	Consumer<InventoryCloseEvent> event;
	ArrayList<HumanEntity> open = new ArrayList<>();
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
	public static void create(Player p,Inventory inv,Consumer<InventoryCloseEvent> event) {
		InventoryGrabber dispatcher = new InventoryGrabber();
		dispatcher.event = event;
		if (inv.getType() ==null || inv.getType() == InventoryType.CHEST) {
			dispatcher.inv = Bukkit.createInventory(dispatcher, inv.getSize(),inv.getTitle());
		} else {
			dispatcher.inv = Bukkit.createInventory(dispatcher, inv.getType(),inv.getTitle());
		}
		dispatcher.inv.setContents(inv.getContents());
		p.closeInventory();
		if (dispatcher.open.contains(p)) return;
		dispatcher.open.add(p);
		p.openInventory(dispatcher.inv);
	}
	protected static void register(Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(new Listener() {
			ArrayList<InventoryGrabber> cache = new ArrayList<>();
			@EventHandler(priority=EventPriority.HIGHEST)
			public void a(InventoryClickEvent e) {
				if (e.getInventory().getHolder() instanceof InventoryGrabber) {
					e.setCancelled(false);
				}
			}
			@EventHandler(priority=EventPriority.HIGHEST)
			public void a(InventoryDragEvent e) {
				if (e.getInventory().getHolder() instanceof InventoryGrabber) {
					e.setCancelled(false);
				}
			}
			
			@EventHandler(priority=EventPriority.LOW)
			public void a(InventoryOpenEvent e) {
				if (cache.contains(e.getInventory().getHolder())) {
					e.setCancelled(true);
				}
			}
			@EventHandler(priority=EventPriority.LOW)
			public void a(InventoryCloseEvent e) {
				Inventory open = e.getInventory();
				if (open != null && open.getHolder() instanceof InventoryGrabber) {
					//e.getPlayer().closeInventory();
					InventoryGrabber dispatcher = (InventoryGrabber)open.getHolder();
					cache.add(dispatcher);
					if (dispatcher.open.contains(e.getPlayer())) {
						new BukkitRunnable() {
							public void run() {
								dispatcher.event.accept(e);
							}
						}.runTaskLater(plugin,3L);
						dispatcher.open.remove(e.getPlayer());
						cache.remove(dispatcher);
					}
				}
			}
		}, plugin);
	}

}
