// REMINDER: regualar aggressives go up to level 5, while rare ones go to level 3

package io.github.secondflight.abilities;

import io.github.secondflight.util.DescriptionHandler;
import io.github.secondflight.effects.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Aggressive implements Listener {
	private final Plugin plugin;
	
	public Aggressive (Plugin plugin) {
		this.plugin = plugin;
	}
	
	DescriptionHandler dh = new DescriptionHandler();
	
	@EventHandler
	public void clickListener(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack i = p.getItemInHand();
		
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			if (dh.hasAggressiveAbility(i, "Invisibility")) {
				
				invisibility(p, dh.getAggressiveAbilityLevel(i), i);
			
			} else if (dh.hasAggressiveAbility(i, "Fire Breath")) {
				fireBreath(p, dh.getAggressiveAbilityLevel(i), i);
			}
			
			
		}
	}
	
	@EventHandler
	public void hitListener(EntityDamageByEntityEvent event) {
		
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			
			
			if (dh.hasAggressiveAbility(damager.getItemInHand(), "Health Steal")) {
				healthSteal(damager, dh.getAggressiveAbilityLevel(damager.getItemInHand()));
				
			}
		}
	}
	
	
	
	
	
	
	
	public static Map<Player, Integer> invisibilityCooldown = new HashMap<Player, Integer>();
	public static Map<Player, Integer> fireBreathCooldown = new HashMap<Player, Integer>();
	
	private void healthSteal (Player damager, int level) {
		
		
		boolean steal = false;
		
		//probablility: 20%
		if (level == 1) {
			int randomNum = DescriptionHandler.randomNumber(1, 5);
			
			if (randomNum == 1) {
				steal = true;
			}
		
		//probability: 30%
		} else if (level == 2) {
			int randomNum = DescriptionHandler.randomNumber(1, 5);
			
			if (randomNum == 1) {
				steal = true;
			}
		
		//probability: 40%
		} else if (level == 3) {
			int randomNum = DescriptionHandler.randomNumber(1, 10);
			
			if (randomNum <= 3) {
				steal = true;
			}
		
		//probability: 50&
		} else if (level == 4) {
			int randomNum = DescriptionHandler.randomNumber(1, 2);
			
			if (randomNum == 1) {
				steal = true;
			}
		
			
		//probability: 60%
		} else if (level == 5) {
			int randomNum = DescriptionHandler.randomNumber(1, 5);
			
			if (randomNum <= 3) {
				steal = true;
			}
		}
		
		if (steal == true) {
			if (damager.getMaxHealth() - damager.getHealth() < 2) {
				damager.setHealth(damager.getMaxHealth());
			} else {
				damager.setHealth(damager.getHealth() + 2);
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
					
					Aggressive.invisibilityCooldown.put(player, iterations*100);
					
					player.sendMessage(Integer.toString(iterations));
					
					if (!(iterations > 0)) {
						item.setDurability((short)0);
						Bukkit.getScheduler().cancelTask(task1);
						
						Aggressive.invisibilityCooldown.put(player, 0);
					}
					
					}
				}, 0L, 100L);
		
		
		
	
		}
	
	/**int task2;
	
	public void invisTimer (final Player player, final int iterations) {
		
			long delay = 0L;
			long interval = 5L;
			
			
			
			task2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				
				
				//int iterations2 = iterations;
				
				
				
				public void run() {
					
					
					// --;
					
					Aggressive.invisibilityCooldown.put(player, iterations*5);
					
					if (!(iterations > 0)) {
						
						Bukkit.getScheduler().cancelTask(task2);
						
						Aggressive.invisibilityCooldown.put(player, 0);
					}
					
					}
				}, delay, interval);
		
		
		
	
		}*/
}



