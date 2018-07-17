package me.dablakbandit.core.players.listener;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.packets.PacketInfo;
import me.dablakbandit.core.server.packet.ServerPacketManager;

public class ServerPacketPlayersListener extends CorePlayersListener{
	
	private static ServerPacketPlayersListener instance = new ServerPacketPlayersListener();
	
	public static ServerPacketPlayersListener getInstance(){
		return instance;
	}
	
	private ServerPacketPlayersListener(){
		ServerPacketManager.getInstance().enable();
	}
	
	public void loginCorePlayers(CorePlayers pl){
		pl.addInfo(new PacketInfo(pl));
	}
	
	@Override
	public void addCorePlayers(CorePlayers pl){
		
	}
	
	@Override
	public void loadCorePlayers(CorePlayers pl){
		// Do something
	}
	
	@Override
	public void saveCorePlayers(CorePlayers pl){
		// Do something
	}
	
	@Override
	public void removeCorePlayers(CorePlayers pl){
		ServerPacketManager.getInstance().remove(pl.getName());
	}
	
}
