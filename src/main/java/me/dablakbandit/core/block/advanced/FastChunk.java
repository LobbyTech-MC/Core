/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.block.advanced;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Material;

import me.dablakbandit.core.block.FastBlockData;
import me.dablakbandit.core.block.FastLocation;
import me.dablakbandit.core.block.NewFastBlockData;

public class FastChunk extends FastBase{
	
	protected Object						nms_chunk;
	protected Map<Integer, FastBlockData>	fastData	= new HashMap<>();
	
	public FastChunk(Object nms_chuck){
		this.nms_chunk = nms_chuck;
	}
	
	public Object getNMSChunk(){
		return nms_chunk;
	}
	
	public Integer getHighestBlockAt(int x, int z){
		try{
			return (int)chunk_a.invoke(nms_chunk, Type_WORLD_SURFACE, x, z);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public FastBlockData getBlockData(FastWorld world, int x, int y, int z){
		int tx = x & 15;
		int tz = z & 15;
		int hash = Objects.hash(tx, y, tz);
		FastBlockData data = fastData.get(hash);
		if(data != null){ return data; }
		data = new NewFastBlockData(world, this, new FastLocation(world.getWorld(), x, y, z));
		fastData.put(hash, data);
		return data;
	}
	
	public void updateHighestBlockAt(Object nms_world, int x, int z){
		try{
			int y = 256;
			Object new_ibd;
			do{
				y--;
				Object nms_bp = con_block_position.newInstance(x, y, z);
				new_ibd = getBlockData(nms_bp);
				Object nms_block = iblock_method_get_block.invoke(new_ibd);
				Material m = (Material)cmn_method_get_material.invoke(null, nms_block);
				if(m != Material.AIR){
					break;
				}
			}while(y > 0);
			
			x &= 15;
			z &= 15;
			
			Object[] sections = (Object[])chunk_method_get_sections.invoke(nms_chunk);
			Object chunk_section = sections[(y >> 4)];
			
			Object new_block = iblock_method_get_block.invoke(new_ibd);
			
			if(chunk_section == empty_chunksection){
				if(new_block == block_air){ return; }
				Object world_provider = world_field_block_provider.get(nms_world);
				chunk_section = con_chunk_section.newInstance(y >> 4 << 4, world_method_provider_g.invoke(world_provider));
				sections[(y >> 4)] = chunk_section;
			}
			
			chunk_section_method_set_type.invoke(chunk_section, x, y & 15, z, new_ibd);
			
			Map map = (Map)chunk_height_map.get(nms_chunk);
			height_map_a.invoke(map.get(Type_MOTION_BLOCKING), x, y, z, new_ibd);
			height_map_a.invoke(map.get(Type_MOTION_BLOCKING_NO_LEAVES), x, y, z, new_ibd);
			height_map_a.invoke(map.get(Type_OCEAN_FLOOR), x, y, z, new_ibd);
			height_map_a.invoke(map.get(Type_WORLD_SURFACE), x, y, z, new_ibd);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Object getBlockData(Object block_position){
		try{
			return chunk_method_get_type.invoke(nms_chunk, block_position);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void release(){
		nms_chunk = null;
	}
}
