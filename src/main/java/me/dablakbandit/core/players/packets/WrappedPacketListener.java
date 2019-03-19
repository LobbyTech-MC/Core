/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.players.packets;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.server.packet.wrapped.WrappedPacket;

public abstract class WrappedPacketListener extends PacketListener{
	@Override
	public boolean read(CorePlayers cp, Object packet){
		return true;
	}
	
	@Override
	public boolean write(CorePlayers cp, Object packet){
		return true;
	}
	
	public boolean isWriteWhitelisted(WrappedPacket packet){
		return true;
	}
	
	public boolean isReadWhitelisted(WrappedPacket packet){
		return true;
	}
	
	public abstract boolean readWrapped(CorePlayers cp, WrappedPacket packet);
	
	public abstract boolean writeWrapped(CorePlayers cp, WrappedPacket packet);
}
