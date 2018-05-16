package me.dablakbandit.core.players.listener;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.scoreboard.ScoreboardInfo;

public class ScoreboardPlayersListener extends CorePlayersListener{
	
	private static ScoreboardPlayersListener instance = new ScoreboardPlayersListener();
	
	public static ScoreboardPlayersListener getInstance(){
		return instance;
	}
	
	@Override
	public void addCorePlayers(CorePlayers pl){
		pl.addInfo(new ScoreboardInfo(pl));
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
