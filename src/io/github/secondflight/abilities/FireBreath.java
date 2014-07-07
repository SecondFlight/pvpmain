package io.github.secondflight.abilities;

import io.github.secondflight.util.DescriptionHandler;
import io.github.secondflight.util.ParticleEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

	



public class FireBreath implements Listener {
	
	Map<Integer, Integer> fireBreathTask = new HashMap<Integer, Integer>();
	
	public static Map<Player, Integer> fireBreathCooldown = new HashMap<Player, Integer>();
	
	private final Plugin plugin;
	
	public FireBreath (Plugin plugin) {
		this.plugin = plugin;
	}
	
	DescriptionHandler dh = new DescriptionHandler();
	
	
	@EventHandler
	public void clickListener(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		//ALWAYS USE event.getItem();
		ItemStack i = event.getItem();
		
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			if (dh.hasAggressiveAbility(i, "Fire Breath")) {
				
				fireBreath(p, dh.getAggressiveAbilityLevel(i), i);
				System.out.println("Fire Breath has finished.");
			}
			
		}
		return;
	}
	
	
	
	private void fireBreath (Player p, int level, ItemStack i) {
		System.out.println("FireBreath has been called for player " + p.getDisplayName() + ".");
		
		int fireDuration = 0;
		int breathDuration = 0;
		
		
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
		
		
		
		System.out.println("Fire duration: " + fireDuration + " ticks");
		System.out.println("Breath duration: " + breathDuration + " ticks");
		
		animation(p, 20, fireDuration);
		
		}
	
	private void animation(Player p, int distance, int burnTime) {
		System.out.println("The animation for Fire Breath had been started with a distance of " + distance + " blocks and a burn duration of " + burnTime + " ticks.");
		
		System.out.println("Getting the list of blocks...");
		final List<Location> locationList = getLocations(p, distance);
		System.out.println("List of blocks has been retreived.");
		

		
		BukkitTask task = new FireBreathTask((JavaPlugin) plugin, locationList).runTaskTimer(this.plugin, 0, 3);
		
		
		
		
	}
	
	private List<Location> getLocations (Player p, int distance) {
		Location l = p.getLocation().add(0, 1, 0);
		Vector v = p.getLocation().getDirection().normalize();
		
		List<Location> list = new ArrayList<Location>();
		
		Location pl = p.getEyeLocation();

		double px = pl.getX();
		double py = pl.getY();
		double pz = pl.getZ();

		double yaw = Math.toRadians(pl.getYaw() + 90);
		double pitch = Math.toRadians(pl.getPitch() + 90);

		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);

		for (int i = 1; i <= distance; i++) {
			Location loc = new Location(p.getWorld(), px + i * x, py + i * z, pz + i * y);
			if (loc.getBlock().getType() == Material.AIR) {
				list.add(loc);
			}
		}
		
		//BlockIterator blocks = new BlockIterator(p.getWorld(), p.getLocation().toVector(), p.getLocation().getDirection().normalize(), 0, distance);
		

		
		System.out.println("getLocations has finished. Returning with the list.");
		return list;
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

class FireBreathTask extends BukkitRunnable  {
	private final JavaPlugin plugin;
	
	List <Location> locationList;
	int howManyLocations;
	
	FireBreathTask(JavaPlugin plugin, List<Location> locations)	{
		this.plugin = plugin;
		locationList = locations;
		howManyLocations = locationList.size();
	}
	
	int currentBlock = 0;
		

		
		
		public void run() {
			System.out.println("Running. Current block is " + currentBlock);
			
			if (currentBlock < howManyLocations) {
				System.out.println("The current block is less than the total. Making a pretty fireball.");
				System.out.println("Fireball will be at " + locationList.get(currentBlock).getX() + ", " + locationList.get(currentBlock).getY() + ", " + locationList.get(currentBlock).getZ() + "." );
				
				ParticleEffect.FLAME.display(locationList.get(currentBlock), (float)0.2, (float)0.2, (float)0.2, (float)0.1, 100);
				
				System.out.println("Pretty fireball created.");
				
				currentBlock += 1;
			} else {
				this.cancel();
			}
			
		}
	//}, 0L, 5L));
}
