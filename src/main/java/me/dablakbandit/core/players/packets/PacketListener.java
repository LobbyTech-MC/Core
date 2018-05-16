package me.dablakbandit.core.players.packets;

import me.dablakbandit.core.players.CorePlayers;

public abstract class PacketListener{
	
	public abstract boolean read(CorePlayers cp, Object packet);
	
	public abstract boolean write(CorePlayers cp, Object packet);
}
