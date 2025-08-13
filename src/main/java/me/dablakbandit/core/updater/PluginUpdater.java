package me.dablakbandit.core.updater;

import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.CorePluginConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginUpdater implements Listener{
	
	private static PluginUpdater updater = new PluginUpdater();
	
	public static PluginUpdater getInstance(){
		return updater;
	}
	
	private PluginUpdater(){
		
	}
	
	private List<PluginInfo> infos = new ArrayList<>();
	
	public void start(){
		Bukkit.getPluginManager().registerEvents(this, CorePlugin.getInstance());
	    int times = CorePluginConfiguration.UPDATE_CHECK.get();
		if(times > 86400){
			times = 86400;
			CoreLog.info("Update time may not be longer than a day.");
		}else if(times < 900){
			times = 900;
			CoreLog.info("Update time may not be less then 15 minutes.");
		}
		int time = 20 * times;
		Bukkit.getScheduler().runTaskTimerAsynchronously(CorePlugin.getInstance(), () -> checkUpdates(), time, time);
		
	}
	
	private void delay(int times){
		if(times <= 0){ return; }
		long next = System.currentTimeMillis() + 1000;
		CoreLog.info("[Core] Continuing in " + times);
		while(System.currentTimeMillis() < next)
			;
		delay(times - 1);
	}

	@Deprecated
	public void checkUpdate(Plugin plugin, String id, String download, String file){
		checkUpdate(plugin, id);
	}
	
	public void checkUpdate(Plugin plugin, String id){
		PluginInfo pi = new PluginInfo(plugin, id);
		pi.checkUpdate(false);
		infos.add(pi);
	}
	
	public void checkUpdates(){
		CoreLog.info("[Core] Checking for " + infos.size() + " updates.");
		for(PluginInfo pi : infos){
			pi.checkUpdate(true);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		if(p.isOp()){
			for(PluginInfo pi : infos){
				for(String s : pi.getMessages()){
					Bukkit.getConsoleSender().sendMessage(s);
				}
			}
		}
	}
	
}
