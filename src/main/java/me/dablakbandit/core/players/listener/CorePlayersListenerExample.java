package me.dablakbandit.core.players.listener;

import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class CorePlayersListenerExample extends CorePlayersListener{
	
	private static CorePlayersListenerExample instance = new CorePlayersListenerExample();
	
	public static CorePlayersListenerExample getInstance(){
		return instance;
	}
	
	static{
		// Best to only one instanceof this class and add it in the onLoad of a plugin like so
		CorePlayerManager.getInstance().addListener(CorePlayersListenerExample.getInstance());
	}
	
	@Override
	public void addCorePlayers(CorePlayers pl){
		// Do something
	}
	
	@Override
	public void loadCorePlayers(CorePlayers pl){
		// Do something
	}
	
	@Override
	public void saveCorePlayers(CorePlayers pl){
		// Do something
	}
	
	@Override
	public void removeCorePlayers(CorePlayers pl){
		// Do something
	}
	
}
