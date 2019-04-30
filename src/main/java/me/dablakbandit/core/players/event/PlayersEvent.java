package me.dablakbandit.core.players.event;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.NMSUtils;

public abstract class PlayersEvent extends PlayerEvent{
	
	public CorePlayers pl;
	
	public PlayersEvent(CorePlayers pl, Player player){
		super(player);
		this.pl = pl;
	}
	
	public PlayersEvent(CorePlayers pl, Player player, boolean async){
		this(pl, player);
		if(async){
			setAsync(this);
		}
	}
	
	private static Field async = NMSUtils.getFieldSilent(Event.class, "async");
	
	private static void setAsync(Event event){
		if(async != null){
			try{
				async.set(event, true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public CorePlayers getPlayers(){
		return pl;
	}
}
