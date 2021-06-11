package me.dablakbandit.core.utils.anvil.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

public interface IAnvilUtil {

    void open(Player player, Consumer<Inventory> after);
}
