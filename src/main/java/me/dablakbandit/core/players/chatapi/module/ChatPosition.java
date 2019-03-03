package me.dablakbandit.core.players.chatapi.module;

import java.util.Comparator;

public enum ChatPosition{
	HEADER, CENTRE, FOOTER;
	
	public static PositionComparator comparator = new PositionComparator();
	
	static class PositionComparator implements Comparator<ChatModule>{
		@Override
		public int compare(ChatModule o1, ChatModule o2){
			return o1.getPosition().ordinal() - o2.getPosition().ordinal();
		}
	}
}
