package me.dablakbandit.core.config.path;

import me.dablakbandit.core.config.RawConfiguration;

public class DoubleNicePath extends Path<Double>{
	public DoubleNicePath(double def){
		super(def);
	}
	
	@Deprecated
	public DoubleNicePath(String old, double def){
		super(old, def);
	}
	
	protected Double get(RawConfiguration config, String path){
		return Double.valueOf(config.getString(path).replaceAll(",", ""));
	}
	
	@Override
	protected Object setAs(Double aDouble){
		return String.format("%.2f", aDouble);
	}
}
