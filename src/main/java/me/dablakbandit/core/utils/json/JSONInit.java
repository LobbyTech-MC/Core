package me.dablakbandit.core.utils.json;

import com.google.gson.JsonObject;

public interface JSONInit{
	
	static <T> T fromJSON(JsonObject jo, Class<T> clazz){
		return JSONParser.fromJSON(jo, clazz);
	}
	
	void init();
}
