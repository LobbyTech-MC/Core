package me.dablakbandit.core.area;

import org.bukkit.Location;

public class RadiusArea extends SphereArea{
	
	public RadiusArea(Location middle){
		super(middle);
	}
	
	@Override
	public boolean isIn(Location loc){
		Location l = loc.clone();
		l.setY(middle.getY());
		if((int)middle.distanceSquared(l) <= Math.pow(getRadius(), 2)){ return true; }
		return false;
	}
	
	@Override
	public RadiusArea getRelative(Location loc){
		return new RadiusArea(middle.clone().subtract(loc));
	}
	
	@Override
	public RadiusArea addRelative(Location loc){
		return new RadiusArea(loc.clone().add(middle));
	}
	
}
