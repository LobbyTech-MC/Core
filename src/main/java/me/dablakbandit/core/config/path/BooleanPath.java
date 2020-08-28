package me.dablakbandit.core.config.path;

import me.dablakbandit.core.config.RawConfiguration;

public class BooleanPath extends Path<Boolean>{
	public BooleanPath(boolean def){
		super(def);
	}
	
	@Deprecated
	public BooleanPath(String old, boolean def){
		super(old, def);
	}
	
	protected Boolean get(RawConfiguration config, String path){
		return config.getBoolean(path);
	}
}
