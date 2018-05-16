package me.dablakbandit.core.players.packets;

import io.netty.channel.Channel;
import me.dablakbandit.core.utils.PacketUtils;

public class AddThread implements Runnable{
	
	private PacketInfo	info;
	private Channel		channel;
	
	public AddThread(final PacketInfo pl){
		this.info = pl;
		try{
			Object handle = PacketUtils.getHandle(pl.getPlayers().getPlayer());
			Object connection = PacketUtils.fieldConnection.get(handle);
			channel = (Channel)PacketUtils.fieldChannel.get(PacketUtils.fieldNetworkManager.get(connection));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		try{
			channel.pipeline().addBefore("packet_handler", "core_listener_player", info.getHandler());
			System.out.print("[Core] Added channel for " + info.getPlayers().getUUIDString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
