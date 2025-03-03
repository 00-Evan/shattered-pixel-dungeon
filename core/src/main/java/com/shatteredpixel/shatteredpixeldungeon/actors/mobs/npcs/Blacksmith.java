/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ParchmentScrap;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.BlacksmithRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBlacksmith;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

public class Blacksmith extends NPC {
	
	{
		spriteClass = BlacksmithSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	@Override
	public Notes.Landmark landmark() {
		return (!Quest.completed() || Quest.rewardsAvailable()) ? Notes.Landmark.TROLL : null;
	}

	@Override
	protected boolean act() {
		if (Dungeon.hero.buff(AscensionChallenge.class) != null){
			die(null);
			Notes.remove( landmark() );
			return true;
		} else if (!Quest.rewardsAvailable() && Quest.completed()){
			Notes.remove( landmark() );
		}
		return super.act();
	}
	
	@Override
	public boolean interact(Char c) {
		
		sprite.turnTo( pos, c.pos );

		if (c != Dungeon.hero){
			return true;
		}
		
		if (!Quest.given) {

			String msg1 = "";
			String msg2 = "";

			switch (Dungeon.hero.heroClass){
				case WARRIOR:   msg1 += Messages.get(Blacksmith.this, "intro_quest_warrior"); break;
				case MAGE:      msg1 += Messages.get(Blacksmith.this, "intro_quest_mage"); break;
				case ROGUE:     msg1 += Messages.get(Blacksmith.this, "intro_quest_rogue"); break;
				case HUNTRESS:  msg1 += Messages.get(Blacksmith.this, "intro_quest_huntress"); break;
				case DUELIST:   msg1 += Messages.get(Blacksmith.this, "intro_quest_duelist"); break;
				case CLERIC:    msg1 += Messages.get(Blacksmith.this, "intro_quest_cleric"); break;
			}

			msg1 += "\n\n" + Messages.get(Blacksmith.this, "intro_quest_start");

			switch (Quest.type){
				case Quest.CRYSTAL: msg2 += Messages.get(Blacksmith.this, "intro_quest_crystal"); break;
				case Quest.GNOLL:   msg2 += Messages.get(Blacksmith.this, "intro_quest_gnoll"); break;
				case Quest.FUNGI:   msg2 += Messages.get(Blacksmith.this, "intro_quest_fungi"); break;
			}

			final String msg1Final = msg1;
			final String msg2Final = msg2;
			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					GameScene.show(new WndQuest(Blacksmith.this, msg1Final) {
						@Override
						public void hide() {
							super.hide();

							Quest.given = true;
							Quest.completed = false;
							Item pick = Quest.pickaxe != null ? Quest.pickaxe : new Pickaxe();
							if (pick.doPickUp( Dungeon.hero )) {
								GLog.i( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", pick.name()) ));
							} else {
								Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
							}
							Quest.pickaxe = null;

							if (msg2Final != ""){
								GameScene.show(new WndQuest(Blacksmith.this, msg2Final));
							}

						}
					} );
				}
			});
			
		} else if (!Quest.completed) {

			String msg = Messages.get(this, "reminder") + "\n\n";
			switch (Quest.type){
				case Quest.CRYSTAL: msg += Messages.get(Blacksmith.this, "reminder_crystal"); break;
				case Quest.GNOLL:   msg += Messages.get(Blacksmith.this, "reminder_gnoll"); break;
				case Quest.FUNGI:   msg += Messages.get(Blacksmith.this, "reminder_fungi"); break;
			}
			tell(msg);

		} else if (Quest.rewardsAvailable()) {

			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					//in case game was closed during smith reward selection
					if (Quest.smithRewards != null && Quest.smiths > 0){
						GameScene.show( new WndBlacksmith.WndSmith( Blacksmith.this, Dungeon.hero ) );
					} else {
						GameScene.show(new WndBlacksmith(Blacksmith.this, Dungeon.hero));
					}
				}
			});

		} else {
			
			tell( Messages.get(this, "get_lost") );
			
		}

		return true;
	}
	
	private void tell( String text ) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show( new WndQuest( Blacksmith.this, text ) );
			}
		});
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		//do nothing
	}

	@Override
	public boolean add( Buff buff ) {
		return false;
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	public static class Quest {

		private static int type = 0;
		public static final int CRYSTAL = 1;
		public static final int GNOLL = 2;
		public static final int FUNGI = 3; //The fungi quest is not implemented, only exists partially in code

		//quest state information
		private static boolean spawned;
		private static boolean given;
		private static boolean started;
		private static boolean bossBeaten;
		private static boolean completed;

		//reward tracking. Stores remaining favor, the pickaxe, and how many of each reward has been chosen
		public static int favor;
		public static Item pickaxe;
		public static int reforges;
		public static int hardens;
		public static int upgrades;
		public static int smiths;

		//pre-generate these so they are consistent between seeds
		public static ArrayList<Item> smithRewards;
		public static Weapon.Enchantment smithEnchant;
		public static Armor.Glyph smithGlyph;
		
		public static void reset() {
			type        = 0;

			spawned		= false;
			given		= false;
			started     = false;
			bossBeaten  = false;
			completed	= false;

			favor       = 0;
			pickaxe     = new Pickaxe().identify();
			reforges    = 0;
			hardens     = 0;
			upgrades    = 0;
			smiths      = 0;

			smithRewards = null;
			smithEnchant = null;
			smithGlyph = null;
		}
		
		private static final String NODE	= "blacksmith";

		private static final String TYPE    	= "type";
		private static final String ALTERNATIVE	= "alternative";

		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String STARTED		= "started";
		private static final String BOSS_BEATEN	= "boss_beaten";
		private static final String COMPLETED	= "completed";

		private static final String FAVOR	    = "favor";
		private static final String PICKAXE	    = "pickaxe";
		private static final String REFORGES	= "reforges";
		private static final String HARDENS	    = "hardens";
		private static final String UPGRADES	= "upgrades";
		private static final String SMITHS	    = "smiths";
		private static final String SMITH_REWARDS = "smith_rewards";
		private static final String ENCHANT		= "enchant";
		private static final String GLYPH		= "glyph";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( TYPE, type );

				node.put( GIVEN, given );
				node.put( STARTED, started );
				node.put( BOSS_BEATEN, bossBeaten );
				node.put( COMPLETED, completed );

				node.put( FAVOR, favor );
				if (pickaxe != null) node.put( PICKAXE, pickaxe );
				node.put( REFORGES, reforges );
				node.put( HARDENS, hardens );
				node.put( UPGRADES, upgrades );
				node.put( SMITHS, smiths );

				if (smithRewards != null) {
					node.put( SMITH_REWARDS, smithRewards );
					if (smithEnchant != null) {
						node.put(ENCHANT, smithEnchant);
						node.put(GLYPH, smithGlyph);
					}
				}
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				type = node.getInt(TYPE);

				given = node.getBoolean( GIVEN );
				started = node.getBoolean( STARTED );
				bossBeaten = node.getBoolean( BOSS_BEATEN );
				completed = node.getBoolean( COMPLETED );

				favor = node.getInt( FAVOR );
				if (node.contains(PICKAXE)) {
					pickaxe = (Item) node.get(PICKAXE);
				} else {
					pickaxe = null;
				}
				reforges = node.getInt( REFORGES );
				hardens = node.getInt( HARDENS );
				upgrades = node.getInt( UPGRADES );
				smiths = node.getInt( SMITHS );

				if (node.contains( SMITH_REWARDS )){
					smithRewards = new ArrayList<>((Collection<Item>) ((Collection<?>) node.getCollection( SMITH_REWARDS )));
					if (node.contains(ENCHANT)) {
						smithEnchant = (Weapon.Enchantment) node.get(ENCHANT);
						smithGlyph   = (Armor.Glyph) node.get(GLYPH);
					}
				}

			} else {
				reset();
			}
		}
		
		public static ArrayList<Room> spawn( ArrayList<Room> rooms ) {
			if (!spawned && Dungeon.depth > 11 && Random.Int( 15 - Dungeon.depth ) == 0) {
				
				rooms.add(new BlacksmithRoom());
				spawned = true;

				//Currently cannot roll the fungi quest, as it is not fully implemented
				type = Random.IntRange(1, 2);
				
				given = false;
				generateRewards( true );
				
			}
			return rooms;
		}

		public static void generateRewards( boolean useDecks ){
			smithRewards = new ArrayList<>();
			smithRewards.add(Generator.randomWeapon(3, useDecks));
			smithRewards.add(Generator.randomWeapon(3, useDecks));
			ArrayList<Item> toUndo = new ArrayList<>();
			while (smithRewards.get(0).getClass() == smithRewards.get(1).getClass()) {
				if (useDecks)   toUndo.add(smithRewards.get(1));
				smithRewards.remove(1);
				smithRewards.add(Generator.randomWeapon(3, useDecks));
			}
			for (Item i : toUndo){
				Generator.undoDrop(i);
			}
			smithRewards.add(Generator.randomArmor(3));

			//30%:+0, 45%:+1, 20%:+2, 5%:+3
			int rewardLevel;
			float itemLevelRoll = Random.Float();
			if (itemLevelRoll < 0.3f){
				rewardLevel = 0;
			} else if (itemLevelRoll < 0.75f){
				rewardLevel = 1;
			} else if (itemLevelRoll < 0.95f){
				rewardLevel = 2;
			} else {
				rewardLevel = 3;
			}

			for (Item i : smithRewards){
				i.level(rewardLevel);
				if (i instanceof Weapon) {
					((Weapon) i).enchant(null);
				} else if (i instanceof Armor){
					((Armor) i).inscribe(null);
				}
				i.cursed = false;
			}

			// 30% base chance to be enchanted, stored separately so status isn't revealed early
			//we generate first so that the outcome doesn't affect the number of RNG rolls
			smithEnchant = Weapon.Enchantment.random();
			smithGlyph = Armor.Glyph.random();

			float enchantRoll = Random.Float();
			if (enchantRoll > 0.3f * ParchmentScrap.enchantChanceMultiplier()){
				smithEnchant = null;
				smithGlyph = null;
			}

		}

		public static int Type(){
			return type;
		}

		public static boolean given(){
			return given;
		}

		public static boolean started(){
			return started;
		}

		public static void start(){
			started = true;
		}

		public static boolean beatBoss(){
			return bossBeaten = true;
		}

		public static boolean bossBeaten(){
			return bossBeaten;
		}

		public static boolean completed(){
			return given && completed;
		}

		public static void complete(){
			completed = true;

			favor = 0;
			DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
			if (gold != null){
				favor += Math.min(2000, gold.quantity()*50);
				gold.detachAll(Dungeon.hero.belongings.backpack);
			}

			Pickaxe pick = Dungeon.hero.belongings.getItem(Pickaxe.class);
			if (pick.isEquipped(Dungeon.hero)) {
				boolean wasCursed = pick.cursed;
				pick.cursed = false; //so that it can always be removed
				pick.doUnequip(Dungeon.hero, false);
				pick.cursed = wasCursed;
			}
			pick.detach(Dungeon.hero.belongings.backpack);
			Quest.pickaxe = pick;

			if (bossBeaten) favor += 1000;

			Statistics.questScores[2] = favor;
		}

		public static boolean rewardsAvailable(){
			return favor > 0
					|| (Quest.smithRewards != null && Quest.smiths > 0)
					|| (pickaxe != null && Statistics.questScores[2] >= 2500);
		}

	}
}
