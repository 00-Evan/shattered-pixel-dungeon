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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
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
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Stasis extends ClericSpell {

	public static Stasis INSTANCE = new Stasis();

	@Override
	public int icon() {
		return HeroIcon.STASIS;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 20 + 20*Dungeon.hero.pointsInTalent(Talent.STASIS)) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero)
				&& hero.hasTalent(Talent.STASIS)
				&& (PowerOfMany.getPoweredAlly() != null || hero.buff(StasisBuff.class) != null);
	}

	@Override
	public float chargeUse(Hero hero) {
		if (hero.buff(StasisBuff.class) != null){
			return 0;
		}
		return super.chargeUse(hero);
	}

	@Override
	public void onCast(HolyTome tome, Hero hero) {

		onSpellCast(tome, hero);

		if (hero.buff(StasisBuff.class) != null){
			hero.sprite.operate(hero.pos);
			hero.buff(StasisBuff.class).act();
			return;
		}

		Char ally = PowerOfMany.getPoweredAlly();

		hero.sprite.zap(ally.pos);
		MagicMissile.boltFromChar(hero.sprite.parent, MagicMissile.LIGHT_MISSILE, ally.sprite, hero.pos, null);

		//TODO preserve positive effects on ally, don't care about negative ones?
		Actor.remove(ally);
		Buff.prolong(hero, StasisBuff.class, 20 + 20*hero.pointsInTalent(Talent.STASIS)).stasisAlly = (Mob)ally;
		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

		if (hero.buff(LifeLink.class) != null && hero.buff(LifeLink.class).object == ally.id()){
			hero.buff(LifeLink.class).detach();
		}

		//TODO will need to make affordances for lots of edge cases like earth golem, wards limits, ghost hero, PoE ally, others?

		//TODO need code in beamign ray and life link to work here, also life link cleric spells?

		hero.spendAndNext(Actor.TICK);

	}

	public static class StasisBuff extends FlavourBuff {

		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.MANY_POWER;
		}

		@Override
		public float iconFadePercent() {
			int duration = 20 + 20*Dungeon.hero.pointsInTalent(Talent.STASIS);
			return Math.max(0, (duration - visualcooldown()) / duration);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", Messages.titleCase(stasisAlly.name()), dispTurns());
		}

		@Override
		public boolean act() {
			ArrayList<Integer> spawnPoints = new ArrayList<>();
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = target.pos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
					spawnPoints.add(p);
				}
			}
			if (spawnPoints.isEmpty()){
				spawnPoints.add(target.pos + PathFinder.NEIGHBOURS8[Random.Int(8)]);
			}
			stasisAlly.pos = Random.element(spawnPoints);
			stasisAlly.clearTime();
			GameScene.add(stasisAlly);

			ScrollOfTeleportation.appear(stasisAlly, stasisAlly.pos);
			Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

			return super.act();
		}

		Mob stasisAlly;

		private static final String ALLY = "ally";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(ALLY, stasisAlly);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			stasisAlly = (Mob)bundle.get(ALLY);
		}
	}

}
