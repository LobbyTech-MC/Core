package me.dablakbandit.core.config.path;

import org.bukkit.entity.Player;

import me.dablakbandit.core.config.RawConfiguration;

public class PermissionPath extends Path<PermissionPath.Permission>{
	public PermissionPath(String def){
		super(new Permission(def));
	}
	
	public boolean has(Player player){
		Permission permission = get();
		if(!permission.check){ return true; }
		return player.hasPermission(permission.permission);
	}
	
	@Override
	protected Permission get(RawConfiguration config, String path){
		Permission permission = def.clone();
		if(isSet(path, "Permission")){
			permission.permission = config.getString(path + ".Permission");
		}
		if(isSet(path, "Check")){
			permission.check = config.getBoolean(path + ".Check");
		}
		return permission;
	}
	
	@Override
	protected Object setAs(RawConfiguration config, Permission permission){
		String path = getActualPath();
		config.set(path + ".Permission", permission.permission);
		config.set(path + ".Check", permission.check);
		return null;
	}
	
	public static class Permission{
		String	permission;
		boolean	check;
		
		public Permission(String permission){
			this(permission, true);
		}
		
		public Permission(String permission, boolean def){
			this.permission = permission;
			this.check = def;
		}
		
		@Override
		protected Permission clone(){
			return new Permission(permission, check);
		}
	}
}
