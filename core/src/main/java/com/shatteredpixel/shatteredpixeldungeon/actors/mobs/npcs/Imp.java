/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DwarfToken;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.AmbitiousImpRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndImpOld;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

public class Imp extends NPC {

	{
		spriteClass = ImpSprite.class;

		properties.add(Property.IMMOVABLE);
	}
	
	private boolean seenBefore = false;

	@Override
	public Notes.Landmark landmark() {
		return Quest.isCompleted() ? null : Notes.Landmark.IMP;
	}

	@Override
	protected boolean act() {
		if (Dungeon.hero.buff(AscensionChallenge.class) != null){
			die(null);
			return true;
		}

		//extra logic in case imp is holding the quest reward
		if (Quest.isCompleted() && Quest.reward != null){
			Dungeon.level.drop(Quest.reward, pos);
			throwItems();
			Quest.reward = null;
		}

		if (Quest.isCompleted() && Quest.score > 2000
				&& fieldOfView != null && !fieldOfView[Dungeon.hero.pos]){
			flee();
		} else if (!Quest.given && Dungeon.level.visited[pos]) {
			if (!seenBefore && Dungeon.level.heroFOV[pos]) {
				yell(Messages.get(this, "hey", Messages.titleCase(Dungeon.hero.name())));
				seenBefore = true;
			}
		} else {
			seenBefore = false;
		}
		
		return super.act();
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
	
	@Override
	public boolean interact(Char c) {
		
		sprite.turnTo( pos, Dungeon.hero.pos );

		if (c != Dungeon.hero){
			return true;
		}

		//pre v4.4.0 logic
		if (Quest.oldQuest) {
			if (Quest.given) {

				DwarfToken tokens = Dungeon.hero.belongings.getItem(DwarfToken.class);
				if (tokens != null && (tokens.quantity() >= 5 || (!Quest.alternative && tokens.quantity() >= 4))) {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show(new WndImpOld(Imp.this, tokens));
						}
					});
				} else {
					tell(Quest.alternative ?
							Messages.get(this, "old_monks_2", Messages.titleCase(Dungeon.hero.name()))
							: Messages.get(this, "old_golems_2", Messages.titleCase(Dungeon.hero.name())));
				}

			} else {
				tell(Messages.get(this, "old_intro") + "\n\n" + (Quest.alternative ?
						Messages.get(this, "old_monks_1", Messages.titleCase(Dungeon.hero.name()))
						: Messages.get(this, "old_golems_1", Messages.titleCase(Dungeon.hero.name()))));
				Quest.given = true;
				Quest.completed = false;
			}
		} else {
			if (!Quest.given()){
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show(new WndQuest(Imp.this, Messages.get(Imp.this, "quest_intro_1")) {
							@Override
							public void hide() {
								super.hide();

								Quest.given = true;
								Quest.completed = false;

								tell(Messages.get(Imp.this, "quest_intro_2"));
							}
						});
					}
				});
			} else if (!Quest.isCompleted()) {
				tell(Messages.get(Imp.this, "quest_in_progress"));
			} else {
				if (Quest.score <= 2000){
					tell(Messages.get(Imp.this, "quest_completed_bad"));
				} else if (Quest.score < 4000){
					tell(Messages.get(Imp.this, "quest_completed_good"));
				} else {
					tell(Messages.get(Imp.this, "quest_completed_great"));
				}
			}
		}

		return true;
	}
	
	private void tell( String text ) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show( new WndQuest( Imp.this, text ));
			}
		});
	}

	public void flee() {
		
		yell( Messages.get(this, "cya", Messages.titleCase(Dungeon.hero.name())) );
		
		destroy();
		sprite.die();
	}

	public static class Quest {

		private static boolean spawned;

		//variables exclusive to old, pre-4.0.0 Imp quest
		private static boolean oldQuest = false;
		private static boolean alternative; //true= golems, false = monks

		//variables shared by both quests
		private static boolean given;
		private static boolean completed;
		public static Item reward; //just used to hold the reward if her's inventory is full in new version

		//variacles exclusive to new quest
		public static ArrayList<Item> rewardOptions = new ArrayList<>();
		public static int hazardFreebies; //player gets two free hits from hazards before they start penalizing score
		public static boolean mirrorUsed = false;
		private static int score; //Not the score used in rankings! This score has no penalty applied
		
		public static void reset() {
			spawned = false;
			given = false;
			completed = false;

			reward = null;
			hazardFreebies = 2;
			mirrorUsed = false;
			score = 0;
		}
		
		private static final String NODE        = "demon";

		private static final String SPAWNED     = "spawned";

		private static final String OLD_QUEST   = "old_quest";
		private static final String ALTERNATIVE = "alternative";
		private static final String REWARD      = "reward";

		private static final String GIVEN       = "given";
		private static final String COMPLETED   = "completed";

		private static final String HAZRD_FREEBIES = "hazard_freebies";
		private static final String SCORE       = "score";
		private static final String REWARD_OPTIONS = "reward_options";
		private static final String MIRROR_USED = "mirror_used";

		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( OLD_QUEST, oldQuest );
				node.put( ALTERNATIVE, alternative );
				
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REWARD, reward );

				node.put( HAZRD_FREEBIES, hazardFreebies );
				node.put( SCORE, score );
				node.put( REWARD_OPTIONS, rewardOptions );
				node.put( MIRROR_USED, mirrorUsed );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

				if (node.contains( OLD_QUEST )){
					oldQuest = node.getBoolean( OLD_QUEST );
				} else {
					oldQuest = true;
				}
				if (oldQuest){
					alternative	= node.getBoolean( ALTERNATIVE );
					score = 0;
					rewardOptions.clear();
					mirrorUsed = false;
				} else {
					alternative = false;
					hazardFreebies = node.getInt( HAZRD_FREEBIES );
					mirrorUsed = node.getBoolean( MIRROR_USED );
					score = node.getInt( SCORE );
					rewardOptions = new ArrayList<>((Collection<Item>) (Collection<?>) node.getCollection( REWARD_OPTIONS ));
				}

				reward = (Item)node.get( REWARD );
				
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
			}
		}

		public static ArrayList<Room> spawn( ArrayList<Room> rooms ) {
			if (!spawned && Dungeon.depth > 16 && Random.Int( 20 - Dungeon.depth ) == 0) {

				rooms.add(new AmbitiousImpRoom());
				spawned = true;

				oldQuest = false;
				reward = null;
				score = 0;
				
				given = false;
				mirrorUsed = false;

				rewardOptions.clear();
				Item artif = Generator.randomArtifact();
				//generate a ring instead
				if (artif != null){
					((Artifact)artif.identify(false)).transferUpgrade(5);
				} else {
					artif = Generator.random(Generator.Category.RING);
					//we delay the ID on rings until the boss is defeated
					artif.level(Random.IntRange(2, 4));
				}
				rewardOptions.add(artif);

				Item ring;
				do {
					ring = Generator.random(Generator.Category.RING);
				} while (ring.getClass() == artif.getClass()); //rare cases of the same kind of ring twice
				//we delay the ID on rings until the boss is defeated
				ring.level(Random.IntRange(2, 4));
				rewardOptions.add(ring);

				if (Random.Int(2) == 0) {
					rewardOptions.add(((Weapon)Generator.random(Generator.Category.WEP_T5)).enchant().identify(false).level(Random.IntRange(2, 4)));
					rewardOptions.add(((Weapon)Generator.random(Generator.Category.MIS_T4)).enchant().identify(false).level(Random.IntRange(3, 5)));
				} else {
					rewardOptions.add(((Weapon)Generator.random(Generator.Category.MIS_T5)).enchant().identify(false).level(Random.IntRange(2, 4)));
					rewardOptions.add(((Weapon)Generator.random(Generator.Category.WEP_T4)).enchant().identify(false).level(Random.IntRange(3, 5)));
				}
				rewardOptions.add(new PlateArmor().inscribe().identify(false).level(Random.IntRange(2, 4)));
				Wand w = (Wand) Generator.random(Generator.Category.WAND);
				w.identify(false).level(Random.IntRange(2, 4));
				w.curCharges = w.maxCharges;
				rewardOptions.add(w);

				for (Item i : rewardOptions){
					i.cursed = false;
				}
			}

			return rooms;
		}

		public static boolean given(){
			return given;
		}

		public static boolean isOld(){
			return oldQuest;
		}

		public static void oldProcess( Mob mob ) {
			if (spawned && oldQuest && given && !completed && Dungeon.depth != 20) {
				if ((alternative && mob instanceof Monk) ||
					(!alternative && mob instanceof Golem)) {
					
					Dungeon.level.drop( new DwarfToken(), mob.pos ).sprite.drop();
				}
			}
		}
		
		public static void oldComplete() {
			reward = null;
			completed = true;

			Statistics.questScores[3] = 4000;
			Notes.remove( Notes.Landmark.IMP );
		}

		public static void complete( int score ){
			completed = true;

			Imp.Quest.score = score;
			Statistics.questScores[3] += score;
			Notes.remove( Notes.Landmark.IMP );
		}
		
		public static boolean isCompleted() {
			return spawned && completed;
		}

		public static boolean earnedShop() {
			return completed && (oldQuest || score > 2000);
		}
	}
}
