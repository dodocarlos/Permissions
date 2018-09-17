package me.dodocarlos.permissions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.dodocarlos.permissions.utils.Methods;

public class Join implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Methods.refreshPlayer(e.getPlayer());
	}
	
}
