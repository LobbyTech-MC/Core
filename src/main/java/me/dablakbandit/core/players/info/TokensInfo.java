package me.dablakbandit.core.players.info;

import me.dablakbandit.core.database.listener.SQLTokens;
import me.dablakbandit.core.players.CorePlayers;

public class TokensInfo extends CorePlayersInfo{
	
	public TokensInfo(CorePlayers pl){
		super(pl);
	}
	
	private int tokens;
	
	@Override
	public void load(){
		tokens = SQLTokens.getInstance().getTokens(pl.getUUIDString());
	}
	
	public void addTokens(int amount){
		tokens += amount;
		SQLTokens.getInstance().addTokens(pl.getUUIDString(), amount);
	}
	
	public boolean takeTokens(int amount){
		if(hasTokens(amount)){
			removeTokens(amount);
			return true;
		}
		return false;
	}
	
	public void removeTokens(int amount){
		tokens -= amount;
		SQLTokens.getInstance().takeTokens(pl.getUUIDString(), amount);
	}
	
	public boolean hasTokens(int amount){
		return tokens >= amount;
	}
	
	@Override
	public void save(){
		
	}
	
	public int getTokens(){
		return tokens;
	}
}
