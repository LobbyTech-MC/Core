package me.dablakbandit.core.config.path;

import org.bukkit.Location;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.utils.LocationUtils;

public class LocationPath extends Path<Location>{
	public LocationPath(Location def){
		super(def);
	}
	
	@Deprecated
	public LocationPath(String old, Location def){
		super(old, def);
	}
	
	protected Location get(RawConfiguration config, String path){
		return LocationUtils.getLocation(config.getString(path));
	}
	
	protected Object setAs(Location location){
		return LocationUtils.locationToString(location);
	}
}
