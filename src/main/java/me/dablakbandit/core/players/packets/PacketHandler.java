package me.dablakbandit.core.players.packets;

import java.util.ArrayList;
import java.util.List;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.server.packet.ServerHandler;
import me.dablakbandit.core.server.packet.ServerPacketListener;
import me.dablakbandit.core.server.packet.ServerPacketManager;

public class PacketHandler extends ServerPacketListener{
	
	private List<PacketListener>	listeners	= new ArrayList<PacketListener>();
	private CorePlayers				pl;
	private ServerHandler			handler;
	
	public PacketHandler(CorePlayers pl){
		this.pl = pl;
		this.handler = ServerPacketManager.getInstance().getHandler(pl.getName());
		if(this.handler != null){
			handler.addListener(this);
		}else{
			pl.getPlayer().kickPlayer("[Core] Packet handler failed, please rejoin");
		}
	}
	
	public CorePlayers getPlayers(){
		return pl;
	}
	
	public void addListener(PacketListener pl){
		listeners.add(pl);
	}
	
	public void bypass(Object packet, boolean bypass) throws Exception{
		handler.bypass(packet, bypass);
	}
	
	@Override
	public boolean read(ServerHandler sh, Object packet){
		boolean read = true;
		for(PacketListener listener : listeners){
			if(!listener.read(pl, packet)){
				read = false;
			}
		}
		return read;
	}
	
	@Override
	public boolean write(ServerHandler sh, Object packet){
		boolean write = true;
		for(PacketListener listener : listeners){
			if(!listener.write(pl, packet)){
				write = false;
			}
		}
		return write;
	}
}
