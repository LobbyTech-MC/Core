package me.dablakbandit.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.utils.itemutils.DefaultItemUtils;
import me.dablakbandit.core.utils.itemutils.IItemUtils;
import me.dablakbandit.core.utils.itemutils._164ItemUtils;
import me.dablakbandit.core.utils.itemutils._1710ItemUtils;

public class ItemUtils{
	
	private static IItemUtils inst = load();
	
	private static IItemUtils load(){
		ItemStack is = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta im = is.getItemMeta();
		im.addEnchant(Enchantment.KNOCKBACK, 20, true);
		is.setItemMeta(im);
		try{
			System.out.print("[Core] Attempting to load default ItemUtils");
			IItemUtils inst = new DefaultItemUtils();
			inst.convertItemStackToJSON(is).equals("");
			System.out.print("[Core] Loaded default, enjoy :)");
			return inst;
		}catch(Exception e){
		}
		try{
			System.out.print("[Core] Attempting to load cauldron 1.7.10 ItemUtils");
			_1710ItemUtils inst = new _1710ItemUtils();
			inst.convertItemStackToJSON(is).equals("");
			System.out.print("[Core] Loaded cauldron 1.7.10, enjoy :)");
			return inst;
		}catch(Exception e){
		}
		try{
			System.out.print("[Core] Attempting to load cauldron 1.6.4 ItemUtils");
			IItemUtils inst = new _164ItemUtils();
			inst.convertItemStackToJSON(is).equals("");
			System.out.print("[Core] Loaded cauldron 1.6.4, enjoy :)");
			return inst;
		}catch(Exception e){
		}
		System.out.print("[Core] Failed to load ItemUtils, disabling to ensure stability.");
		Bukkit.getPluginManager().disablePlugin(CorePlugin.getInstance());
		return null;
	}
	
	public static IItemUtils getInstance(){
		return inst;
	}
}
