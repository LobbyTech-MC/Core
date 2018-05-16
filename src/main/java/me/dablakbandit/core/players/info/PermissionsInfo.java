package me.dablakbandit.core.players.info;

import java.util.ArrayList;
import java.util.List;

import me.dablakbandit.core.database.listener.SQLPermissions;
import me.dablakbandit.core.players.CorePlayers;

public class PermissionsInfo extends CorePlayersInfo{
	
	public PermissionsInfo(CorePlayers pl){
		super(pl);
	}
	
	private List<String> permissions = new ArrayList<String>();
	
	@Override
	public void load(){
		permissions = SQLPermissions.getInstance().getPermissions(pl.getUUIDString());
	}
	
	public void addPermission(String permission){
		permissions.add(permission);
		SQLPermissions.getInstance().addPermission(pl.getUUIDString(), permission);
	}
	
	public void removePermission(String permission){
		permissions.remove(permission);
		SQLPermissions.getInstance().removePermission(pl.getUUIDString(), permission);
	}
	
	public boolean hasPermission(String s){
		if(pl.getPlayer().isOp()){ return true; }
		return permissions.contains(s);
	}
	
	@Override
	public void save(){
		
	}
}
