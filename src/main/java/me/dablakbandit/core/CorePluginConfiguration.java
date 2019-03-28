package me.dablakbandit.core;

import java.util.Arrays;

import org.bukkit.plugin.Plugin;

import me.dablakbandit.core.configuration.PluginConfiguration;

public class CorePluginConfiguration extends PluginConfiguration{
	
	private static CorePluginConfiguration configuration = new CorePluginConfiguration(CorePlugin.getInstance());
	
	public static CorePluginConfiguration getInstance(){
		return configuration;
	}
	
	public static StringPath		TOKENS_COMMAND	= new StringPath("Tokens.Command", "tokens");
	public static StringListPath	TOKENS_ALIASES	= new StringListPath("Tokens.Aliases", Arrays.asList(new String[]{ "token", }));
	
	public static IntegerPath		UPDATE_CHECK	= new IntegerPath("Update_Check", 3600);
	
	private CorePluginConfiguration(Plugin plugin){
		super(plugin);
	}
	
	public void load(){
		loadPaths();
	}
	
	public void reload(){
		load();
	}
	
}
