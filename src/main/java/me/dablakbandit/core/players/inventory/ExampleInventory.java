package me.dablakbandit.core.players.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.core.players.CorePlayers;

public class ExampleInventory extends OpenInventory{
	
	private static ItemStack	hub			= change(new ItemStack(Material.PAPER), ChatColor.YELLOW + "Hub", ChatColor.GRAY + "Return to hub");
	private static ItemStack	servers		= change(new ItemStack(Material.RED_ROSE), ChatColor.YELLOW + "Servers", ChatColor.GRAY + "List of servers");
	private static ItemStack	minigames	= change(new ItemStack(Material.NAME_TAG), ChatColor.YELLOW + "Minigames", ChatColor.GRAY + "List of minigames");
	private static ItemStack	links		= change(new ItemStack(Material.BOOK_AND_QUILL), ChatColor.YELLOW + "Links", ChatColor.GRAY + "List of links");
	private static ItemStack	cosmetics	= change(new ItemStack(Material.ENDER_CHEST), ChatColor.YELLOW + "Cosmetics", ChatColor.GRAY + "Cosmetics menu");
	private static ItemStack	warps		= change(new ItemStack(Material.ACACIA_DOOR_ITEM), ChatColor.YELLOW + "Warps", ChatColor.GRAY + "Warps menu");
	private static ItemStack	wallet		= change(new ItemStack(Material.BOOK), ChatColor.YELLOW + "Wallet", ChatColor.GRAY + "Wallet");
	private static ItemStack	close		= change(new ItemStack(Material.BARRIER), ChatColor.RED + "Close Menu");
	
	@Override
	public boolean open(CorePlayers pl, Player player){
		Inventory inv = Bukkit.createInventory(null, 45, "Core");
		set(pl, player, inv);
		return player.openInventory(inv) != null;
	}
	
	@Override
	public void set(CorePlayers pl, Player player, Inventory inv){
		inv.setItem(10, hub);
		inv.setItem(12, servers);
		inv.setItem(14, minigames);
		inv.setItem(16, links);
		inv.setItem(29, warps);
		inv.setItem(31, cosmetics);
		inv.setItem(33, wallet);
		inv.setItem(44, close);
	}
	
	@Override
	public void parseClick(CorePlayers pl, Player player, Inventory inv, Inventory top, InventoryClickEvent event){
		// Top part of the inventory
		if(inv.equals(top)){
			switch(event.getRawSlot()){
			case 10:{
				// Hub
				return;
			}
			case 12:{
				// Servers
				return;
			}
			case 14:{
				// Minigames
				return;
			}
			case 16:{
				// Links
				return;
			}
			case 29:{
				// Warps
				return;
			}
			case 31:{
				// Cosmetics
				// pl.setOpenInventory(new CosmeticsInventory());
				return;
			}
			case 33:{
				// Wallet
				player.performCommand("/bal");
				return;
			}
			case 44:{
				// Close
				player.closeInventory();
				return;
			}
			}
			// Bottom side (Player inventory)
		}else{
			// Cancel clicking in their inventory
			event.setCancelled(true);
		}
	}
	
	@Override
	public void parseInventoryDrag(CorePlayers pl, Player player, Inventory inv, Inventory top, InventoryDragEvent event){
		// Cancels draging of stacked items
		event.setCancelled(true);
	}
	
}
