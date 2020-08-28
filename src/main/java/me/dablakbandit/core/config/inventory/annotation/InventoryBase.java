package me.dablakbandit.core.config.inventory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InventoryBase{
	//@formatter:off
	String[] value() default {
		"This config file handles all inventory sizes and titles for this plugin allowing all items to be dynamically changed",
		"",
		"Inventory Node parameters",
		"Required: Size, Title", 
		"Extra: Permission"
	};
	//@formatter:on
}
