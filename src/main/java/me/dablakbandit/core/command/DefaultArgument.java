package me.dablakbandit.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.dablakbandit.core.commands.AdvancedArgument;
import me.dablakbandit.core.configuration.CommandConfiguration;

public class DefaultArgument extends AdvancedArgument{
	public DefaultArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length == 0){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
		}else{
			base.sendUnknownCommand(s, cmd, label, original);
		}
	}
	
	@Override
	public void init(){
		
	}
}
