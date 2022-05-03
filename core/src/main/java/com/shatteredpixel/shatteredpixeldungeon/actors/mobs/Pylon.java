/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PylonSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Pylon extends Mob {
	private int var2;
	{
		spriteClass = PylonSprite.class;

		HP = HT = 50;

		maxLvl = -2;

		properties.add(Property.MINIBOSS);
		properties.add(Property.INORGANIC);
		properties.add(Property.ELECTRIC);
		properties.add(Property.IMMOVABLE);

		state = PASSIVE;
		alignment = Alignment.NEUTRAL;
	}

	private int targetNeighbor = Random.Int(8);

	@Override
	protected boolean act() {
		spend(TICK);

		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null) {
			int n;
			do {
				n = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Dungeon.level.passable[n] && !Dungeon.level.avoid[n]);
			Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
		}

		if (alignment == Alignment.NEUTRAL){
			return true;
		}

		int cell1 = pos + PathFinder.CIRCLE8[targetNeighbor];
		int cell2 = pos + PathFinder.CIRCLE8[(targetNeighbor+4)%8];

		sprite.flash();
		if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[cell1] || Dungeon.level.heroFOV[cell2]) {
			sprite.parent.add(new Lightning(DungeonTilemap.raisedTileCenterToWorld(cell1),
					DungeonTilemap.raisedTileCenterToWorld(cell2), null));
			CellEmitter.get(cell1).burst(SparkParticle.FACTORY, 3);
			CellEmitter.get(cell2).burst(SparkParticle.FACTORY, 3);
			Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
		}

		shockChar(Actor.findChar(cell1));
		shockChar(Actor.findChar(cell2));

		targetNeighbor = (targetNeighbor+1)%8;

		return true;
	}

	private void shockChar( Char ch ){
		if (ch != null && !(ch instanceof NewDM300)){
			ch.sprite.flash();
			ch.damage(Random.NormalIntRange(10, 20), new Electricity());

			if (ch == Dungeon.hero && !ch.isAlive()){
				Dungeon.fail(NewDM300.class);
				GLog.n( Messages.get(Electricity.class, "ondeath") );
				for(int i = 0;i <= (6 + (1 -(float)HP/HT)) ; i++){
					int newPos = 0;
					do {
						newPos = Random.Int(Dungeon.level.length());
					} while (
							Dungeon.level.solid[newPos] ||
									Dungeon.level.distance(newPos, enemy.pos) < 12 ||
									Actor.findChar(newPos) != null);
					if (Random.Int(1000) <= 500){
						FlameB01 rat = new FlameB01();
						rat.state = rat.WANDERING;
						rat.pos = newPos;
						GameScene.add(rat);
						rat.beckon(pos );
					}else if(Random.Int(500) <= 250){
						MolotovHuntsman rat = new MolotovHuntsman();
						rat.state = rat.WANDERING;
						rat.pos = newPos;
						GameScene.add(rat);
						rat.beckon(pos );
					}else if(Random.Int(250) <= 125){
						FetidRat rat = new FetidRat ();
						rat.state = rat.WANDERING;
						rat.pos = newPos;
						GameScene.add(rat);
						rat.beckon(pos );
					}else if(Random.Int(125) <= 75){
						Spinner rat = new Spinner();
						rat.state = rat.WANDERING;
						rat.pos = newPos;
						GameScene.add(rat);
						rat.beckon(pos );
					}else if(Random.Int(75) <= 37){
						DM100 rat = new DM100();
						rat.state = rat.WANDERING;
						rat.pos = newPos;
						GameScene.add(rat);
						rat.beckon(pos );
					}else if(Random.Int(37) <= 18){
						BlackHost rat = new BlackHost();
						rat.state = rat.WANDERING;
						rat.pos = newPos;
						GameScene.add(rat);
						rat.beckon(pos );
					}else{
						FireGhost rat = new FireGhost();
						rat.state = rat.WANDERING;
						rat.pos = newPos;
						GameScene.add(rat);
						rat.beckon(pos );
						int i2 = this.var2 + 10;
						SummoningTrap one = new SummoningTrap();
						one.pos = this.pos;
						one.activate();
						AlarmTrap two = new AlarmTrap();
						two.pos = this.pos;
						two.activate();
					}
					yell( Messages.get(this, "arise") );

				}
			}
		}
	}

	public void activate(){
		alignment = Alignment.ENEMY;
		((PylonSprite) sprite).activate();
	}

	@Override
	public CharSprite sprite() {
		PylonSprite p = (PylonSprite) super.sprite();
		if (alignment != Alignment.NEUTRAL) p.activate();
		return p;
	}

	@Override
	public void notice() {
		//do nothing
	}

	@Override
	public String description() {
		if (alignment == Alignment.NEUTRAL){
			return Messages.get(this, "desc_inactive");
		} else {
			return Messages.get(this, "desc_active");
		}
	}

	@Override
	public boolean interact(Char c) {
		return true;
	}

	@Override
	public void add(Buff buff) {
		//immune to all buffs/debuffs when inactive
		if (alignment != Alignment.NEUTRAL) {
			super.add(buff);
		}
	}

	@Override
	public void damage(int dmg, Object src) {
		//immune to damage when inactive
		if (alignment == Alignment.NEUTRAL){
			return;
		}
		if (dmg >= 15){
			//takes 15/16/17/18/19/20 dmg at 15/17/20/24/29/36 incoming dmg
			dmg = 14 + (int)(Math.sqrt(8*(dmg - 14) + 1) - 1)/2;
		}
		super.damage(dmg, src);
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		((NewCavesBossLevel)Dungeon.level).eliminatePylon();
	}

	private static final String ALIGNMENT = "alignment";
	private static final String TARGET_NEIGHBOUR = "target_neighbour";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ALIGNMENT, alignment);
		bundle.put(TARGET_NEIGHBOUR, targetNeighbor);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		alignment = bundle.getEnum(ALIGNMENT, Alignment.class);
		targetNeighbor = bundle.getInt(TARGET_NEIGHBOUR);
	}

	{
		immunities.add( Paralysis.class );
		immunities.add( Amok.class );
		immunities.add( Sleep.class );
		immunities.add( ToxicGas.class );
		immunities.add( Terror.class );
		immunities.add( Vertigo.class );
	}

}
