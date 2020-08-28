package me.dablakbandit.core.utils.json.serializer;

import java.lang.reflect.Type;

import org.bukkit.inventory.ItemStack;

import com.google.gson.*;

import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.itemutils.IItemUtils;

public class ItemStackSerializer implements JsonSerializer<Object>, JsonDeserializer<Object>{
	
	private static ItemStackSerializer serializer = new ItemStackSerializer();
	
	public static ItemStackSerializer getInstance(){
		return serializer;
	}
	
	private static IItemUtils itemUtils = ItemUtils.getInstance();
	
	private ItemStackSerializer(){
		
	}
	
	@Override
	public Object deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException{
		try{
			JsonObject itemstack = json.getAsJsonObject();
			return itemUtils.convertJSONToItemStack(new JSONObject(itemstack.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public JsonElement serialize(Object src, Type type, JsonSerializationContext context){
		try{
			String json = itemUtils.convertItemStackToJSON((ItemStack)src).toString();
			return new JsonParser().parse(json).getAsJsonObject();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
