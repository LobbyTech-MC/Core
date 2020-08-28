package me.dablakbandit.core.utils.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils{
	public LocationUtils(){
	}
	
	public static Location getLocation(String location){
		if(location != null){
			String[] args = location.split(", ");
			return new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
		}else{
			return null;
		}
	}
	
	public static String locationToString(Location location){
		return location != null ? location.getWorld().getName() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ() + ", " + location.getYaw() + ", " + location.getPitch() : null;
	}
	
	public static String locationToBasic(Location location){
		return location != null ? location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() : null;
	}
}
