package me.dablakbandit.core.utils;

import me.dablakbandit.core.utils.itemutils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.CorePlugin;

public class ItemUtils{
	
	private static IItemUtils inst = load();
	
	private static IItemUtils load(){
		ItemStack is = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta im = is.getItemMeta();
		im.addEnchant(Enchantment.KNOCKBACK, 20, true);
		is.setItemMeta(im);
		try{
			CoreLog.info("[Core] Attempting to load default ItemUtils");
			IItemUtils inst = new DefaultItemUtils();
			inst.convertItemStackToJSON(is).equals("");
			CoreLog.info("[Core] Loaded default, enjoy :)");
			return inst;
		}catch(Exception e){
		}
		try{
			CoreLog.info("[Core] Attempting to load 1.16 ItemUtils");
			IItemUtils inst = new _116ItemUtils();
			inst.convertItemStackToJSON(is).equals("");
			CoreLog.info("[Core] Loaded 1.16, enjoy :)");
			return inst;
		}catch(Exception e){
		}
		try{
			CoreLog.info("[Core] Attempting to load cauldron 1.7.10 ItemUtils");
			_1710ItemUtils inst = new _1710ItemUtils();
			inst.convertItemStackToJSON(is).equals("");
			CoreLog.info("[Core] Loaded cauldron 1.7.10, enjoy :)");
			return inst;
		}catch(Exception e){
		}
		try{
			CoreLog.info("[Core] Attempting to load cauldron 1.6.4 ItemUtils");
			IItemUtils inst = new _164ItemUtils();
			inst.convertItemStackToJSON(is).equals("");
			CoreLog.info("[Core] Loaded cauldron 1.6.4, enjoy :)");
			return inst;
		}catch(Exception e){
		}
		CoreLog.info("[Core] Failed to load ItemUtils, disabling to ensure stability.");
		Bukkit.getPluginManager().disablePlugin(CorePlugin.getInstance());
		return null;
	}
	
	public static IItemUtils getInstance(){
		return inst;
	}
}
