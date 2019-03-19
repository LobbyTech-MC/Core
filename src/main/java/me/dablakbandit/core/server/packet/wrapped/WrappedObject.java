/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.server.packet.wrapped;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.itemutils.IItemUtils;

public class WrappedObject{
	
	protected Object	object;
	protected Class<?>	clazz;
	
	public WrappedObject(Object object){
		this.object = object;
		this.clazz = object.getClass();
	}
	
	public <T> List<T> getObjects(Class<T> clazz, Class<?> from){
		List<T> list = new ArrayList<>();
		try{
			for(Field f : NMSUtils.getFields(from)){
				if(f.getType().equals(clazz)){
					list.add((T)f.get(object));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public <T> List<T> getObjects(Class<T> clazz){
		List<T> list = new ArrayList<>();
		try{
			for(Field f : NMSUtils.getFields(clazz)){
				if(f.getType().equals(clazz)){
					list.add((T)f.get(object));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public <T> void write(int index, Object object, Class<T> clazz){
		int curIndex = 0;
		try{
			for(Field f : NMSUtils.getFields(this.clazz)){
				if(f.getType().equals(clazz)){
					if(curIndex == index){
						f.set(this.object, object);
						break;
					}
					curIndex++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Object getRawObject(){
		return object;
	}
	
	public void setRawObject(Object object){
		this.object = this.object;
	}
	
	public List<Enum> getEnums(){
		List<Enum> list = new ArrayList<>();
		try{
			for(Field f : NMSUtils.getFields(clazz)){
				if(f.getType().isEnum()){
					list.add((Enum)f.get(object));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public void writeEnum(int index, Enum enu){
		int curIndex = 0;
		try{
			for(Field f : NMSUtils.getFields(clazz)){
				if(f.getType().isEnum()){
					if(curIndex == index){
						f.set(object, enu);
						break;
					}
					curIndex++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Boolean> getBooleans(){
		return getObjects(Boolean.class);
	}
	
	public void writeBoolean(int index, boolean val){
		write(index, val, Boolean.class);
	}
	
	public List<Integer> getInts(){
		return getObjects(int.class);
	}
	
	public void writeInt(int index, int i){
		write(index, i, int.class);
	}
	
	public List<int[]> getIntArrays(){
		return getObjects(int[].class);
	}
	
	public void writeIntArray(int index, int[] i){
		write(index, i, int[].class);
	}
	
	public List<Byte> getBytes(){
		return getObjects(byte.class);
	}
	
	public void writeByte(int index, byte b){
		write(index, b, byte.class);
	}
	
	public List<Double> getDoubles(){
		return getObjects(double.class);
	}
	
	public void writeDouble(int index, double d){
		write(index, d, double.class);
	}
	
	public List<Float> getFloats(){
		return getObjects(float.class);
	}
	
	public void writeFloat(int index, float f){
		write(index, f, float.class);
	}
	
	public List<String> getStrings(){
		return getObjects(String.class);
	}
	
	public void writeString(int index, String s){
		write(index, s, String.class);
	}
	
	public List<UUID> getUUIDs(){
		return getObjects(UUID.class);
	}
	
	public void writeUUID(int index, UUID uuid){
		write(index, uuid, UUID.class);
	}
	
	public List<WrappedObject> getWrappedObject(Class<?> clazz){
		List<WrappedObject> list = new ArrayList<>();
		try{
			for(Field f : NMSUtils.getFields(clazz)){
				if(f.getType().equals(clazz)){
					list.add(new WrappedObject(f.get(object)));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public void writeWrapped(int index, WrappedObject object, Class<?> clazz){
		write(index, object.getRawObject(), clazz);
	}
	
	private static Class<?> classItemStack = NMSUtils.getClass("ItemStack");
	
	public List<ItemStack> getItemStacks(){
		List<ItemStack> list = new ArrayList<>();
		IItemUtils utils = ItemUtils.getInstance();
		try{
			for(Object o : getObjects(classItemStack)){
				list.add(utils.asBukkitCopy(o));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public void writeItemStack(int index, ItemStack is){
		int curIndex = 0;
		try{
			for(Field f : NMSUtils.getFields(clazz)){
				if(f.getType().equals(classItemStack)){
					if(curIndex == index){
						f.set(object, is);
						break;
					}
					curIndex++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<List<WrappedObject>> getLists(){
		List<List<WrappedObject>> lists = new ArrayList<>();
		try{
			for(Field f : NMSUtils.getFields(clazz)){
				if(List.class.isAssignableFrom(f.getType())){
					List<WrappedObject> newList = new ArrayList<WrappedObject>();
					List list = (List)f.get(object);
					for(Object o : list){
						newList.add(new WrappedObject(o));
					}
					lists.add(newList);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return lists;
	}
	
	public void writeList(int index, List<WrappedObject> objects){
		int curIndex = 0;
		try{
			for(Field f : NMSUtils.getFields(clazz)){
				if(List.class.isAssignableFrom(f.getType())){
					if(curIndex == index){
						List list = (List)f.get(object);
						list.clear();
						for(WrappedObject wrapped : objects){
							list.add(wrapped.getRawObject());
						}
						break;
					}
					curIndex++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
