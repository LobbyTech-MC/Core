package me.dablakbandit.core.players.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;

public class ScoreboardInfo extends CorePlayersInfo{
	
	private Scoreboard board;
	
	public ScoreboardInfo(CorePlayers pl){
		super(pl);
		Player player = pl.getPlayer();
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		for(CorePlayers pls : CorePlayerManager.getInstance().getPlayers().values()){
			Player pla = pls.getPlayer();
			String pName = pla.getName();
			Team team = board.registerNewTeam(pName);
			team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
			team.addEntry(pName);
			Team team2 = pla.getScoreboard().getTeam(player.getName());
			if(team2 == null){
				team2 = pla.getScoreboard().registerNewTeam(player.getName());
				team2.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
				team2.addEntry(player.getName());
			}else{
				team2.addEntry(player.getName());
			}
		}
		player.setScoreboard(board);
	}
	
	public Scoreboard getScoreboard(){
		return board;
	}
	
	private static ChatColor[]	colors	= ChatColor.values();
	private Team[]				teams	= new Team[16];
	
	public void setupSideboard(String title){
		Objective obj = board.registerNewObjective("side", "dummy");
		obj.setDisplayName(title);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		for(int i = 0; i < 16; i++){
			Team t = board.registerNewTeam("Line" + i);
			String score = colors[i].toString();
			t.addEntry(score);
			obj.getScore(score).setScore(0 - i);
			teams[i] = t;
		}
	}
	
	public void set(int score, String value){
		String prefix = "";
		String suffix = "";
		if(value.length() > 16){
			prefix = value.substring(0, 16);
			suffix = ChatColor.getLastColors(prefix) + value.substring(16, Math.min(32, value.length()));
		}else{
			prefix = value;
		}
		Team t = teams[0 - score];
		t.setPrefix(prefix);
		t.setSuffix(suffix);
	}
	
	@Override
	public void load(){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void save(){
		// TODO Auto-generated method stub
		
	}
	
}
