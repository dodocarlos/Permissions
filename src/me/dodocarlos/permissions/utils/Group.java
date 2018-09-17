package me.dodocarlos.permissions.utils;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.dodocarlos.permissions.Main;

public class Group {
	
	private String name;
	private ArrayList<String> perms = new ArrayList<>();
	private ArrayList<String> users = new ArrayList<>();
	
	public Group(String name){
		this.name = name;
		this.perms = Main.db.getGroupPerms(name);
	}
	
	public void addUser(Player p){
		this.users.add(p.getName());
	}
	
	public ArrayList<String> getPerms(){
		return this.perms;
	}
	
	public ArrayList<String> getUsers(){
		return this.users;
	}
	
	public boolean hasPerm(String perm){
		return this.perms.contains(perm);
	}
	
	public String getName(){
		return this.name;
	}
	
	public static Group getGroupByName(String name){
		for(Group gr : Vars.groups){
			if(gr.getName().equalsIgnoreCase(name)){
				return gr;
			}
		}
		return null;
	}
	
}
