package me.dablakbandit.core.players.event;

import org.bukkit.event.HandlerList;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.inventory.OpenInventory;

public class OpenInventoryClosedEvent extends PlayersEvent{
	
	private OpenInventory closed;
	
	public OpenInventoryClosedEvent(CorePlayers pl, OpenInventory closed){
		super(pl, pl.getPlayer());
		this.closed = closed;
	}
	
	public OpenInventory getClosed(){
		return closed;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
