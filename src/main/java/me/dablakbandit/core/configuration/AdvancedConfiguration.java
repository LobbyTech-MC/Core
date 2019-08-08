/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.configuration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import me.dablakbandit.core.utils.LocationUtils;
import me.dablakbandit.core.utils.NMSUtils;

public abstract class AdvancedConfiguration{
	
	protected AdvancedConfiguration	instance;
	protected Plugin				plugin;
	
	public AdvancedConfiguration(Plugin plugin){
		this.instance = this;
		this.plugin = plugin;
	}
	
	protected void loadPaths(){
		loadPaths(this.instance.getClass(), getThis());
	}
	
	protected void loadPaths(Class<?> clazz, Object from){
		reloadConfig();
		try{
			boolean save = false;
			for(Field f : NMSUtils.getFields(clazz)){
				if(Path.class.isAssignableFrom(f.getType())){
					Path p = (Path)f.get(from);
					p.setInstance(instance);
					if(p.retrieve(getConfig())){
						saveConfig();
					}
					p.init();
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public abstract void reloadConfig();
	
	public abstract void saveConfig();
	
	public abstract FileConfiguration getConfig();
	
	public abstract AdvancedConfiguration getThis();
	
	public abstract void delete();
	
	public abstract static class Path<T>{
		protected T						value, def;
		protected String				prePath, path;
		protected String				old;
		protected AdvancedConfiguration	instance;
		
		protected Path(String path, T def){
			this.path = path;
			this.def = def;
		}
		
		protected Path(String path, String old, T def){
			this(path, def);
			this.old = old;
		}
		
		public void setInstance(AdvancedConfiguration instance){
			this.instance = instance;
		}
		
		public T get(){
			return value;
		}
		
		public T get(T def){
			return value != null ? value : def;
		}
		
		public T override(T val){
			if(val != null){
				if(value != val){
					set(val, true);
				}
				return val;
			}
			return value;
		}
		
		public void set(T t, boolean reload_save){
			this.value = t;
			if(reload_save){
				instance.reloadConfig();
			}
			FileConfiguration config = instance.getConfig();
			String path = getActualPath();
			config.set(path, t == null ? t : setAs(t));
			if(reload_save){
				instance.saveConfig();
			}
		}
		
		public void set(T t){
			set(t, true);
		}
		
		protected Object setAs(T t){
			return t;
		}
		
		public String getPath(){
			return this.path;
		}
		
		public String getPrePath(){
			return this.prePath;
		}
		
		public void setPrePath(String value){
			this.prePath = value;
		}
		
		private String getActualPath(){
			if(prePath == null){
				return path;
			}else{
				return prePath + "." + path;
			}
		}
		
		protected abstract T get(FileConfiguration config, String path);
		
		public boolean retrieve(FileConfiguration config){
			String path = getActualPath();
			if(this.old != null && config.isSet(this.old)){
				this.value = get(config, this.old);
				config.set(this.old, (Object)null);
				set(this.def, false);
				return true;
			}else if(config.isSet(path)){
				this.value = get(config, path);
				return false;
			}else{
				this.value = this.def;
				set(this.def, false);
				return true;
			}
		}
		
		public void init(){
			
		}
	}
	
	public static abstract class ListPath<T>extends Path<List<T>>{
		
		protected ListPath(String path, List<T> def){
			super(path, def);
		}
		
		protected ListPath(String path, String old, List<T> def){
			super(path, old, def);
		}
		
		public void add(T t){
			List<T> list = get();
			list.add(t);
			set(list);
		}
		
		public void remove(T t){
			List<T> list = get();
			list.remove(t);
			set(list);
		}
	}
	
	public static class BooleanPath extends Path<Boolean>{
		
		public BooleanPath(String path, boolean def){
			super(path, def);
		}
		
		public BooleanPath(String path, String old, boolean def){
			super(path, old, def);
		}
		
		@Override
		protected Boolean get(FileConfiguration config, String path){
			return config.getBoolean(path);
		}
	}
	
	public static class LongPath extends Path<Long>{
		
		public LongPath(String path, long def){
			super(path, def);
		}
		
		public LongPath(String path, String old, long def){
			super(path, old, def);
		}
		
		@Override
		protected Long get(FileConfiguration config, String path){
			return config.getLong(path);
		}
	}
	
	public static class StringPath extends Path<String>{
		
		public StringPath(String path, String def){
			super(path, def);
		}
		
		public StringPath(String path, String old, String def){
			super(path, old, def);
		}
		
		@Override
		protected String get(FileConfiguration config, String path){
			return config.getString(path);
		}
	}
	
	public static class IntegerPath extends Path<Integer>{
		
		public IntegerPath(String path, int def){
			super(path, def);
		}
		
		public IntegerPath(String path, String old, int def){
			super(path, old, def);
		}
		
		@Override
		protected Integer get(FileConfiguration config, String path){
			return config.getInt(path);
		}
	}
	
	public static class DoublePath extends Path<Double>{
		
		public DoublePath(String path, double def){
			super(path, def);
		}
		
		public DoublePath(String path, String old, double def){
			super(path, old, def);
		}
		
		@Override
		protected Double get(FileConfiguration config, String path){
			return config.getDouble(path);
		}
	}
	
	public static class StringListPath extends ListPath<String>{
		
		public StringListPath(String path, String... def){
			super(path, Arrays.asList(def));
		}
		
		public StringListPath(String path, List<String> def){
			super(path, def);
		}
		
		public StringListPath(String path, String old, List<String> def){
			super(path, old, def);
		}
		
		@Override
		protected List<String> get(FileConfiguration config, String path){
			return config.getStringList(path);
		}
		
	}
	
	public static class MaterialListPath extends ListPath<Material>{
		
		public MaterialListPath(String path, Material... def){
			super(path, Arrays.asList(def));
		}
		
		public MaterialListPath(String path, List<Material> def){
			super(path, def);
		}
		
		public MaterialListPath(String path, String old, List<Material> def){
			super(path, old, def);
		}
		
		@Override
		protected List<Material> get(FileConfiguration config, String path){
			return config.getStringList(path).stream().map(Material::getMaterial).collect(Collectors.toList());
		}
		
		@Override
		protected Object setAs(List<Material> list){
			return list.stream().map(m -> m.toString()).collect(Collectors.toList());
		}
		
	}
	
	public static class LocationPath extends Path<Location>{
		
		public LocationPath(String path, Location def){
			super(path, def);
		}
		
		public LocationPath(String path, String old, Location def){
			super(path, old, def);
		}
		
		@Override
		protected Location get(FileConfiguration config, String path){
			return LocationUtils.getLocation(config.getString(path));
		}
		
		@Override
		protected Object setAs(Location location){
			return LocationUtils.locationToString(location);
		}
		
	}
	
	public static class EnumPath<T extends Enum<T>>extends Path<T>{
		
		Class<T> clazz;
		
		public EnumPath(String path, Class<T> clazz, T def){
			super(path, def);
			this.clazz = clazz;
		}
		
		public EnumPath(String path, String old, Class<T> clazz, T def){
			super(path, old, def);
			this.clazz = clazz;
		}
		
		@Override
		protected T get(FileConfiguration config, String path){
			return (T)NMSUtils.getEnum(config.getString(path), clazz);
		}
		
		@Override
		protected Object setAs(T val){
			return val.name();
		}
	}
}
