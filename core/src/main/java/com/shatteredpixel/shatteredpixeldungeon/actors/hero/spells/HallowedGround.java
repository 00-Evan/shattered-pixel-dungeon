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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HallowedGround extends TargetedClericSpell {

	public static final HallowedGround INSTANCE = new HallowedGround();

	@Override
	public int icon() {
		return HeroIcon.HALLOWED_GROUND;
	}

	@Override
	public float chargeUse(Hero hero) {
		return 2;
	}

	@Override
	public int targetingFlags() {
		return Ballistica.STOP_TARGET;
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero) && hero.hasTalent(Talent.HALLOWED_GROUND);
	}

	@Override
	protected void onTargetSelected(HolyTome tome, Hero hero, Integer target) {

		if (target == null){
			return;
		}

		if (Dungeon.level.solid[target] || !Dungeon.level.heroFOV[target]){
			GLog.w(Messages.get(this, "invalid_target"));
			return;
		}

		ArrayList<Char> affected = new ArrayList<>();

		PathFinder.buildDistanceMap(target, BArray.not(Dungeon.level.solid, null), hero.pointsInTalent(Talent.HALLOWED_GROUND));
		for (int i = 0; i < Dungeon.level.length(); i++){
			if (PathFinder.distance[i] != Integer.MAX_VALUE){
				int c = Dungeon.level.map[i];
				if (c == Terrain.EMPTY || c == Terrain.EMBERS || c == Terrain.EMPTY_DECO) {
					Level.set( i, Terrain.GRASS);
					GameScene.updateMap( i );
					CellEmitter.get(i).burst(LeafParticle.LEVEL_SPECIFIC, 2);
				}
				GameScene.add(Blob.seed(i, 20, HallowedTerrain.class));
				CellEmitter.get(i).burst(ShaftParticle.FACTORY, 2);

				Char ch = Actor.findChar(i);
				if (ch != null){
					affected.add(ch);
				}
			}
		}

		Char ally = PowerOfMany.getPoweredAlly();
		if (ally != null && ally.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null){
			if (affected.contains(hero) && !affected.contains(ally)){
				affected.add(ally);
			} else if (!affected.contains(hero) && affected.contains(ally)){
				affected.add(hero);
			}
		}

		for (Char ch : affected){
			affectChar(ch);
		}

		//5 casts per hero level before furrowing
		Buff.affect(hero, HallowedFurrowTracker.class).countUp(1);

		Sample.INSTANCE.play(Assets.Sounds.MELD);
		hero.sprite.zap(target);
		hero.spendAndNext( 1f );

		onSpellCast(tome, hero);

	}

	private void affectChar( Char ch ){
		if (ch.alignment == Char.Alignment.ALLY){

			if (ch == Dungeon.hero || ch.HP == ch.HT){
				Buff.affect(ch, Barrier.class).incShield(10);
				ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, "10", FloatingText.SHIELDING );
			} else {
				int barrier = 10 - (ch.HT - ch.HP);
				barrier = Math.max(barrier, 0);
				ch.HP += 10 - barrier;
				ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(10-barrier), FloatingText.HEALING );
				if (barrier > 0){
					Buff.affect(ch, Barrier.class).incShield(barrier);
					ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(barrier), FloatingText.SHIELDING );
				}
			}
		} else if (!ch.flying) {
			Buff.affect(ch, Roots.class, 1f);
		}
	}

	public String desc(){
		int area = 1 + 2*Dungeon.hero.pointsInTalent(Talent.HALLOWED_GROUND);
		return Messages.get(this, "desc", area) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	public static class HallowedTerrain extends Blob {

		@Override
		protected void evolve() {

			int cell;

			Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );

			ArrayList<Char> affected = new ArrayList<>();

			// on avg, hallowed ground produces 9/17/25 tiles of grass, 100/67/50% of total tiles
			int chance = 10 + 10*Dungeon.hero.pointsInTalent(Talent.HALLOWED_GROUND);

			for (int i = area.left-1; i <= area.right; i++) {
				for (int j = area.top-1; j <= area.bottom; j++) {
					cell = i + j*Dungeon.level.width();
					if (cur[cell] > 0) {

						//fire destroys hallowed terrain
						if (fire != null && fire.volume > 0 && fire.cur[cell] > 0){
							off[cell] = cur[cell] = 0;
							continue;
						}

						int c = Dungeon.level.map[cell];
						if (c == Terrain.GRASS && Dungeon.level.plants.get(c) == null) {
							if (Random.Int(chance) == 0) {
								if (!Regeneration.regenOn()
										|| (Dungeon.hero.buff(HallowedFurrowTracker.class) != null && Dungeon.hero.buff(HallowedFurrowTracker.class).count() > 5)){
									Level.set(cell, Terrain.FURROWED_GRASS);
								} else {
									Level.set(cell, Terrain.HIGH_GRASS);
								}
								GameScene.updateMap(cell);
								CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 5);
							}
						} else if (c == Terrain.EMPTY || c == Terrain.EMBERS || c == Terrain.EMPTY_DECO) {
							Level.set(cell, Terrain.GRASS);
							GameScene.updateMap(cell);
							CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 2);
						}

						Char ch = Actor.findChar(cell);
						if (ch != null){
							affected.add(ch);
						}

						off[cell] = cur[cell] - 1;
						volume += off[cell];
					} else {
						off[cell] = 0;
					}
				}
			}

			Char ally = PowerOfMany.getPoweredAlly();
			if (ally != null && ally.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null){
				if (affected.contains(Dungeon.hero) && !affected.contains(ally)){
					affected.add(ally);
				} else if (!affected.contains(Dungeon.hero) && affected.contains(ally)){
					affected.add(Dungeon.hero);
				}
			}

			for (Char ch :affected){
				affectChar(ch);
			}

		}

		private void affectChar( Char ch ){
			if (ch.alignment == Char.Alignment.ALLY){
				if (ch == Dungeon.hero || ch.HP == ch.HT){
					Buff.affect(ch, Barrier.class).incShield(1);
					ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, "1", FloatingText.SHIELDING );
				} else {
					ch.HP++;
					ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, "1", FloatingText.HEALING );
				}
			} else if (!ch.flying && ch.buff(Roots.class) == null){
				Buff.prolong(ch, Cripple.class, 1f);
			}
		}

		@Override
		public void use(BlobEmitter emitter) {
			super.use( emitter );
			emitter.pour( ShaftParticle.FACTORY, 1f );
		}

		@Override
		public String tileDesc() {
			return Messages.get(this, "desc");
		}
	}

	public static class HallowedFurrowTracker extends CounterBuff{{revivePersists = true;}}

}
