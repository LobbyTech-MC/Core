package me.dablakbandit.core.database.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SQLPermissions extends SQLListener{
	
	private static SQLPermissions listener = new SQLPermissions();
	
	public static SQLPermissions getInstance(){
		return listener;
	}
	
	private SQLPermissions(){
	}
	
	private static PreparedStatement get_permissions, add_permission, remove_permission;
	
	@Override
	public void setup(Connection con){
		try{
			// Create the table
			con.prepareStatement("CREATE TABLE IF NOT EXISTS `permissions` (`uuid` VARCHAR(36) NOT NULL, `permission` TINYTEXT NOT NULL);").execute();
			
			// Create the preparedstatement
			get_permissions = con.prepareStatement("SELECT * FROM `permissions` WHERE `uuid` = ?;");
			add_permission = con.prepareStatement("INSERT INTO `permissions` (`uuid`, `permission`) VALUES (?,?);");
			remove_permission = con.prepareStatement("DELETE FROM `permissions` WHERE `uuid` = ? AND `permission` = ?;");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<String> getPermissions(String uuid){
		List<String> permissions = new ArrayList<String>();
		try{
			ensureConnection();
			get_permissions.setString(1, uuid);
			ResultSet rs = get_permissions.executeQuery();
			while(rs.next()){
				permissions.add(rs.getString("permission"));
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return permissions;
	}
	
	public void addPermission(String uuid, String permission){
		try{
			add_permission.setString(1, uuid);
			add_permission.setString(2, permission);
			add_permission.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void removePermission(String uuid, String permission){
		try{
			remove_permission.setString(1, uuid);
			remove_permission.setString(2, permission);
			remove_permission.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void close(Connection con){
		closeStatements();
	}
	
}
