/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.server.packet.wrapped;

import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.itemutils.IItemUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;

public class WrappedObject{

	protected static Map<Class<?>, List<Field>> upperFieldMap = Collections.synchronizedMap(new HashMap<>());
	protected Object	object;
	protected Class<?>	clazz;
	
	public WrappedObject(Object object){
		this.object = object;
		this.clazz = object.getClass();
	}

	private synchronized List<Field> getUpperFields() throws Exception {
		return upperFieldMap.computeIfAbsent(clazz, (c) -> {
			try {
				return NMSUtils.getFieldsIncludingUpper(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	public List<Object> getObjects(){
		List list = new ArrayList<>();
		try{
			for(Field f : getUpperFields()){
				list.add(f.get(object));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public void writeObject(int index, Object object){
		int curIndex = 0;
		try{
			for(Field f : getUpperFields()){
				if(curIndex == index){
					f.set(this.object, object);
					break;
				}
				curIndex++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public <T> List<T> getObjects(Class<T> type, Class<?> from){
		List<T> list = new ArrayList<>();
		try{
			for(Field f : NMSUtils.getFields(from)){
				if(f.getType().equals(type)){
					list.add((T)f.get(object));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public <T> List<T> getObjects(Class<T> type){
		List<T> list = new ArrayList<>();
		try{
			for(Field f : getUpperFields()){
				if(f.getType().equals(type)){
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
			for(Field f : getUpperFields()){
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
			for(Field f : getUpperFields()){
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
			for(Field f : getUpperFields()){
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
		return getObjects(boolean.class);
	}
	
	public void writeBoolean(int index, boolean val){
		write(index, val, boolean.class);
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
			for(Field f : getUpperFields()){
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
	
	private static Class<?> classItemStack = NMSUtils.getNMSClass("ItemStack");
	
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
			for(Field f : getUpperFields()){
				if(f.getType().equals(classItemStack)){
					if(curIndex == index){
						f.set(object, ItemUtils.getInstance().getNMSCopy(is));
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
			for(Field f : getUpperFields()){
				if(List.class.isAssignableFrom(f.getType())){
					List<WrappedObject> newList = new ArrayList<WrappedObject>();
					List list = (List)f.get(object);
					if(list != null){
						for(Object o : list){
							newList.add(new WrappedObject(o));
						}
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
			for(Field f : getUpperFields()){
				if(List.class.isAssignableFrom(f.getType())){
					if(curIndex == index){
						List list = (List)f.get(object);
						if(list == null){
							list = new ArrayList();
							f.set(object, list);
						}
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
	
	public Map<Object, WrappedObject> getMapValues(int index){
		int curIndex = 0;
		Map<Object, WrappedObject> map = new HashMap<>();
		try{
			for(Field f : getUpperFields()){
				if(Map.class.isAssignableFrom(f.getType())){
					if(curIndex == index){
						Map value = (Map)f.get(object);
						value.forEach((key, value1) -> map.put(key, new WrappedObject(value1)));
						break;
					}
					curIndex++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	public void writeMapValue(int index, Object key, WrappedObject value){
		int curIndex = 0;
		try{
			for(Field f : getUpperFields()){
				if(Map.class.isAssignableFrom(f.getType())){
					if(curIndex == index){
						Map map = (Map)f.get(object);
						map.put(key, value.getRawObject());
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
