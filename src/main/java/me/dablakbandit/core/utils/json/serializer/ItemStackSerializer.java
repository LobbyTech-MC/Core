package me.dablakbandit.core.utils.json.serializer;

import com.google.gson.*;
import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.itemutils.IItemUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Base64;

public class ItemStackSerializer implements JsonSerializer<Object>, JsonDeserializer<Object>{
	
	private static ItemStackSerializer serializer = new ItemStackSerializer();
	
	public static ItemStackSerializer getInstance(){
		return serializer;
	}
	
	private static IItemUtils itemUtils;
	
	private ItemStackSerializer(){
		
	}
	
	@Override
	public Object deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException{
		try {
			String asString = json.getAsString();
			byte[] byteArray = Base64.getDecoder().decode(asString);
			ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
			BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);
			ItemStack itemStack = (ItemStack) bois.readObject();
			bois.close();
			itemStack.getType();
			return itemStack;
		} catch (Exception e) {
			if(itemUtils==null){
				itemUtils = ItemUtils.getInstance();
			}
			try{
				JsonObject itemstack = json.getAsJsonObject();
				return itemUtils.convertJSONToItemStack(new JSONObject(itemstack.toString()));
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public JsonElement serialize(Object item, Type type, JsonSerializationContext context){
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos);
			boos.writeObject(item);

			boos.flush();
			boos.close();

			byte[] byteArray = baos.toByteArray();
			String base64Encoded = Base64.getEncoder().encodeToString(byteArray);
			return new JsonPrimitive(base64Encoded);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
