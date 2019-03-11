package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class Region {

	World world;
	int minx;
	int miny;
	int minz;
	int maxx;
	int maxy;
	int maxz;
	
	public Region(Location pos1,Location pos2) {
		if (pos1.getWorld() != pos2.getWorld()) throw new IllegalArgumentException("different world");
		world = pos1.getWorld();
		minx = Math.min(pos1.getBlockX(), pos2.getBlockX());
		miny = Math.min(pos1.getBlockY(), pos2.getBlockY());
		minz = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
		maxx = Math.max(pos1.getBlockX(), pos2.getBlockX());
		maxy = Math.max(pos1.getBlockY(), pos2.getBlockY());
		maxz = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
	}
	
	public List<Location> getBoundingBox() {
		ArrayList<Location> locs = new ArrayList<>();
		for (int x = minx;x<maxx;x++) {
			for (int y = miny;y<maxy;y++) {
				for (int z = minz;z<maxz;z++) {
					locs.add(new Location(world,x,y,z));
				}
			}
		}
		return locs;
	}
	
	public boolean isInside(Location loc) {
		if (loc.getWorld() != world) return false;
		return
				minx >= loc.getBlockX() && loc.getBlockX() <= maxx &&
				miny >= loc.getBlockY() && loc.getBlockY() <= maxy &&
				minz >= loc.getBlockZ() && loc.getBlockZ() <= maxz;
	}
	
	
	public void setBlock(Material mat) {
		for (Location loc : getBoundingBox()) {
			new BukkitRunnable() {
				public void run() {
					new BukkitRunnable() {
						public void run() {
							loc.getBlock().setType(mat);
						}
					}.runTask(UserInterface.getInstance());
				}
			}.runTaskAsynchronously(UserInterface.getInstance());
		}
	}
	
	public void replace(Material mat,Material to) {
		for (Location loc : getBoundingBox()) {
			new BukkitRunnable() {
				public void run() {
					new BukkitRunnable() {
						public void run() {
							if (loc.getBlock().getType() == mat) {
								loc.getBlock().setType(to);
							}
						}
					}.runTask(UserInterface.getInstance());
				}
			}.runTaskAsynchronously(UserInterface.getInstance());
		}
	}
	
}