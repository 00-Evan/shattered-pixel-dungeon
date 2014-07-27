/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.actors.mobs.npcs;

import java.util.HashSet;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ParalyticGas;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.quest.DriedRose;
import com.watabou.pixeldungeon.items.quest.RatSkull;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.levels.SewerLevel;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.FetidRatSprite;
import com.watabou.pixeldungeon.sprites.GhostSprite;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.pixeldungeon.windows.WndSadGhost;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ghost extends Mob.NPC {

	{
		name = "sad ghost";
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = State.WANDERING;
	}
	
	private static final String TXT_ROSE1	=
		"Hello adventurer... Once I was like you - strong and confident... " +
		"And now I'm dead... But I can't leave this place... Not until I have my _dried rose_... " +
		"It's very important to me... Some monster stole it from my body...";
	
	private static final String TXT_ROSE2	=
		"Please... Help me... Find the rose...";
	
	private static final String TXT_RAT1	=
		"Hello adventurer... Once I was like you - strong and confident... " +
		"And now I'm dead... But I can't leave this place... Not until I have my revenge... " +
		"Slay the _fetid rat_, that has taken my life...";
		
	private static final String TXT_RAT2	=
		"Please... Help me... Slay the abomination...";

	
	public Ghost() {
		super();

		Sample.INSTANCE.load( Assets.SND_GHOST );
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public float speed() {
		return 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return DUMMY;
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
		
		Sample.INSTANCE.play( Assets.SND_GHOST );
		
		if (Quest.given) {
			
			Item item = Quest.alternative ?
				Dungeon.hero.belongings.getItem( RatSkull.class ) :
				Dungeon.hero.belongings.getItem( DriedRose.class );	
			if (item != null) {
				GameScene.show( new WndSadGhost( this, item ) );
			} else {
				GameScene.show( new WndQuest( this, Quest.alternative ? TXT_RAT2 : TXT_ROSE2 ) );
				
				int newPos = -1;
				for (int i=0; i < 10; i++) {
					newPos = Dungeon.level.randomRespawnCell();
					if (newPos != -1) {
						break;
					}
				}
				if (newPos != -1) {
					
					Actor.freeCell( pos );
					
					CellEmitter.get( pos ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
					pos = newPos;
					sprite.place( pos );
					sprite.visible = Dungeon.visible[pos];
				}
			}
			
		} else {
			GameScene.show( new WndQuest( this, Quest.alternative ? TXT_RAT1 : TXT_ROSE1 ) );
			Quest.given = true;
			
			Journal.add( Journal.Feature.GHOST );
		}
	}
	
	@Override
	public String description() {
		return 
			"The ghost is barely visible. It looks like a shapeless " +
			"spot of faint light with a sorrowful face.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Paralysis.class );
		IMMUNITIES.add( Roots.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	public static class Quest {
		
		private static boolean spawned;
		
		private static boolean alternative;

		private static boolean given;
		
		private static boolean processed;
		
		private static int depth;
		
		private static int left2kill;
		
		public static Weapon weapon;
		public static Armor armor;
		
		public static void reset() {
			spawned = false; 
			
			weapon = null;
			armor = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
		private static final String LEFT2KILL	= "left2kill";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String ARMOR		= "armor";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( ALTERNATIVE, alternative );
				if (!alternative) {
					node.put( LEFT2KILL, left2kill );
				}
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
				node.put( PROCESSED, processed );
				
				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				
				alternative	=  node.getBoolean( ALTERNATIVE );
				if (!alternative) {
					left2kill = node.getInt( LEFT2KILL );
				}
				
				given	= node.getBoolean( GIVEN );
				depth	= node.getInt( DEPTH );
				processed	= node.getBoolean( PROCESSED );
				
				weapon	= (Weapon)node.get( WEAPON );
				armor	= (Armor)node.get( ARMOR );
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level ) {
			if (!spawned && Dungeon.depth > 1 && Random.Int( 5 - Dungeon.depth ) == 0) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.randomRespawnCell();
				} while (ghost.pos == -1);
				level.mobs.add( ghost );
				Actor.occupyCell( ghost );
				
				spawned = true;
				alternative = Random.Int( 2 ) == 0;
				if (!alternative) {
					left2kill = 8;
				}
				
				given = false;
				processed = false;
				depth = Dungeon.depth;
				
				do {
					weapon = (Weapon)Generator.random( Generator.Category.WEAPON );
				} while (weapon instanceof MissileWeapon);
				armor = (Armor)Generator.random( Generator.Category.ARMOR );
					
				for (int i=0; i < 3; i++) {
					Item another;
					do {
						another = Generator.random( Generator.Category.WEAPON );
					} while (another instanceof MissileWeapon);
					if (another.level > weapon.level) {
						weapon = (Weapon)another;
					}
					another = Generator.random( Generator.Category.ARMOR );
					if (another.level > armor.level) {
						armor = (Armor)another;
					}
				}
				weapon.identify();
				armor.identify();
			}
		}
		
		public static void process( int pos ) {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				if (alternative) {
					
					FetidRat rat = new FetidRat();
					rat.state = Mob.State.WANDERING;
					rat.pos = Dungeon.level.randomRespawnCell();
					if (rat.pos != -1) {
						GameScene.add( rat );
						processed = true;
					}
					
				} else {
					
					if (Random.Int( left2kill ) == 0) {
						Dungeon.level.drop( new DriedRose(), pos ).sprite.drop();
						processed = true;
					} else {
						left2kill--;
					}
					
				}
			}
		}
		
		public static void complete() {
			weapon = null;
			armor = null;
			
			Journal.remove( Journal.Feature.GHOST );
		}
	}
	
	public static class FetidRat extends Mob {

		{
			name = "fetid rat";
			spriteClass = FetidRatSprite.class;
			
			HP = HT = 15;
			defenseSkill = 5;
			
			EXP = 0;
			maxLvl = 5;	
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 2, 6 );
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 12;
		}
		
		@Override
		public int dr() {
			return 2;
		}
		
		@Override
		public int defenseProc( Char enemy, int damage ) {
			
			GameScene.add( Blob.seed( pos, 20, ParalyticGas.class ) );
			
			return super.defenseProc(enemy, damage);
		}
		
		@Override
		public void die( Object cause ) {
			super.die( cause );
			
			Dungeon.level.drop( new RatSkull(), pos ).sprite.drop();
		}
		
		@Override
		public String description() {
			return
				"This marsupial rat is much larger, than a regular one. It is surrounded by a foul cloud.";
		}
		
		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
		static {
			IMMUNITIES.add( Paralysis.class );
		}
		
		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}
}
