package me.dablakbandit.core.minigame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

import me.dablakbandit.core.CorePlugin;

public class DontCustomListener extends RegisteredListener{
	
	private List<String> no = new ArrayList<String>();
	
	public DontCustomListener(List<String> no){
		super(null, null, EventPriority.LOWEST, CorePlugin.getInstance(), true);
		this.no = no;
	}
	
	public void callEvent(Event event){
		if(event instanceof Cancellable && !no.contains(event.getEventName())){
			((Cancellable)event).setCancelled(true);
		}
	}
	
	public Listener getListener(){
		return new Listener(){
		};
	}
	
}
