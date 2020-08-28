package me.dablakbandit.core.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class InventoryHandlers{
	
	public static <T extends Enum<T>> InventoryHandlers createHandlers(Class<T> enumm, Function<T, InventoryHandler> function){
		InventoryHandlers handler = new InventoryHandlers();
		for(Enum<T> enumConstant : enumm.getEnumConstants()){
			handler.addHandler(enumConstant, function);
		}
		return handler;
	}
	
	private Map<Object, Function<Object, InventoryHandler>> handlers = new HashMap();
	
	protected InventoryHandlers(){
		
	}
	
	private void addHandler(Object handler, Function function){
		handlers.put(handler, function);
	}
	
	public <T extends Enum<T>> InventoryHandler createInventory(T enumm){
		return handlers.get(enumm).apply(enumm);
	}
}
