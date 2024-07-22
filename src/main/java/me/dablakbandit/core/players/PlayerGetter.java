package me.dablakbandit.core.players;

import com.google.common.base.Charsets;
import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerGetter{
	
	private static boolean huuid = hasUUID();
	
	public static boolean hasUUID(){
		try{
			Class<?> bukkit = Bukkit.class;
			NMSUtils.getMethodSilent(bukkit, "getPlayer", UUID.class);
			
			return true;
		}catch(Throwable e){
		}
		return false;
	}
	
	public static Player getPlayer(String uuid, String name){
		return getPlayer(UUID.fromString(uuid), name);
	}
	
	public static Player getPlayer(UUID uuid, String name){
		try{
			if(huuid){ return Bukkit.getPlayer(uuid); }
		}catch(Exception e){
		}
		return Bukkit.getPlayerExact(name);
	}
	
	public static String getUUID(Player player){
		try{
			if(huuid){ return player.getUniqueId().toString(); }
		}catch(Exception e){
		}
		return UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(Charsets.UTF_8)).toString();
	}

	public static UUID getUuid(Player player){
		try{
			if(huuid){ return player.getUniqueId(); }
		}catch(Exception e){
		}
		return UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(Charsets.UTF_8));
	}
	
}
