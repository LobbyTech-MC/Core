package me.dablakbandit.core.area;

import org.bukkit.Location;

public class CuboidArea extends Area{
	
	protected Location start, end;
	
	public CuboidArea(Location start, Location end){
		this.start = start;
		this.end = end;
	}
	
	public int getXDifference(){
		return Math.abs(start.getBlockX() - end.getBlockX());
	}
	
	public int getYDifference(){
		return Math.abs(start.getBlockY() - end.getBlockY());
	}
	
	public int getZDifference(){
		return Math.abs(start.getBlockZ() - end.getBlockZ());
	}
	
	@Override
	public boolean isIn(Location loc){
		return (isXIn(loc.getBlockX(), start.getBlockX(), end.getBlockX()) && isYIn(loc.getBlockY(), start.getBlockY(), end.getBlockY()) && isZIn(loc.getBlockZ(), start.getBlockZ(), end.getBlockZ()));
	}
	
	public boolean isXIn(int x, int x1, int x2){
		if(x1 >= x2){
			return x <= x1 && x >= x2;
		}else{
			return x >= x1 && x <= x2;
		}
	}
	
	public boolean isYIn(int y, int y1, int y2){
		if(y1 >= y2){
			return y <= y1 && y >= y2;
		}else{
			return y >= y1 && y <= y2;
		}
	}
	
	public boolean isZIn(int z, int z1, int z2){
		if(z1 >= z2){
			return z <= z1 && z >= z2;
		}else{
			return z >= z1 && z <= z2;
		}
	}
	
	public void setStart(Location l){
		this.start = l;
	}
	
	public Location getStart(){
		return start;
	}
	
	public void setEnd(Location l){
		this.end = l;
	}
	
	public Location getEnd(){
		return end;
	}
	
	public CuboidArea normalize(){
		double x1 = start.getX(), x2 = end.getX(), y1 = start.getY(), y2 = end.getY(), z1 = start.getZ(), z2 = end.getZ();
		return new CuboidArea(new Location(start.getWorld(), Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2)), new Location(start.getWorld(), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2)));
	}
	
	public boolean overlaps(CuboidArea ca){
		CuboidArea t = normalize();
		CuboidArea c = ca.normalize();
		
		double x1 = t.getStart().getX();
		double y1 = t.getStart().getY();
		double z1 = t.getStart().getZ();
		
		double x2 = t.getEnd().getX();
		double y2 = t.getEnd().getY();
		double z2 = t.getEnd().getZ();
		
		double x3 = c.getStart().getX();
		double y3 = c.getStart().getY();
		double z3 = c.getStart().getZ();
		
		double x4 = c.getEnd().getX();
		double y4 = c.getEnd().getY();
		double z4 = c.getEnd().getZ();
		
		return !(x3 > x2) && !(z3 > z2) && !(y3 > y2) && !(y1 > y4) && !(x1 > x4) && !(z1 > z4);
	}
	
	@Override
	public Location getTeleportLocation(){
		return new Location(start.getWorld(), (start.getX() + end.getX()) / 2.0, (start.getY() + end.getY()) / 2.0, (start.getZ() + end.getZ()) / 2.0);
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName() + " [ " + start.toString() + " / " + end.toString() + " ]";
	}
	
	@Override
	public CuboidArea getRelative(Location loc){
		return new CuboidArea(start.clone().subtract(loc), end.clone().subtract(loc));
	}
	
	@Override
	public CuboidArea addRelative(Location loc){
		return new CuboidArea(loc.clone().add(start), loc.clone().add(end));
	}
}
