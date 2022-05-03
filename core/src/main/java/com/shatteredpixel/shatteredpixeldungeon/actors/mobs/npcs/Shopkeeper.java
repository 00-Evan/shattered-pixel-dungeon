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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.ShopDiedBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class Shopkeeper extends NPC {

	{
		spriteClass = ShopkeeperSprite.class;
		properties.add(Property.BOSS);
		properties.add(Property.IMMOVABLE);
	}
	public static boolean seenBefore = false;

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
	}

	@Override
	protected boolean act() {
		if (!seenBefore && Dungeon.level.heroFOV[pos]) {
			yell(Messages.get(this, "greetings", Dungeon.hero.name()));
			seenBefore = true;
			//Buff.affect(this, ChampionEnemy.AntiMagic.class);
			//Buff.affect(this, ChampionEnemy.Halo.class);
		} else if(seenBefore && !Dungeon.level.heroFOV[pos]) {
			seenBefore = false;
			yell(Messages.get(this, "goodbye", Dungeon.hero.name()));
		}
		throwItem();

		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}

	@Override
	public void damage( int dmg, Object src ) {
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}

	/*
		Buff.prolong(Dungeon.hero, Blindness.class, Blindness.DURATION * 4f);
		GameScene.flash(0x80FFFFFF);
		Buff.affect(hero, Burning.class).reignite(hero, 15f);
		Dungeon.level.seal();
		Mob moa = new MoloHR();
		moa.pos = pos;
		GameScene.add(moa);
		yell(Messages.get(this, "arise"));
		new ShopGuardEye().spawnAround(pos);
		new ShopGuard().spawnAround(pos);
		Buff.affect(moa, ChampionEnemy.Growing.class);
		Buff.affect(moa, ChampionEnemy.Projecting.class);
		Buff.affect(moa, ChampionEnemy.AntiMagic.class);
		Buff.affect(moa, ChampionEnemy.Giant.class);
		Buff.affect(moa, ChampionEnemy.Blessed.class);
		Buff.affect(moa, ChampionEnemy.Halo.class);
		for (Mob mob : Dungeon.level.mobs) {
			switch (Random.Int(7)) {
				case 0:
				default:
					Buff.affect(mob, ChampionEnemy.Blazing.class);
					break;
				case 1:
					Buff.affect(mob, ChampionEnemy.Projecting.class);
					break;
				case 2:
					Buff.affect(mob, ChampionEnemy.AntiMagic.class);
					break;
				case 3:
					Buff.affect(mob, ChampionEnemy.Giant.class);
					break;
				case 4:
					Buff.affect(mob, ChampionEnemy.Blessed.class);
					break;
				case 5:
					Buff.affect(mob, ChampionEnemy.Growing.class);
					break;
				case 6:
					Buff.affect(mob, ChampionEnemy.Halo.class);
					break;
			}
		}
		yell(Messages.get(this, "dead"));*/

	public void flee() {
		destroy();
		CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
		Sample.INSTANCE.play(Assets.Sounds.ALERT);
		Music.INSTANCE.play(Assets.RUN, true);
		hero.sprite.burst(15597568, 9);
		sprite.killAndErase();
		new ShopDiedBook().quantity(1).identify().collect();
		CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
		GLog.negative(Messages.get(this, "guards"));
		GLog.n( Messages.get( ShopDiedBook.class, "chill") );
		Buff.affect(hero, ShopDiedBook.ShopSpawner.class);
	}

	public void destroy() {
		super.destroy();
		for (Heap heap: Dungeon.level.heaps.valueList()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
				heap.type = Heap.Type.HEAP;//Allow them to be picked up
			}
		}
	}

	@Override
	public boolean reset() {
		return true;
	}

	//shopkeepers are greedy!
	public static int sellPrice(Item item){
		return item.value() * 5 * (Dungeon.depth / 5 + 1);
	}
	
	public static WndBag sell() {
		return GameScene.selectItem( itemSelector, WndBag.Mode.FOR_SALE, Messages.get(Shopkeeper.class, "sell"));
	}

	public static boolean willBuyItem( Item item ){
		if (item.value() <= 0)                                               return false;
		if (item.unique && !item.stackable)                                 return false;
		if (item instanceof Armor && ((Armor) item).checkSeal() != null)    return false;
		if (item.isEquipped(hero) && item.cursed)                   return false;
		return true;
	}
	
	public static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public boolean interact(Char c) {
		if (c != hero) {
			return true;
		}
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				sell();
			}
		});
		return true;
	}
}
