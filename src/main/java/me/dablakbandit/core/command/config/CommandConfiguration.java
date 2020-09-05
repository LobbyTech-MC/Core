package me.dablakbandit.core.command.config;

import me.dablakbandit.core.command.config.annotation.CommandBase;
import me.dablakbandit.core.config.comment.CommentConfiguration;
import me.dablakbandit.core.utils.NMSUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandConfiguration extends CommentConfiguration{
	
	public CommandConfiguration(JavaPlugin plugin, String filename){
		super(plugin, filename);
	}
	
	public void load(){
		this.reloadConfig();
		try{
			boolean save = NMSUtils.getFields(this.getClass()).stream().filter(field -> Command.class.isAssignableFrom(field.getType())).filter(field -> {
				String name = WordUtils.capitalize(field.getName().toLowerCase().replaceAll("_", " "));
				try{
					return ((Command)field.get(null)).get(this, name, name.replaceAll(" ", ".Argument."));
				}catch(IllegalAccessException e){
					e.printStackTrace();
				}
				return false;
			}).count() > 0;
			if(save){
				this.saveConfig();
			}
		}catch(Exception var8){
			var8.printStackTrace();
		}
		
	}
	
	public boolean parseAnnotation(Field field){
		String name = WordUtils.capitalize(field.getName().toLowerCase().replaceAll("_", " ")).replaceAll(" ", ".Argument.");
		return parseAnnotations(name, field.getAnnotations());
	}
	
	@Override
	protected boolean parseAnnotations(String path, Annotation[] annotations){
		boolean save = super.parseAnnotations(path, annotations);
		return Arrays.stream(annotations).filter(annotation -> {
			if(annotation instanceof CommandBase){ return setComment(path, ((CommandBase)annotation).value()); }
			return false;
		}).count() > 0 || save;
	}
	
	public void reload(){
		this.load();
	}
	
	public static class Command extends me.dablakbandit.core.configuration.CommandConfiguration.Command{
		
		public Command(String command){
			this(command, (String)null);
		}
		
		public Command(String command, String permission){
			this(command, permission, new String[0]);
		}
		
		public Command(String command, String permission, String[] aliases){
			this(command, permission, aliases, new String[0]);
		}
		
		public Command(String command, String permission, String[] aliases, String[] info){
			super(command, permission, aliases, info);
		}
	}
}
