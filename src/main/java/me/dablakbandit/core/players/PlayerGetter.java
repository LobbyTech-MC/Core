package me.dablakbandit.core.players;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.base.Charsets;

import me.dablakbandit.core.utils.NMSUtils;

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
		try{
			if(huuid){ return Bukkit.getPlayer(UUID.fromString(uuid)); }
		}catch(Exception e){
		}
		return Bukkit.getPlayer(name);
	}
	
	public static Player getPlayer(UUID uuid, String name){
		try{
			if(huuid){ return Bukkit.getPlayer(uuid); }
		}catch(Exception e){
		}
		return Bukkit.getPlayer(name);
	}
	
	public static String getUUID(Player player){
		try{
			if(huuid){ return player.getUniqueId().toString(); }
		}catch(Exception e){
		}
		String name = player.getName();
		return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)).toString();
	}
	
}
