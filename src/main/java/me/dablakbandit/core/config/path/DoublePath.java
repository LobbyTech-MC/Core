package me.dablakbandit.core.config.path;

import me.dablakbandit.core.config.RawConfiguration;

public class DoublePath extends Path<Double>{
	public DoublePath(double def){
		super(def);
	}
	
	@Deprecated
	public DoublePath(String old, double def){
		super(old, def);
	}
	
	protected Double get(RawConfiguration config, String path){
		return config.getDouble(path);
	}
}
