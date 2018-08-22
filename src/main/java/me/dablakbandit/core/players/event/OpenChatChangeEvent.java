package me.dablakbandit.core.players.event;

import org.bukkit.event.HandlerList;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.chatapi.OpenChat;

public class OpenChatChangeEvent extends PlayersEvent{
	
	private OpenChat from, to;
	
	public OpenChatChangeEvent(CorePlayers pl, OpenChat from, OpenChat to){
		super(pl, pl.getPlayer());
		this.from = from;
		this.to = to;
	}
	
	public OpenChat getFrom(){
		return from;
	}
	
	public OpenChat getTo(){
		return to;
	}
	
	public void setTo(OpenChat oc){
		to = oc;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers(){
		return handlers;
	}
}
