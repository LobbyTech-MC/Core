package me.dablakbandit.core;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.bukkit.plugin.Plugin;

import me.dablakbandit.core.configuration.PluginConfiguration;

public class CorePluginConfiguration extends PluginConfiguration{
	
	private static CorePluginConfiguration	configuration;
	
	public static StringPath				TOKENS_COMMAND	= new StringPath("Tokens.Command", "tokens");
	public static StringListPath			TOKENS_ALIASES	= new StringListPath("Tokens.Aliases", Arrays.asList(new String[]{ "token", }));
	
	public static IntegerPath				UPDATE_CHECK	= new IntegerPath("Update_Check", 3600);
	
	private CorePluginConfiguration(Plugin plugin){
		super(plugin);
	}
	
	public static void setup(Plugin plugin){
		configuration = new CorePluginConfiguration(plugin);
		load();
	}
	
	public static void load(){
		configuration.plugin.reloadConfig();
		try{
			boolean save = false;
			for(Field f : CorePluginConfiguration.class.getDeclaredFields()){
				if(Path.class.isAssignableFrom(f.getType())){
					Path p = (Path)f.get(null);
					if(!save){
						save = p.retrieve(configuration.plugin.getConfig());
					}else{
						p.retrieve(configuration.plugin.getConfig());
					}
				}
			}
			if(save){
				configuration.plugin.saveConfig();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void reload(){
		load();
	}
	
}
