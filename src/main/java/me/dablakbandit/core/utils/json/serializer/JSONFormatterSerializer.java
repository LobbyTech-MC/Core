package me.dablakbandit.core.utils.json.serializer;

import java.lang.reflect.Type;

import com.google.gson.*;

import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;

public class JSONFormatterSerializer implements JsonSerializer<JSONFormatter>, JsonDeserializer<JSONFormatter>{
	@Override
	public JSONFormatter deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException{
		try{
			JSONFormatter jf = new JSONFormatter();
			jf.append(new JSONObject(json.getAsJsonObject().toString()));
			return jf;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public JsonElement serialize(JSONFormatter src, Type type, JsonSerializationContext context){
		try{
			return new JsonParser().parse(src.toJSON()).getAsJsonObject();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
