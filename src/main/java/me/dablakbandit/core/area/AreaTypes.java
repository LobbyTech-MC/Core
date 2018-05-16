package me.dablakbandit.core.area;

public enum AreaTypes{
	CUBOID(CuboidArea.class), RADIUS(RadiusArea.class), RECTANGLE(RectangleArea.class), SPEHERE(SphereArea.class);
	
	private Class<? extends Area> clazz;
	
	AreaTypes(Class<? extends Area> clazz){
		this.clazz = clazz;
	}
	
	public Class<? extends Area> getAreaClass(){
		return clazz;
	}
}
