package me.dablakbandit.core.players.inventory;

import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class OpenInventory{
	
	public OpenInventory(){
		load();
	}
	
	public void load(){
		
	}
	
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
		is.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 1);
		return hide(is);
	}
	
	protected static ItemStack clone(ItemStack is, String s){
		return change(is.clone(), s);
	}
	
	protected static ItemStack change(ItemStack is, String s, String... strings){
		return change(is, s, strings.length == 0 ? Collections.emptyList() : Arrays.asList(strings));
	}

	protected static ItemStack change(ItemStack is, String s, List<String> lore){
		ItemMeta im = is.getItemMeta();
		if(s != null){
			im.setDisplayName(s);
		}
		if(lore != null && lore.size() > 0){
			im.setLore(lore);
		}
		is.setItemMeta(im);
		return is;
	}

	protected static ItemStack add(ItemStack is, String... strings){
		return add(is, strings.length == 0 ? Collections.emptyList() : Arrays.asList(strings));
	}
	
	protected static ItemStack add(ItemStack is, List<String> strings){
		if(strings == null || strings.size() == 0){
			return is;
		}
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null){
			lore = strings;
		}else{
			lore.addAll(strings);
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	protected static ItemStack replaceCloneNameLore(ItemStack is, String name, List<String> lore, String... replacements){
		return replaceNameLore(is.clone(), name, lore, replacements);
	}

	protected static ItemStack replaceNameLore(ItemStack is, String name, List<String> lore, String... replacements){
		if(name != null) {
			for(int i = 0; i < replacements.length; i += 2){
				name = name.replaceAll(replacements[i], replacements[i + 1]);
			}
		}
		if(lore != null && lore.size() > 0) {
			List<String> replacedLore = new ArrayList<>();
			for(int index = 0; index < lore.size(); index++){
				String temp = lore.get(index);
				for(int i = 0; i < replacements.length; i += 2){
					temp = temp.replaceAll(replacements[i], replacements[i + 1]);
				}
				replacedLore.add(temp);
			}
			return change(is, name, replacedLore);
		}
		return change(is, name);
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
