package me.dablakbandit.core.utils.packet;

import io.netty.channel.Channel;
import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultPacketUtils implements IPacketUtils {

    private static DefaultPacketUtils instance = new DefaultPacketUtils();

    public static DefaultPacketUtils getInstance() {
        return instance;
    }

    public static Class<?> classEntityPlayer = NMSUtils.getClassSilent("net.minecraft.server.level.EntityPlayer");
    public static Class<?> classPlayerConnection = NMSUtils.getPossibleClass("net.minecraft.server.network.PlayerConnection", "net.minecraft.server.network.ServerGamePacketListenerImpl");
    public static Class<?> classPlayerConnectionSend = NMSUtils.getPossibleClass("net.minecraft.server.network.PlayerConnection", "net.minecraft.server.network.ServerPlayerConnection");
    public static Class<?> classPacket = NMSUtils.getClassSilent("net.minecraft.network.protocol.Packet");
    public static Class<?> classNetworkManager = NMSUtils.getClassSilent("net.minecraft.network.NetworkManager");

    public static Field fieldChannel = NMSUtils.getFirstFieldOfTypeSilent(classNetworkManager, Channel.class);
    public static Field fieldNetworkManager = NMSUtils.getFirstFieldOfTypeSilent(classPlayerConnection, classNetworkManager);

    public static Field fieldConnection = NMSUtils.getFirstFieldOfTypeSilent(classEntityPlayer, classPlayerConnection);

    public static Method methodSendPacket = NMSUtils.getMethodSilent(classPlayerConnectionSend, new String[]{"b", "a", "sendPacket", "send"}, classPacket);

	static {
		// if any are null
		if (classEntityPlayer == null || classPlayerConnection == null || classPlayerConnectionSend == null || classPacket == null || fieldConnection == null || methodSendPacket == null) {
			CoreLog.debug("classEntityPlayer: " + classEntityPlayer);
			CoreLog.debug("classPlayerConnection: " + classPlayerConnection);
			CoreLog.debug("classPlayerConnectionSend: " + classPlayerConnectionSend);
			CoreLog.debug("classPacket: " + classPacket);
			CoreLog.debug("fieldConnection: " + fieldConnection);
			CoreLog.debug("methodSendPacket: " + methodSendPacket);
		}
	}

    public void sendPacket(Player player, Object packet) throws Exception {
        if (!player.isOnline()) {
            return;
        }
        Object ppco = getPlayerConnection(player);
        methodSendPacket.invoke(ppco, packet);
    }

    public void sendPackets(Player player, Collection<?> packets) throws Exception {
        if (!player.isOnline()) {
            return;
        }
        Object entityplayer = getHandle(player);
        Object ppco = fieldConnection.get(entityplayer);
        for (Object packet : packets) {
            methodSendPacket.invoke(ppco, packet);
        }
    }

    public void sendPacket(Collection<? extends Player> players, Object packet) throws Exception {
        for (Player player : players) {
            sendPacket(player, packet);
        }
    }

    public void sendPackets(Collection<? extends Player> players, Collection<Object> packets) throws Exception {
        for (Player player : players) {
            sendPackets(player, packets);
        }
    }

    public void sendPacketRaw(Collection<Object> cons, Object packet) throws Exception {
        for (Object o : cons) {
            methodSendPacket.invoke(o, packet);
        }
    }

    public Object getPlayerConnection(Player player) throws Exception {
        Object entityplayer = getHandle(player);
        return fieldConnection.get(entityplayer);
    }

	public static Map<Class, Method> mapGetHandle = new HashMap<Class, Method>();

	public Object getHandle(Entity entity) throws Exception {
		Method m = mapGetHandle.get(entity.getClass());
		if (m == null) {
			m = NMSUtils.getMethod(entity.getClass(), "getHandle");
			mapGetHandle.put(entity.getClass(), m);
		}
		return m.invoke(entity);
	}

	@Override
	public Field getFieldConnection() throws Exception {
		return fieldConnection;
	}


    @Override
    public Class<?> getClassPacket() {
        return classPacket;
    }

    @Override
	public Field getFieldChannel() throws Exception {
		return fieldChannel;
	}

	@Override
	public Field getFieldNetworkManager() throws Exception {
		return fieldNetworkManager;
	}

    @Override
    public Constructor getConBlockPosition() {
        return NMSUtils.getConstructor(NMSUtils.getClass("net.minecraft.core.BlockPosition"), int.class, int.class, int.class);
    }
}
