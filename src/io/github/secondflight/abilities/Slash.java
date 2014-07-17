package io.github.secondflight.abilities;

import io.github.secondflight.util.DescriptionHandler;
import io.github.secondflight.util.ParticleEffect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 * 
 * @author Toon Sevrin
 * @author Joshua Wade
 * 
 * Original by Toon Sevrin
 * Implemented by Joshua Wade
 * 
 * Used with permission.
 * He's basically my project leader srsly
 * never doubt the worm
 *
 */

public class Slash implements Listener {
	DescriptionHandler dh = new DescriptionHandler();
	
	private Plugin plugin;

	public Slash(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteractEvent(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (dh.hasAggressiveAbility(e.getItem(), "Slash"))	{
				slash(e.getPlayer(), 0);
			}
		}
	}

	public void slash(final Player p, final int angle) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				
				//rotate player head
				Location loc =p.getLocation();
				loc.setYaw(loc.getYaw()+20);
				p.teleport(loc);
				
				//particle location = p.getLocation().add(loc.getDirection().normalize().multiply(5))
				//add your damage code here (check nearby entities)
				
				//fire the particle
				ParticleEffect.ENCHANTMENT_TABLE.display(p.getLocation().add(0, 2, 0).add(loc.getDirection().normalize().multiply(3)), (float)0.2, (float)0.2, (float)0.2, (float)0.1, 20);
				ParticleEffect.displayBlockDust(p.getLocation().add(0, 2, 0).add(loc.getDirection().normalize().multiply(3)), 41, (byte)0, (float)0.05, (float)0.05, (float)0.05, (float)0.1, 20);
				
				ParticleEffect.ENCHANTMENT_TABLE.display(p.getLocation().add(0, 2, 0).add(loc.getDirection().normalize().multiply(5)), (float)0.2, (float)0.2, (float)0.2, (float)0.1, 20);
				ParticleEffect.displayBlockDust(p.getLocation().add(0, 2, 0).add(loc.getDirection().normalize().multiply(3)), 41, (byte)0, (float)0.05, (float)0.05, (float)0.05, (float)0.1, 20);
				
				ParticleEffect.ENCHANTMENT_TABLE.display(p.getLocation().add(0, 2, 0).add(loc.getDirection().normalize().multiply(7)), (float)0.2, (float)0.2, (float)0.2, (float)0.1, 20);
				ParticleEffect.displayBlockDust(p.getLocation().add(0, 2, 0).add(loc.getDirection().normalize().multiply(3)), 41, (byte)0, (float)0.05, (float)0.05, (float)0.05, (float)0.1, 20);
						
				//play sound effect
				loc.getWorld().playSound(loc, Sound.FIZZ, 1, 1);
				
				//if he is rotating to the left rotate to the right after angle = 180
					if(angle !=360)
					slash(p,angle+20);
					
			}
		}, 2l);
	}
}
