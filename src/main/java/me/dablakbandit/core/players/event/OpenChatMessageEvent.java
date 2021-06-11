package me.dablakbandit.core.players.event;

import org.bukkit.event.HandlerList;

import me.dablakbandit.core.players.CorePlayers;

public class OpenChatMessageEvent extends PlayersEvent{
	
	private String message;
	
	public OpenChatMessageEvent(CorePlayers pl, String message, boolean async){
		super(pl, pl.getPlayer(), async);
		this.message = message;
	}
	
	public String getMessage(){
		return message;
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
