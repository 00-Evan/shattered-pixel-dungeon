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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrystalSpireSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class CrystalSpire extends Mob {

	{
		HP = HT = 200;
		spriteClass = CrystalSpireSprite.class;

		state = PASSIVE;

		alignment = Alignment.NEUTRAL;

		properties.add(Property.IMMOVABLE);
		properties.add(Property.MINIBOSS);
		properties.add(Property.INORGANIC);
	}

	@Override
	protected boolean act() {
		alerted = false;
		return super.act();
	}

	//TODO just whaling on this thing is boring, it has to do something in retaliation other than aggroing guardians

	@Override
	public void beckon(int cell) {
		//do nothing
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public void damage(int dmg, Object src) {
		if (!(src instanceof Pickaxe) ){
			dmg = 0;
		}
		super.damage(dmg, src);
	}

	@Override
	public boolean interact(Char c) {
		if (c == Dungeon.hero){
			final Pickaxe p = Dungeon.hero.belongings.getItem(Pickaxe.class);

			if (p == null){
				//maybe a game log entry here?
				return true;
			}

			Dungeon.hero.sprite.attack(pos, new Callback() {
				@Override
				public void call() {
					//does its own special damage calculation that's only influenced by pickaxe level
					int dmg = Random.NormalIntRange(3+p.buffedLvl(), 15+3*p.buffedLvl());

					damage(dmg, p);
					sprite.bloodBurstA(Dungeon.hero.sprite.center(), dmg);
					sprite.flash();

					if (isAlive()) {
						Sample.INSTANCE.play(Assets.Sounds.SHATTER, 1f, Random.Float(1.15f, 1.25f));
						((CrystalSpireSprite) sprite).updateIdle();
					} else {
						Sample.INSTANCE.play(Assets.Sounds.SHATTER);
						Sample.INSTANCE.playDelayed(Assets.Sounds.ROCKS, 0.1f);
						PixelScene.shake( 3, 0.7f );
						Blacksmith.Quest.beatBoss();
					}

					for (Char ch : Actor.chars()){
						if (ch instanceof CrystalWisp){
							((CrystalWisp)ch).beckon(pos);
						} else if (ch instanceof CrystalGuardian){
							//TODO we want some way to encourage the player to explore first, but also not disturb guardians.
							// maybe wisps alone are enough for this?
							if (((CrystalGuardian) ch).state == ((CrystalGuardian) ch).SLEEPING){
								Buff.affect(ch, Haste.class, 6f);
							}
							((CrystalGuardian) ch).beckon(pos);
							if (((CrystalGuardian) ch).state != HUNTING){
								((CrystalGuardian) ch).aggro(Dungeon.hero);
							}
						}
					}

					Dungeon.hero.spendAndNext(Actor.TICK);
				}
			});
			return false;

		}
		return true;
	}

	public CrystalSpire(){
		super();
		switch (Random.Int(3)){
			case 0: default:
				spriteClass = CrystalSpireSprite.Blue.class;
				break;
			case 1:
				spriteClass = CrystalSpireSprite.Green.class;
				break;
			case 2:
				spriteClass = CrystalSpireSprite.Red.class;
				break;
		}
	}

	@Override
	public float spawningWeight() {
		return 0;
	}

	public static final String SPRITE = "sprite";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SPRITE, spriteClass);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		spriteClass = bundle.getClass(SPRITE);
	}

	{
		immunities.add( Paralysis.class );
		immunities.add( Amok.class );
		immunities.add( Sleep.class );
		immunities.add( Terror.class );
		immunities.add( Dread.class );
		immunities.add( Vertigo.class );
	}

}
