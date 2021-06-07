package me.dablakbandit.core.players;

import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.commands.TokensCommand;
import me.dablakbandit.core.database.DatabaseManager;
import me.dablakbandit.core.database.listener.SQLPermissions;
import me.dablakbandit.core.database.listener.SQLTokens;
import me.dablakbandit.core.players.chatapi.ChatAPIPlayersListener;
import me.dablakbandit.core.players.info.CorePlayersInfo;
import me.dablakbandit.core.players.inventory.OpenInventory;
import me.dablakbandit.core.players.listener.*;
import me.dablakbandit.core.players.selection.SelectionPlayerListener;
import me.dablakbandit.core.server.packet.ServerPacketManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CorePlayerManager implements Listener{
	
	private static CorePlayerManager manager = new CorePlayerManager();
	
	public static CorePlayerManager getInstance(){
		return manager;
	}
	
	private Map<String, CorePlayers>	players		= new HashMap<String, CorePlayers>();
	
	private List<CorePlayersListener>	listeners	= new ArrayList<CorePlayersListener>();
	
	private CorePlayerManager(){
		
	}
	
	public void addListener(CorePlayersListener cpl){
		if(!listeners.contains(cpl)){
			listeners.add(cpl);
			for(CorePlayers pl : players.values()){
				cpl.addCorePlayers(pl);
				cpl.loadCorePlayers(pl);
			}
		}
	}
	
	public void removeListener(CorePlayersListener cpl){
		if(listeners.contains(cpl)){
			listeners.remove(cpl);
			for(CorePlayers pl : players.values()){
				cpl.saveCorePlayers(pl);
				cpl.removeCorePlayers(pl);
			}
		}
	}
	
	public void enablePermissions(){
		addListener(PermissionsPlayersListener.getInstance());
		DatabaseManager.getInstance().enableCoreDatabase();
		DatabaseManager.getInstance().getCoreDatabase().addListener(SQLPermissions.getInstance());
	}
	
	public void enableTokens(){
		addListener(TokensPlayersListener.getInstance());
		DatabaseManager.getInstance().enableCoreDatabase();
		DatabaseManager.getInstance().getCoreDatabase().addListener(SQLTokens.getInstance());
		TokensCommand.getInstance();
	}
	
	public void enablePacketListener(){
		addListener(ServerPacketPlayersListener.getInstance());
	}
	
	public void enableSelectionListener(){
		addListener(SelectionPlayerListener.getInstance());
	}
	
	public void enableScoreboard(){
		addListener(ScoreboardPlayersListener.getInstance());
	}
	
	public void enableChatAPI(){
		enablePacketListener();
		addListener(ChatAPIPlayersListener.getInstance());
	}
	
	public void enable(){
		Bukkit.getPluginManager().registerEvents(this, CorePlugin.getInstance());
		for(Player player : Bukkit.getOnlinePlayers()){
			addPlayer(player);
			loadPlayer(player);
		}
	}
	
	public void disable(){
		for(Player player : Bukkit.getOnlinePlayers()){
			removePlayer(player);
		}
		ServerPacketManager.getInstance().disable();
	}
	
	public Map<String, CorePlayers> getPlayers(){
		return players;
	}
	
	public CorePlayers getPlayer(Player player){
		if(player == null){ return null; }
		String uuid = PlayerGetter.getUUID(player);
		return players.get(uuid);
	}
	
	public CorePlayers getPlayer(String uuid){
		return players.get(uuid);
	}
	
	public <T extends CorePlayersInfo> List<T> getInfo(Class<T> clazz){
		List<T> list = new ArrayList<T>();
		for(CorePlayers pl : players.values()){
			T t = pl.getInfo(clazz);
			if(t != null){
				list.add(t);
			}
		}
		return list;
	}
	
	private CorePlayers addPlayer(Player player){
		CorePlayers pl = getPlayer(player);
		if(pl != null){ return pl; }
		pl = new CorePlayers(player);
		players.put(pl.getUUIDString(), pl);
		for(CorePlayersListener cpl : listeners){
			cpl.loginCorePlayers(pl);
		}
		return pl;
	}
	
	private void loadPlayer(Player player){
		CorePlayers pl = getPlayer(player);
		if(pl == null){
			pl = addPlayer(player);
		}
		if(pl == null){ return; }
		for(CorePlayersListener cpl : listeners){
			cpl.addCorePlayers(pl);
		}
		pl.load();
		for(CorePlayersListener cpl : listeners){
			cpl.loadCorePlayers(pl);
		}
	}
	
	private void removePlayer(Player player){
		CorePlayers pl = getPlayer(player);
		if(pl == null){ return; }
		player.closeInventory();
		if(pl.isLoaded()){
			for(CorePlayersListener cpl : listeners){
				cpl.saveCorePlayers(pl);
			}
			pl.save();
			for(CorePlayersListener cpl : listeners){
				cpl.removeCorePlayers(pl);
			}
		}
		players.remove(pl.getUUIDString());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerLoginEvent event){
		if(event.getResult() == PlayerLoginEvent.Result.ALLOWED){
			addPlayer(event.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		loadPlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent event){
		if(!event.isCancelled()){
			removePlayer(event.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event){
		removePlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event){
		if(!(event.getWhoClicked() instanceof Player)){ return; }
		Player player = (Player)event.getWhoClicked();
		CorePlayers pl = getPlayer(player);
		if(pl == null)
			return;
		if(pl.getOpenInventory() != null){
			if(event.getSlot() == -999){
				// Drop packet
				event.setCancelled(true);
				return;
			}
			Inventory top = player.getOpenInventory().getTopInventory();
			Inventory clicked = null;
			if(event.getSlot() < 0){
				// Null click
				return;
			}else if(event.getView().getTopInventory() != null && event.getRawSlot() < event.getView().getTopInventory().getSize()){
				clicked = event.getView().getTopInventory();
			}else{
				clicked = event.getView().getBottomInventory();
			}
			if(top == null || clicked == null){
				// Shouldn't happen but just check anyways
				return;
			}
			OpenInventory io = pl.getOpenInventory();
			io.parseClick(pl, player, clicked, top, event);
		}else if(event.getInventory().getType() == InventoryType.CRAFTING){
			// Player Inventory do something maybe?
			// parseClick(event, event.getInventory(), player, pl);
		}else{
			// All other clicks (other inventories not from this plugin)
		}
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event){
		if(event.isCancelled() || !(event.getWhoClicked() instanceof Player)){ return; }
		Player player = (Player)event.getWhoClicked();
		CorePlayers pl = getPlayer(player);
		if(pl == null){ return; }
		if(pl.getOpenInventory() != null){
			Inventory top = player.getOpenInventory().getTopInventory();
			Inventory inv = event.getInventory();
			pl.getOpenInventory().parseInventoryDrag(pl, player, inv, top, event);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		if(!(event.getPlayer() instanceof Player)){ return; }
		Player player = (Player)event.getPlayer();
		CorePlayers pl = getPlayer(player);
		if(pl == null){ return; }
		if(pl.getOpenInventory() != null){
			if(event.getInventory().getType() != InventoryType.CRAFTING){
				pl.closeInventory(player);
			}
		}else if(event.getInventory().getType() == InventoryType.CRAFTING){
			// Player Inventory closing (can detect closing but not opening)
			// onClose(player, pl);
		}
	}
	
}
