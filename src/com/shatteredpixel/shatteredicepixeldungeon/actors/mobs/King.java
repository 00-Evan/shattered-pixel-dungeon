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
package com.shatteredpixel.shatteredicepixeldungeon.actors.mobs;

import java.util.HashSet;

import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Vertigo;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.Badges;
import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredicepixeldungeon.items.ArmorKit;
import com.shatteredpixel.shatteredicepixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredicepixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredicepixeldungeon.items.wands.WandOfBlink;
import com.shatteredpixel.shatteredicepixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredicepixeldungeon.items.weapon.enchantments.Death;
import com.shatteredpixel.shatteredicepixeldungeon.levels.CityBossLevel;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.KingSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.UndeadSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class King extends Mob {
	
	private static final int MAX_ARMY_SIZE	= 5;
	
	{
		name = "King of Dwarves";
		spriteClass = KingSprite.class;
		
		HP = HT = 300;
		EXP = 40;
		defenseSkill = 25;
		
		Undead.count = 0;
	}
	
	private boolean nextPedestal = true;
	
	private static final String PEDESTAL = "pedestal";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( PEDESTAL, nextPedestal );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		nextPedestal = bundle.getBoolean( PEDESTAL );
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 20, 38 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 32;
	}
	
	@Override
	public int dr() {
		return 14;
	}
	
	@Override
	public String defenseVerb() {
		return "parried";
	}
	
	@Override
	protected boolean getCloser( int target ) {
		return canTryToSummon() ? 
			super.getCloser( CityBossLevel.pedestal( nextPedestal ) ) : 
			super.getCloser( target );
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return canTryToSummon() ? 
			pos == CityBossLevel.pedestal( nextPedestal ) : 
			Level.adjacent( pos, enemy.pos );
	}
	
	private boolean canTryToSummon() {
		if (Undead.count < maxArmySize()) {
			Char ch = Actor.findChar( CityBossLevel.pedestal( nextPedestal ) );
			return ch == this || ch == null;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean attack( Char enemy ) {
		if (canTryToSummon() && pos == CityBossLevel.pedestal( nextPedestal )) {
			summon();
			return true;
		} else {
			if (Actor.findChar( CityBossLevel.pedestal( nextPedestal ) ) == enemy) {
				nextPedestal = !nextPedestal;
			}
			return super.attack(enemy);
		}
	}
	
	@Override
	public void die( Object cause ) {

		GameScene.bossSlain();
		Dungeon.level.drop( new ArmorKit(), pos ).sprite.drop();
		Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();
		
		super.die( cause );
		
		Badges.validateBossSlain();
		
		yell( "You cannot kill me, " + Dungeon.hero.givenName() + "... I am... immortal..." );
	}
	
	private int maxArmySize() {
		return 1 + MAX_ARMY_SIZE * (HT - HP) / HT;
	}
	
	private void summon() {

		nextPedestal = !nextPedestal;
		
		sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );		
		Sample.INSTANCE.play( Assets.SND_CHALLENGE );
		
		boolean[] passable = Level.passable.clone();
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				passable[((Char)actor).pos] = false;
			}
		}
		
		int undeadsToSummon = maxArmySize() - Undead.count;

		PathFinder.buildDistanceMap( pos, passable, undeadsToSummon );
		PathFinder.distance[pos] = Integer.MAX_VALUE;
		int dist = 1;
		
	undeadLabel:
		for (int i=0; i < undeadsToSummon; i++) {
			do {
				for (int j=0; j < Level.LENGTH; j++) {
					if (PathFinder.distance[j] == dist) {
						
						Undead undead = new Undead();
						undead.pos = j;
						GameScene.add( undead );
						
						WandOfBlink.appear( undead, j );
						new Flare( 3, 32 ).color( 0x000000, false ).show( undead.sprite, 2f ) ;
						
						PathFinder.distance[j] = Integer.MAX_VALUE;
						
						continue undeadLabel;
					}
				}
				dist++;
			} while (dist < undeadsToSummon);
		}
		
		yell( "Arise, slaves!" );
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( "How dare you!" );
	}
	
	@Override
	public String description() {
		return
			"The last king of dwarves was known for his deep understanding of processes of life and death. " +
			"He has persuaded members of his court to participate in a ritual, that should have granted them " +
			"eternal youthfulness. In the end he was the only one, who got it - and an army of undead " +
			"as a bonus.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
		RESISTANCES.add( WandOfDisintegration.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Paralysis.class );
		IMMUNITIES.add( Vertigo.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	public static class Undead extends Mob {
		
		public static int count = 0;
		
		{
			name = "undead dwarf";
			spriteClass = UndeadSprite.class;
			
			HP = HT = 28;
			defenseSkill = 15;
			
			EXP = 0;
			
			state = WANDERING;
		}
		
		@Override
		protected void onAdd() {
			count++;
			super.onAdd();
		}
		
		@Override
		protected void onRemove() {
			count--;
			super.onRemove();
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 12, 16 );
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 16;
		}
		
		@Override
		public int attackProc( Char enemy, int damage ) {
			if (Random.Int( MAX_ARMY_SIZE ) == 0) {
				Buff.prolong( enemy, Paralysis.class, 1 );
			}
			
			return damage;
		}
		
		@Override
		public void damage( int dmg, Object src ) {
			super.damage( dmg, src );
			if (src instanceof ToxicGas) {		
				((ToxicGas)src).clear( pos );
			}
		}
		
		@Override
		public void die( Object cause ) {
			super.die( cause );
			
			if (Dungeon.visible[pos]) {
				Sample.INSTANCE.play( Assets.SND_BONES );
			}
		}
		
		@Override
		public int dr() {
			return 5;
		}
		
		@Override
		public String defenseVerb() {
			return "blocked";
		}
		
		@Override
		public String description() {
			return
				"These undead dwarves, risen by the will of the King of Dwarves, were members of his court. " +
				"They appear as skeletons with a stunning amount of facial hair.";
		}
		
		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
		static {
			IMMUNITIES.add( Death.class );
			IMMUNITIES.add( Paralysis.class );
		}
		
		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}
}
