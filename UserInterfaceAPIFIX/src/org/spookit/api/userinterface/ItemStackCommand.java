package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.spookit.api.userinterface.Asker.AnswerHandler;
import org.spookit.api.userinterface.templates.BorderedGUIPaginator;
@SuppressWarnings("deprecation")
public class ItemStackCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command p1, String p2, String[] args) {
		String prefix = Str.c("&8[&6ItemStack&8] &7");
		if (!sender.hasPermission("userinterfaceapi.modifyitemstack")) {
			sender.sendMessage(prefix+"You don't have permission to do this!");
			return true;
		}
		Player p = (Player)sender;
		int page = 0;
		ItemSlot item = new ItemSlot(p.getItemInHand());
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("name")) {
				if (args.length > 1) {
					String appender = args[1];
					for (int i = 2;i<args.length;i++) {
						appender+=" "+args[i];
					}
					p.setItemInHand((item).name(appender));
					sender.sendMessage(prefix+"Renamed to "+appender);
					return true;
				}
				sender.sendMessage(prefix+"Rename an item. Usage: /its name <displayname>");
				return true;
			}
			if (args[0].equalsIgnoreCase("addlore")) {
				if (args.length > 1) {
					String appender = args[1];
					for (int i = 2;i<args.length;i++) {
						appender+=" "+args[i];
					}
					p.setItemInHand((item).addLore(appender));
					sender.sendMessage(prefix+"Lore '"+appender+"' added");
					return true;
				}
				sender.sendMessage(prefix+"Add a lore. Usage: /its addlore <lore>");
				return true;
			}
			if (args[0].equalsIgnoreCase("remlore")) {
				if (args.length > 1) {
					String appender = args[1];
					for (int i = 2;i<args.length;i++) {
						appender+=" "+args[i];
					}
					int index = 0;
					try {
						index = Integer.parseInt(args[1]);
						appender = null;
					} catch (Throwable t) {
					}
					if (appender != null) {
						p.setItemInHand((item).removeLore(appender));
					} else {
						p.setItemInHand(item.removeLore(index));
					}
					if (appender == null) appender = item.getItemMeta().hasLore() ? index > 0 ? index < item.getItemMeta().getLore().size() ? item.getItemMeta().getLore().get(index) : "" : "" : ""; 
					sender.sendMessage(prefix+"Removed lore : "+appender);
					return true;
				}
				ArrayList<ItemSlot> slots = new ArrayList<>();
				int index = 0;
				for (String lore : (item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : new ArrayList<String>())) {
					final int id = index;
					slots.add(new ItemSlot(Material.NAME_TAG).name(lore).addLore("&7","&7Click here to remove").setExecutor(new ItemEvent() {
						
						@Override
						public boolean run(InventoryClickEvent e) {
							item.removeLore(id);
							return true;
						}
					}));
					index++;
				}
				BorderedGUIPaginator.open(0, p, slots, "&1Remove Lore", new ItemSlot(Material.ARROW).name("&cExit").setExecutor(new ItemEvent() {
					
					@Override
					public boolean run(InventoryClickEvent e) {
						e.getWhoClicked().closeInventory();
						return true;
					}
				}));
				return true;
			}
			if (args[0].equalsIgnoreCase("clearlore")) {
				item.meta(meta->meta.setLore(null));
				sender.sendMessage(prefix+"Cleared!");
				return true;
			}
			if (args[0].equalsIgnoreCase("enchant")) {
				ArrayList<ItemSlot> slots = new ArrayList<>();
				Variable<Runnable> run = new Variable<>();
				for (Enchantment ench : Enchantment.values()) {
					slots.add(new ItemSlot(Material.ENCHANTED_BOOK).name("&a"+ench.getName()).lore("&7Click here to add this enchantment").setExecutor(new ItemEvent() {
						
						@Override
						public boolean run(InventoryClickEvent e) {
							Asker.ask(p, new AnswerHandler() {
								
								@Override
								public boolean accept(AsyncPlayerChatEvent e) {
									if (e.getMessage().equalsIgnoreCase("cancel")) {
										p.sendMessage(prefix+"Cancelled!");
										return true;
									}
									try {
										int level = Integer.parseInt(e.getMessage());
										p.setItemInHand(item.meta(meta->meta.addEnchant(ench, level, true)));
										return true;
									} catch (Throwable t) {
									}
									return false;
								}
							}, Center.titledCenter(null),"&a&lSET ENCHANTMENT LEVEL","Type in your chat box the enchantment level","&eType 'cancel' to cancel this action",Center.titledCenter(null));
							return true;
						}
					}));
				}
				final int pa = page;
				run.set(new Runnable() {
					
					@Override
					public void run() {
						BorderedGUIPaginator.open(pa, p, slots, "&1Add Enchantment", new ItemSlot(Material.ARROW).name("&cExit").setExecutor(new ItemEvent() {
							
							@Override
							public boolean run(InventoryClickEvent e) {
								p.closeInventory();
								return true;
							}
						}));
					}
				});
				run.get().run();
				return true;
			}
			if (args[0].equalsIgnoreCase("unenchant")) {
				ArrayList<ItemSlot> slots = new ArrayList<>();
				for (Entry<Enchantment,Integer> enchs : item.getEnchantments().entrySet()) {
					slots.add(new ItemSlot(Material.ENCHANTED_BOOK).name("&a"+enchs.getKey().getName()).addLore("&7Level: &b"+enchs.getValue(),"&7","&7Click here to remove this enchantment").setExecutor(new ItemEvent() {
						
						@Override
						public boolean run(InventoryClickEvent e) {
							p.setItemInHand(item.meta(meta->{
								meta.removeEnchant(enchs.getKey());
							}));
							return true;
						}
					}));
				}
				BorderedGUIPaginator.open(0, p, slots, "&1Remove Enchantment", new ItemSlot(Material.ARROW).name("&cExit").setExecutor(new ItemEvent() {
					
					@Override
					public boolean run(InventoryClickEvent e) {
						p.closeInventory();
						return true;
					}
				}));
				return true;
			}
			if (args[0].equalsIgnoreCase("data")) {
				if (args.length > 1) {
					int dura = 0;
					DyeColor dc;
					try {
						dura = Integer.parseInt(args[1]);
						dc = null;
					} catch (Throwable t) {
						dc = DyeColor.valueOf(args[1].toUpperCase());
					}
					final DyeColor color = dc;
					final int dur = dura;
					
					p.setItemInHand(item.consume(slot->{
						if (color == null) {
							slot.setDurability((short)dur);
						} else {
							MaterialData data = slot.getData();
							if (data instanceof Colorable) {
								((Colorable) data).setColor(color);
							} else {
								data.setData(color.getWoolData());
								sender.sendMessage(prefix+"That item data is not Colorable! It will use the databyte instead! ("+color.getWoolData()+")");
							}
						}
					}));
					sender.sendMessage(prefix+"Data setted to "+item.getData().getData());
					return true;
				}
				sender.sendMessage(prefix+"Set the item data. Usage: /its data <durability|Color>. Example:");
				sender.sendMessage(Str.c("&7/its data 14 (Set data using bytedata/durability)"));
				sender.sendMessage(Str.c("&7/its data WHITE (Set data using Color)"));
				return true;
			}
			if (args[0].equalsIgnoreCase("flags")) {
				flags(item,p);
				return true;
			}
			try {
				page = Integer.parseInt(args[0])-1;
			} catch (Throwable t) {
				sender.sendMessage(prefix+"Input a number! "+t);
				return true;
			}
		}
		Paginator<String> pages = new Paginator<>(Arrays.asList(
				"name:Rename an item",
				"addlore:Add lore",
				"setlore:Set lore",
				"remlore:Remove lore",
				"clearlore:Clear lore",
				"enchant:Add enchantment",
				"unenchant:Remove enchantment",
				"data:Set data",
				"amount:Set amount",
				"flags:Manage item flags",
				"unbreakable:Toggle unbreakable item",
				"addtag:Add NBT tag",
				"remtag:Remove NBT tag",
				"attr:Manage attributes"
				),9);
		sender.sendMessage(Center.titledCenter("&a&lITEMSTACK UTILITIES"));
		for (String pa : pages.paginate(page)) {
			sender.sendMessage(Str.c("&f"+pa.split(":")[0]+" &8- &7"+pa.split(":")[1]));
		}
		sender.sendMessage(Center.titledCenter(null));
		return true;
 	}

	public void flags(ItemSlot item,Player p) {
		ArrayList<ItemSlot> slots = new ArrayList<>();
		for (ItemFlag flag : ItemFlag.values()) {
			slots.add(new ItemSlot(XMaterial.INK_SAC.parseItem()).name(item.getItemMeta().hasItemFlag(flag) ? "&a"+flag.name() : "&c"+flag.name()).lore(item.getItemMeta().hasItemFlag(flag)?"&7Click here to use this FLAG" : "&7Click here to dispose this FLAG").setExecutor(new ItemEvent() {
				
				@Override
				public boolean run(InventoryClickEvent e) {
					item.meta(meta->{
						if (meta.hasItemFlag(flag)) {
							meta.removeItemFlags(flag);
						} else {
							meta.addItemFlags(flag);
						}
					});
					p.setItemInHand(item);
					flags(item,p);
					return true;
				}
			}));
		}
		BorderedGUIPaginator.open(0, p, slots, "&1ItemFlags Manager", null);
	}
}
