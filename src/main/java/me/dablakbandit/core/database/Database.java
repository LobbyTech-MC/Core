package me.dablakbandit.core.database;

import java.sql.Connection;

import me.dablakbandit.core.database.listener.SQLListener;

public abstract class Database{
	
	public abstract Connection openConnection();
	
	public abstract Connection getConnection();
	
	public abstract boolean isConnected();
	
	public abstract void closeConnection();
	
	public abstract void addListener(SQLListener listener);
}
