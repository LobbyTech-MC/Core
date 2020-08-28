package me.dablakbandit.core.inventory;

public class InventoryDescriptor{
	
	private int		size;
	private String	title;
	private String	permission;
	
	public InventoryDescriptor(int size, String title){
		this(size, title, null);
	}
	
	public InventoryDescriptor(int size, String title, String permission){
		this.size = size;
		this.title = title;
		this.permission = permission;
		fixSize();
	}
	
	private void fixSize(){
		if(this.size % 9 == 0){ return; }
		int lines = Math.max(1, Math.min(6, this.size % 9));
		this.size = lines * 9;
	}
	
	public int getSize(){
		return size;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setSize(int size){
		this.size = Math.min(54, Math.max(9, size));
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getPermission(){
		return permission;
	}
	
	public void setPermission(String permission){
		this.permission = permission;
	}
	
	public boolean hasPermission(){
		return this.permission != null;
	}
}
