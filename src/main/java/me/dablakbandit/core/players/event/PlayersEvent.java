package me.dablakbandit.core.players.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import me.dablakbandit.core.players.CorePlayers;

public abstract class PlayersEvent extends PlayerEvent{
	
	public CorePlayers pl;
	
	public PlayersEvent(CorePlayers pl, Player player){
		super(player);
		this.pl = pl;
	}
	
	public CorePlayers getPlayers(){
		return pl;
	}
}
