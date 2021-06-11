package me.dablakbandit.core.utils.anvil.impl;

import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.PacketUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import static me.dablakbandit.core.utils.NMSUtils.*;

public class _16AnvilUtil implements IAnvilUtil{
    public static Class<?>			classEntity						= getNMSClass("Entity");
    public static Class<?> classEntityHuman = NMSUtils.getNMSClass("EntityHuman");
    public static Class<?> classEntityPlayer = NMSUtils.getNMSClass("EntityPlayer");

    private static Class<?>			classWorld						= NMSUtils.getNMSClass("World");
    private static Class<?>			classContainer					= NMSUtils.getNMSClass("Container");
    private static Class<?>			classContainerAnvil				= NMSUtils.getNMSClass("ContainerAnvil");
    private static Class<?>			classBlockPosition				= NMSUtils.getNMSClassSilent("BlockPosition");
    private static Class<?>			classPacketPlayOutOpenWindow	= NMSUtils.getNMSClass("PacketPlayOutOpenWindow");
    private static Class<?>			classChatMessage				= NMSUtils.getNMSClass("ChatMessage");
    private static Class<?>			classContainers					= NMSUtils.getNMSClassSilent("Containers");

    private static Class<?>			classContainerAccess			= NMSUtils.getNMSClassSilent("ContainerAccess");
    private static Method atContainerAccess				= NMSUtils.getMethodSilent(classContainerAccess, "at", classWorld, classBlockPosition);
    private static final Method getBukkitView = NMSUtils.getMethod(classContainer, "getBukkitView");

    private static Constructor<?> conContainerAnvil				= getConContainerAnvil();

    private static Constructor<?> getConContainerAnvil(){
        if(classContainers != null){ return getConstructorSilent(classContainerAnvil, int.class, NMSUtils.getNMSClass("PlayerInventory"), classContainerAccess); }
        if(classBlockPosition != null){ return getConstructorSilent(classContainerAnvil, NMSUtils.getNMSClass("PlayerInventory"), classWorld, classBlockPosition, classEntityHuman); }
        return getConstructorSilent(classContainerAnvil, NMSUtils.getNMSClass("PlayerInventory"), classWorld, int.class, int.class, int.class, classEntityHuman);
    }

    private static Constructor<?>	conBlockPosition			= getConstructorSilent(classBlockPosition, int.class, int.class, int.class);
    private static Class<?>			classIChatBaseComponent		= NMSUtils.getNMSClass("IChatBaseComponent");
    private static Constructor<?>	conPacketPlayOutOpenWindow	= getConPacketPlayOutOpenWindow();

    private static Constructor<?> getConPacketPlayOutOpenWindow(){
        try{
            return getConstructorWithException(classPacketPlayOutOpenWindow, int.class, classContainers, classIChatBaseComponent);
        }catch(Exception e){

        }
        try{
            return getConstructorWithException(classPacketPlayOutOpenWindow, int.class, String.class, classIChatBaseComponent, int.class);
        }catch(Exception e){
            try{
                return getConstructorWithException(classPacketPlayOutOpenWindow, int.class, int.class, String.class, int.class, boolean.class);
            }catch(Exception e1){
                e.printStackTrace();
                e1.printStackTrace();
            }
        }
        return null;
    }

    private static Constructor<?>	conChatMessage				= getConstructorSilent(classChatMessage, String.class, Object[].class);

    private static Field fieldCheckReachable			= NMSUtils.getField(classContainer, "checkReachable");
    private static Field			fieldTitle					= NMSUtils.getFieldSilent(classContainer, "title");
    private static Field			fieldWindowID				= NMSUtils.getField(classContainer, "windowId");
    private static Field			fieldInventory				= NMSUtils.getField(classEntityHuman, "inventory");
    private static Field			fieldActiveContainer		= NMSUtils.getField(classEntityHuman, "activeContainer");
    private static Field			fieldWorld					= NMSUtils.getField(classEntity, "world");

    private static Method			nextContainerCounter		= getMethodSilent(classEntityPlayer, "nextContainerCounter");
    private static Method			addSlotListener				= getMethodSilent(classContainer, "addSlotListener", NMSUtils.getNMSClass("ICrafting"));

    private static Object			blockPosition				= classBlockPosition != null ? NMSUtils.newInstance(conBlockPosition, 0, 0, 0) : null;
    private static Object			chatMessage					= NMSUtils.newInstance(conChatMessage, "Enter", new Object[0]);

    private static Class			classItemStack				= NMSUtils.getNMSClass("ItemStack");

    private static Class			classPacketPlayOutSetSlot	= NMSUtils.getNMSClass("PacketPlayOutSetSlot");
    private static Constructor<?>	conPacketPlayOutSetSlot		= getConstructor(classPacketPlayOutSetSlot, int.class, int.class, classItemStack);

    private static Class<?>			classCraftContainer			= NMSUtils.getOBCClassSilent("inventory.CraftContainer");
    private static Method			getNotchInventoryType		= NMSUtils.getMethodSilent(classCraftContainer, "getNotchInventoryType", InventoryType.class);
    private static Field			fieldANVIL					= NMSUtils.getFieldSilent(classContainers, "ANVIL");
    private static Object			objectANVIL;

    static{
        try{
            objectANVIL = fieldANVIL.get(null);
        }catch(Exception e){

        }
    }

    public static Object getType(InventoryType type) throws Exception{
        if(objectANVIL != null){ return objectANVIL; }
        return getNotchInventoryType.invoke(null, type);
    }

    private static Object getPacketPlayOutOpenWindow(int id) throws Exception{
        switch(conPacketPlayOutOpenWindow.getParameterTypes().length){
            case 3:
                Object type = getType(InventoryType.ANVIL);
                return conPacketPlayOutOpenWindow.newInstance(id, type, chatMessage);
            case 4:
                return conPacketPlayOutOpenWindow.newInstance(id, "minecraft:anvil", chatMessage, 0);
            case 5:
                conPacketPlayOutOpenWindow.newInstance(id, 8, "Repairing", 9, true);
            default:
                throw new Exception("Constructor not found");
        }
    }

    public void open(Player player, Consumer<Inventory> after){
        try{
            Object nmsPlayer = NMSUtils.getHandle(player);
            Object anvilcon;
            if(classBlockPosition != null){
                if(conContainerAnvil.getParameterTypes().length == 3){
                    Object at = atContainerAccess.invoke(null, fieldWorld.get(nmsPlayer), blockPosition);
                    anvilcon = conContainerAnvil.newInstance(0, fieldInventory.get(nmsPlayer), at);
                    if(fieldTitle != null){
                        fieldTitle.set(anvilcon, chatMessage);
                    }
                }else{
                    anvilcon = conContainerAnvil.newInstance(fieldInventory.get(nmsPlayer), fieldWorld.get(nmsPlayer), blockPosition, nmsPlayer);
                }
            }else{
                anvilcon = conContainerAnvil.newInstance(fieldInventory.get(nmsPlayer), fieldWorld.get(nmsPlayer), 0, 0, 0, nmsPlayer);
            }
            fieldCheckReachable.set(anvilcon, false);

            int c = (Integer)nextContainerCounter.invoke(nmsPlayer);
            PacketUtils.sendPacket(player, getPacketPlayOutOpenWindow(c));
            fieldActiveContainer.set(nmsPlayer, anvilcon);
            fieldWindowID.set(anvilcon, c);
            addSlotListener.invoke(anvilcon, nmsPlayer);
            after.accept(((InventoryView) getBukkitView.invoke(anvilcon)).getTopInventory());
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
