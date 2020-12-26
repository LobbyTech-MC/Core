package me.dablakbandit.core.utils.json;

import com.google.gson.JsonObject;

public interface JSONFinal {
	
	static <T> T fromJSON(JsonObject jo, Class<T> clazz){
		return JSONParser.fromJSON(jo, clazz);
	}

	void finalJson();
}
