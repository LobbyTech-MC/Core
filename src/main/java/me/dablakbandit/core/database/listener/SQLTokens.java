package me.dablakbandit.core.database.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLTokens extends SQLListener{
	
	private static SQLTokens listener = new SQLTokens();
	
	public static SQLTokens getInstance(){
		return listener;
	}
	
	private SQLTokens(){
	}
	
	private static PreparedStatement get_tokens, set_tokens, add_tokens;
	
	@Override
	public void setup(Connection con){
		try{
			ensureConnection();
			con.createStatement().execute("CREATE TABLE IF NOT EXISTS `tokens` (`uuid` VARCHAR(36) NOT NULL, `tokens` INT NOT NULL DEFAULT '0', PRIMARY KEY (`uuid`));");
			get_tokens = con.prepareStatement("SELECT * FROM `tokens` WHERE `uuid` = ?;");
			set_tokens = con.prepareStatement("INSERT INTO `tokens` (`uuid`, `tokens`) VALUES (?,?) ON DUPLICATE KEY UPDATE `tokens` = ?;");
			add_tokens = con.prepareStatement("INSERT INTO `tokens` (`uuid`, `tokens`) VALUES (?,?) ON DUPLICATE KEY UPDATE `tokens` = (`tokens` + ?);");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int getTokens(String uuid){
		int tokens = 0;
		try{
			ensureConnection();
			get_tokens.setString(1, uuid);
			ResultSet rs = get_tokens.executeQuery();
			if(rs.next()){
				tokens = rs.getInt("tokens");
			}else{
				setTokens(uuid, 0);
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return tokens;
	}
	
	public void takeTokens(String uuid, int i){
		addTokens(uuid, -i);
	}
	
	public void addTokens(String uuid, int i){
		try{
			ensureConnection();
			add_tokens.setString(1, uuid);
			add_tokens.setInt(2, i);
			add_tokens.setInt(3, i);
			add_tokens.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setTokens(String uuid, int i){
		try{
			ensureConnection();
			set_tokens.setString(1, uuid);
			set_tokens.setInt(2, i);
			set_tokens.setInt(3, i);
			set_tokens.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void close(Connection con){
		closeStatements();
	}
	
}
