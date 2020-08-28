package me.dablakbandit.core.config.path;

import java.util.function.Supplier;

import me.dablakbandit.core.config.AdvancedConfiguration;
import me.dablakbandit.core.config.RawConfiguration;

public abstract class Path<T> implements Supplier<T>{
	protected T						value;
	protected T						def;
	protected String				prePath;
	protected String				path;
	protected String				old;
	protected AdvancedConfiguration	instance;
	
	protected Path(T def){
		this.def = def;
	}
	
	@Deprecated
	protected Path(String old, T def){
		this(def);
		this.old = old;
	}
	
	public void setInstance(AdvancedConfiguration instance){
		this.instance = instance;
	}
	
	public T get(){
		return this.value;
	}
	
	public T getDefault(){
		return def;
	}
	
	public boolean hasChangedDefault(){
		return !get().equals(def);
	}
	
	public T get(T def){
		return this.value != null ? this.value : def;
	}
	
	public T override(T val){
		if(val != null){
			if(this.value != val){
				this.set(val, true);
			}
			
			return val;
		}else{
			return this.value;
		}
	}
	
	public void set(T t, boolean reload_save){
		this.value = t;
		reload(reload_save);
		RawConfiguration config = this.instance.getConfig();
		String path = this.getActualPath();
		if(t != null){
			Object set = this.setAs(config, t);
			if(set != null){
				config.set(path, set);
			}
		}
		save(reload_save);
	}
	
	private void reload(boolean reload){
		if(reload){
			this.instance.reloadConfig();
		}
	}
	
	private void save(boolean save){
		if(save){
			this.instance.saveConfig();
		}
	}
	
	public void set(T t){
		this.set(t, true);
	}
	
	protected Object setAs(RawConfiguration config, T t){
		return setAs(t);
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
	
	public String getActualPath(){
		return this.prePath == null ? this.path : this.prePath + "." + this.path;
	}
	
	protected abstract T get(RawConfiguration var1, String var2);
	
	public boolean retrieve(String fieldPath, RawConfiguration config){
		this.path = fieldPath;
		String path = this.getActualPath();
		if(this.old != null && config.isSet(this.old)){
			this.value = this.get(config, this.old);
			config.set(this.old, (Object)null);
			this.set(this.def, false);
			return true;
		}else if(config.isSet(path)){
			this.value = this.get(config, path);
			return false;
		}else{
			this.value = this.def;
			this.set(this.def, false);
			return true;
		}
	}
	
	public boolean isSet(String base, String value){
		return this.instance.getConfig().isSet(base + "." + value);
	}
	
	public boolean isSet(String base, String... values){
		RawConfiguration config = this.instance.getConfig();
		for(String s : values){
			if(!config.isSet(base + "." + s)){ return false; }
		}
		return true;
	}
	
	public void init(){
	}
}
