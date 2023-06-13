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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArrowMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class PocketedMobs extends TargetedSpell {
	
	{
		image = ItemSpriteSheet.POCKETED_MOBS;
		usesTargeting = true;
	}
	
	private Class<?extends Char> pocketedMob = null;

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		//prevents exploits
		if (pocketedMob != null){
			actions.remove(AC_DROP);
			actions.remove(AC_THROW);
		}
		return actions;
	}

	@Override
	protected void affectTarget(Ballistica bolt, Hero hero) {
		if (pocketedMob == null) {
			quantity++; //storing a mobs doesn't consume the spell
			final Char ch = Actor.findChar(bolt.collisionPos);

			if (ch != null && ch instanceof Mob
					&& !(Char.hasProp(ch, Char.Property.BOSS) || Char.hasProp(ch, Char.Property.MINIBOSS) || ch instanceof NPC)) {
				if (Random.Float() > (Math.round( ch.HP / ch.HT ))-0.1f){   // 체력이 낮을수록 잘 잡히나, 최소확률 10%를 보장
	//				if (ch.buff(MobsAlly.class) != null) quantity++;  // 다시 내껄 잡을때 수량 +1
					//그러나 사기칠 수 있기에 없앰
					pocketedMob = ch.getClass();
					Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
					ScrollOfRecharging.charge(hero);

					CellEmitter.get(ch.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
					ch.die(PocketedMobs.class);
					ch.sprite.killAndErase();
					Dungeon.level.mobs.remove(ch);
				}else {
					CellEmitter.get( bolt.collisionPos ).burst( Speck.factory( Speck.WOOL ), 10 );
					Sample.INSTANCE.play( Assets.Sounds.PUFF );
					GLog.w(Messages.get(this, "no_pocketed"));
				}
			}else {
				GLog.w(Messages.get(this, "no_mobs"));
			}
		} else {
			ArrayList<Integer> spawnPoints = new ArrayList<>();

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = bolt.collisionPos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
					spawnPoints.add(p);
				}
			}
			if (!spawnPoints.isEmpty()) {

				Mob mob = (Mob) Reflection.newInstance(pocketedMob);
				GameScene.add(mob);
				CellEmitter.get(bolt.collisionPos).burst(Speck.factory(Speck.WOOL), 10);
				Sample.INSTANCE.play(Assets.Sounds.PUFF);

				Buff.affect(mob, MobsAlly.class);
				mob.HP = mob.HT;
				pocketedMob = null;

				Char ch = Actor.findChar(bolt.collisionPos);
				if (ch == null) {ScrollOfTeleportation.appear(mob, bolt.collisionPos );}
				else {ScrollOfTeleportation.appear(mob, Random.element(spawnPoints) );}

			} else {
				GLog.w(Messages.get(SpiritHawk.class, "no_space"));
			}
		}
	}



	@Override
	public String desc() {
		String desc = super.desc();
		if (pocketedMob != null){
			desc += "\n\n" + Messages.get(this, "desc_mobs", Messages.get(pocketedMob, "name"));
		}
		return desc;
	}


	@Override
	public ItemSprite.Glowing glowing() {
		if (pocketedMob != null)  return new ItemSprite.Glowing(0xE3E3E3, 0.5f);
		return super.glowing();
	}

	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((40 + 50) / 4f));
	}
	
	private static final String POCKETED_MOBS = "pocketed_mobs";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (pocketedMob != null) bundle.put(POCKETED_MOBS, pocketedMob);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(POCKETED_MOBS)) pocketedMob = bundle.getClass(POCKETED_MOBS);
	}

	public static class MobsAlly extends AllyBuff {

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add(CharSprite.State.HEARTS);
			else    target.sprite.remove(CharSprite.State.HEARTS);
		}


	}
	
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfSirensSong.class, MetalShard.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = PocketedMobs.class;
			outQuantity = 6 ;
		}
		
	}
	
}
