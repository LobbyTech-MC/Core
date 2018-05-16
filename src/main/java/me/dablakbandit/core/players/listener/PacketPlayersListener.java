package me.dablakbandit.core.players.listener;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.packets.PacketInfo;

public class PacketPlayersListener extends CorePlayersListener{
	
	private static PacketPlayersListener instance = new PacketPlayersListener();
	
	public static PacketPlayersListener getInstance(){
		return instance;
	}
	
	@Override
	public void addCorePlayers(CorePlayers pl){
		pl.addInfo(new PacketInfo(pl));
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
		// Do something
	}
	
}
