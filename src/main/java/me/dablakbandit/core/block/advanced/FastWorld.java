package me.dablakbandit.core.block.advanced;

import org.bukkit.*;
import org.bukkit.entity.Player;

import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.block.FastBlockData;
import me.dablakbandit.core.block.advanced.objectmap.ObjectMap;

public class FastWorld extends FastBase{
	
	protected final ObjectMap<FastChunk>	chunks	= ObjectMap.getMap();
	
	protected World							world;
	protected Object						nms_world;
	
	public FastWorld(World world){
		this.world = world;
		try{
			nms_world = world_method_get_handle.invoke(world);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public World getWorld(){
		return world;
	}
	
	public void invalidate(long a){
		chunks.remove(a);
	}
	
	public Object getNMSWorld(){
		return nms_world;
	}
	
	public FastChunk getChunkAt(int x, int z) throws Exception{
		final Object cps = world_get_chunk_provider_server.invoke(nms_world, null);
		Long check = a(x, z);
		if(!(boolean)cps_is_loaded.invoke(cps, x, z) || !chunks.containsKey(check)){
			Bukkit.getScheduler().runTask(CorePlugin.getInstance(), new Runnable(){
				@Override
				public void run(){
					try{
						cps_get_chunk_at.invoke(cps, x, z);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			while(!(boolean)cps_is_loaded.invoke(cps, x, z)){
			}
			Object chunk = cps_get_chunk_at.invoke(cps, x, z);
			FastChunk fc = new FastChunk(chunk);
			chunks.put(check, fc);
			return fc;
		}
		FastChunk fc = chunks.get(check);
		fc.updateLastAccessed();
		return fc;
	}
	
	public FastBlockData getFastDataAt(int x, int y, int z){
		try{
			FastChunk fc = getChunkAt(x >> 4, z >> 4);
			Object nms_chunk = fc.getNMSChunk();
			Object nms_bp = con_block_position.newInstance(x, y, z);
			Object nms_ibd = fc.getBlockData(nms_bp);
			Object nms_block = iblock_method_get_block.invoke(nms_ibd);
			Material m = (Material)cmn_method_get_material.invoke(null, nms_block);
			byte b = 0;
			if(has_block_method_to_legacy_data){
				b = ((Integer)block_method_to_legacy_data.invoke(nms_block, nms_ibd)).byteValue();
			}
			return new FastBlockData(new Location(world, x, y, z), nms_world, nms_block, nms_ibd, m, b);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getHighestYAt(int x, int z){
		try{
			FastChunk fc = getChunkAt(x >> 4, z >> 4);
			return fc.getHighestBlockAt(x & 15, z & 15);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	protected int getLower(int x, int z){
		if(x < z){ return x; }
		return z;
	}
	
	protected int getGreater(int x, int z){
		if(x < z){ return z; }
		return x;
	}
	
	public void updateLight(int Blockx1, int Blockz1, int Blockx2, int Blockz2){
		int cx1 = getGreater(Blockx1, Blockx2) >> 4;
		int cz1 = getGreater(Blockz1, Blockz2) >> 4;
		int cx2 = getLower(Blockx1, Blockx2) >> 4;
		int cz2 = getLower(Blockz1, Blockz2) >> 4;
		try{
			for(int i = cx2; i <= cx1; i++){
				for(int j = cz2; j <= cz1; j++){
					Object nms_chunk = getChunkAt(i, j).getNMSChunk();
					updateLight(nms_chunk);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void updateLight(Location from, Location to){
		updateLight(from.getBlockX(), from.getBlockZ(), to.getBlockX(), to.getBlockZ());
	}
	
	private static void updateLight(Object nms_chunk) throws Exception{
		chunk_method_init_lighting.invoke(nms_chunk);
	}
	
	public void updateChunks(int Blockx1, int Blockz1, int Blockx2, int Blockz2){
		int cx1 = getGreater(Blockx1, Blockx2) >> 4;
		int cz1 = getGreater(Blockz1, Blockz2) >> 4;
		int cx2 = getLower(Blockx1, Blockx2) >> 4;
		int cz2 = getLower(Blockz1, Blockz2) >> 4;
		try{
			for(int i = cx2; i <= cx1; i++){
				for(int j = cz2; j <= cz1; j++){
					updateChunk(i, j);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void updateChunks(Location from, Location to){
		updateChunks(from.getBlockX(), from.getBlockZ(), to.getBlockX(), to.getBlockZ());
	}
	
	private void updateChunk(int x, int z) throws Exception{
		Object nms_chunk = getChunkAt(x, z).getNMSChunk();
		Object packet = con_packet_play_out_map_chunk.newInstance(nms_chunk, 65535);
		for(Player p : world.getPlayers()){
			Object nms_player = player_method_get_handle.invoke(p);
			Object con = player_field_player_connection.get(nms_player);
			player_connection_method_send_packet.invoke(con, packet);
		}
	}
	
	public void updateChunksAndLight(int Blockx1, int Blockz1, int Blockx2, int Blockz2){
		int cx1 = getGreater(Blockx1, Blockx2) >> 4;
		int cz1 = getGreater(Blockz1, Blockz2) >> 4;
		int cx2 = getLower(Blockx1, Blockx2) >> 4;
		int cz2 = getLower(Blockz1, Blockz2) >> 4;
		try{
			for(int i = cx2; i <= cx1; i++){
				for(int j = cz2; j <= cz1; j++){
					Chunk chunk = world.getChunkAt(i, j);
					Object nms_chunk = craft_chunk_method_get_handle.invoke(chunk);
					updateLight(nms_chunk);
					updateChunk(i, j);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void updateChunksAndLight(Location from, Location to){
		updateChunksAndLight(from.getBlockX(), from.getBlockZ(), to.getBlockX(), to.getBlockZ());
	}
	
	public Material getMaterialAt(Location loc){
		return getMaterialAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	public Material getMaterialAt(int px, int py, int pz){
		try{
			int x = px >> 4;
			int z = pz >> 4;
			Object nms_chunk = getChunkAt(x, z);
			Object nms_bp = con_block_position.newInstance(px, py, pz);
			
			Object nms_ibd = chunk_method_get_block_data.invoke(nms_chunk, nms_bp);
			Object nms_block = iblock_method_get_block.invoke(nms_ibd);
			return (Material)cmn_method_get_material.invoke(null, nms_block);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void setBlockFast(Location l, int blockId, byte data){
		setBlockFast(l.getBlockX(), l.getBlockY(), l.getBlockZ(), blockId, data);
	}
	
	public void setBlockFast(Location l, Material m, byte data){
		setBlockFast(l.getBlockX(), l.getBlockY(), l.getBlockZ(), m.getId(), data);
	}
	
	public void setBlockFast(int x, int y, int z, int blockId, byte data){
		try{
			Object nms_chunk = getChunkAt(x >> 4, z >> 4).getNMSChunk();
			Object nms_bp = con_block_position.newInstance(x, y, z);
			int combined = blockId + (data << 12);
			Object nms_ibd = block_method_get_combined.invoke(null, combined);
			int i = x & 0xF;
			int j = y;
			int k = z & 0xF;
			int l = k << 4 | i;
			
			int[] height_map = (int[])chunk_method_get_height_map.invoke(nms_chunk);
			
			if(j >= height_map[l] - 1){
				height_map[l] = 64537;
			}
			Object nms_ibd1 = chunk_method_get_block_data.invoke(nms_chunk, nms_bp);
			if(nms_ibd == nms_ibd1){ return; }
			Object nms_block = iblock_method_get_block.invoke(nms_ibd);
			Object nms_block1 = iblock_method_get_block.invoke(nms_ibd1);
			
			Object[] sections = (Object[])chunk_method_get_sections.invoke(nms_chunk);
			
			Object chunk_section = sections[(j >> 4)];
			if(chunk_section == empty_chunksection){
				if(nms_block == block_air){ return; }
				Object world_provider = world_field_block_provider.get(nms_world);
				chunk_section = con_chunk_section.newInstance(j >> 4 << 4, world_method_provider_m.invoke(world_provider));
				sections[(j >> 4)] = chunk_section;
			}
			chunk_section_method_set_type.invoke(chunk_section, i, j & 0xF, k, nms_ibd);
			if(nms_block != nms_block1){
				if((boolean)world_field_client_side.get(nms_world)){
					block_method_remove.invoke(nms_block1, nms_world, nms_bp, nms_ibd1);
				}else if(nms_itile_entity_class.isAssignableFrom(nms_block1.getClass())){
					world_method_s.invoke(nms_world, nms_bp);
				}
			}
			Object block_type = chunk_section_method_get_type.invoke(chunk_section, i, j & 0xF, k);
			Object nms_block2 = iblock_method_get_block.invoke(block_type);
			if(nms_block2 != nms_block){ return; }
			if(nms_itile_entity_class.isAssignableFrom(nms_block1.getClass())){
				Object tile_entity = chunk_method_a.invoke(nms_chunk, nms_bp, check);
				if(tile_entity != null){
					tile_entity_method_invalidate_block_cache.invoke(tile_entity);
				}
			}
			if(!(boolean)world_field_client_side.get(nms_world) && nms_block1 != nms_block && !(boolean)world_field_capture_block_states.get(nms_world) || nms_block__tile_entity_class.isAssignableFrom(nms_block.getClass())){
				block_method_on_place.invoke(nms_block, nms_world, nms_bp, nms_ibd);
			}
			
			if(nms_itile_entity_class.isAssignableFrom(nms_block.getClass())){
				Object tile_entity = chunk_method_a.invoke(nms_chunk, nms_bp, check);
				if(tile_entity == null){
					itile_entity_method_a.invoke(nms_block, nms_world, block_method_to_legacy_data.invoke(nms_block, nms_ibd));
					world_method_set_tile_entity.invoke(nms_world, nms_bp, tile_entity);
				}
				if(tile_entity != null){
					tile_entity_method_invalidate_block_cache.invoke(tile_entity);
				}
			}
			chunk_field_must_save.set(nms_chunk, true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public long a(int var0, int var1){
		return (long)var0 & 4294967295L | ((long)var1 & 4294967295L) << 32;
	}
}
