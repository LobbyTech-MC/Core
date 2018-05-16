package me.dablakbandit.core.utils;

import org.bukkit.Location;

public class BasicLocation{
	
	protected int x, y, z;
	
	public BasicLocation(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Location getRelative(Location loc){
		return loc.clone().add(x, y, z);
	}
	
	public boolean equals(Object o){
		if(!(o instanceof BasicLocation)){ return false; }
		BasicLocation bl = (BasicLocation)o;
		return this.x == bl.x && this.y == bl.y && this.z == bl.z;
	}
}
