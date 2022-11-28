package me.dablakbandit.core.config.comment;

import me.dablakbandit.core.CoreLog;
import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.comment.annotation.Delete;
import me.dablakbandit.core.config.comment.annotation.DeleteArray;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class CommentConfiguration extends RawConfiguration{
	
	private static Map<Class<?>, Function<Object, String[]>> commentSupplierMap = new HashMap<>();
	private static Map<Class<?>, Function<Object, String[]>> deleteSupplierMap = new HashMap<>();

	static{
		addCommentSupplier(Comment.class, (c) -> c.value().split("\n"));
		addCommentSupplier(CommentArray.class, CommentArray::value);

		addDeleteSupplier(Delete.class, (c) -> c.value().split("\n"));
		addDeleteSupplier(DeleteArray.class, DeleteArray::value);
	}

	@Deprecated
	public static <T> void addCommandSupplier(Class<T> clazz, Function<T, String[]> function){
		commentSupplierMap.put(clazz, (Function<Object, String[]>)function);
	}
	
	public static <T> void addCommentSupplier(Class<T> clazz, Function<T, String[]> function){
		commentSupplierMap.put(clazz, (Function<Object, String[]>)function);
	}

	public static <T> void addDeleteSupplier(Class<T> clazz, Function<T, String[]> function){
		deleteSupplierMap.put(clazz, (Function<Object, String[]>)function);
	}
	
	private final Map<String, String[]>	comments;
	private boolean						error	= false;
	
	public CommentConfiguration(Plugin plugin, String name){
		super(plugin, name);
		this.comments = new HashMap<>();
		init();
	}
	
	@Override
	public String saveToString(){
		String contents = super.saveToString();
		Map<String, String> alphabetical = new LinkedHashMap();
		
		String[] lines = contents.split("\n");
		
		int currentLayer = 0;
		StringBuilder currentPath = new StringBuilder();
		
		StringBuilder sb = new StringBuilder();
		
		int lineNumber = 0;
		for(String line : lines){
			String trimmed = line.trim();
			if(trimmed.startsWith("#")){
				continue;
			}
			if(trimmed.startsWith("-")){
				alphabetical.computeIfPresent(currentPath.toString(), (k, v) -> v += line + '\n');
				continue;
			}
			
			if(!line.isEmpty()){
				if(line.contains(":")){
					int layerFromLine = getLayerFromLine(line, lineNumber);
					if(layerFromLine <= currentLayer){
						String append = regressPathBy(currentLayer - layerFromLine + 1, currentPath.toString());
						currentPath.setLength(0);
						currentPath.append(append);
					}
					
					String key = getKeyFromLine(line);
					
					if(layerFromLine == 0){
						currentPath.setLength(0);
					}else{
						currentPath.append(".");
					}
					currentPath.append(key);
					
					String path = currentPath.toString();
					if(comments.containsKey(path)){
						for(String comment : comments.get(path)){
							createComment(layerFromLine, sb, comment);
						}
					}
					currentLayer = layerFromLine;
				}
			}
			sb.append(line);
			sb.append('\n');
			if(!currentPath.toString().isEmpty()){
				alphabetical.put(currentPath.toString(), sb.toString());
			}
			sb.setLength(0);
		}
		sb.setLength(0);
		alphabetical.forEach((k, v) -> sb.append(v));
		alphabetical = null;
		return sb.toString();
	}
	
	@Override
	public void save(File file) throws IOException{
		if(!error){
			super.save(file);
		}
	}
	
	private void createComment(int layer, StringBuilder builder, String raw){
		for(int i = 0; i < layer * 2; i++){
			builder.append(' ');
		}
		if(!raw.startsWith("#")){
			builder.append("# ");
		}
		builder.append(raw);
		builder.append('\n');
	}
	
	@Override
	public void loadFromString(String contents) throws InvalidConfigurationException{
		try{
			super.loadFromString(contents);
		}catch(InvalidConfigurationException exception){
			if(!error){
				CoreLog.error(exception.getMessage());
				error = true;
			}
			return;
		}
		
		List<String> list = new ArrayList<>();
		Collections.addAll(list, contents.split("\n"));
		
		int currentLayer = 0;
		StringBuilder currentPath = new StringBuilder();
		List<String> comment = new ArrayList<>();
		
		int lineNumber = 0;
		for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); lineNumber++){
			String line = iterator.next();
			
			String trimmed = line.trim();
			
			if(trimmed.startsWith("#") || trimmed.isEmpty()){
				comment.add(trimmed);
				continue;
			}
			if(trimmed.startsWith("-")){
				continue;
			}
			
			if(!line.isEmpty()){
				if(line.contains(":")){
					
					int layerFromLine = getLayerFromLine(line, lineNumber);
					
					if(layerFromLine <= currentLayer){
						String append = regressPathBy(currentLayer - layerFromLine + 1, currentPath.toString());
						currentPath.setLength(0);
						currentPath.append(append);
					}
					
					String key = getKeyFromLine(line);
					
					if(layerFromLine == 0){
						currentPath.setLength(0);
					}else{
						currentPath.append(".");
					}
					currentPath.append(key);
					
					if(comment.size() > 0){
						String path = currentPath.toString();
						comments.put(path, comment.toArray(new String[0]));
						comment.clear();
					}
					currentLayer = layerFromLine;
				}
			}
		}
	}

	public boolean deletePaths(String path, String... delete){
		return deletePaths(path, () -> delete);
	}

	public boolean deletePaths(String path, Supplier<String[]> lines){
		return Arrays.stream(lines.get()).filter(line -> {
			String checkPath = path + "." + line;
			if(isSet(checkPath)){
				set(checkPath, null);
				return true;
			}
			return false;
		}).count() > 0;
	}

	
	public boolean setComment(String path, String... lines){
		return setComment(path, () -> lines);
	}
	
	public boolean setComment(String path, Supplier<String[]> lines){
		String[] newComment = Arrays.stream(lines.get()).map(line -> {
			return line.startsWith("# ") ? line : ("# " + line).trim();
		}).toArray(String[]::new);
		return !Arrays.equals(newComment, comments.put(path, newComment));
	}
	
	private String getKeyFromLine(String line){
		String key = null;
		for(int i = 0; i < line.length(); i++){
			if(line.charAt(i) == ':'){
				key = line.substring(0, i);
				break;
			}
		}
		return key == null ? null : key.trim();
	}
	
	private String regressPathBy(int i, String currentPath){
		if(i <= 0){ return currentPath; }
		List<String> rebuild = Arrays.asList(currentPath.split("\\."));
		rebuild = rebuild.subList(0, rebuild.size() - i);
		return String.join(".", rebuild);
	}
	
	private int getLayerFromLine(String line, int lineNumber){
		double d = 0;
		for(int i = 0; i < line.length(); i++){
			if(line.charAt(i) == ' '){
				d += 0.5;
			}else{
				break;
			}
		}
		return (int)d;
	}
	
	protected boolean parseAnnotations(String path, Annotation[] annotations){
		if(annotations.length == 0){ return false; }
		long count = Arrays.stream(annotations).filter(annotation -> {
			for (Map.Entry<Class<?>, Function<Object, String[]>> entry : commentSupplierMap.entrySet()) {
				if (entry.getKey().isInstance(annotation)) {
					return setComment(path, entry.getValue().apply(annotation));
				}
			}
			return false;
		}).count();
		count += Arrays.stream(annotations).filter(annotation -> {
			for (Map.Entry<Class<?>, Function<Object, String[]>> entry : deleteSupplierMap.entrySet()) {
				if (entry.getKey().isInstance(annotation)) {
					return deletePaths(path, entry.getValue().apply(annotation));
				}
			}
			return false;
		}).count();
		return count > 0;
	}
	
}