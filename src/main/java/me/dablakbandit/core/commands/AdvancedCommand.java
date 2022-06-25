package me.dablakbandit.core.commands;

import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.configuration.CommandConfiguration;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.*;

public abstract class AdvancedCommand extends AbstractCommand{
	
	protected int		cooldown;

	public AdvancedCommand(Plugin plugin, CommandConfiguration.Command command){
		this(plugin, command.getCommand(), command.getPermission(), Arrays.asList(command.getAliases()));
		this.cooldown = command.getCooldown();
	}
	
	public AdvancedCommand(Plugin plugin, String command, String permission, List<String> aliases){
		super(command, null, null, null, aliases, plugin);
		this.permission = permission;
		reg();
		init();
	}
	
	protected Map<String, AdvancedArgument> defaultArguments = new TreeMap<String, AdvancedArgument>();
	protected Map<String, AdvancedArgument> arguments = new TreeMap<String, AdvancedArgument>();
	protected Map<Integer, TabCompleter>	tabs;

	public Map<String, AdvancedArgument> getArguments(){
		return arguments;
	}

	public Map<String, AdvancedArgument> getDefaultArguments() {
		return defaultArguments;
	}

	public boolean hasPermission(CommandSender s){
		return permission == null ? true : s.hasPermission(permission);
	}
	
	public void sendArguments(CommandSender s, Command cmd){
		sendArguments(s, cmd.getLabel(), arguments.entrySet());
	}
	
	public void sendArguments(CommandSender s, Command cmd, Set<Map.Entry<String, AdvancedArgument>> args){
		sendArguments(s, cmd.getLabel(), args);
	}
	
	public void sendArguments(CommandSender s, Command cmd, String[] add, Set<Map.Entry<String, AdvancedArgument>> args){
		sendArguments(s, cmd.getLabel(), add, args);
	}
	
	public void sendArguments(CommandSender s, Command cmd, String[] arg, String[] original, Set<Map.Entry<String, AdvancedArgument>> args){
		sendArguments(s, cmd.getLabel(), Arrays.copyOfRange(original, 0, original.length - arg.length), args);
	}
	
	public void sendArguments(CommandSender s, String base, Set<Map.Entry<String, AdvancedArgument>> args){
		sendArguments(s, base, new String[0], args);
	}
	
	public void sendArguments(CommandSender s, String base, String[] add, Set<Map.Entry<String, AdvancedArgument>> args){
		String added = "";
		for(String a : add){
			added = added + " " + a;
		}
		sendTitle(s, base, added);
		for(Map.Entry<String, AdvancedArgument> e : args){
			AdvancedArgument aa = e.getValue();
			if(aa != null && !aa.hasPermission(s)){
				continue;
			}
			String after = e.getKey();
			if(aa != null && aa instanceof EndArgument){
				if(aa.getArguments().size() > 0){
					for(String a : aa.getArguments().keySet()){
						sendArgument(s, base, added, e.getKey() + " " + a);
					}
					continue;
				}
			}
			sendArgument(s, base, added, e.getKey());
		}
	}
	
	protected void sendTitle(CommandSender s, String base, String args){
		s.sendMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + WordUtils.capitalize(base) + WordUtils.capitalize(args) + ChatColor.GRAY + "]");
	}
	
	protected void sendArgument(CommandSender s, String base, String args, String aditional){
		s.sendMessage("/" + base + args + " " + aditional);
	}
	
	public void sendFormattedMessage(CommandSender s, String message){
		s.sendMessage(ChatColor.GRAY + message);
	}
	
	private boolean hasNonNullArguments(){
		for(Map.Entry<String, AdvancedArgument> e : arguments.entrySet()){
			if(e.getValue() != null){ return true; }
		}
		return false;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(!hasPermission(s)){
			sendPermission(s, cmd, label, args);
			return false;
		}
		if(args.length == 0 || !hasNonNullArguments()){
			onBaseCommand(s, cmd, label, args);
			return true;
		}
		AdvancedArgument aa = arguments.get(args[0].toLowerCase());
		if(aa == null){
			sendUnknownCommand(s, cmd, label, args);
			return false;
		}
		aa.onCommand(s, cmd, label, Arrays.copyOfRange(args, 1, args.length), args);
		return true;
	}
	
	public void sendPermission(CommandSender s, Command cmd, String label, String[] args){
		s.sendMessage(ChatColor.RED + "You do not have the permission to perform this command.");
	}
	
	public void sendUnknownCommand(CommandSender s, Command cmd, String label, String[] args){
		String command = cmd.getLabel();
		for(int i = 0; i < args.length; i++){
			command += " " + args[i];
		}
		s.sendMessage(ChatColor.RED + "Unknown command /" + command);
	}
	
	public abstract void onBaseCommand(CommandSender s, Command cmd, String label, String[] args);
	
	public abstract void init();
	
	public void addArgument(AdvancedArgument aa){
		arguments.put(aa.getArgument(), aa);
		defaultArguments.put(aa.getDefaultArgument(), aa);
		aa.setBase(this);
		aa.init();
	}
	
	public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args){
		if(tabs != null){
			int i = args.length - 1;
			TabCompleter completer = tabs.get(i);
			if(completer != null){ return completer.onTabComplete(s, args[i], args); }
		}
		List<String> list = new ArrayList<String>();
		if(!hasPermission(s)){ return list; }
		if(args.length == 0){
			for(Map.Entry<String, AdvancedArgument> e : arguments.entrySet()){
				if(e.getValue() == null || e.getValue().hasPermission(s)){
					list.add(e.getKey());
				}
			}
		}else{
			String a = args[0].toLowerCase();
			if(arguments.containsKey(a)){
				AdvancedArgument aa = arguments.get(a);
				if(aa != null && aa.hasPermission(s)){ return aa.onTabComplete(s, cmd, label, Arrays.copyOfRange(args, 1, args.length), args); }
			}
			if(args.length == 1){
				for(Map.Entry<String, AdvancedArgument> e : arguments.entrySet()){
					if(e.getKey().toLowerCase().startsWith(args[0].toLowerCase()) && (e.getValue() == null || e.getValue().hasPermission(s))){
						list.add(e.getKey());
					}
				}
			}
		}
		return list;
	}

	protected void addTabCompleter(int arg, TabCompleter completer){
		initTabs();
		tabs.put(arg, completer);
	}

	protected void initTabs(){
		if(tabs == null){
			tabs = new HashMap<>();
		}
	}
	
}
