/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.block.advanced;

import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.block.FastBlockData;
import me.dablakbandit.core.block.advanced.objectmap.ObjectMap;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Level;

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
		FastChunk dc = chunks.remove(a);
		if(dc != null){
			dc.release();
		}
	}
	
	public Object getNMSWorld(){
		return nms_world;
	}
	
	public abstract class ChunkGet implements Callable<Object>{
		
		AtomicReference<Object> chunk = new AtomicReference<>();
		
		void setChunk(Object object){
			chunk.set(object);
		}
		
		@Override
		public Object call() throws Exception{
			return null;
		}
	}
	
	public FastChunk getChunkAt(int x, int z) throws Exception{
		final Object cps = world_get_chunk_provider_server.invoke(nms_world, null);
		Long check = a(x, z);
		FastChunk fastChunk = chunks.get(check);
		if(fastChunk != null){ return fastChunk; }
		if(!(boolean)cps_is_loaded.invoke(cps, x, z) || !chunks.containsKey(check)){
			ChunkGet get = new ChunkGet(){
				@Override
				public Object call(){
					try{
						Chunk c = world.getChunkAt(x, z);
						setChunk(craft_chunk_method_get_handle.invoke(c));
					}catch(Exception e){
					}
					return null;
				}
			};
			Bukkit.getScheduler().callSyncMethod(CorePlugin.getInstance(), get);
			int sleep = 1;
			do{
				try{
					Thread.sleep(sleep);
					sleep += 10;
					if(sleep % 200 == 0){
						CoreLog.error("FAILED RETRIEVING CHUNK " + x + ", " + z);
					}
				}catch(Exception e){
				}
			}while(get.chunk.get() == null);
			Object chunk = get.chunk.get();
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
			return fc.getBlockData(this, x, y, z);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getHighestYAt(int x, int z){
		try{
			FastChunk fc = getChunkAt(x >> 4, z >> 4);
			return fc.getHighestBlockAt(x & 0xF, z & 0xF);
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
	
	public void updateHeightMap(int Blockx1, int Blockz1, int Blockx2, int Blockz2, Consumer<Long> consumer){
		int cx1 = getGreater(Blockx1, Blockx2) >> 4;
		int cz1 = getGreater(Blockz1, Blockz2) >> 4;
		int cx2 = getLower(Blockx1, Blockx2) >> 4;
		int cz2 = getLower(Blockz1, Blockz2) >> 4;
		try{
			int total = (cx1 - cx2 + 1) * (cz1 - cz2 + 1);
			int count = 0;
			broadcast(count + "/" + total);
			for(int i = cx2; i <= cx1; i++){
				for(int j = cz2; j <= cz1; j++){
					int addx = i * 16;
					int addz = j * 16;
					FastChunk fc = getChunkAt(i, j);
					for(int x = 0; x < 16; x++){
						for(int z = 0; z < 16; z++){
							fc.updateHighestBlockAt(nms_world, addx + x, addz + z);
						}
					}
					consumer.accept(a(i, j));
					count++;
					if(count % 100 == 0){
						broadcast(count + "/" + total);
					}
				}
			}
			broadcast("Done.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void broadcast(String message){
		Bukkit.getOnlinePlayers().forEach(pl -> pl.sendMessage(message));
		CorePlugin.getInstance().getLogger().log(Level.INFO, message);
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
			
			Object nms_ibd = chunk_method_get_type.invoke(nms_chunk, nms_bp);
			Object nms_block = iblock_method_get_block.invoke(nms_ibd);
			return (Material)cmn_method_get_material.invoke(null, nms_block);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void setBlockFast(Location loc, Material material) throws Exception{
		setBlockFast(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), material);
	}
	
	public void setBlockFast(int x, int y, int z, Material material) throws Exception{
		Object nms_bp = con_block_position.newInstance(x, y, z);
		
		BlockData md = material.createBlockData();
		Object new_ibd = cbd_get_state.invoke(md);
		// Object new_block = iblock_method_get_block.invoke(new_ibd);
		
		FastChunk fc = getChunkAt(x >> 4, z >> 4);
		Object nms_chunk = fc.getNMSChunk();
		
		// Object old_ibd = fc.getBlockData(nms_bp);
		// Object old_block = iblock_method_get_block.invoke(old_ibd);
		
		/*-if(!(boolean)iblock_method_is_air.invoke(new_ibd) && nms_block_tile_entity_class.isAssignableFrom(new_block.getClass()) && new_block != old_block){
			// Maybe not needed?
		}*/
		setTypeAndData(fc, x, y, z, nms_bp, new_ibd);
	}
	
	protected void setTypeAndData(FastChunk fc, int x, int y, int z, Object nms_bp, Object new_ibd) throws Exception{
		Object nms_chunk = fc.getNMSChunk();
		x &= 15;
		z &= 15;
		int highest = fc.getHighestBlockAt(x, z);
		Object old_ibd = chunk_method_get_type.invoke(nms_chunk, nms_bp);
		if(new_ibd == old_ibd){ return; }
		
		Object new_block = iblock_method_get_block.invoke(new_ibd);
		Object old_block = iblock_method_get_block.invoke(old_ibd);
		
		Object[] sections = (Object[])chunk_method_get_sections.invoke(nms_chunk);
		Object chunk_section = sections[(y >> 4)];
		
		boolean flag1 = false;
		
		if(chunk_section == empty_chunksection){
			if(new_block == block_air){ return; }
			Object world_provider = world_field_block_provider.get(nms_world);
			chunk_section = con_chunk_section.newInstance(y >> 4 << 4, world_method_provider_g.invoke(world_provider));
			sections[(y >> 4)] = chunk_section;
			flag1 = y >= highest;
		}
		chunk_section_method_set_type.invoke(chunk_section, x, y & 0xF, z, new_ibd);
		
		Map map = (Map)chunk_height_map.get(nms_chunk);
		height_map_a.invoke(map.get(Type_MOTION_BLOCKING), x, y, z, new_ibd);
		height_map_a.invoke(map.get(Type_MOTION_BLOCKING_NO_LEAVES), x, y, z, new_ibd);
		height_map_a.invoke(map.get(Type_OCEAN_FLOOR), x, y, z, new_ibd);
		height_map_a.invoke(map.get(Type_WORLD_SURFACE), x, y, z, new_ibd);
		
		world_method_n.invoke(nms_world, nms_bp);
		
		Object cs_block = iblock_method_get_block.invoke(chunk_section_method_get_type.invoke(chunk_section, x, y & 15, z));
		if(cs_block != new_block){ return; }
		if(nms_itile_entity_class.isAssignableFrom(old_block.getClass())){
			Object tile_entity = chunk_method_a.invoke(nms_chunk, nms_bp, check);
			if(tile_entity != null){
				tile_entity_method_invalidate_block_cache.invoke(tile_entity);
			}
		}
		if(!(boolean)world_field_client_side.get(nms_world) && old_block != new_block && !(boolean)world_field_capture_block_states.get(nms_world) || nms_block_tile_entity_class.isAssignableFrom(new_block.getClass())){
			// iblock_method_on_place.invoke(new_ibd, nms_world, nms_bp, new_ibd);
		}
		
		if(nms_itile_entity_class.isAssignableFrom(new_block.getClass())){
			Object tile_entity = chunk_method_a.invoke(nms_chunk, nms_bp, check);
			if(tile_entity == null){
				itile_entity_method_a.invoke(new_block, nms_world);
				world_method_set_tile_entity.invoke(nms_world, nms_bp, tile_entity);
			}
			if(tile_entity != null){
				tile_entity_method_invalidate_block_cache.invoke(tile_entity);
			}
		}
		chunk_field_must_save.set(nms_chunk, true);
	}
	
	static long elegant(long x, long z){
		return x < z ? z * z + x : x * x + x + z;
	}
	
	public static long a(long x, long z){
		if(x < 0){
			if(z < 0){ return 3 + 4 * elegant(-x - 1, -z - 1); }
			return 2 + 4 * elegant(-x - 1, z);
		}
		if(z < 0){ return 1 + 4 * elegant(x, -z - 1); }
		return 4 * elegant(x, z);
	}
}
