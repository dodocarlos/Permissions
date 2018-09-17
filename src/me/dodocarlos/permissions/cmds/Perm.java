package me.dodocarlos.permissions.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dodocarlos.permissions.Main;
import me.dodocarlos.permissions.utils.Methods;
import me.dodocarlos.permissions.utils.Vars;

public class Perm implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("cmd.perm") || sender.isOp()){
			if(args.length < 3){
				sender.sendMessage(Methods.toColoredString(Vars.tag + Vars.defaultColor + "Use: " + Vars.infoColor 
						+ "/perm <group|user> <add|remove> <nome|player> <perm>" + Vars.defaultColor 
						+ " ou " + Vars.infoColor + "/perm user <nick> group set <grupo>"));
			}else{
				if(args[0].equalsIgnoreCase("group")){
					if(args[1].equalsIgnoreCase("add")){
						String name = args[2];
						String perm = args[3];
						Main.db.addGroupPerm(name, perm);
						Methods.refreshGroups();
						sender.sendMessage(Methods.toColoredString(Vars.tag + Vars.defaultColor + "Permissao " + Vars.infoColor + perm
								+ Vars.defaultColor + " adcionada ao grupo " + Vars.infoColor + name));
					}
					if(args[1].equalsIgnoreCase("remove")){
						String name = args[2];
						String perm = args[3];
						Main.db.removeGroupPerm(name, perm);
						Methods.refreshGroups();
						sender.sendMessage(Methods.toColoredString(Vars.tag + Vars.defaultColor + "Permissao " + Vars.infoColor + perm
								+ Vars.defaultColor + " removida do grupo " + Vars.infoColor + name));
					}
				}
				if(args[0].equalsIgnoreCase("user")){
					if(args[1].equalsIgnoreCase("add")){
						try{
							System.out.println(args[2]);
							Player p = Bukkit.getPlayer(args[2]);
							String perm = args[3];
							Main.db.addPlayerPerm(p, perm);
							Methods.refreshPlayer(p);
							sender.sendMessage(Methods.toColoredString(Vars.tag + Vars.defaultColor + "Permissao " + Vars.infoColor + perm
									+ Vars.defaultColor + " adcionada ao jogador " + Vars.infoColor + p.getName()));
						}catch(Exception e){
							e.printStackTrace();
							sender.sendMessage(Methods.toColoredString(Vars.tag + "Este jogador esta offline"));
						}
					}
					if(args[1].equalsIgnoreCase("remove")){
						try{
							Player p = Bukkit.getPlayer(args[2]);
							String perm = args[3];
							Main.db.removePlayerPerm(p, perm);
							Methods.refreshPlayer(p);
							sender.sendMessage(Methods.toColoredString(Vars.tag + Vars.defaultColor + "Permissao " + Vars.infoColor + perm
									+ Vars.defaultColor + " removida do jogador " + Vars.infoColor + p.getName()));
						}catch(Exception e){
							sender.sendMessage(Methods.toColoredString(Vars.tag + "Este jogador esta offline"));
						}
					}
					if(args[1].toLowerCase() != "remove" || args[1].toLowerCase() != "add"){
						if(args[2].equalsIgnoreCase("group")){
							if(args[3].equalsIgnoreCase("set")){
								try{
									Player p = Bukkit.getPlayer(args[1]);
									String group = args[4];
									Main.db.setPlayerGroup(p, group);
									Methods.refreshPlayer(p);
									sender.sendMessage(Methods.toColoredString(Vars.tag + Vars.defaultColor + "O jogador " + Vars.infoColor 
											+ p.getName() + Vars.defaultColor + " agora esta no grupo " + Vars.infoColor + group));
								}catch(Exception e){
									sender.sendMessage(Methods.toColoredString(Vars.tag + "Este jogador esta offline"));
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}else{
			sender.sendMessage(Methods.toColoredString(Vars.tag + "&cSem permissao"));
		}
		
		return false;
	}
	
}
