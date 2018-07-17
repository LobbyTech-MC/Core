package me.dablakbandit.core.server.packet;

public abstract class ServerPacketListener{
	
	public abstract boolean read(ServerHandler handler, Object packet);
	
	public abstract boolean write(ServerHandler handler, Object packet);
}
