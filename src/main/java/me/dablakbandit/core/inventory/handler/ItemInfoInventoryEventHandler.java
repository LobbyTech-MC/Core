package me.dablakbandit.core.inventory.handler;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import me.dablakbandit.core.players.CorePlayers;

public interface ItemInfoInventoryEventHandler<T>{
	
	void onClick(CorePlayers pl, T t, Inventory inventory, InventoryClickEvent event);
	
}
