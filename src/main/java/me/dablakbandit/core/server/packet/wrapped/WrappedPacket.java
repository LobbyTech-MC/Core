/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.server.packet.wrapped;

public class WrappedPacket extends WrappedObject{
	
	public WrappedPacket(Object packet){
		super(packet);
	}
	
	public Object getRawPacket(){
		return super.getRawObject();
	}
	
	public void setRawPacket(Object object){
		super.setRawObject(object);
	}
	
	public boolean isPlayIn(){
		return getRawObject().getClass().getSimpleName().startsWith("PacketPlayIn");
	}
	
	public boolean isPlayOut(){
		return getRawObject().getClass().getSimpleName().startsWith("PacketPlayOut");
	}
	
	public boolean isLoginIn(){
		return getRawObject().getClass().getSimpleName().startsWith("PacketLoginIn");
	}
	
	public boolean isLoginOut(){
		return getRawObject().getClass().getSimpleName().startsWith("PacketLoginOut");
	}
	
	public boolean isHandshakingIn(){
		return getRawObject().getClass().getSimpleName().startsWith("PacketHandshakingIn");
	}
	
	public boolean isStatusIn(){
		return getRawObject().getClass().getSimpleName().startsWith("PacketStatusIn");
	}
	
	public boolean isStatusOut(){
		return getRawObject().getClass().getSimpleName().startsWith("PacketStatusOut");
	}
}
