package me.dodocarlos.permissions;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.dodocarlos.permissions.cmds.Perm;
import me.dodocarlos.permissions.listeners.Join;
import me.dodocarlos.permissions.utils.DB;
import me.dodocarlos.permissions.utils.Methods;
import me.dodocarlos.permissions.utils.Vars;

public class Main extends JavaPlugin{
	
	public static DB db;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onLoad() {
		Vars.main = this;
		Vars.setup();
		config();
		for(Player p : Bukkit.getOnlinePlayers()){
			p.kickPlayer(Methods.toColoredString(Vars.tag + Vars.infoColor + "Plugin recarregado, todos foram kickados!"));
		}
		db = new DB();
		Bukkit.getConsoleSender().sendMessage("Conectado ao banco de dados!");
		Methods.setupGroups();
	}
	
	@Override
	public void onEnable() {
		//Commands
		getCommand("perm").setExecutor(new Perm());
		
		Bukkit.getPluginManager().registerEvents(new Join(), this);
	}
	
	@Override
	public void onDisable() {
		
	}

	public void config(){
		File configf = new File(getDataFolder(), "config.yml");
		if(!configf.exists()){
			saveDefaultConfig();		
		}	
	}
	
}
