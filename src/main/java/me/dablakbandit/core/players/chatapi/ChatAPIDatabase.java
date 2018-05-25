package me.dablakbandit.core.players.chatapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.database.DatabaseManager;
import me.dablakbandit.core.database.listener.SQLListener;
import me.dablakbandit.core.database.sqlite.SQLiteDatabase;

public class ChatAPIDatabase extends SQLListener{
	
	private static ChatAPIDatabase instance = new ChatAPIDatabase();
	
	public static ChatAPIDatabase getInstance(){
		return instance;
	}
	
	private SQLiteDatabase db;
	
	private ChatAPIDatabase(){
		db = DatabaseManager.getInstance().createSQLiteDatabase(CorePlugin.getInstance(), "chatapi.db", true);
		db.addListener(this);
	}
	
	private static PreparedStatement get, set;
	
	@Override
	public void setup(Connection con){
		try{
			con.createStatement().execute("CREATE TABLE IF NOT EXISTS `chat_api` (`uuid` VARCHAR(36) NOT NULL, `header` INTEGER, `text` INTEGER, PRIMARY KEY (`uuid`));");
			get = con.prepareStatement("SELECT * FROM `chat_api` WHERE `uuid` = ?;");
			set = con.prepareStatement("INSERT OR REPLACE INTO `chat_api` (`uuid`, `header`, `text`) VALUES (?,?,?);");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void load(ChatAPIInfo cai){
		try{
			get.setString(1, cai.getPlayers().getUUIDString());
			ResultSet rs = get.executeQuery();
			if(rs.next()){
				cai.setHeaderFooter(rs.getInt("header"));
				cai.setTextWidth(rs.getInt("text"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void save(ChatAPIInfo cai){
		try{
			set.setString(1, cai.getPlayers().getUUIDString());
			set.setInt(2, cai.getHeaderFooter());
			set.setInt(3, cai.getTextWidth());
			set.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void close(Connection con){
		closeStatements();
	}
	
}
