package me.dablakbandit.core.database.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.core.database.Database;
import me.dablakbandit.core.database.listener.SQLListener;

public class SQLiteDatabase extends Database{
	
	private JavaPlugin			plugin;
	private final String		database;
	private Connection			connection;
	private List<SQLListener>	listeners	= new ArrayList<SQLListener>();
	
	public SQLiteDatabase(String database, boolean open){
		this(null, database, open);
	}
	
	public SQLiteDatabase(JavaPlugin plugin, String database, boolean open){
		this.plugin = plugin;
		this.database = database;
		if(open){
			openConnection();
		}
	}
	
	public Connection openConnection(){
		try{
			Class.forName("org.sqlite.JDBC");
			File f = (plugin == null ? new File(database) : new File(plugin.getDataFolder(), database));
			if(!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			if(!f.exists())
				f.createNewFile();
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + f);
			setup();
		}catch(SQLException e){
			System.out.print(e.getMessage());
			closeConnection();
		}catch(ClassNotFoundException e){
			System.out.print("JDBC Driver not found!");
		}catch(Exception e){
			e.printStackTrace();
		}
		return this.connection;
	}
	
	public boolean checkConnection(){
		return this.connection != null;
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
	public void closeConnection(){
		if(this.connection != null){
			for(SQLListener listener : listeners){
				listener.close(connection);
			}
			try{
				this.connection.close();
				this.connection = null;
			}catch(SQLException e){
				System.out.print("Error closing the SQLite Connection!");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean isConnected(){
		return this.connection != null;
	}
	
	public void setup(){
		for(SQLListener listener : listeners){
			listener.setup(connection);
		}
	}
	
	@Override
	public void addListener(SQLListener listener){
		getConnection();
		listeners.add(listener);
		listener.setDatabase(this);
		listener.setup(connection);
	}
}
