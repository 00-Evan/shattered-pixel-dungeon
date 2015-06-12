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
package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ResultDescriptions;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.HashSet;

public class WandOfTransfusion extends Wand {

	{
		name = "Wand of Transfusion";
		image = ItemSpriteSheet.WAND_TRANSFUSION;

		collisionProperties = Ballistica.PROJECTILE;
	}

	private boolean freeCharge = false;

	//FIXME: this is sloppy
	private static HashSet<Class> undeadMobs = new HashSet<Class>(Arrays.asList(
			//Any Location
			Wraith.class,
			//Sewers
			Ghost.FetidRat.class,
			//Prison
			Skeleton.class,
			//City
			Warlock.class, Monk.class, Senior.class,
			King.class, King.Undead.class,
			//Halls
			Succubus.class,
			Yog.RottingFist.class
	));

	@Override
	protected void onZap(Ballistica beam) {

		for (int c : beam.subPath(0, beam.dist))
			CellEmitter.center(c).burst( BloodParticle.BURST, 1 );

		int cell = beam.collisionPos;

		Char ch = Actor.findChar(cell);
		Heap heap = Dungeon.level.heaps.get(cell);

		//this wand does a bunch of different things depending on what it targets.

		//if we find a character..
		if (ch != null && ch instanceof Mob){

			//heals an ally, or charmed/corrupted enemy
			if (((Mob) ch).ally || ch.buff(Charm.class) != null || ch.buff(Corruption.class) != null){

				int missingHP = ch.HT - ch.HP;
				//heals 30%+3%*lvl missing HP.
				int healing = (int)Math.ceil((missingHP * (0.30f+(0.03f*level))));
				ch.HP += healing;
				ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1 + level / 2);
				ch.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healing);

			//harms the undead
			} else if (undeadMobs.contains(ch.getClass())){

				//deals 30%+5%*lvl total HP.
				int damage = (int) Math.ceil(ch.HT*(0.3f+(0.05f*level)));
				ch.damage(damage, this);
				ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + level);
				Sample.INSTANCE.play(Assets.SND_BURNING);

			//charms an enemy
			} else {

				float duration = 5+level;
				Buff.affect(ch, Charm.class, Charm.durationFactor(ch) * duration).object = curUser.id();

				duration *= Random.Float(0.75f, 1f);
				Buff.affect(curUser, Charm.class, Charm.durationFactor(ch) * duration).object = ch.id();

				ch.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
				curUser.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );

			}


		//if we find an item...
		} else if (heap != null && heap.type == Heap.Type.HEAP){
			Item item = heap.peek();

			//30% + 10%*lvl chance to uncurse the item and reset it to base level if degraded.
			if (item != null && Random.Float() <= 0.3f+level*0.1f){
				if (item.cursed){
					item.cursed = false;
					CellEmitter.get(cell).start( ShadowParticle.UP, 0.05f, 10 );
					Sample.INSTANCE.play(Assets.SND_BURNING);
				}

				int lvldiffFromBase = item.level - (item instanceof Ring ? 1 : 0);
				if (lvldiffFromBase < 0){
					item.upgrade(-lvldiffFromBase);
					CellEmitter.get(cell).start(Speck.factory(Speck.UP), 0.2f, 3);
					Sample.INSTANCE.play(Assets.SND_EVOKE);
				}
			}

		//if we find some trampled grass...
		} else if (Dungeon.level.map[cell] == Terrain.GRASS) {

			//regrow one grass tile, suuuuuper useful...
			Dungeon.level.set(cell, Terrain.HIGH_GRASS);
			GameScene.updateMap(cell);
			CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 4);

		//If we find embers...
		} else if (Dungeon.level.map[cell] == Terrain.EMBERS) {

			//30% + 3%*lvl chance to grow a random plant, or just regrow grass.
			if (Random.Float() <= 0.3f+level*0.03f) {
				Dungeon.level.plant((Plant.Seed) Generator.random(Generator.Category.SEED), cell);
				CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 8);
				GameScene.updateMap(cell);
			} else{
				Dungeon.level.set(cell, Terrain.HIGH_GRASS);
				GameScene.updateMap(cell);
				CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}

		} else
			return; //don't damage the hero if we can't find a target;

		if (!freeCharge) {
			damageHero();
		} else {
			freeCharge = false;
		}
	}


	//this wand costs health too
	private void damageHero(){
		// 15% of max hp
		int damage = (int)Math.ceil(curUser.HT*0.15f);
		curUser.damage(damage, this);

		if (!curUser.isAlive()){
			Dungeon.fail( Utils.format(ResultDescriptions.ITEM, name) );
			GLog.n("You killed yourself with your own Wand of Transfusion...");
		}
	}

	@Override
	protected int initialCharges() {
		return 1;
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		// lvl 0 - 10%
		// lvl 1 - 18%
		// lvl 2 - 25%
		if (Random.Int( level + 10 ) >= 9){
			//grants a free use of the staff
			freeCharge = true;
			GLog.p("Your staff is charged with the life energy of your enemy!");
			attacker.sprite.emitter().burst(BloodParticle.BURST, 20);
		}
	}

	@Override
	protected void fx(Ballistica beam, Callback callback) {
		curUser.sprite.parent.add(
				new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.tileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0xCC0000 );
		particle.am = 0.6f;
		particle.setLifespan(0.8f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2.5f);
		particle.radiateXY(1f);
	}

	private static final String FREECHARGE = "freecharge";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		freeCharge = bundle.getBoolean( FREECHARGE );
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( FREECHARGE, freeCharge );
	}

	@Override
	public String desc() {
		return "A fairly plainly shaped wand, it stands out due to its magenta hue and pitch black gem at the tip.\n" +
				"\n" +
				"This wand will take some of your life energy and blast it at a target. This effect is very versatile: " +
				"allies will be healed, enemies will be temporarily charmed, and hostile undead will take considerable damage. " +
				"The life force effect can also be potent at dispelling curses as well. " +
				"The life energy drain is significant though, using this wand will deal damage to you in addition to consuming charges.";
	}
}
