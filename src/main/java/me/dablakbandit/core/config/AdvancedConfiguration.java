package me.dablakbandit.core.config;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.WordUtils;
import org.bukkit.plugin.Plugin;

import me.dablakbandit.core.config.path.Path;
import me.dablakbandit.core.utils.NMSUtils;

public abstract class AdvancedConfiguration{
	protected AdvancedConfiguration	instance	= this;
	protected Plugin				plugin;
	
	public AdvancedConfiguration(Plugin plugin){
		this.plugin = plugin;
	}
	
	public void load(){
		loadPaths();
	}
	
	protected void loadPaths(){
		this.loadPaths(this.instance.getClass(), this);
	}
	
	protected void loadPaths(Class<?> clazz, Object from){
		this.reloadConfig();
		
		try{
			Set<Boolean> set = NMSUtils.getFields(clazz).stream().filter(field -> Path.class.isAssignableFrom(field.getType())).map(field -> {
				try{
					return new AbstractMap.SimpleEntry<>(field, (Path)field.get(from));
				}catch(IllegalAccessException e){
					e.printStackTrace();
				}
				return null;
			}).map(pair -> loadPath(pair.getKey(), pair.getValue())).collect(Collectors.toSet());
			boolean save = set.contains(Boolean.TRUE);
			if(save){
				saveConfig();
			}
		}catch(Exception var7){
			var7.printStackTrace();
		}
	}
	
	protected boolean loadPath(Field field, Path path){
		path.setInstance(this.instance);
		String fieldPath = WordUtils.capitalize(field.getName().toLowerCase().replaceAll("_", " ")).replace(" ", ".");
		boolean save = path.retrieve(fieldPath, this.getConfig());
		path.init();
		return save;
	}
	
	public abstract void reloadConfig();
	
	public abstract void saveConfig();
	
	public abstract RawConfiguration getConfig();
	
	public abstract void delete();
}
