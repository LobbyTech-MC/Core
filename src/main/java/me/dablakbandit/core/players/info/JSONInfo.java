package me.dablakbandit.core.players.info;

import com.google.gson.JsonObject;

import me.dablakbandit.core.utils.json.JSONParser;

public interface JSONInfo{
	
	void jsonInit();
	
	void jsonFinal();
	
	default JsonObject toJson(){
		return JSONParser.toJson(this);
	}
	
}
