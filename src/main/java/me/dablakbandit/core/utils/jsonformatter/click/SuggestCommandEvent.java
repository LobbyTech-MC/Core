package me.dablakbandit.core.utils.jsonformatter.click;

import me.dablakbandit.core.json.JSONObject;

public class SuggestCommandEvent extends ClickEvent{
	
	private JSONObject object = new JSONObject();
	
	public SuggestCommandEvent(String suggest){
		if(!suggest.startsWith("/"))
			suggest = "/" + suggest;
		try{
			object.put("action", "suggest_command");
			object.put("value", suggest);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public JSONObject getEvent(){
		return object;
	}
	
}
