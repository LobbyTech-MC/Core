package me.dablakbandit.core.players.packets;

import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.server.packet.ServerHandler;
import me.dablakbandit.core.server.packet.ServerPacketListener;
import me.dablakbandit.core.server.packet.ServerPacketManager;
import me.dablakbandit.core.server.packet.cancel.CancelPacketListener;
import me.dablakbandit.core.server.packet.wrapped.WrappedPacket;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketHandler extends ServerPacketListener{
	
	private List<PacketListener>	listeners	= Collections.synchronizedList(new ArrayList<PacketListener>());
	private CorePlayers				pl;
	private ServerHandler			handler;
	
	public PacketHandler(CorePlayers pl){
		this.pl = pl;
		this.handler = ServerPacketManager.getInstance().getHandler(pl.getPlayer());
		if(this.handler != null){
			handler.addListener(this);
		}else{
			Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), new Runnable(){
				@Override
				public void run(){
					pl.getPlayer().kickPlayer("[Core] Packet handler failed, please rejoin");
				}
			}, 0);
		}
	}
	
	public ServerHandler getHandler(){
		return handler;
	}
	
	public CorePlayers getPlayers(){
		return pl;
	}
	
	public void addListener(PacketListener pl){
		synchronized(listeners){
			listeners.add(pl);
		}
	}
	
	@Deprecated
	public void bypass(Object packet, boolean bypass) throws Exception{
		bypassWrite(packet, bypass);
	}
	
	public void bypassWrite(Object packet, boolean bypass) throws Exception{
		handler.bypassWrite(packet, bypass);
	}
	
	public void bypassRead(Object packet, boolean bypass) throws Exception{
		handler.bypassRead(packet, bypass);
	}
	
	@Override
	public boolean read(ServerHandler sh, Object packet){
		boolean read = true;
		WrappedPacket wrappped = new WrappedPacket(packet);
		synchronized(listeners){
			for(PacketListener listener : listeners){
				if(listener instanceof WrappedPacketListener){
					WrappedPacketListener wrappedListener = (WrappedPacketListener)listener;
					if(wrappedListener.isReadWhitelisted(wrappped) && !wrappedListener.readWrapped(pl, wrappped)){
						read = false;
					}
				}else{
					if(!listener.read(pl, packet)){
						read = false;
						if(listener instanceof CancelPacketListener){
							break;
						}
					}
				}
			}
		}
		return read;
	}
	
	@Override
	public boolean write(ServerHandler sh, Object packet){
		boolean write = true;
		WrappedPacket wrappped = new WrappedPacket(packet);
		synchronized(listeners){
			for(PacketListener listener : listeners){
				if(listener instanceof WrappedPacketListener){
					WrappedPacketListener wrappedListener = (WrappedPacketListener)listener;
					if(wrappedListener.isWriteWhitelisted(wrappped) && !wrappedListener.writeWrapped(pl, wrappped)){
						write = false;
					}
				}else{
					if(!listener.write(pl, packet)){
						write = false;
						if(listener instanceof CancelPacketListener){
							break;
						}
					}
				}
			}
		}
		return write;
	}
}
