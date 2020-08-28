package me.dablakbandit.core.config.path;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.utils.NMSUtils;

public class EnumPath<T extends Enum<T>>extends Path<T>{
	Class<T> clazz;
	
	public EnumPath(Class<T> clazz, T def){
		super(def);
		this.clazz = clazz;
	}
	
	@Deprecated
	public EnumPath(String old, Class<T> clazz, T def){
		super(old, def);
		this.clazz = clazz;
	}
	
	protected T get(RawConfiguration config, String path){
		return (T)NMSUtils.getEnum(config.getString(path), this.clazz);
	}
	
	protected Object setAs(T val){
		return val.name();
	}
}
