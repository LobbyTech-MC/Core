package me.dablakbandit.core.config.path;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public abstract class ListPath<T>extends Path<List<T>>{
	protected ListPath(List<T> def){
		super(def);
	}
	
	@Deprecated
	protected ListPath(String old, List<T> def){
		super(old, def);
	}
	
	public boolean add(T t){
		List<T> list = this.get();
		if(list instanceof AbstractList){
			list = new ArrayList<>(list);
		}
		boolean ret = list.add(t);
		this.set(list);
		return ret;
	}
	
	public boolean remove(T t){
		List<T> list = this.get();
		boolean ret = list.remove(t);
		this.set(list);
		return ret;
	}
}
