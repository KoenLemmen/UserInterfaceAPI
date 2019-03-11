package org.spookit.api.userinterface;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spookit.api.userinterface.PlayerInput.PlayerInputAdapter;
import org.spookit.api.userinterface.PlayerInput.PlayerInputType;

public class UserInterface extends JavaPlugin {

	static UserInterface instance;

	public static UserInterface getInstance() {
		return instance;
	}

	SignInListener tp;
	public void onEnable() {
		instance = this;
		tp = new SignInListener();
		getServer().getPluginManager().registerEvents(new InterfaceListener(), this);
		getServer().getPluginManager().registerEvents(new Asker(), this);
		//getServer().getPluginManager().registerEvents(new IdentifiedItemListener(), this);
		InventoryGrabber.register(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String la, String[] args) {
		String prefix = c("&8[&aUI&8] &7");
		try {
			cmd(sender, args);
		} catch (Throwable t) {
			sender.sendMessage(prefix+"Error! "+t);
		}
		return true;
	}

	String c(String a) {
		return ChatColor.translateAlternateColorCodes('&', a);
	}

	static enum A {
		ANVIL,CHAT,EVENT,SIGN;
		static A a(String name) {
			for (A a : values()) if (a.name().equalsIgnoreCase(name)) return a;
			return null;
		}
		PlayerInputType toType() {
			switch (this) {
			case ANVIL: return PlayerInput.ANVIL;
			case CHAT: return PlayerInput.CHAT;
			case EVENT: return PlayerInput.PLAYER_EVENT;
			case SIGN: return PlayerInput.SIGN;
			}
			return null;
		}
	}
	void cmd(CommandSender sender, String[] args) {
		String prefix = c("&8[&aUI&8] &7");
		if (!sender.hasPermission("userinterfaceapi.use")) {
			sender.sendMessage(prefix+"You dont have permission to do this");
			return;
		}
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(new String[] { c("&5&m-----------------------------------------------"),
						Center.center("&d&lUSER INTERFACE API"), c("&f/ui help &7- &fShow this page"),
						c("&f/ui ask &7- &fRequest for player input"),
						c("&f/its &7- &fModify itemstack"),
						c("&5&m-----------------------------------------------"), });
				return;
			}
			if (args[0].equalsIgnoreCase("ask")) {
				if (args.length > 3) {
					Player pla = Bukkit.getPlayerExact(args[1]);
					if (pla == null) {
						sender.sendMessage(prefix+"Player unknown :(");
						return;
					}
					A a = A.a(args[2]);
					if (a == null) {
						sender.sendMessage(prefix+"Invalid type of player input! Only ANVIL, CHAT, EVENT, and SIGN");
						return;
					}
					String question = new String();
					for (int i = 3;i<args.length;i++) question+=args[i];
					PlayerInput input = new PlayerInput(a.toType(), new PlayerInputAdapter() {
						
						@SuppressWarnings("unchecked")
						@Override
						public void input(Player p, Object o) {
							if (o instanceof Variable[]) {
								Variable<String>[] vars = (Variable<String>[])o;
								if (vars.length > 0) o = vars[vars.length - 1];
							}
							sender.sendMessage(prefix+"Player Input Result: "+o);
						}
						
						@Override
						public boolean accept(PlayerInputType type) {
							return true;
						}
					}, question);
					input.ask(pla);
					sender.sendMessage(prefix+"Player Input requested! Please wait for the result!");
					return;
				}
				sender.sendMessage(prefix+"Request for player input. Usage: /ui ask <Player> <ANVIL|CHAT|EVENT|SIGN> <Question>");
				return;
			}
			sender.sendMessage(prefix + "Syntax error! Do /ui help for help");
			return;
		}
		sender.sendMessage(prefix + "UserInterfaceAPI by BlueObsidian");
	}
}
