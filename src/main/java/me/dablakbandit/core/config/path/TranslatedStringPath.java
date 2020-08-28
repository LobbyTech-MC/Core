package me.dablakbandit.core.config.path;

import org.bukkit.ChatColor;

import me.dablakbandit.core.config.RawConfiguration;

public class TranslatedStringPath extends Path<String>{
	public TranslatedStringPath(String def){
		super(def);
	}
	
	@Deprecated
	public TranslatedStringPath(String old, String def){
		super(old, def);
	}
	
	protected String get(RawConfiguration config, String path){
		return ChatColor.translateAlternateColorCodes('&', config.getString(path));
	}
	
	@Override
	protected Object setAs(String s){
		return s.replaceAll("§", "&");
	}
}
