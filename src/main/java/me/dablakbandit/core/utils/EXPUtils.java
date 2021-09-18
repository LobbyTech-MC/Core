package me.dablakbandit.core.utils;

import org.bukkit.entity.Player;

public class EXPUtils{

	@Deprecated
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

	@Deprecated
	public static int getExpRequired(int level){
		if(ItemUtils.getInstance().hasBanner()){
			return level == 0 ? 0 : level > 31 ? 9 * --level - 158 : level > 16 ? 5 * --level - 38 : --level * 2 + 7;
		}else{
			return level == 0 ? 0 : level > 29 ? 7 * --level - 148 : level > 15 ? --level * 3 - 28 : 17;
		}
	}

	@Deprecated
	public static int getTotalExperience(Player player){
		return Math.round(getExpRequired(player.getLevel() + 1) * player.getExp()) + getTotalExperience(player.getLevel());
	}

	@Deprecated
	public static int getTotalExperience(int level){
		if(ItemUtils.getInstance().hasBanner()){
			return (int)(level == 0 ? 0 : level > 31 ? 4.5 * Math.pow(level, 2) - 162.5 * level + 2220 : level > 16 ? 2.5 * Math.pow(level, 2) - 40.5 * level + 360 : Math.pow(level, 2) + 6 * level);
		}else{
			return (int)(level == 0 ? 0 : level > 30 ? 3.5 * Math.pow(level, 2) - 151.5 * level + 2220 : level > 15 ? 1.5 * Math.pow(level, 2) - 29.5 * level + 360 : level * 17);
		}
	}

	@Deprecated
	public static int getExpUntilNextLevel(Player player){
		int i = getExpRequired(player.getLevel());
		return i - (int)Math.round((double)i * player.getExp());
	}

	public static int getExp(Player player) {
		return getExpFromLevel(player.getLevel())
				+ Math.round(getExpToNext(player.getLevel()) * player.getExp());
	}

	public static void addExp(Player player, int exp) {
		exp += getExp(player);
		setExp(player, exp);
	}

	public static void setExp(Player player, int exp) {
		if (exp < 0) {
			exp = 0;
		}
		double levelAndExp = getLeveledFromExp(exp);
		int level = (int) levelAndExp;
		player.setLevel(level);
		player.setExp((float) (levelAndExp - level));
	}


	public static int getLevelFromExp(long exp){
		if(ItemUtils.getInstance().hasBanner()) {
			return getLevelFromExpBanner(exp);
		}else{
			return getLevelFromExpOld(exp);
		}
	}

	private static int getLevelFromExpOld(long exp) {
		int level = 0;
		while(getExpFromLevelOld(level) < exp){
			level++;
		}
		return level;
	}

	private static int getLevelFromExpBanner(long exp) {
		if (exp > 1395) {
			return (int) ((Math.sqrt(72 * exp - 54215D) + 325) / 18);
		}
		if (exp > 315) {
			return (int) (Math.sqrt(40 * exp - 7839D) / 10 + 8.1);
		}
		if (exp > 0) {
			return (int) (Math.sqrt(exp + 9D) - 3);
		}
		return 0;
	}

	public static int getExpFromLevel(int level){
		if(ItemUtils.getInstance().hasBanner()){
			return getExpFromLevelBanner(level);
		}else{
			return getExpFromLevelOld(level);
		}
	}

	private static int getExpFromLevelOld(int level){
		if(level > 30){
			return (int)(3.5 * level * level - 151.5 * level + 2220);
		}
		if(level > 15){
			return  (int)(1.5 * level * level - 29.5 * level + 360);
		}
		return level * 17;
	}

	private static int getExpFromLevelBanner(int level) {
		if (level > 30) {
			return (int) (4.5 * level * level - 162.5 * level + 2220);
		}
		if (level > 15) {
			return (int) (2.5 * level * level - 40.5 * level + 360);
		}
		return level * level + 6 * level;
	}

	public static double getLeveledFromExp(long exp) {
		int level = getLevelFromExp(exp);
		float remainder = exp - (float) getExpFromLevel(level);
		float progress = remainder / getExpToNext(level);
		return (double) level + progress;
	}

	public static int getExpToNext(int level){
		if(ItemUtils.getInstance().hasBanner()){
			return getExpToNextBanner(level);
		}else{
			return getExpToNextOld(level);
		}
	}

	private static int getExpToNextOld(int level) {
		if (level >= 31) {
			// Simplified formula. 62 + (currentLevel - 30) * 7;
			return level * 7 - 148;
		}
		if (level >= 16) {
			// Simplified formula. Internal: 17 + (currentLevel - 15) * 3;
			return level * 3 - 28;
		}
		// Internal: 17
		return 17;
	}

	private static int getExpToNextBanner(int level) {
		if (level >= 30) {
			// Simplified formula. Internal: 112 + (level - 30) * 9
			return level * 9 - 158;
		}
		if (level >= 15) {
			// Simplified formula. Internal: 37 + (level - 15) * 5
			return level * 5 - 38;
		}
		// Internal: 7 + level * 2
		return level * 2 + 7;
	}
}
