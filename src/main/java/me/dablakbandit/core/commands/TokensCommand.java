package me.dablakbandit.core.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.core.CoreLanguageConfiguration;
import me.dablakbandit.core.CorePluginConfiguration;
import me.dablakbandit.core.database.listener.SQLTokens;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.TokensInfo;

public class TokensCommand extends AbstractCommand{
	
	private static TokensCommand command = new TokensCommand(CorePluginConfiguration.TOKENS_COMMAND.get());
	
	public static TokensCommand getInstance(){
		return command;
	}
	
	public TokensCommand(String command){
		super(command, null, null, null, CorePluginConfiguration.TOKENS_ALIASES.get());
		register();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String Label, String[] args){
		if(args.length == 0){
			if(!(s instanceof Player)){ return false; }
			Player player = (Player)s;
			CorePlayers cp = CorePlayerManager.getInstance().getPlayer(player);
			player.sendMessage(CoreLanguageConfiguration.MESSAGE_TOKENS_BALANCE.getMessage().replaceAll("<i>", "" + cp.getInfo(TokensInfo.class).getTokens()));
		}else{
			if(!s.isOp()){ return false; }
			switch(args[0].toLowerCase()){
			case "give":{
				if(args.length < 3){
					s.sendMessage("/" + Label + " give <uuid> <amount>");
					break;
				}
				UUID uuid = UUID.fromString(args[1]);
				if(uuid == null){
					s.sendMessage("Invalid UUID");
					break;
				}
				int give = 0;
				try{
					give = Integer.parseInt(args[2]);
				}catch(Exception e){
					s.sendMessage("Invalid amount");
					break;
				}
				Player player = Bukkit.getPlayer(uuid);
				if(player != null){
					TokensInfo ti = CorePlayerManager.getInstance().getPlayer(player).getInfo(TokensInfo.class);
					ti.addTokens(give);
				}else{
					SQLTokens.getInstance().addTokens(uuid.toString(), give);
				}
				break;
			}
			case "info":{
				if(args.length < 2){
					s.sendMessage("/" + Label + "info <uuid>");
					break;
				}
				UUID uuid = UUID.fromString(args[1]);
				if(uuid == null){
					s.sendMessage("Invalid UUID");
					break;
				}
				Player player = Bukkit.getPlayer(uuid);
				if(player != null){
					TokensInfo ti = CorePlayerManager.getInstance().getPlayer(player).getInfo(TokensInfo.class);
					player.sendMessage(CoreLanguageConfiguration.MESSAGE_TOKENS_BALANCE.getMessage().replaceAll("<i>", "" + ti.getTokens()));
				}
				break;
			}
			}
		}
		return true;
	}
	
}
