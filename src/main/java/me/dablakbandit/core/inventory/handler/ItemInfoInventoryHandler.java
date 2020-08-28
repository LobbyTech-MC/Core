package me.dablakbandit.core.inventory.handler;

import org.bukkit.inventory.Inventory;

import me.dablakbandit.core.players.CorePlayers;

public interface ItemInfoInventoryHandler<T>{
	
	void onClick(CorePlayers pl, T t, Inventory inventory);
	
}
