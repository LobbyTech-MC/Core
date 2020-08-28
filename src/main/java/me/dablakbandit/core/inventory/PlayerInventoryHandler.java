package me.dablakbandit.core.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import me.dablakbandit.core.players.CorePlayers;

public abstract class PlayerInventoryHandler{
	
	public abstract void parseClick(CorePlayers pl, Inventory clicked, Inventory top, InventoryClickEvent event);
	
	public abstract void parseInventoryDrag(CorePlayers corePlayers, Inventory clicked, Inventory top, InventoryDragEvent event);
	
}
