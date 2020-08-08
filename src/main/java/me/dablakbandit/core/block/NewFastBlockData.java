/*
 * Copyright (c) 2020 Ashley Thew
 */

package me.dablakbandit.core.block;

import org.bukkit.Location;
import org.bukkit.Material;

import me.dablakbandit.core.block.advanced.FastChunk;
import me.dablakbandit.core.block.advanced.FastWorld;

public class NewFastBlockData extends FastBlockData{
	
	private FastWorld		fastWorld;
	private FastChunk		fastChunk;
	private FastLocation	fastLocation;
	private Object			blockPosition, blockData, block;
	private Material		material;
	
	public NewFastBlockData(FastWorld world, FastChunk fastChunk, FastLocation location){
		super(null, null, null, null, null, (byte)0);
		this.fastWorld = world;
		this.fastChunk = fastChunk;
		this.fastLocation = location;
	}
	
	public FastChunk getFastChunk(){
		return fastChunk;
	}
	
	public FastLocation getFastLocation(){
		return fastLocation;
	}
	
	public Location getBukkitLocation(){
		return fastLocation.getLocation();
	}
	
	public Object getBlockPosition(){
		return blockPosition != null ? blockPosition : (blockPosition = newBlockPosition(fastLocation.getBlockX(), fastLocation.getBlockY(), fastLocation.getBlockZ()));
	}
	
	public Object getBlockData(){
		return blockData != null ? blockData : (blockData = getBlockData(getBlockPosition()));
	}
	
	public Object getBlockData(Object blockPosition){
		return fastChunk.getBlockData(blockPosition);
	}
	
	public Object getBlock(){
		return block != null ? block : (block = getBlock(getBlockData()));
	}
	
	public Object getBlock(Object blockData){
		return getBlockFromData(blockData);
	}
	
	public Material getMaterial(){
		return getMaterial(getBlock());
	}
	
	public Material getMaterial(Object block){
		return getMaterialFromBlock(block);
	}
	
	public Location getLocation(){
		return getBukkitLocation();
	}
	
	public Material getType(){
		return material != null ? material : (material = getMaterial());
	}
	
	public byte getData(){
		return 0;
	}
	
	public Object getWorld(){
		return fastWorld.getNMSWorld();
	}
	
}
