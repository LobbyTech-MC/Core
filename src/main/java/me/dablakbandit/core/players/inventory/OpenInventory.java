package me.dablakbandit.core.players.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dablakbandit.core.players.CorePlayers;

public abstract class OpenInventory{
	
	public abstract boolean open(CorePlayers pl, Player player);
	
	public abstract void set(CorePlayers pl, Player player, Inventory inv);
	
	public abstract void parseClick(CorePlayers pl, Player player, Inventory inv, Inventory top, InventoryClickEvent event);
	
	public abstract void parseInventoryDrag(CorePlayers pl, Player player, Inventory inv, Inventory top, InventoryDragEvent event);
	
	public void onClose(CorePlayers pl, Player player){
	}
	
	protected static ItemStack change(ItemStack is, String s){
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(s);
		is.setItemMeta(im);
		return is;
	}
	
	protected static ItemStack hide(ItemStack is){
		ItemMeta im = is.getItemMeta();
		im.setUnbreakable(true);
		im.addItemFlags(ItemFlag.values());
		is.setItemMeta(im);
		return is;
	}
	
	protected static ItemStack glow(ItemStack is){
		is.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
		return hide(is);
	}
	
	protected static ItemStack clone(ItemStack is, String s){
		return change(is.clone(), s);
	}
	
	protected static ItemStack change(ItemStack is, String s, String... strings){
		ItemMeta im = is.getItemMeta();
		if(s != null){
			im.setDisplayName(s);
		}
		if(strings.length > 0){
			im.setLore(Arrays.asList(strings));
		}
		is.setItemMeta(im);
		return is;
	}
	
	protected static ItemStack add(ItemStack is, String... strings){
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null){
			lore = Arrays.asList(strings);
		}else{
			lore.addAll(Arrays.asList(strings));
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	protected static ItemStack clone(ItemStack is, String s, String... strings){
		return change(is.clone(), s, strings);
	}
	
	protected static ItemStack create(Material mat, int damage, String name){
		return change(new ItemStack(mat, 1, (short)damage), name);
	}
	
	protected static ItemStack create(Material mat, int damage, String name, String... lore){
		return change(new ItemStack(mat, 1, (short)damage), name, lore);
	}
	
	public boolean actionInteruptable(){
		return true;
	}
	
}
