package me.dablakbandit.core.utils;

import org.bukkit.Material;

public class Version{
	
	public static boolean isAtleastEight(){
		return ItemUtils.getInstance().hasBanner();
	}
	
	private static boolean nine = isNine();
	
	private static boolean isNine(){
		try{
			Material m = Material.ELYTRA;
			return true;
		}catch(Throwable e){
		}
		return false;
	}
	
	public static boolean isAtleastNine(){
		return nine;
	}
	
	private static boolean thirteen = isThirteen();
	
	private static boolean isThirteen(){
		try{
			Material m = Material.BLACK_STAINED_GLASS_PANE;
			return true;
		}catch(Throwable e){
		}
		return false;
	}
	
	public static boolean isAtleastThirteen(){
		return thirteen;
	}
}
