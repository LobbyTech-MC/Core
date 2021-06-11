package me.dablakbandit.core.server.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.PacketUtils;
import me.dablakbandit.core.utils.packet.types.LoginInStart;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.net.SocketAddress;
import java.util.*;

public class ServerPacketManager{
	
	private static ServerPacketManager manager = new ServerPacketManager();
	
	public static ServerPacketManager getInstance(){
		return manager;
	}
	
	private boolean						enabled		= false;
	
	private List<ServerPacketListener>	listeners	= new ArrayList<ServerPacketListener>();
	
	private Map<String, ServerHandler>	handlers	= new HashMap<String, ServerHandler>();
	
	private ServerPacketManager(){
		addListener(new ServerPacketListener(){
			@Override
			public boolean read(ServerHandler sh, Object packet){
				if(LoginInStart.classPacketLoginInStart.equals(packet.getClass())){
					try{
						addHandler(LoginInStart.getProfile(packet).getName(), sh);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				return true;
			}
			
			@Override
			public boolean write(ServerHandler sh, Object packet){
				return true;
			}
		});
	}
	
	public void enable(){
		if(!enabled){
			enabled = true;
			addServerConnectionChannel();
		}
	}
	
	public void disable(){
		CoreLog.info("------------------------------");
		CoreLog.info("[Core] Disabling ServerPacketManager");
		if(enabled){
			try{
				Server server = Bukkit.getServer();
				Object dedicatedserver = NMSUtils.getMethod(server.getClass(), "getServer").invoke(server);
				{
					ServerWrapper sw = new ServerWrapper(dedicatedserver);
					List currentlist = sw.getG();
					List newlist = Collections.synchronizedList(new ArrayList());
					for(Object o : currentlist){
						ChannelFuture cf = (ChannelFuture)o;
						SocketAddress in = cf.channel().localAddress();
						CoreLog.info("[Core] Disabling " + in);
						cf.channel().close().sync();
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		CoreLog.info("------------------------------");
	}
	
	public void addListener(ServerPacketListener spl){
		listeners.add(spl);
	}
	
	public List<ServerPacketListener> getListeners(){
		return listeners;
	}
	
	public void addHandler(String uuid, ServerHandler handler){
		handlers.put(uuid, handler);
	}
	
	public ServerHandler getHandler(String uuid){
		return handlers.get(uuid);
	}
	
	public void remove(String name){
		handlers.remove(name);
	}
	
	public void addServerConnectionChannel(){
		try{
			CoreLog.info("------------------------------");
			CoreLog.info("[Core] Enabling ServerPacketManager");
			Server server = Bukkit.getServer();
			Object dedicatedserver = NMSUtils.getMethod(server.getClass(), "getServer").invoke(server);
			{
				ServerWrapper sw = new ServerWrapper(dedicatedserver);
				sw.create();
				for(Player player : Bukkit.getOnlinePlayers()){
					try{
						Object handle = PacketUtils.getHandle(player);
						Object connection = PacketUtils.getFieldConnection().get(handle);
						Channel channel = (Channel)PacketUtils.getFieldChannel().get(PacketUtils.getFieldNetworkManager().get(connection));
						ServerHandler sh = new ServerHandler(channel);
						handlers.put(player.getName(), sh);
						try{
							channel.pipeline().remove("core_listener_server");
						}catch(Exception e){
							e.printStackTrace();
						}
						channel.pipeline().addBefore("packet_handler", "core_listener_server", sh);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			CoreLog.info("------------------------------");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
