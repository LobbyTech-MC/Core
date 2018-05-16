package me.dablakbandit.core.players.listener;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.PermissionsInfo;

public class PermissionsPlayersListener extends CorePlayersListener{
	
	private static PermissionsPlayersListener instance = new PermissionsPlayersListener();
	
	public static PermissionsPlayersListener getInstance(){
		return instance;
	}
	
	@Override
	public void addCorePlayers(CorePlayers pl){
		pl.addInfo(new PermissionsInfo(pl));
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
