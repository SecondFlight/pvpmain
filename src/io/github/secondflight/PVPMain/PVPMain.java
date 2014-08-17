package io.github.secondflight.PVPMain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.secondflight.mob.CustomMob;
import io.github.secondflight.mob.MobTracker;
import io.github.secondflight.player.DamageHandler;
import io.github.secondflight.player.ExperienceHandler;
import io.github.secondflight.util.BukkitSerialization;
import io.github.secondflight.util.ItemUtil;
import io.github.secondflight.abilities.*;

public class PVPMain extends JavaPlugin implements Listener {
	public final Logger logger = Logger.getLogger("Minecraft");
	public static PVPMain plugin;
	//public static Plugin alsoAPlugin;
	
	@Override
	public void onDisable() {
		try {
			saveVaults();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " has been disabled.");
	
		//saveConfig();
	}
	
	Aggressive agg;
	
	public static Map<Player, Inventory> vaultMap = new HashMap<Player, Inventory>();
	
	@Override
	public void onEnable() {
		plugin = this;
		
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " has been Enabled.");
		
		if (Bukkit.getServer().getOnlinePlayers().length > 0) {
			try {
				loadVaults();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//alsoAPlugin = this;
		
		//PluginManager pm = getServer().getPluginManager();
		
		getServer().getPluginManager().registerEvents(this, this);
		
		getServer().getPluginManager().registerEvents(new Aggressive(this), this);
		
		getServer().getPluginManager().registerEvents(new Invisibility(this), this);
		getServer().getPluginManager().registerEvents(new FireBreath(this), this);
		getServer().getPluginManager().registerEvents(new LandMine(this), this);
		getServer().getPluginManager().registerEvents(new Lightning(this), this);
		getServer().getPluginManager().registerEvents(new Slash(this), this);
		
		getServer().getPluginManager().registerEvents(new DamageHandler(this), this);
		
		getServer().getPluginManager().registerEvents(new MobTracker(this), this);
		
		PVPMain plugin;
		agg = new Aggressive(this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
	
	}
	
	
	
		
		@EventHandler
		public void chatEvent (AsyncPlayerChatEvent event) {
			event.setCancelled(true);
			event.getPlayer().getServer().broadcastMessage("[lv. " + getConfig().get("players." + event.getPlayer().getUniqueId().toString() + ".level") + "] <" + event.getPlayer().getDisplayName() + "> " + event.getMessage());
			//event.setMessage("[lvl " + getConfig().get("player." + event.getPlayer().getUniqueId().toString() + ".level") + "]" + event.getMessage());
		}
	

	
	// when a player logs in
	@EventHandler
	private void playerJoinListener (PlayerJoinEvent event) throws IOException {
		// teleport the player to spawn
		//event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
		
		String uuid = event.getPlayer().getUniqueId().toString();
		
		// if this is their first time
		if (this.getConfig().get("players." + uuid) == null) {
			event.getPlayer().sendMessage(ChatColor.GOLD + "Hi, and welcome!");
			event.getPlayer().sendMessage(ChatColor.YELLOW + "It looks like this is your first time");
			event.getPlayer().sendMessage(ChatColor.YELLOW + "playing this gamemode. If you need help,");
			event.getPlayer().sendMessage(ChatColor.YELLOW + "(wutever you do goes here lel)");
			//TODO: tutorial system of some sort
			
			// might not be needed, we'll see
			
			event.getPlayer().getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
			event.getPlayer().getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
			
			event.getPlayer().getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
			
			
			
			//ALL CONFIG ENTRIES GO HERE
			
			
			this.getConfig().set("players." + uuid, "");
			
			
		// otherwise, if the player is already listed in the config	
		} else {

			event.getPlayer().sendMessage(ChatColor.GOLD + "Welcome back!");
			event.getPlayer().sendMessage(ChatColor.GRAY + "If you've forgotten how to play, you can");
			event.getPlayer().sendMessage(ChatColor.GRAY + "always take a peek at the tutorial. :)");
			event.getPlayer().sendMessage(ChatColor.GRAY + "Have fun!");
			
		}
		
		/**
		 * The following code checks for all the config entries. Any entries you want in the config
		 * can be added in this format. No need to do saveConfig(), as this is done at the very end.
		 */
		
		//Player level
		String level = "players." + uuid + ".level";
		if (this.getConfig().get(level) == null) {
			this.getConfig().set(level, 1);
		}
		
		//vault
		
		if (this.getConfig().get("players." + uuid + ".vault") == null) {
			Inventory inv = Bukkit.createInventory(null, 54, event.getPlayer().getName() + "'s vault");
			this.getConfig().set("players." + uuid + ".vault", BukkitSerialization.toBase64(inv));
			vaultMap.put(event.getPlayer(), inv);
		} else {
			Inventory inv = BukkitSerialization.fromBase64(this.getConfig().getString("players." + uuid + ".vault"));
			vaultMap.put(event.getPlayer(), inv);
		}
		
		
		
		saveConfig();
	
	
	}
	
	@EventHandler
	private void playerLeaveListener (PlayerQuitEvent event) {
		this.getConfig().set("players." + event.getPlayer().getUniqueId() + ".vault", BukkitSerialization.toBase64(vaultMap.get(event.getPlayer())));
		this.saveConfig();
		
		vaultMap.remove(event.getPlayer());
	}
	
	public  void saveVaults() throws IOException {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			this.getConfig().set("players." + p.getUniqueId().toString() + ".vault", BukkitSerialization.toBase64(vaultMap.get(p)));
		}
	}
	
	public void loadVaults() throws IOException {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			vaultMap.put(p, BukkitSerialization.fromBase64(this.getConfig().get("players." + p.getUniqueId().toString() + ".vault").toString()));
		}
	}
	
	// Commands
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (sender instanceof Player) {	
			Player player = (Player) sender;
			if (commandLabel.equalsIgnoreCase("item") && args.length == 2)
			{
				
		
		
				ItemUtil iu = new ItemUtil();
				//iu.test(new ItemStack (Material.DIAMOND_SWORD), player, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
				iu.CreateWeapon(new ItemStack (Material.DIAMOND_SWORD), player, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		
				//player.sendMessage(Integer.toString(DescriptionHandler.randomNumber(1, 2)));
			}
		
			if (commandLabel.equalsIgnoreCase("pvpmain")) {
				if (args.length == 0) {
					player.sendMessage("-----------------------------------------------------");
					player.sendMessage("/pvpmain setlevel [player] [number]");
					player.sendMessage("        -- Sets the level of the player.");
					player.sendMessage("/pvpmain spawn [mob type] [name]");
					player.sendMessage("        -- Spawns a mob of the given type with the given custom name.");
					player.sendMessage("/pvpmain trackedlist");
					player.sendMessage("        -- Lists the currently tracked mobs and their basic info.");
					player.sendMessage("-----------------------------------------------------");
				} else if (args.length >= 1 && args[0].equalsIgnoreCase("setlevel")) {
					if (args.length < 2 || args.length > 3) {
						player.sendMessage(ChatColor.RED + "Invalid arguments.");
						player.sendMessage(ChatColor.RED + "/pvpmain setlevel [player] [number]");
					} else if (args.length == 3) {
						boolean displayError = true;
						for (Player p : player.getServer().getOnlinePlayers()) {
							
							if (p.getDisplayName().equalsIgnoreCase(args[1])) {
								ExperienceHandler eh = new ExperienceHandler(plugin);
								eh.setLevel(p, Integer.parseInt(args[2]));
								if (p.getUniqueId().equals(player.getUniqueId())) {
									player.sendMessage("Your level has been set to " + Integer.parseInt(args[2]) + ".");
								} else {
									player.sendMessage(p.getDisplayName() + "'s level has been set to " + Integer.parseInt(args[2]) + ".");
								}
								displayError = false;
							}
							
						}
						if (displayError == true) {
							player.sendMessage(ChatColor.RED + "That player does not exist or is not online.");
						}
					} else if (args.length == 2) {
						int level = Integer.parseInt(args[1]);
						
						ExperienceHandler eh = new ExperienceHandler(plugin);
						eh.setLevel(player, Integer.parseInt(args[1]));
						
						//setLevel(player, level);
						
						player.sendMessage("Your level has been set to " + level + ".");
					}
				} else if (args.length > 2 && args[0].equalsIgnoreCase("spawn")) {
					EntityType type = null;
					
					if (args[1].equalsIgnoreCase("zombie")) {
						type = EntityType.ZOMBIE;
					} else if (args[1].equalsIgnoreCase("skeleton")) {
						type = EntityType.SKELETON;
					} else if (args[1].equalsIgnoreCase("creeper")) {
						type = EntityType.CREEPER;
					} else if (args[1].equalsIgnoreCase("spider")) {
						type = EntityType.SPIDER;
					} else if (args[1].equalsIgnoreCase("cavespider")) {
						type = EntityType.CAVE_SPIDER;
					} else if (args[1].equalsIgnoreCase("silverfish")) {
						type = EntityType.SILVERFISH;
					} else if (args[1].equalsIgnoreCase("enderman")) {
						type = EntityType.ENDERMAN;
					} else if (args[1].equalsIgnoreCase("pig")) {
						type = EntityType.PIG;
					} else if (args[1].equalsIgnoreCase("sheep")) {
						type = EntityType.SHEEP;
					} else if (args[1].equalsIgnoreCase("cow")) {
						type = EntityType.COW;
					} else if (args[1].equalsIgnoreCase("mooshroom")) {
						type = EntityType.MUSHROOM_COW;
					} else if (args[1].equalsIgnoreCase("wolf") || args[1].equalsIgnoreCase("dog")) {
						type = EntityType.WOLF;
					} else if (args[1].equalsIgnoreCase("ocelot")) {
						type = EntityType.OCELOT;
					} else if (args[1].equalsIgnoreCase("villager")) {
						type = EntityType.VILLAGER;
					} else if (args[1].equalsIgnoreCase("slime")) {
						type = EntityType.SLIME;
					} else {
						player.sendMessage(ChatColor.RED + args[1] + " is not a recognized mob name.");
					}
					
					String mobName = "";
					for (int i = 2; i < args.length; i++) {
						mobName = mobName + args[i] + " ";
					}
					
					mobName.trim();
					
					MobTracker.newTracker(new CustomMob(mobName, player.getLocation(), type));
				}
				
				else if (args.length == 1 && args[0].equalsIgnoreCase("trackedlist")) {
					player.sendMessage("There are " + MobTracker.mobList.size() + " mobs currently being tracked.");
					
					if (MobTracker.mobList.size() > 0) {
						player.sendMessage("The following mobs are being tracked:");
						player.sendMessage("---");
						for (CustomMob mob : MobTracker.mobList) {
							player.sendMessage("Mob name: " + mob.getName());
							player.sendMessage("Mob type: " + mob.getLivingEntity().getType().name());
							player.sendMessage("---");
						}
					}
				}
				
				else if (args.length == 1 && args[0].equalsIgnoreCase("vault")) {
					player.openInventory(vaultMap.get(player));
				}
			}
		
		}	
		return false;
	
	
	
	
	
	
	
	
	
	
	
	}
}



