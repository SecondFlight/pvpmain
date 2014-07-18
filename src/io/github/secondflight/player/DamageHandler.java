package io.github.secondflight.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;


public class DamageHandler implements Listener{
	private final Plugin plugin;
	
	public DamageHandler (Plugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDamage (EntityDamageEvent event) {
		
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			double damage = event.getDamage();
			
			
		}
	}
	
	
}
