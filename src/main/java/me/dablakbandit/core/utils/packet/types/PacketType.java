/*
 * Copyright (c) 2021 Ashley Thew
 */

package me.dablakbandit.core.utils.packet.types;

import me.dablakbandit.core.utils.NMSUtils;

public interface PacketType{
	
	public static Class<?>	classEntity		= getClassNMS("net.minecraft.world.entity.Entity", "Entity");
	public static Class<?>	classItemStack	= PacketType.getClassNMS("net.minecraft.world.item.ItemStack", "ItemStack");
	
	public static Class<?> getClassNMS(String normal, String nms){
		Class<?> clazz = NMSUtils.getClassSilent(normal);
		if(clazz == null){
			clazz = NMSUtils.getNMSClassSilent(nms);
		}
		return clazz;
	}
}
