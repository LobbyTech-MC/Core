package me.dablakbandit.core.players.chatapi;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.listener.CorePlayersListener;
import me.dablakbandit.core.players.packets.PacketInfo;

public class ChatAPIPlayersListener extends CorePlayersListener{
	
	private static ChatAPIPlayersListener instance = new ChatAPIPlayersListener();
	
	public static ChatAPIPlayersListener getInstance(){
		return instance;
	}
	
	private ChatAPIPlayersListener(){
		ChatAPIDatabase.getInstance();
	}
	
	@Override
	public void addCorePlayers(CorePlayers pl){
		pl.addInfo(new ChatAPIInfo(pl));
	}
	
	@Override
	public void loadCorePlayers(CorePlayers pl){
		pl.getInfo(PacketInfo.class).getHandler().addListener(ChatAPIListener.getInstance());
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
