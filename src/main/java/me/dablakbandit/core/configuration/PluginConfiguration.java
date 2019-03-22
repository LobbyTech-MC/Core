package me.dablakbandit.core.configuration;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public abstract class PluginConfiguration{
	
	protected Plugin				plugin;
	protected PluginConfiguration	instance;
	
	public PluginConfiguration(Plugin plugin){
		this.plugin = plugin;
		this.instance = this;
	}
	
	protected void loadPaths(){
		instance.plugin.reloadConfig();
		try{
			boolean save = false;
			for(Field f : instance.getClass().getDeclaredFields()){
				if(Path.class.isAssignableFrom(f.getType())){
					Path p = (Path)f.get(null);
					p.setInstance(this);
					if(p.retrieve(instance.plugin.getConfig())){
						instance.plugin.saveConfig();
					}
					p.init();
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Deprecated
	public void set(Path p, Object to){
		FileConfiguration config = plugin.getConfig();
		config.set(p.getPath(), to);
		plugin.saveConfig();
		p.retrieve(config);
	}
	
	public abstract static class Path<T>{
		protected T						value, def;
		protected String				path;
		protected String				old;
		protected PluginConfiguration	instance;
		
		private Path(String path, T def){
			this.path = path;
			this.def = def;
		}
		
		private Path(String path, String old, T def){
			this(path, def);
			this.old = old;
		}
		
		protected void setInstance(PluginConfiguration instance){
			this.instance = instance;
		}
		
		public T get(){
			return value;
		}
		
		public void set(T t){
			this.value = t;
			instance.plugin.reloadConfig();
			FileConfiguration config = instance.plugin.getConfig();
			config.set(path, t);
			instance.plugin.saveConfig();
		}
		
		public String getPath(){
			return this.path;
		}
		
		protected abstract T get(FileConfiguration config, String path);
		
		public boolean retrieve(FileConfiguration config){
			if(this.old != null && config.isSet(this.old)){
				this.value = get(config, this.old);
				config.set(this.old, (Object)null);
				config.set(this.path, this.def);
				return true;
			}else if(config.isSet(this.path)){
				this.value = get(config, this.path);
				return false;
			}else{
				this.value = this.def;
				config.set(this.path, this.def);
				return true;
			}
		}
		
		protected void init(){
			
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
	
	public static class StringListPath extends Path<List<String>>{
		
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
}
