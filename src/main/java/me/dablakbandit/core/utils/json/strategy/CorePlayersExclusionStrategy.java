/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.utils.json.strategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import me.dablakbandit.core.players.CorePlayers;

public class CorePlayersExclusionStrategy implements ExclusionStrategy{
	
	@Override
	public boolean shouldSkipField(FieldAttributes f){
		return f.getDeclaredType().getClass().equals(CorePlayers.class);
	}
	
	@Override
	public boolean shouldSkipClass(Class<?> clazz){
		return clazz.equals(CorePlayers.class);
	}
}
