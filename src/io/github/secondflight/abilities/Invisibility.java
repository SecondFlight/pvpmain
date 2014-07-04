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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.ItemStack;

public class Invisibility implements Listener {
	
	public static Map<Player, Integer> invisibilityCooldown = new HashMap<Player, Integer>();
	
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
		
		if (invisibilityCooldown.get(player) == null) {
			invisibilityCooldown.put(player, 0);
		}
		
		if (invisibilityCooldown.get(player) == 0) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1, true));
			
			
			durabilityTimer(i, cooldown, player);
			
		} /** else if (invisibilityCooldown.get(player) > 0) {
			
			
			player.sendMessage(ChatColor.RED + "You need to wait " + ChatColor.WHITE + Math.ceil((invisibilityCooldown.get(player)/20)) + ChatColor.RED + " more seconds before you can use invisibility again.");
			
		}*/
	}
	
	int task1;
	
	public void durabilityTimer (final ItemStack item, final int ticks, final Player player) {
		//player.sendMessage("durabilityTimer has been called");
		
			
			
			
			
			
			item.setDurability(item.getType().getMaxDurability());
			
			task1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				
				
				short maxDurability = item.getType().getMaxDurability();
				short durability = maxDurability;
				short interval = (short)Math.round(maxDurability/(ticks / 100));
				int iterations = Math.round(ticks/100);
				
				
				
				public void run() {
					
					durability = (short) (durability - interval);
					item.setDurability(durability);
					iterations = iterations - 1;
					
					Invisibility.invisibilityCooldown.put(player, iterations*100);
					
					player.sendMessage(Integer.toString(iterations));
					
					if (!(iterations > 0)) {
						item.setDurability((short)0);
						Bukkit.getScheduler().cancelTask(task1);
						
						Invisibility.invisibilityCooldown.put(player, 0);
					}
					
					}
				}, 0L, 100L);
		
		
		
	
		}
}
