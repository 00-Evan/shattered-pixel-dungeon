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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class CursedRose extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_ROSE1; // Using dried rose sprite for now

		levelCap = 10;

		charge = 100;
		chargeCap = 100;

		defaultAction = AC_SWAP;

		cursed = true;
		cursedKnown = true;

		bones = false; // Unique starting artifact
	}

	public static final String AC_SWAP = "SWAP";
	public static final String AC_LINK = "LINK";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge >= 25 && !cursed) {
			actions.add(AC_SWAP);
		}
		if (isEquipped(hero) && charge >= 50) {
			actions.add(AC_LINK);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_SWAP)) {
			if (!isEquipped(hero)) {
				GLog.i(Messages.get(this, "need_to_equip"));
			} else if (charge < 25) {
				GLog.i(Messages.get(this, "low_charge"));
			} else {
				// TODO: Implement position swapping with twin
				GLog.i(Messages.get(this, "swap_placeholder"));
				charge -= 25;
				updateQuickslot();
			}
		} else if (action.equals(AC_LINK)) {
			if (!isEquipped(hero)) {
				GLog.i(Messages.get(this, "need_to_equip"));
			} else if (charge < 50) {
				GLog.i(Messages.get(this, "low_charge"));
			} else {
				// Link buff - share health/buffs with twin
				Buff.affect(hero, TwinLink.class, 10f);
				Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
				hero.sprite.emitter().start(Speck.factory(Speck.HEART), 0.3f, 5);
				charge -= 50;
				updateQuickslot();
			}
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new CursedRoseRecharge();
	}

	@Override
	public void charge(Hero target, float amount) {
		if (charge < chargeCap) {
			charge += Math.round(amount * (1f + level() * 0.1f));
			updateQuickslot();
			if (charge >= chargeCap) {
				charge = chargeCap;
				GLog.p(Messages.get(this, "full_charge"));
			}
		}
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if (isEquipped(Dungeon.hero)) {
			if (!cursed) {
				desc += "\n\n" + Messages.get(this, "desc_equipped");
			} else {
				desc += "\n\n" + Messages.get(this, "desc_cursed");
			}
		}

		return desc;
	}

	public class CursedRoseRecharge extends ArtifactBuff {
		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed) {
				charge += 1;
				if (charge == chargeCap) {
					GLog.p(Messages.get(CursedRose.class, "full_charge"));
				}
				updateQuickslot();
			}

			spend(TICK * 10);
			return true;
		}
	}

	// Twin Link buff - connects the two Revenants
	public static class TwinLink extends Buff {

		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.CORRUPT; // Placeholder icon
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0.5f, 0f, 1f); // Purple tint
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns());
		}
	}

	private static final String CURSED_KNOWN = "cursed_known";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CURSED_KNOWN, cursedKnown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		cursedKnown = bundle.getBoolean(CURSED_KNOWN);
	}

	@Override
	public Item upgrade() {
		if (level() < levelCap) {
			cursed = false;
		}
		return super.upgrade();
	}
}
