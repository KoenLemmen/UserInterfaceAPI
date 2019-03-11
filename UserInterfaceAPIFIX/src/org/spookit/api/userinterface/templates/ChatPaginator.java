package org.spookit.api.userinterface.templates;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.spookit.api.userinterface.Interator;
import org.spookit.api.userinterface.Paginator;

public class ChatPaginator {

	Map<Integer,List<String>> pages = new LinkedHashMap<>();
	ChatPaginator() {}
	public static ChatPaginator create(String...strings) {
		ChatPaginator pa = new ChatPaginator();
		Paginator<String> pagi = new Paginator<>(Arrays.asList(strings), 9);
		for (int i : new Interator(pagi.totalPage())) {
			pa.pages.put(i, pagi.paginate(i));
		}
		return pa;
	}
	public void send(int page,CommandSender sender) {
		List<String> sts = pages.get(page);
		if (sts == null) {
			sender.sendMessage(color("&cPage not found :("));
			return;
		}
		for (String s : sts) {
			sender.sendMessage(color(s));
		}
	}
	public List<String> getPage(int page) {
		return pages.get(page);
	}
	public int size() {
		return pages.size();
	}
	String color(String a) {
		return ChatColor.translateAlternateColorCodes('&', a);
	}
}
