package me.dablakbandit.core.utils;

import static me.dablakbandit.core.utils.NMSUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;

import io.netty.channel.Channel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class PacketUtils{
	
	public static Class<?>				classEntity							= NMSUtils.getNMSClass("Entity"), classEntityHuman = NMSUtils.getNMSClass("EntityHuman"), classEntityPlayer = NMSUtils.getNMSClass("EntityPlayer"), classPlayerConnection = NMSUtils.getNMSClassSilent("PlayerConnection"),
	classPacket = NMSUtils.getNMSClassSilent("Packet"), classNetworkManager = NMSUtils.getNMSClassSilent("NetworkManager");
	
	public static Class<?>				classMinecraftKey					= NMSUtils.getNMSClass("MinecraftKey");
	public static Class<?>				classItemStack						= NMSUtils.getNMSClass("ItemStack");
	public static Class<?>				enumDificulty						= NMSUtils.getNMSClass("EnumDifficulty");
	public static Class<?>				classPlayerInteractManager			= NMSUtils.getNMSClass("PlayerInteractManager");
	
	public static Field					fieldPlayerInteractManagerGamemode	= NMSUtils.getField(classPlayerInteractManager, "gamemode");
	public static Field					fieldEntityPlayerInteractManager	= NMSUtils.getField(classEntityPlayer, "playerInteractManager");
	
	public static Field					fieldChannel						= NMSUtils.getFirstFieldOfType(classNetworkManager, Channel.class), fieldNetworkManager = NMSUtils.getFirstFieldOfType(classPlayerConnection, classNetworkManager),
	fieldConnection = NMSUtils.getFirstFieldOfType(classEntityPlayer, classPlayerConnection), fieldGameProfile = NMSUtils.getFirstFieldOfType(classEntityHuman, GameProfile.class);
	
	public static Field					fieldMinecraftKeyB					= NMSUtils.getField(classMinecraftKey, "b");
	
	@SuppressWarnings("rawtypes")
	public static Map<Class, Method>	mapGetHandle						= new HashMap<Class, Method>();
	
	public static Object getHandle(Entity entity) throws Exception{
		Method m = mapGetHandle.get(entity.getClass());
		if(m == null){
			m = NMSUtils.getMethod(entity.getClass(), "getHandle");
			mapGetHandle.put(entity.getClass(), m);
		}
		return m.invoke(entity);
	}
	
	public static class Animation{
		
		public static Class<?>			classPacketPlayOutAnimation			= NMSUtils.getNMSClass("PacketPlayOutAnimation");
		public static Constructor<?>	constructorPacketPlayOutAnimation	= NMSUtils.getConstructor(classPacketPlayOutAnimation, classEntity, int.class);
		
		public static Object getPacket(Entity entity, int i) throws Exception{
			return constructorPacketPlayOutAnimation.newInstance(getHandle(entity), i);
		}
		
	}
	
	public static class MapChunk{
		
		public static Class<?> classPacketPlayOutMapChunk = NMSUtils.getNMSClass("PacketPlayOutMapChunk");
		
	}
	
	public static class Position{
		
		public static Class<?> classPacketPlayOutPosition = NMSUtils.getNMSClass("PacketPlayOutPosition");
		
	}
	
	public static class SpawnEntityLiving{
		
		public static Class<?> classPacketPlayOutSpawnEntityLiving = NMSUtils.getNMSClass("PacketPlayOutSpawnEntityLiving");
		
	}
	
	public static class CloseWindow{
		
		public static Class<?> classPacketPlayInCloseWindow = NMSUtils.getNMSClass("PacketPlayInCloseWindow");
		
	}
	
	public static class Sound{
		
		public static Class<?>	classPacketPlayOutNamedSoundEffect	= NMSUtils.getNMSClass("PacketPlayOutNamedSoundEffect");
		public static Field		fieldSoundEffect					= NMSUtils.getFirstFieldOfType(classPacketPlayOutNamedSoundEffect, classSoundEffect), fieldMinecraftKey = NMSUtils.getFirstFieldOfType(classSoundEffect, classMinecraftKey);
		
		public static void sendBreakSound(Player player, Block block) throws Exception{
			Object nmsblock = NMSUtils.getBlockHandle(block);
			Object step = fieldStepSound.get(nmsblock);
			if(fieldStepSound != null){
				Object sound = fieldBreakSound.get(step);
				Object key = fieldMinecraftKey.get(sound);
				String name = (String)fieldMinecraftKeyB.get(key);
				sendPlay(player, name);
			}else{
				sendPlay(player, (String)methodGetBreakSound.invoke(step));
			}
		}
		
		public static String getSoundName(Object pponse) throws Exception{
			return (String)fieldMinecraftKeyB.get(fieldMinecraftKey.get(fieldSoundEffect.get(pponse)));
		}
		
		public static void sendPlay(Player player, String name, Location loc, float volume, float pitch){
			player.playSound(loc, name, volume, pitch);
		}
		
		public static void sendPlay(Player player, String name, Location loc, float volume){
			sendPlay(player, name, loc, volume, 1.0F);
		}
		
		public static void sendPlay(Player player, String name, Location loc){
			sendPlay(player, name, loc, 1.0F);
		}
		
		public static void sendPlay(Player player, String name){
			Location loc = player.getLocation();
			sendPlay(player, name, loc);
		}
	}
	
	public static class Chat{
		
		public static Class<?>	classPacketPlayOutChat	= NMSUtils.getNMSClass("PacketPlayOutChat");
		public static Class<?>	classPacketPlayInChat	= NMSUtils.getNMSClass("PacketPlayInChat");
		public static Class<?>	classChatMessageType	= NMSUtils.getNMSClassSilent("ChatMessageType");
		public static Field		fieldPacketPlayOutChatB	= NMSUtils.getFirstFieldOfType(classPacketPlayOutChat, classChatMessageType != null ? classChatMessageType : byte.class);
		public static Field		fieldPacketPlayInChatA	= NMSUtils.getField(classPacketPlayInChat, "a");
		
		private static Class<?>	classIChatBaseComponent	= NMSUtils.getNMSClass("IChatBaseComponent");
		private static Class<?>	classChatSerializer		= NMSUtils.getNMSClass("ChatSerializer", "IChatBaseComponent");
		
		private static Method	methodChatSerializerA	= NMSUtils.getMethod(classChatSerializer, "a", classIChatBaseComponent);
		private static Field	fieldPacketPlayOutChatA	= NMSUtils.getField(classPacketPlayOutChat, "a");
		private static Field	fieldPacketPlayOutChatC	= NMSUtils.getField(classPacketPlayOutChat, "components");
		
		public static String getMessage(Object packet) throws Exception{
			String s = deSerialize(fieldPacketPlayOutChatA.get(packet));
			if(s.equals("null")){
				Object bc = fieldPacketPlayOutChatC.get(packet);
				if(bc != null){
					s = ComponentSerializer.toString((BaseComponent[])bc);
				}
			}
			return s;
		}
		
		public static String deSerialize(Object ichat) throws Exception{
			return (String)methodChatSerializerA.invoke(null, ichat);
		}
		
		public static String getMessageIn(Object packet) throws Exception{
			return (String)fieldPacketPlayInChatA.get(packet);
		}
		
		@SuppressWarnings("rawtypes")
		public static byte getByte(Object packet) throws Exception{
			Object o = fieldPacketPlayOutChatB.get(packet);
			if(o.getClass().equals(classChatMessageType)){ return (byte)((Enum)o).ordinal(); }
			return (byte)o;
		}
	}
	
	public static class ResourcePack{
		
		public static Class<?>			classPacketPlayOutResourcePackSend	= NMSUtils.getNMSClass("PacketPlayOutResourcePackSend");
		public static Constructor<?>	conPacketPlayOutResourcePackSend	= NMSUtils.getConstructor(classPacketPlayOutResourcePackSend, String.class, String.class);
		
		public static void sendResourcePack(Player player, String url, String hash) throws Exception{
			Object packet = conPacketPlayOutResourcePackSend.newInstance(url, hash);
			sendPacket(player, packet);
		}
		
		public static void sendResourcePack(Collection<? extends Player> players, String url, String hash) throws Exception{
			Object packet = conPacketPlayOutResourcePackSend.newInstance(url, hash);
			sendPacket(players, packet);
		}
	}
	
	public static class TabComplete{
		
		public static Class<?>	classPacketPlayOutTabComplete		= NMSUtils.getNMSClass("PacketPlayOutTabComplete");
		public static Class<?>	classPacketPlayInTabComplete		= NMSUtils.getNMSClass("PacketPlayInTabComplete");
		public static Field		fieldPacketPlayOutTabCompletea		= NMSUtils.getField(classPacketPlayOutTabComplete, "a");
		public static Field		fieldPacketPlayInTabCompleteString	= NMSUtils.getFirstFieldOfType(classPacketPlayInTabComplete, String.class);
		
		public static String[] getOutA(Object packet) throws Exception{
			return (String[])fieldPacketPlayOutTabCompletea.get(packet);
		}
		
		public static String getInString(Object packet) throws Exception{
			return (String)fieldPacketPlayInTabCompleteString.get(packet);
		}
	}
	
	public static class PlayerMove{
		
		public static Class<?> classPacketPlayInPosition = NMSUtils.getNMSClass("PacketPlayInPosition", "PacketPlayInFlying");
		
	}
	
	public static class EntityMetadata{
		
		public static Class<?>			classPacketPlayOutEntityMetadata		= NMSUtils.getNMSClass("PacketPlayOutEntityMetadata");
		public static Class<?>			classDataWatcher						= NMSUtils.getNMSClass("DataWatcher");
		public static Class<?>			classDataWatcherItem					= NMSUtils.getInnerClass(classDataWatcher, "Item");
		
		public static Field				fieldPacketPlayOutEntityMetadataa		= NMSUtils.getField(classPacketPlayOutEntityMetadata, "a");
		public static Field				fieldPacketPlayOutEntityMetadatab		= NMSUtils.getField(classPacketPlayOutEntityMetadata, "b");
		
		public static Constructor<?>	constructorPacketPlayOutEntityMetadata	= NMSUtils.getConstructor(classPacketPlayOutEntityMetadata);
		public static Constructor<?>	constructorPacketPlayOutEntityMetadata2	= NMSUtils.getConstructor(classPacketPlayOutEntityMetadata, int.class, classDataWatcher, boolean.class);
		public static Constructor<?>	constructorDataWatcherItem				= classDataWatcherItem.getConstructors()[0];
		
		public static Field				fieldDataWatcherItema					= NMSUtils.getField(classDataWatcherItem, "a");
		public static Field				fieldDataWatcherItemb					= NMSUtils.getField(classDataWatcherItem, "b");
		public static Field				fieldDataWatcherb						= NMSUtils.getField(classDataWatcher, "d");
		
		public static Method			methodGetDataWatcher					= NMSUtils.getMethod(classEntity, "getDataWatcher");
		
		public static int getA(Object packet) throws Exception{
			return (int)fieldPacketPlayOutEntityMetadataa.get(packet);
		}
		
		@SuppressWarnings("unchecked")
		public static List<Object> getB(Object packet) throws Exception{
			return (List<Object>)fieldPacketPlayOutEntityMetadatab.get(packet);
		}
		
		public static Object getItemB(Object item) throws Exception{
			return fieldDataWatcherItemb.get(item);
		}
		
		public static void setItemB(Object item, Object set) throws Exception{
			fieldDataWatcherItemb.set(item, set);
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static Object clonePacket(Object packet) throws Exception{
			Object o = constructorPacketPlayOutEntityMetadata.newInstance();
			fieldPacketPlayOutEntityMetadataa.set(o, fieldPacketPlayOutEntityMetadataa.get(packet));
			List l = getB(packet);
			List n = new ArrayList();
			for(Object i : l){
				n.add(constructorDataWatcherItem.newInstance(fieldDataWatcherItema.get(i), fieldDataWatcherItemb.get(i)));
			}
			fieldPacketPlayOutEntityMetadatab.set(o, n);
			return o;
		}
		
		public static Object getPacket(Entity entity) throws Exception{
			int id = entity.getEntityId();
			Object nmsEntity = NMSUtils.getHandle(entity);
			Object data = methodGetDataWatcher.invoke(nmsEntity);
			Object packet = constructorPacketPlayOutEntityMetadata2.newInstance(id, data, true);
			return packet;
		}
		
	}
	
	public static class PlayerInfo{
		
		public static Class<?>			classPacketPlayOutPlayerInfo	= NMSUtils.getNMSClass("PacketPlayOutPlayerInfo");
		public static Constructor<?>	conPacketPlayOutPlayerInfo		= NMSUtils.getConstructor(classPacketPlayOutPlayerInfo);
		public static Class<?>			enumPlayerInfoAction			= NMSUtils.getNMSClass("EnumPlayerInfoAction", "PacketPlayOutPlayerInfo");
		public static Class<?>			classPlayerInfoData				= NMSUtils.getNMSClass("PlayerInfoData", "PacketPlayOutPlayerInfo");
		
		public static Constructor<?>	conPlayerInfoData				= classPlayerInfoData.getConstructors()[0];
		
		public static Field				fieldPacketPlayOutPlayerInfoA	= NMSUtils.getField(classPacketPlayOutPlayerInfo, "a");
		public static Field				fieldPacketPlayOutPlayerInfoB	= NMSUtils.getField(classPacketPlayOutPlayerInfo, "b");
		public static Field				fieldEntityPlayerPing			= NMSUtils.getField(classEntityPlayer, "ping");
		public static Field				fieldEntityPlayerListName		= NMSUtils.getField(classEntityPlayer, "listName");
		public static Field				fieldPlayerInfoDataGameProfile	= NMSUtils.getFirstFieldOfType(classPlayerInfoData, GameProfile.class);
		
		public static Object			enumRemovePlayer				= NMSUtils.getEnum("REMOVE_PLAYER", enumPlayerInfoAction), enumAddPlayer = NMSUtils.getEnum("ADD_PLAYER", enumPlayerInfoAction);
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static Object getRemovePacket(Player player){
			try{
				Object ep = getHandle(player);
				Object packet = conPacketPlayOutPlayerInfo.newInstance();
				fieldPacketPlayOutPlayerInfoA.set(packet, enumRemovePlayer);
				Object data = conPlayerInfoData.newInstance(packet, fieldGameProfile.get(ep), fieldEntityPlayerPing.get(ep), fieldPlayerInteractManagerGamemode.get(fieldEntityPlayerInteractManager.get(ep)), fieldEntityPlayerListName.get(ep));
				((List)fieldPacketPlayOutPlayerInfoB.get(packet)).add(data);
				return packet;
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static Object getAddPacket(Player player){
			try{
				Object ep = getHandle(player);
				Object packet = conPacketPlayOutPlayerInfo.newInstance();
				fieldPacketPlayOutPlayerInfoA.set(packet, enumAddPlayer);
				Object data = conPlayerInfoData.newInstance(packet, fieldGameProfile.get(ep), fieldEntityPlayerPing.get(ep), fieldPlayerInteractManagerGamemode.get(fieldEntityPlayerInteractManager.get(ep)), fieldEntityPlayerListName.get(ep));
				((List)fieldPacketPlayOutPlayerInfoB.get(packet)).add(data);
				return packet;
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public static class Respawn{
		
		public static Class<?>			classPacketPlayOutServerRespawn		= NMSUtils.getNMSClass("PacketPlayOutRespawn");
		public static Class<?>			classWorld							= NMSUtils.getNMSClass("World");
		public static Class<?>			classWorldData						= NMSUtils.getNMSClass("WorldData");
		public static Class<?>			classWorldType						= NMSUtils.getNMSClass("WorldType");
		public static Constructor<?>	conPacketPlayOutServerRespawn		= NMSUtils.getConstructor(classPacketPlayOutServerRespawn);
		
		public static Field				fieldPacketPlayOutServerRespawnA	= NMSUtils.getField(classPacketPlayOutServerRespawn, "a");
		public static Field				fieldPacketPlayOutServerRespawnB	= NMSUtils.getField(classPacketPlayOutServerRespawn, "b");
		public static Field				fieldPacketPlayOutServerRespawnC	= NMSUtils.getField(classPacketPlayOutServerRespawn, "c");
		public static Field				fieldPacketPlayOutServerRespawnD	= NMSUtils.getField(classPacketPlayOutServerRespawn, "d");
		
		public static Field				fieldEntityDimension				= NMSUtils.getField(classEntity, "dimension");
		public static Field				fieldEntityWorld					= NMSUtils.getField(classEntity, "world");
		public static Field				fieldWorldWorldData					= NMSUtils.getField(classWorld, "worldData");
		public static Field				fieldWorldDataEnumDificulty			= NMSUtils.getFirstFieldOfType(classWorldData, enumDificulty);
		public static Field				fieldWorldDataWorldType				= NMSUtils.getFirstFieldOfType(classWorldData, classWorldType);
		
		public static Object getPacket(Player player) throws Exception{
			try{
				Object packet = conPacketPlayOutServerRespawn.newInstance();
				Object ep = getHandle(player);
				fieldPacketPlayOutServerRespawnA.set(packet, fieldEntityDimension.get(ep));
				Object wd = fieldWorldWorldData.get(fieldEntityWorld.get(ep));
				fieldPacketPlayOutServerRespawnB.set(packet, fieldWorldDataEnumDificulty.get(wd));
				fieldPacketPlayOutServerRespawnC.set(packet, fieldPlayerInteractManagerGamemode.get(fieldEntityPlayerInteractManager.get(ep)));
				fieldPacketPlayOutServerRespawnD.set(packet, fieldWorldDataWorldType.get(wd));
				return packet;
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public static class EntitySpawn{
		
		public static Class<?>	classPacketPlayOutEntitySpawn	= NMSUtils.getNMSClass("PacketPlayOutSpawnEntity");
		
		public static Field		fieldPacketPlayOutEntitySpawnA	= NMSUtils.getField(classPacketPlayOutEntitySpawn, "a");
	}
	
	public static Field		fieldPlayerConnection	= NMSUtils.getFieldSilent(classEntityPlayer, "playerConnection");
	public static Method	methodSendPacket		= NMSUtils.getMethodSilent(classPlayerConnection, "sendPacket", classPacket);
	
	public static void sendPacket(Player player, Object packet) throws Exception{
		Object ppco = getPlayerConnection(player);
		methodSendPacket.invoke(ppco, packet);
	}
	
	public static void sendPackets(Player player, Collection<?> packets) throws Exception{
		Object entityplayer = getHandle(player);
		Object ppco = fieldPlayerConnection.get(entityplayer);
		for(Object packet : packets){
			methodSendPacket.invoke(ppco, packet);
		}
	}
	
	public static void sendPacket(Collection<? extends Player> players, Object packet) throws Exception{
		for(Player player : players){
			sendPacket(player, packet);
		}
	}
	
	public static void sendPackets(Collection<? extends Player> players, Collection<Object> packets) throws Exception{
		for(Player player : players){
			sendPackets(player, packets);
		}
	}
	
	public static void sendPacketRaw(Collection<Object> cons, Object packet) throws Exception{
		for(Object o : cons){
			methodSendPacket.invoke(o, packet);
		}
	}
	
	public static Object getPlayerConnection(Player player) throws Exception{
		Object entityplayer = getHandle(player);
		return fieldPlayerConnection.get(entityplayer);
	}
	
	public static Class<?>			classBlockPosition		= NMSUtils.getNMSClass("BlockPosition");
	public static Class<?>			classBlock				= NMSUtils.getNMSClass("Block");
	
	public static Method			methodGetX				= NMSUtils.getMethod(classBlockPosition, "getX");
	public static Method			methodGetY				= NMSUtils.getMethod(classBlockPosition, "getY");
	public static Method			methodGetZ				= NMSUtils.getMethod(classBlockPosition, "getZ");
	
	public static Class<?>			classStepSound			= NMSUtils.getInnerClassSilent(classBlock, "StepSound");
	
	public static Method			methodGetBreakSound		= classStepSound == null ? null : NMSUtils.getMethodSilent(classStepSound, "getBreakSound");
	
	public static Class<?>			classSoundEffectType	= NMSUtils.getNMSClassSilent("SoundEffectType");
	public static Class<?>			classSoundEffect		= NMSUtils.getNMSClassSilent("SoundEffect");
	
	public static Field				fieldStepSound			= NMSUtils.getFirstFieldOfType(classBlock, classSoundEffectType == null ? classStepSound : classSoundEffectType);
	public static Field				fieldBreakSound			= classSoundEffectType == null ? null : NMSUtils.getFirstFieldOfType(classSoundEffectType, classSoundEffect);
	public static Field				fieldMinecraftKey		= classSoundEffect == null ? null : NMSUtils.getFirstFieldOfType(classSoundEffect, classMinecraftKey);
	
	public static Constructor<?>	conBlockPosition		= NMSUtils.getConstructor(classBlockPosition, int.class, int.class, int.class);
	
	public static class BlockBreakAnimation{
		
		public static Class<?>			classPacketPlayOutBlockBreakAnimation	= NMSUtils.getNMSClass("PacketPlayOutBlockBreakAnimation");
		
		public static Constructor<?>	conPacketPlayOutBlockBreakAnimation		= NMSUtils.getConstructor(classPacketPlayOutBlockBreakAnimation, int.class, classBlockPosition, int.class);
		
		public static Object getPacket(int id, Block block, int destroy) throws Exception{
			try{
				return conPacketPlayOutBlockBreakAnimation.newInstance(id, conBlockPosition.newInstance(block.getX(), block.getY(), block.getZ()), destroy);
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public static class BlockDig{
		
		public static Class<?>	classPacketPlayInBlockDig	= NMSUtils.getNMSClass("PacketPlayInBlockDig");
		public static Class<?>	classEnumPlayerDigType		= NMSUtils.getInnerClass(classPacketPlayInBlockDig, "EnumPlayerDigType");
		
		public static Field		fieldPacketPlayInBlockDigA	= NMSUtils.getField(classPacketPlayInBlockDig, "a");
		public static Field		fieldPacketPlayInBlockDigC	= NMSUtils.getField(classPacketPlayInBlockDig, "c");
		
		public static Block getBlock(Object packet, World world) throws Exception{
			Object bp = fieldPacketPlayInBlockDigA.get(packet);
			return world.getBlockAt((int)methodGetX.invoke(bp), (int)methodGetY.invoke(bp), (int)methodGetZ.invoke(bp));
		}
		
		@SuppressWarnings("rawtypes")
		public static Enum getEnumPlayerDig(Object packet) throws Exception{
			return (Enum)fieldPacketPlayInBlockDigC.get(packet);
		}
		
	}
	
	public static class Bed{
		
		public static Class<?>			classPacketPlayOutBed	= NMSUtils.getNMSClass("PacketPlayOutBed");
		
		public static Field				fieldPacketPlayOutBedA	= NMSUtils.getField(classPacketPlayOutBed, "a");
		public static Field				fieldPacketPlayOutBedB	= NMSUtils.getField(classPacketPlayOutBed, "b");
		
		public static Constructor<?>	conPacketPlayOutBed		= NMSUtils.getConstructor(classPacketPlayOutBed);
		
		public static Method			methodEntityPlayera		= NMSUtils.getMethod(classEntityPlayer, "a", boolean.class, boolean.class, boolean.class);
		
		public static Object getPacket(int id, Location loc) throws Exception{
			Object packet = conPacketPlayOutBed.newInstance();
			fieldPacketPlayOutBedA.set(packet, id);
			fieldPacketPlayOutBedB.set(packet, conBlockPosition.newInstance(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
			return packet;
		}
	}
	
	public static class KeepAlive{
		
		public static Class<?>	classPacketPlayOutKeepAlive	= NMSUtils.getNMSClass("PacketPlayOutKeepAlive");
		public static Class<?>	classPacketPlayInKeepAlive	= NMSUtils.getNMSClass("PacketPlayInKeepAlive");
		
	}
	
	public static class SetSlot{
		public static Class<?>		classPacketPlayOutSetSlot	= NMSUtils.getNMSClass("PacketPlayOutSetSlot");
		
		public static Field			fieldPacketPlayOutSetSlotA	= NMSUtils.getField(classPacketPlayOutSetSlot, "a");
		public static Field			fieldPacketPlayOutSetSlotB	= NMSUtils.getField(classPacketPlayOutSetSlot, "b");
		public static Field			fieldPacketPlayOutSetSlotC	= NMSUtils.getField(classPacketPlayOutSetSlot, "c");
		
		public static Constructor	conPacketPlayOutSetSlot		= NMSUtils.getConstructor(classPacketPlayOutSetSlot, int.class, int.class, classItemStack);
		
		public static Object getPacket(int window, int slot, ItemStack item) throws Exception{
			return conPacketPlayOutSetSlot.newInstance(window, slot, ItemUtils.getInstance().getNMSCopy(item));
		}
		
		public static int getID(Object packet) throws Exception{
			return (int)fieldPacketPlayOutSetSlotA.get(packet);
		}
		
		public static int getSlot(Object packet) throws Exception{
			return (int)fieldPacketPlayOutSetSlotB.get(packet);
		}
		
		public static Object getItemStack(Object packet) throws Exception{
			return fieldPacketPlayOutSetSlotC.get(packet);
		}
	}
	
	public static class WindowItems{
		public static Class<?>		classPacketPlayOutWindowItems	= NMSUtils.getNMSClass("PacketPlayOutWindowItems");
		
		public static Field			fieldPacketPlayOutWindowItemsA	= NMSUtils.getField(classPacketPlayOutWindowItems, "a");
		public static Field			fieldPacketPlayOutWindowItemsB	= NMSUtils.getField(classPacketPlayOutWindowItems, "b");
		
		public static Class<?>		classNonNullList				= NMSUtils.getNMSClassSilent("NonNullList");
		
		public static Constructor	conNonNullList					= NMSUtils.getConstructorSilent(classNonNullList, List.class, Object.class);
		
		public static Field			fieldNonNullListA				= NMSUtils.getFieldSilent(classNonNullList, "a");
		public static Field			fieldNonNullListB				= NMSUtils.getFieldSilent(classNonNullList, "b");
		
		public static int getID(Object packet) throws Exception{
			return (int)fieldPacketPlayOutWindowItemsA.get(packet);
		}
		
		public static List<Object> getItems(Object packet) throws Exception{
			return (List<Object>)fieldPacketPlayOutWindowItemsB.get(packet);
		}
		
		public static List<Object> getTrueItems(Object packet) throws Exception{
			return (List<Object>)fieldNonNullListA.get(fieldPacketPlayOutWindowItemsB.get(packet));
		}
		
		public static void limitItems(Object packet, int limit) throws Exception{
			List nnl = (List)fieldPacketPlayOutWindowItemsB.get(packet);
			List set;
			if(classNonNullList != null){
				Object def = fieldNonNullListB.get(nnl);
				List list = new ArrayList();
				for(int i = 0; i < limit; i++){
					list.add(nnl.get(i));
				}
				set = (List)conNonNullList.newInstance(list, def);
			}else{
				List list = new ArrayList();
				for(int i = 0; i < limit; i++){
					list.add(nnl.get(i));
				}
				set = list;
			}
			setItems(packet, set);
		}
		
		public static void setItems(Object packet, List list) throws Exception{
			fieldPacketPlayOutWindowItemsB.set(packet, list);
		}
		
		public static List createNonNull(List old, List list) throws Exception{
			Object def = fieldNonNullListB.get(old);
			return (List)conNonNullList.newInstance(list, def);
		}
	}
	
	public static class WindowClick{
		
		public static Class<?>	classPacketPlayInWindowClick	= NMSUtils.getNMSClass("PacketPlayInWindowClick");
		
		public static Field		fieldPacketPlayInWindowClickA	= NMSUtils.getField(classPacketPlayInWindowClick, "a");
		
		public static int getID(Object packet) throws Exception{
			return (int)fieldPacketPlayInWindowClickA.get(packet);
		}
		
	}
	
	public static class OpenWindow{
		
		public static Class<?>			classPacketPlayOutOpenWindow	= NMSUtils.getNMSClass("PacketPlayOutOpenWindow");
		
		private static Class<?>			classContainer					= NMSUtils.getNMSClass("Container");
		private static Class<?>			classContainerAnvil				= NMSUtils.getNMSClass("ContainerAnvil");
		private static Class<?>			classBlockPosition				= NMSUtils.getNMSClassSilent("BlockPosition");
		private static Class<?>			classChatMessage				= NMSUtils.getNMSClass("ChatMessage");
		private static Class<?>			classIChatBaseComponent			= NMSUtils.getNMSClassSilent("IChatBaseComponent");
		private static Class<?>			classContainers					= NMSUtils.getNMSClassSilent("Containers");
		
		private static Constructor<?>	conContainerAnvil				= getConContainerAnvil();
		
		private static Constructor<?> getConContainerAnvil(){
			if(classBlockPosition != null){
				return getConstructorSilent(classContainerAnvil, NMSUtils.getNMSClass("PlayerInventory"), NMSUtils.getNMSClass("World"), classBlockPosition, classEntityHuman);
			}else{
				return getConstructorSilent(classContainerAnvil, NMSUtils.getNMSClass("PlayerInventory"), NMSUtils.getNMSClass("World"), int.class, int.class, int.class, classEntityHuman);
			}
		}
		
		private static Constructor<?>	conBlockPosition			= getConstructorSilent(classBlockPosition, int.class, int.class, int.class);
		private static Constructor<?>	conPacketPlayOutOpenWindow	= getConPacketPlayOutOpenWindow();
		
		private static Constructor<?> getConPacketPlayOutOpenWindow(){
			Constructor<?> con = getConstructorSilent(classPacketPlayOutOpenWindow, int.class, int.class, String.class, int.class, boolean.class);
			if(con == null){
				con = getConstructorSilent(classPacketPlayOutOpenWindow, int.class, String.class, classIChatBaseComponent, int.class);
			}
			if(con == null){
				try{
					con = getConstructorSilent(classPacketPlayOutOpenWindow, int.class, classContainer, classIChatBaseComponent);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			return con;
		}
		
		private static Constructor<?>	conChatMessage				= getConstructorSilent(classChatMessage, String.class, Object[].class);
		
		private static Field			fieldCheckReachable			= NMSUtils.getField(classContainer, "checkReachable");
		private static Field			fieldWindowID				= NMSUtils.getField(classContainer, "windowId");
		private static Field			fieldInventory				= NMSUtils.getField(classEntityHuman, "inventory");
		private static Field			fieldActiveContainer		= NMSUtils.getField(classEntityHuman, "activeContainer");
		private static Field			fieldWorld					= NMSUtils.getField(classEntity, "world");
		
		private static Method			nextContainerCounter		= getMethodSilent(classEntityPlayer, "nextContainerCounter");
		private static Method			addSlotListener				= getMethodSilent(classContainer, "addSlotListener", NMSUtils.getNMSClass("ICrafting"));
		
		private static Object			blockPosition				= classBlockPosition != null ? newInstance(conBlockPosition, 0, 0, 0) : null;
		private static Object			chatMessage					= newInstance(conChatMessage, "Enter", new Object[0]);
		
		private static Class			classItemStack				= NMSUtils.getNMSClass("ItemStack");
		
		private static Class			classPacketPlayOutSetSlot	= NMSUtils.getNMSClass("PacketPlayOutSetSlot");
		private static Constructor<?>	conPacketPlayOutSetSlot		= getConstructor(classPacketPlayOutSetSlot, int.class, int.class, classItemStack);
		
		private static Class<?>			classCraftContainer			= NMSUtils.getOBCClass("inventory.CraftContainer");
		private static Method			getNotchInventoryType		= NMSUtils.getMethod(classCraftContainer, "getNotchInventoryType", InventoryType.class);
		
		public static Object getType(InventoryType type) throws Exception{
			return getNotchInventoryType.invoke(null, type);
		}
		
		public static Object getAnvilPacketPlayOutOpenWindow(int id) throws Exception{
			if(conPacketPlayOutOpenWindow.getParameterTypes()[2].equals(classIChatBaseComponent)){
				Object type = getType(InventoryType.ANVIL);
				return conPacketPlayOutOpenWindow.newInstance(id, type, chatMessage, 0);
			}else{
				return conPacketPlayOutOpenWindow.newInstance(id, 8, "Repairing", 9, true);
			}
		}
		
		public static Object getPacketPlayOutOpenWindow(int id, InventoryType it, String title, int size) throws Exception{
			Object type = getType(it);
			if(conPacketPlayOutOpenWindow.getParameterTypes()[2].equals(classIChatBaseComponent)){
				return conPacketPlayOutOpenWindow.newInstance(id, type, newInstance(conChatMessage, title, new Object[0]), 0);
			}else{
				return conPacketPlayOutOpenWindow.newInstance(id, type, title, size, false);
			}
		}
		
		public static void openAnvil(Player player){
			try{
				Object nmsPlayer = NMSUtils.getHandle(player);
				Object anvilcon;
				
				if(classBlockPosition != null){
					anvilcon = conContainerAnvil.newInstance(fieldInventory.get(nmsPlayer), fieldWorld.get(nmsPlayer), blockPosition, nmsPlayer);
				}else{
					anvilcon = conContainerAnvil.newInstance(fieldInventory.get(nmsPlayer), fieldWorld.get(nmsPlayer), 0, 0, 0, nmsPlayer);
				}
				fieldCheckReachable.set(anvilcon, false);
				
				int c = (Integer)nextContainerCounter.invoke(nmsPlayer);
				
				sendPacket(player, getAnvilPacketPlayOutOpenWindow(c));
				fieldActiveContainer.set(nmsPlayer, anvilcon);
				fieldWindowID.set(anvilcon, c);
				addSlotListener.invoke(anvilcon, nmsPlayer);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public static class LoginOutSuccess{
		
		public static Class<?>	classPacketLoginOutSuccess	= NMSUtils.getNMSClass("PacketLoginOutSuccess");
		
		public static Field		gp							= NMSUtils.getFirstFieldOfType(classPacketLoginOutSuccess, GameProfile.class);
		
		public static GameProfile getProfile(Object packet) throws Exception{
			return (GameProfile)gp.get(packet);
		}
		
	}
	
	public static class LoginInStart{
		
		public static Class<?>	classPacketLoginInStart	= NMSUtils.getNMSClass("PacketLoginInStart");
		
		public static Field		gp						= NMSUtils.getFirstFieldOfType(classPacketLoginInStart, GameProfile.class);
		
		public static GameProfile getProfile(Object packet) throws Exception{
			return (GameProfile)gp.get(packet);
		}
		
	}
	
	public static class HandshakingInSetProtocol{
		
		public static Class<?>	classPacketHandshakingInSetProtocol			= NMSUtils.getNMSClass("PacketHandshakingInSetProtocol");
		public static Field		fieldPacketHandshakingInSetProtocolProtocol	= NMSUtils.getFirstFieldOfType(classPacketHandshakingInSetProtocol, int.class);
		public static Field		fieldPacketHandshakingInSetProtocolHostname	= NMSUtils.getFirstFieldOfType(classPacketHandshakingInSetProtocol, String.class);
		public static Field		fieldPacketHandshakingInSetProtocolPort		= NMSUtils.getLastFieldOfType(classPacketHandshakingInSetProtocol, int.class);
		public static Field		fieldPacketHandshakingInSetProtocolStatus	= NMSUtils.getField(classPacketHandshakingInSetProtocol, "d");
	}
	
	public static class EntityEquipment{
		
		public static Class<?>			classPacketPlayOutEntityEquipment	= NMSUtils.getNMSClass("PacketPlayOutEntityEquipment");
		public static Field				fieldPacketPlayOutEntityEquipmenta	= NMSUtils.getField(classPacketPlayOutEntityEquipment, "a");
		public static Field				fieldPacketPlayOutEntityEquipmentb	= NMSUtils.getField(classPacketPlayOutEntityEquipment, "b");
		public static Field				fieldPacketPlayOutEntityEquipmentc	= NMSUtils.getField(classPacketPlayOutEntityEquipment, "c");
		public static Class<?>			classEnumItemSlot					= NMSUtils.getNMSClass("EnumItemSlot");
		
		public static Constructor<?>	conPacketPlayOutEntityEquipment		= NMSUtils.getConstructor(classPacketPlayOutEntityEquipment, int.class, classEnumItemSlot, ItemUtils.getInstance().getNMSItemClass());
		
		public static int getEntityID(Object packet) throws Exception{
			return (int)fieldPacketPlayOutEntityEquipmenta.get(packet);
		}
		
		public static Object getEnumItemSlot(Object packet) throws Exception{
			return fieldPacketPlayOutEntityEquipmentb.get(packet);
		}
		
		public static Object getItemStack(Object packet) throws Exception{
			return fieldPacketPlayOutEntityEquipmentc.get(packet);
		}
		
		public static void setItemStack(Object packet, Object item) throws Exception{
			fieldPacketPlayOutEntityEquipmentc.set(packet, item);
		}
		
		public static Object create(int id, int slot) throws Exception{
			return conPacketPlayOutEntityEquipment.newInstance(id, NMSUtils.getEnum(slot, classEnumItemSlot), ItemUtils.getInstance().getEmpty());
		}
		
		public static Object create(int id, String slot) throws Exception{
			return conPacketPlayOutEntityEquipment.newInstance(id, NMSUtils.getEnum(slot, classEnumItemSlot), ItemUtils.getInstance().getEmpty());
		}
		
		public static Object create(int id, int slot, ItemStack is) throws Exception{
			return conPacketPlayOutEntityEquipment.newInstance(id, NMSUtils.getEnum(slot, classEnumItemSlot), ItemUtils.getInstance().getNMSCopy(is));
		}
		
		public static Object create(int id, String slot, ItemStack is) throws Exception{
			return conPacketPlayOutEntityEquipment.newInstance(id, NMSUtils.getEnum(slot, classEnumItemSlot), ItemUtils.getInstance().getNMSCopy(is));
		}
		
		public static Object create(int id, Object slot, ItemStack is) throws Exception{
			return conPacketPlayOutEntityEquipment.newInstance(id, slot, ItemUtils.getInstance().getNMSCopy(is));
		}
		
	}
}
