package me.dablakbandit.core.configuration;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public abstract class PluginConfiguration extends AdvancedConfiguration{
	
	public PluginConfiguration(Plugin plugin){
		super(plugin);
	}
	
	protected void loadPaths(){
		super.loadPaths();
	}
	
	@Override
	public void reloadConfig(){
		this.plugin.reloadConfig();
	}
	
	@Override
	public void saveConfig(){
		this.plugin.saveConfig();
	}
	
	@Override
	public FileConfiguration getConfig(){
		return this.plugin.getConfig();
	}
	
	@Override
	public AdvancedConfiguration getThis(){
		return null;
	}
	
	@Override
	public void delete(){
	}
	
	@Deprecated
	public void set(Path p, Object to){
		FileConfiguration config = plugin.getConfig();
		config.set(p.getPath(), to);
		plugin.saveConfig();
		p.retrieve(config);
	}
	
	@Deprecated
	public abstract static class Path<T>extends AdvancedConfiguration.Path<T>{
		
		private Path(String path, T def){
			super(path, def);
		}
		
		private Path(String path, String old, T def){
			super(path, old, def);
		}
		
		public T get(){
			return super.get();
		}
		
		public void set(T t){
			super.set(t);
		}
	}
	
	@Deprecated
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
	
	@Deprecated
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
	
	@Deprecated
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
	
	@Deprecated
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
	
	@Deprecated
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
