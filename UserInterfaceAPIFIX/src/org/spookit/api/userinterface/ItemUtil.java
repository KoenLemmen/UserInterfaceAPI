package org.spookit.api.userinterface;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {

	public boolean isVisibleItem(Material m) {
		if (m == Material.AIR) return false;
		try {
			return isItemReflection3(new ItemStack(m));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	static Class<?> cb(String a) throws Exception {
		String pack = Bukkit.getServer().getClass().getPackage().getName();
		return Class.forName(pack+"."+a);
	}
	static boolean isItemReflection3(ItemStack item) throws Exception {
    	return cb("util.CraftMagicNumbers").getDeclaredMethod("getItem",Material.class).invoke(null, item.getType()) != null;
    }
}
