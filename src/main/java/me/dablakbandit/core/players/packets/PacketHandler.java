package me.dablakbandit.core.players.packets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;

import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.server.packet.ServerHandler;
import me.dablakbandit.core.server.packet.ServerPacketListener;
import me.dablakbandit.core.server.packet.ServerPacketManager;

public class PacketHandler extends ServerPacketListener{
	
	private List<PacketListener>	listeners	= Collections.synchronizedList(new ArrayList<PacketListener>());
	private CorePlayers				pl;
	private ServerHandler			handler;
	
	public PacketHandler(CorePlayers pl){
		this.pl = pl;
		this.handler = ServerPacketManager.getInstance().getHandler(pl.getName());
		if(this.handler != null){
			handler.addListener(this);
		}else{
			Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), new Runnable(){
				@Override
				public void run(){
					pl.getPlayer().kickPlayer("[Core] Packet handler failed, please rejoin");
				}
			}, 0);
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
