package me.dablakbandit.core.config.path;

import me.dablakbandit.core.config.RawConfiguration;

public class EmptyPath extends Path{
	public EmptyPath(){
		super(null);
	}
	
	@Deprecated
	public EmptyPath(String old){
		super(old, null);
	}
	
	protected Object get(RawConfiguration config, String path){
		return null;
	}
	
	@Override
	public void set(Object o, boolean reload_save){
		
	}
}
