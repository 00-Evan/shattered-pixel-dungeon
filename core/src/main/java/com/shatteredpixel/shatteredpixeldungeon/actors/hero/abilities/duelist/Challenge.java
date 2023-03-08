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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
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
	public float chargeUse( Hero hero ) {
		float chargeUse = super.chargeUse(hero);
		if (hero.buff(EliminationMatchTracker.class) != null){
			//reduced charge use by 20%/36%/50%/60%
			chargeUse *= Math.pow(0.795, hero.pointsInTalent(Talent.ELIMINATION_MATCH));
		}
		return chargeUse;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		if (target == null){
			return;
		}

		Char targetCh = Actor.findChar(target);
		if (targetCh == null || !Dungeon.level.heroFOV[target]){
			GLog.w(Messages.get(this, "no_target"));
			return;
		}

		if (hero.buff(DuelParticipant.class) != null){
			GLog.w(Messages.get(this, "already_dueling"));
			return;
		}

		if (targetCh.alignment == hero.alignment){
			GLog.w(Messages.get(this, "ally_target"));
			return;
		}

		boolean[] passable = Dungeon.level.passable.clone();
		for (Char c : Actor.chars()) {
			if (c != hero) passable[c.pos] = false;
		}
		PathFinder.buildDistanceMap(targetCh.pos, passable);
		int[] reachable = PathFinder.distance.clone();

		int blinkpos = hero.pos;
		if (hero.hasTalent(Talent.CLOSE_THE_GAP)){

			int blinkrange = 1 + hero.pointsInTalent(Talent.CLOSE_THE_GAP);
			PathFinder.buildDistanceMap(hero.pos, BArray.not(Dungeon.level.solid,null), blinkrange);

			for (int i = 0; i < PathFinder.distance.length; i++){
				if (PathFinder.distance[i] == Integer.MAX_VALUE
						|| reachable[i] == Integer.MAX_VALUE
						|| i == targetCh.pos){
					continue;
				}

				if (Dungeon.level.distance(i, targetCh.pos) < Dungeon.level.distance(blinkpos, targetCh.pos)){
					blinkpos = i;
				} else if (Dungeon.level.distance(i, targetCh.pos) == Dungeon.level.distance(blinkpos, targetCh.pos)){
					if (Dungeon.level.trueDistance(i, hero.pos) < Dungeon.level.trueDistance(blinkpos, hero.pos)){
						blinkpos = i;
					}
				}
			}
		}

		if (PathFinder.distance[blinkpos] == Integer.MAX_VALUE){
			GLog.w(Messages.get(this, "unreachable_target"));
			return;
		}

		if (Dungeon.level.distance(blinkpos, targetCh.pos) >= 5){
			GLog.w(Messages.get(this, "distant_target"));
			return;
		}

		if (blinkpos != hero.pos){
			Dungeon.hero.pos = blinkpos;
			Dungeon.level.occupyCell(Dungeon.hero);
			//prevents the hero from being interrupted by seeing new enemies
			Dungeon.observe();
			GameScene.updateFog();
			Dungeon.hero.checkVisibleMobs();

			Dungeon.hero.sprite.place( Dungeon.hero.pos );
			CellEmitter.get( Dungeon.hero.pos ).burst( Speck.factory( Speck.WOOL ), 6 );
			Sample.INSTANCE.play( Assets.Sounds.PUFF );
		}

		boolean bossTarget = Char.hasProp(targetCh, Char.Property.BOSS);
		for (Char toFreeze : Actor.chars()){
			if (toFreeze != targetCh && toFreeze.alignment != Char.Alignment.ALLY && !(toFreeze instanceof NPC)
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

		if (hero.buff(EliminationMatchTracker.class) != null){
			hero.buff(EliminationMatchTracker.class).detach();
		}
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.CLOSE_THE_GAP, Talent.INVIGORATING_VICTORY, Talent.ELIMINATION_MATCH, Talent.HEROIC_ENERGY};
	}

	public static class EliminationMatchTracker extends FlavourBuff{};

	public static class DuelParticipant extends Buff {

		public static float DURATION = 10f;

		private int left = (int)DURATION;
		private int takenDmg = 0;

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

		public void addDamage(int dmg){
			takenDmg += dmg;
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

				if (other == null
					|| target.alignment == other.alignment
					|| Dungeon.level.distance(target.pos, other.pos) > 5) {
					detach();
				}
			}

			spend(TICK);
			return true;
		}

		@Override
		public void detach() {
			super.detach();
			if (target != Dungeon.hero){
				if (!target.isAlive() || target.alignment == Dungeon.hero.alignment){
					Sample.INSTANCE.play(Assets.Sounds.BOSS);

					if (Dungeon.hero.hasTalent(Talent.INVIGORATING_VICTORY)){
						DuelParticipant heroBuff = Dungeon.hero.buff(DuelParticipant.class);

						int hpToHeal = 0;
						if (heroBuff != null){
							hpToHeal = heroBuff.takenDmg;
						}

						//heals for 30%/50%/65%/75% of taken damage plus 3/6/9/12 bonus, based on talent points
						hpToHeal = (int)Math.round(hpToHeal * (1f - Math.pow(0.707f, Dungeon.hero.pointsInTalent(Talent.INVIGORATING_VICTORY))));
						hpToHeal += 3*Dungeon.hero.pointsInTalent(Talent.INVIGORATING_VICTORY);
						hpToHeal = Math.min(hpToHeal, Dungeon.hero.HT - Dungeon.hero.HP);
						if (hpToHeal > 0){
							Dungeon.hero.HP += hpToHeal;
							Dungeon.hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.33f, 6 );
							Dungeon.hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Dewdrop.class, "heal", hpToHeal) );
						}
					}
				}

				for (Char ch : Actor.chars()) {
					if (ch.buff(SpectatorFreeze.class) != null) {
						ch.buff(SpectatorFreeze.class).detach();
					}
					if (ch.buff(DuelParticipant.class) != null && ch != target) {
						ch.buff(DuelParticipant.class).detach();
					}
				}
			} else {
				if (Dungeon.hero.isAlive()) {
					GameScene.flash(0x80FFFFFF);

					if (Dungeon.hero.hasTalent(Talent.ELIMINATION_MATCH)){
						Buff.affect(target, EliminationMatchTracker.class, 3);
					}
				}
			}
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", left);
		}

		private static final String LEFT = "left";
		private static final String TAKEN_DMG = "taken_dmg";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(LEFT, left);
			bundle.put(TAKEN_DMG, takenDmg);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			left = bundle.getInt(LEFT);
			takenDmg = bundle.getInt(TAKEN_DMG);
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
