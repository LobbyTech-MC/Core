/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.block.advanced_legacy;

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
			if(has_nms_height_map){
				return (int)chunk_b.invoke(nms_chunk, Type_MOTION_BLOCKING, x, z);
			}else{
				return (int)chunk_b.invoke(nms_chunk, x, z);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getBlockData(Object block_position) throws Exception{
		return chunk_method_get_block_data.invoke(nms_chunk, block_position);
	}
}
