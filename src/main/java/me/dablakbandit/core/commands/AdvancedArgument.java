package me.dablakbandit.core.commands;

import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.configuration.CommandConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public abstract class AdvancedArgument{

	protected Map<String, AdvancedArgument>	defaultArguments	= new TreeMap<String, AdvancedArgument>();
	protected Map<String, AdvancedArgument>	arguments	= new TreeMap<String, AdvancedArgument>();
	protected Map<Integer, TabCompleter>	tabs;

	protected String						argument, defaultArgument;
	protected AdvancedArgument				upper;
	protected AdvancedCommand				base;
	protected int							cooldown;
	
	protected String						permission;
	
	public AdvancedArgument(CommandConfiguration.Command command){
		this(command.getCommand(), command.getPermission(), command.getAliases());
		this.cooldown = command.getCooldown();
		this.defaultArgument = command.getDefaultCommand();
	}
	
	public AdvancedArgument(String argument){
		this(argument, null, new String[0]);
	}
	
	public AdvancedArgument(String argument, String permission, String... aliases){
		this.argument = argument;
		this.defaultArgument = argument;
		this.permission = permission;
		for(String s : aliases){
			new AbstractCommand(s){
				@Override
				public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
					AdvancedArgument.this.onCommand(s, cmd, label, args, args);
					return true;
				}
			}.reg();
		}
	}
	
	public Map<String, AdvancedArgument> getArguments(){
		return arguments;
	}

	public AdvancedArgument getDefaultArgument(String argument){
		return defaultArguments.get(argument);
	}

	public String getArgument(){
		return argument;
	}

	public String getDefaultArgument() {
		return defaultArgument;
	}

	public boolean hasPermission(CommandSender s){
		return permission == null ? true : s.hasPermission(permission);
	}
	
	protected void setBase(AdvancedCommand aa){
		this.base = aa;
	}
	
	public AbstractCommand getBase(){
		return base;
	}
	
	protected void setUpper(AdvancedArgument aa){
		this.upper = aa;
	}
	
	public AdvancedArgument getUpper(){
		return upper;
	}
	
	public void addArgument(AdvancedArgument aa){
		defaultArguments.put(aa.getDefaultArgument(), aa);
		arguments.put(aa.getArgument(), aa);
		aa.setBase(this.base);
		aa.setUpper(this);
		aa.init();
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(!hasPermission(s)){
			base.sendPermission(s, cmd, label, args);
			return false;
		}
		if(args.length == 0){
			onArgument(s, cmd, label, args, original);
			return true;
		}
		AdvancedArgument aa = arguments.get(args[0].toLowerCase());
		if(aa == null){
			base.sendUnknownCommand(s, cmd, label, original);
			return false;
		}
		aa.onCommand(s, cmd, label, Arrays.copyOfRange(args, 1, args.length), original);
		return true;
	}
	
	protected abstract void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original);
	
	public abstract void init();
	
	public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(tabs != null){
			int i = args.length - 1;
			TabCompleter completer = tabs.get(i);
			if(completer != null){ return completer.onTabComplete(s, args[i], args); }
		}
		List<String> list = new ArrayList<String>();
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
				if(aa != null && aa.hasPermission(s)){ return aa.onTabComplete(s, cmd, label, Arrays.copyOfRange(args, 1, args.length), original); }
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
