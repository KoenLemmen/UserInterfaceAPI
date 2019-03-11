package org.spookit.api.userinterface.templates;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.spookit.api.userinterface.Interface;
import org.spookit.api.userinterface.InterfaceBuilder;
import org.spookit.api.userinterface.ItemEvent;
import org.spookit.api.userinterface.ItemSlot;
import org.spookit.api.userinterface.XMaterial;

public class ConfirmGUI {

	public static enum UserResult {
		YES(new ItemSlot(XMaterial.GREEN_CONCRETE.parseItem()).name("&aYes"),false),
		NO(new ItemSlot(XMaterial.RED_CONCRETE.parseItem()).name("&aNo"),true),
		CANCEL(new ItemSlot(XMaterial.RED_CONCRETE.parseItem()).name("&aCancel"),true),
		DENY(new ItemSlot(XMaterial.RED_CONCRETE.parseItem()).name("&aDeny"),true),
		ACCEPT(new ItemSlot(XMaterial.GREEN_CONCRETE.parseItem()).name("&aAccept"),false);
		
		ItemStack a;
		boolean b;
		UserResult(ItemStack item,boolean bad) {
			a = item;
			b = bad;
		}
		public boolean isBad() {
			return b;
		}
		ItemStack getItem() {
			return a;
		}
		public boolean is(UserResult user) {
			return name().equals(user.name());
		}
	}
	public static ConfirmGUI create(String question,UserResult... answers) {
		ConfirmGUI con = new ConfirmGUI(question,answers);
		return con;
	}
	String a;
	ConfirmGUI(String qu,UserResult[] ans) {
		a = qu;
		res = ans;
	}
	int length() {
		int total = 0;
		for (int i = 0;i<res.length;i+=9) total+=9;
		return total;
	}
	UserResult[] res;
	public static abstract class AnswerHandler {
		public abstract void onClick(Player p,UserResult res);
	}
	public void ask(Player p,AnswerHandler handler) {
		int goodLeft = 0;
		int badLeft = (res.length > 5 ? length() : 4);
		ArrayList<ItemSlot> slot = new ArrayList<>();
		for (UserResult result : res) {
			int sl = -1;
			if (result.isBad()) {
				sl = badLeft;
				badLeft--;
			} else {
				sl = goodLeft;
				goodLeft++;
			}
			slot.add(new ItemSlot(result.getItem()).setSlot(sl).setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					handler.onClick((Player)e.getWhoClicked(), result);
					return false;
				}
			}));
		}
		InterfaceBuilder interb = new InterfaceBuilder().title(a);
		if (res.length > 5) {
			interb.size(length());
		} else {
			interb.type(InventoryType.HOPPER);
		}
		interb.addAll(slot);
		interb.fillEmpty(new ItemSlot(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).name("&a"));
		Interface inter = interb.build();
		inter.open(p);
	}
}
