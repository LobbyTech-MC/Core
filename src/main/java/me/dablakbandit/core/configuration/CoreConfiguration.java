/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CoreConfiguration extends AdvancedConfiguration{
	
	private Configuration config;
	
	public CoreConfiguration(JavaPlugin plugin, String path){
		super(plugin);
		config = new Configuration(plugin, path);
	}
	
	@Override
	public void reloadConfig(){
		config.reloadConfig();
	}
	
	@Override
	public void saveConfig(){
		config.saveConfig();
	}
	
	@Override
	public FileConfiguration getConfig(){
		return config.getConfig();
	}
	
	@Override
	public AdvancedConfiguration getThis(){
		return this;
	}
	
	@Override
	public void delete(){
		config.getFile().delete();
	}
}
