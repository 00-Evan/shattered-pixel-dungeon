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
package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class TalismanOfForesight extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_TALISMAN;

		exp = 0;
		levelCap = 10;

		charge = 0;
		partialCharge = 0;
		chargeCap = 100;

		defaultAction = AC_SCRY;
	}

	public static final String AC_SCRY = "SCRY";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && charge == chargeCap && !cursed)
			actions.add(AC_SCRY);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);

		if (action.equals(AC_SCRY)){

			if (!isEquipped(hero))        GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (charge != chargeCap) GLog.i( Messages.get(this, "no_charge") );
			else {
				hero.sprite.operate(hero.pos);
				hero.busy();
				Sample.INSTANCE.play(Assets.SND_BEACON);
				charge = 0;
				for (int i = 0; i < Level.LENGTH; i++) {

					int terr = Dungeon.level.map[i];
					if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

						GameScene.updateMap(i);

						if (Dungeon.visible[i]) {
							GameScene.discoverTile(i, terr);
						}
					}
				}

				GLog.p( Messages.get(this, "scry") );

				updateQuickslot();

				Buff.affect(hero, Awareness.class, Awareness.DURATION);
				Dungeon.observe();
			}
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Foresight();
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if ( isEquipped( Dungeon.hero ) ){
			if (!cursed) {
				desc += "\n\n" + Messages.get(this, "desc_worn");

			} else {
				desc += "\n\n" + Messages.get(this, "desc_cursed");
			}
		}

		return desc;
	}

	public class Foresight extends ArtifactBuff{
		private int warn = 0;

		@Override
		public boolean act() {
			spend( TICK );

			boolean smthFound = false;

			int distance = 3;

			int cx = target.pos % Level.WIDTH;
			int cy = target.pos / Level.WIDTH;
			int ax = cx - distance;
			if (ax < 0) {
				ax = 0;
			}
			int bx = cx + distance;
			if (bx >= Level.WIDTH) {
				bx = Level.WIDTH - 1;
			}
			int ay = cy - distance;
			if (ay < 0) {
				ay = 0;
			}
			int by = cy + distance;
			if (by >= Level.HEIGHT) {
				by = Level.HEIGHT - 1;
			}

			for (int y = ay; y <= by; y++) {
				for (int x = ax, p = ax + y * Level.WIDTH; x <= bx; x++, p++) {

					if (Dungeon.visible[p] && Level.secret[p] && Dungeon.level.map[p] != Terrain.SECRET_DOOR)
							smthFound = true;
				}
			}

			if (smthFound && !cursed){
				if (warn == 0){
					GLog.w( Messages.get(this, "uneasy") );
					if (target instanceof Hero){
						((Hero)target).interrupt();
					}
				}
				warn = 3;
			} else {
				if (warn > 0){
					warn --;
				}
			}
			BuffIndicator.refreshHero();

			//fully charges in 2500 turns at lvl=0, scaling to 1000 turns at lvl = 10.
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
				partialCharge += 0.04+(level()*0.006);

				if (partialCharge > 1 && charge < chargeCap) {
					partialCharge--;
					charge++;
				} else if (charge >= chargeCap) {
					partialCharge = 0;
					GLog.p( Messages.get(this, "full_charge") );
				}
			}

			return true;
		}

		public void charge(){
			charge = Math.min(charge+(2+(level()/3)), chargeCap);
			exp++;
			if (exp >= 4 && level() < levelCap) {
				upgrade();
				GLog.p( Messages.get(this, "levelup") );
				exp -= 4;
			}
		}

		@Override
		public String toString() {
			return  Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc");
		}

		@Override
		public int icon() {
			if (warn == 0)
				return BuffIndicator.NONE;
			else
				return BuffIndicator.FORESIGHT;
		}
	}
}
