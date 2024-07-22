package me.dablakbandit.core.utils;

import org.bukkit.Material;

public class Version{


	public static boolean banner = getBanner();

	public static boolean getBanner(){
		try{
			Material m = getMaterial("BANNER", "BLACK_BANNER");
			if(m != null){ return true; }
		}catch(Exception e){
		}
		return false;
	}

	public static boolean hasBanner(){
		return banner;
	}

	public static Material getMaterial(String... possible){
		for(String s : possible){
			try{
				Material m = Material.valueOf(s);
				if(m != null){ return m; }
			}catch(Exception e){
			}
		}
		return null;
	}

	public static boolean isAtleastEight(){
		return hasBanner();
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
