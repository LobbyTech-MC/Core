package me.dablakbandit.core.config.inventory;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.bukkit.plugin.Plugin;

import me.dablakbandit.core.config.comment.CommentConfiguration;
import me.dablakbandit.core.config.inventory.annotation.InventoryBase;

public class InventoryConfiguration extends CommentConfiguration{
	
	public InventoryConfiguration(Plugin plugin, String filename){
		super(plugin, filename);
	}
	
	protected boolean parseAnnotations(String path, Annotation[] annotations){
		boolean save = super.parseAnnotations(path, annotations);
		return Arrays.stream(annotations).filter(annotation -> {
			if(annotation instanceof InventoryBase){ return setComment(path, ((InventoryBase)annotation).value()); }
			return false;
		}).count() > 0 || save;
	}
}
