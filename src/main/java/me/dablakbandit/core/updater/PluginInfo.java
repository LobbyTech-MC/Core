/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import me.dablakbandit.core.CoreLog;

public class PluginInfo{
	
	private Plugin			plugin;
	public String			id, latest, download, file;
	private List<String>	messages	= new ArrayList<>();
	
	public PluginInfo(Plugin plugin, String id){
		this.plugin = plugin;
		this.id = id;
		this.latest = plugin.getDescription().getVersion();
	}
	
	public void setDownloadAndFile(String download, String file){
		this.download = download;
		this.file = file;
	}
	
	public String getLatest(){
		return latest;
	}
	
	public int getLatestVersion(){
		return Integer.parseInt(latest.replace(".", ""));
	}
	
	public List<String> getMessages(){
		return messages;
	}
	
	public void checkUpdate(boolean print){
		String current_version = getLatest();
		CoreLog.info("[Core] Checking update for " + plugin.getName() + " v" + current_version);
		try{
			URL checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + id);
			URLConnection con = checkURL.openConnection();
			con.setConnectTimeout(2000);
			String new_version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
			if(new_version.contains("[")){
				new_version = new_version.substring(0, new_version.indexOf('['));
			}
			int new_version_number = Integer.parseInt(new_version.replace(".", ""));
			if(new_version_number > getLatestVersion()){
				latest = new_version;
				if(download()){
					addMessage("[Core] " + ChatColor.GREEN + "Plugin " + plugin.getName() + " has been updated to v" + new_version + ", please restart your server.", print);
				}else{
					addMessage("[Core] " + ChatColor.GREEN + "Plugin " + plugin.getName() + " has an update! Old: v" + current_version + ", New: v" + new_version, print);
				}
			}
		}catch(Exception e){
			System.out.print("[Core] Unable to check update for " + plugin.getName() + " v" + current_version);
		}
	}
	
	private void addMessage(String message, boolean print){
		if(print){
			Bukkit.getConsoleSender().sendMessage(message);
		}
		messages.add(message);
	}
	
	public boolean download(){
		if(download == null || file == null){ return false; }
		try{
			ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(download).openStream());
			File folder = new File("plugins/update/");
			if(!folder.exists()){
				folder.mkdirs();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(new File(folder, file));
			FileChannel fileChannel = fileOutputStream.getChannel();
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
