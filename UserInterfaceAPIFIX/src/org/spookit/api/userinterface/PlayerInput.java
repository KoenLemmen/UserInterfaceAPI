package org.spookit.api.userinterface;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.spookit.api.userinterface.SignInListener.SignListener;

public class PlayerInput {

	public static PlayerInputType CHAT;
	public static PlayerInputType SIGN;
	public static PlayerInputType PLAYER_EVENT;
	public static PlayerInputType COMMAND;
	public static PlayerInputType ANVIL;

	static {
		SIGN = new PlayerInputType() {

			@Override
			public void fire(Player p) {
				Location l = p.getLocation().clone();
				while (l.getBlock().getType().equals(Material.AIR) && l.getY() > 0) {
					l.subtract(0, 1, 0);
				}
				l.add(0, 1, 0);
				Block operator = l.getBlock();
				final Material type = operator.getType();
				operator.setType(XMaterial.SIGN.parseMaterial());
				operator.getState().setType(XMaterial.SIGN.parseMaterial());
				Sign sign = (Sign) operator.getState();
				open(p, sign);
				operator.setType(type);
				PlayerInputType pit = this;
				SignListener listen;
				SignListener.listeners.add(listen = new SignListener() {
					
					@Override
					public boolean input(Player pl, String[] messages, boolean isCancelled) {
						if (pl == p) {
							pit.input(p, messages);
							return true;
						}
						return isCancelled;
					}
				});
				SignListener.listeners.remove(listen);
				//Bukkit.getPluginManager().registerEvents(a, UserInterface.getInstance());
			}

			public boolean showMessage() {
				return false;
			}
		};
		CHAT = new PlayerInputType() {

			@Override
			public void fire(Player p) {
				Listener hook = null;
				hook = new Listener() {
					@EventHandler(priority = EventPriority.HIGHEST)
					public void run(AsyncPlayerChatEvent e) {
						if (e.getPlayer() != p)
							return;
						e.setCancelled(true);
						input(e.getPlayer(), e.getMessage());
						HandlerList.unregisterAll(this);
					}
				};
				Bukkit.getPluginManager().registerEvents(hook, UserInterface.getInstance());
			}

		};
		PLAYER_EVENT = new PlayerInputType() {

			@Override
			public void fire(Player p) {
				final SimpleRegisteredListener listen2 = new SimpleRegisteredListener(new Listener() {
				}, new SimpleEventExecutor() {

					@Override
					public void execute(Listener paramListener, Event paramEvent) throws EventException {
						if (paramEvent instanceof PlayerEvent) {
							PlayerEvent e = (PlayerEvent) paramEvent;
							if (e.getPlayer() != p)
								return;
							input(e.getPlayer(), e);
							if (this.listen != null)
								for (HandlerList hl : HandlerList.getHandlerLists()) {
									hl.unregister(this.listen);
								}
						}
					}
				}, EventPriority.LOWEST, UserInterface.getInstance(), false);
				for (HandlerList hl : HandlerList.getHandlerLists()) {
					hl.register(listen2);
				}
			}
		};
		COMMAND = new PlayerInputType() {

			@Override
			public void fire(Player p) {
				Listener listen = new Listener() {
					@EventHandler(priority = EventPriority.LOWEST)
					public void run(PlayerCommandPreprocessEvent e) {
						if (e.getPlayer() != p)
							return;
						HandlerList.unregisterAll(this);
						input(e.getPlayer(), e.getMessage());
					}
				};
				Bukkit.getPluginManager().registerEvents(listen, UserInterface.getInstance());
			}

		};
	}

	static final void open(Player player, Sign sign) {
		for (int i = 0; i < 4; ++i)
			sign.setLine(i, ChatColor.translateAlternateColorCodes('&', sign.getLine(i)));
		sign.update();

		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object connection = handle.getClass().getField("playerConnection").get(handle);

			Field tileField = sign.getClass().getDeclaredField("sign");
			tileField.setAccessible(true);
			Object tileSign = tileField.get(sign);

			Field editable = tileSign.getClass().getDeclaredField("isEditable");
			editable.setAccessible(true);
			editable.set(tileSign, true);

			Field handler = tileSign.getClass().getDeclaredField("h");
			handler.setAccessible(true);
			handler.set(tileSign, handle);

			Object position = getNMSClass("BlockPosition$PooledBlockPosition")
					.getMethod("d", double.class, double.class, double.class)
					.invoke(null, sign.getX(), sign.getY(), sign.getZ());

			Object packet = getNMSClass("PacketPlayOutOpenSignEditor").getConstructor(getNMSClass("BlockPosition"))
					.newInstance(position);

			connection.getClass().getDeclaredMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public static String version() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}

	static Class<?> getNMSClass(String clazz) {
		try {
			return Class.forName("net.minecraft.server." + version() + "." + clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	static class SimpleRegisteredListener extends RegisteredListener {
		public SimpleRegisteredListener(Listener listener, SimpleEventExecutor executor, EventPriority priority,
				Plugin plugin, boolean ignoreCancelled) {
			super(listener, executor, priority, plugin, ignoreCancelled);
			executor.listen = this;
		}
	}

	static abstract class SimpleEventExecutor implements EventExecutor {
		SimpleRegisteredListener listen;
	}

	public abstract static class PlayerInputAdapter implements Cloneable {
		public final PlayerInputAdapter clone() {
			try {
				return (PlayerInputAdapter) super.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}

		public abstract boolean accept(PlayerInputType type);

		PlayerInputType last;
		Player lastPlayer;

		final void input0(PlayerInputType t, Player p, Object o) {
			last = t;
			lastPlayer = p;
			input(p, o);
			return;
		}

		public abstract void input(Player p, Object o);

		protected final void reask() {
			if (last != null && lastPlayer != null)
				last.fire(lastPlayer);
		}
	}

	public static abstract class PlayerInputAnvil extends PlayerInputAdapter implements Cloneable {
		public final boolean accept(PlayerInputType a) {
			return a == ANVIL;
		}

		@SuppressWarnings("unchecked")
		public final void input(Player p, Object o) {
			if (o instanceof Variable[]) {
				Variable<String>[] vars = (Variable<String>[]) o;
				String response = vars[1].get();
				vars[0].set(input(p, response));
			}
		}

		public abstract String input(Player p, String response);
	}

	public static abstract class PlayerInputChat extends PlayerInputAdapter implements Cloneable {
		public final boolean accept(PlayerInputType a) {
			return true;
		}

		public final void input(Player p, Object a) {
			input(p, a.toString());
		}

		public abstract void input(Player p, String chat);
	}

	public static abstract class PlayerInputSign extends PlayerInputAdapter implements Cloneable {
		public final boolean accept(PlayerInputType t) {
			return t == SIGN;
		}

		public final void input(Player p, Object a) {
			if (a instanceof String[]) {
				input(p, (String[]) a);
			}
		}

		public abstract void input(Player p, String[] lines);
	}

	public static abstract class PlayerInputPlayerEvent extends PlayerInputAdapter implements Cloneable {
		public final boolean accept(PlayerInputType t) {
			return t == PLAYER_EVENT;
		}

		public final void input(Player p, Object a) {
			if (a instanceof PlayerEvent) {
				input(p, (PlayerEvent) a);
			}
		}

		public abstract void input(Player p, PlayerEvent e);
	}

	public static abstract class PlayerInputCommand extends PlayerInputAdapter implements Cloneable {
		public final boolean accept(PlayerInputType t) {
			return t == COMMAND;
		}

		public final void input(Player p, Object v) {
			if (v instanceof String) {
				if (v.toString().split(" ").length > 0) {
					v = v.toString().split(" ")[0];
				}
				Command cm = Bukkit.getPluginCommand(v.toString());
				if (cm != null) {
					input(p, cm,v.toString());
				}
			}
		}

		public abstract void input(Player p, Command cmd,String label);
	}

	public static abstract class PlayerInputType implements Cloneable {
		String[] messages;

		public final PlayerInputType clone() {
			try {
				return (PlayerInputType) super.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}

		PlayerInputAdapter adapter;

		public abstract void fire(Player p);

		protected final void input(Player p, Object o) {
			if (adapter != null && adapter.accept(this)) {
				adapter.input0(this, p, o);
			}
		}

		public boolean showMessage() {
			return true;
		}
	}

	String[] msg;
	PlayerInputType t;
	PlayerInputAdapter a;

	public PlayerInput(String[] messages, PlayerInputType type, PlayerInputAdapter adapter) {
		msg = messages.clone();
		t = type.clone();
		t.messages = messages;
		t.adapter = adapter.clone();
		a = adapter.clone();
	}

	public PlayerInput(PlayerInputType type, PlayerInputAdapter adapter, String... messages) {
		this(messages, type, adapter);
	}

	public void ask(Player p) {
		if (p == null)
			return;
		if (t.showMessage())
			for (String a : msg) {
				Center.sendCenteredMessage(p, a);
			}
		t.fire(p);
	}

}
