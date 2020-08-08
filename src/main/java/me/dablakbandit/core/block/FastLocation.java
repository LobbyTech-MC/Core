/*
 * Copyright (c) 2020 Ashley Thew
 */

package me.dablakbandit.core.block;

import org.bukkit.Location;
import org.bukkit.World;

public class FastLocation{
	
	private World	world;
	private double	x, y, z;
	
	public FastLocation(World world, double x, double y, double z){
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	private Location location;
	
	public Location getLocation(){
		return location == null ? location = new Location(world, x, y, z) : location;
	}
	
	public World getWorld(){
		return world;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
	
	public int getBlockX(){
		return (int)x;
	}
	
	public int getBlockY(){
		return (int)y;
	}
	
	public int getBlockZ(){
		return (int)z;
	}
	
}
