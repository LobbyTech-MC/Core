package me.dablakbandit.core.utils.packet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;

public interface IPacketUtils {

    void sendPacket(Player player, Object packet) throws Exception;

    void sendPackets(Player player, Collection<?> packets) throws Exception;

    void sendPacket(Collection<? extends Player> players, Object packet) throws Exception;

    void sendPackets(Collection<? extends Player> players, Collection<Object> packets) throws Exception;

    void sendPacketRaw(Collection<Object> cons, Object packet) throws Exception;

    Object getPlayerConnection(Player player) throws Exception;

    Object getHandle(Entity entity) throws Exception;

    Field getFieldConnection() throws Exception;

    Field getFieldChannel() throws Exception;

    Field getFieldNetworkManager() throws Exception;

    Constructor getConBlockPosition();

    Class<?> getClassPacket();
}
