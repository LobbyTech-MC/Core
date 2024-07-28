package me.dablakbandit.core;

import me.dablakbandit.core.commands.AbstractCommand;
import me.dablakbandit.core.database.DatabaseManager;
import me.dablakbandit.core.metrics.NewMetrics;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.updater.PluginUpdater;
import me.dablakbandit.core.utils.anvil.AnvilUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CorePlugin extends JavaPlugin{
	
	private static CorePlugin main;
	
	public static CorePlugin getInstance(){
		return main;
	}
	
	public void onLoad(){
		main = this;
		CorePluginConfiguration.getInstance().load();
		CoreLanguageConfiguration.setup(this, "language.yml");
		// Loads PlayerManager class
		CorePlayerManager.getInstance();
		PluginUpdater.getInstance().checkUpdate(this, "56780", "https://github.com/Dablakbandit/Core/raw/master/output/core-latest.jar", "core-latest.jar");
	}

	private void delayMetrics(){
		NewMetrics metrics = new NewMetrics(this, 2565);
		metrics.addCustomChart(new NewMetrics.AdvancedPie("common_plugins", new Callable<Map<String, Integer>>() {
			@Override
			public Map<String, Integer> call() throws Exception {
				Map<String, Integer> map = new HashMap<>();
				for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
					map.put(plugin.getName(), 1);
				}
				return map;
			}
		}));
	}

	public void onEnable(){
		// Enables PlayerManager class
		CorePlayerManager.getInstance().enable();
		
		// PluginUpdater
		PluginUpdater.getInstance().start();
		AnvilUtil.load();
		
		AbstractCommand.enable();
		Bukkit.getScheduler().runTaskLater(this, ()->{
			delayMetrics();
		}, 20);
	}
	
	public void onDisable(){
		CorePlayerManager.getInstance().disable();
		
		DatabaseManager.getInstance().close();
	}
	
}
