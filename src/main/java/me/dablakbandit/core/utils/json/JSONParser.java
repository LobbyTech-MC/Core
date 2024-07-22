package me.dablakbandit.core.utils.json;

import com.google.gson.*;
import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.json.serializer.ItemStackSerializer;
import me.dablakbandit.core.utils.json.serializer.JSONFormatterSerializer;
import me.dablakbandit.core.utils.json.serializer.LocationSerializer;
import me.dablakbandit.core.utils.json.strategy.AnnotationExclusionStrategy;
import me.dablakbandit.core.utils.json.strategy.CorePlayersExclusionStrategy;
import me.dablakbandit.core.utils.json.strategy.Exclude;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public class JSONParser{

	private static GsonBuilder builder = new GsonBuilder();
	private static JsonParser	parser	= new JsonParser();
	private static Gson			gson;

	static {
		builder.serializeNulls();
		builder.registerTypeAdapterFactory(new JSONDataFactory());
		builder.setExclusionStrategies(new AnnotationExclusionStrategy());
		builder.setExclusionStrategies(new CorePlayersExclusionStrategy());
		builder.registerTypeAdapter(ItemStack.class, ItemStackSerializer.getInstance());
		Class<?> obcClass = NMSUtils.getOBCClassSilent("inventory.CraftItemStack");
		if(obcClass==null){
			obcClass = NMSUtils.getClassSilent("org.bukkit.craftbukkit.inventory.CraftItemStack");
		}
		if(obcClass!=null){
			builder.registerTypeAdapter(obcClass, ItemStackSerializer.getInstance());
		}
		builder.registerTypeAdapter(JSONFormatter.class, new JSONFormatterSerializer());
		builder.registerTypeAdapter(Location.class, new LocationSerializer());
		build();
	}

	private static void build(){
		gson = builder.create();
	}

	public static void registerTypeAdapter(Class<?> clazz, Object adapter){
		builder.registerTypeAdapter(clazz, adapter);
		build();
	}
	
	public static <T> T fromJSON(JsonObject jo, Class<T> clazz){
		return gson.fromJson(jo, clazz);
	}
	
	public static <T> T fromJSON(String json, Class<T> clazz){
		return gson.fromJson(json, clazz);
	}
	
	public static JsonElement parse(String json){
		return parser.parse(json);
	}
	
	public static JsonObject toJson(Object o){
		try{
			return parser.parse(gson.toJson(o)).getAsJsonObject();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static void loadAndCopy(Object from, JsonObject json){
		Object cloned = fromJSON(json, from.getClass());
		copy(from, cloned);
	}

	public static void loadAndCopy(Object from, String json){
		Object cloned = fromJSON(json, from.getClass());
		copy(from, cloned);
	}

	private static void copy(Object from, Object cloned){
		try{
			NMSUtils.getFields(from.getClass()).forEach(field -> {
				if(field.getAnnotation(Exclude.class) != null){ return; }
				if(Modifier.isStatic(field.getModifiers())){ return; }
				if(field.getDeclaringClass().equals(from.getClass())){
					try{
						Object original = field.get(from);
						Object value = field.get(cloned);
						if(original instanceof Collection){
							if(value == null){ return; }
							((Collection)original).clear();
							((Collection)original).addAll((Collection)value);
						}else if(original instanceof Map){
							if(value == null){ return; }
							((Map)original).putAll((Map)value);
						}
						field.set(from, value);
					}catch(Exception e){
						System.err.println(field.getName());
						e.printStackTrace();
					}
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
