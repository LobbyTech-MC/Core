package me.dablakbandit.core.config.path;

import me.dablakbandit.core.config.RawConfiguration;

public class StringPath extends Path<String>{
	public StringPath(String def){
		super(def);
	}
	
	@Deprecated
	public StringPath(String old, String def){
		super(old, def);
	}
	
	protected String get(RawConfiguration config, String path){
		return config.getString(path);
	}
}
