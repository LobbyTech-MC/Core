package me.dablakbandit.core.players.packets;

import io.netty.channel.Channel;
import me.dablakbandit.core.utils.PacketUtils;

public class RemoveThread implements Runnable{
	
	private PacketInfo	info;
	private Channel		channel;
	
	public RemoveThread(final PacketInfo pl){
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
			channel.pipeline().remove("core_listener_player");
			System.out.print("[Core] Removed channel for " + info.getPlayers().getUUIDString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
