package me.dablakbandit.core.utils;

import java.util.List;
import java.util.Random;

public class Utils{
	
	private static Random r = new Random();
	
	public static final int random(int min, int max){
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}
	
	public static final int random(Random r, int min, int max){
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(r, n));
	}
	
	public static final double random(double min, double max){
		final double n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random((int)n));
	}
	
	public static final int next(int max, int min){
		return min + (int)(r.nextDouble() * ((max - min) + 1));
	}
	
	public static final int random(int maxValue){
		if(maxValue <= 0)
			return 0;
		return r.nextInt(maxValue + 1);
	}
	
	public static final int random(Random r, int maxValue){
		if(maxValue <= 0)
			return 0;
		return r.nextInt(maxValue + 1);
	}
	
	public static final double random(double maxValue){
		return r.nextDouble() * maxValue;
	}
	
	public static final double randomDouble(){
		return r.nextDouble();
	}
	
	public static boolean randomNext(){
		return r.nextBoolean();
	}
	
	public static boolean randomNext(int i){
		return r.nextInt(i - 1) == 0;
	}
	
	public static <T> T randomElement(T[] array){
		return array[(int)(r.nextDouble() * array.length)];
	}
	
	public static <T> T randomElement(List<T> list){
		return list.get((int)(r.nextDouble() * list.size()));
	}
}
