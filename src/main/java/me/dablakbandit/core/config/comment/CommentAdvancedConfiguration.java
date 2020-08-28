package me.dablakbandit.core.config.comment;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

import org.bukkit.plugin.Plugin;

import me.dablakbandit.core.config.AdvancedConfiguration;
import me.dablakbandit.core.config.path.Path;

public abstract class CommentAdvancedConfiguration extends AdvancedConfiguration{
	
	private CommentConfiguration config;
	
	public CommentAdvancedConfiguration(Plugin plugin, String path){
		this(plugin, path, CommentConfiguration::new);
	}
	
	public CommentAdvancedConfiguration(Plugin plugin, String path, BiFunction<Plugin, String, CommentConfiguration> config){
		super(plugin);
		this.config = config.apply(plugin, path);
	}
	
	@Override
	protected boolean loadPath(Field field, Path path){
		boolean save = super.loadPath(field, path);
		return config.parseAnnotations(path.getActualPath(), field.getAnnotations()) || save;
	}
	
	@Override
	public void reloadConfig(){
		this.config.reloadConfig();
	}
	
	@Override
	public void saveConfig(){
		this.config.saveConfig();
	}
	
	@Override
	public CommentConfiguration getConfig(){
		return this.config;
	}
	
	@Override
	public void delete(){
		if(this.config.getFile().exists()){
			this.config.getFile().delete();
		}
	}
	
}
