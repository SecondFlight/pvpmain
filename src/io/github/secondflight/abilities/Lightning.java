package io.github.secondflight.abilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.secondflight.util.DescriptionHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Lightning implements Listener {
	private final Plugin plugin;
	
	public Lightning (Plugin plugin) {
		this.plugin = plugin;
	}
	
	DescriptionHandler dh = new DescriptionHandler();
	
	@EventHandler
	public void interactEvent (PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (dh.hasAggressiveAbility(event.getPlayer().getItemInHand(), "Lightning Strike")) {
				int radius = -1;
				int strikes = -1;
				
				switch (dh.getAggressiveAbilityLevel(event.getItem())) {
					case 1: radius = 5; strikes = 3; break;
					case 2: radius = 8; strikes = 5; break;
					case 3: radius = 12; strikes = 7; break;
				}
				
				lightningStrike (event.getPlayer(), radius, strikes);
				
			}
		}
	}
	
	public void lightningStrike (Player p, int radius, int strikes) {
		int range = 50;
		
		Location pl = p.getEyeLocation();
		
		Location strikeLocation = null;

		double px = pl.getX();
		double py = pl.getY();
		double pz = pl.getZ();

		double yaw = Math.toRadians(pl.getYaw() + 90);
		double pitch = Math.toRadians(pl.getPitch() + 90);

		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);

		
		for (int i = 1; i <= range; i++) {
			Location loc = new Location(p.getWorld(), px + i * x, py + i * z, pz + i * y);
			if (!(loc.getBlock().getType() == Material.AIR)) {
				strikeLocation = loc;
				break;
			}
		
		}
		
		if (!(strikeLocation == null)) {
			System.out.println("about to do lightning effect");
			lightningEffect (strikeLocation, radius, strikes);
		}
	}
	
	private void lightningEffect (Location l, int radius, int strikes) {
		List<Location> list = new ArrayList<Location>();
		
		for (int i = 1; i <= strikes; i++) {
			int num1 = randomNumber(0, radius*2) - radius;
			int num2 = randomNumber(0, radius*2) - radius;
			list.add(l.clone().add(num1, 0, num2));
		}
		
		System.out.println("got past the list adding, next is the lightning task");
		
		BukkitTask task = new LightningTask ((JavaPlugin) this.plugin, list).runTaskTimer(plugin, 0, 1);
	}
	
	public static int randomNumber(int lower, int upper) {
		return lower + (int)(Math.random() * ((upper - lower) + 1));
	}
	
}

class LightningTask extends BukkitRunnable  {
	private final JavaPlugin plugin;
	List<Location> locations;
	List<Boolean> fireOnTick = new ArrayList<Boolean>();
	
	int numberOfBlanksToAdd;
	
	LightningTask (JavaPlugin plugin, List<Location> locs) {
		System.out.println("Got to creation of task");
		
		this.plugin = plugin;
		locations = locs;
		
		for (int i = 1; i <= numberOfBlanksToAdd; i++) {
			fireOnTick.add(false);
		}
		
		System.out.println("Added falses");
		
		for (int i = 1; i <= locations.size(); i++) {
			fireOnTick.add(true);
		}
		
		System.out.println("Added trues");
		
		Collections.shuffle(fireOnTick);
		
		System.out.println("shuffled");
		
		maxLocation = (locations.size() - 1);
		maxFireOnTick = (fireOnTick.size() - 1);
	}
	
	int currentLocation = 0;
	int maxLocation;
	
	int currentFireOnTick = 0;
	int maxFireOnTick;
	
	public void run() {
		System.out.println("got to run");
		if (currentLocation <= maxLocation && currentFireOnTick <= maxFireOnTick) {
			if (fireOnTick.get(currentFireOnTick) == true) {
				locations.get(currentLocation).getWorld().strikeLightning(locations.get(currentLocation));
				currentLocation ++;
			}
		} else {
			
			this.cancel();
		}
		
		currentFireOnTick ++;
		
	}
}
