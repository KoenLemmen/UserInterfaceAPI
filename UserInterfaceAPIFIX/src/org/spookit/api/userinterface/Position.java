package org.spookit.api.userinterface;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Position extends Location {

	public Position(String world,double x,double y,double z) {
		super(getWorld(world), x, y, z);
	}
	
	public Position(String world,double x,double y,double z,float yaw,float pitch) {
		super(getWorld(world), x, y, z, yaw,pitch);
	}
	
	public Position(String world,String x,String y,String z,String yaw,String pitch) {
		super(getWorld(world),Double.parseDouble(x),Double.parseDouble(y),Double.parseDouble(z),Float.parseFloat(yaw),Float.parseFloat(pitch));
	}
	
	public String toString() {
		return getWorld().getName()+";"+getX()+";"+getY()+";"+getZ()+";"+getYaw()+";"+getPitch();
	}
	
	public static Position valueOf(String s) {
		String[] splitted = s.split(";");
		return new Position(splitted[0],splitted[1],splitted[2],splitted[3],splitted[4],splitted[5]);
	}
	
	public static World getWorld(String name) {
		for (World w : new ArrayList<>(Bukkit.getWorlds())) {
			if (w.getName().equals(name)) return w;
		}
		return null;
	}
}
