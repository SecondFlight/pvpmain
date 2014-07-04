package io.github.secondflight.abilities;

import io.github.secondflight.util.DescriptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

	



public class FireBreath implements Listener {
	
	
	public static Map<Player, Integer> fireBreathCooldown = new HashMap<Player, Integer>();
	
	private final Plugin plugin;
	
	public FireBreath (Plugin plugin) {
		this.plugin = plugin;
	}
	
	DescriptionHandler dh = new DescriptionHandler();
	
	
	@EventHandler
	public void clickListener(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack i = p.getItemInHand();
		
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			if (dh.hasAggressiveAbility(i, "Fire Breath")) {
				fireBreath(p, dh.getAggressiveAbilityLevel(i), i);
			}
			
			
		}
	}
	
	
	
	private void fireBreath (Player p, int level, ItemStack i) {
		int fireDuration;
		int breathDuration;
		
		
		if (level == 1) {
			fireDuration = 60;
			breathDuration = 80;
		} else if (level == 2) {
			fireDuration = 80;
			breathDuration = 80;
		} else if (level == 3) {
			fireDuration = 100;
			breathDuration = 120;
		} else if (level == 4) {
			fireDuration = 120;
			breathDuration = 160;
		} else if (level == 5) {
			fireDuration = 140;
			breathDuration = 160;
		}
		
		
		
		
		
		}
	



int task1;

private void durabilityTimer (final ItemStack item, final int ticks, final Player player) {
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
				
				FireBreath.fireBreathCooldown.put(player, iterations*100);
				
				player.sendMessage(Integer.toString(iterations));
				
				if (!(iterations > 0)) {
					item.setDurability((short)0);
					Bukkit.getScheduler().cancelTask(task1);
					
					FireBreath.fireBreathCooldown.put(player, 0);
				}
				
				}
			}, 0L, 100L);
	
	
	

	}
}
