package me.dablakbandit.core.players.info;

import me.dablakbandit.core.players.CorePlayers;

public abstract class CorePlayersInfo{
	
	protected CorePlayers pl;
	
	public CorePlayersInfo(CorePlayers pl){
		this.pl = pl;
	}
	
	public CorePlayers getPlayers(){
		return pl;
	}
	
	public abstract void load();
	
	public abstract void save();
}
