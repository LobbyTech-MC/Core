package me.dablakbandit.core.players.listener;

import me.dablakbandit.core.players.CorePlayers;

public abstract class CorePlayersListener{
	
	public void loginCorePlayers(CorePlayers pl){
		
	}
	
	public abstract void addCorePlayers(CorePlayers pl);
	
	public abstract void loadCorePlayers(CorePlayers pl);
	
	public abstract void saveCorePlayers(CorePlayers pl);
	
	public abstract void removeCorePlayers(CorePlayers pl);
}
