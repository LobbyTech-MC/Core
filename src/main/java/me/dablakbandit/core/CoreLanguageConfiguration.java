package me.dablakbandit.core;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.core.configuration.Configuration;

public class CoreLanguageConfiguration extends Configuration{
	
	private static CoreLanguageConfiguration	config;
	
	// TITLE
	public static LanguageMessage				MESSAGE_TOKENS_BALANCE		= new LanguageMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Core " + ChatColor.RESET + ChatColor.YELLOW + ">> " + ChatColor.WHITE + " Your balance is: <i> Token(s).");
	
	public static LanguageMessage				MESSAGE_NOT_ENOUGH_TOKENS	= new LanguageMessage(ChatColor.RED + "You do not have enough tokens to do that.");
	public static LanguageMessage				MESSAGE_PURCHASED			= new LanguageMessage(ChatColor.GREEN + "You purchased <n> " + ChatColor.RESET + ChatColor.GREEN + "for <i> tokens.");
	public static LanguageMessage				MESSAGE_YOU_NOW_HAVE		= new LanguageMessage(ChatColor.GREEN + "You now have <i> tokens.");
	
	private CoreLanguageConfiguration(JavaPlugin plugin, String filename){
		super(plugin, filename);
	}
	
	public static void setup(JavaPlugin plugin, String filename){
		config = new CoreLanguageConfiguration(plugin, filename);
		load();
	}
	
	public static void load(){
		config.reloadConfig();
		try{
			for(Field f : CoreLanguageConfiguration.class.getDeclaredFields()){
				if(f.getType().equals(LanguageMessage.class)){
					((LanguageMessage)f.get(null)).get(f.getName().replaceFirst("_", "."));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void reload(){
		load();
	}
	
	public static class LanguageMessage{
		
		private String def, get;
		
		private LanguageMessage(String def){
			this.def = translateDefault('&', def);
		}
		
		private void get(String path){
			if(config.getConfig().isSet(path)){
				get = config.getConfig().getString(path);
			}else{
				config.getConfig().set(path, def);
				config.saveConfig();
				get = def;
			}
			get = ChatColor.translateAlternateColorCodes('&', get);
			get = StringEscapeUtils.unescapeJava(get);
		}
		
		public String getMessage(){
			return get;
		}
		
		public static final char COLOR_CHAR = '\u00A7';
		
		private String translateDefault(char to, String textToTranslate){
			char[] b = textToTranslate.toCharArray();
			for(int i = 0; i < b.length - 1; i++){
				if(b[i] == COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1){
					b[i] = to;
					b[i + 1] = Character.toLowerCase(b[i + 1]);
				}
			}
			return new String(b);
		}
	}
}
