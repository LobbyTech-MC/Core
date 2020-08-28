package me.dablakbandit.core.config.item.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ItemBase{
	//@formatter:off
	String[] value() default {
		"This config file handles all gui items for this plugin allowing all items to be dynamically changed",
		"",
		"Item Node parameters",
		"Required: Material, Durability, Amount",
	};
	//@formatter:on
}
