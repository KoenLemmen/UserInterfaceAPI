package org.spookit.api.userinterface;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Asker implements Listener {

	static Map<Player,AnsHan> handlers = new HashMap<>();
	@EventHandler(priority=EventPriority.HIGHEST)
	public void dispatch(AsyncPlayerChatEvent e) {
		AnsHan handler = handlers.get(e.getPlayer());
		if (handler != null) {
			e.setCancelled(true);
			if (handler.accept(e)) {
				handlers.remove(e.getPlayer());
			} else {
				e.getPlayer().sendMessage(new Str(handler.msgs).colorize().center().toArray());
			}
		}
	}
	@EventHandler(priority=EventPriority.HIGH)
	public void remove(PlayerQuitEvent e) {
		handlers.remove(e.getPlayer());
	}
	public static interface AnswerHandler {
		public abstract boolean accept(AsyncPlayerChatEvent e);
	}
	public static void ask(Player p,AnswerHandler handler,String...messages) {
		p.closeInventory();
		p.sendMessage(new Str(messages).colorize().center().toArray());
		handlers.put(p, new AnsHan() {
			{
				msgs = messages; 
			}
			@Override
			public boolean accept(AsyncPlayerChatEvent e) {
				return handler.accept(e);
			}
		});
	}
	private static abstract class AnsHan implements AnswerHandler {
		public String[] msgs;
	}
	public static interface Confirmer {
		public abstract void onClick(boolean confirmed);
	}
	public static void ask(Player p,Confirmer con,String msg) {
		new InterfaceBuilder().title("&a"+msg).type(InventoryType.HOPPER).add(new ItemSlot(XMaterial.GREEN_CONCRETE.parseItem()).name("&a&lYES").setSlot(0).setExecutor(new ItemEvent() {
			
			@Override
			public boolean run(InventoryClickEvent e) {
				con.onClick(true);
				return true;
			}
		})).add(new ItemSlot(XMaterial.RED_CONCRETE.parseItem()).setSlot(4).name("&c&lNO").setExecutor(new ItemEvent() {
			
			@Override
			public boolean run(InventoryClickEvent e) {
				con.onClick(false);
				return true;
			}
		})).fillEmpty(new ItemSlot(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).name("&7")).build().open(p);
	}
	public static void ask(Player p,Consumer<Str> str,String question,String defaultAnswer) {
		Interface inte = new InterfaceBuilder().title("&1"+question).type(InventoryType.ANVIL).add(new ItemSlot(Material.NAME_TAG).name(defaultAnswer)).closeEvent(new CloseEvent() {
			
			@Override
			public void onClose(InventoryCloseEvent e) {
				Interface inter = (Interface)e.getInventory().getHolder();
				ItemSlot at = inter.get(2);
				if (at == null && p.isOnline()) {
					ask(p,str,question,defaultAnswer);
				} else {
					str.accept(new Str(at == null? defaultAnswer : at.getItemMeta().getDisplayName()));
				}
			}
		}).clickEvent(new ItemEvent() {
			
			@Override
			public boolean run(InventoryClickEvent e) {
				Interface inter = (Interface)e.getInventory().getHolder();
				ItemSlot at = inter.get(2);
				if (at == null && p.isOnline()) {
					ask(p,str,question,defaultAnswer);
				} else {
					str.accept(new Str(at == null? defaultAnswer : at.getItemMeta().getDisplayName()));
				}
				return true;
			}
		}).build();
		inte.open(p);
	}
}
