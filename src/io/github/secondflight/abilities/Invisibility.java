package io.github.secondflight.abilities;

import java.util.HashMap;
import java.util.Map;

import io.github.secondflight.util.DescriptionHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.inventory.ItemStack;

public class Invisibility implements Listener {
	
	private final Plugin plugin;
	
	public Invisibility (Plugin plugin) {
		this.plugin = plugin;
	}
	
	DescriptionHandler dh = new DescriptionHandler();
	
	@EventHandler
	public void clickListener (PlayerInteractEvent event) {
		
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			DescriptionHandler dh = new DescriptionHandler();
			ItemStack i = event.getItem();
			Player p = event.getPlayer();
			
			if (dh.hasAggressiveAbility(i, "Invisibility")) {
		
				invisibility(p, dh.getAggressiveAbilityLevel(i), i);
	
			}
		}
	}
	
	private void invisibility (Player player, int level, ItemStack i) {
		
		int duration = 0;
		int cooldown = 1000;
		
		if (level == 1) {
			duration = 200;
		} else if (level == 2) {
			duration = 300;
		} else if (level == 3) {
			duration = 400;
		}
		
		if (invisDurabilityTimer.cooldownMap.get(player) == null) {
			invisDurabilityTimer.cooldownMap.put(player, 0);
		}
		
		if (invisDurabilityTimer.cooldownMap.get(player) == 0) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1, true));
			
			
			durabilityTimer(i, cooldown, player);
			
		} 
	}
	

	
	private void durabilityTimer (final ItemStack item, final int ticks, final Player player) {
		//player.sendMessage("durabilityTimer has been called");
		
		int ticksBetween = 100;	
		BukkitTask task = new invisDurabilityTimer ((JavaPlugin) plugin, item, player, ticks, ticksBetween).runTaskTimer(this.plugin, 0, ticksBetween);
			
			
			

		
		
		
	
		}
}

class invisDurabilityTimer extends BukkitRunnable {
	private final JavaPlugin plugin;
	
	short maxDurability;
	short durability;
	short interval;
	int iterations;
	int ticksBetween;
	ItemStack item;
	Player player;
	
	public static Map<Player, Integer> cooldownMap = new HashMap<Player, Integer>();
	
	
	public invisDurabilityTimer (JavaPlugin plugin, ItemStack i, Player p, int ticks, int ticksBetween) {	
		this.plugin = plugin;
		
		this.ticksBetween = ticksBetween;
		
		item = i;
		player = p;
		
		maxDurability = item.getType().getMaxDurability();
		durability = maxDurability;
		interval = (short)Math.round(maxDurability/(ticks / ticksBetween));
		iterations = Math.round(ticks/ticksBetween);
		
		item.setDurability(item.getType().getMaxDurability());
		
	}	
		


		public void run() {
			
			durability = (short) (durability - interval);
			item.setDurability(durability);
			iterations = iterations - 1;
			
			cooldownMap.put(player, iterations*ticksBetween);
			
			if (!(iterations > 0)) {
				item.setDurability((short)0);
				this.
				
				cooldownMap.put(player, null);
			}
			
		}
}
