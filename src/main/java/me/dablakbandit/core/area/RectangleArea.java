package me.dablakbandit.core.area;

import org.bukkit.Location;

public class RectangleArea extends CuboidArea{
	
	public RectangleArea(Location start, Location end){
		super(start, end);
	}
	
	@Override
	public RectangleArea normalize(){
		CuboidArea ca = super.normalize();
		return new RectangleArea(ca.getStart(), ca.getEnd());
	}
	
	@Override
	public boolean isIn(Location loc){
		return loc.getWorld() == start.getWorld() && isXIn(loc.getBlockX(), start.getBlockX(), end.getBlockX()) && isZIn(loc.getBlockZ(), start.getBlockZ(), end.getBlockZ());
	}
	
	@Override
	public boolean overlaps(CuboidArea ca){
		CuboidArea t = normalize();
		CuboidArea c = ca.normalize();
		
		double x1 = t.getStart().getX();
		double z1 = t.getStart().getZ();
		
		double x2 = t.getEnd().getX();
		double z2 = t.getEnd().getZ();
		
		double x3 = c.getStart().getX();
		double z3 = c.getStart().getZ();
		
		double x4 = c.getEnd().getX();
		double z4 = c.getEnd().getZ();
		
		return !(x3 > x2) && !(z3 > z2) && !(x1 > x4) && !(z1 > z4);
	}
	
	@Override
	public RectangleArea getRelative(Location loc){
		return new RectangleArea(start.clone().subtract(loc), end.clone().subtract(loc));
	}
	
	@Override
	public RectangleArea addRelative(Location loc){
		return new RectangleArea(loc.clone().add(start), loc.clone().add(end));
	}
	
}
