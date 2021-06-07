package me.dablakbandit.core.server.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.CorePluginConfiguration;
import me.dablakbandit.core.server.packet.wrapped.WrappedPacket;
import me.dablakbandit.core.server.packet.wrapped.WrappedServerPacketListener;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class ServerHandler extends ChannelDuplexHandler{
	
	private boolean						active		= true;
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
	
	public boolean isActive(){
		return active;
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception{
		try{
			super.close(ctx, promise);
		}catch(Exception e){
			if(CorePluginConfiguration.CATCH_CANCELLED_PACKET.get()){
				Bukkit.getScheduler().runTask(CorePlugin.getInstance(), () -> {
					try{
						super.close(ctx, promise);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				});
			}else{
				active = false;
				CorePlugin.getInstance().getLogger().log(Level.WARNING, e.getMessage());
				e.printStackTrace();
				try{
					channel.close(promise);
				}catch(Exception e1){
					
				}
			}
		}
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception{
		if(!active){ return; }
		if(chc == null){
			chc = ctx;
		}
		try{
			if(write(ctx.channel(), msg)){
				super.write(ctx, msg, promise);
			}
		}catch(Exception e){
				if(CorePluginConfiguration.CATCH_CANCELLED_PACKET.get()){
					Bukkit.getScheduler().runTask(CorePlugin.getInstance(), () -> {
						try{
							super.write(ctx, msg, promise);
						}catch(Exception ex){
							ex.printStackTrace();
						}
					});
				}else{
					CorePlugin.getInstance().getLogger().log(Level.WARNING, e.getMessage());
					e.printStackTrace();
					try{
						channel.close(promise);
					}catch(Exception e1){

					}
				}
			}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
		if(!active){ return; }
		if(chc == null){
			chc = ctx;
		}
		try{
			if(read(ctx.channel(), msg)){
				super.channelRead(ctx, msg);
			}
		}catch(Exception e){
			if(CorePluginConfiguration.CATCH_CANCELLED_PACKET.get()){
				Bukkit.getScheduler().runTask(CorePlugin.getInstance(), () -> {
					try{
						super.channelRead(ctx, msg);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				});
			}else{
				CorePlugin.getInstance().getLogger().log(Level.WARNING, e.getMessage());
				e.printStackTrace();
				try{
					channel.close();
				}catch(Exception e1){

				}
			}
		}
	}
	
	private void ensureMainThread(Runnable runnable){
		if(!CorePluginConfiguration.CATCH_CANCELLED_PACKET.get()){
			runnable.run();
			return;
		}
	}
	
	public void bypassWrite(Object packet, boolean bypass) throws Exception{
		if(!Bukkit.getServer().isPrimaryThread()){
			Bukkit.getScheduler().runTask(CorePlugin.getInstance(), () -> {
				try{
					bypassWrite(packet, bypass);
				}catch(Exception e){
					e.printStackTrace();
				}
			});
			return;
		}
		if(!active){ return; }
		if(bypass){
			super.write(chc, packet, chc.newPromise());
		}else{
			write(chc, packet, chc.newPromise());
		}
	}
	
	public void bypassRead(Object packet, boolean bypass) throws Exception{
		if(!active){ return; }
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
