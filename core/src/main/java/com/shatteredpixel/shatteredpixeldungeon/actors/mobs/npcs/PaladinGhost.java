/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FetidRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollTrickster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreatCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.sundry.Holywater;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.sundry.PaladinSeal;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndPaladinGhost;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class PaladinGhost extends NPC {

	{
		spriteClass = ImpSprite.class;

		flying = true;

		properties.add(Property.IMMOVABLE);
	}
	
	private boolean seenBefore = false;
	
	@Override
	protected boolean act() {
		if (Dungeon.hero.buff(AscensionChallenge.class) != null){
			die(null);
			return true;
		}
		if (!Quest.given && Dungeon.level.visited[pos]) {
			if (!seenBefore) {
				yell( Messages.get(this, "help", Messages.titleCase(Dungeon.hero.name()) ) );
			}
			Notes.add( Notes.Landmark.PALADIN );
			seenBefore = true;
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

		if (PaladinGhost.Quest.given) {


				if (PaladinGhost.Quest.processed) {


					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show(new WndPaladinGhost(PaladinGhost.this, PaladinGhost.Quest.type));
						}
					});
				} else {


					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							switch (PaladinGhost.Quest.type) {
								case 1:
								default:
									GameScene.show(new WndQuest(PaladinGhost.this, Messages.get(PaladinGhost.this, "vampire_2")));
									break;
								case 2:
									GameScene.show(new WndQuest(PaladinGhost.this, Messages.get(PaladinGhost.this, "troll_2")));
									break;
								case 3:
									GameScene.show(new WndQuest(PaladinGhost.this, Messages.get(PaladinGhost.this, "dm666_2")));
									break;
							}
						}
					});

					int newPos = -1;
					for (int i = 0; i < 10; i++) {
						newPos = Dungeon.level.randomRespawnCell( this );
						if (newPos != -1) {
							break;
						}
					}
					if (newPos != -1) {

						CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
						pos = newPos;
						sprite.place(pos);
						sprite.visible = Dungeon.level.heroFOV[pos];
					}
				}

		} else {
			Mob questBoss;
			String txt_quest;

			switch (PaladinGhost.Quest.type){
				case 1: default:
					questBoss = new FetidRat();
					txt_quest = Messages.get(this, "vampire_1", Messages.titleCase(Dungeon.hero.name())); break;
				case 2:
					questBoss = new GnollTrickster();
					txt_quest = Messages.get(this, "troll_1", Messages.titleCase(Dungeon.hero.name())); break;
				case 3:
					questBoss = new GreatCrab();
					txt_quest = Messages.get(this, "dm666_1", Messages.titleCase(Dungeon.hero.name())); break;
			}

			questBoss.pos = Dungeon.level.randomRespawnCell( this );

			if (questBoss.pos != -1) {
				GameScene.add(questBoss);
				PaladinGhost.Quest.given = true;
				Notes.add( Notes.Landmark.GHOST );
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndQuest( PaladinGhost.this, txt_quest ) );
					}
				});
			}

		}

		return true;
	}


/*
		if (Quest.given) {
			
			DwarfToken tokens = Dungeon.hero.belongings.getItem( DwarfToken.class );
			if (tokens != null && (tokens.quantity() >= 5 || (!Quest.alternative && tokens.quantity() >= 4))) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndImp( PaladinGhost.this, tokens ) );
					}
				});
			} else {
				tell( Quest.alternative ?
						Messages.get(this, "monks_2", Messages.titleCase(Dungeon.hero.name()))
						: Messages.get(this, "golems_2", Messages.titleCase(Dungeon.hero.name())) );
			}
			
		} else {
			tell( Quest.alternative ? Messages.get(this, "monks_1") : Messages.get(this, "golems_1") );
			Quest.given = true;
			Quest.completed = false;
			Notes.add( Notes.Landmark.IMP );
		}

		return true;
	}
	*/

	private void tell( String text ) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show( new WndQuest( PaladinGhost.this, text ));
			}
		});
	}
	
	public void flee() {

		//再见
		yell( Messages.get(this, "cya", Messages.titleCase(Dungeon.hero.name())) );
		
		destroy();
		sprite.die();
	}

	public static class Quest {
		private static int type;
		
		private static boolean spawned;
		private static boolean given;
		private static boolean completed;
		private static int depth;
		private static boolean processed;

		public static Item reward;
		
		public static void reset() {
			spawned = false;
			given = false;
			completed = false;

			reward = null;
		}
		
		private static final String NODE		= "demon";
		
		private static final String ALTERNATIVE	= "alternative";
		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String DEPTH		= "depth";
		private static final String REWARD		= "reward";
		private static final String TYPE        = "type";
		private static final String PROCESSED	= "processed";

		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( TYPE, type );
				
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( PROCESSED, processed );
				node.put( DEPTH, depth );

				node.put( REWARD, reward );

			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				node.put( TYPE, type );
				
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				depth = node.getInt( DEPTH );
				reward = (Item)node.get( REWARD );

			}
		}
		
		public static void spawn( CavesLevel level ) {
			if (!spawned && Dungeon.depth > 16 && Random.Int( 20 - Dungeon.depth ) == 0) {
				
				PaladinGhost npc = new PaladinGhost();
				do {
					npc.pos = level.randomRespawnCell( npc );
				} while (
						npc.pos == -1 ||
						level.heaps.get( npc.pos ) != null ||
						level.traps.get( npc.pos) != null ||
						level.findMob( npc.pos ) != null ||
						//The imp doesn't move, so he cannot obstruct a passageway
						!(level.passable[npc.pos + PathFinder.CIRCLE4[0]] && level.passable[npc.pos + PathFinder.CIRCLE4[2]]) ||
						!(level.passable[npc.pos + PathFinder.CIRCLE4[1]] && level.passable[npc.pos + PathFinder.CIRCLE4[3]]));
				level.mobs.add( npc );
				
				spawned = true;

				//always assigns monks on floor 17, golems on floor 19, and 50/50 between either on 18
				switch (Dungeon.depth){
					case 12: default:
						type = 1;
						break;
					case 13:
						type = 2;
						break;
					case 14:
						type = 3;
						break;
				}
				
				given = false;

				do {
					int thing;
					thing = Random.Int( 1,2 );
					switch ( thing ){
						case 1: default:
							reward = new PaladinSeal();
							break;
						case 2:
							reward = new Holywater();
							break;
					}

				} while ( !reward.cursed );
				reward.cursed = false;
			}
		}
		
		public static void process( Mob mob ) {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				GLog.n( Messages.get(Ghost.class, "find_me") );
				Sample.INSTANCE.play( Assets.Sounds.GHOST );
				processed = true;
				Statistics.questScores[0] = 1000;
			}
		}
		
		public static void complete() {

			completed = true;

			Statistics.questScores[3] = 4000;
			Notes.remove( Notes.Landmark.PALADIN );
		}
		
		public static boolean isCompleted() {
			return completed;
		}
	}
}
//后续需要改动，通过玩家选择，收走护甲或锤子，才能获取圣水和圣骑士护符；怪物生成在上锁房间里