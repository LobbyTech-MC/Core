package me.dablakbandit.core;

import me.dablakbandit.core.configuration.AdvancedConfiguration;
import me.dablakbandit.core.configuration.PluginConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class CorePluginConfiguration extends PluginConfiguration{
	
	private static CorePluginConfiguration configuration = new CorePluginConfiguration(CorePlugin.getInstance());
	
	public static CorePluginConfiguration getInstance(){
		return configuration;
	}
	
	public static StringPath						TOKENS_COMMAND			= new StringPath("Tokens.Command", "tokens");
	public static StringListPath					TOKENS_ALIASES			= new StringListPath("Tokens.Aliases", Arrays.asList(new String[]{ "token", }));
	
	public static IntegerPath						UPDATE_CHECK			= new IntegerPath("Update_Check", 3600);
	public static AdvancedConfiguration.BooleanPath	CATCH_CANCELLED_PACKET	= new AdvancedConfiguration.BooleanPath("Catch_Cancelled_Packet", false);
	public static AdvancedConfiguration.BooleanPath	LATE_BIND	= new AdvancedConfiguration.BooleanPath("late-bind", false);

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
