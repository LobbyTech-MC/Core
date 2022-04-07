package me.dablakbandit.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CoreLog {
	
	private static String prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "Core" + ChatColor.GRAY + "] ";
	
	public static String getPrefix(){
		return prefix;
	}
	
	public static void print(String message){
			Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.WHITE + message);
	}
	
	public static void info(String message){
			Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.DARK_AQUA + message);
	}

	public static void debug(Object object){
		debug(object == null ? "null" : object.toString());
	}

	public static void debug(Object... object){
		debug(Arrays.stream(object).map(o -> o == null ? "null" : o.toString()).collect(Collectors.joining(", ")));
	}

	public static void debug(String message){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String from = "Unknown";
		for(StackTraceElement st : stackTraceElements){
			String to = st.toString();
			if(!to.startsWith("java.lang") && !to.contains("CoreLog") && !to.startsWith("java.base/java.lang")){
				from = to;
				break;
			}
		}
		Bukkit.getConsoleSender().sendMessage(prefix + from + " " + ChatColor.YELLOW + message);
	}
	
	public static void error(String message){
			errorAlways(message);
	}
	
	public static void errorAlways(String message){
		Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + message);
	}
	
}
