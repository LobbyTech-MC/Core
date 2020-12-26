/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.utils.json;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class JSONData implements JSONInit{
	
	public static <T> T fromJSON(JsonObject jo, Class<T> clazz){
		return JSONInit.fromJSON(jo, clazz);
	}
	
	public JsonObject toJson(){
		return JSONParser.toJson(this);
	}
	
	public void initJson(){
		
	}
	
	public void read(JsonReader reader){
		
	}
}
