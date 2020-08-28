package me.dablakbandit.core.inventory;

import me.dablakbandit.core.inventory.handler.ItemInfoHandler;
import me.dablakbandit.core.inventory.handler.ItemInfoInventoryEventHandler;
import me.dablakbandit.core.inventory.handler.ItemInfoInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.inventory.OpenInventory;
import me.dablakbandit.core.utils.string.StringListReplacer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class InventoryHandler<T>extends OpenInventory{
	
	private BiFunction<T, Integer, ItemStack>[]	itemSupplier;
	private Function[]							functions;
	private ItemInfoInventoryEventHandler[]		handlers;
	private PlayerInventoryHandler				playerInventoryHandler;
	protected InventoryDescriptor				descriptor;
	
	public void setItem(int slot, BiFunction<T, Integer, ItemStack> supplier, Function<Integer, T> function, ItemInfoInventoryEventHandler<T> handler){
		if(slot < 0 || slot >= itemSupplier.length){ return; }
		itemSupplier[slot] = supplier;
		functions[slot] = function;
		handlers[slot] = handler;
	}
	
	public void setItem(int slot, BiFunction<T, Integer, ItemStack> supplier, ItemInfoInventoryEventHandler<T> handler){
		if(slot < 0 || slot >= itemSupplier.length){ return; }
		itemSupplier[slot] = supplier;
		handlers[slot] = handler;
	}
	
	public void setItem(int slot, Function<T, ItemStack> supplier, ItemInfoInventoryEventHandler<T> handler){
		if(slot < 0 || slot >= itemSupplier.length){ return; }
		itemSupplier[slot] = (t, i) -> supplier.apply(t);
		handlers[slot] = handler;
	}
	
	public void setItem(int slot, Supplier<ItemStack> supplier, ItemInfoInventoryEventHandler<T> handler){
		if(slot < 0 || slot >= itemSupplier.length){ return; }
		itemSupplier[slot] = (t, i) -> supplier.get();
		handlers[slot] = handler;
	}
	
	public void setAll(int size, Supplier<ItemStack> supplier){
		for(int slot = 0; slot < size; slot++){
			setItem(slot, supplier);
		}
	}

	public void setAll(int size, Function<T, ItemStack> supplier){
		for(int slot = 0; slot < size; slot++){
			setItem(slot, supplier);
		}
	}
	
	public void setItem(int slot, Function<T, ItemStack> supplier, ItemInfoInventoryHandler<T> handler){
		setItem(slot, supplier, (a, b, c, d) -> handler.onClick(a, b, c));
	}
	
	public void setItem(int slot, Function<T, ItemStack> supplier, ItemInfoHandler<T> handler){
		setItem(slot, supplier, (a, b, c, d) -> handler.onClick(a, b));
	}
	
	public void setItem(int slot, Function<T, ItemStack> supplier, Consumer<CorePlayers> handler){
		setItem(slot, supplier, (a, b, c, d) -> handler.accept(a));
	}
	
	public void setItem(int slot, Function<T, ItemStack> supplier){
		setItem(slot, supplier, (ItemInfoInventoryEventHandler)null);
	}
	
	public void setItem(int slot, Supplier<ItemStack> supplier){
		setItem(slot, supplier, (ItemInfoInventoryEventHandler)null);
	}
	
	public void setItem(int slot, Supplier<ItemStack> supplier, ItemInfoInventoryHandler<T> handler){
		setItem(slot, supplier, (a, b, c, d) -> handler.onClick(a, b, c));
	}
	
	public void setItem(int slot, Supplier<ItemStack> supplier, ItemInfoHandler<T> handler){
		setItem(slot, supplier, (a, b, c, d) -> handler.onClick(a, b));
	}
	
	public void setItem(int slot, Supplier<ItemStack> supplier, Consumer<CorePlayers> handler){
		setItem(slot, supplier, (a, b, c, d) -> handler.accept(a));
	}
	
	public void setup(){
		itemSupplier = new BiFunction[descriptor.getSize()];
		functions = new Function[descriptor.getSize()];
		handlers = new ItemInfoInventoryEventHandler[descriptor.getSize()];
		init();
	}
	
	public abstract void init();
	
	public abstract T getInvoker(CorePlayers pl);
	
	public void setPlayerInventoryHandler(PlayerInventoryHandler handler){
		this.playerInventoryHandler = handler;
	}
	
	public void setDescriptor(InventoryDescriptor descriptor){
		this.descriptor = descriptor;
	}
	
	protected boolean open(CorePlayers pl, Player player, int size, String title){
		Inventory inv = Bukkit.createInventory(null, size, title.replace("<player>", player.getName()));
		set(pl, player, inv);
		return player.openInventory(inv) != null;
	}
	
	@Override
	public boolean open(CorePlayers pl, Player player){
		return open(pl, player, descriptor.getSize(), ChatColor.translateAlternateColorCodes('&', descriptor.getTitle()));
	}
	
	@Override
	public void set(CorePlayers pl, Player player, Inventory inventory){
		T invoker = getInvoker(pl);
		for(int slot = 0; slot < itemSupplier.length; slot++){
			BiFunction<T, Integer, ItemStack> function = itemSupplier[slot];
			if(function == null){
				inventory.setItem(slot, null);
			}else{
				inventory.setItem(slot, function.apply(invoker, slot));
			}
		}
	}
	
	@Override
	public void parseClick(CorePlayers pl, Player player, Inventory clicked, Inventory top, InventoryClickEvent event){
		if(clicked == top){
			if(defaultCancel()){
				event.setCancelled(true);
			}
			int slot = event.getRawSlot();
			ItemInfoInventoryEventHandler handler = handlers[slot];
			if(handler == null){ return; }
			Function<Integer, Object> function = functions[slot];
			if(function == null){
				function = (i) -> getInvoker(pl);
			}
			handler.onClick(pl, function.apply(slot), clicked, event);
		}else{
			if(playerInventoryHandler == null){
				event.setCancelled(true);
				return;
			}
			playerInventoryHandler.parseClick(pl, clicked, top, event);
		}
	}
	
	@Override
	public void parseInventoryDrag(CorePlayers pl, Player player, Inventory clicked, Inventory top, InventoryDragEvent event){
		if(playerInventoryHandler == null){
			event.setCancelled(true);
			return;
		}
		playerInventoryHandler.parseInventoryDrag(pl, clicked, top, event);
	}
	
	protected boolean defaultCancel(){
		return true;
	}
	
	protected static List<String> replaceLore(List<String> lore, String match, String replace){
		return lore.stream().map(s -> s.replace(match, replace)).collect(Collectors.toList());
	}
	
	protected static ItemStack clone(ItemStack is, String name, List<String> lore){
		return change(is.clone(), name, lore.toArray(new String[0]));
	}
	
	protected static ItemStack clone(ItemStack is, String name, StringListReplacer lore){
		return change(is.clone(), name, lore.toArray());
	}
	
	public boolean hasPermission(Player player){
		return !descriptor.hasPermission() || player.hasPermission(descriptor.getPermission());
	}
}
