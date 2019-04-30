package me.dablakbandit.core.area;

import org.bukkit.Location;

public class RadiusArea extends SphereArea{
	
	public RadiusArea(Location middle){
		this(middle, 0);
	}
	
	public RadiusArea(Location middle, double radius){
		super(middle, radius);
	}
	
	@Override
	public boolean isIn(Location loc){
		Location l = loc.clone();
		l.setY(middle.getY());
		return (int)middle.distanceSquared(l) <= Math.pow(getRadius(), 2);
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
