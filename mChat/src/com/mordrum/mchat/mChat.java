package com.mordrum.mchat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Jesse
 * Date: 3/10/13
 * Time: 5:32 PM
 */
public class mChat {

	protected static List<Replacement> replacements = new ArrayList<Replacement>();
	protected static HashMap<String, Channel> channels;
	protected static HashMap<String, String> activeChannel;
	static String globalChannel = "global";

	protected static void Initialize() {
		replacements = new ArrayList<Replacement>();
		channels = new HashMap<String, Channel>();
		activeChannel = new HashMap<String, String>();
	}

	public static void RegisterNewReplacement(Replacement replacement) {
		replacements.add(replacement);
	}

	public static String ParseString(String stringToParse, Player chatter) {
		return ParseString(stringToParse, chatter, null);
	}

	public static String ParseString(String stringToParse, Player chatter, AsyncPlayerChatEvent event) {
		for (Replacement r : replacements) {    //Loop through replacements
			Matcher matcher = r.getPattern().matcher(stringToParse);
			StringBuffer sb = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(sb, r.call(chatter, event));
			}
			matcher.appendTail(sb);
			stringToParse = sb.toString();
		}
		stringToParse = ChatColor.translateAlternateColorCodes('&', stringToParse);
		stringToParse = stringToParse.replaceAll(Pattern.quote("msg"), Matcher.quoteReplacement(event.getMessage()));
		if (chatter.hasPermission("mchat." + activeChannel.get(chatter.getName()) + ".colorcodes")) {
			stringToParse = ChatColor.translateAlternateColorCodes('&', stringToParse);
		}
		stringToParse = stringToParse.replaceAll("[{}]", "");
		stringToParse = ParseLinks(stringToParse);
		return stringToParse;
	}

	public static String ParseLinks(String stringToParse) {
		String[] words = stringToParse.split(" ");
		String message = stringToParse;
		for (String word : words) {
			if ((word.startsWith("http://") || word.startsWith("https://") || word.startsWith("www.") || word.endsWith(".com") || word.endsWith(".net") || word.endsWith(".org"))
					&& (!(word.endsWith("http://") || word.endsWith("https://") || word.endsWith("www.") || word.startsWith(".com") || word.startsWith(".net") || word.startsWith(".org")))) {
				if (!word.contains("@")) {
					String shortenedURL = new AdFlyShortener(Main.adflyURL + "&url=" + word).shorten();
					if (shortenedURL != null) {
						message = message.replace(word, shortenedURL);
					}
				}
			}
		}
		return message;
	}

	public static List<Replacement> getReplacements() {
		return replacements;
	}

	public static void RegisterNewChannel(Channel ch) {
		channels.put(ch.getName(), ch);
	}

	public static void AddListenerToChannel(String listener, String channel) {
		Channel ch = channels.get(channel);
		if (!ch.getListeners().contains(listener)) ch.addListener(listener);
	}

	public static void AddListenerToChannel(Player listener, String channel) {
		AddListenerToChannel(listener.getName(), channel);
	}

	public static void AddListenerToChannel(String listener, Channel channel) {
		AddListenerToChannel(listener, channel.getName());
	}

	public static void AddListenerToChannel(Player listener, Channel channel) {
		AddListenerToChannel(listener.getName(), channel.getName());
	}
}