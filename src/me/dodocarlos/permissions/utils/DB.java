package me.dodocarlos.permissions.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.entity.Player;

public class DB {
	
	public Connection conn;
	public Statement stm;
	
	public DB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		    String url = "jdbc:mysql://" + Vars.server + "/" + Vars.db + "?autoReconnect=true";
		    
		    conn = DriverManager.getConnection(url, Vars.user, Vars.pass);

			stm = conn.createStatement();

			stm.executeUpdate("CREATE TABLE IF NOT EXISTS usergroup(id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(500), groupname varchar(500), PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");
			stm.executeUpdate("CREATE TABLE IF NOT EXISTS userperms(id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(500), perm varchar(500), PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");
			stm.executeUpdate("CREATE TABLE IF NOT EXISTS groups(id int(11) NOT NULL AUTO_INCREMENT, name varchar(500), perm varchar(500), PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void registerPlayer(Player p){
		try{
			String uuid = p.getUniqueId().toString();
			if(!hasPlayerData(p)){
				stm.executeUpdate("INSERT INTO userperms(uuid, perm) values('" + uuid + "', 'null')");
				stm.executeUpdate("INSERT INTO usergroup(uuid, groupname) values('" + uuid + "', 'default')");
			}
		}catch(SQLException e){
		}
	}
	
	public boolean hasPlayerData(Player p){
		try {
			String uuid = p.getUniqueId().toString();
			PreparedStatement query = conn.prepareStatement("SELECT * FROM userperms where uuid = ?");
			query.setString(1, uuid);
			ResultSet rs = query.executeQuery();
			return rs.next();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<String> getPlayerPerms(Player p){
		String uuid = p.getUniqueId().toString();
		ArrayList<String> perms = new ArrayList<> ();
		try{
			if(!hasPlayerData(p)){
				registerPlayer(p);
			}
			ResultSet rs = stm.executeQuery("SELECT perm from userperms where uuid = '" + uuid + "'");			
			while(rs.next()){
				perms.add(rs.getString(1));
			}
		}catch(SQLException e){
			registerPlayer(p);
		}
		return perms;
	}
	
	public String getPlayerGroup(Player p){
		String uuid = p.getUniqueId().toString();
		String group = "default";
		try{
			if(!hasPlayerData(p)){
				registerPlayer(p);
			}
			ResultSet rs = stm.executeQuery("SELECT groupname from usergroup where uuid = '" + uuid + "'");			
			while(rs.next()){
				group = rs.getString(1);
			}
		}catch(SQLException e){
			registerPlayer(p);
		}
		return group;
	}
	
	public ArrayList<String> getGroupPerms(String name){
		ArrayList<String> perms = new ArrayList<> ();
		try{
			ResultSet rs = stm.executeQuery("SELECT perm from groups where name = '" + name + "'");			
			while(rs.next()){
				perms.add(rs.getString(1));
			}
		}catch(SQLException e){
		}
		return perms;
	}
	
	public ArrayList<String> getGroups(){
		ArrayList<String> groups = new ArrayList<> ();
		String gname = "";
		try{
			ResultSet rs = stm.executeQuery("SELECT name from groups");			
			while(rs.next()){
				gname = rs.getString(1);
				if(!groups.contains(gname)){
					groups.add(gname);
				}
			}
			if(groups.size() == 0){
				try {
					stm.executeUpdate("INSERT INTO groups(name, perm) values('default', 'null');");
				}catch(Exception ex){}
				rs = stm.executeQuery("SELECT name from groups");			
				while(rs.next()){
					gname = rs.getString(1);
					if(!groups.contains(gname)){
						groups.add(gname);
					}
				}
			}
		}catch(SQLException e){
			try {
				stm.executeUpdate("INSERT INTO groups(name, perm) values('default', 'null');");
			}catch(Exception ex){}
		}
		return groups;
	}
	
	public void addPlayerPerm(Player p, String perm){
		String uuid = p.getUniqueId().toString();
		try{
			if(!hasPlayerData(p)){
				registerPlayer(p);
			}
			stm.executeUpdate("INSERT INTO userperms(uuid, perm) values('" + uuid + "', '" + perm + "')");
		}catch(SQLException e){
			registerPlayer(p);
		}
	}
	
	public void removeGroupPerm(String name, String perm){
		try{
			stm.executeUpdate("DELETE FROM groups WHERE name = '" + name + "' AND perm = '" + perm + "';");
		}catch(SQLException e){
		}
	}
	
	public void removePlayerPerm(Player p, String perm){
		String uuid = p.getUniqueId().toString();
		try{
			stm.executeUpdate("DELETE FROM userperms WHERE uuid = '" + uuid + "' AND perm = '" + perm + "';");
		}catch(SQLException e){
		}
	}
	
	public void addGroupPerm(String name, String perm){
		try{
			stm.executeUpdate("INSERT INTO groups(name, perm) values('" + name + "', '" + perm + "')");
		}catch(SQLException e){
		}
	}
	
	public void setPlayerGroup(Player p, String group){
		String uuid = p.getUniqueId().toString();
		try{
			if(!hasPlayerData(p)){
				registerPlayer(p);
			}
			stm.executeUpdate("UPDATE usergroup set groupname = '" + group + "' WHERE uuid = '" + uuid + "';");
		}catch(SQLException e){
			registerPlayer(p);
		}
	}
	
}
