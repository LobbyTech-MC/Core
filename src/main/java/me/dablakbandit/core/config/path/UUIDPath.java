package me.dablakbandit.core.config.path;

import me.dablakbandit.core.config.RawConfiguration;

import java.util.UUID;

public class UUIDPath extends Path<UUID>{
	public UUIDPath(UUID def){
		super(def);
	}

	@Deprecated
	public UUIDPath(String old, UUID def){
		super(old, def);
	}
	
	protected UUID get(RawConfiguration config, String path){
		return UUID.fromString(config.getString(path));
	}
	
	@Override
	protected Object setAs(UUID s){
		return s.toString();
	}
}
