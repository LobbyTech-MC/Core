package me.dablakbandit.core.players.packets;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;

public class PacketInfo extends CorePlayersInfo{
	
	public PacketHandler handler;
	
	public PacketInfo(CorePlayers cp){
		super(cp);
		handler = new PacketHandler(cp);
	}
	
	public PacketHandler getHandler(){
		return handler;
	}
	
	@Override
	public void load(){
		
	}
	
	@Override
	public void save(){
		
	}
	
}
