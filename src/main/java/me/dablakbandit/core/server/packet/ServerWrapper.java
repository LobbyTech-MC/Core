package me.dablakbandit.core.server.packet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import me.dablakbandit.core.utils.NMSUtils;

public class ServerWrapper{
	
	private static Class<?>		classServerConnection		= NMSUtils.getNMSClass("ServerConnection");
	private static Class<?>		classMinecraftServer		= NMSUtils.getNMSClass("MinecraftServer");
	private static Class<?>		classNetworkManager			= NMSUtils.getNMSClass("NetworkManager");
	private static Class<?>		classLegacyPingHandler		= NMSUtils.getNMSClass("LegacyPingHandler");
	private static Class<?>		classPacketSplitter			= NMSUtils.getNMSClass("PacketSplitter");
	private static Class<?>		classPacketDecoder			= NMSUtils.getNMSClass("PacketDecoder");
	private static Class<?>		classPacketPrepender		= NMSUtils.getNMSClass("PacketPrepender");
	private static Class<?>		classPacketEncoder			= NMSUtils.getNMSClass("PacketEncoder");
	private static Class<?>		classEnumProtocolDirection	= NMSUtils.getNMSClass("EnumProtocolDirection");
	private static Class<?>		classPropertyManager		= NMSUtils.getNMSClass("PropertyManager");
	private static Class<?>		classPacketListener			= NMSUtils.getNMSClass("PacketListener");
	private static Class<?>		classLazyInitVar			= NMSUtils.getNMSClass("LazyInitVar");
	private static Class<?>		classHandshakeListener		= NMSUtils.getNMSClass("HandshakeListener");
	
	private static Constructor	conNetworkManager			= NMSUtils.getConstructor(classNetworkManager, classEnumProtocolDirection);
	private static Constructor	conLegacyPingHandler		= NMSUtils.getConstructor(classLegacyPingHandler, classServerConnection);
	private static Constructor	conPacketDecoder			= NMSUtils.getConstructor(classPacketDecoder, classEnumProtocolDirection);
	private static Constructor	conPacketEncoder			= NMSUtils.getConstructor(classPacketEncoder, classEnumProtocolDirection);
	private static Constructor	conPacketSplitter			= NMSUtils.getConstructor(classPacketSplitter);
	private static Constructor	conPacketPrepender			= NMSUtils.getConstructor(classPacketPrepender);
	
	private static Constructor	conHandshakeListener		= NMSUtils.getConstructor(classHandshakeListener, classMinecraftServer, classNetworkManager);
	
	private static Method		getBoolean					= NMSUtils.getMethod(classPropertyManager, "getBoolean", String.class, boolean.class);
	private static Method		methodC						= NMSUtils.getMethod(classLazyInitVar, "c");
	
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
	
	private static Field	fieldA	= NMSUtils.getField(classServerConnection, "a");
	private static Field	fieldB	= NMSUtils.getField(classServerConnection, "b");
	
	private static Field	fieldG	= NMSUtils.getFirstFieldOfType(classServerConnection, List.class);
	private static Field	fieldH	= NMSUtils.getLastFieldOfType(classServerConnection, List.class);
	
	private Object			dedicatedserver, serverconnection, propertymanager;
	
	public ServerWrapper(Object dedicatedserver){
		try{
			this.dedicatedserver = dedicatedserver;
			propertymanager = NMSUtils.getFirstFieldOfType(dedicatedserver.getClass(), classPropertyManager).get(dedicatedserver);
			serverconnection = NMSUtils.getFirstFieldOfType(classMinecraftServer, classServerConnection).get(dedicatedserver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ChannelFuture create(ChannelFuture current){
		try{
			SocketAddress in = current.channel().localAddress();
			System.out.println("[Core] Recreating " + in);
			current.channel().close().sync();
			ChannelFuture cf = ((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(getSocketClass())).childHandler(new ChannelInitializer(){
				protected void initChannel(Channel channel) throws Exception{
					try{
						channel.config().setOption(ChannelOption.TCP_NODELAY, true);
					}catch(ChannelException var3){
						
					}
					
					//@formatter:off
                    channel	.pipeline()
                            .addLast("timeout", new ReadTimeoutHandler(30))
                            .addLast("legacy_query", (ChannelHandler)conLegacyPingHandler.newInstance(serverconnection))
                            .addLast("splitter", (ChannelHandler)conPacketSplitter.newInstance())
                            .addLast("decoder", (ChannelHandler)conPacketDecoder.newInstance(NMSUtils.getEnum("SERVERBOUND", classEnumProtocolDirection)))
                            .addLast("prepender", (ChannelHandler)conPacketPrepender.newInstance())
                            .addLast("encoder", (ChannelHandler)conPacketEncoder.newInstance(NMSUtils.getEnum("CLIENTBOUND", classEnumProtocolDirection)));
                    //@formatter:on
					Object networkmanager = conNetworkManager.newInstance(NMSUtils.getEnum("SERVERBOUND", classEnumProtocolDirection));
					getH().add(networkmanager);
					channel.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
					ServerHandler sh = new ServerHandler(channel);
					channel.pipeline().addBefore("packet_handler", "core_listener_server", sh);
					methodSetPacketListener.invoke(networkmanager, conHandshakeListener.newInstance(dedicatedserver, networkmanager));
				}
			}).group((EventLoopGroup)methodC.invoke(getLazyInitVar())).localAddress(in)).bind().syncUninterruptibly();
					
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
			if((Epoll.isAvailable()) && (boolean)getBoolean.invoke(propertymanager, "use-native-transport", true)){
				return EpollServerSocketChannel.class;
			}else{
				return NioServerSocketChannel.class;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getLazyInitVar(){
		try{
			if((Epoll.isAvailable()) && (boolean)getBoolean.invoke(propertymanager, "use-native-transport", true)){
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
