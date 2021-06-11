/*
 * Copyright (c) 2021 Ashley Thew
 */

package me.dablakbandit.core.utils.packet.types;

import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class SetSlot{
	
	public static Class<?>		classPacketPlayOutSetSlot	= PacketType.getClassNMS("net.minecraft.network.protocol.game.PacketPlayOutSetSlot", "PacketPlayOutSetSlot");
	
	public static Field			fieldPacketPlayOutSetSlotA	= NMSUtils.getDirectFields(classPacketPlayOutSetSlot).get(0);
	public static Field			fieldPacketPlayOutSetSlotB	= NMSUtils.getDirectFields(classPacketPlayOutSetSlot).get(1);
	public static Field			fieldPacketPlayOutSetSlotC	= NMSUtils.getDirectFields(classPacketPlayOutSetSlot).get(2);
	
	public static Constructor	conPacketPlayOutSetSlot		= NMSUtils.getConstructor(classPacketPlayOutSetSlot, int.class, int.class, PacketType.classItemStack);
	
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
