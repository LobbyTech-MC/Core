package me.dablakbandit.core.utils.jsonformatter.hover;

import me.dablakbandit.core.json.JSONObject;

public class ShowAchievementEvent extends HoverEvent{
	
	private JSONObject object = new JSONObject();
	
	public ShowAchievementEvent(String achievement){
		try{
			object.put("action", "show_achievement");
			object.put("value", achievement);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public JSONObject getEvent(){
		return object;
	}
	
}
