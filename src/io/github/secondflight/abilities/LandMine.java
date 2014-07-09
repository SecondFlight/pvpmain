package io.github.secondflight.abilities;

import java.awt.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.secondflight.util.DescriptionHandler;
import io.github.secondflight.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

//TODO: Reset when player goes to spawn or changes items, however that will work.

public class LandMine implements Listener{
	private final Plugin plugin;
	
	public LandMine (Plugin plugin) {
		this.plugin = plugin;
	}
	
	static HashMap<Block, Player> mineMap = new HashMap<Block, Player>();
	static HashMap<Player, List<Block>> listMap = new HashMap<Player, List<Block>>();
	
	DescriptionHandler dh = new DescriptionHandler();
	
	@EventHandler
	public void joinEvent (PlayerJoinEvent event) {
		if (!(listMap.containsKey(event.getPlayer()))) {
			listMap.put(event.getPlayer(), new ArrayList<Block>());
		}
	}
	
	@EventHandler
	public void leaveEvent (PlayerQuitEvent event) {
		resetMines(event.getPlayer());
	}
	
	@EventHandler
	public void interactEvent (PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player p = event.getPlayer();
			ItemStack i = p.getItemInHand();
			
			
			if (dh.hasAggressiveAbility(i, "Land Mines")) {
				int minesAllowed = -1;
				
				switch (dh.getAggressiveAbilityLevel(i)) {
					case 1: minesAllowed = 3; break;
					case 2: minesAllowed = 5; break;
					case 3: minesAllowed = 7; break;
					case 4: minesAllowed = 9; break;
					case 5: minesAllowed = 11; break;
				}
				
				System.out.println("Creating new mine.");
				newLandMine (p, event.getClickedBlock(), minesAllowed);
			}
		}
		
		if (event.getAction() == Action.PHYSICAL) {
			Block b = event.getClickedBlock();
			if (b.getType() == Material.STONE_PLATE) {
				if (!(getMinePlacer(b) == null) && !(getMinePlacer(b).equals(event.getPlayer()))) {
					explodeMine(b, event.getPlayer(), 5);
				}
			}
		}
	}
	
	@EventHandler
	public void entityChangeblockEvent (EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			if (!(mineMap.get(event.getBlock()) == null)) {
				explodeMine (event.getBlock(), event.getEntity(), 5);
			}
		}
	}
	

	
	public void newLandMine (Player p, Block b, int minesAllowed) {
		registerNewMine(p, b, minesAllowed);
	}
	
	private static void registerNewMine (Player p, Block b, int minesAllowed) {
		Block blockAbove = b.getLocation().add(0, 1, 0).getBlock();
		if (blockAbove.getType() == Material.AIR) {
			System.out.println("The block being checked is air. The mine will now be created.");
			
			System.out.println("Setting the block to a stone pressure plate...");
			blockAbove.setType(Material.STONE_PLATE);
			System.out.println("Done.");
			
			System.out.println("Putting the block and player in mineMap...");
			mineMap.put(blockAbove, p);
			System.out.println("Done.");

			System.out.println("Retrieving list... of mines for the player...");
			List<Block> list = listMap.get(p);
			System.out.println("Done.");
			
			if (list.size() == minesAllowed) {
				System.out.println("The list is at its limit. The oldest mine will be despawned, and a new one will be created.");
				list.add(0, blockAbove);
				System.out.println("Added mine to the list.");
				despawnMine (list.get(list.size() - 1));
				System.out.println("Finished despawning the oldest mine.");
			} else if (list.size() < minesAllowed) {
				System.out.println("The list has room for more. Adding a mine...");
				list.add(0, blockAbove);
				System.out.println("Done.");
			} else {
				System.out.println("[PVPMain] ERROR: Tried to add a mine to the list of a player (" + p.getDisplayName() + "), but his list of mines exceeds his current limit ( " + Integer.toString(minesAllowed) + ".");
				return;
			}
			
			System.out.println("Setting mineMap and listMap...");
			mineMap.put(b, p);
			listMap.put(p, list);
			System.out.println("Done.");
			
		}
	}
	
	private void explodeMine(Block b, Player p, int radius) {
		fakeExplosion (b.getLocation(), (Entity) p, radius);
		despawnMine (b);
	}
	
	private void explodeMine(Block b, Entity e, int radius) {
		fakeExplosion (b.getLocation(), e, radius);
		despawnMine (b);
	}
	
	private static void despawnMine(Block b) {
		System.out.println("Despawning block...");
		System.out.println("Getting mineMap value for the block...");
		Player p = mineMap.get(b);
		System.out.println("Done.");
		
		System.out.println("Getting listMap value for the player...");
		List<Block> list = listMap.get(p);
		System.out.println("Done.");
		
		System.out.println("Setting the block to air...");
		b.setType(Material.AIR);
		System.out.println("Done.");
		
		System.out.println("Removing the block from the list...");
		list.remove(list.indexOf(b));
		System.out.println("Done.");
		
		System.out.println("Setting mineMap and listMap...");
		mineMap.put(b, null);
		listMap.put(p, list);
		System.out.println("Done.");
		
		System.out.println("Block despawning process has completed.");
	}
	
	
	public static void resetMines (Player p) {
		if (listMap.get(p).size() > 0) {
			List<Block> list = listMap.get(p);
			for (Block b : list) {
				despawnMine(b);
			}
			
			
			//listMap.put(p, null);
		}
	}
	
	/**
	 * Gets the player who placed a mine at the given block. Returns null if there is no mine at the given block.
	 * @param b
	 * @return
	 */
	private static Player getMinePlacer (Block b) {
		if (mineMap.containsValue(b)) {
			return mineMap.get(b);
		} else {
			return null;
		}
	}
	
	private void fakeExplosion (Location l, Entity ent, int radius) {
		ParticleEffect.HUGE_EXPLOSION.display(l, 0, 0, 0, 0, 1);
		l.getWorld().playSound(l, Sound.EXPLODE, 10, 1);
		
		for (Entity e : ent.getNearbyEntities(radius, radius, radius)) {
			if (e instanceof LivingEntity) {
				((LivingEntity) e).damage(8);
			}
		}
	}
	
	/**private void fakeExplosion (Block b, Entity e, int radius) {
		Location l = b.getLocation();
		
		System.out.println("Creating a fake explosion based on entities in 9 chunks.");
		List<Entity> entitiesPre = new ArrayList<Entity>();
		
		System.out.println("Getting entities in the location's chunk and all 8 surrounding chunks...");
		for (Entity e : l.getWorld().getChunkAt(l.add(-16, 0, -16)).getEntities()) {
			entitiesPre.add(e);
		}
		
		for (Entity e : l.getWorld().getChunkAt(l.add(0, 0, -16)).getEntities()) {
			entitiesPre.add(e);
		}
		
		for (Entity e : l.getWorld().getChunkAt(l.add(16, 0, -16)).getEntities()) {
			entitiesPre.add(e);
		}
		
		for (Entity e : l.getWorld().getChunkAt(l.add(-16, 0, 0)).getEntities()) {
			entitiesPre.add(e);
		}
		
		for (Entity e : l.getWorld().getChunkAt(l.add(0, 0, 0)).getEntities()) {
			entitiesPre.add(e);
		}
		
		for (Entity e : l.getWorld().getChunkAt(l.add(16, 0, 0)).getEntities()) {
			entitiesPre.add(e);
		}
		
		for (Entity e : l.getWorld().getChunkAt(l.add(-16, 0, 16)).getEntities()) {
			entitiesPre.add(e);
		}
		
		for (Entity e : l.getWorld().getChunkAt(l.add(0, 0, 16)).getEntities()) {
			entitiesPre.add(e);
		}
		
		for (Entity e : l.getWorld().getChunkAt(l.add(16, 0, 16)).getEntities()) {
			entitiesPre.add(e);
		}
		System.out.println("Done.");
		
		System.out.println("Setting some variables...");
		List<Entity> entities = new ArrayList<Entity>();
		Entity closest = null;
		double closestDistSquared = Double.MAX_VALUE;
		System.out.println("Done.");
		
		System.out.println("Iterating through the entities to see if they are within the radius and adding them to a new list...");
		for (Entity e : entitiesPre) {
			
			System.out.println("Getting distance of this location...");
			double entDistSquared = e.getLocation().distanceSquared(l);
			
			if (entDistSquared <= radius*radius || e instanceof LivingEntity) {
				System.out.println("This location is close enough, and the entity is either a mob or a player.");
				System.out.println("This entity is a " + e.getType().name().toString());
				entities.add(e);
				System.out.println("Entity has been added to a new list.");
				
				if (closest == null || closestDistSquared < entDistSquared) {
					System.out.println("This entity is the new closest! *clapclap*");
					closest = e;
					closestDistSquared = entDistSquared;
				}
			} else {
				System.out.println("This location is not close enough.");
			}
			
		}
		System.out.println("Iteration complete.");
		

		
		if (!(closest instanceof Player)) {
		
		
			System.out.println("Playing the explosion sound and particle...");
			ParticleEffect.HUGE_EXPLOSION.display(l, 0, 0, 0, 0, 1);
			l.getWorld().playSound(l, Sound.EXPLODE, 10, 1);
			System.out.println("Done.");
		
			System.out.println("Damaging entities and players...");
			for (Entity e : entities) {
				if (entities.size() > 0) {
					((LivingEntity) e).damage(8);
				}
			}
			
			despawnMine (b);
			
			System.out.println("Done.");
			
			
		}
	}**/
}
