package io.github.secondflight.player;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ExperienceHandler {
	public final Plugin plugin;
	
	public ExperienceHandler (Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void setLevel (Player p, int level) {
		plugin.getConfig().set("players." + p.getUniqueId().toString() + ".level", level);
		plugin.saveConfig();
	}
}
