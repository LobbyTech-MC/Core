package me.dablakbandit.core.players.chatapi;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.event.OpenChatMessageEvent;
import me.dablakbandit.core.players.packets.PacketListener;
import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.PacketUtils;
import me.dablakbandit.core.utils.packet.types.PacketType;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class ChatAPIListener extends PacketListener{
	
	private static ChatAPIListener instance = new ChatAPIListener();
	
	public static ChatAPIListener getInstance(){
		return instance;
	}
	
	protected static Class<?> packetPlayOutPlayerChat = getPacketPlayOutChat();
	protected static Class<?> packetDisguisedChatPacket = NMSUtils.getClassSilent("net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket");
	protected static Class<?> packetSystemChatPacket= NMSUtils.getClassSilent("net.minecraft.network.protocol.game.ClientboundSystemChatPacket");

	protected static Class<?> getPacketPlayOutChat(){
		Class<?> clazz = NMSUtils.getClassSilent("net.minecraft.network.protocol.game.ClientboundPlayerChatPacket");
		if(clazz != null){
			return clazz;
		}
		clazz = PacketType.getClassNMS("net.minecraft.network.protocol.game.PacketPlayOutChat", "PacketPlayOutChat");
		if(clazz==null){
			try{
				clazz =Class.forName("net.minecraft.network.play.server.S02PacketChat");
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return clazz;
	}
	
	private static Class<?>	chatMessageType	= getChatMessageTypeClass();

	protected static Class<?> getChatMessageTypeClass(){
		Class<?> classChatMessageType = PacketType.getClassNMS("net.minecraft.network.chat.ChatMessageType", "ChatMessageType");
		try{
			Class<?> bClass = NMSUtils.getInnerClassWithException(classChatMessageType, "b");
			if(bClass!=null){
				classChatMessageType = bClass;
			}
		}catch (Exception e){
		}
		return classChatMessageType;
	}

	protected static Field playerPacketField = getPlayerPacketField();
	protected static Field disguisedChatPacketField = NMSUtils.getFirstFieldOfTypeSilent(packetDisguisedChatPacket, chatMessageType);
	protected static Field systemChatPacketField = NMSUtils.getFirstFieldOfTypeSilent(packetSystemChatPacket, boolean.class);

	protected static Field chatMessageTypeFieldA = NMSUtils.getFirstFieldOfTypeSilent(chatMessageType, int.class);

	protected static Field getPlayerPacketField(){
		try{
			return NMSUtils.getFirstFieldOfTypeWithException(packetPlayOutPlayerChat, byte.class);
		}catch(Exception e){
			try{
				return NMSUtils.getFirstFieldOfTypeWithException(packetPlayOutPlayerChat, chatMessageType);
			}catch (Exception e1){
				return NMSUtils.getFirstFieldOfType(packetPlayOutPlayerChat, int.class);
			}
		}
	}
	
	@Override
	public boolean write(CorePlayers cp, Object packet){
		if(packet.getClass().equals(packetPlayOutPlayerChat)){
			try{
				byte a = 0;
				Object c = playerPacketField.get(packet);
				if(c.getClass().equals(Byte.class)){
					a = (byte)c;
				}else if(chatMessageType.isEnum()){
					Enum<?> e = NMSUtils.getEnum(c.toString(), chatMessageType);
					a = (byte)e.ordinal();
				}else{
					a = ((Integer) chatMessageTypeFieldA.get(c)).byteValue();
				}
				if(a == 0 || a == 1){
					if(cancel(cp, packet)){ return false; }
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(packet.getClass().equals(packetDisguisedChatPacket)){
			try{
				byte a = 0;
				Object c = disguisedChatPacketField.get(packet);
				a = ((Integer) chatMessageTypeFieldA.get(c)).byteValue();
				if(a == 0 || a == 1 || a == 4){
					if(cancel(cp, packet)){ return false; }
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(packet.getClass().equals(packetSystemChatPacket)){
			try{
				byte a = 0;
				boolean value = (boolean) systemChatPacketField.get(packet);
				if(!value){
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
		if(pl.getPaused()){
			try{
				OpenChatMessageEvent event = new OpenChatMessageEvent(cp, PacketUtils.Chat.getMessage(packet), !Bukkit.isPrimaryThread());
				Bukkit.getPluginManager().callEvent(event);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return pl.getPaused();
	}
	
	@Override
	public boolean read(CorePlayers cp, Object packet){
		return true;
	}
	
}
