package me.dablakbandit.core.vault;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class Eco{
	
	public static Eco	manager	= new Eco();
	private Economy		economy;
	
	private Eco(){
		
	}
	
	public static Eco getInstance(){
		return manager;
	}
	
	private void initEconomy(){
		try{
			RegisteredServiceProvider<Economy> rsp1 = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
			economy = rsp1.getProvider();
		}catch(Exception a){
			a.printStackTrace();
		}
	}
	
	public Economy getEconomy(){
		if(economy == null){
			initEconomy();
		}
		return economy;
	}
}
