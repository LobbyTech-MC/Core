package me.dablakbandit.core.players.chatapi;

import java.lang.reflect.Field;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.packets.PacketListener;
import me.dablakbandit.core.utils.NMSUtils;

public class ChatAPIListener extends PacketListener{
	
	private static ChatAPIListener instance = new ChatAPIListener();
	
	public static ChatAPIListener getInstance(){
		return instance;
	}
	
	protected static Class<?> packetPlayOutChat = getPacketPlayOutChat();
	
	protected static Class<?> getPacketPlayOutChat(){
		try{
			return NMSUtils.getNMSClassWithException("PacketPlayOutChat");
		}catch(Exception e){
		}
		try{
			return Class.forName("net.minecraft.network.play.server.S02PacketChat");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static Class<?>	chatMessageType	= NMSUtils.getNMSClassSilent("ChatMessageType");
	
	protected static Field	b				= getChatMessageType();
	
	protected static Field getChatMessageType(){
		try{
			return NMSUtils.getFirstFieldOfTypeWithException(packetPlayOutChat, byte.class);
		}catch(Exception e){
			return NMSUtils.getFirstFieldOfType(packetPlayOutChat, chatMessageType);
		}
	}
	
	@Override
	public boolean write(CorePlayers cp, Object packet){
		if(packet.getClass().equals(packetPlayOutChat)){
			try{
				byte a = 0;
				Object c = b.get(packet);
				if(c.getClass().equals(Byte.class)){
					a = (byte)c;
				}else{
					Enum<?> e = NMSUtils.getEnum(c.toString(), chatMessageType);
					a = (byte)e.ordinal();
				}
				if(a == 0 || a == 1){
					if(cancel(cp, packet)){ return false; }
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean cancel(CorePlayers cp, Object packet){
		ChatAPIInfo pl = cp.getInfo(ChatAPIInfo.class);
		if(pl.getPaused()){
			if(pl.getAllowed() > 0){
				pl.setAllowed(pl.getAllowed() - 1);
				return false;
			}
		}else if(pl.getAllowed() > 0){
			pl.setAllowed(pl.getAllowed() - 1);
			return false;
		}
		pl.getPackets().add(packet);
		while(pl.getPackets().size() > 150){
			pl.getPackets().remove(0);
		}
		return pl.getPaused();
	}
	
	@Override
	public boolean read(CorePlayers cp, Object packet){
		return true;
	}
	
}
