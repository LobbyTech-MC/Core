package me.dablakbandit.core.config.path;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;

import me.dablakbandit.core.config.RawConfiguration;

public class MaterialListPath extends ListPath<Material>{
	public MaterialListPath(Material... def){
		super(Arrays.asList(def));
	}
	
	public MaterialListPath(List<Material> def){
		super(def);
	}
	
	@Deprecated
	public MaterialListPath(String old, List<Material> def){
		super(old, def);
	}
	
	protected List<Material> get(RawConfiguration config, String path){
		return config.getStringList(path).stream().map(Material::getMaterial).collect(Collectors.toList());
	}
	
	protected Object setAs(List<Material> list){
		return list.stream().map(Enum::name).collect(Collectors.toList());
	}
}
