package me.dablakbandit.core.config.path;

import me.dablakbandit.core.config.RawConfiguration;

public class IntegerPath extends Path<Integer>{
	public IntegerPath(int def){
		super(def);
	}
	
	@Deprecated
	public IntegerPath(String old, int def){
		super(old, def);
	}
	
	protected Integer get(RawConfiguration config, String path){
		if (config == null) {
			return 0;
		}
		return config.getInt(path);
	}
}
