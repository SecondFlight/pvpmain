package io.github.secondflight.mob;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import io.github.secondflight.mob.MobTracker;

public class CustomMob {
	public String name;
	public LivingEntity entity;
	
	/**
	 * 
	 * @param name
	 * The name of the mob. Will be displayed above its head.
	 * 
	 * @param spawnLocation
	 * The location the mob will be spawned at.
	 * 
	 * @param type
	 * The EntityType for the mob. Must be a LivingEntity.
	 * 
	 *  
	 */
	public CustomMob (String name, Location spawnLocation, EntityType type) {
		
		
		this.name = name;
		
		
		Entity e = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, type);
		if (e instanceof LivingEntity) {
			this.entity = (LivingEntity) e;
		} else {
			throw new IllegalArgumentException("Tried to create a CustomMob object with an invalid entity type.");
		}
		
		
		
		entity.setCustomName(name);
		entity.setCustomNameVisible(true);
		
		
	}
	
	public String getName() {
		return name;
	}
	
	public LivingEntity getLivingEntity() {
		return entity;
	}

	
}


