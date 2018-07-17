package me.dablakbandit.core.players.selection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.listener.CorePlayersListener;
import me.dablakbandit.core.utils.ItemUtils;

public class SelectionPlayerListener extends CorePlayersListener implements Listener{
	
	private static SelectionPlayerListener selection = new SelectionPlayerListener();
	
	public static SelectionPlayerListener getInstance(){
		return selection;
	}
	
	private ItemStack tool;
	
	private SelectionPlayerListener(){
		Bukkit.getPluginManager().registerEvents(this, CorePlugin.getInstance());
		this.tool = new ItemStack(ItemUtils.getInstance().getMaterial("WOOD_SPADE", "WOODEN_SHOVEL"));
		ItemMeta temp = this.tool.getItemMeta();
		temp.setDisplayName(ChatColor.RED + "Selection Tool");
		this.tool.setItemMeta(temp);
	}
	
	@Override
	public void addCorePlayers(CorePlayers pl){
		pl.addInfo(new SelectionPlayerInfo(pl));
	}
	
	@Override
	public void loadCorePlayers(CorePlayers pl){
		
	}
	
	@Override
	public void saveCorePlayers(CorePlayers pl){
		
	}
	
	@Override
	public void removeCorePlayers(CorePlayers pl){
		
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getHand() == EquipmentSlot.OFF_HAND){ return; }
		if(event.getPlayer().getInventory().getItemInMainHand().equals(tool)){
			event.setCancelled(true);
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				CorePlayerManager.getInstance().getPlayer(event.getPlayer()).getInfo(SelectionPlayerInfo.class).setPoint2(event.getClickedBlock().getLocation());
				event.getPlayer().sendMessage("Point 2 set");
			}else if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				CorePlayerManager.getInstance().getPlayer(event.getPlayer()).getInfo(SelectionPlayerInfo.class).setPoint1(event.getClickedBlock().getLocation());
				event.getPlayer().sendMessage("Point 1 set");
			}
		}
	}
	
	public ItemStack getTool(){
		return tool;
	}
	
}
