/*
 * Copyright (c) 2021 Ashley Thew
 */

package me.dablakbandit.core.utils.packet.types;

import com.mojang.authlib.GameProfile;
import me.dablakbandit.core.utils.NMSUtils;

import java.lang.reflect.Field;

public class LoginInStart{
	
	public static Class<?> classPacketLoginInStart = getPacket();
	
	private static Class<?> getPacket(){
		Class<?> clazz = NMSUtils.getClassSilent("net.minecraft.network.protocol.login.PacketLoginInStart");
		if(clazz == null){
			clazz = NMSUtils.getNMSClass("PacketLoginInStart");
		}
		return clazz;
	}
	
	public static Field gp = NMSUtils.getFirstFieldOfType(classPacketLoginInStart, GameProfile.class);
	
	public static GameProfile getProfile(Object packet) throws Exception{
		return (GameProfile)gp.get(packet);
	}
	
}
