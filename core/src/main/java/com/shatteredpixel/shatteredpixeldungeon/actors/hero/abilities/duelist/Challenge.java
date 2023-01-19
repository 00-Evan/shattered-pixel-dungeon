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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class Challenge extends ArmorAbility {

	{
		baseChargeUse = 35;
	}

	@Override
	public int icon() {
		return HeroIcon.CHALLENGE;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	public int targetedPos(Char user, int dst) {
		return dst;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		if (target == null || !Dungeon.level.heroFOV[target]){
			return;
		}

		Char targetCh = Actor.findChar(target);

		if (hero.buff(DuelParticipant.class) != null){
			GLog.w(Messages.get(this, "already_dueling"));
			return;
		}

		if (targetCh != null){

			if (targetCh.alignment == hero.alignment){
				GLog.w(Messages.get(this, "ally_target"));
				return;
			}

			boolean[] passable = Dungeon.level.passable.clone();
			for (Char c : Actor.chars()) {
				if (c != hero) passable[c.pos] = false;
			}

			PathFinder.buildDistanceMap(targetCh.pos, passable);
			if (PathFinder.distance[hero.pos] == Integer.MAX_VALUE){
				GLog.w(Messages.get(this, "unreachable_target"));
				return;
			}

			if (Dungeon.level.distance(hero.pos, targetCh.pos) >= 5){
				GLog.w(Messages.get(this, "distant_target"));
				return;
			}

			boolean bossTarget = Char.hasProp(targetCh, Char.Property.BOSS);
			for (Char toFreeze : Actor.chars()){
				if (toFreeze != targetCh && toFreeze.alignment != hero.alignment
					&& (!bossTarget || !(Char.hasProp(targetCh, Char.Property.BOSS) || Char.hasProp(targetCh, Char.Property.BOSS_MINION)))) {
					Actor.delayChar(toFreeze, DuelParticipant.DURATION);
					Buff.affect(toFreeze, SpectatorFreeze.class, DuelParticipant.DURATION);
				}
			}

			Buff.affect(targetCh, DuelParticipant.class);
			Buff.affect(hero, DuelParticipant.class);
			if (targetCh instanceof Mob){
				((Mob) targetCh).aggro(hero);
			}

			GameScene.flash(0x80FFFFFF);
			Sample.INSTANCE.play(Assets.Sounds.DESCEND);

			armor.charge -= chargeUse( hero );
			armor.updateQuickslot();
			hero.sprite.zap(target);

			hero.next();
		}
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.CLOSE_THE_GAP, Talent.INVIGORATING_VICTORY, Talent.ELIMINATION_MATCH, Talent.HEROIC_ENERGY};
	}

	public static class DuelParticipant extends Buff {

		public static float DURATION = 10f;

		private int left = (int)DURATION;

		@Override
		public int icon() {
			return BuffIndicator.CHALLENGE;
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - left) / DURATION);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(left);
		}

		@Override
		public boolean act() {

			left--;
			if (left == 0) {
				detach();
			} else {
				Char other = null;
				for (Char ch : Actor.chars()){
					if (ch != target && ch.buff(DuelParticipant.class) != null){
						other = ch;
					}
				}

				if (other == null){
					detach();
				} else if (Dungeon.level.distance(target.pos, other.pos) > 5){
					detach();
				}
			}

			spend(TICK);
			return true;
		}

		@Override
		public void detach() {
			super.detach();
			if (!target.isAlive()) {
				if (target.alignment != Dungeon.hero.alignment){
					Sample.INSTANCE.play(Assets.Sounds.BOSS);
					GameScene.flash(0x80FFFFFF);
				}

				for (Char ch : Actor.chars()) {
					if (ch.buff(SpectatorFreeze.class) != null) {
						ch.buff(SpectatorFreeze.class).detach();
					}
					if (ch.buff(DuelParticipant.class) != null && ch != target) {
						ch.buff(DuelParticipant.class).detach();
					}
				}
			} else if (target == Dungeon.hero){
				GameScene.flash(0x80FFFFFF);
			}
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", left);
		}

		private static final String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(LEFT, left);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			left = bundle.getInt(LEFT);
		}
	}

	public static class SpectatorFreeze extends FlavourBuff {

		@Override
		public void fx(boolean on) {
			if (on) {
				target.sprite.add(CharSprite.State.DARKENED);
				target.sprite.add(CharSprite.State.PARALYSED);
			} else {
				//allies can't be spectator frozen, so just check doom
				if (target.buff(Doom.class) == null) target.sprite.remove(CharSprite.State.DARKENED);
				if (target.paralysed == 0) target.sprite.remove(CharSprite.State.PARALYSED);
			}
		}

		@Override
		public void detach(){
			super.detach();
			if (cooldown() > 0) {
				Actor.delayChar(target, -cooldown());
			}
		}

		{
			immunities.addAll(new BlobImmunity().immunities());
		}

	}
}
