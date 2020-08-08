package me.dablakbandit.core.block;

import org.bukkit.Location;
import org.bukkit.Material;

import me.dablakbandit.core.block.advanced.FastBase;

public class FastBlockData extends FastBase{
	
	protected Location	location;
	protected Material	material;
	protected byte		data;
	protected Object	world, block, block_data;
	
	public FastBlockData(Location location, Object world, Object block, Object block_data, Material material, byte data){
		this.location = location;
		this.material = material;
		this.data = data;
		this.world = world;
		this.block = block;
		this.block_data = block_data;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public Material getType(){
		return material;
	}
	
	public byte getData(){
		return data;
	}
	
	public Object getWorld(){
		return world;
	}
	
	public Object getBlock(){
		return block;
	}
	
	public Object getBlockData(){
		return block_data;
	}
	
	public Object getBlockPosition(){
		return newBlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
}
