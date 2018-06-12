package me.dablakbandit.core.block.advanced;

public class FastChunk extends FastBase{
	
	protected Object nms_chunk;
	
	public FastChunk(Object nms_check){
		this.nms_chunk = nms_check;
	}
	
	public Object getNMSChunk(){
		return nms_chunk;
	}
	
	public Integer getHighestBlockAt(int x, int z){
		try{
			return (int)chunk_b.invoke(nms_chunk, x, z);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getBlockData(Object block_position) throws Exception{
		return chunk_method_get_block_data.invoke(nms_chunk, block_position);
	}
}
