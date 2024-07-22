package me.dablakbandit.core.server.packet;

import io.netty.channel.Channel;
import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.PacketUtils;
import me.dablakbandit.core.utils.packet.types.LoginInStart;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.*;

public class ServerPacketManager{
	
	private static ServerPacketManager manager = new ServerPacketManager();
	
	public static ServerPacketManager getInstance(){
		return manager;
	}
	
	private boolean						enabled		= false;
	
	private List<ServerPacketListener>	listeners	= new ArrayList<ServerPacketListener>();
	
	private Map<String, ServerHandler>	handlers	= new HashMap<>();
	private Map<String, List<ServerHandler>> pendingHandlers = Collections.synchronizedMap(new HashMap<>());
	
	private ServerPacketManager(){
		addListener(new ServerPacketListener(){
			@Override
			public boolean read(ServerHandler sh, Object packet){
				if(LoginInStart.classPacketLoginInStart.equals(packet.getClass())){
					try{
						addPendingHandler(LoginInStart.getName(packet), sh);
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
//					ServerWrapper sw = new ServerWrapper(dedicatedserver);
//					for (Channel channel : sw.getServerChannels(Bukkit.getServer())) {
//						SocketAddress in = channel.localAddress();
//						CoreLog.info("[Core] Disabling " + in);
//						channel.pipeline().remove("core_listener_server");
//					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
//			enabled = false;
		}
		CoreLog.info("------------------------------");
	}
	
	public void addListener(ServerPacketListener spl){
		listeners.add(spl);
	}
	
	public List<ServerPacketListener> getListeners(){
		return listeners;
	}
	
	public void addPendingHandler(String name, ServerHandler handler){
		pendingHandlers.computeIfAbsent(name, k -> Collections.synchronizedList(new ArrayList<>())).add(handler);
	}
	
	public ServerHandler getHandler(Player player){
		List<ServerHandler> serverHandlers = pendingHandlers.get(player.getName());
		ServerHandler handler = serverHandlers.stream().filter(serverHandler -> serverHandler.getChannel().isActive() && serverHandler.getChannel().isOpen()).findFirst().orElse(null);
		pendingHandlers.remove(player.getName());
		return handler;
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
						ServerHandler sh = new ServerHandler(channel, PacketUtils.getClassPacket());
						addPendingHandler(player.getName(), sh);
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
