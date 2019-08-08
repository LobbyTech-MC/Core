/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.block.advanced;

public class FastChunk extends FastBase{
	
	protected Object nms_chunk;
	
	public FastChunk(Object nms_chuck){
		this.nms_chunk = nms_chuck;
	}
	
	public Object getNMSChunk(){
		return nms_chunk;
	}
	
	public Integer getHighestBlockAt(int x, int z){
		try{
			return (int)chunk_a.invoke(nms_chunk, Type_MOTION_BLOCKING, x, z);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getBlockData(Object block_position) throws Exception{
		return chunk_method_get_type.invoke(nms_chunk, block_position);
	}
	
	public void release(){
		nms_chunk = null;
	}
}
