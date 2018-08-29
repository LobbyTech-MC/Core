package me.dablakbandit.core.utils;

import org.bukkit.entity.Player;

public class EXPUtils{
	
	public static void setTotalExperience(Player player, int exp){
		player.setExp(0.0F);
		player.setLevel(0);
		player.setTotalExperience(exp);
		int level = 0;
		while(exp >= getTotalExperience(level + 1)){
			level++;
		}
		int levelexp = getTotalExperience(level);
		int left = exp - levelexp;
		int bar = getExpRequired(level + 1);
		player.setLevel(level);
		player.setExp((float)((double)left / (double)bar));
	}
	
	public static int getExpRequired(int level){
		System.out.println(ItemUtils.getInstance().hasBanner());
		if(ItemUtils.getInstance().hasBanner()){
			return level == 0 ? 0 : level > 31 ? 9 * --level - 158 : level > 16 ? 5 * --level - 38 : --level * 2 + 7;
		}else{
			return level == 0 ? 0 : level > 29 ? 7 * --level - 148 : level > 15 ? --level * 3 - 28 : 17;
		}
	}
	
	public static int getTotalExperience(Player player){
		return Math.round(getExpRequired(player.getLevel() + 1) * player.getExp()) + getTotalExperience(player.getLevel());
	}
	
	public static int getTotalExperience(int level){
		if(ItemUtils.getInstance().hasBanner()){
			return (int)(level == 0 ? 0 : level > 31 ? 4.5 * Math.pow(level, 2) - 162.5 * level + 2220 : level > 16 ? 2.5 * Math.pow(level, 2) - 40.5 * level + 360 : Math.pow(level, 2) + 6 * level);
		}else{
			return (int)(level == 0 ? 0 : level > 30 ? 3.5 * Math.pow(level, 2) - 151.5 * level + 2220 : level > 15 ? 1.5 * Math.pow(level, 2) - 29.5 * level + 360 : Math.pow(level, 2) * 17);
		}
	}
	
	public static int getExpUntilNextLevel(Player player){
		int i = getExpRequired(player.getLevel());
		return i - (int)Math.round((double)i * player.getExp());
	}
}
