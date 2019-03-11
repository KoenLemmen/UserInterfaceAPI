package org.spookit.api.userinterface;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spookit.externalcore.Reflection;
import org.spookit.externalcore.Reflection.FieldAccessor;
import org.spookit.externalcore.TinyProtocol;

import io.netty.channel.Channel;

public class SignInListener extends TinyProtocol {

	public SignInListener() {
		super(Bukkit.getPluginManager().getPlugin("UserInterfaceAPI"));
	}
	
	FieldAccessor<String[]> sign = Reflection.getField("{nms}.PacketPlayInUpdateSign", String[].class, 0);
	
	public Object onPacketInAsync(Player sender,Channel channel,Object packet) {
		if (sign.hasField(packet)) {
			String[] s = sign.get(packet);
			if (SignListener.broadcast(sender,s)) {
				return null;
			}
		}
		return super.onPacketInAsync(sender, channel, packet);
	}
	
	public static interface SignListener {
		public static final ArrayList<SignListener> listeners = new ArrayList<>();
		static boolean broadcast(Player p ,String[]messages) {
			boolean ret = false;
			for (SignListener s : listeners) {
				ret = s.input(p, messages, ret);
			}
			return ret;
		}
		public boolean input(Player p,String[]messages,boolean isCancelled);
	}
	
}

