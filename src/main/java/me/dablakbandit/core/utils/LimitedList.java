package me.dablakbandit.core.utils;

import java.util.ArrayList;

public class LimitedList<T>extends ArrayList<T>{
	
	private static final long	serialVersionUID	= 6607056949463639272L;
	private int					size;
	
	public LimitedList(int size){
		this.size = size;
	}
	
	@Override
	public boolean add(T e){
		while(size() >= size){
			super.remove(0);
		}
		return super.add(e);
	}
	
	@Override
	public void add(int i, T item){
		while(this.size() >= size){
			super.remove(0);
		}
		super.add(i, item);
	}
}
