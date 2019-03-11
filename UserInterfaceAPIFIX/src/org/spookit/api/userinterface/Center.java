package org.spookit.api.userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Center {
	private final static int CENTER_PX = 154;

	public static String center(String message) {
		if (message == null || message.equals(""))
			return message;
		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			} else if (previousCode == true) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else
					isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		return sb.toString()+message;
	}
	public static String titledCenter(String message) {
		if (message == null) message = new String();
		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			} else if (previousCode == true) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else
					isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = 154 - halvedMessageSize;
		int spaceLength = DefaultFontInfo.MINUS.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.translateAlternateColorCodes('&', "&8&m"));
		while (compensated < toCompensate) {
			sb.append(ChatColor.translateAlternateColorCodes('&', "-"));
			compensated += spaceLength;
		}
		sb.append(message);
		compensated = 0;
		sb.append(ChatColor.translateAlternateColorCodes('&', "&8&m"));
		while (compensated < toCompensate) {
			sb.append("-");
			compensated += spaceLength;
		}
		return sb.toString();
	}
	public static void sendCenteredMessage(Player player, String message) {
		if (message == null || message.equals(""))
			player.sendMessage("");
		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			} else if (previousCode == true) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else
					isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		player.sendMessage(sb.toString() + message);
	}

	public static void main(String[]args) {
		for (String s : multicenter("abcdefghijklmnopqrstuvwxyz12345677890abcdefghijklmnopqrstuvwxyz0987654321")) {
			System.out.println(s);
		}
	}
	public static List<String> multicenter(String message) {
		if (message == null || message.equals(""))
			return Arrays.asList(message);
		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		String ap = new String();
		for (char c : message.toCharArray()) {
			if (messagePxSize >= CENTER_PX*2) {
				ap+=c;
			}
			if (c == '§') {
				previousCode = true;
				continue;
			} else if (previousCode == true) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else
					isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		String sb = new String();
		ArrayList<String> messages = new ArrayList<>();
		while (compensated < toCompensate) {
			sb += " ";
			compensated += spaceLength;
		}
		sb += message;
		//System.out.println(ap);
		sb = sb.substring(0,sb.length()-ap.length());
		messages.add(sb);
		if (!ap.isEmpty()) {
			//System.out.println(ap);
			messages.addAll(multicenter(ap));
		}
		return messages;
	}

}
