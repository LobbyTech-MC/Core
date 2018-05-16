package me.dablakbandit.core.players.chatapi;

import java.util.List;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;
import me.dablakbandit.core.players.packets.PacketHandler;
import me.dablakbandit.core.players.packets.PacketInfo;
import me.dablakbandit.core.utils.LimitedList;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;

public class ChatAPIInfo extends CorePlayersInfo{
	
	public ChatAPIInfo(CorePlayers pl){
		super(pl);
	}
	
	private List<Object>	packets	= new LimitedList<Object>(100);
	private boolean			paused	= false;
	private int				allowed	= 0;
	
	protected OpenChat		open_chat;
	
	@Override
	public void load(){
	}
	
	@Override
	public void save(){
		
	}
	
	public OpenChat getOpenChat(){
		return open_chat;
	}
	
	public void setOpenChat(OpenChat chat){
		if(chat != null)
			chat.open(pl, pl.getPlayer());
		this.open_chat = chat;
		if(chat != null){
			paused = true;
		}else{
			paused = false;
			resend();
		}
	}
	
	private static JSONFormatter jf = new JSONFormatter().append(" ");
	
	private void resend(){
		int i = 100 - allowed;
		allowed = i;
		while(i > 0){
			i--;
			jf.send(pl.getPlayer());
		}
		try{
			PacketHandler handler = pl.getInfo(PacketInfo.class).getHandler();
			for(Object packet : packets){
				handler.bypass(packet, true);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Object> getPackets(){
		return packets;
	}
	
	public boolean getPaused(){
		return paused;
	}
	
	public void setPaused(boolean b){
		paused = b;
	}
	
	public int getAllowed(){
		return allowed;
	}
	
	public void setAllowed(int i){
		allowed = i;
	}
	
}
