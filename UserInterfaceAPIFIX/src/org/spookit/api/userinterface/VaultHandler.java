package org.spookit.api.userinterface;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public final class VaultHandler {

	private static VaultHandler handler;
	public static VaultHandler getInstance() {
		if (handler == null) new VaultHandler();
		return handler;
	}
	public VaultHandler() {
		handler = this;
		if (!setupChat()) {
			System.out.println("[UI] Failure to setup VaultChat :(");
		}
		if (!setupPermission()) {
			System.out.println("[UI] Failure to setup VaultPermission :(");
		}
		if (!setupEconomy()) {
			System.out.println("[UI] Failure to setup VaultEconomy :(");
		}
	}
	public Chat getChatHandler() {
		return chat;
	}
	public Economy getEconomyHandler() {
		return economy;
	}
	public Permission getPermissionHandler() {
		return permission;
	}
	private Chat chat;
	private Economy economy;
	private Permission permission;
	private boolean setupPermission() {
		RegisteredServiceProvider<Permission> permProvider = Bukkit.getServicesManager().getRegistration(Permission.class);
		if (permProvider != null) {
			permission = (Permission) permProvider.getProvider();
		}
		return permission != null;
		
	}
	private boolean setupChat() {
		RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServicesManager().getRegistration(Chat.class);
		if (chatProvider != null) {
			chat = (Chat) chatProvider.getProvider();
		}

		return chat != null;
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			economy = (Economy) economyProvider.getProvider();
		}

		return economy != null;
	}
}
