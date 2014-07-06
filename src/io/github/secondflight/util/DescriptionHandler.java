package io.github.secondflight.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DescriptionHandler
{
	
	
	private static int damagePos = 0;
	private static int aggressiveAbilityPos = 1;
	private static int passiveAbilityPos = 2;
	
	/**
	* internal method, replaces the lore of the item with holders for various things
	* WARNING: REPLACES LORE, MUST BE RUN AS A FIRST STEP!
	*/
	
	public void setBlankAbilities(ItemStack i) {
		ItemMeta m = i.getItemMeta();
		
		List<String> lore = new ArrayList<String>();
		lore.add("Damage: 0 - 0");
		lore.add("Aggressive Ability: ");
		lore.add("Passive Ability: ");
		m.setLore(lore);
		i.setItemMeta(m);
		
	}
	
	
	// returns the aggressive ability
	public String getAggressiveAbility (ItemStack i) {
		ItemMeta m = i.getItemMeta();
		String abilityLine = m.getLore().get(aggressiveAbilityPos);
		
		String ability = abilityLine.substring(20, abilityLine.length()-2);
		return ability;
		
		
	}
	
	// returns the aggressive ability level
	public int getAggressiveAbilityLevel (ItemStack i) {
		ItemMeta m = i.getItemMeta();
		String abilityLine = m.getLore().get(aggressiveAbilityPos);
			
		int level = Integer.parseInt(abilityLine.substring(abilityLine.length()-1));
		return level;
	}
	
	
	// returns true if the item has the aggressive ability
		public boolean hasAggressiveAbility (ItemStack i, String aggressiveAbility) {
			System.out.println("Checking if the player has " + aggressiveAbility + "...");
			
			if (!(i == null)) {
				ItemMeta m = i.getItemMeta();
				System.out.println("The item's meta has been assigned to m.");
			
			
				if (m.hasLore() && m.getLore().size() >= aggressiveAbilityPos) {
					System.out.println("The lore line that should contain the aggressive ability is not null.");
				
					String abilityLine = m.getLore().get(aggressiveAbilityPos);
					System.out.println("That lore line has been assigned to string abilityLine.");
			
				
					String ability = abilityLine.substring(20, abilityLine.length()-2);
					System.out.println("A substring of abilityLine has been assigned to ability");
				
					if (ability.equalsIgnoreCase(aggressiveAbility)) {
						System.out.println("The item has " + aggressiveAbility + ".");
						return true;
					}
				}
			}
			
			System.out.println("The item does not have " + aggressiveAbility + ".");
			return false;
			
			
		}
	
	// sets the aggressive ability
	public void setAggressiveAbility (String s, int level, ItemStack i) {
		ItemMeta m = i.getItemMeta();
		String abilityLine = m.getLore().get(aggressiveAbilityPos);
		
		
			String ability = "Aggressive Ability: " + s + " " + level;
			List<String> lore = m.getLore();
			lore.set(aggressiveAbilityPos, ability);
			m.setLore(lore);
			i.setItemMeta(m);
			return;
		
	}
	
	// returns the passive ability
	public String getPassiveAbility (ItemStack i) {
		ItemMeta m = i.getItemMeta();
		String abilityLine = m.getLore().get(passiveAbilityPos);
		
		String ability = abilityLine.substring(17, abilityLine.length()-2);
		return ability;
	}
	
	// returns the passive ability level
		public int getPassiveAbilityLevel (ItemStack i) {
			ItemMeta m = i.getItemMeta();
			String abilityLine = m.getLore().get(passiveAbilityPos);
				
			int level = Integer.parseInt(abilityLine.substring(abilityLine.length()-1));
			return level;
		}
		
	// returns true if the item has the passive ability
			public boolean hasPassiveAbility (ItemStack i, String passiveAbility) {
				if (!(i == null)) {
					ItemMeta m = i.getItemMeta();
					if (m.hasLore() && m.getLore().size() >= passiveAbilityPos) {
						String abilityLine = m.getLore().get(passiveAbilityPos);
				
					
						String ability = abilityLine.substring(20, abilityLine.length()-2);
						if (ability.equalsIgnoreCase(passiveAbility)) {
							return true;
						}
					}
				}
				
				return false;
				
					
					
			}
	
	// sets the passive ability
	public void setPassiveAbility (String s, int level, ItemStack i) {
		
		ItemMeta m = i.getItemMeta();
		String abilityLine = m.getLore().get(passiveAbilityPos);
		
		String ability = "Passive Ability: " + s + " " + level;
		List<String> lore = m.getLore();
		lore.set(passiveAbilityPos, ability);
		m.setLore(lore);
		i.setItemMeta(m);
		return;
	}
	
	// returns the low bound for damage
	public int getDamageLowBound (ItemStack i) {
		ItemMeta m = i.getItemMeta();
		String abilityLine = m.getLore().get(damagePos);
		
		int damage = Integer.valueOf(abilityLine.substring(8, 9));
		return damage;
	}
	
	// reutrns the high bound for damage
	public int getDamageHighBound (ItemStack i) {
		ItemMeta m = i.getItemMeta();
		String abilityLine = m.getLore().get(damagePos);
		
		int damage = Integer.valueOf(abilityLine.substring(12, 13));
		return damage;
		
	}
	
	// sets the low bound for damage
	public void setDamageLowBound (int in, ItemStack i) {
		ItemMeta m = i.getItemMeta();
		String oldDamageLine = m.getLore().get(damagePos);
		
		String newDamageLine = "Damage: " + in + " - " + oldDamageLine.substring(12, 13);
		List<String> lore = m.getLore();
		lore.set(damagePos, newDamageLine);
		m.setLore(lore);
		i.setItemMeta(m);
		return;
		
	}
	
	// sets the high bound for damage
	public void setDamageHighBound (int in, ItemStack i) {
		ItemMeta m = i.getItemMeta();
		String oldDamageLine = m.getLore().get(damagePos);
		
		
		String newDamageLine = "Damage: " + oldDamageLine.substring(8, 9) + " - " + in;
		List<String> lore = m.getLore();
		lore.set(damagePos, newDamageLine);
		m.setLore(lore);
		i.setItemMeta(m);
		return;
		
	}
	
	
	
	
	
	
	
	
	
	public void setRandomAggressive (ItemStack i, int rarity) {
		String randAggressive = aggressiveList[randomNumber(0, aggressiveList.length - 1)];
		String randAggressiveRare = aggressiveListRare[randomNumber(0, aggressiveListRare.length - 1)];
		
		if (rarity == 1) {
			int randomNum = randomNumber (1, 3);
			
			if (randomNum == 1 || randomNum == 2) {
				this.setAggressiveAbility(randAggressive, 1, i);
			} else if (randomNum == 3) {
				this.setAggressiveAbility(randAggressive, 2, i);
			}
		}
		
		if (rarity == 2) {
			int randomNum = randomNumber(1, 2);
			
			if (randomNum == 1) {
				this.setAggressiveAbility(randAggressive, 1, i);
			} else if (randomNum == 2) {
				this.setAggressiveAbility(randAggressive, 2, i);
			}
		}
		
		if (rarity == 3) {
			
			int randomNumRare = randomNumber(1, 2);
			
			
			if (randomNumRare == 1) {
				int randomNum = randomNumber(1, 5);
				
				if (randomNum == 1) {
					this.setAggressiveAbility(randAggressive, 2, i);
				
				} else if (randomNum == 2 || randomNum == 3) {
					this.setAggressiveAbility(randAggressive, 3, i);
				
				} else if (randomNum == 4 || randomNum == 5) {
					this.setAggressiveAbility(randAggressive, 4, i);
				}
			} else if (randomNumRare == 2) {
				int randomNum = randomNumber(1, 2);
				
				if (randomNum == 1) {
					this.setAggressiveAbility(randAggressiveRare, 1, i);
				
				} else if (randomNum == 2) {
					this.setAggressiveAbility(randAggressiveRare, 2, i);
				}
			}
		}

		if (rarity == 4) {

			int randomNumRare = randomNumber(1, 2);
			
			if (randomNumRare == 1) {
				int randomNum = randomNumber(1, 2);
				
				if (randomNum == 1) {
					this.setAggressiveAbility(randAggressive, 4, i);
				
				} else if (randomNum == 2) {
					this.setAggressiveAbility(randAggressive, 5, i);
				}
			} else if (randomNumRare == 2) {
				int randomNum = randomNumber(1, 2);
				
				if (randomNum == 1) {
					this.setAggressiveAbility(randAggressiveRare, 2, i);
				
				} else if (randomNum == 2) {
					this.setAggressiveAbility(randAggressiveRare, 3, i);
				}
			}
		}		
	}
	
	public void setRandomPassive (ItemStack i, int rarity) {
		
		String randPassive = passiveList[randomNumber(0, passiveList.length - 1)];
		
		if (rarity == 1) {
			int randomNum = randomNumber (1, 3);
			
			if (randomNum == 1 || randomNum == 2) {
				this.setPassiveAbility(randPassive, 1, i);
			} else if (randomNum == 3) {
				this.setPassiveAbility(randPassive, 2, i);
			}
		}
		
		if (rarity == 2) {
			int randomNum = randomNumber(1, 2);
			
			if (randomNum == 1) {
				this.setPassiveAbility(randPassive, 1, i);
			} else if (randomNum == 2) {
				this.setPassiveAbility(randPassive, 2, i);
			}
		}
		
		if (rarity == 3) {
			
			
			
			
			int randomNum = randomNumber(1, 5);
				
			if (randomNum == 1) {
				this.setPassiveAbility(randPassive, 2, i);
				
			} else if (randomNum == 2 || randomNum == 3) {
				this.setPassiveAbility(randPassive, 3, i);
				
			} else if (randomNum == 4 || randomNum == 5) {
				this.setPassiveAbility(randPassive, 4, i);
			}
			
		}

		if (rarity == 4) {
			
			int randomNum = randomNumber(1, 2);
				
			if (randomNum == 1) {
				this.setPassiveAbility(randPassive, 4, i);
				
			} else if (randomNum == 2) {
				this.setPassiveAbility(randPassive, 5, i);
			}
			
		}		
	}
	
	
	
	public static String[] aggressiveList = {"Health Steal", "Fire Breath"};
	
	public static String[] aggressiveListRare = {"Invisibility"};
	
	public static String[] passiveList = {"Regen"};
	



	public static int randomNumber(int lower, int upper) {
		return lower + (int)(Math.random() * ((upper - lower) + 1));
	}

}

