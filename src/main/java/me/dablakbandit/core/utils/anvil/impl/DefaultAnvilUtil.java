package me.dablakbandit.core.utils.anvil.impl;

import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.PacketUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import static me.dablakbandit.core.utils.NMSUtils.getConstructorSilent;

public class DefaultAnvilUtil implements IAnvilUtil {
    public static Class<?> classEntity = NMSUtils.getClassSilent("net.minecraft.world.entity.Entity");
    public static Class<?> classEntityHuman = NMSUtils.getClassSilent("net.minecraft.world.entity.player.EntityHuman");
    public static Class<?> classEntityPlayer = NMSUtils.getClassSilent("net.minecraft.server.level.EntityPlayer");

    private static final Class<?> classWorld = NMSUtils.getClassSilent("net.minecraft.world.level.World");
    private static final Class<?> classContainer = NMSUtils.getClassSilent("net.minecraft.world.inventory.Container");
    private static final Class<?> classPlayerInventory = NMSUtils.getClassSilent("net.minecraft.world.entity.player.PlayerInventory");
    private static final Class<?> classContainerAnvil = NMSUtils.getClassSilent("net.minecraft.world.inventory.ContainerAnvil");
    private static final Class<?> classBlockPosition = NMSUtils.getClassSilent("net.minecraft.core.BlockPosition");
    private static final Class<?> classPacketPlayOutOpenWindow = NMSUtils.getClassSilent("net.minecraft.network.protocol.game.PacketPlayOutOpenWindow");
    private static final Class<?> classChatMessage = NMSUtils.getClassSilent("net.minecraft.network.chat.ChatMessage");
    private static final Class<?> classContainers = NMSUtils.getClassSilent("net.minecraft.world.inventory.Containers");
    private static final Class<?> classContainerSynchronizer = NMSUtils.getClassSilent("net.minecraft.world.inventory.ContainerSynchronizer");

    private static final Class<?> classContainerAccess = NMSUtils.getClassSilent("net.minecraft.world.inventory.ContainerAccess");
    private static final Method atContainerAccess = NMSUtils.getMethod(classContainerAccess, new String[]{"a","at"}, classWorld, classBlockPosition);
    private static final Method getBukkitView = NMSUtils.getMethodSilent(classContainer, "getBukkitView");

    private static final Constructor<?> conContainerAnvil = getConstructorSilent(classContainerAnvil, int.class, NMSUtils.getClassSilent("net.minecraft.world.entity.player.PlayerInventory"), classContainerAccess);

    private static final Constructor<?> conBlockPosition = getConstructorSilent(classBlockPosition, int.class, int.class, int.class);
    private static final Class<?> classIChatBaseComponent = NMSUtils.getClassSilent("net.minecraft.network.chat.IChatBaseComponent");
    private static final Constructor<?> conPacketPlayOutOpenWindow = getConstructorSilent(classPacketPlayOutOpenWindow, int.class, classContainers, classIChatBaseComponent);

    private static final Constructor<?> conChatMessage = getConstructorSilent(classChatMessage, String.class, Object[].class);

    private static final Field fieldCheckReachable = NMSUtils.getFieldSilent(classContainer, "checkReachable");
    private static final Field fieldTitle = NMSUtils.getFieldSilent(classContainer, "title");
    private static final Field fieldWindowID = NMSUtils.getFieldSilent(classContainer, "j");
    private static final Field fieldInventory = NMSUtils.getFirstFieldOfTypeSilent(classEntityHuman, classPlayerInventory);
    private static final Field fieldActiveContainer = NMSUtils.getFirstFieldOfTypeSilent(classEntityHuman, classContainer);
    private static final Field fieldWorld = NMSUtils.getFirstFieldOfTypeSilent(classEntity, classWorld);

    private static final Method nextContainerCounter = NMSUtils.getMethodSilent(classEntityPlayer, "nextContainerCounter");
    private static final Method initMenu = NMSUtils.getMethod(classEntityPlayer, new String[]{"a","initMenu"}, classContainer);

    private static final Object blockPosition = NMSUtils.newInstance(conBlockPosition, 0, 0, 0);

    private static final Class<?> classCraftContainer = NMSUtils.getOBCClassSilent("inventory.CraftContainer");
    private static final Field fieldANVIL = NMSUtils.getFieldSilent(classContainers, "h");
    private static Object objectANVIL;

    static {
        try {
            objectANVIL = fieldANVIL.get(null);
        } catch (Exception ignored) {

        }
    }

    public static Object getObjectAnvil() {
        return objectANVIL;
    }

    private static Object getPacketPlayOutOpenWindow(int id) throws Exception {
        return getPacketPlayOutOpenWindow("Enter", id);
    }

    private static Object getPacketPlayOutOpenWindow(String message, int id) throws Exception {
        Object chatMessage = NMSUtils.newInstance(conChatMessage, message, new Object[0]);
        return conPacketPlayOutOpenWindow.newInstance(id, objectANVIL, chatMessage);
    }

    public void open(Player player, Consumer<Inventory> after) {
        open(player, "Enter", after);
    }

    public void open(Player player, String message, Consumer<Inventory> after) {
        try {
            Object nmsPlayer = NMSUtils.getHandle(player);
            Object anvilcon;
            Object at = atContainerAccess.invoke(null, fieldWorld.get(nmsPlayer), blockPosition);
            System.out.println(nmsPlayer);
            anvilcon = conContainerAnvil.newInstance(0, fieldInventory.get(nmsPlayer), at);
            if (fieldTitle != null) {
                Object chatMessage = NMSUtils.newInstance(conChatMessage, message, new Object[0]);
                fieldTitle.set(anvilcon, chatMessage);
            }
            fieldCheckReachable.set(anvilcon, false);

            int c = (Integer) nextContainerCounter.invoke(nmsPlayer);
            PacketUtils.sendPacket(player, getPacketPlayOutOpenWindow(message, c));
            fieldActiveContainer.set(nmsPlayer, anvilcon);
            fieldWindowID.set(anvilcon, c);
            initMenu.invoke(nmsPlayer, anvilcon);
            after.accept(((InventoryView) getBukkitView.invoke(anvilcon)).getTopInventory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void open(Player player, Runnable after) {
        open(player, (i)->{
            after.run();
        });
    }


}
