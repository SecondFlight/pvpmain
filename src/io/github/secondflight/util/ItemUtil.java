package io.github.secondflight.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.secondflight.util.DescriptionHandler;

public class ItemUtil {
	/**
	 * WEAPONS
	 * 
	 * Sharp things
	 * 
	 * Dagger: 
	 * Special Property: Backstab
	 * Does less damage otherwise, really good daggers can one-shot with a backstab
	 * 
	 * Longsword:
	 * Special Property: None
	 * Of the normal weapons, in general it has higher attack damage but isn't as good at blocking.
	 * 
	 * Broadsword:
	 * Special Property: none really, but it's good at blocking
	 * good at blocking, less good at damaging
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public void test(ItemStack i, Player p, int level, int rarity) {
		p.sendMessage("works");
	}
	
	// Used to create a random weapon. This method would be used in the main class when the player obtains a new Item
	public void CreateWeapon(ItemStack i, Player p, int level, int rarity) 
	{
		ItemStack item = i;
		DescriptionHandler dh = new DescriptionHandler();
		dh.setBlankAbilities(item);
		dh.setDamageLowBound(1, item);
		dh.setDamageHighBound(10, item);
		
		
		dh.setRandomAggressive(item, rarity);
		dh.setRandomPassive(item, rarity);
		
		
		
		
		NameGenerator ng = new NameGenerator();
		
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ng.createName(item, level, rarity, "Sword", dh.getAggressiveAbility(item)));
		item.setItemMeta(meta);
		
		
		p.getWorld().dropItemNaturally(p.getLocation(), item);
		
		p.sendMessage(dh.getAggressiveAbility(item));
		p.sendMessage(Integer.toString(dh.getAggressiveAbilityLevel(item)));
		p.sendMessage(dh.getPassiveAbility(item));
		p.sendMessage(Integer.toString(dh.getPassiveAbilityLevel(item)));
		p.sendMessage(Integer.toString(dh.getDamageLowBound(item)));
		p.sendMessage(Integer.toString(dh.getDamageHighBound(item)));
		
	}
	
	// Used to create a random piece of armor. This method would be used in the main class when the player obtains a new Item
	public void CreateArmor(ItemStack i, PassiveAbilities pa) 
	{
		
	}
}

//Internal Class for Aggressive Abilities
class AggressiveAbilities implements Listener
{
	/**
	 * List of aggressive abilities for melee weapons, along with descriptions, their rarity, the lowest level they can appear at, and if they've been implemented yet.
	 * Add any abilities you think of below and we can add them one at a time.
	 * 
	 * NOTE: Anything marked TBD is to be decided later, once we have more of this figured out.
	 * 
	 * Backstab: Not implemented
	 * Description: Does higher damage when the player is standing behind. Maybe can do one-shot on very rare items?
	 * Rarity: rare
	 * Lowest level: 1 (?)
	 * 
	 * Summon Wither Skulls: Not implemented
	 * Description: Summons wither skulls with a cooldown.
	 * Rarity: very rare
	 * Lowest level: TBD
	 * 
	 * Critical: Not implemented
	 * Description: MOVED TO PASSIVE ABILITIES. If this should be something different, you can change it :)
	 * 
	 * Mystical: Not implemented
	 * Description: not sure, please fill this out pizazzle
	 * Rarity: ?
	 * Lowest level: TBD
	 * 
	 * Steal Health: Not implemented
	 * Description: On each hit, the player has a chance to steal some health from their victim
	 * Rarity: common
	 * Lowest level: TBD
	 * 
	 */
	
	/**
	 * List of aggressive abilities for bows:
	 * 
	 * Teleport: Not implemented
	 * Description: If the player holds down right click for long enough, the arrow becomes a teleporting arrow and the player will teleport to where the player lands.
	 * Rarity: rare
	 * Lowest level: TBD
	 * 
	 */
	
	// I think that some abilities should probably unlock at higher levels. A level 1 player probably shouldn't
	// be able to summon wither skulls, for example. Also, it would probalby work better if some abilities
	// could only be gotten on the rarer items.
	
	//List of all Available abilities
	static String[] list = {"Backstab", "Summon Wither Skulls", "Critical", "Mystical"};
	
	static Random r = new Random();
	
	// The index variable is the number that the Random instance got
	static int index = r.nextInt(list.length);
	
	//Picked item is the String that was picked
	static String pickedItem = list[index];
}


//Internal Class for Passive Abilities
class PassiveAbilities 
{
	/**
	 * List of passive abilities for weapons, along with descriptions, their rarity, the lowest level they can appear at, and if they've been implemented yet.
	 * Add your ideas below :)
	 * 
	 * Critical: Not implemented
	 * Description: Increases critical hit chance
	 * Rarity: common
	 * Lowest level: 1
	 * 
	 * 
	 */
	
	
	
}









// creates a random name based on inputs
class NameGenerator
{
	public static int randomNumber(int lower, int upper) {
		return lower + (int)(Math.random() * ((upper - lower) + 1));
	}
	
	public String createName (ItemStack item, int itemLevel, int rarityLevel, String itemType, String aggAbility) {
		
		String rarity = new String("");
		String passiveAbility = new String("");
		//String itemTitle = new String("");
		String aggressiveAbility = new String("");
		
		if (rarityLevel == 1) {
			if (itemLevel > 0 && itemLevel < 10) {	
				String[] nameList = {"Pretty Normal", "Normal", "Boring", "Musty", "Moldy", "Plain", "Useless",
										"Broken", "Green", "Dull", "Somewhat Shiny",
									 	"Cracked", "Mundane", "Wasted", "Overused", "Well-Loved",
									 	"Plastic", "Toy", "Dreadful", "Stabby", "Pathetic", "Cheap", "Old"};
				rarity = nameList[NameGenerator.randomNumber(0, nameList.length - 1)];
			}
			
			else if (itemLevel > 9 && itemLevel < 40) {
				String[] nameList = {"Decent", "Chipped", "Probably Useful", "Such", "Special", "Pickled",
										"Pretty", "Rainbow", "Pretty Decent", "Normal", "Beautiful"};
				rarity = nameList[NameGenerator.randomNumber(0, nameList.length - 1)];
			}
			
			else if (itemLevel > 39) {
				String[] nameList = {"Pretty Nice", "Collectible", "Used, Like New", "Sparkling", "Magical", "Gas-Powered",
										"Fruit-Flavored", "Expensive", 
										"Well-Kept"};
				rarity = nameList[NameGenerator.randomNumber(0, nameList.length - 1)];
			}
			
		} else if (rarityLevel == 2) {
			
			
			String[] nameList = {"Nice", "Handy", "Glowing", "Magical", "Antique", "Beautiful", "Reflective",
								"Frivolous", "Spectacular"};
			rarity = nameList[NameGenerator.randomNumber(0, nameList.length - 1)];
			
			
		} else if (rarityLevel == 3) {
			
			String[] nameList = {"Shimmering", "Sparkling", "Glowing", "Special", "Powerful", "Woeful", "Worthy"};
			rarity = nameList[NameGenerator.randomNumber(0, nameList.length - 1)];
			
			
		} else if (rarityLevel == 4) {
			
			String[] nameList = {"Surging", "Overpowering", "Epic", "Orcish", "Legendary", "Crystal-clear", "Aincient",
								"Radient", "Flowering"};
			rarity = nameList[NameGenerator.randomNumber(0, nameList.length - 1)];
			
		}
		
		
		/**if	 (itemType.equalsIgnoreCase("sword") ||
				itemType.equalsIgnoreCase("knife") ||
				itemType.equalsIgnoreCase("longsword") ||
				itemType.equalsIgnoreCase("dagger") ||
				itemType.equalsIgnoreCase("broadsword")
				) 
		{
			
		}*/
		
		
		return new String ("[Level " + itemLevel + "] " + rarity + " " + itemType + " " + aggressiveAbility);
	}
}





