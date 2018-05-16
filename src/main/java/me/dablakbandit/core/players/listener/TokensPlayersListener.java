package me.dablakbandit.core.players.listener;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.TokensInfo;

public class TokensPlayersListener extends CorePlayersListener{
	
	private static TokensPlayersListener instance = new TokensPlayersListener();
	
	public static TokensPlayersListener getInstance(){
		return instance;
	}
	
	@Override
	public void addCorePlayers(CorePlayers pl){
		pl.addInfo(new TokensInfo(pl));
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
