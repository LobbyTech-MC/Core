/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.server.packet.wrapped;

import me.dablakbandit.core.server.packet.ServerHandler;
import me.dablakbandit.core.server.packet.ServerPacketListener;

public abstract class WrappedServerPacketListener extends ServerPacketListener{
	@Override
	public boolean read(ServerHandler handler, Object packet){
		return true;
	}
	
	@Override
	public boolean write(ServerHandler handler, Object packet){
		return true;
	}
	
	public boolean isWriteWhitelisted(WrappedPacket packet){
		return true;
	}
	
	public boolean isReadWhitelisted(WrappedPacket packet){
		return true;
	}
	
	public abstract boolean readWrapped(ServerHandler handler, WrappedPacket packet);
	
	public abstract boolean writeWrapped(ServerHandler handler, WrappedPacket packet);
}
