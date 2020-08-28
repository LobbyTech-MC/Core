package me.dablakbandit.core.config.path;

import java.util.List;

public abstract class ListPath<T>extends Path<List<T>>{
	protected ListPath(List<T> def){
		super(def);
	}
	
	@Deprecated
	protected ListPath(String old, List<T> def){
		super(old, def);
	}
	
	public void add(T t){
		List<T> list = this.get();
		list.add(t);
		this.set(list);
	}
	
	public void remove(T t){
		List<T> list = this.get();
		list.remove(t);
		this.set(list);
	}
}
