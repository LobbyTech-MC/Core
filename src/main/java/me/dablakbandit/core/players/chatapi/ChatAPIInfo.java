package me.dablakbandit.core.players.chatapi;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.event.OpenChatChangeEvent;
import me.dablakbandit.core.players.info.CorePlayersInfo;
import me.dablakbandit.core.players.packets.PacketHandler;
import me.dablakbandit.core.players.packets.PacketInfo;
import me.dablakbandit.core.utils.LimitedList;
import me.dablakbandit.core.utils.PacketUtils;
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
		ChatAPIDatabase.getInstance().load(this);
	}
	
	@Override
	public void save(){
		ChatAPIDatabase.getInstance().save(this);
	}
	
	public OpenChat getOpenChat(){
		return open_chat;
	}
	
	public void setOpenChat(OpenChat chat){
		OpenChatChangeEvent event = new OpenChatChangeEvent(pl, this.open_chat, chat);
		Bukkit.getPluginManager().callEvent(event);
		chat = event.getTo();
		if(chat != null){
			if(!chat.isInitialized()){
				chat.init();
				chat.setInitialized();
			}
			chat.open(pl, pl.getPlayer());
		}
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
		allowed = 1;
		JSONFormatter jf = new JSONFormatter().newLine(i);
		try{
			for(int k = 0; k < packets.size(); k++){
				if(k != 0){
					jf.newLine();
				}
				Object packet = packets.get(k);
				jf.append(new JSONObject(PacketUtils.Chat.getMessage(packet)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		jf.send(pl.getPlayer());
		try{
			PacketHandler handler = pl.getInfo(PacketInfo.class).getHandler();
			for(Object packet : packets){
				// handler.bypass(packet, true);
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
	
	protected int		hf	= 53, twidth = 53;
	protected String	hfs	= makeHeaderFooterString();
	
	private String makeHeaderFooterString(){
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH);
		for(int i = 0; i < hf; i++){
			sb.append("-");
		}
		return sb.toString();
	}
	
	public int getHeaderFooter(){
		return hf;
	}
	
	public void setHeaderFooter(int i){
		hf = i;
		this.hfs = makeHeaderFooterString();
	}
	
	public int getTextWidth(){
		return twidth;
	}
	
	public void setTextWidth(int i){
		if(i > 0)
			twidth = i;
	}
	
	public String getHeaderFooterString(){
		return hfs;
	}
	
	public void refresh(){
		setOpenChat(open_chat);
	}
	
}
