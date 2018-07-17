package me.dablakbandit.core.utils.jsonformatter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.dablakbandit.core.json.JSONArray;
import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.utils.NMSUtils;
import me.dablakbandit.core.utils.jsonformatter.click.ClickEvent;
import me.dablakbandit.core.utils.jsonformatter.hover.HoverEvent;

public class JSONFormatter{
	
	private JSONArray		ja		= new JSONArray();
	private Builder			builder	= new Builder();
	private String			color	= "";
	private List<JSONArray>	all		= new ArrayList<JSONArray>();
	private boolean			newline	= true;
	
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
			}
			for(int i = 0; i < json.ja.length(); i++){
				add(json.ja.getJSONObject(i));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
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
		resetAll();
		return this;
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
			return a.invoke(null, toJSON());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Object> toSerializedList(){
		List<Object> list = new ArrayList<Object>();
		try{
			for(String s : toList()){
				list.add(a.invoke(null, s));
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
					builder = new Builder();
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
			return ppocc.newInstance(toSerialized());
		}catch(Exception e){
		}
		return null;
	}
	
	public Object getPacket(byte b){
		try{
			return ppoccb.newInstance(toSerialized(), b);
		}catch(Exception e){
		}
		return null;
	}
	
	public List<Object> getPacketList(){
		List<Object> list = new ArrayList<Object>();
		try{
			for(Object o : toSerializedList()){
				list.add(ppocc.newInstance(o));
			}
			return list;
		}catch(Exception e){
		}
		return null;
	}
	
	private static Class<?>			cs		= NMSUtils.getNMSClassSilent("ChatSerializer", "IChatBaseComponent"), icbc = NMSUtils.getNMSClassSilent("IChatBaseComponent"), ppoc = NMSUtils.getNMSClassSilent("PacketPlayOutChat"), pc = NMSUtils.getNMSClassSilent("PlayerConnection"),
	p = NMSUtils.getNMSClassSilent("Packet"), ep = NMSUtils.getNMSClassSilent("EntityPlayer"), ccm = NMSUtils.getOBCClass("util.CraftChatMessage");
	@SuppressWarnings("unused")
	private static Method			a		= NMSUtils.getMethodSilent(cs, "a", String.class), sp = NMSUtils.getMethodSilent(pc, "sendPacket", p), fromString = NMSUtils.getMethod(ccm, "fromString", String.class, boolean.class);
	private static Field			ppc		= NMSUtils.getFieldSilent(ep, "playerConnection");
	
	private static Class<?>			cmt		= NMSUtils.getNMSClassSilent("ChatMessageType");
	private static Constructor<?>	ppocc	= NMSUtils.getConstructorSilent(ppoc, icbc), ppoccb = NMSUtils.getConstructorSilent(ppoc, icbc, cmt == null ? byte.class : cmt);
	private static boolean			b		= check(cs, icbc, ppoc, pc, p, ep, a, sp, ppc, ppocc);
	
	public static void sendRawMessage(Player player, String message, byte b){
		try{
			Object entityplayer = NMSUtils.getHandle(player);
			Object ppco = ppc.get(entityplayer);
			JSONObject jo = new JSONObject();
			jo.put("text", message);
			String send = jo.toString();
			Object a1 = a.invoke(null, send);
			Object a = b;
			if(cmt != null){
				a = NMSUtils.getEnum(b, cmt);
			}
			sp.invoke(ppco, ppoccb.newInstance(a1, a));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static boolean check(Object... o){
		for(Object a : o){
			if(a == null)
				return false;
		}
		return true;
	}
	
	private static void send(Player player, JSONFormatter jf){
		if(!jf.newline){
			send1(player, jf);
		}else if(b){
			try{
				Object entityplayer = NMSUtils.getHandle(player);
				Object ppco = ppc.get(entityplayer);
				sp.invoke(ppco, jf.getPacket());
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
				Object ppco = ppc.get(entityplayer);
				List<Object> packets = jf.getPacketList();
				List<String> jsons = null;
				for(int i = 0; i < packets.size(); i++){
					try{
						sp.invoke(ppco, packets.get(i));
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
		
		private StringBuilder	sb		= new StringBuilder("");
		private boolean			bold	= false, italic = false, magic = false, strikethrough = false, underline = false, changed = false;
		
		public Builder(){
		}
		
		public Builder(Builder b){
			bold = b.bold;
			italic = b.italic;
			magic = b.magic;
			strikethrough = b.strikethrough;
			underline = b.underline;
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
