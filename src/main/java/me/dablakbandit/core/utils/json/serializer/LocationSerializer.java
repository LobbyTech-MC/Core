package me.dablakbandit.core.utils.json.serializer;

import java.lang.reflect.Type;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.*;

public class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location>{
	@Override
	public Location deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException{
		try{
			JsonObject data = json.getAsJsonObject();
			Location loc = new Location(Bukkit.getWorld(data.get("world").getAsString()), data.get("x").getAsDouble(), data.get("y").getAsDouble(), data.get("z").getAsDouble());
			
			if(data.has("yaw")){
				loc.setYaw(data.get("yaw").getAsFloat());
			}
			if(data.has("pitch")){
				loc.setPitch(data.get("pitch").getAsFloat());
			}
			return loc;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public JsonElement serialize(Location src, Type type, JsonSerializationContext context){
		try{
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("world", src.getWorld().getName());
			jsonObject.addProperty("x", src.getX());
			jsonObject.addProperty("y", src.getY());
			jsonObject.addProperty("z", src.getZ());
			jsonObject.addProperty("yaw", src.getYaw());
			jsonObject.addProperty("pitch", src.getPitch());
			return jsonObject;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
