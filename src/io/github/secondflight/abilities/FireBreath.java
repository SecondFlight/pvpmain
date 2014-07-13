package io.github.secondflight.abilities;

import io.github.secondflight.util.DescriptionHandler;
import io.github.secondflight.util.ParticleEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
import org.bukkit.util.Vector;

	



public class FireBreath implements Listener {
	
	Map<Integer, Integer> fireBreathTask = new HashMap<Integer, Integer>();
	
	
	
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
				if (fireBreathDurabilityTimer.cooldownMap.get(p) == null)
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
		int cooldown = 200;
		
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
		
		
		
		
		animation(p, 20, fireDuration, breathDuration, cooldown);
		
		}
	
	private void animation(Player p, int distance, int burnTime, int breathDuration, int cooldown) {
		
		BukkitTask task = new FireBreathCallTask((JavaPlugin) plugin, p, distance, burnTime, breathDuration, 5).runTaskTimer(this.plugin, 0, 5);
		durabilityTimer(p.getItemInHand(), cooldown, p);
	}
	




	private void durabilityTimer (final ItemStack item, final int ticks, final Player player) {
	//player.sendMessage("durabilityTimer has been called");
	
		
		int ticksBetween = 20;
		BukkitTask task = new fireBreathDurabilityTimer((JavaPlugin) plugin, item, player, ticks, ticksBetween).runTaskTimer(this.plugin, 0, 20);
		
		
		
		
	
	
	

	}
	

}

class FireBreathTask extends BukkitRunnable  {
	private final JavaPlugin plugin;
	
	List <Location> locationList;
	List <Entity> entities = new ArrayList <Entity>();
	int howManyLocations;
	Player p;
	int radius;
	int burnTime;
	
	FireBreathTask(JavaPlugin plugin, List<Location> locations, Player p, int distance, int burnTime)	{
		this.plugin = plugin;
		locationList = locations;
		howManyLocations = locationList.size();
		this.p = p;
		for (Entity e : p.getNearbyEntities(distance + 2, distance + 2, distance + 2)) {
			entities.add(e);
		}
		radius = 2;
		this.burnTime = burnTime;
	}
	
	int currentBlock = 0;
		

		
		
		public void run() {
			//System.out.println("Running. Current block is " + currentBlock);
			
			if (currentBlock < howManyLocations) {
				//System.out.println("The current block is less than the total. Making a pretty fireball.");
				//System.out.println("Fireball will be at " + locationList.get(currentBlock).getX() + ", " + locationList.get(currentBlock).getY() + ", " + locationList.get(currentBlock).getZ() + "." );
				
				ParticleEffect.FLAME.display(locationList.get(currentBlock), (float)0.2, (float)0.2, (float)0.2, (float)0.1, 20);
				ParticleEffect.SMOKE.display(locationList.get(currentBlock), (float)0.2, (float)0.2, (float)0.2, (float)0.1, 15);
				
				//System.out.println("Pretty fireball created.");
				
				//System.out.println("Applying damage and setting fire to applicable entities...");
				if (entities == null) {
					this.cancel();
				}
				
				if (!(entities.isEmpty())) {
					//System.out.println("Entities is not empty. Going through each entity...");
					for (Entity e : entities) {
						//System.out.println("This entity is a " + e.getType().name().toString());
						if ((e instanceof LivingEntity || e instanceof Player) && !(e.equals(p))) {
							//System.out.println("This " + e.getType().name().toString() + " is a LivingEntity");
							LivingEntity ent = (LivingEntity) e;
							if (e.getLocation().distanceSquared(locationList.get(currentBlock)) < (radius*radius)) {
								//System.out.println(ChatColor.AQUA + "This entity needs to be damaged.");
								ent.damage(2);
								e.setFireTicks(burnTime);
							}
						}
					}
				}
					
				currentBlock += 1;
			} else {
				this.cancel();
			}
			
		}
}

class FireBreathCallTask extends BukkitRunnable  {
	private final JavaPlugin plugin;
	List <Entity> entities = new ArrayList <Entity>();
	Player p;
	int radius;
	int burnTime;
	int distance;
	int breathDuration;
	int iterations;
	int ticksBetweenBreath;
	
	FireBreathCallTask(JavaPlugin plugin, Player p, int distance, int burnTime, int breathDuration, int ticksBetweenBreath)	{
		this.plugin = plugin;
		this.p = p;
		this.distance = distance;
		this.burnTime = burnTime;
		this.breathDuration = breathDuration;
		this.ticksBetweenBreath = ticksBetweenBreath;
		this.iterations = Math.round(breathDuration / ticksBetweenBreath);
		//System.out.println("The animation for Fire Breath had been started with a distance of " + distance + " blocks and a burn duration of " + burnTime + " ticks.");

		
		
		
	}
	
	
	private List<Location> getLocations (Player p, int distance) {
		Location l = p.getLocation().add(0, 1, 0);
		Vector v = p.getLocation().getDirection().normalize();
		
		List<Location> list = new ArrayList<Location>();
		
		Location pl = p.getEyeLocation().subtract(0, 0.5, 0);

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
				if (!(loc.getBlock().getType().isSolid()) && !(loc.getBlock().getType() == Material.GLASS)) {
					// spaces out the particles to make them move twice as fast
					// sorta hacky, I know
					if ((i & 1) == 0) {
						list.add(loc);
					}
				} else {
					break;
				}
		
		}
		
		

		
		//System.out.println("getLocations has finished. Returning with the list.");
		return list;
	}
	
	
	int currentIteration = 1;
	
	
	public void run() {
		
		if (currentIteration <= iterations) {
			BukkitTask task = new FireBreathTask((JavaPlugin) plugin, getLocations(p, distance), p, distance, burnTime).runTaskTimer(this.plugin, 0, 3);
			currentIteration ++;
			//p.sendMessage(Integer.toString(currentIteration));
		} else {
			this.cancel();
		}
		
	}
}

class fireBreathDurabilityTimer extends BukkitRunnable {
	private final JavaPlugin plugin;
	
	short maxDurability;
	short durability;
	short interval;
	int iterations;
	int ticksBetween;
	ItemStack item;
	Player player;
	
	public static Map<Player, Integer> cooldownMap = new HashMap<Player, Integer>();
	
	
	public fireBreathDurabilityTimer (JavaPlugin plugin, ItemStack i, Player p, int ticks, int ticksBetween) {	
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


