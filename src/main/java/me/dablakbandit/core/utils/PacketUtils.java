package me.dablakbandit.core.utils;

import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.utils.packet.DefaultPacketUtils;
import me.dablakbandit.core.utils.packet.IPacketUtils;
import me.dablakbandit.core.utils.packet._16PacketUtils;
import me.dablakbandit.core.utils.packet.types.PacketType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PacketUtils{
	
	private static IPacketUtils packetUtils = load();
	
	private static IPacketUtils load(){
		try{
			CoreLog.info("Attempting to load default PacketUtils");
			packetUtils = new DefaultPacketUtils();
			if(packetUtils.getFieldConnection() != null){
				CoreLog.info("Loaded default, enjoy :)");
				return packetUtils;
			}
		}catch(Exception e){
		}
		try{
			CoreLog.info("Attempting to load 1.16 PacketUtils");
			packetUtils = new _16PacketUtils();
			CoreLog.info("Loaded 1.16, enjoy :)");
			return packetUtils;
		}catch(Exception e){
		}
		return null;
	}
	
	@Deprecated
	public static Constructor<?>	conBlockPosition	= packetUtils.getConBlockPosition();
	@Deprecated
	public static Class<?>			classPacket			= PacketType.getClassNMS("net.minecraft.network.protocol.Packet", "Packet");
	
	public static void sendPacket(Player player, Object packet) throws Exception{
		packetUtils.sendPacket(player, packet);
	}
	
	public static void sendPackets(Player player, Collection<?> packets) throws Exception{
		packetUtils.sendPackets(player, packets);
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
		packetUtils.sendPacketRaw(cons, packet);
	}
	
	public static Object getPlayerConnection(Player player) throws Exception{
		return packetUtils.getPlayerConnection(player);
	}
	
	public static Object getHandle(Entity entity) throws Exception{
		return packetUtils.getHandle(entity);
	}
	
	public static Field getFieldConnection() throws Exception{
		return packetUtils.getFieldConnection();
	}
	
	public static Field getFieldChannel() throws Exception{
		return packetUtils.getFieldChannel();
	}
	
	public static Field getFieldNetworkManager() throws Exception{
		return packetUtils.getFieldNetworkManager();
	}
	
	@Deprecated
	public static class Animation{
		
		public static Object getPacket(Entity entity, int i) throws Exception{
			return me.dablakbandit.core.utils.packet.types.Animation.getPacket(entity, i);
		}
		
	}
	
	@Deprecated
	public static class SetSlot{
		
		public static Class<?>	classPacketPlayOutSetSlot	= PacketType.getClassNMS("net.minecraft.network.protocol.game.PacketPlayOutSetSlot", "PacketPlayOutSetSlot");
		public static Field		fieldPacketPlayOutSetSlotC	= NMSUtils.getDirectFields(classPacketPlayOutSetSlot).get(2);
		
		public static Object getPacket(int window, int slot, ItemStack item) throws Exception{
			return me.dablakbandit.core.utils.packet.types.SetSlot.getPacket(window, slot, item);
		}
		
		public static int getID(Object packet) throws Exception{
			return me.dablakbandit.core.utils.packet.types.SetSlot.getID(packet);
		}
		
		public static int getSlot(Object packet) throws Exception{
			return me.dablakbandit.core.utils.packet.types.SetSlot.getSlot(packet);
		}
		
		public static Object getItemStack(Object packet) throws Exception{
			return me.dablakbandit.core.utils.packet.types.SetSlot.getItemStack(packet);
		}
	}
	
	@Deprecated
	public static class Sound{
		public static void sendPlay(Player player, String name){
			player.playSound(player.getLocation(), name, 1f, 1f);
		}
	}
	
	@Deprecated
	public static class WindowItems{
		public static Class<?>		classPacketPlayOutWindowItems	= PacketType.getClassNMS("net.minecraft.network.protocol.game.PacketPlayOutWindowItems", "PacketPlayOutWindowItems");
		
		public static Field			fieldPacketPlayOutWindowItemsA	= NMSUtils.getField(classPacketPlayOutWindowItems, "a");
		public static Field			fieldPacketPlayOutWindowItemsB	= NMSUtils.getField(classPacketPlayOutWindowItems, "b");
		
		public static Class<?>		classNonNullList				= PacketType.getClassNMS("net.minecraft.core.NonNullList", "NonNullList");
		
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
		
		public static void setItems(Object packet, List list) throws Exception{
			fieldPacketPlayOutWindowItemsB.set(packet, list);
		}
		
		public static List createNonNull(List old, List list) throws Exception{
			Object def = fieldNonNullListB.get(old);
			return (List)conNonNullList.newInstance(list, def);
		}
	}
	
	@Deprecated
	public static class TabComplete{
		
		public static Class<?>	classPacketPlayOutTabComplete		= PacketType.getClassNMS("net.minecraft.network.protocol.game.PacketPlayOutTabComplete", "PacketPlayOutTabComplete");
		public static Class<?>	classPacketPlayInTabComplete		= PacketType.getClassNMS("net.minecraft.network.protocol.game.PacketPlayInTabComplete", "PacketPlayInTabComplete");
		public static Field		fieldPacketPlayOutTabCompletea		= NMSUtils.getFieldSilent(classPacketPlayOutTabComplete, "a");
		public static Field		fieldPacketPlayInTabCompleteString	= NMSUtils.getFirstFieldOfTypeSilent(classPacketPlayInTabComplete, String.class);
		
		public static String[] getOutA(Object packet) throws Exception{
			return (String[])fieldPacketPlayOutTabCompletea.get(packet);
		}
		
		public static String getInString(Object packet) throws Exception{
			return (String)fieldPacketPlayInTabCompleteString.get(packet);
		}
	}
	
	@Deprecated
	public static class EntityMetadata{
		
		public static Class<?>			classPacketPlayOutEntityMetadata		= NMSUtils.getNMSClassSilent("PacketPlayOutEntityMetadata");
		public static Class<?>			classDataWatcher						= PacketType.getClassNMS("net.minecraft.network.syncher.DataWatcher", "DataWatcher");
		public static Class<?>			classDataWatcherItem					= NMSUtils.getInnerClassSilent(classDataWatcher, "Item");
		
		public static Field				fieldPacketPlayOutEntityMetadataa		= NMSUtils.getFieldSilent(classPacketPlayOutEntityMetadata, "a");
		public static Field				fieldPacketPlayOutEntityMetadatab		= NMSUtils.getFieldSilent(classPacketPlayOutEntityMetadata, "b");
		
		public static Constructor<?>	constructorPacketPlayOutEntityMetadata	= NMSUtils.getConstructorSilent(classPacketPlayOutEntityMetadata);
		public static Constructor<?>	constructorPacketPlayOutEntityMetadata2	= NMSUtils.getConstructorSilent(classPacketPlayOutEntityMetadata, int.class, classDataWatcher, boolean.class);
		public static Constructor<?>	constructorDataWatcherItem				= classDataWatcherItem.getConstructors()[0];
		
		public static Field				fieldDataWatcherItema					= NMSUtils.getFieldSilent(classDataWatcherItem, "a");
		public static Field				fieldDataWatcherItemb					= NMSUtils.getFieldSilent(classDataWatcherItem, "b");
		public static Field				fieldDataWatcherb						= NMSUtils.getFieldSilent(classDataWatcher, "d");
		
		public static Method			methodGetDataWatcher					= NMSUtils.getMethodSilent(PacketType.classEntity, "getDataWatcher");
		
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
	
	@Deprecated
	public static class Chat{
		
		private static Class<?>	classPacketPlayOutChat	= PacketType.getClassNMS("net.minecraft.network.protocol.game.PacketPlayOutChat", "PacketPlayOutChat");
		
		private static Class<?>	classIChatBaseComponent	= PacketType.getClassNMS("net.minecraft.network.chat.IChatBaseComponent", "IChatBaseComponent");
		private static Class<?>	classChatSerializer		= NMSUtils.getInnerClass(classIChatBaseComponent, "ChatSerializer");
		
		private static Method	methodChatSerializerA	= NMSUtils.getMethodSilent(classChatSerializer, "a", classIChatBaseComponent);
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
	}
	
}
