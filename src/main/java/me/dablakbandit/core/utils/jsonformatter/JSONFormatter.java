package me.dablakbandit.core.utils.jsonformatter;

import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.json.JSONArray;
import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.jsonformatter.click.ClickEvent;
import me.dablakbandit.core.utils.jsonformatter.hover.HoverEvent;
import me.dablakbandit.core.utils.packet.types.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JSONFormatter{
	
	private JSONArray		ja		= new JSONArray();
	private Builder			builder	= new Builder();
	private String			color	= "";
	private List<JSONArray>	all		= new ArrayList<JSONArray>();
	private boolean			newline	= true;
	private int				lines	= 1;
	
	public JSONFormatter(){
	}
	
	public JSONFormatter(boolean newline){
		this.newline = newline;
	}
	
	public JSONFormatter append(JSONFormatter json){
		if(json.ja.length() == 0)
			return this;
		try{
			if(newline && json.newline){
				all.addAll(json.all);
				lines += json.lines - 1;
			}
			for(int i = 0; i < json.ja.length(); i++){
				add(json.ja.getJSONObject(i));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	public void append(JSONObject jo){
		try{
			if(jo.has("extra")){
				JSONArray ja = jo.getJSONArray("extra");
				append(fromJsonArray(ja));
			}
			if(jo.has("text")){
				append(jo.getString("text"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public JSONFormatter appendDirect(JSONObject jo){
		append("", new BuilderMaker() {
			@Override
			public JSONObject make() {
				return jo;
			}
		});
		return this;
	}
	
	public int getSize(){
		if(newline){ return 1; }
		return all.size() + 1;
	}
	
	public JSONFormatter newLine(){
		if(newline){
			append("\n");
		}else{
			all.add(ja);
			ja = new JSONArray();
		}
		lines++;
		resetAll();
		return this;
	}
	
	public int getLines(){
		return lines;
	}
	
	public JSONFormatter newLine(int amount){
		for(int i = 0; i < amount; i++)
			newLine();
		return this;
	}
	
	public void clear(){
		ja = new JSONArray();
		builder = new Builder();
		color = "";
	}
	
	public JSONFormatter resetAll(){
		return resetColors().resetModifiers();
	}
	
	public JSONFormatter resetColors(){
		color = "";
		return this;
	}
	
	public JSONFormatter resetModifiers(){
		builder = new Builder();
		return this;
	}
	
	public String toNormalString(){
		UnBuilder un = new UnBuilder();
		List<JSONObject> list = toJSONList();
		if(list != null){
			try{
				for(JSONObject jo : list){
					if(jo.has("extra")){
						JSONArray ja = jo.getJSONArray("extra");
						for(int i = 0; i < ja.length(); i++){
							un.unBuild(ja.getJSONObject(i));
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return un.toString();
	}
	
	public String toJSON(){
		JSONObject jo = new JSONObject();
		try{
			if(ja.length() > 0)
				jo.put("extra", ja);
			jo.put("text", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return jo.toString();
	}
	
	public JSONArray getJsonArray(){
		return ja;
	}
	
	public static JSONFormatter fromJsonArray(JSONArray ja){
		JSONFormatter jf = new JSONFormatter();
		jf.ja = ja;
		return jf;
	}
	
	public List<JSONObject> toJSONList(){
		List<JSONObject> list = new ArrayList<JSONObject>();
		try{
			for(JSONArray ja : all){
				JSONObject jo = new JSONObject();
				if(ja.length() > 0)
					jo.put("extra", ja);
				jo.put("text", "");
				list.add(jo);
			}
			JSONObject jo = new JSONObject();
			if(ja.length() > 0)
				jo.put("extra", ja);
			jo.put("text", "");
			list.add(jo);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> toList(){
		List<String> list = new ArrayList<String>();
		try{
			for(JSONArray ja : all){
				JSONObject jo = new JSONObject();
				if(ja.length() > 0)
					jo.put("extra", ja);
				jo.put("text", "");
				list.add(jo.toString());
			}
			JSONObject jo = new JSONObject();
			if(ja.length() > 0)
				jo.put("extra", ja);
			jo.put("text", "");
			list.add(jo.toString());
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Object toSerialized(){
		try{
			return methodChatSerializerAString.invoke(null, toJSON());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONFormatter fromSerialized(Object object){
		try{
			JSONFormatter jf = new JSONFormatter();
			jf.append(new JSONObject((String)methodChatSerializerAIChat.invoke(null, object)));
			return jf;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Object> toSerializedList(){
		List<Object> list = new ArrayList<Object>();
		try{
			for(String s : toList()){
				list.add(methodChatSerializerAString.invoke(null, s));
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONFormatter send(Player player){
		JSONFormatter.send(player, this);
		return this;
	}
	
	public JSONFormatter add(JSONObject jo){
		if(ja == null)
			ja = new JSONArray();
		if(jo != null)
			ja.put(jo);
		return this;
	}
	
	private JSONFormatter append(String text, BuilderMaker bm){
		builder = new Builder(builder);
		for(int i = 0; i < text.length(); i++){
			char c = text.charAt(i);
			switch(c){
			case '§':{
				if((i + 1) == text.length()){
					builder.append(c);
					continue;
				}
				ChatColor cc = ChatColor.getByChar(text.charAt(i + 1));
				if(cc == null){
					builder.append(c);
					break;
				}
				add(bm.make());
				switch(cc){
				case BOLD:
					builder = new Builder(builder);
					builder.bold = true;
					break;
				case ITALIC:
					builder = new Builder(builder);
					builder.italic = true;
					break;
				case MAGIC:
					builder = new Builder(builder);
					builder.magic = true;
					break;
				case RESET:
					builder = new Builder();
					color = "";
					break;
				case STRIKETHROUGH:
					builder = new Builder(builder);
					builder.strikethrough = true;
					break;
				case UNDERLINE:
					builder = new Builder(builder);
					builder.underline = true;
					break;
				default:{
					String font = builder.font;
					builder = new Builder();
					builder.font = font;
					color = cc.name().toLowerCase();
					break;
				}
				}
				i++;
				break;
			}
			default:{
				builder.append(c);
			}
			}
		}
		add(bm.make());
		return this;
	}

	public JSONFormatter defaultFont(){
		return setFont("minecraft:default");
	}

	public JSONFormatter setFont(String font){
		if(builder.font != null){
			if(builder.font.equals(font)) {
				return this;
			}
		}
		builder.font = font;
		return this;
	}
	
	public JSONFormatter append(String text){
		return append(text, new BuilderMaker(){
			@Override
			public JSONObject make(){
				return builder.toString(color);
			}
		});
	}

	
	public JSONFormatter appendHover(String text, final HoverEvent hevent){
		return append(text, new BuilderMaker(){
			@Override
			public JSONObject make(){
				return builder.toStringHover(color, hevent);
			}
		});
	}
	
	public JSONFormatter appendClick(String text, final ClickEvent cevent){
		return append(text, new BuilderMaker(){
			@Override
			public JSONObject make(){
				return builder.toStringClick(color, cevent);
			}
		});
	}
	
	public JSONFormatter appendHoverClick(String text, final HoverEvent hevent, final ClickEvent cevent){
		return append(text, new BuilderMaker(){
			@Override
			public JSONObject make(){
				return builder.toStringHoverClick(color, hevent, cevent);
			}
		});
	}
	
	public Object getPacket(){
		try{
			if(ppocc != null){
				return ppocc.newInstance(toSerialized());
			}else{
				return ppoccb.newInstance(toSerialized(), classChatMessageType.getEnumConstants()[0], null);
			}
		}catch(Exception e){
		}
		return null;
	}
	
	public List<Object> getPacketList(){
		List<Object> list = new ArrayList<Object>();
		try{
			for(Object o : toSerializedList()){
				if(ppocc != null){
					list.add(ppocc.newInstance(o));
				}else{
					list.add(ppoccb.newInstance(o, classChatMessageType.getEnumConstants()[0], null));
				}
			}
			return list;
		}catch(Exception e){
		}
		return null;
	}
	
	private static Class<?>			classChatSerializer			= NMSUtils.getNMSClassSilent("ChatSerializer", "IChatBaseComponent");
	static {
		if(classChatSerializer==null){
			classChatSerializer = NMSUtils.getInnerClassSilent(NMSUtils.getClassSilent("net.minecraft.network.chat.IChatBaseComponent"), "ChatSerializer");
		}
	}
	private static Class<?>			classIChatBaseComponent		= PacketType.getClassesNMS("IChatBaseComponent", "net.minecraft.network.chat.IChatBaseComponent", "net.minecraft.network.chat.Component");
	private static Class<?>			classPacketPlayOutChat		= getPacketPlayOutChat();

	protected static Class<?> getPacketPlayOutChat(){
		Class<?> clazz = NMSUtils.getClassSilent("net.minecraft.network.protocol.game.ClientboundPlayerChatPacket");
		if(clazz != null){
			return clazz;
		}
		clazz = PacketType.getClassNMS("net.minecraft.network.protocol.game.PacketPlayOutChat", "PacketPlayOutChat");
		if(clazz==null){
			try{
				clazz =Class.forName("net.minecraft.network.play.server.S02PacketChat");
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return clazz;
	}
	private static Class<?> 		classPlayerConnection 		= PacketType.getClassNMS("net.minecraft.server.network.PlayerConnection", "PlayerConnection");
	private static Class<?>			classPacket					= PacketType.getClassNMS("net.minecraft.network.protocol.Packet", "Packet");
	private static Class<?>			classEntityPlayer			= PacketType.getClassNMS("net.minecraft.server.level.EntityPlayer", "EntityPlayer");
	private static Class<?> 		classCraftChatMessage 		= NMSUtils.getOBCClass("util.CraftChatMessage");
	private static Class<?>			classChatMessageType		= PacketType.getClassNMS("net.minecraft.network.chat.ChatMessageType", "ChatMessageType");
	
	private static Method			methodChatSerializerAString	= NMSUtils.getMethodSilent(classChatSerializer, "a", String.class);
	private static Method			methodChatSerializerAIChat	= NMSUtils.getMethodSilent(classChatSerializer, "a", classIChatBaseComponent);
	private static Method			methodSendPacket			= NMSUtils.getMethodSilent(classPlayerConnection, new String[]{"a", "sendPacket"}, classPacket);
	private static Method			fromString					= NMSUtils.getMethod(classCraftChatMessage, "fromString", String.class, boolean.class);
	
	private static Field			fieldPlayerConnection		= NMSUtils.getFirstFieldOfTypeSilent(classEntityPlayer, classPlayerConnection);
	
	private static Constructor<?>	ppocc						= NMSUtils.getConstructorSilent(classPacketPlayOutChat, classIChatBaseComponent);
	private static Constructor<?>	ppoccb						= NMSUtils.getConstructorSilent(classPacketPlayOutChat, classIChatBaseComponent, classChatMessageType, UUID.class);
	
	private static boolean			b							= check(classChatSerializer, classIChatBaseComponent, classPacketPlayOutChat, classPlayerConnection, classPacket, classEntityPlayer, methodChatSerializerAString, methodSendPacket, fieldPlayerConnection);
	
	private static boolean check(Object... o){
		for(Object a : o){
			if(a == null){
				return false; }
		}
		return true;
	}
	
	private static void send(Player player, JSONFormatter jf){
		if(!jf.newline){
			send1(player, jf);
		}else if(b){
			try{
				Object entityplayer = NMSUtils.getHandle(player);
				Object ppco = fieldPlayerConnection.get(entityplayer);
				methodSendPacket.invoke(ppco, jf.getPacket());
			}catch(Exception e){
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jf.toJSON());
			}
		}else{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jf.toJSON());
		}
	}
	
	private static void send1(Player player, JSONFormatter jf){
		if(b){
			try{
				Object entityplayer = NMSUtils.getHandle(player);
				Object ppco = fieldPlayerConnection.get(entityplayer);
				List<Object> packets = jf.getPacketList();
				List<String> jsons = null;
				for(int i = 0; i < packets.size(); i++){
					try{
						methodSendPacket.invoke(ppco, packets.get(i));
					}catch(Exception e){
						if(jsons == null){
							jsons = jf.toList();
						}
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jsons.get(i));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			for(String json : jf.toList()){
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + json);
			}
		}
	}
	
	private class UnBuilder{
		
		private StringBuilder	sb		= new StringBuilder("");
		private boolean			bold	= false, italic = false, magic = false, strikethrough = false, underline = false, had = false;
		private ChatColor		color;

		private String 			font;
		
		public UnBuilder(){
			
		}
		
		public void unBuild(JSONObject jo) throws Exception{
			String append = "";
			boolean reset = false;
			if(jo.has("color")){
				String c = jo.getString("color");
				
				if(color == null){
					ChatColor cc = ChatColor.valueOf(c.toUpperCase());
					append = append + "§" + cc.getChar();
					color = cc;
					had = true;
				}else{
					ChatColor cc = ChatColor.valueOf(c.toUpperCase());
					if(cc != color){
						append = append + "§" + cc.getChar();
						color = cc;
					}
				}
			}else{
				color = null;
				// This actually probably shouldn't be here, color isn't reset afterwards
				if(had){
					reset = true;
					bold = false;
					italic = false;
					magic = false;
					strikethrough = false;
					underline = false;
					had = false;
				}
			}
			if(jo.has("bold") && jo.getBoolean("bold")){
				if(!bold){
					append = append + ChatColor.BOLD;
					bold = true;
				}
			}else if(bold){
				reset = true;
				bold = false;
			}
			if(jo.has("italic") && jo.getBoolean("italic")){
				if(!italic){
					append = append + ChatColor.ITALIC;
					italic = true;
				}
			}else if(italic){
				reset = true;
				italic = false;
			}
			if(jo.has("obfuscated") && jo.getBoolean("obfuscated")){
				if(!magic){
					append = append + ChatColor.MAGIC;
					magic = true;
				}
			}else if(magic){
				reset = true;
				magic = false;
			}
			if(jo.has("strikethrough") && jo.getBoolean("strikethrough")){
				if(!strikethrough){
					append = append + ChatColor.STRIKETHROUGH;
					strikethrough = true;
				}
			}else if(strikethrough){
				reset = true;
				strikethrough = false;
			}
			if(jo.has("underlined") && jo.getBoolean("underlined")){
				if(!underline){
					append = append + ChatColor.UNDERLINE;
					underline = true;
				}
			}else if(underline){
				reset = true;
				underline = false;
			}
			font = jo.optString("font", null);
			if(reset){
				sb.append(ChatColor.RESET);
				/*-
				 ???
				  if(color != null){
					sb.append(color);
				}*/
			}
			sb.append(append);
			if(jo.has("text")){
				sb.append(jo.getString("text"));
			}
		}
		
		public String toString(){
			return sb.toString();
		}
	}
	
	private class Builder{
		
		private StringBuilder	sb		= new StringBuilder();
		private boolean			bold	= false, italic = false, magic = false, strikethrough = false, underline = false, changed = false;

		private String font;
		
		public Builder(){
		}
		
		public Builder(Builder b){
			bold = b.bold;
			italic = b.italic;
			magic = b.magic;
			strikethrough = b.strikethrough;
			underline = b.underline;
			font = b.font;
		}
		
		public void append(char c){
			sb.append(c);
			changed = true;
		}
		
		private JSONObject toString(String color, BuilderHelper bh){
			String string = sb.toString();
			if(!changed)
				return null;
			if(string.length() == 0)
				return null;
			JSONObject jo = new JSONObject();
			try{
				if(!color.equals(""))
					jo.put("color", color);
				if(bold)
					jo.put("bold", true);
				if(italic)
					jo.put("italic", true);
				if(magic)
					jo.put("obfuscated", true);
				if(strikethrough)
					jo.put("strikethrough", true);
				if(underline)
					jo.put("underlined", true);
				if(font!=null)
					jo.put("font", font);
				bh.add(jo);
				jo.put("text", string);
			}catch(Exception e){
				e.printStackTrace();
			}
			return jo;
		}
		
		public JSONObject toString(String color){
			return toString(color, new BuilderHelper(){
				@Override
				public void add(JSONObject jo) throws Exception{
				}
			});
		}
		
		public JSONObject toStringHover(String color, final HoverEvent event){
			return toString(color, new BuilderHelper(){
				@Override
				public void add(JSONObject jo) throws Exception{
					if(event.getEvent().length() > 1)
						jo.put("hoverEvent", event.getEvent());
				}
			});
		}
		
		public JSONObject toStringClick(String color, final ClickEvent event){
			return toString(color, new BuilderHelper(){
				@Override
				public void add(JSONObject jo) throws Exception{
					if(event.getEvent().length() > 1)
						jo.put("clickEvent", event.getEvent());
				}
			});
		}
		
		public JSONObject toStringHoverClick(String color, final HoverEvent hevent, final ClickEvent cevent){
			return toString(color, new BuilderHelper(){
				@Override
				public void add(JSONObject jo) throws Exception{
					if(hevent.getEvent().length() > 1)
						jo.put("hoverEvent", hevent.getEvent());
					if(cevent.getEvent().length() > 1)
						jo.put("clickEvent", cevent.getEvent());
				}
			});
		}
		
	}
	
	private abstract class BuilderMaker{
		public abstract JSONObject make();
	}
	
	private abstract class BuilderHelper{
		public abstract void add(JSONObject jo) throws Exception;
	}
}
