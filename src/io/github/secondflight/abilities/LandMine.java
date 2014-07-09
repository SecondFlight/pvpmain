package io.github.secondflight.abilities;

import io.github.secondflight.util.DescriptionHandler;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class LandMine implements Listener{
	private final Plugin plugin;
	
	public LandMine (Plugin plugin) {
		this.plugin = plugin;
	}
	
	DescriptionHandler dh = new DescriptionHandler();
	
	
}
