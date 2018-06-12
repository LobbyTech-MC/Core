package me.dablakbandit.core.block.advanced.objectmap;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class NormalMap<T>extends ObjectMap<T>{
	
	protected final Long2ObjectMap<T> chunks = new Long2ObjectOpenHashMap(8192);
	
	@Override
	public T get(long l){
		return chunks.get(l);
	}
	
	@Override
	public void put(long l, T t){
		chunks.put(l, t);
	}
	
	@Override
	public boolean containsKey(long check){
		return chunks.containsKey(check);
	}
}
