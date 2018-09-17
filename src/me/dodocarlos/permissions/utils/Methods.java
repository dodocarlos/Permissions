package me.dodocarlos.permissions.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.dodocarlos.permissions.Main;

public class Methods {
	
	public static String toColoredString(String s){
		return s.replaceAll("&", "§");
	}
	
	@SuppressWarnings("deprecation")
	public static void setupGroups(){
		Vars.groups.clear();
		ArrayList<String> groups = Main.db.getGroups();
		for(String name : groups){
			Vars.groups.add(new Group(name));
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			refreshPlayer(p);
		}
	}
	
	public static void refreshGroups(){
		Vars.groups.clear();
		setupGroups();
	}
	
	public static void refreshPlayer(Player p){
		String g = Main.db.getPlayerGroup(p);
		Group pGroup = Group.getGroupByName(g);
		ArrayList<String> perms = Main.db.getPlayerPerms(p);
		for(String perm : pGroup.getPerms()){
			perms.add(perm);
		}
		
//		if(!Vars.playerAttachments.containsKey(p.getName())){
		
		for(PermissionAttachmentInfo pi : p.getEffectivePermissions()){
			if(pi.getAttachment() != null){
				for(String pr : pi.getAttachment().getPermissions().keySet()){
					if(pr != null){
						pi.getAttachment().unsetPermission(pr);
					}
				}
			}
		}
		
		PermissionAttachment attach = p.addAttachment(Vars.main);
		for(String perm : perms){
			attach.setPermission(perm, true);
		}
//			Vars.playerAttachments.put(p.getName(), attach);
//		}
	}
	
}
