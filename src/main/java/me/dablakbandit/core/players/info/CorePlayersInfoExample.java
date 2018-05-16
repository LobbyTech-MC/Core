package me.dablakbandit.core.players.info;

import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class CorePlayersInfoExample extends CorePlayersInfo{
	
	static{
		// This is for example purposes only
		// Adds coin storage for CorePlayers
		// Reccomended adding this info from the addCorePlayers method from a CorePlayersListener
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer("<uuid>");
		pl.addInfo(new CorePlayersInfoExample(pl));
		
		// To get the info just do the following
		CorePlayersInfoExample example = pl.getInfo(CorePlayersInfoExample.class);
		
		// Do stuff with example
		example.setCoins(10);
		example.addCoins(5);
		if(example.takeCoins(20)){
			// Wont work as they have 15 coins
		}else{
			example.setCoins(0);
		}
	}
	
	protected int coins = 0;
	
	public CorePlayersInfoExample(CorePlayers pl){
		super(pl);
	}
	
	public int getCoins(){
		return coins;
	}
	
	public void setCoins(int i){
		coins = i;
	}
	
	public void addCoins(int i){
		coins += i;
	}
	
	public boolean takeCoins(int i){
		if(coins < i){ return false; }
		coins -= 1;
		return true;
	}
	
	@Override
	public void load(){
		// Load coins from database
	}
	
	@Override
	public void save(){
		// Save coins to database
	}
	
}
