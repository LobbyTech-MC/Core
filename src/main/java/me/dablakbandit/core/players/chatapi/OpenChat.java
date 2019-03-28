package me.dablakbandit.core.players.chatapi;

import org.bukkit.entity.Player;

import me.dablakbandit.core.players.CorePlayers;

public abstract class OpenChat{
	
	protected boolean initialized;
	
	protected boolean isInitialized(){
		return initialized;
	}
	
	protected void setInitialized(){
		this.initialized = true;
	}
	
	public abstract void open(CorePlayers pl, Player player);
	
	public void init(){
	}
}
