package me.dablakbandit.core.server.packet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.CorePluginConfiguration;
import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.packet.types.PacketType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerWrapper{
	
	private static Class<?>		classServerConnection			= PacketType.getClassNMS("net.minecraft.server.network.ServerConnection", "ServerConnection");
	private static Class<?>		classMinecraftServer			= PacketType.getClassNMS("net.minecraft.server.MinecraftServer", "MinecraftServer");
	private static Class<?>		classNetworkManager				= PacketType.getClassNMS("net.minecraft.network.NetworkManager", "NetworkManager");
	private static Class<?>		classLegacyPingHandler			= PacketType.getClassNMS("net.minecraft.server.network.LegacyPingHandler","LegacyPingHandler");
	private static Class<?>		classPacketSplitter				= PacketType.getClassNMS("net.minecraft.network.PacketSplitter", "PacketSplitter");
	private static Class<?>		classPacketDecoder				= PacketType.getClassNMS("net.minecraft.network.PacketDecoder", "PacketDecoder");
	private static Class<?>		classPacketPrepender			= PacketType.getClassNMS("net.minecraft.network.PacketPrepender", "PacketPrepender");
	private static Class<?>		classPacketEncoder				= PacketType.getClassNMS("net.minecraft.network.PacketEncoder", "PacketEncoder");
	private static Class<?>		classEnumProtocolDirection		= PacketType.getClassNMS("net.minecraft.network.protocol.EnumProtocolDirection", "EnumProtocolDirection");
	private static Class<?>		classDedicatedServerSettings	= PacketType.getClassNMS("net.minecraft.server.dedicated.DedicatedServerSettings", "DedicatedServerSettings");
	private static Class<?>		classPropertyManager			= PacketType.getClassNMS("net.minecraft.server.dedicated.PropertyManager","PropertyManager");
	private static Class<?>		classDedicatedServerProperties			= PacketType.getClassNMS("net.minecraft.server.dedicated.DedicatedServerProperties","DedicatedServerProperties");

	private static Class<?>		classPacketListener				= PacketType.getClassNMS("net.minecraft.network.PacketListener","PacketListener");
	private static Class<?>		classLazyInitVar				= PacketType.getClassNMS("net.minecraft.util.LazyInitVar", "LazyInitVar");
	private static Class<?>		classHandshakeListener			= PacketType.getClassNMS("net.minecraft.server.network.HandshakeListener", "HandshakeListener");
	
	private static Constructor	conNetworkManager				= NMSUtils.getConstructor(classNetworkManager, classEnumProtocolDirection);
	private static Constructor	conLegacyPingHandler			= NMSUtils.getConstructorSilent(classLegacyPingHandler, classServerConnection);
	private static Constructor	conPacketDecoder				= NMSUtils.getConstructor(classPacketDecoder, classEnumProtocolDirection);
	private static Constructor	conPacketEncoder				= NMSUtils.getConstructor(classPacketEncoder, classEnumProtocolDirection);
	private static Constructor	conPacketSplitter				= NMSUtils.getConstructor(classPacketSplitter);
	private static Constructor	conPacketPrepender				= NMSUtils.getConstructor(classPacketPrepender);
	
	private static Constructor	conHandshakeListener			= NMSUtils.getConstructor(classHandshakeListener, classMinecraftServer, classNetworkManager);
	
	private static Method		getBoolean						= NMSUtils.getMethod(classPropertyManager, new String[]{"a", "getBoolean"}, String.class, boolean.class);

	private static Method		methodC							= NMSUtils.getMethod(classLazyInitVar, "c");
	
	static{
		if(methodC == null){
			methodC = NMSUtils.getMethod(classLazyInitVar, "a");
		}
	}
	
	private static Method methodSetPacketListener = NMSUtils.getMethod(classNetworkManager, "setPacketListener", classPacketListener);
	
	static{
		if(methodSetPacketListener == null){
			try{
				methodSetPacketListener = NMSUtils.getMethod(classNetworkManager, "a", classPacketListener);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static Field	fieldA				= NMSUtils.getField(classServerConnection, "a");
	private static Field	fieldB				= NMSUtils.getField(classServerConnection, "b");
	
	private static Field	fieldG				= NMSUtils.getFirstFieldOfType(classServerConnection, List.class);
	private static Field	fieldH				= NMSUtils.getLastFieldOfType(classServerConnection, List.class);
	
	private Object			dedicatedserver, serverconnection, propertymanager;
	
	private static Class<?>	classSpigotConfig	= NMSUtils.getClassSilent("org.spigotmc.SpigotConfig");
	private static Field	fieldLateBind		= NMSUtils.getFieldSilent(classSpigotConfig, "lateBind");
	
	public ServerWrapper(Object dedicatedserver){
		try{
			this.dedicatedserver = dedicatedserver;
			propertymanager = NMSUtils.getFirstFieldOfType(dedicatedserver.getClass(), classDedicatedServerSettings != null ? classDedicatedServerSettings : classPropertyManager).get(dedicatedserver);
			if(classDedicatedServerSettings != null){
				propertymanager = NMSUtils.getFirstFieldOfType(classDedicatedServerSettings, classDedicatedServerProperties).get(propertymanager);
			}
			serverconnection = NMSUtils.getFirstFieldOfType(classMinecraftServer, classServerConnection).get(dedicatedserver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private boolean isLateBind(){
		try{
			if(fieldLateBind != null){
				return (boolean) fieldLateBind.get(null);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return CorePluginConfiguration.LATE_BIND.get();
	}
	
	public void create(){
		try{
			if(serverconnection == null && isLateBind()){
				serverconnection = classServerConnection.getConstructors()[0].newInstance(dedicatedserver);
				Field currentConnection = NMSUtils.getFirstFieldOfType(classMinecraftServer, classServerConnection);
				if(fieldLateBind != null){
					fieldLateBind.set(null, false);
				}else{
					List currentlist = getG();
					for (Object o : currentlist) {
						ChannelFuture cf = (ChannelFuture)o;
						cf.channel().close().sync();
					}
				}
				currentConnection.set(dedicatedserver, serverconnection);

				String ip = (String)NMSUtils.getFirstFieldOfType(classMinecraftServer, String.class).get(dedicatedserver);
				int port = (int)NMSUtils.getFirstFieldOfType(classMinecraftServer, int.class).get(dedicatedserver);
				List newlist = Collections.synchronizedList(new ArrayList());
				ChannelFuture cf = create(InetAddress.getByName(ip), port);
				newlist.add(cf);
				setG(newlist);
			}else{
				List newlist = Collections.synchronizedList(new ArrayList());
				List currentlist = getG();
				for(Object o : currentlist){
					ChannelFuture cf = (ChannelFuture)o;
					newlist.add(create(cf));
				}
				setG(newlist);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ChannelFuture create(ChannelFuture current){
		try{
			SocketAddress in = current.channel().localAddress();
			CoreLog.info("[Core] Recreating " + in);
			current.channel().close().sync();
			ChannelFuture cf = ((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(getSocketClass())).childHandler(new ChannelInitializer(){
				protected void initChannel(Channel channel) throws Exception{
					try{
						channel.config().setOption(ChannelOption.TCP_NODELAY, true);
					}catch(ChannelException var3){
						
					}
					
					//@formatter:off
                    ChannelPipeline pipeline = channel.pipeline();
					pipeline.addLast("timeout", new ReadTimeoutHandler(30))
					.addLast("legacy_query", (ChannelHandler)conLegacyPingHandler.newInstance(serverconnection))
					.addLast("splitter", (ChannelHandler)conPacketSplitter.newInstance())
					.addLast("decoder", (ChannelHandler)conPacketDecoder.newInstance(NMSUtils.getEnum("SERVERBOUND", classEnumProtocolDirection)))
					.addLast("prepender", (ChannelHandler)conPacketPrepender.newInstance())
					.addLast("encoder", (ChannelHandler)conPacketEncoder.newInstance(NMSUtils.getEnum("CLIENTBOUND", classEnumProtocolDirection)));
                    //@formatter:on
					Object networkmanager = conNetworkManager.newInstance(NMSUtils.getEnum("SERVERBOUND", classEnumProtocolDirection));
					getH().add(networkmanager);
					ServerHandler sh = new ServerHandler(channel);
					channel.pipeline().addLast("core_listener_server", sh);
					channel.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
					methodSetPacketListener.invoke(networkmanager, conHandshakeListener.newInstance(dedicatedserver, networkmanager));
				}
			}).group((EventLoopGroup)methodC.invoke(getLazyInitVar())).localAddress(in)).bind().syncUninterruptibly();
					
			return cf;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public ChannelFuture create(InetAddress address, int port){
		try{
			CoreLog.info("[Core] Creating " + address + ":" + port);
			ChannelFuture cf = ((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(getSocketClass())).childHandler(new ChannelInitializer(){
				protected void initChannel(Channel channel) throws Exception{
					try{
						channel.config().setOption(ChannelOption.TCP_NODELAY, true);
					}catch(ChannelException var3){
						
					}
					
					//@formatter:off
					channel.pipeline()
							.addLast("timeout", new ReadTimeoutHandler(30))
							.addLast("legacy_query", (ChannelHandler) conLegacyPingHandler.newInstance(serverconnection))
							.addLast("splitter", (ChannelHandler) conPacketSplitter.newInstance())
							.addLast("decoder", (ChannelHandler) conPacketDecoder.newInstance(NMSUtils.getEnum("SERVERBOUND", classEnumProtocolDirection)))
							.addLast("prepender", (ChannelHandler) conPacketPrepender.newInstance())
							.addLast("encoder", (ChannelHandler) conPacketEncoder.newInstance(NMSUtils.getEnum("CLIENTBOUND", classEnumProtocolDirection)));
					//@formatter:on
					Object networkmanager = conNetworkManager.newInstance(NMSUtils.getEnum("SERVERBOUND", classEnumProtocolDirection));
					getH().add(networkmanager);
					ServerHandler sh = new ServerHandler(channel);
					channel.pipeline().addLast("core_listener_server", sh);
					channel.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
					methodSetPacketListener.invoke(networkmanager, conHandshakeListener.newInstance(dedicatedserver, networkmanager));
				}
			}).group((EventLoopGroup)methodC.invoke(getLazyInitVar())).localAddress(address, port)).bind().syncUninterruptibly();
			return cf;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List getG() throws Exception{
		return (List)fieldG.get(serverconnection);
	}
	
	public void setG(List list) throws Exception{
		fieldG.set(serverconnection, list);
	}
	
	public List getH() throws Exception{
		return (List)fieldH.get(serverconnection);
	}
	
	public Class<? extends ServerChannel> getSocketClass(){
		try{
			if((boolean)getBoolean.invoke(propertymanager, "use-native-transport", true) && (Epoll.isAvailable())){
				return EpollServerSocketChannel.class;
			}else{
				return NioServerSocketChannel.class;
			}
		}catch(Exception e){
			System.out.println(propertymanager);
			System.out.println(getBoolean);
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getLazyInitVar(){
		try{
			if((boolean)getBoolean.invoke(propertymanager, "use-native-transport", true) && (Epoll.isAvailable())){
				return fieldB.get(null);
			}else{
				return fieldA.get(null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List getActualG(){
		try{
			List g = getG();
			Field f = NMSUtils.getField(g.getClass().getSuperclass(), "list");
			return (List)f.get(g);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List getActualH(){
		try{
			List h = getH();
			Field f = NMSUtils.getField(h.getClass().getSuperclass(), "list");
			return (List)f.get(h);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
