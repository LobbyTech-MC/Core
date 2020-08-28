/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.core.commands.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class TabCompleter {
	
	private static Predicate<String> predicate(String with){
		return s -> s.toLowerCase().startsWith(with.toLowerCase());
	}
	
	public static TabCompleter	PLAYERS	= new TabCompleter(){
											@Override
											public List<String> onTabComplete(CommandSender s, String start, String[] args){
												return getPlayerList(start.toLowerCase());
											}
											
											private List<String> getPlayerList(String with){
												return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(TabCompleter.predicate(with)).collect(Collectors.toList());
											}
											
										};
	
	public static TabCompleter	UUIDS	= new TabCompleter(){
											@Override
											public List<String> onTabComplete(CommandSender s, String start, String[] args){
												return getUUIDList(start.toLowerCase());
											}
											
											private List<String> getUUIDList(String with){
												return Bukkit.getOnlinePlayers().stream().map(p -> p.getUniqueId().toString()).filter(TabCompleter.predicate(with)).collect(Collectors.toList());
											}
											
										};
	
	public static TabCompleter	ENTITY	= ofEnum(EntityType.class);
	public static TabCompleter	BOOLEAN	= ofValues("true", "false");
	
	public static TabCompleter ofEnum(Class<? extends Enum> e){
		return new TabCompleter(){
			@Override
			public List<String> onTabComplete(CommandSender s, String start, String[] args){
				return TabCompleter.getAllThatStartWith(e, start.toUpperCase());
			}
		};
	}
	
	public static TabCompleter ofValues(String... vals){
		return new TabCompleter(){
			@Override
			public List<String> onTabComplete(CommandSender s, String start, String[] args){
				return Arrays.stream(vals).filter(TabCompleter.predicate(start)).collect(Collectors.toList());
			}
		};
	}
	
	public static TabCompleter ofValues(Collection<String> vals){
		return new TabCompleter(){
			@Override
			public List<String> onTabComplete(CommandSender s, String start, String[] args){
				return vals.stream().filter(TabCompleter.predicate(start)).collect(Collectors.toList());
			}
		};
	}
	
	public static List<String> getAllThatStartWith(Class<? extends Enum> e, String with){
		return Arrays.stream(e.getEnumConstants()).filter(en -> en.name().startsWith(with)).map(Enum::name).collect(Collectors.toList());
	}
	
	public static List<String> getAllThatStartWith(List<String> list, String with){
		return list.stream().filter(en -> en.startsWith(with)).collect(Collectors.toList());
	}
	
	public static <T> TabCompleter mapCollection(Collection<T> col, Function<? super T, String> mapper){
		return new TabCompleter(){
			@Override
			public List<String> onTabComplete(CommandSender s, String start, String[] args){
				return col.stream().map(mapper).filter(TabCompleter.predicate(start)).collect(Collectors.toList());
			}
		};
	}
	
	public abstract List<String> onTabComplete(CommandSender s, String start, String[] args);
}
