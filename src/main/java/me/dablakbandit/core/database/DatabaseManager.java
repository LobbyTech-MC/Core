package me.dablakbandit.core.database;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.core.CorePlugin;
import me.dablakbandit.core.configuration.Configuration;
import me.dablakbandit.core.database.mysql.MySQLConfiguration;
import me.dablakbandit.core.database.mysql.MySQLDatabase;
import me.dablakbandit.core.database.sqlite.SQLiteDatabase;

public class DatabaseManager{
	
	private static DatabaseManager manager = new DatabaseManager();
	
	public static DatabaseManager getInstance(){
		return manager;
	}
	
	private MySQLDatabase	mysql;
	private List<Database>	databases	= new ArrayList<Database>();
	
	private DatabaseManager(){
		
	}
	
	public void enableCoreDatabase(){
		if(mysql == null){
			mysql = createMySQLDatabase(new MySQLConfiguration(new Configuration(CorePlugin.getInstance(), "mysql.yml")));
		}
	}
	
	public MySQLDatabase getCoreDatabase(){
		return mysql;
	}
	
	public MySQLDatabase createMySQLDatabase(MySQLConfiguration config){
		return createMySQLDatabase(config, true);
	}
	
	public MySQLDatabase createMySQLDatabase(MySQLConfiguration config, boolean open){
		MySQLDatabase database = new MySQLDatabase(config, open);
		databases.add(database);
		return database;
	}
	
	public SQLiteDatabase createSQLiteDatabase(JavaPlugin plugin, String file){
		return createSQLiteDatabase(plugin, file, true);
	}
	
	public SQLiteDatabase createSQLiteDatabase(JavaPlugin plugin, String file, boolean open){
		SQLiteDatabase database = new SQLiteDatabase(plugin, file, open);
		databases.add(database);
		return database;
	}
	
	public void close(){
		for(Database database : databases){
			database.closeConnection();
		}
	}
	
}
