package me.dablakbandit.core.config.path;

import java.util.Arrays;
import java.util.List;

import me.dablakbandit.core.config.RawConfiguration;

public class StringListPath extends ListPath<String>{
	public StringListPath(String... def){
		super(Arrays.asList(def));
	}
	
	public StringListPath(List<String> def){
		super(def);
	}
	
	@Deprecated
	public StringListPath(String old, List<String> def){
		super(old, def);
	}
	
	protected List<String> get(RawConfiguration config, String path){
		return config.getStringList(path);
	}
}
