package io.github.secondflight.abilities;

import java.awt.Event;

import io.github.secondflight.util.DescriptionHandler;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class LandMine implements Listener{
	private final Plugin plugin;
	
	public LandMine (Plugin plugin) {
		this.plugin = plugin;
	}
	
	DescriptionHandler dh = new DescriptionHandler();
	
	@EventHandler
	public void clickEvent (PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player p = event.getPlayer();
			ItemStack i = p.getItemInHand();
			
			if (dh.hasAggressiveAbility(i, "Land Mines")) {
				newLandMine (p, event.getClickedBlock());
			}
		}
	}
	
	public void newLandMine (Player p, Block b) {
		
	}
}
