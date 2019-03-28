/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.players.chatapi.module;

import org.bukkit.entity.Player;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;

public class TitleModule<T extends CorePlayersInfo>extends ChatModule<T>{
	
	protected String	title;
	protected int		new_lines	= 1;
	
	public TitleModule(String title){
		this.title = title;
	}
	
	public TitleModule(String title, int new_lines){
		this(title);
		this.new_lines = new_lines;
	}
	
	@Override
	public ChatPosition getPosition(){
		return ChatPosition.HEADER;
	}
	
	@Override
	public void append(JSONFormatter jf, CorePlayers pl, T editorInfo, Player player){
		jf.append(" ").append(title).resetAll().newLine(new_lines);
	}
}
