package me.dablakbandit.core.database.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import me.dablakbandit.core.database.DatabaseManager;

@SuppressWarnings("unused")
public class SQLListenerExample extends SQLListener{
	
	// This is an example class for adding a listener to the sql system
	
	// Create one instance of this class
	private static SQLListenerExample listener = new SQLListenerExample();
	
	public static SQLListenerExample getInstance(){
		return listener;
	}
	
	static{
		// Add this class to the main mysql connection
		// This is best on plugin load
		DatabaseManager.getInstance().getCoreDatabase().addListener(SQLListenerExample.getInstance());
		
		// Accessing methods from the listener
		int coins = SQLListenerExample.getInstance().getCoins("<uuid>");
	}
	
	private SQLListenerExample(){
	}
	
	private static PreparedStatement get_coins;
	
	@Override
	public void setup(Connection con){
		try{
			// Create the table
			con.prepareStatement("CREATE TABLE IF NOT EXISTS `coins` (`uuid` VARCHAR(36) NOT NULL, `coins` INT NOT NULL, PRIMARY KEY (`uuid`));").execute();
			
			// Create the preparedstatement
			get_coins = con.prepareStatement("SELECT * FROM `coins` WHERE `uuid` = ?;");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int getCoins(String uuid){
		int coins = 0;
		try{
			// This is required to make sure the connection is still valid
			ensureConnection();
			get_coins.setString(1, uuid);
			ResultSet rs = get_coins.executeQuery();
			if(rs.next()){
				coins = rs.getInt("coins");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return coins;
	}
	
	@Override
	public void close(Connection con){
		// Will close all preparedstatements from this class
		closeStatements();
	}
	
}
