package me.dablakbandit.core.players.chatapi.module;

import org.bukkit.entity.Player;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;

public abstract class ChatModule<T extends CorePlayersInfo>{
	
	public abstract ChatPosition getPosition();
	
	public abstract void append(JSONFormatter jf, CorePlayers pl, T t, Player player);
}
