/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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

import java.util.Collection;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Journal;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WandmakerSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndWandmaker;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Wandmaker extends NPC {

	{
		name = "old wandmaker";
		spriteClass = WandmakerSprite.class;
	}

	private static final String INTRO_WARRIOR	=
		"Oh, what a pleasant surprise to meet a hero in such a depressing place! " +
		"If you're up to helping an old man out, I may have a task for you.\n\n";

	private static final String INTRO_ROGUE		=
		"Oh Goodness, you startled me! I haven't met a bandit from this place that still has his sanity, " +
		"so you must be from the surface! If you're up to helping a stranger out, I may have a task for you.\n\n";

	private static final String INTRO_MAGE		=
		"Oh, hello %s! I heard there was some ruckus regarding you and the wizards institute? " +
		"Oh never mind, I never liked those stick-in-the-muds anyway. If you're willing, I may have a task for you.\n\n";

	private static final String INTRO_HUNTRESS	=
		"Oh, hello miss! A friendly face is a pleasant surprise down here isn't it? " +
		"In fact, I swear I've seen your face before, but I can't put my finger on it... " +
		"Oh never mind, if you're here for adventure, I may have a task for you.\n\n";

	private static final String INTRO_1 	=
		"I came here to find a rare ingredient for a wand, but I've gotten myself lost, " +
		"and my magical shield is weakening. I'll need to leave soon, but can't bear to go without getting what I came for.";


	private static final String INTRO_DUST	=
		"I'm looking for some _corpse dust_. It's a special kind of cursed bone meal that usually shows up in places like this. " +
		"There should be a barricaded room around here somewhere, I'm sure some dust will turn up there. " +
		"Do be careful though, the curse the dust carries is quite potent, _get back to me as fast as you can_ and I'll cleanse it for you.\n\n";

	private static final String INTRO_EMBER	=
		"I'm looking for some _fresh embers_ from a newborn fire elemental. Elementals usually pop up when a summoning ritual isn't controlled, " +
		"so just find some candles and a ritual site and I'm sure you can get one to pop up. " +
		"You might want to _keep some sort of freezing item handy_ though, elementals are very powerful, but ice will take them down quite easily.\n\n";

	private static final String INTRO_BERRY	=
		"The old warden of this prison kept a _rotberry plant_, and I'm after one of its seeds. The plant has probably gone wild by now though, " +
		"so getting it to give up a seed might be tricky. Its garden should be somewhere around here. " +
		"Try to _keep away from its vine lashers_ if you want to stay in one piece. Using fire might be tempting but please don't, you'll kill the plant and destroy its seeds.\n\n";

	private static final String INTRO_2 	=
			"If you can get that for me, I'll be happy to pay you with one of my finely crafted wands! " +
			"I brought two with me, so you can take whichever one you prefer.";

	private static final String REMINDER_DUST	=
		"Any luck with corpse dust, %s? Look for some barricades.";

	private static final String REMINDER_EMBER	=
		"Any luck with those embers, %s? You'll need to find four candles and the ritual site.";
	
	private static final String REMINDER_BERRY	=
		"Any luck with a Rotberry seed, %s? Look for a room filled with vegetation.";

	
	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public String defenseVerb() {
		return "absorbed";
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		if (Quest.given) {
			
			Item item;
			switch (Quest.type) {
				case 1:
				default:
					item = Dungeon.hero.belongings.getItem(CorpseDust.class);
					break;
				case 2:
					item = Dungeon.hero.belongings.getItem(Embers.class);
					break;
				case 3:
					item = Dungeon.hero.belongings.getItem(Rotberry.Seed.class);
					break;
			}

			if (item != null) {
				GameScene.show( new WndWandmaker( this, item ) );
			} else {
				String msg = "";
				switch(Quest.type){
					case 1:
						msg = REMINDER_DUST;
						break;
					case 2:
						msg = REMINDER_EMBER;
						break;
					case 3:
						msg = REMINDER_BERRY;
						break;
				}
				GameScene.show(new WndQuest(this, Utils.format(msg, Dungeon.hero.givenName())));
			}
			
		} else {

			String msg1 = "";
			String msg2 = "";
			switch(Dungeon.hero.heroClass){
				case WARRIOR:
					msg1 += INTRO_WARRIOR;
					break;
				case ROGUE:
					msg1 += INTRO_ROGUE;
					break;
				case MAGE:
					msg1 += INTRO_MAGE;
					break;
				case HUNTRESS:
					msg1 += INTRO_HUNTRESS;
					break;
			}

			msg1 += INTRO_1;

			switch (Quest.type){
				case 1:
					msg2 += INTRO_DUST;
					break;
				case 2:
					msg2 += INTRO_EMBER;
					break;
				case 3:
					msg2 += INTRO_BERRY;
					break;
			}

			msg2 += INTRO_2;
			final String msg2final = msg2;
			final NPC wandmaker = this;

			GameScene.show(new WndQuest(wandmaker, Utils.format(msg1, Dungeon.hero.givenName())){
				@Override
				public void hide() {
					super.hide();
					GameScene.show(new WndQuest(wandmaker, Utils.format(msg2final, Dungeon.hero.givenName())));
				}
			});

			Journal.add( Journal.Feature.WANDMAKER );
			Quest.given = true;
		}
	}
	
	@Override
	public String description() {
		return
			"This old yet hale gentleman wears a slightly confused " +
			"expression. He is protected by a magic shield.";
	}
	
	public static class Quest {

		private static int type;
		// 1 = corpse dust quest
		// 2 = elemental embers quest
		// 3 = rotberry quest
		
		private static boolean spawned;
		
		private static boolean given;
		
		public static Wand wand1;
		public static Wand wand2;
		
		public static void reset() {
			spawned = false;
			type = 0;

			wand1 = null;
			wand2 = null;
		}
		
		private static final String NODE		= "wandmaker";
		
		private static final String SPAWNED		= "spawned";
		private static final String TYPE		= "type";
		private static final String GIVEN		= "given";
		private static final String WAND1		= "wand1";
		private static final String WAND2		= "wand2";

		private static final String RITUALPOS	= "ritualpos";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( TYPE, type );
				
				node.put( GIVEN, given );
				
				node.put( WAND1, wand1 );
				node.put( WAND2, wand2 );

				if (type == 2){
					node.put( RITUALPOS, CeremonialCandle.ritualPos );
				}

			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

				//TODO remove when pre-0.3.2 saves are no longer supported
				if (node.contains(TYPE)) {
					type = node.getInt(TYPE);
				} else {
					type = node.getBoolean("alternative")? 1 : 3;
				}
				
				given = node.getBoolean( GIVEN );
				
				wand1 = (Wand)node.get( WAND1 );
				wand2 = (Wand)node.get( WAND2 );

				if (type == 2){
					CeremonialCandle.ritualPos = node.getInt( RITUALPOS );
				}

			} else {
				reset();
			}
		}
		
		public static boolean spawn( PrisonLevel level, Room room, Collection<Room> rooms ) {
			if (!spawned && (type != 0 || (Dungeon.depth > 6 && Random.Int( 10 - Dungeon.depth ) == 0))) {
				// decide between 1,2, or 3 for quest type.
				// but if the no herbalism challenge is enabled, only pick 1 or 2, no rotberry.
				if (type == 0) type = Random.Int(Dungeon.isChallenged(Challenges.NO_HERBALISM) ? 2 : 3)+1;

				//note that we set the type but can fail here. This ensures that if a level needs to be re-generated
				//we don't re-roll the quest, it will try to assign itself to that new level with the same type.
				if (setRoom( rooms )){
					Wandmaker npc = new Wandmaker();
					do {
						npc.pos = room.random();
					} while (level.map[npc.pos] == Terrain.ENTRANCE || level.map[npc.pos] == Terrain.SIGN);
					level.mobs.add( npc );

					spawned = true;

					given = false;
					wand1 = (Wand) Generator.random(Generator.Category.WAND);
					wand1.upgrade();

					do {
						wand2 = (Wand) Generator.random(Generator.Category.WAND);
					} while (wand2.getClass().equals(wand1.getClass()));
					wand2.upgrade();

					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		}
		
		private static boolean setRoom( Collection<Room> rooms) {
			Room questRoom = null;
			for (Room r : rooms){
				if (r.type == Room.Type.STANDARD && r.width() > 5 && r.height() > 5){
					if (type == 2 || r.connected.size() == 1){
						questRoom = r;
						break;
					}
				}
			}

			if (questRoom == null){
				return false;
			}

			switch (type){
				case 1: default:
					questRoom.type = Room.Type.MASS_GRAVE;
					break;
				case 2:
					questRoom.type = Room.Type.RITUAL_SITE;
					break;
				case 3:
					questRoom.type = Room.Type.ROT_GARDEN;
					break;
			}

			return true;
		}
		
		public static void complete() {
			wand1 = null;
			wand2 = null;
			
			Journal.remove( Journal.Feature.WANDMAKER );
		}
	}
}
