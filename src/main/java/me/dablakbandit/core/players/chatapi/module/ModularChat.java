package me.dablakbandit.core.players.chatapi.module;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.chatapi.ChatAPIInfo;
import me.dablakbandit.core.players.chatapi.OpenChat;
import me.dablakbandit.core.players.info.CorePlayersInfo;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;

public abstract class ModularChat<T extends CorePlayersInfo>extends OpenChat{
	
	private Class<T>			clazz;
	protected List<ChatModule>	modules		= new ArrayList<ChatModule>();
	protected List<ChatModule>	headers		= new ArrayList<ChatModule>();
	protected List<ChatModule>	contents	= new ArrayList<ChatModule>();
	protected List<ChatModule>	footers		= new ArrayList<ChatModule>();
	
	public ModularChat(Class<T> clazz){
		this.clazz = clazz;
		init();
	}
	
	public abstract void init();
	
	public T getInfo(CorePlayers pl){
		return pl.getInfo(clazz);
	}
	
	public abstract boolean hasHeaderFooter();
	
	public void addModule(ChatModule module){
		switch(module.getPosition()){
		case HEADER:
			headers.add(module);
			break;
		case CENTRE:
			contents.add(module);
			break;
		case FOOTER:
			footers.add(module);
			break;
		}
		modules.add(module);
	}
	
	public void open(CorePlayers pl, Player player){
		boolean header_footer = hasHeaderFooter();
		JSONFormatter header = new JSONFormatter();
		JSONFormatter footer = new JSONFormatter();
		ChatAPIInfo cai = pl.getInfo(ChatAPIInfo.class);
		
		T info = getInfo(pl);
		if(header_footer){
			header.append(cai.getHeaderFooterString()).resetAll().newLine();
		}
		
		for(ChatModule chatModule : headers){
			chatModule.append(header, pl, info, player);
		}
		
		for(ChatModule chatModule : footers){
			chatModule.append(footer, pl, info, player);
		}
		if(header_footer){
			footer.append(cai.getHeaderFooterString());
		}
		for(ChatModule cm : contents){
			cm.append(header, pl, info, player);
		}
		int add = 21 - header.getLines() - footer.getLines();
		header.newLine(add);
		header.append(footer);
		send(header, pl);
	}
	
	protected void send(JSONFormatter jf, CorePlayers pl){
		ChatAPIInfo cai = pl.getInfo(ChatAPIInfo.class);
		cai.setAllowed(cai.getAllowed() + jf.getSize());
		jf.send(pl.getPlayer());
	}
}
