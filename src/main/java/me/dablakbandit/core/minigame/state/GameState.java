package me.dablakbandit.core.minigame.state;

import org.bukkit.event.Listener;

public abstract class GameState implements Listener{
	
	public abstract void enable();
	
	public abstract void disable();
	
}
