package me.dablakbandit.core.command.config;

import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.command.DefaultArgument;
import me.dablakbandit.core.commands.AdvancedArgument;
import me.dablakbandit.core.commands.AdvancedCommand;
import me.dablakbandit.core.utils.NMSUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

public class ConfigurationCommand extends AdvancedCommand{
	
	protected CommandConfiguration config;
	
	protected ConfigurationCommand(Plugin plugin, CommandConfiguration config, CommandConfiguration.Command base){
		super(plugin, base);
		this.config = config;
	}
	
	@Override
	public void onBaseCommand(CommandSender s, Command cmd, String label, String[] args){
		sendArguments(s, cmd);
	}
	
	@Override
	public void init(){
		
	}

	private static final Comparator<Field> fieldComparator = new Comparator<Field>() {
		@Override
		public int compare(Field o1, Field o2) {
			return o1.getName().split("_").length - o2.getName().split("_").length;
		}
	};
	
	protected void loadArguments(){
		try{
			boolean save = NMSUtils.getFields(config.getClass()).stream().filter(this::filterArgument).sorted(fieldComparator).filter(this::loadArgument).count() > 0;
			save |= NMSUtils.getFields(config.getClass()).stream().filter(field -> !field.getName().contains("_")).filter(this::parseAnnotation).count() > 0;
			if(save){
				config.saveConfig();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void saveConfig(Field field){
	}
	
	private void loadArgument(me.dablakbandit.core.configuration.CommandConfiguration.Command command, String path) throws ClassNotFoundException{
		ClassLoader cl = this.getClass().getClassLoader();
		AdvancedArgument argument = null;
		try{
			Class<?> clazz = cl.loadClass(path);
			argument = (AdvancedArgument)clazz.getDeclaredConstructor(CommandConfiguration.Command.class).newInstance(command);
		}catch(NoSuchMethodException | ClassNotFoundException notFound){
			argument = new DefaultArgument(command);
		}catch(IllegalAccessException | InstantiationException | InvocationTargetException exc){
			exc.printStackTrace();
			return;
		}
		String[] follow = path.split("\\.");
		try{
			int max = this.getClass().getPackage().getName().split("\\.").length + 2;
			if(follow.length == max){
				addArgument(argument);
			}else{
				follow = Arrays.copyOfRange(follow, max - 1, follow.length);
				AdvancedArgument base = defaultArguments.get(follow[0]);
				for(int i = 1; i < follow.length - 1; i++){
					base = base.getDefaultArgument(follow[i]);
				}
				base.addArgument(argument);
			}
		}catch(Exception e){
			CoreLog.error("Issue with " + path + ", " + follow[0]);
			e.printStackTrace();
		}
	}
	
	private boolean parseAnnotation(Field field){
		return config.parseAnnotation(field);
	}
	
	private boolean loadArgument(Field field){
		String path = getPath(field);
		try{
			loadArgument((CommandConfiguration.Command)field.get(null), path);
			return parseAnnotation(field);
		}catch(Exception e){
			plugin.getLogger().warning("Missing argument " + path);
		}
		return false;
	}
	
	private boolean filterArgument(Field field){
		if(!me.dablakbandit.core.configuration.CommandConfiguration.Command.class.isAssignableFrom(field.getType())){ return false; }
		return field.getName().contains("_");
	}
	
	private String getPath(Field field){
		String path = this.getClass().getPackage().getName();
		path += ".arguments.";
		String[] check = field.getName().toLowerCase().split("_");
		path += String.join(".", Arrays.copyOfRange(check, 1, check.length - 1));
		if(check.length > 2){
			path += ".";
		}
		path += WordUtils.capitalize(check[check.length - 1]) + "Argument";
		return path;
	}
}
