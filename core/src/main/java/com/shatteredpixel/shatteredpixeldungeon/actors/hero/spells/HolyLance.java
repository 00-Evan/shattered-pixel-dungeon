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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class HolyLance extends TargetedClericSpell {

	public static final HolyLance INSTANCE = new HolyLance();

	@Override
	public int icon() {
		return HeroIcon.HOLY_LANCE;
	}

	@Override
	public String desc() {
		int min = 15 + 15*Dungeon.hero.pointsInTalent(Talent.HOLY_LANCE);
		int max = Math.round(27.5f + 27.5f*Dungeon.hero.pointsInTalent(Talent.HOLY_LANCE));
		return Messages.get(this, "desc", min, max) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero)
				&& hero.hasTalent(Talent.HOLY_LANCE)
				&& hero.buff(LanceCooldown.class) == null;
	}

	@Override
	public float chargeUse(Hero hero) {
		return 4;
	}

	@Override
	public int targetingFlags() {
		return Ballistica.PROJECTILE;
	}

	@Override
	protected void onTargetSelected(HolyTome tome, Hero hero, Integer target) {
		if (target == null){
			return;
		}

		Ballistica aim = new Ballistica(hero.pos, target, targetingFlags());

		if (Actor.findChar( aim.collisionPos ) == hero){
			GLog.i( Messages.get(Wand.class, "self_target") );
			return;
		}

		if (Actor.findChar(aim.collisionPos) != null) {
			QuickSlotButton.target(Actor.findChar(aim.collisionPos));
		} else {
			QuickSlotButton.target(Actor.findChar(target));
		}

		hero.sprite.zap( target );
		hero.busy();

		Sample.INSTANCE.play(Assets.Sounds.ZAP);

		Char enemy = Actor.findChar(aim.collisionPos);
		if (enemy != null) {
			((MissileSprite) hero.sprite.parent.recycle(MissileSprite.class)).
					reset(hero.sprite,
							enemy.sprite,
							new HolyLanceVFX(),
							new Callback() {
								@Override
								public void call() {
									int min = 15 + 15*Dungeon.hero.pointsInTalent(Talent.HOLY_LANCE);
									int max = Math.round(27.5f + 27.5f*Dungeon.hero.pointsInTalent(Talent.HOLY_LANCE));
									if (Char.hasProp(enemy, Char.Property.UNDEAD) || Char.hasProp(enemy, Char.Property.DEMONIC)){
										min = max;
									}
									enemy.damage(Random.NormalIntRange(min, max), HolyLance.this);
									Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.8f, 1f) );
									Sample.INSTANCE.play( Assets.Sounds.HIT_STAB, 1, Random.Float(0.8f, 1f) );

									enemy.sprite.burst(0xFFFFFFFF, 10);
									hero.spendAndNext(1f);
									onSpellCast(tome, hero);
									FlavourBuff.affect(hero, LanceCooldown.class, 50f);
								}
							});
		} else {
			((MissileSprite) hero.sprite.parent.recycle(MissileSprite.class)).
					reset(hero.sprite,
							target,
							new HolyLanceVFX(),
							new Callback() {
								@Override
								public void call() {
									Splash.at(target, 0xFFFFFFFF, 10);
									Dungeon.level.pressCell(aim.collisionPos);
									hero.spendAndNext(1f);
									onSpellCast(tome, hero);
									FlavourBuff.affect(hero, LanceCooldown.class, 50f);
								}
							});
		}

	}

	public static class HolyLanceVFX extends Item {

		{
			image = ItemSpriteSheet.THROWING_SPIKE;
		}

		@Override
		public ItemSprite.Glowing glowing() {
			return new ItemSprite.Glowing(0xFFFFFF, 0.1f);
		}

		@Override
		public Emitter emitter() {
			Emitter emitter = new Emitter();
			emitter.pos( 5, 5, 0, 0);
			emitter.fillTarget = false;
			emitter.pour(SparkParticle.FACTORY, 0.025f);
			return emitter;
		}
	}

	public static class LanceCooldown extends FlavourBuff {

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0.67f, 0.67f, 0);
		}

		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	}
}
