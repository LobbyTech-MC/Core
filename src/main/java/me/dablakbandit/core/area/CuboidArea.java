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
			if(x <= x1 && x >= x2){ return true; }
		}else{
			if(x >= x1 && x <= x2){ return true; }
		}
		return false;
	}
	
	public boolean isYIn(int y, int y1, int y2){
		if(y1 >= y2){
			if(y <= y1 && y >= y2){ return true; }
		}else{
			if(y >= y1 && y <= y2){ return true; }
		}
		return false;
	}
	
	public boolean isZIn(int z, int z1, int z2){
		if(z1 >= z2){
			if(z <= z1 && z >= z2){ return true; }
		}else{
			if(z >= z1 && z <= z2){ return true; }
		}
		return false;
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
		double x1 = start.getX();
		double x2 = end.getX();
		double y1 = start.getY();
		double y2 = end.getY();
		double z1 = start.getZ();
		double z2 = end.getZ();
		
		double lx;
		double hx;
		double ly;
		double hy;
		double lz;
		double hz;
		
		if(x1 < x2){
			lx = x1;
			hx = x2;
		}else{
			lx = x2;
			hx = x1;
		}
		
		if(y1 < y2){
			ly = y1;
			hy = y2;
		}else{
			ly = y2;
			hy = y1;
		}
		
		if(z1 < z2){
			lz = z1;
			hz = z2;
		}else{
			lz = z2;
			hz = z1;
		}
		
		return new CuboidArea(new Location(start.getWorld(), lx, ly, lz), new Location(start.getWorld(), hx, hy, hz));
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
		
		if(x3 > x2 || z3 > z2 || y3 > y2 || y1 > y4 || x1 > x4 || z1 > z4){ return false; }
		return true;
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
