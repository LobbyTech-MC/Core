package me.dablakbandit.core.players.chatapi.module;

import org.bukkit.entity.Player;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.chatapi.ChatAPIInfo;
import me.dablakbandit.core.players.chatapi.OpenChat;
import me.dablakbandit.core.players.info.CorePlayersInfo;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;

public abstract class ChatModule<T extends CorePlayersInfo>{
	
	public abstract ChatPosition getPosition();
	
	public abstract void append(JSONFormatter jf, CorePlayers pl, T t, Player player);
	
	public void refresh(CorePlayers pl){
		pl.getInfo(ChatAPIInfo.class).refresh();
	}
	
	public void refresh(T t){
		refresh(t.getPlayers());
	}
	
	public void open(CorePlayers pl, OpenChat chat){
		pl.getInfo(ChatAPIInfo.class).setOpenChat(chat);
	}
}
