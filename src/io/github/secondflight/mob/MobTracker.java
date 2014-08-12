package io.github.secondflight.mob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import io.github.secondflight.mob.CustomMob;

public class MobTracker implements Listener {
	private final Plugin plugin;
	
	public MobTracker (Plugin plugin) {
		this.plugin = plugin;
	}
	
	

	public static List<CustomMob> mobList = new ArrayList();
	
	public static void newTracker (CustomMob mob) {
		mobList.add(mob);
	}
	
	@EventHandler
	public void entityDeath (EntityDeathEvent event) {
		
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) event.getEntity();
			
			for (CustomMob mob : mobList) {
				if (mob.getLivingEntity().equals(livingEntity)) {
					mobList.remove(mobList.indexOf(mob));
					break;
				}
			}
		}
	}
}

class Tracker {
	Tracker (LivingEntity entity) {
		
	}
}

