package me.dablakbandit.core.utils.string;

import java.util.List;
import java.util.stream.Collectors;

public class StringListReplacer{
	
	private List<String> source;
	
	public StringListReplacer(List<String> source){
		this.source = source;
	}
	
	public StringListReplacer replace(String match, String replace){
		return new StringListReplacer(source.stream().map(s -> s.replaceAll(match, replace)).collect(Collectors.toList()));
	}
	
	public List<String> get(){
		return source;
	}
	
	public String[] toArray(){
		return source.toArray(new String[0]);
	}
}
