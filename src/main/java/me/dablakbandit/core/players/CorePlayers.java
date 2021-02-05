package me.dablakbandit.core.players;

import me.dablakbandit.core.players.event.OpenInventoryChangeEvent;
import me.dablakbandit.core.players.info.CorePlayersInfo;
import me.dablakbandit.core.players.inventory.OpenInventory;
import me.dablakbandit.core.players.inventory.Updater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class CorePlayers{
	
	protected Player													player;
	protected String													uuid;
	protected String													name;
	protected boolean													loaded	= false;
	
	// Open inventory and Opening inventory (for checks on close of current inventory)
	protected OpenInventory												open_inv, opening_inv;
	
	protected Map<Class<? extends CorePlayersInfo>, CorePlayersInfo>	info	= new LinkedHashMap<Class<? extends CorePlayersInfo>, CorePlayersInfo>();
	protected Map<Class<?>, CorePlayersInfo> infoType = new LinkedHashMap<>();

	public CorePlayers(Player player){
		this.player = player;
		this.uuid = PlayerGetter.getUUID(player);
		this.name = player.getName();
	}
	
	public CorePlayers(String uuid){
		this.uuid = uuid;
	}
	
	public void load(){
		// TODO load before
		for(CorePlayersInfo cpi : info.values()){
			cpi.load();
		}
		loaded = true;
	}
	
	public boolean isLoaded(){
		return loaded;
	}
	
	public void save(){
		for(CorePlayersInfo cpi : info.values()){
			cpi.save();
		}
		// TODO save after
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public String getUUIDString(){
		return uuid;
	}
	
	public String getName(){
		return name;
	}
	
	public UUID getUUID(){
		return UUID.fromString(uuid);
	}
	
	public OpenInventory getOpenInventory(){
		return open_inv;
	}
	
	public OpenInventory getOpeningInventory(){
		return opening_inv;
	}
	
	public void setOpenInventory(OpenInventory open){
		if(this.open_inv == open){ return; }
		OpenInventoryChangeEvent oice = new OpenInventoryChangeEvent(this, this.open_inv, open);
		Bukkit.getPluginManager().callEvent(oice);
		open = oice.getTo();
		if(open == null){
			player.closeInventory();
			return;
		}
		this.opening_inv = open;
		if(open.open(this, player)){
			this.open_inv = open;
		}
	}
	
	public void refreshInventory(){
		if(this.open_inv == null){ return; }
		this.open_inv.set(this, player, player.getOpenInventory().getTopInventory());
	}
	
	public void refreshInventory(Class<? extends OpenInventory> clazz){
		if(!hasInventoryOpen(clazz)){ return; }
		this.open_inv.set(this, player, player.getOpenInventory().getTopInventory());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends OpenInventory> void updateInventory(Class<T> clazz, Updater<T> u){
		if(!hasInventoryOpen(clazz)){ return; }
		u.update((T)open_inv, this);
	}
	
	public boolean hasInventoryOpen(Class<? extends OpenInventory> clazz){
		if(this.open_inv == null){ return false; }
		return checkInventoryClass(this.open_inv.getClass(), clazz);
		// Class<?> check = this.open_inv.getClass().toString().contains("$") ? this.open_inv.getClass().getSuperclass() : this.open_inv.getClass();
		// return check.isAssignableFrom(clazz);
	}
	
	public boolean hasInventoryOpening(Class<? extends OpenInventory> clazz){
		if(this.opening_inv == null){ return false; }
		return checkInventoryClass(this.opening_inv.getClass(), clazz);
		// Class<?> check = this.opening_inv.getClass().toString().contains("$") ? this.opening_inv.getClass().getSuperclass() : this.opening_inv.getClass();
		// return check.isAssignableFrom(clazz);
	}
	
	private boolean checkInventoryClass(Class<? extends OpenInventory> to, Class<? extends OpenInventory> with){
		if(with.isAssignableFrom(to)){ return true; }
		if(to.getSuperclass().equals(OpenInventory.class)){ return false; }
		return checkInventoryClass((Class<? extends OpenInventory>)to.getSuperclass(), with);
	}
	
	public void closeInventory(Player player){
		if(this.open_inv != null){
			OpenInventory oi = this.open_inv;
			this.open_inv = null;
			oi.onClose(this, player);
		}
	}
	
	public Collection<CorePlayersInfo> getAllInfo(){
		return info.values();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends CorePlayersInfo> T getInfo(Class<T> clazz){
		return (T)info.get(clazz);
	}
	
	public <T extends CorePlayersInfo> List<T> getAllInfo(Class<T> clazz){
		List<T> list = new ArrayList<>();
		for(Map.Entry<Class<? extends CorePlayersInfo>, CorePlayersInfo> info : info.entrySet()){
			if(clazz.isAssignableFrom(info.getKey())){
				list.add((T)info.getValue());
			}
		}
		return list;
	}

	@Deprecated
	public <T> T getFirstInfoType(Class<T> clazz){
		return getType(clazz);
	}

	public <T> T getType(Class<T> clazz){
		Object t = infoType.get(clazz);
		if(t == null){
			List list = getAllInfoType(clazz);
			if(list.size() != 0){
				t = list.get(0);
				infoType.put(clazz, (CorePlayersInfo) t);
			}
		}
		return t != null ? (T)t : null;
	}

	public <T> List<T> getAllInfoType(Class<T> clazz){
		List<T> list = new ArrayList<>();
		for(Map.Entry<Class<? extends CorePlayersInfo>, CorePlayersInfo> info : info.entrySet()){
			if(clazz.isAssignableFrom(info.getKey())){
				list.add((T)info.getValue());
			}
		}
		return list;
	}
	
	public <T extends CorePlayersInfo> void addInfo(T instance){
		info.put(instance.getClass(), instance);
	}
}
