package me.dablakbandit.core.database.listener;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;

import me.dablakbandit.core.database.Database;
import me.dablakbandit.core.utils.NMSUtils;

public abstract class SQLListener{
	
	protected Database database;
	
	public void setDatabase(Database database){
		this.database = database;
	}
	
	public Database getDatabase(){
		return database;
	}
	
	public void ensureConnection(){
		if(database != null){
			database.getConnection();
		}
	}
	
	public abstract void setup(Connection con);
	
	public abstract void close(Connection con);
	
	public void closeStatements(){
		try{
			for(Field f : NMSUtils.getFields(this.getClass())){
				if(f.getType().equals(PreparedStatement.class)){
					PreparedStatement ps = (PreparedStatement)f.get(this);
					if(ps != null){
						ps.close();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
