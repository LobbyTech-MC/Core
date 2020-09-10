package me.dablakbandit.core.config;

import me.dablakbandit.core.config.path.Path;
import me.dablakbandit.core.utils.NMSUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.AbstractMap;

public abstract class AdvancedConfiguration{
	protected Plugin				plugin;
	
	public AdvancedConfiguration(Plugin plugin){
		this.plugin = plugin;
	}
	
	public void load(){
		loadPaths();
	}
	
	protected void loadPaths(){
		this.loadPaths(this.getClass(), this);
	}
	
	protected void loadPaths(Class<?> clazz, Object from){
		this.reloadConfig();
		try{
			boolean save = NMSUtils.getFields(clazz).stream().filter(field -> Path.class.isAssignableFrom(field.getType())).map(field -> {
				try{
					return new AbstractMap.SimpleEntry<>(field, (Path)field.get(from));
				}catch(IllegalAccessException e){
					e.printStackTrace();
				}
				return null;
			}).map(pair -> loadPath(pair.getKey(), pair.getValue())).count() > 0;
			if(save){
				saveConfig();
			}
		}catch(Exception var7){
			var7.printStackTrace();
		}
	}
	
	protected boolean loadPath(Field field, Path path){
		path.setInstance(this);
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
