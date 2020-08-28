package me.dablakbandit.core.inventory;

import java.util.function.Function;

public class PlayerInventoryHandlers{
	
	public static <T extends Enum<T>> PlayerInventoryHandlers createHandlers(T enumm, Function<T, PlayerInventoryHandler> function){
		return null;
	}
	
	private PlayerInventoryHandlers(){
		
	}
}
