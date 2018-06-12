package me.dablakbandit.core.block.advanced;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.dablakbandit.core.utils.NMSUtils;

public abstract class FastBase{
	
	protected long last_accessed = System.currentTimeMillis();
	
	public long getLastAccesed(){
		return last_accessed;
	}
	
	protected void updateLastAccessed(){
		last_accessed = System.currentTimeMillis();
	}
	
	protected static Class<?>		nms_world_class								= NMSUtils.getNMSClass("World");
	protected static Class<?>		nms_world_server_class						= NMSUtils.getNMSClass("WorldServer");
	protected static Class<?>		nms_player_connection_class					= NMSUtils.getNMSClass("PlayerConnection");
	protected static Class<?>		nms_packet_class							= NMSUtils.getNMSClass("Packet");
	protected static Class<?>		nms_entity_player_class						= NMSUtils.getNMSClass("EntityPlayer");
	protected static Class<?>		obc_craft_world_class						= NMSUtils.getOBCClass("CraftWorld");
	protected static Class<?>		obc_craft_chunk_class						= NMSUtils.getOBCClass("CraftChunk");
	protected static Class<?>		obc_craft_player_class						= NMSUtils.getOBCClass("entity.CraftPlayer");
	protected static Class<?>		nms_chunk_class								= NMSUtils.getNMSClass("Chunk");
	protected static Class<?>		nms_block_class								= NMSUtils.getNMSClass("Block");
	protected static Class<?>		nms_blocks_class							= NMSUtils.getNMSClass("Blocks");
	protected static Class<?>		nms_block__tile_entity_class				= NMSUtils.getNMSClass("BlockTileEntity");
	protected static Class<?>		nms_block_position_class					= NMSUtils.getNMSClass("BlockPosition");
	protected static Class<?>		nms_base_block_position_class				= NMSUtils.getNMSClass("BaseBlockPosition");
	protected static Class<?>		nms_iblock_data_class						= NMSUtils.getNMSClass("IBlockData");
	protected static Class<?>		nms_chunk_section_class						= NMSUtils.getNMSClass("ChunkSection");
	protected static Class<?>		nms_world_provider_class					= NMSUtils.getNMSClass("WorldProvider");
	protected static Class<?>		nms_itile_entity_class						= NMSUtils.getNMSClass("ITileEntity");
	protected static Class<?>		nms_tile_entity_class						= NMSUtils.getNMSClass("TileEntity");
	protected static Class<?>		nms_packet_play_out_map_chunk_class			= NMSUtils.getNMSClass("PacketPlayOutMapChunk");
	protected static Class<?>		nms_chunk_provider_server					= NMSUtils.getNMSClass("ChunkProviderServer");
	protected static Class<?>		nms_enum_tile_entity_state					= NMSUtils.getInnerClass(nms_chunk_class, "EnumTileEntityState");
	
	protected static Method			player_method_get_handle					= NMSUtils.getMethod(obc_craft_player_class, "getHandle");
	protected static Method			craft_chunk_method_get_handle				= NMSUtils.getMethod(obc_craft_chunk_class, "getHandle");
	protected static Method			player_connection_method_send_packet		= NMSUtils.getMethod(nms_player_connection_class, "sendPacket", nms_packet_class);
	protected static Method			world_method_get_handle						= NMSUtils.getMethod(obc_craft_world_class, "getHandle");
	protected static Method			world_method_get_chunk						= NMSUtils.getMethod(nms_world_class, "getChunkAt", int.class, int.class);
	protected static Method			world_method_s								= NMSUtils.getMethod(nms_world_class, "s", nms_block_position_class);
	protected static Method			world_method_c								= NMSUtils.getMethod(nms_world_class, "c", int.class, int.class);
	protected static Method			world_method_set_tile_entity				= NMSUtils.getMethod(nms_world_class, "setTileEntity", nms_block_position_class, nms_tile_entity_class);
	protected static Method			world_get_chunk_provider_server				= NMSUtils.getMethod(nms_world_server_class, "getChunkProviderServer");
	protected static Method			cps_is_loaded								= NMSUtils.getMethod(nms_chunk_provider_server, "isLoaded", int.class, int.class);
	protected static Method			cps_get_chunk_at							= NMSUtils.getMethod(nms_chunk_provider_server, "getChunkAt", int.class, int.class);
	protected static Method			block_method_get_combined					= NMSUtils.getMethod(nms_block_class, "getByCombinedId", int.class);
	protected static Method			block_method_to_legacy_data					= NMSUtils.getMethod(nms_block_class, "toLegacyData", nms_iblock_data_class);
	protected static Method			block_method_on_place						= NMSUtils.getMethod(nms_block_class, "onPlace", nms_world_class, nms_block_position_class, nms_iblock_data_class);
	protected static Method			iblock_method_get_block						= NMSUtils.getMethod(nms_iblock_data_class, "getBlock");
	protected static Method			tile_entity_method_invalidate_block_cache	= NMSUtils.getMethod(nms_tile_entity_class, "invalidateBlockCache");
	protected static Method			itile_entity_method_a						= NMSUtils.getMethod(nms_itile_entity_class, "a", nms_world_class, int.class);
	protected static Method			chunk_method_get_height_map					= NMSUtils.getMethod(nms_chunk_class, "r");
	protected static Method			chunk_method_get_block_data					= NMSUtils.getMethod(nms_chunk_class, "getBlockData", new Class[]{ nms_block_position_class });
	protected static Method			chunk_method_get_sections					= NMSUtils.getMethod(nms_chunk_class, "getSections");
	protected static Method			chunk_method_init_lighting					= NMSUtils.getMethod(nms_chunk_class, "initLighting");
	protected static Method			chunk_method_a								= NMSUtils.getMethod(nms_chunk_class, "a", nms_block_position_class, nms_enum_tile_entity_state);
	protected static Method			world_method_provider_m						= NMSUtils.getMethod(nms_world_provider_class, "m");
	protected static Method			chunk_section_method_set_type				= NMSUtils.getMethod(nms_chunk_section_class, "setType", int.class, int.class, int.class, nms_iblock_data_class);
	protected static Method			chunk_section_method_get_type				= NMSUtils.getMethod(nms_chunk_section_class, "getType", int.class, int.class, int.class);
	protected static Method			block_method_remove							= NMSUtils.getMethod(nms_block_class, "remove", nms_world_class, nms_block_position_class, nms_iblock_data_class);
	protected static Method			block_method_get_id							= NMSUtils.getMethod(nms_block_class, "getId", nms_block_class);
	protected static Method			chunk_b										= NMSUtils.getMethod(nms_chunk_class, "b", int.class, int.class);
	
	protected static Constructor<?>	con_block_position							= NMSUtils.getConstructor(nms_block_position_class, int.class, int.class, int.class);
	protected static Constructor<?>	con_chunk_section							= NMSUtils.getConstructor(nms_chunk_section_class, int.class, boolean.class);
	protected static Constructor<?>	con_packet_play_out_map_chunk				= NMSUtils.getConstructor(nms_packet_play_out_map_chunk_class, nms_chunk_class, int.class);
	
	protected static Field			chunksection_empty							= NMSUtils.getField(nms_chunk_class, "a");
	protected static Field			block_field_air								= NMSUtils.getField(nms_blocks_class, "AIR");
	protected static Field			world_field_block_provider					= NMSUtils.getField(nms_world_class, "worldProvider");
	protected static Field			world_field_client_side						= NMSUtils.getField(nms_world_class, "isClientSide");
	protected static Field			world_field_capture_block_states			= NMSUtils.getField(nms_world_class, "captureBlockStates");
	protected static Field			chunk_field_must_save						= NMSUtils.getField(nms_chunk_class, "mustSave");
	protected static Field			player_field_player_connection				= NMSUtils.getField(nms_entity_player_class, "playerConnection");
	
	protected static Object			empty_chunksection							= NMSUtils.getFieldValue(chunksection_empty, null);
	protected static Object			block_air									= NMSUtils.getFieldValue(block_field_air, null);
	protected static Object			check										= NMSUtils.getEnum("CHECK", nms_enum_tile_entity_state);
}
