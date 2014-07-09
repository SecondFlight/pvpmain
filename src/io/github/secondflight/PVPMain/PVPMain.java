package io.github.secondflight.PVPMain;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.secondflight.util.DescriptionHandler;
import io.github.secondflight.util.ItemUtil;
import io.github.secondflight.abilities.*;

public class PVPMain extends JavaPlugin implements Listener {
	public final Logger logger = Logger.getLogger("Minecraft");
	public static PVPMain plugin;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " has been disabled.");
	
		//saveConfig();
	}
	
	Aggressive agg;
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " has been Enabled.");
		
		PluginManager pm = getServer().getPluginManager();
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new Aggressive(this), this);
		getServer().getPluginManager().registerEvents(new Invisibility(this), this);
		getServer().getPluginManager().registerEvents(new FireBreath(this), this);
		getServer().getPluginManager().registerEvents(new LandMine(this), this);
		
		PVPMain plugin;
		agg = new Aggressive(this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
	
	}
	
	
	
		
		@EventHandler
		public void chatEvent (AsyncPlayerChatEvent event) {
			event.setCancelled(true);
			event.getPlayer().getServer().broadcastMessage("[lvl " + getConfig().get("players." + event.getPlayer().getUniqueId().toString() + ".level") + "] <" + event.getPlayer().getDisplayName() + "> " + event.getMessage());
			//event.setMessage("[lvl " + getConfig().get("player." + event.getPlayer().getUniqueId().toString() + ".level") + "]" + event.getMessage());
		}
	

	
	// when a player logs in
	@EventHandler
	private void playerJoinListener (PlayerJoinEvent event) {
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
		
	saveConfig();
	
	
	}
	
	
	
	// Commands
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (commandLabel.equalsIgnoreCase("test") && args.length == 2)
		{
			Player player = (Player) sender;
		
		
			ItemUtil iu = new ItemUtil();
			//iu.test(new ItemStack (Material.DIAMOND_SWORD), player, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
			iu.CreateWeapon(new ItemStack (Material.DIAMOND_SWORD), player, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		
			//player.sendMessage(Integer.toString(DescriptionHandler.randomNumber(1, 2)));
		}
		
		return false;
	}	
	
	
	int task1;
	
	
	
	
	
	
	
	
}


