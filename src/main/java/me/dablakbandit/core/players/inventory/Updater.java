package me.dablakbandit.core.players.inventory;

import me.dablakbandit.core.players.CorePlayers;

public abstract class Updater<T extends OpenInventory>{
	
	public abstract void update(T t, CorePlayers pl);
}
