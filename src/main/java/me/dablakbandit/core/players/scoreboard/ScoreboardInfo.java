package me.dablakbandit.core.players.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;

public class ScoreboardInfo extends CorePlayersInfo{

	private static ScoreboardManager scoreboardManager 			= Bukkit.getScoreboardManager();
	private static String[]					colors				= new String[15];

	static{
		for(int i = 0; i < colors.length; i++){
			String s = ChatColor.values()[i].toString();
			colors[i] = s + s + s + s + s + s + s + ChatColor.RESET;
		}
	}

	private Team[]				teams	= new Team[15];
	private Scoreboard board;
	
	public ScoreboardInfo(CorePlayers pl){
		super(pl);
		setup();
	}

	private void setup(){
		Player player = pl.getPlayer();
		board = scoreboardManager.getNewScoreboard();
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

		for(int i = 0; i < colors.length; i++) {
			String score = colors[i];
			Team t = board.registerNewTeam("scoreboard-" + i);
			t.addEntry(score);
			teams[i] = t;
		}
	}
	
	public Scoreboard getScoreboard(){
		return board;
	}

	
	public void setupSideboard(String title){
		Objective obj = board.getObjective("side");
		if(obj == null){
			obj = board.registerNewObjective("side", "dummy");
		}
		obj.setDisplayName(title);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		for(int i = 0; i < colors.length; i++){
			Team t = teams[i];
			String score = colors[i];
			obj.getScore(score).setScore(-i);
			teams[i] = t;
		}
	}

	public void updateName(String entry, String teamName, String prefix, String suffix){
		Team team = getTeam(teamName);
		if(team == null){
			team = createTeam(teamName);
		}
		if(prefix != null) {
			team.setPrefix(prefix);
		}
		if(suffix != null) {
			team.setSuffix(suffix);
		}
		if(entry!=null&& !team.hasEntry(entry)) {
			team.addEntry(entry);
		}
	}

	public Team getTeam(String teamName){
		return board.getTeam(teamName);
	}

	public Team createTeam(String teamName){
		Team team = board.registerNewTeam(teamName);
		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		team.setCanSeeFriendlyInvisibles(false);
		return team;
	}

	public void setTeamColor(String teamName, ChatColor color){
		Team team = getTeam(teamName);
		team.setColor(color);
	}
	
	public void set(int score, String value){
		Team team = teams[-score];
		team.setPrefix(value);
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
