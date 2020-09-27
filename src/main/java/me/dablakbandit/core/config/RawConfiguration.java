package me.dablakbandit.core.config;

import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RawConfiguration extends YamlConfiguration{
	
	private String	fileName;
	private Plugin	plugin;
	private File	file;
	
	public RawConfiguration(String name){
		this(null, name);
	}
	
	public RawConfiguration(Plugin plugin, String name){
		this.plugin = plugin;
		this.fileName = name.endsWith(".yml") ? name : name + ".yml";
	}
	
	protected void init(){
		removeSplitLines();
		loadFile();
		createData();
		try{
			loadConfig();
		}catch(IOException | InvalidConfigurationException e){
			e.printStackTrace();
		}
	}

	protected void removeSplitLines(){
		DumperOptions options;
		try{
			options = (DumperOptions) NMSUtils.getFirstFieldOfType(YamlConfiguration.class, DumperOptions.class).get(this);
			options.setSplitLines(false);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void loadConfig() throws FileNotFoundException, IOException, InvalidConfigurationException{
		this.load(file);
	}
	
	private File getDataFolder(){
		return this.plugin == null ? new File(".") : this.plugin.getDataFolder();
	}
	
	public File loadFile(){
		File folder = getDataFolder();
		if(!folder.exists()){
			folder.mkdirs();
		}
		return this.file = new File(folder, this.fileName);
	}
	
	public File getFile(){
		return file;
	}
	
	public void saveConfig(){
		this.file = new File(getDataFolder(), this.fileName);
		try{
			this.save(this.file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void save(File file) throws IOException{
		super.save(file);
	}
	
	public void createData(){
		if(!file.exists()){
			saveDefault();
		}
	}
	
	private void saveDefault(){
		if(this.plugin != null){
			if(this.plugin.getResource(this.fileName) == null){
				try{
					this.file.createNewFile();
				}catch(IOException e){
					e.printStackTrace();
				}
			}else{
				this.plugin.saveResource(this.fileName, false);
			}
		}
	}
	
	public void delete(){
		if(this.file.exists()){
			this.file.delete();
		}
	}
	
	public void reloadConfig(){
		try{
			loadConfig();
		}catch(IOException | InvalidConfigurationException e){
			e.printStackTrace();
		}
	}
	
}
