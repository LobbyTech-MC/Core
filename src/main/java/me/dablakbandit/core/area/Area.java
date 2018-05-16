package me.dablakbandit.core.area;

import org.bukkit.Location;

public abstract class Area{
	
	public abstract boolean isIn(Location loc);
	
	public abstract Location getTeleportLocation();
	
	public abstract Area getRelative(Location loc);
	
	public abstract Area addRelative(Location loc);
}
