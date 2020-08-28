package me.dablakbandit.core.config.path;

import me.dablakbandit.core.config.RawConfiguration;

public class LongPath extends Path<Long>{
	public LongPath(long def){
		super(def);
	}
	
	@Deprecated
	public LongPath(String old, long def){
		super(old, def);
	}
	
	protected Long get(RawConfiguration config, String path){
		return config.getLong(path);
	}
}
