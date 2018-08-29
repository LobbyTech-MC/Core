package me.dablakbandit.core.minigame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.minigame.state.GameState;

public class MiniGameManager{
	
	private static MiniGameManager manager = new MiniGameManager();
	
	public static MiniGameManager getMainInstance(){
		return manager;
	}
	
	private static List<MiniGameManager> minigames = new ArrayList<MiniGameManager>();
	
	public static void disableAll(){
		for(MiniGameManager mgm : minigames){
			mgm.disable();
		}
	}
	
	private GameState state;
	
	public MiniGameManager(){
		minigames.add(this);
	}
	
	public GameState getState(){
		return state;
	}
	
	public void setState(GameState state){
		if(this.state != null){
			HandlerList.unregisterAll(this.state);
			this.state.disable();
		}
		this.state = state;
		if(state != null){
			state.enable();
			Bukkit.getPluginManager().registerEvents(state, CorePlugin.getInstance());
		}
	}
	
	public void disable(){
		if(this.state != null){
			state.disable();
		}
	}
	
	private RegisteredListener cl;
	
	public void cancelAllBut(String... strings){
		if(cl != null){
			removeListener();
		}
		cl = new DontCustomListener(Arrays.asList(strings));
		registerListener();
	}
	
	public void cancelAll(String... strings){
		if(cl != null){
			removeListener();
		}
		cl = new DoCustomListener(Arrays.asList(strings));
		registerListener();
	}
	
	public void removeListener(){
		if(cl != null){
			for(HandlerList hl : HandlerList.getHandlerLists()){
				hl.unregister(cl);
			}
		}
	}
	
	private void registerListener(){
		for(HandlerList hl : HandlerList.getHandlerLists()){
			hl.register(cl);
		}
	}
}
