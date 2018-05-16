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
		new Thread(new AddThread(this), "Core Player Channel Adder").start();
	}
	
	@Override
	public void save(){
		new Thread(new RemoveThread(this), "Core Player Channel Remover").start();
	}
	
}
