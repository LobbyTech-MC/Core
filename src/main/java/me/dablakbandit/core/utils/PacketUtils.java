package me.dablakbandit.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import io.netty.channel.Channel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class PacketUtils{
	
	public static Class<?>				classEntity							= NMSUtils.getNMSClass("Entity"), classEntityHuman = NMSUtils.getNMSClass("EntityHuman"), classEntityPlayer = NMSUtils.getNMSClass("EntityPlayer"), classPlayerConnection = NMSUtils.getNMSClassSilent("PlayerConnection"),
	classPacket = NMSUtils.getNMSClassSilent("Packet"), classNetworkManager = NMSUtils.getNMSClassSilent("NetworkManager");
	
	public static Class<?>				classMinecraftKey					= NMSUtils.getNMSClass("MinecraftKey");
	public static Class<?>				enumGamemode						= NMSUtils.getNMSClass("EnumGamemode");
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
	
	public static class Sound{
		
		public static Class<?>	classPacketPlayOutNamedSoundEffect	= NMSUtils.getNMSClass("PacketPlayOutNamedSoundEffect");
		public static Field		fieldSoundEffect					= NMSUtils.getFirstFieldOfType(classPacketPlayOutNamedSoundEffect, classSoundEffect), fieldMinecraftKey = NMSUtils.getFirstFieldOfType(classSoundEffect, classMinecraftKey);
		
		public static void sendBreakSound(Player player, Block block) throws Exception{
			Object nmsblock = NMSUtils.getBlockHandle(block);
			Object step = fieldStepSound.get(nmsblock);
			Object sound = fieldBreakSound.get(step);
			Object key = fieldMinecraftKey.get(sound);
			String name = (String)fieldMinecraftKeyB.get(key);
			sendPlay(player, name);
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
		public static Class<?>	classChatMessageType	= NMSUtils.getNMSClassSilent("ChatMessageType");
		public static Field		fieldPacketPlayOutChatB	= NMSUtils.getFirstFieldOfType(classPacketPlayOutChat, classChatMessageType != null ? classChatMessageType : byte.class);
		
		private static Class<?>	classIChatBaseComponent	= NMSUtils.getNMSClass("IChatBaseComponent");
		private static Class<?>	classChatSerializer		= NMSUtils.getNMSClass("ChatSerializer", "IChatBaseComponent");
		
		private static Method	methodChatSerializerA	= NMSUtils.getMethod(classChatSerializer, "a", classIChatBaseComponent);
		private static Field	fieldPacketPlayOutChatA	= NMSUtils.getField(classPacketPlayOutChat, "a");
		private static Field	fieldPacketPlayOutChatC	= NMSUtils.getField(classPacketPlayOutChat, "components");
		
		public static String getMessage(Object packet) throws Exception{
			String s = (String)methodChatSerializerA.invoke(null, fieldPacketPlayOutChatA.get(packet));
			if(s.equals("null")){
				Object bc = fieldPacketPlayOutChatC.get(packet);
				if(bc != null){
					s = ComponentSerializer.toString((BaseComponent[])bc);
				}
			}
			if(s.startsWith("{\"extra\":[{\"strikethrough\":true,\"color\":\"blue\",\"text\":\"--")){ return "{}"; }
			System.out.print("TEST:" + s);
			return s;
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
		
		public static Class<?>	classPacketPlayOutTabComplete	= NMSUtils.getNMSClass("PacketPlayOutTabComplete");
		public static Class<?>	classPacketPlayInTabComplete	= NMSUtils.getNMSClass("PacketPlayInTabComplete");
		public static Field		fieldPacketPlayOutTabCompletea	= NMSUtils.getField(classPacketPlayOutTabComplete, "a");
		public static Field		fieldPacketPlayInTabCompletea	= NMSUtils.getField(classPacketPlayInTabComplete, "a");
		
		public static String[] getOutA(Object packet) throws Exception{
			return (String[])fieldPacketPlayOutTabCompletea.get(packet);
		}
		
		public static String getInA(Object packet) throws Exception{
			return (String)fieldPacketPlayInTabCompletea.get(packet);
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
	
	public static Class<?>			classSoundEffectType	= NMSUtils.getNMSClass("SoundEffectType");
	public static Class<?>			classSoundEffect		= NMSUtils.getNMSClass("SoundEffect");
	
	public static Field				fieldStepSound			= NMSUtils.getFirstFieldOfType(classBlock, classSoundEffectType);
	public static Field				fieldBreakSound			= NMSUtils.getFirstFieldOfType(classSoundEffectType, classSoundEffect);
	public static Field				fieldMinecraftKey		= NMSUtils.getFirstFieldOfType(classSoundEffect, classMinecraftKey);
	
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
}
