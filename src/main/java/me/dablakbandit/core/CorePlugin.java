package me.dablakbandit.core;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.core.database.DatabaseManager;
import me.dablakbandit.core.metrics.Metrics;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.updater.PluginUpdater;

public class CorePlugin extends JavaPlugin{
	
	private static CorePlugin main;
	
	public static CorePlugin getInstance(){
		return main;
	}
	
	public void onLoad(){
		main = this;
		PluginUpdater.getInstance().checkUpdate(this, "56780");
		// Loads PlayerManager class
		CorePlayerManager.getInstance();
		CorePluginConfiguration.setup(this);
		CoreLanguageConfiguration.setup(this, "language.yml");
		new Metrics(this, "Core_");
	}
	
	public void onEnable(){
		// Enables PlayerManager class
		CorePlayerManager.getInstance().enable();
		
		// PluginUpdater
		PluginUpdater.getInstance().start();
	}
	
	public void onDisable(){
		CorePlayerManager.getInstance().disable();
		
		// Enable later, when we actually have a database
		DatabaseManager.getInstance().close();
	}
	
}
