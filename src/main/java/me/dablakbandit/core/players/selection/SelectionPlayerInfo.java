package me.dablakbandit.core.players.selection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;

public class SelectionPlayerInfo extends CorePlayersInfo{
	
	private Location				point1, point2;
	private List<SelectionListener>	listeners	= new ArrayList<SelectionListener>();
	
	public SelectionPlayerInfo(CorePlayers pl){
		super(pl);
	}
	
	public Location getPoint1(){
		return point1;
	}
	
	public Location getPoint2(){
		return point2;
	}
	
	public void setPoint1(Location l){
		point1 = l;
		for(SelectionListener sl : listeners){
			sl.onSelectPoint1(pl);
		}
	}
	
	public void setPoint2(Location l){
		point2 = l;
		for(SelectionListener sl : listeners){
			sl.onSelectPoint2(pl);
		}
	}
	
	public List<SelectionListener> getListeners(){
		return listeners;
	}
	
	public SelectionListener addListener(SelectionListener listener){
		listeners.add(listener);
		return listener;
	}
	
	public void removeListener(SelectionListener listener){
		listeners.remove(listener);
	}
	
	@Override
	public void load(){
		
	}
	
	@Override
	public void save(){
		
	}
	
}
