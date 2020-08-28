package me.dablakbandit.core.config.path;

import org.bukkit.ChatColor;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.inventory.InventoryDescriptor;

public class InventoryDescriptionPath extends Path<InventoryDescriptor>{
	
	public InventoryDescriptionPath(int size, String title){
		super(new InventoryDescriptor(size, title));
	}
	
	public InventoryDescriptionPath(int size, String title, String permission){
		super(new InventoryDescriptor(size, title, permission));
	}
	
	protected InventoryDescriptor get(RawConfiguration config, String path){
		InventoryDescriptor id = def;
		if(isSet(path, "Size")){
			id.setSize(config.getInt(path + ".Size"));
		}
		if(isSet(path, "Title")){
			id.setTitle(ChatColor.translateAlternateColorCodes('&', config.getString(path + ".Title")));
		}
		if(isSet(path, "Permission")){
			id.setPermission(config.getString(path + ".Permission"));
		}
		return id;
	}
	
	@Override
	protected Object setAs(RawConfiguration config, InventoryDescriptor id){
		String path = getActualPath();
		config.set(path + ".Size", id.getSize());
		config.set(path + ".Title", id.getTitle().replaceAll("§", "&"));
		config.set(path + ".Permission", id.getPermission());
		return null;
	}
}
