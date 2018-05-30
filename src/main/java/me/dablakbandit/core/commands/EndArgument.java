package me.dablakbandit.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.dablakbandit.core.configuration.CommandConfiguration;

public abstract class EndArgument extends AdvancedArgument{
	
	public EndArgument(CommandConfiguration.Command command){
		super(command);
		for(String s : command.getInfo()){
			arguments.put(s, null);
		}
	}
	
	public EndArgument(String argument, String... args){
		super(argument);
		for(String s : args){
			arguments.put(s, null);
		}
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(!hasPermission(s)){
			base.sendPermission(s, cmd, label, args);
			return false;
		}
		onArgument(s, cmd, label, args, original);
		return true;
	}
	
	@Override
	public void init(){
		
	}
}
