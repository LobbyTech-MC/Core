package me.dablakbandit.core.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import me.dablakbandit.core.utils.NMSUtils;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter{
	
	protected final String			command;
	protected final String			description;
	protected final List<String>	alias;
	protected final String			usage;
	protected final String			permMessage;
	protected final Plugin			plugin;
	
	protected static CommandMap		cmap;
	
	public AbstractCommand(String command){
		this(command, null, null, null, null, null);
	}
	
	public AbstractCommand(String command, String usage){
		this(command, usage, null, null, null, null);
	}
	
	public AbstractCommand(String command, String usage, String description){
		this(command, usage, description, null, null, null);
	}
	
	public AbstractCommand(String command, String usage, String description, String permissionMessage){
		this(command, usage, description, permissionMessage, null, null);
	}
	
	public AbstractCommand(String command, String usage, String description, List<String> aliases){
		this(command, usage, description, null, aliases, null);
	}
	
	public AbstractCommand(String command, String usage, String description, String permissionMessage, List<String> aliases){
		this(command, usage, description, permissionMessage, aliases, null);
	}
	
	public AbstractCommand(String command, String usage, String description, String permissionMessage, List<String> aliases, Plugin plugin){
		this.command = command.toLowerCase();
		this.usage = usage;
		this.description = description;
		this.permMessage = permissionMessage;
		this.alias = aliases;
		this.plugin = plugin;
	}
	
	private static CommandMap				commandMap		= getCommandMap();
	private static Field					knownCommands	= getKnownCommands();
	
	private static boolean					loaded			= false;
	
	private static List<AbstractCommand>	list			= new ArrayList<AbstractCommand>();
	
	public static void enable(){
		loaded = true;
		for(AbstractCommand ac : list){
			ac.register();
		}
		list.clear();
	}
	
	public void register(){
		if(!loaded){
			list.add(this);
		}else{
			reg();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void reg(){
		ReflectCommand cmd = plugin == null ? new ReflectCommand(this.command) : new PluginReflectCommand(this.command, this.plugin);
		if(this.alias != null)
			cmd.setAliases(this.alias);
		if(this.description != null)
			cmd.setDescription(this.description);
		if(this.usage != null)
			cmd.setUsage(this.usage);
		if(this.permMessage != null)
			cmd.setPermissionMessage(this.permMessage);
		try{
			Map<String, Command> commands = (Map<String, Command>)knownCommands.get(commandMap);
			commands.remove(this.command);
			knownCommands.set(commandMap, commands);
		}catch(Exception e){
			e.printStackTrace();
			System.out.print(commandMap.getClass().getName());
		}
		commandMap.register(this.command, "", cmd);
		cmd.setExecutor(this);
	}
	
	static Field getKnownCommands(){
		try{
			Class<?> clazz = commandMap.getClass();
			if(clazz.getSimpleName().equals("FakeSimpleCommandMap"))
				clazz = clazz.getSuperclass();
			return NMSUtils.getField(clazz, "knownCommands");
		}catch(Exception e){
			e.printStackTrace();
			System.out.print(commandMap.getClass().getName());
		}
		return null;
	}
	
	static CommandMap getCommandMap(){
		if(cmap == null){
			try{
				final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
				f.setAccessible(true);
				cmap = (CommandMap)f.get(Bukkit.getServer());
			}catch(Exception e){
				e.printStackTrace();
			}
			return cmap;
		}else{
			return cmap;
		}
	}
	
	private class ReflectCommand extends Command{
		private AbstractCommand exe = null;
		
		protected ReflectCommand(String command){
			super(command);
		}
		
		public void setExecutor(AbstractCommand exe){
			this.exe = exe;
		}
		
		public boolean execute(CommandSender sender, String commandLabel, String[] args){
			if(exe != null){
				exe.onCommand(sender, this, commandLabel, args);
			}
			return false;
		}
		
		@Override
		public List<String> tabComplete(CommandSender sender, String alias, String[] args){
			List<String> list = exe.onTabComplete(sender, this, alias, args);
			if(list != null)
				return list;
			return super.tabComplete(sender, alias, args);
		}
		
	}
	
	private class PluginReflectCommand extends ReflectCommand implements PluginIdentifiableCommand{
		
		protected Plugin plugin;
		
		protected PluginReflectCommand(String command, Plugin plugin){
			super(command);
			this.plugin = plugin;
		}
		
		@Override
		public Plugin getPlugin(){
			return plugin;
		}
	}
	
	public boolean isPlayer(CommandSender sender){
		return (sender instanceof Player);
	}
	
	public boolean isAuthorized(CommandSender sender, String permission){
		return sender.hasPermission(permission);
	}
	
	public boolean isAuthorized(Player player, String permission){
		return player.hasPermission(permission);
	}
	
	public boolean isAuthorized(CommandSender sender, Permission perm){
		return sender.hasPermission(perm);
	}
	
	public boolean isAuthorized(Player player, Permission perm){
		return player.hasPermission(perm);
	}
	
	public abstract boolean onCommand(CommandSender s, Command cmd, String label, String[] args);
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
		return null;
	}
}
