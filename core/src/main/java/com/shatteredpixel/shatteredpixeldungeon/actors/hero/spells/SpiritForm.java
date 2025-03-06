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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.Trinity;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SpiritForm extends ClericSpell {

	public static SpiritForm INSTANCE = new SpiritForm();

	@Override
	public int icon() {
		return HeroIcon.SPIRIT_FORM;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", ringLevel(), artifactLevel()) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	public float chargeUse(Hero hero) {
		return 4;
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero) && hero.hasTalent(Talent.SPIRIT_FORM);
	}

	@Override
	public void onCast(HolyTome tome, Hero hero) {

		GameScene.show(new Trinity.WndItemtypeSelect(tome, this));

	}

	public static int ringLevel(){
		return Dungeon.hero.pointsInTalent(Talent.SPIRIT_FORM);
	}

	public static int artifactLevel(){
		return 2 + 2*Dungeon.hero.pointsInTalent(Talent.SPIRIT_FORM);
	}

	public static class SpiritFormBuff extends FlavourBuff{

		{
			type = buffType.POSITIVE;
		}

		public static final float DURATION = 20f;

		private Bundlable effect;

		@Override
		public int icon() {
			return BuffIndicator.TRINITY_FORM;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0, 1, 0);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

		public void setEffect(Bundlable effect){
			this.effect = effect;
			if (effect instanceof RingOfMight){
				((Ring) effect).level(ringLevel());
				Dungeon.hero.updateHT( false );
			}
		}

		@Override
		public void detach() {
			super.detach();
			if (effect instanceof RingOfMight){
				Dungeon.hero.updateHT( false );
			}
		}

		public Ring ring(){
			if (effect instanceof Ring){
				((Ring) effect).level(ringLevel());
				return (Ring) effect;
			}
			return null;
		}

		public Artifact artifact(){
			if (effect instanceof Artifact){
				if (((Artifact) effect).visiblyUpgraded() < artifactLevel()){
					((Artifact) effect).transferUpgrade(artifactLevel() - ((Artifact) effect).visiblyUpgraded());
				}
				return (Artifact) effect;
			}
			return null;
		}

		@Override
		public String desc() {
			if (ring() != null){
				return Messages.get(this, "desc", Messages.titleCase(ring().name()), dispTurns());
			} else if (artifact() != null){
				return Messages.get(this, "desc", Messages.titleCase(artifact().name()), dispTurns());
			}
			return super.desc();
		}

		private static final String EFFECT = "effect";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(EFFECT, effect);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			effect = bundle.get(EFFECT);
		}

	}

	public static void applyActiveArtifactEffect(ClassArmor armor, Artifact effect){
		if (effect instanceof AlchemistsToolkit){
			Talent.onArtifactUsed(Dungeon.hero);
			AlchemyScene.assignToolkit((AlchemistsToolkit) effect);
			Game.switchScene(AlchemyScene.class);

		} else if (effect instanceof DriedRose){
			ArrayList<Integer> spawnPoints = new ArrayList<>();
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null && !Dungeon.level.solid[p]) {
					spawnPoints.add(p);
				}
			}
			if (!spawnPoints.isEmpty()) {
				Wraith w = Wraith.spawnAt(Random.element(spawnPoints), Wraith.class);

				w.HP = w.HT = 20 + 8*artifactLevel();
				Buff.affect(w, Corruption.class);
			}
			Talent.onArtifactUsed(Dungeon.hero);
			Dungeon.hero.spendAndNext(1f);

		} else if (effect instanceof EtherealChains){
			GameScene.selectCell(((EtherealChains) effect).caster);
			if (Dungeon.quickslot.contains(armor)) {
				QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(armor));
			}

		} else if (effect instanceof HornOfPlenty){
			((HornOfPlenty) effect).doEatEffect(Dungeon.hero, 1);

		} else if (effect instanceof MasterThievesArmband){
			GameScene.selectCell(((MasterThievesArmband) effect).targeter);
			if (Dungeon.quickslot.contains(armor)) {
				QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(armor));
			}

		} else if (effect instanceof SandalsOfNature){
			((SandalsOfNature) effect).curSeedEffect = Random.oneOf(
					Blindweed.Seed.class, Fadeleaf.Seed.class, Firebloom.Seed.class,
					Icecap.Seed.class, Sorrowmoss.Seed.class, Stormvine.Seed.class
			);

			GameScene.selectCell(((SandalsOfNature) effect).cellSelector);
			if (Dungeon.quickslot.contains(armor)) {
				QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(armor));
			}

		} else if (effect instanceof TalismanOfForesight){
			GameScene.selectCell(((TalismanOfForesight) effect).scry);

		} else if (effect instanceof TimekeepersHourglass){
			Buff.affect(Dungeon.hero, Swiftthistle.TimeBubble.class).reset(artifactLevel());
			Dungeon.hero.spendAndNext(1f);

		} else if (effect instanceof UnstableSpellbook){
			((UnstableSpellbook) effect).doReadEffect(Dungeon.hero);
		}
	}

}
