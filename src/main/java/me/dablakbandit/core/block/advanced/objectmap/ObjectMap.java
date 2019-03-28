/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.block.advanced.objectmap;

import java.util.Collection;

import me.dablakbandit.core.utils.NMSUtils;

public abstract class ObjectMap<T>{
	
	public abstract T get(long l);
	
	public abstract void remove(long l);
	
	public abstract Collection<T> values();
	
	public abstract void put(long l, T t);
	
	public abstract boolean containsKey(long check);
	
	public static ObjectMap getMap(){
		try{
			Class<?> c = NMSUtils.getClassWithException("org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectMap");
			if(c == null){ throw new Exception("No."); }
			return new OBCMap();
		}catch(Exception e){
			return new NormalMap();
		}
	}
}
