package me.dablakbandit.core.config.path;

import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.core.config.RawConfiguration;

public class ItemPath extends Path<ItemStack> implements Supplier<ItemStack>{
	
	public ItemPath(Material material){
		this(material, 1);
	}
	
	public ItemPath(Material material, int amount){
		this(material, 1, 0);
	}
	
	public ItemPath(Material material, int amount, int durability){
		this(new ItemStack(material, 1, (short)durability));
	}
	
	public ItemPath(ItemStack def){
		super(def);
	}
	
	@Deprecated
	public ItemPath(String old, ItemStack def){
		super(old, def);
	}
	
	protected ItemStack get(RawConfiguration config, String path){
		ItemStack is = def.clone();
		if(isSet(path, "Material")){
			is.setType(Material.valueOf(config.getString(path + ".Material")));
		}
		if(isSet(path, "Durability")){
			is.setDurability((short)config.getInt(path + ".Durability"));
		}
		if(isSet(path, "Amount")){
			is.setAmount(config.getInt(path + ".Amount"));
		}
		return is;
	}
	
	@Override
	protected Object setAs(RawConfiguration config, ItemStack itemStack){
		String path = getActualPath();
		config.set(path + ".Material", itemStack.getType().name());
		config.set(path + ".Durability", itemStack.getDurability());
		config.set(path + ".Amount", itemStack.getAmount());
		return null;
	}
	
}
