package me.dablakbandit.core.server.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.dablakbandit.core.server.packet.wrapped.WrappedPacket;
import me.dablakbandit.core.server.packet.wrapped.WrappedServerPacketListener;

public class ServerHandler extends ChannelDuplexHandler{
	
	private ChannelHandlerContext		chc;
	
	private List<ServerPacketListener>	listeners	= Collections.synchronizedList(new ArrayList<ServerPacketListener>());
	
	public void addListener(ServerPacketListener listener){
		listeners.add(listener);
	}
	
	private Channel channel;
	
	public ServerHandler(Channel channel){
		this.channel = channel;
	}
	
	public Channel getChannel(){
		return channel;
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception{
		if(chc == null){
			chc = ctx;
		}
		if(write(ctx.channel(), msg)){
			super.write(ctx, msg, promise);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
		if(chc == null){
			chc = ctx;
		}
		if(read(ctx.channel(), msg)){
			super.channelRead(ctx, msg);
		}
	}
	
	public void bypassWrite(Object packet, boolean bypass) throws Exception{
		if(bypass){
			super.write(chc, packet, chc.newPromise());
		}else{
			write(chc, packet, chc.newPromise());
		}
	}
	
	public void bypassRead(Object packet, boolean bypass) throws Exception{
		if(bypass){
			super.channelRead(chc, packet);
		}else{
			channelRead(chc, packet);
		}
	}
	
	public boolean write(Channel channel, Object msg){
		boolean write = true;
		WrappedPacket wrappedPacket = new WrappedPacket(msg);
		for(ServerPacketListener listener : ServerPacketManager.getInstance().getListeners()){
			if(listener instanceof WrappedServerPacketListener){
				WrappedServerPacketListener wrappedListener = (WrappedServerPacketListener)listener;
				if(wrappedListener.isWriteWhitelisted(wrappedPacket) && !wrappedListener.writeWrapped(this, wrappedPacket)){
					write = false;
				}
			}else if(!listener.write(this, msg)){
				write = false;
			}
		}
		for(ServerPacketListener listener : listeners){
			if(listener instanceof WrappedServerPacketListener){
				WrappedServerPacketListener wrappedListener = (WrappedServerPacketListener)listener;
				if(wrappedListener.isWriteWhitelisted(wrappedPacket) && !wrappedListener.writeWrapped(this, wrappedPacket)){
					write = false;
				}
			}else if(!listener.write(this, msg)){
				write = false;
			}
		}
		return write;
	}
	
	public boolean read(Channel channel, Object msg){
		boolean read = true;
		WrappedPacket wrappedPacket = new WrappedPacket(msg);
		for(ServerPacketListener listener : ServerPacketManager.getInstance().getListeners()){
			if(listener instanceof WrappedServerPacketListener){
				WrappedServerPacketListener wrappedListener = (WrappedServerPacketListener)listener;
				if(wrappedListener.isReadWhitelisted(wrappedPacket) && !wrappedListener.readWrapped(this, wrappedPacket)){
					read = false;
				}
			}else if(!listener.read(this, msg)){
				read = false;
			}
		}
		for(ServerPacketListener listener : listeners){
			if(listener instanceof WrappedServerPacketListener){
				WrappedServerPacketListener wrappedListener = (WrappedServerPacketListener)listener;
				if(wrappedListener.isReadWhitelisted(wrappedPacket) && !wrappedListener.readWrapped(this, wrappedPacket)){
					read = false;
				}
			}else if(!listener.read(this, msg)){
				read = false;
			}
		}
		return read;
	}
}
