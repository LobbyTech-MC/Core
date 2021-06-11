/*
 * Copyright (c) 2021 Ashley Thew
 */

package me.dablakbandit.core.utils.packet.types;

import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.PacketUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;

public class Animation {
	
	public static Class<?> classPacketPlayOutAnimation = PacketType.getClassNMS("net.minecraft.network.protocol.game.PacketPlayOutAnimation", "PacketPlayOutAnimation");


	public static Constructor<?> constructorPacketPlayOutAnimation = NMSUtils.getConstructor(classPacketPlayOutAnimation, PacketType.classEntity, int.class);

	public static Object getPacket(Entity entity, int i) throws Exception {
		return constructorPacketPlayOutAnimation.newInstance(PacketUtils.getHandle(entity), i);
	}
	
}
