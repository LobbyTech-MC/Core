package me.dablakbandit.core.area;

import org.bukkit.Location;

public class SphereArea extends Area{
	
	protected int		radius;
	protected Location	middle;
	
	public SphereArea(Location middle){
		this.middle = middle;
	}
	
	@Override
	public boolean isIn(Location loc){
		return (int)middle.distanceSquared(loc) <= Math.pow(getRadius(), 2);
	}
	
	public void setMiddle(Location loc){
		this.middle = loc;
	}
	
	public Location getMiddle(){
		return middle;
	}
	
	public void setRadius(int radius){
		this.radius = radius;
	}
	
	public int getRadius(){
		return this.radius;
	}
	
	@Override
	public Location getTeleportLocation(){
		return middle;
	}
	
	@Override
	public SphereArea getRelative(Location loc){
		return new SphereArea(middle.clone().subtract(loc));
	}
	
	@Override
	public SphereArea addRelative(Location loc){
		return new SphereArea(loc.clone().add(middle));
	}
}
