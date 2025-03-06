/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class BeamingRay extends TargetedClericSpell {

	public static BeamingRay INSTANCE = new BeamingRay();

	@Override
	public int icon() {
		return HeroIcon.BEAMING_RAY;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 4*Dungeon.hero.pointsInTalent(Talent.BEAMING_RAY), 30 + 5*Dungeon.hero.pointsInTalent(Talent.BEAMING_RAY)) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	public int targetingFlags() {
		return Ballistica.STOP_TARGET;
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero)
				&& hero.hasTalent(Talent.BEAMING_RAY)
				&& (PowerOfMany.getPoweredAlly() != null || Stasis.getStasisAlly() != null);
	}

	@Override
	protected void onTargetSelected(HolyTome tome, Hero hero, Integer target) {
		if (target == null){
			return;
		}

		Char ally = PowerOfMany.getPoweredAlly();

		if (ally == null){
			//temporary, for distance checks
			ally = Dungeon.hero;
		}

		int telePos = target;

		if (!Dungeon.level.insideMap(telePos)){
			GLog.w(Messages.get(this, "no_space"));
			return;
		}

		if (Dungeon.level.solid[telePos] || !Dungeon.level.heroFOV[telePos] || Actor.findChar(telePos) != null){
			telePos = -1;
			for (int i : PathFinder.NEIGHBOURS8){
				if (Actor.findChar(target+i) == null && Dungeon.level.heroFOV[target+i]
						&& (Dungeon.level.passable[target+i] || (ally.flying && Dungeon.level.avoid[target+i])) ){
					if (telePos == -1 || Dungeon.level.trueDistance(telePos, ally.pos) > Dungeon.level.trueDistance(target+i, ally.pos)){
						telePos =  target+i;
					}
				}
			}
		}

		if (telePos == -1){
			GLog.w(Messages.get(this, "no_space"));
			return;
		}

		if (ally == Dungeon.hero){
			ally = Stasis.getStasisAlly();
		}

		int range = 4*hero.pointsInTalent(Talent.BEAMING_RAY);
		if (Char.hasProp(ally, Char.Property.IMMOVABLE)){
			range /= 2;
		}
		if (Dungeon.level.distance(ally.pos, telePos) > range){
			GLog.w(Messages.get(this, "out_of_range"));
			return;
		}

		Char chTarget = null;
		if (Actor.findChar(target) != null && Actor.findChar(target).alignment == Char.Alignment.ENEMY){
			chTarget = Actor.findChar(target);
		}

		if (ally == Stasis.getStasisAlly()){
			ally.pos = telePos;
			GameScene.add((Mob) ally);
			hero.buff(Stasis.StasisBuff.class).detach();
			hero.sprite.parent.add(
					new Beam.SunRay(hero.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(telePos)));

			if (ally.buff(LifeLink.class) != null){
				Buff.prolong(Dungeon.hero, LifeLink.class, ally.buff(LifeLink.class).cooldown()).object = ally.id();
			}
		} else {
			hero.sprite.parent.add(
					new Beam.SunRay(ally.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(telePos)));
		}

		hero.sprite.zap(telePos);
		ScrollOfTeleportation.appear(ally, telePos);

		if (chTarget == null){
			for (Char ch : Actor.chars()){
				if (ch.alignment == Char.Alignment.ENEMY && Dungeon.level.distance(ch.pos, telePos) <= 4){
					if (chTarget == null || Dungeon.level.trueDistance(chTarget.pos, ally.pos) < Dungeon.level.trueDistance(ch.pos,  ally.pos)) {
						chTarget = ch;
					}
				}
			}
		}

		if (chTarget != null) {
			if (ally instanceof DirectableAlly) {
				((DirectableAlly) ally).targetChar(chTarget);
			} else if (ally instanceof Mob) {
				((Mob) ally).aggro(chTarget);
			}
			FlavourBuff.prolong(ally, BeamingRayBoost.class, BeamingRayBoost.DURATION).object = chTarget.id();
		} else {
			if (ally instanceof DirectableAlly) {
				((DirectableAlly) ally).clearDefensingPos();
			}
			//just the buff with no target
			FlavourBuff.prolong(ally, BeamingRayBoost.class, BeamingRayBoost.DURATION);
		}

		hero.spendAndNext(Actor.TICK);
		Dungeon.observe();
		GameScene.updateFog();

		onSpellCast(tome, hero);
	}

	public static class BeamingRayBoost extends FlavourBuff {

		{
			type = buffType.POSITIVE;
		}

		public int object = 0;

		public static final float DURATION = 10f;

		private static final String OBJECT  = "object";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( OBJECT, object );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			object = bundle.getInt( OBJECT );
		}

		@Override
		public int icon() {
			return BuffIndicator.HOLY_WEAPON;
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

	}

}
