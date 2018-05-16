package me.dablakbandit.core.players.packets;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.dablakbandit.core.players.CorePlayers;

public class PacketHandler extends ChannelDuplexHandler{
	
	private List<PacketListener>	listeners	= new ArrayList<PacketListener>();
	private ChannelHandlerContext	chc;
	private CorePlayers				pl;
	
	public PacketHandler(CorePlayers pl){
		this.pl = pl;
	}
	
	public CorePlayers getPlayers(){
		return pl;
	}
	
	public void addListener(PacketListener pl){
		listeners.add(pl);
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception{
		if(chc == null){
			chc = ctx;
		}
		if(write(msg)){
			super.write(ctx, msg, promise);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
		if(chc == null){
			chc = ctx;
		}
		if(read(msg)){
			super.channelRead(ctx, msg);
		}
	}
	
	public void bypass(Object packet, boolean bypass) throws Exception{
		if(bypass){
			super.write(chc, packet, chc.newPromise());
		}else{
			write(chc, packet, chc.newPromise());
		}
	}
	
	public boolean write(Object msg){
		boolean write = true;
		for(PacketListener listener : listeners){
			if(!listener.write(pl, msg)){
				write = false;
			}
		}
		return write;
	}
	
	public boolean read(Object msg){
		boolean read = true;
		for(PacketListener listener : listeners){
			if(!listener.read(pl, msg)){
				read = false;
			}
		}
		return read;
	}
	
}
