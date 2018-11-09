package me.dablakbandit.core.players.event;

import org.bukkit.event.HandlerList;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.inventory.OpenInventory;

public class OpenInventoryChangeEvent extends PlayersEvent{
	
	private OpenInventory from, to;
	
	public OpenInventoryChangeEvent(CorePlayers pl, OpenInventory from, OpenInventory to){
		super(pl, pl.getPlayer());
		this.from = from;
		this.to = to;
	}
	
	public OpenInventory getFrom(){
		return from;
	}
	
	public OpenInventory getTo(){
		return to;
	}
	
	public void setTo(OpenInventory oc){
		to = oc;
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
