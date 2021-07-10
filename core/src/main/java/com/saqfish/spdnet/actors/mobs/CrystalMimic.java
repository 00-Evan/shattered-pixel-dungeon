/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.actors.mobs;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Actor;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.Corruption;
import com.saqfish.spdnet.actors.buffs.Haste;
import com.saqfish.spdnet.actors.buffs.Terror;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.effects.CellEmitter;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.items.Heap;
import com.saqfish.spdnet.items.Honeypot;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.artifacts.Artifact;
import com.saqfish.spdnet.items.rings.Ring;
import com.saqfish.spdnet.items.scrolls.ScrollOfTeleportation;
import com.saqfish.spdnet.items.wands.Wand;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.sprites.CharSprite;
import com.saqfish.spdnet.sprites.MimicSprite;
import com.saqfish.spdnet.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CrystalMimic extends Mimic {

	{
		spriteClass = MimicSprite.Crystal.class;

		FLEEING = new Fleeing();
	}

	@Override
	public String name() {
		if (alignment == Alignment.NEUTRAL){
			return Messages.get(Heap.class, "crystal_chest");
		} else {
			return super.name();
		}
	}

	@Override
	public String description() {
		if (alignment == Alignment.NEUTRAL){
			String desc = null;
			for (Item i : items){
				if (i instanceof Artifact){
					desc = Messages.get(Heap.class, "crystal_chest_desc", Messages.get(Heap.class, "artifact"));
					break;
				} else if (i instanceof Ring){
					desc = Messages.get(Heap.class, "crystal_chest_desc", Messages.get(Heap.class, "ring"));
					break;
				} else if (i instanceof Wand){
					desc = Messages.get(Heap.class, "crystal_chest_desc", Messages.get(Heap.class, "wand"));
					break;
				}
			}
			if (desc == null) {
				desc = Messages.get(Heap.class, "locked_chest_desc");
			}
			return desc + "\n\n" + Messages.get(this, "hidden_hint");
		} else {
			return super.description();
		}
	}

	//does not deal bonus damage, steals instead. See attackProc
	@Override
	public int damageRoll() {
		if (alignment == Alignment.NEUTRAL) {
			alignment = Alignment.ENEMY;
			int dmg = super.damageRoll();
			alignment = Alignment.NEUTRAL;
			return dmg;
		} else {
			return super.damageRoll();
		}
	}

	public void stopHiding(){
		state = FLEEING;
		//haste for 2 turns if attacking
		if (alignment == Alignment.NEUTRAL){
			Buff.affect(this, Haste.class, 2f);
		} else {
			Buff.affect(this, Haste.class, 1f);
		}
		if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
			enemy = Dungeon.hero;
			target = Dungeon.hero.pos;
			enemySeen = true;
			GLog.w(Messages.get(this, "reveal") );
			CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
			Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 1.25f);
		}
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (alignment == Alignment.NEUTRAL && enemy == Dungeon.hero){
			steal( Dungeon.hero );

		} else {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				if (Dungeon.level.passable[pos+i] && Actor.findChar(pos+i) == null){
					candidates.add(pos + i);
				}
			}

			if (!candidates.isEmpty()){
				ScrollOfTeleportation.appear(enemy, Random.element(candidates));
			}

			if (alignment == Alignment.ENEMY) state = FLEEING;
		}
		return super.attackProc(enemy, damage);
	}

	protected void steal( Hero hero ) {

		int tries = 10;
		Item item;
		do {
			item = hero.belongings.randomUnequipped();
		} while (tries-- > 0 && (item == null || item.unique || item.level() > 0));

		if (item != null && !item.unique && item.level() < 1 ) {

			GLog.w( Messages.get(this, "ate", item.name()) );
			if (!item.stackable) {
				Dungeon.quickslot.convertToPlaceholder(item);
			}
			item.updateQuickslot();

			if (item instanceof Honeypot){
				items.add(((Honeypot)item).shatter(this, this.pos));
				item.detach( hero.belongings.backpack );
			} else {
				items.add(item.detach( hero.belongings.backpack ));
				if ( item instanceof Honeypot.ShatteredPot)
					((Honeypot.ShatteredPot)item).pickupPot(this);
			}

		}
	}

	@Override
	protected void generatePrize() {
		//Crystal mimic already contains a prize item. Just guarantee it isn't cursed.
		for (Item i : items){
			i.cursed = false;
			i.cursedKnown = true;
		}
	}

	private class Fleeing extends Mob.Fleeing{
		@Override
		protected void nowhereToRun() {
			if (buff( Terror.class ) == null && buff( Corruption.class ) == null) {
				if (enemySeen) {
					sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Mob.class, "rage"));
					state = HUNTING;
				} else if (!Dungeon.level.heroFOV[pos] && Dungeon.level.distance(Dungeon.hero.pos, pos) >= 6) {
					GLog.n( Messages.get(CrystalMimic.class, "escaped"));
					if (Dungeon.level.heroFOV[pos]) CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
					destroy();
					sprite.killAndErase();
				} else {
					state = WANDERING;
				}
			} else {
				super.nowhereToRun();
			}
		}
	}

}
