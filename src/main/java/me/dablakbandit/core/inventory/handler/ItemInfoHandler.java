package me.dablakbandit.core.inventory.handler;

import me.dablakbandit.core.players.CorePlayers;

public interface ItemInfoHandler<T>{
	
	void onClick(CorePlayers pl, T t);
	
}
