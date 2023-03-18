package cards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import effects.Effect;

public class CardFactory {
	public static String beastAppearances[] = {"kingfisher", "raven_egg", "sparrow", "magpie", "raven", "turkey_vulture", "stunted_wolf","wolf_cub","bloodhound","dire_wolf","wolf"};
	
	public static String robotAppearances[] = {"xformerbatbot","s0n1a","xformerporcupinebot","qu177","xformergrizzlybot","gr1zz","bomb_latcher","exeskeleton","shield_latcher","skel_e_latcher"};
	
	public static String undeadAppearances[] = {"armored_zombie","bone_lord","bone_lords_horn","bone_pile","bone_prince","dead_hand","dead_pets","draugr"};
	
	public static String wizardAppearances[] = {"alchemist","amalgam_wiz"};
	
	public static Card mainCard(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u0, String type) throws IOException {
		int u = u0;
		
		int levelRarity = 0;
		
		while (u%10 == 9) {
			u = (u * multiplicator)%modulo;
			levelRarity++;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		if (type.equals("beast")) {
			return beastCard(modulo, multiplicator, globalStrengh, rarityStrengh, u, levelRarity);
		} else if (type.equals("robot")) {
			return robotCard(modulo, multiplicator, globalStrengh, rarityStrengh, u, levelRarity);
		} else if (type.equals("undead")) {
			return undeadCard(modulo, multiplicator, globalStrengh, rarityStrengh, u, levelRarity);
		}
		
		return wizardCard(modulo, multiplicator, globalStrengh, rarityStrengh, u, levelRarity);
	}
	
	public static Card sourceCard(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u0, String type) throws IOException {
		int u = u0;
		
		int levelRarity = 0;
		
		while (u%10 == 9) {
			u = (u * multiplicator)%modulo;
			levelRarity++;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		if (type.equals("beast")) {
			return beastCardSource(modulo, multiplicator, globalStrengh, rarityStrengh, u, levelRarity);
		} else if (type.equals("robot")) {
			return robotCardSource(modulo, multiplicator, globalStrengh, rarityStrengh, u, levelRarity);
		} else if (type.equals("robot")) {
			return undeadCardSource(modulo, multiplicator, globalStrengh, rarityStrengh, u, levelRarity);
		}
		
		return wizardCardSource(modulo, multiplicator, globalStrengh, rarityStrengh, u, levelRarity);
	}
	
	
	private static BeastCard beastCard(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u, int levelRarity) throws IOException {
		int nbstats = 0;
		int level = 0;
		int attackmin = 0;
		String typeCost = "blood";
		if (u%4 == 3) {
			typeCost = "bone";
			u = (u * multiplicator + 2*levelRarity)%modulo;
			int lvlmax = 8;
			ArrayList<Integer> integerSeen = new ArrayList<Integer>();
			while (u%4 == 3 && !integerSeen.contains(u)) {
				integerSeen.add(u);
				u = (u * multiplicator + 2*levelRarity)%modulo;
				lvlmax++;
			}
			u = (u * multiplicator + 2*levelRarity)%modulo;
			level = 2 + u%(lvlmax-1);
			u = (u * multiplicator + 2*levelRarity)%modulo;
			nbstats = level * globalStrengh / 4 + levelRarity * rarityStrengh;
			nbstats = nbstats - u%(1+globalStrengh/8);
			u = (u * multiplicator + 2*levelRarity)%modulo;
		} else {
			int lvlmax = 2;
			u = (u * multiplicator + 2*levelRarity)%modulo;
			ArrayList<Integer> integerSeen = new ArrayList<Integer>();
			while (u%4 == 3 && !integerSeen.contains(u)) {
				integerSeen.add(u);
				u = (u * multiplicator + 2*levelRarity)%modulo;
				lvlmax++;
			}
			u = (u * multiplicator + 2*levelRarity)%modulo;
			level = 1 + u%(lvlmax);
			u = (u * multiplicator + 2*levelRarity)%modulo;
			nbstats = level * globalStrengh + levelRarity * rarityStrengh;
			nbstats = nbstats - u%(1+globalStrengh/4);
			u = (u * multiplicator + 2*levelRarity)%modulo;
			nbstats = nbstats + Math.max(0, level - 1 - u%(1+globalStrengh/4));
			u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		
		//effects
		boolean stopEffects = false;
		List<Effect> effects = new ArrayList<>();
		List<String> sortedeffects = new ArrayList<>();
		
		//effect1
		if (u%4>0) {
			String effectName = Effect.namesBeastEffects.get(u%(Effect.namesBeastEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "beast", 1));
				} else {
					effects.add(new Effect(effectName, "beast"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName)) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		
		//effect2
		if (!stopEffects) {
		if (u%4>1) {
			String effectName = Effect.namesBeastEffects.get(u%(Effect.namesBeastEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0 && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "beast", 1));
				} else {
					effects.add(new Effect(effectName, "beast"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect3
		if (!stopEffects) {
		if (u%4>2) {
			String effectName = Effect.namesBeastEffects.get(u%(Effect.namesBeastEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0 && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "beast", 1));
				} else {
					effects.add(new Effect(effectName, "beast"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//levels effects
		for (int i=0;i<effects.size();i++) {
			Effect effect = effects.get(i);
			if (Effect.namesLevelEffects.contains(effect.getName())) {
				if (Effect.namesResourceEffects.contains(effect.getName())) {
					int lvlmaxEffect = 1;
					int lvlmaxMaxEffect = 1 + nbstats/effect.getCostStats();
					ArrayList<Integer> integerSeen = new ArrayList<Integer>();
					while (u%4 == 3 && !integerSeen.contains(u) && lvlmaxEffect < lvlmaxMaxEffect) {
						integerSeen.add(u);
						u = (u * multiplicator + 2*levelRarity)%modulo;
						lvlmaxEffect++;
					}
					u = (u * multiplicator + 2*levelRarity)%modulo;
					int levelEffect = 1 + u%lvlmaxEffect;
					u = (u * multiplicator + 2*levelRarity)%modulo;
					nbstats -= (levelEffect-1)*effect.getCostStats();
					effect.setLevel(levelEffect);
				} else {
					int levelEffect = 1 + u%(1+nbstats/effect.getCostStats());
					u = (u * multiplicator + 2*levelRarity)%modulo;
					levelEffect = Math.min(levelEffect, 1 + u%(1+nbstats/effect.getCostStats()));
					u = (u * multiplicator + 2*levelRarity)%modulo;
					nbstats -= (levelEffect-1)*effect.getCostStats();
					effect.setLevel(levelEffect);
				}
			}
		}
		
		//attack and hp
		int bonusAttack = u%(1+nbstats/2);
		int attack = attackmin + bonusAttack;
		int hp = 1 + nbstats - 2*bonusAttack;
		u = (u * multiplicator + 2*levelRarity)%modulo;
		String appearance = beastAppearances[u%(beastAppearances.length)];
		
		return new BeastCard(appearance, typeCost, level, hp, attack, effects, levelRarity, true);
	}

	private static UndeadCard undeadCard(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u, int levelRarity) throws IOException {
		int nbstats = 0;
		int level = 0;
		int attackmin = 0;
		int lvlmax = 10;
		ArrayList<Integer> integerSeen = new ArrayList<Integer>();
		while (u%4 == 3 && !integerSeen.contains(u)) {
			integerSeen.add(u);
			u = (u * multiplicator + 2*levelRarity)%modulo;
			lvlmax++;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		level = 1 + u%(lvlmax-1);
		u = (u * multiplicator + 2*levelRarity)%modulo;
		level = Math.min(lvlmax, 1 + u%(lvlmax-1));
		u = (u * multiplicator + 2*levelRarity)%modulo;
		nbstats = (level+1) * globalStrengh / 4 + levelRarity * rarityStrengh;
		nbstats = nbstats - u%(1+globalStrengh/8);
		u = (u * multiplicator + 2*levelRarity)%modulo;
		
		//effects
		boolean stopEffects = false;
		List<Effect> effects = new ArrayList<>();
		List<String> sortedeffects = new ArrayList<>();
		
		//effect1
		if (u%4>0) {
			String effectName = Effect.namesUndeadEffects.get(u%(Effect.namesUndeadEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "undead", 1));
				} else {
					effects.add(new Effect(effectName, "undead"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName)) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		
		//effect2
		if (!stopEffects) {
		if (u%4>1) {
			String effectName = Effect.namesUndeadEffects.get(u%(Effect.namesUndeadEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0 && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "undead", 1));
				} else {
					effects.add(new Effect(effectName, "undead"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect3
		if (!stopEffects) {
		if (u%4>2) {
			String effectName = Effect.namesUndeadEffects.get(u%(Effect.namesUndeadEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0 && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "undead", 1));
				} else {
					effects.add(new Effect(effectName, "undead"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//levels effects
		for (int i=0;i<effects.size();i++) {
			Effect effect = effects.get(i);
			if (Effect.namesLevelEffects.contains(effect.getName())) {
				if (Effect.namesResourceEffects.contains(effect.getName())) {
					int lvlmaxEffect = 1;
					int lvlmaxMaxEffect = 1 + nbstats/effect.getCostStats();
					ArrayList<Integer> integerSeen2 = new ArrayList<Integer>();
					while (u%4 == 3 && !integerSeen2.contains(u) && lvlmaxEffect < lvlmaxMaxEffect) {
						integerSeen2.add(u);
						u = (u * multiplicator + 2*levelRarity)%modulo;
						lvlmaxEffect++;
					}
					u = (u * multiplicator + 2*levelRarity)%modulo;
					int levelEffect = 1 + u%lvlmaxEffect;
					u = (u * multiplicator + 2*levelRarity)%modulo;
					nbstats -= (levelEffect-1)*effect.getCostStats();
					effect.setLevel(levelEffect);
				} else {
					int levelEffect = 1 + u%(1+nbstats/effect.getCostStats());
					u = (u * multiplicator + 2*levelRarity)%modulo;
					levelEffect = Math.min(levelEffect, 1 + u%(1+nbstats/effect.getCostStats()));
					u = (u * multiplicator + 2*levelRarity)%modulo;
					nbstats -= (levelEffect-1)*effect.getCostStats();
					effect.setLevel(levelEffect);
				}
			}
		}
		
		//attack and hp
		int bonusAttack = u%(1+nbstats/2);
		int attack = attackmin + bonusAttack;
		int hp = 1 + nbstats - 2*bonusAttack;
		u = (u * multiplicator + 2*levelRarity)%modulo;
		String appearance = undeadAppearances[u%(undeadAppearances.length)];
		
		return new UndeadCard(appearance, level, hp, attack, effects, levelRarity, true);
	}
	
	
	private static RobotCard robotCard(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u, int levelRarity) throws IOException {
		int nbstats = 0;
		int level = 0;
		int attackmin = 0;
		int nbMox = 0;
		if (u%4 == 3) {
			//1mox
			nbMox++;
			u = (u * multiplicator + 2*levelRarity)%modulo;
			if (u%4 == 3) {
				//2mox
				nbMox++;
				u = (u * multiplicator + 2*levelRarity)%modulo;
				if (u%4 == 3) {
					//3mox
					nbMox++;
					u = (u * multiplicator + 2*levelRarity)%modulo;
				}
			}
		}
		int lvlmax = 6;
		ArrayList<Integer> integerSeen = new ArrayList<Integer>();
		while (u%4 == 3 && !integerSeen.contains(u)) {
			integerSeen.add(u);
			u = (u * multiplicator + 2*levelRarity)%modulo;
			lvlmax++;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		level = u%(lvlmax)+1;
		u = (u * multiplicator + 2*levelRarity)%modulo;
		nbstats = (2+level)*globalStrengh/4 + levelRarity*rarityStrengh;
		
		//effects
		boolean stopEffects = false;
		List<Effect> effects = new ArrayList<>();
		List<String> sortedeffects = new ArrayList<>();
		
		//effect1
		if (u%5>0) {
			String effectName = Effect.namesRobotEffects.get(u%(Effect.namesRobotEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "robot", 1));
				} else {
					effects.add(new Effect(effectName, "robot"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName)) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		
		//effect2
		if (!stopEffects) {
		if (u%5>1) {
			String effectName = Effect.namesRobotEffects.get(u%(Effect.namesRobotEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0 && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "robot", 1));
				} else {
					effects.add(new Effect(effectName, "robot"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect3
		if (!stopEffects) {
		if (u%5>2) {
			String effectName = Effect.namesRobotEffects.get(u%(Effect.namesRobotEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0 && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "robot", 1));
				} else {
					effects.add(new Effect(effectName, "robot"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect4
		if (!stopEffects) {
			if (u%5>3) {
				String effectName = Effect.namesRobotEffects.get(u%(Effect.namesRobotEffects.size()));
				if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
					stopEffects = true;
				} else if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0 && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
					stopEffects = true;
				} else {
					sortedeffects.add(effectName);
					if (Effect.namesLevelEffects.contains(effectName)) {
						effects.add(new Effect(effectName, "robot", 1));
					} else {
						effects.add(new Effect(effectName, "robot"));
					}
					nbstats -= effects.get(effects.size()-1).getCostStats();
					if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0) {
						attackmin++;
						nbstats -= 2;
					}
				}
			} else {
				stopEffects = true;
			}
			u = (u * multiplicator + 2*levelRarity)%modulo;
			}
		//levels effects
				for (int i=0;i<effects.size();i++) {
					Effect effect = effects.get(i);
					if (Effect.namesLevelEffects.contains(effect.getName())) {
						if (Effect.namesResourceEffects.contains(effect.getName())) {
							int lvlmaxEffect = 1;
							int lvlmaxMaxEffect = 1 + nbstats/effect.getCostStats();
							ArrayList<Integer> integerSeen2 = new ArrayList<Integer>();
							while (u%4 == 3 && !integerSeen2.contains(u) && lvlmaxEffect < lvlmaxMaxEffect) {
								integerSeen2.add(u);
								u = (u * multiplicator + 2*levelRarity)%modulo;
								lvlmaxEffect++;
							}
							u = (u * multiplicator + 2*levelRarity)%modulo;
							u = (u * multiplicator + 2*levelRarity)%modulo;
							int levelEffect = 1 + u%lvlmaxEffect;
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						} else {
							int levelEffect = 1 + u%(1+nbstats/effect.getCostStats());
							u = (u * multiplicator + 2*levelRarity)%modulo;
							levelEffect = Math.min(levelEffect, 1 + u%(1+nbstats/effect.getCostStats()));
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						}
					}
				}
		
		//attack and hp
		int bonusAttack = u%(1+nbstats/2);
		if (effects.stream().anyMatch(effect -> effect.getName().equals("bifurcated_strike"))) {
			bonusAttack = u%(1+nbstats/3);
		}
		int attack = attackmin + bonusAttack;
		int hp = 1 + nbstats - 2*bonusAttack;
		u = (u * multiplicator + 2*levelRarity)%modulo;
		String appearance = robotAppearances[u%(robotAppearances.length)];
		
		return new RobotCard(appearance, level, hp, attack, effects, levelRarity, true);
	}
	
	private static WizardCard wizardCard(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u, int levelRarity) throws IOException {
		int nbstats = 0;
		int level = 0;
		int attackmin = 0;
		int nbAnyMox = 0;
		int nbGreenMox = 0;
		int nbOrangeMox = 0;
		int nbBlueMox = 0;
		/*if (u%4 == 3) {
			//1mox
			nbMox++;
			u = (u * multiplicator + 2*levelRarity)%modulo;
			if (u%4 == 3) {
				//2mox
				nbMox++;
				u = (u * multiplicator + 2*levelRarity)%modulo;
				if (u%4 == 3) {
					//3mox
					nbMox++;
					u = (u * multiplicator + 2*levelRarity)%modulo;
				}
			}
		}*/
		int lvlmax = 1;
		ArrayList<Integer> integerSeen = new ArrayList<Integer>();
		while (u%4 == 3 && !integerSeen.contains(u)) {
			integerSeen.add(u);
			u = (u * multiplicator + 2*levelRarity)%modulo;
			lvlmax++;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		level = u%(lvlmax)+1;
		u = (u * multiplicator + 2*levelRarity)%modulo;
		
		if (level == 1) {
			if (u%3 == 0) {
				nbGreenMox ++;
			}
			if (u%3 == 1) {
				nbOrangeMox ++;
			}
			if (u%3 == 2) {
				nbBlueMox ++;
			}
			u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		if (level == 2) {
			if (u%2 == 0) {
				//one color
				u = (u * multiplicator + 2*levelRarity)%modulo;
				if (u%3 == 0) {
					nbGreenMox ++;
					u = (u * multiplicator + 2*levelRarity)%modulo;
					if (u%2 == 0) {
						nbAnyMox ++;
					} else {
						nbGreenMox ++;
					}
				} else if (u%3 == 1) {
					nbOrangeMox ++;
					u = (u * multiplicator + 2*levelRarity)%modulo;
					if (u%2 == 0) {
						nbAnyMox ++;
					} else {
						nbOrangeMox ++;
					}
				} else {
					nbBlueMox ++;
					u = (u * multiplicator + 2*levelRarity)%modulo;
					if (u%2 == 0) {
						nbAnyMox ++;
					} else {
						nbBlueMox ++;
					}
				}
			} else {
				//two colors
				u = (u * multiplicator + 2*levelRarity)%modulo;
				if (u%3 == 0) {
					nbGreenMox ++;
					nbOrangeMox ++;
				}
				if (u%3 == 1) {
					nbGreenMox ++;
					nbBlueMox ++;
				}
				if (u%3 == 2) {
					nbOrangeMox ++;
					nbBlueMox ++;
				}
			}
		}
		if (level>2) {
			if (u%3 == 0) {
				//one color
				u = (u * multiplicator + 2*levelRarity)%modulo;
				int colorGem = u%3;
				u = (u * multiplicator + 2*levelRarity)%modulo;
				int nbcoloredGem = 1+u%level;
				nbAnyMox += level - nbcoloredGem;
				if (colorGem == 0) {
					nbGreenMox += nbcoloredGem;
				}
				if (colorGem == 1) {
					nbOrangeMox += nbcoloredGem;
				}
				if (colorGem == 2) {
					nbBlueMox += nbcoloredGem;
				}
				u = (u * multiplicator + 2*levelRarity)%modulo;
			} else if (u%3 == 1) {
				//two colors
				u = (u * multiplicator + 2*levelRarity)%modulo;
				int absentColorGem = u%3;
				u = (u * multiplicator + 2*levelRarity)%modulo;
				int nbcoloredGem = 1+u%(level/2);
				nbAnyMox += level - 2 *nbcoloredGem;
				if (absentColorGem == 0) {
					nbOrangeMox += nbcoloredGem;
					nbBlueMox += nbcoloredGem;
				}
				if (absentColorGem == 1) {
					nbGreenMox += nbcoloredGem;
					nbBlueMox += nbcoloredGem;
				}
				if (absentColorGem == 2) {
					nbGreenMox += nbcoloredGem;
					nbOrangeMox += nbcoloredGem;
				}
				u = (u * multiplicator + 2*levelRarity)%modulo;
			} else {
				//all colors
				u = (u * multiplicator + 2*levelRarity)%modulo;
				int nbcoloredGem = 1+u%(level/3);
				nbAnyMox += level - 3 *nbcoloredGem;
				nbGreenMox += nbcoloredGem;
				nbOrangeMox += nbcoloredGem;
				nbBlueMox += nbcoloredGem;
				u = (u * multiplicator + 2*levelRarity)%modulo;
			}
		}
		
		nbstats = (2*nbAnyMox + 3*(level-nbAnyMox))*globalStrengh/4 + levelRarity*rarityStrengh;
		
		
		//effects
		boolean stopEffects = false;
		List<Effect> effects = new ArrayList<>();
		List<String> sortedeffects = new ArrayList<>();
		
		//effect1
		if (u%4>0) {
			String effectName = Effect.namesWizardEffects.get(u%(Effect.namesWizardEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "wizard", 1));
				} else {
					effects.add(new Effect(effectName, "wizard"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName)) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		
		//effect2
		if (!stopEffects) {
		if (u%4>1) {
			String effectName = Effect.namesWizardEffects.get(u%(Effect.namesWizardEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0 && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "wizard", 1));
				} else {
					effects.add(new Effect(effectName, "wizard"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect3
		if (!stopEffects) {
		if (u%4>2) {
			String effectName = Effect.namesWizardEffects.get(u%(Effect.namesWizardEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0 && Effect.mapEffectToCost.get(effectName) + 2 > nbstats) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "wizard", 1));
				} else {
					effects.add(new Effect(effectName, "wizard"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				if (Effect.namesAttackEffects.contains(effectName) && attackmin == 0) {
					attackmin++;
					nbstats -= 2;
				}
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		
		//levels effects
				for (int i=0;i<effects.size();i++) {
					Effect effect = effects.get(i);
					if (Effect.namesLevelEffects.contains(effect.getName())) {
						if (Effect.namesResourceEffects.contains(effect.getName())) {
							int lvlmaxEffect = 1;
							int lvlmaxMaxEffect = 1 + nbstats/effect.getCostStats();
							ArrayList<Integer> integerSeen2 = new ArrayList<Integer>();
							while (u%4 == 3 && !integerSeen2.contains(u) && lvlmaxEffect < lvlmaxMaxEffect) {
								integerSeen2.add(u);
								u = (u * multiplicator + 2*levelRarity)%modulo;
								lvlmaxEffect++;
							}
							u = (u * multiplicator + 2*levelRarity)%modulo;
							int levelEffect = 1 + u%lvlmaxEffect;
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						} else {
							int levelEffect = 1 + u%(1+nbstats/effect.getCostStats());
							u = (u * multiplicator + 2*levelRarity)%modulo;
							levelEffect = Math.min(levelEffect, 1 + u%(1+nbstats/effect.getCostStats()));
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						}
					}
				}
		
		//attack and hp
		int bonusAttack = u%(1+nbstats/2);
		if (effects.stream().anyMatch(effect -> effect.getName().equals("bifurcated_strike"))) {
			bonusAttack = u%(1+nbstats/3);
		}
		int attack = attackmin + bonusAttack;
		int hp = 1 + nbstats - 2*bonusAttack;
		u = (u * multiplicator + 2*levelRarity)%modulo;
		String appearance = wizardAppearances[u%(wizardAppearances.length)];
		
		return new WizardCard(appearance, nbAnyMox, nbGreenMox, nbOrangeMox, nbBlueMox, level, hp, attack, effects, levelRarity, true);
		
	}
	
	private static BeastCard beastCardSource(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u, int levelRarity) throws IOException {
		int nbstats = 0;
		String typeCost = "blood";
		nbstats = globalStrengh/8 + levelRarity * rarityStrengh;
		nbstats = nbstats - u%(1+globalStrengh/8);
		u = (u * multiplicator + 2*levelRarity)%modulo;
		//effects
		boolean stopEffects = false;
		List<Effect> effects = new ArrayList<>();
		List<String> sortedeffects = new ArrayList<>();
		
		//effect1
		if (u%4>0) {
			String effectName = Effect.namesBeastEffects.get(u%(Effect.namesBeastEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || Effect.namesAttackEffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "beast", 1));
				} else {
					effects.add(new Effect(effectName, "beast"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		
		//effect2
		if (!stopEffects) {
		if (u%4>1) {
			String effectName = Effect.namesBeastEffects.get(u%(Effect.namesBeastEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || Effect.namesAttackEffects.contains(effectName) || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "beast", 1));
				} else {
					effects.add(new Effect(effectName, "beast"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect3
		if (!stopEffects) {
		if (u%4>2) {
			String effectName = Effect.namesBeastEffects.get(u%(Effect.namesBeastEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats  || Effect.namesAttackEffects.contains(effectName) || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "beast", 1));
				} else {
					effects.add(new Effect(effectName, "beast"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//levels effects
				for (int i=0;i<effects.size();i++) {
					Effect effect = effects.get(i);
					if (Effect.namesLevelEffects.contains(effect.getName())) {
						if (Effect.namesResourceEffects.contains(effect.getName())) {
							int lvlmaxEffect = 1;
							int lvlmaxMaxEffect = 1 + nbstats/effect.getCostStats();
							ArrayList<Integer> integerSeen = new ArrayList<Integer>();
							while (u%4 == 3 && !integerSeen.contains(u) && lvlmaxEffect < lvlmaxMaxEffect) {
								integerSeen.add(u);
								u = (u * multiplicator + 2*levelRarity)%modulo;
								lvlmaxEffect++;
							}
							u = (u * multiplicator + 2*levelRarity)%modulo;
							int levelEffect = 1 + u%lvlmaxEffect;
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						} else {
							int levelEffect = 1 + u%(1+nbstats/effect.getCostStats());
							u = (u * multiplicator + 2*levelRarity)%modulo;
							levelEffect = Math.min(levelEffect, 1 + u%(1+nbstats/effect.getCostStats()));
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						}
					}
				}
		
		//attack and hp
		int hp = 1 + nbstats;
		return new BeastCard("squirrel", typeCost, 0, hp, 0, effects, levelRarity, false);
	}
	
	private static RobotCard robotCardSource(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u, int levelRarity) throws IOException {
		int nbstats = 0;
		int level = 0;
		int nbMox = 0;
		if (u%4 == 3) {
			//1mox
			nbMox++;
			u = (u * multiplicator + 2*levelRarity)%modulo;
			if (u%4 == 3) {
				//2mox
				nbMox++;
				u = (u * multiplicator + 2*levelRarity)%modulo;
				if (u%4 == 3) {
					//3mox
					nbMox++;
					u = (u * multiplicator + 2*levelRarity)%modulo;
				}
			}
		}
		level = 1;
		nbstats = (2+level)*globalStrengh/4 + levelRarity*rarityStrengh;
		
		//effects
		boolean stopEffects = false;
		List<Effect> effects = new ArrayList<>();
		List<String> sortedeffects = new ArrayList<>();
		
		//effect1
		if (u%5>0) {
			String effectName = Effect.namesRobotEffects.get(u%(Effect.namesRobotEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || Effect.namesAttackEffects.contains(effectName) || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "robot", 1));
				} else {
					effects.add(new Effect(effectName, "robot"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		
		//effect2
		if (!stopEffects) {
		if (u%5>1) {
			String effectName = Effect.namesRobotEffects.get(u%(Effect.namesRobotEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || Effect.namesAttackEffects.contains(effectName) || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "robot", 1));
				} else {
					effects.add(new Effect(effectName, "robot"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect3
		if (!stopEffects) {
		if (u%5>2) {
			String effectName = Effect.namesRobotEffects.get(u%(Effect.namesRobotEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || Effect.namesAttackEffects.contains(effectName) || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "robot", 1));
				} else {
					effects.add(new Effect(effectName, "robot"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect4
		if (!stopEffects) {
			if (u%5>3) {
				String effectName = Effect.namesRobotEffects.get(u%(Effect.namesRobotEffects.size()));
				if (Effect.mapEffectToCost.get(effectName) > nbstats || Effect.namesAttackEffects.contains(effectName) || sortedeffects.contains(effectName)) {
					stopEffects = true;
				} else {
					sortedeffects.add(effectName);
					if (Effect.namesLevelEffects.contains(effectName)) {
						effects.add(new Effect(effectName, "robot", 1));
					} else {
						effects.add(new Effect(effectName, "robot"));
					}
					nbstats -= effects.get(effects.size()-1).getCostStats();
					
				}
			} else {
				stopEffects = true;
			}
			u = (u * multiplicator + 2*levelRarity)%modulo;
			}
		//levels effects
				for (int i=0;i<effects.size();i++) {
					Effect effect = effects.get(i);
					if (Effect.namesLevelEffects.contains(effect.getName())) {
						if (Effect.namesResourceEffects.contains(effect.getName())) {
							int lvlmaxEffect = 1;
							int lvlmaxMaxEffect = 1 + nbstats/effect.getCostStats();
							ArrayList<Integer> integerSeen = new ArrayList<Integer>();
							while (u%4 == 3 && !integerSeen.contains(u) && lvlmaxEffect < lvlmaxMaxEffect) {
								integerSeen.add(u);
								u = (u * multiplicator + 2*levelRarity)%modulo;
								lvlmaxEffect++;
							}
							u = (u * multiplicator + 2*levelRarity)%modulo;
							int levelEffect = 1 + u%lvlmaxEffect;
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						} else {
							int levelEffect = 1 + u%(1+nbstats/effect.getCostStats());
							u = (u * multiplicator + 2*levelRarity)%modulo;
							levelEffect = Math.min(levelEffect, 1 + u%(1+nbstats/effect.getCostStats()));
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						}
					}
				}
		
		//hp
		int hp = 1 + nbstats;
		
		return new RobotCard("empty_vessel", level, hp, 0, effects, levelRarity, false);
	}

	private static UndeadCard undeadCardSource(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u, int levelRarity) throws IOException {
		int nbstats = 0;
		nbstats = globalStrengh/8 + levelRarity * rarityStrengh;
		nbstats = nbstats - u%(1+globalStrengh/8);
		u = (u * multiplicator + 2*levelRarity)%modulo;
		//effects
		boolean stopEffects = false;
		List<Effect> effects = new ArrayList<>();
		List<String> sortedeffects = new ArrayList<>();
		
		//effect1
		sortedeffects.add("brittle");
		effects.add(new Effect("brittle", "undead"));
		
		//effect2
		if (!stopEffects) {
		if (u%4>1) {
			String effectName = Effect.namesUndeadEffects.get(u%(Effect.namesUndeadEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "undead", 1));
				} else {
					effects.add(new Effect(effectName, "undead"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect3
		if (!stopEffects) {
		if (u%4>2) {
			String effectName = Effect.namesUndeadEffects.get(u%(Effect.namesUndeadEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "undead", 1));
				} else {
					effects.add(new Effect(effectName, "undead"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//levels effects
				for (int i=0;i<effects.size();i++) {
					Effect effect = effects.get(i);
					if (Effect.namesLevelEffects.contains(effect.getName())) {
						if (Effect.namesResourceEffects.contains(effect.getName())) {
							int lvlmaxEffect = 1;
							int lvlmaxMaxEffect = 1 + nbstats/effect.getCostStats();
							ArrayList<Integer> integerSeen = new ArrayList<Integer>();
							while (u%4 == 3 && !integerSeen.contains(u) && lvlmaxEffect < lvlmaxMaxEffect) {
								integerSeen.add(u);
								u = (u * multiplicator + 2*levelRarity)%modulo;
								lvlmaxEffect++;
							}
							u = (u * multiplicator + 2*levelRarity)%modulo;
							int levelEffect = 1 + u%lvlmaxEffect;
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						} else {
							int levelEffect = 1 + u%(1+nbstats/effect.getCostStats());
							u = (u * multiplicator + 2*levelRarity)%modulo;
							levelEffect = Math.min(levelEffect, 1 + u%(1+nbstats/effect.getCostStats()));
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						}
					}
				}
		
		//attack and hp
		int bonusAttack = u%(1+nbstats/2);
		int attack = 1 + bonusAttack;
		int hp = 1 + nbstats - 2*bonusAttack;

		return new UndeadCard("skeleton", 0, hp, attack, effects, levelRarity, false);
	}
	
	private static WizardCard wizardCardSource(int modulo, int multiplicator, int globalStrengh, int rarityStrengh, int u, int levelRarity) throws IOException {
		int nbstats = 0;
		nbstats = 2 + globalStrengh/8 + levelRarity * rarityStrengh;
		nbstats = nbstats - u%(1+globalStrengh/8);
		u = (u * multiplicator + 2*levelRarity)%modulo;
		//effects
		boolean stopEffects = false;
		List<Effect> effects = new ArrayList<>();
		List<String> sortedeffects = new ArrayList<>();
		
		int maxGemEffects = 1;
		if (nbstats >= 4) {
			maxGemEffects = 2;
		}
		if (nbstats >= 6) {
			maxGemEffects = 3;
		}
		int nbGemEffects = 1;
		if (nbGemEffects < maxGemEffects && u%4 == 3) {
			nbGemEffects ++;
			u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		if (nbGemEffects < maxGemEffects && u%4 == 3) {
			nbGemEffects ++;
			u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		
		if (nbGemEffects == 1) {
			if (u%3 == 0) {
				effects.add(new Effect("green_gem", "wizard", 1));
			}
			if (u%3 == 1) {
				effects.add(new Effect("orange_gem", "wizard", 1));
			}
			if (u%3 == 2) {
				effects.add(new Effect("blue_gem", "wizard", 1));
			}
			nbstats -= 2;
		}
		if (nbGemEffects == 2) {
			if (u%3 == 0) {
				effects.add(new Effect("orange_gem", "wizard", 1));
				effects.add(new Effect("blue_gem", "wizard", 1));
			}
			if (u%3 == 1) {
				effects.add(new Effect("green_gem", "wizard", 1));
				effects.add(new Effect("blue_gem", "wizard", 1));
			}
			if (u%3 == 2) {
				effects.add(new Effect("green_gem", "wizard", 1));
				effects.add(new Effect("orange_gem", "wizard", 1));
			}
			nbstats -= 4;
		}
		if (nbGemEffects == 3) {
			effects.add(new Effect("green_gem", "wizard", 1));
			effects.add(new Effect("orange_gem", "wizard", 1));
			effects.add(new Effect("blue_gem", "wizard", 1));
			nbstats -= 6;
		}
		
		//effect1
		//gem effect
		
		//effect2
		if (!stopEffects && effects.size()<2) {
		if (u%4>1) {
			String effectName = Effect.namesWizardEffects.get(u%(Effect.namesWizardEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || Effect.namesAttackEffects.contains(effectName) || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "wizard", 1));
				} else {
					effects.add(new Effect(effectName, "wizard"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
				
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//effect3
		if (!stopEffects && effects.size()<3) {
		if (u%4>2) {
			String effectName = Effect.namesWizardEffects.get(u%(Effect.namesWizardEffects.size()));
			if (Effect.mapEffectToCost.get(effectName) > nbstats || Effect.namesAttackEffects.contains(effectName) || sortedeffects.contains(effectName)) {
				stopEffects = true;
			} else {
				sortedeffects.add(effectName);
				if (Effect.namesLevelEffects.contains(effectName)) {
					effects.add(new Effect(effectName, "wizard", 1));
				} else {
					effects.add(new Effect(effectName, "wizard"));
				}
				nbstats -= effects.get(effects.size()-1).getCostStats();
			}
		} else {
			stopEffects = true;
		}
		u = (u * multiplicator + 2*levelRarity)%modulo;
		}
		//levels effects
				for (int i=0;i<effects.size();i++) {
					Effect effect = effects.get(i);
					if (Effect.namesLevelEffects.contains(effect.getName())) {
						if (Effect.namesResourceEffects.contains(effect.getName())) {
							int lvlmaxEffect = 1;
							int lvlmaxMaxEffect = 1 + nbstats/effect.getCostStats();
							ArrayList<Integer> integerSeen = new ArrayList<Integer>();
							while (u%4 == 3 && !integerSeen.contains(u) && lvlmaxEffect < lvlmaxMaxEffect) {
								integerSeen.add(u);
								u = (u * multiplicator + 2*levelRarity)%modulo;
								lvlmaxEffect++;
							}
							u = (u * multiplicator + 2*levelRarity)%modulo;
							int levelEffect = 1 + u%lvlmaxEffect;
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						} else {
							int levelEffect = 1 + u%(1+nbstats/effect.getCostStats());
							u = (u * multiplicator + 2*levelRarity)%modulo;
							levelEffect = Math.min(levelEffect, 1 + u%(1+nbstats/effect.getCostStats()));
							u = (u * multiplicator + 2*levelRarity)%modulo;
							nbstats -= (levelEffect-1)*effect.getCostStats();
							effect.setLevel(levelEffect);
						}
					}
				}
		
		//hp
		int hp = 1 + nbstats;

		return WizardCard.sourceCard(hp, effects, levelRarity);
	}
}
