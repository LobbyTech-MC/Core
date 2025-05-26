package me.dablakbandit.core.players.event;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import java.lang.reflect.Field;

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
	
	private static Field async = NMSUtils.getPossibleField(Event.class, "async", "isAsync");
	
	private static void setAsync(Event event){
		if(async != null){
			try{
				if(async.getType() == boolean.class){
					async.set(event, true);
				} else {
					async.set(event, net.kyori.adventure.util.TriState.TRUE);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public CorePlayers getPlayers(){
		return pl;
	}
}
