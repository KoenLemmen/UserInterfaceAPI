package org.spookit.api.userinterface;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemSlot extends ItemStack {
	public static final ItemSlot BLACK_BORDER = new ItemSlot(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).name("&7");
	public static final Random RANDOM = new Random();
	public static ItemSlot randomBorderColor() {
		return new ItemSlot(XMaterial.fromString("STAINED_GLASS_PANE:"+RANDOM.nextInt(15)).parseItem());
	}
	int slot = -1;
	ItemEvent e;

	public ItemSlot() {
		super();
	}

	public ItemStack asItem() {
		return super.clone();
	}
	public ItemSlot(ItemStack item) {
		super(item instanceof ItemSlot ? ((ItemSlot)item).asItem() : item);
	}

	public ItemSlot(Material m) {
		super(m);
	}

	public ItemSlot(Material m, int amount) {
		super(m, amount);
	}

	public ItemSlot(Material m, int amount, short data) {
		super(m, amount, data);
	}
	public ItemSlot(Material m,int amount,int data) {
		this(m,amount,(short)data);
	}

	public ItemSlot setExecutor(ItemEvent e) {
		this.e = e;
		return this;
	}

	public ItemSlot setSlot(int slot) {
		this.slot = slot;
		return this;
	}

	public ItemSlot consume(Consumer<ItemSlot> slot) {
		slot.accept(this);
		return this;
	}
	
	public ItemSlot name(String name) {
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(name == null ? name : ChatColor.translateAlternateColorCodes('&', name));
		setItemMeta(meta);
		return this;
	}

	public ItemSlot addLore(String...strings) {
		meta(i->{
			List<String> lore = i.getLore();
			if (lore == null) {
				lore(strings);
			} else {
				lore.addAll(new Str(strings).colorize().asList());
				i.setLore(lore);
			}
		});
		return this;
	}
	public ItemSlot removeLore(String...strings) {
		meta(i->{
			List<String> lore = i.getLore();
			if (lore != null) lore.removeAll(new Str(strings).asList());
			i.setLore(lore);
		});
		return this;
	}
	@Deprecated
	public ItemSlot place(Location loc) {
		loc.getBlock().setType(getType());
		return this;
	}
	public ItemSlot removeLore(int...indexes) {
		meta(i->{
			List<String> lore = i.getLore();
			for (int a : indexes) {
				if (lore != null && a<lore.size()) {
					lore.set(a, null);
				}
			}
			if (lore != null) {
				for (String s : lore) {
					if (s != null) {
						addLore(s);
					}
				}
			}
			i.setLore(lore);
		});
		return this;
	}
	public ItemSlot lore(String... strings) {
		for (int i = 0; i < strings.length; i++)
			strings[i] = ChatColor.translateAlternateColorCodes('&', strings[i]);
		meta(i -> i.setLore(Arrays.asList(strings)));
		return this;
	}

	public ItemSlot bookmeta(Consumer<BookMeta> meta) {
		ItemMeta m = getItemMeta();
		if (m instanceof BookMeta) {
			meta.accept((BookMeta) m);
			setItemMeta(m);
		}
		return this;
	}

	public ItemSlot skullmeta(Consumer<SkullMeta> meta) {
		ItemMeta m = getItemMeta();
		if (m instanceof SkullMeta) {
			meta.accept((SkullMeta) m);
			setItemMeta(m);
		}
		return this;
	}

	public ItemSlot meta(Consumer<ItemMeta> meta) {
		ItemMeta me = getItemMeta();
		meta.accept(me);
		setItemMeta(me);
		return this;
	}

	public ItemSlot bannermeta(Consumer<BannerMeta> meta) {
		ItemMeta m = getItemMeta();
		if (m instanceof BannerMeta) {
			meta.accept((BannerMeta) m);
			setItemMeta(m);
		}
		return this;
	}

	public ItemSlot fireworkeffectmeta(Consumer<FireworkEffectMeta> meta) {
		ItemMeta m = getItemMeta();
		if (m instanceof FireworkEffectMeta) {
			meta.accept((FireworkEffectMeta) m);
			setItemMeta(m);
		}
		return this;
	}

	public ItemSlot fireworkmeta(Consumer<FireworkMeta> meta) {
		ItemMeta m = getItemMeta();
		if (m instanceof FireworkMeta) {
			meta.accept((FireworkMeta) m);
			setItemMeta(m);
		}
		return this;
	}

	public ItemSlot leatherarmormeta(Consumer<LeatherArmorMeta> meta) {
		ItemMeta m = getItemMeta();
		if (m instanceof LeatherArmorMeta) {
			meta.accept((LeatherArmorMeta) m);
			setItemMeta(m);
		}
		return this;
	}

	public ItemSlot mapmeta(Consumer<MapMeta> meta) {
		ItemMeta m = getItemMeta();
		if (m instanceof MapMeta) {
			meta.accept((MapMeta) m);
			setItemMeta(m);
		}
		return this;
	}

	public ItemSlot potionmeta(Consumer<PotionMeta> meta) {
		ItemMeta m = getItemMeta();
		if (m instanceof PotionMeta) {
			meta.accept((PotionMeta) m);
			setItemMeta(m);
		}
		return this;
	}

	public ItemSlot repairable(Consumer<Repairable> meta) {
		ItemMeta m = getItemMeta();
		if (m instanceof Repairable) {
			meta.accept((Repairable) m);
			setItemMeta(m);
		}
		return this;
	}

	public int getSlot() {
		return slot;
	}

	public ItemSlot clone() {
		ItemSlot slot = new ItemSlot(super.clone());
		slot.slot = this.slot;
		slot.e = e;
		return slot;
	}
	public ItemSlot drop(Location l) {
		l.getWorld().dropItem(l,this);
		return this;
	}
	
	public ItemStack dropNaturally(Location l) {
		l.getWorld().dropItemNaturally(l, this);
		return this;
	}
	public void onPut(Inventory inv) {
		if (slot == -1) {
			slot = inv.firstEmpty();
		}
	}
}
