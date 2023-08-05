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

package com.shatteredpixel.shatteredpixeldungeon.items.sundry;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

import java.util.ArrayList;

public class Holywater extends Item {

	private static final int MAX_VOLUME	= 5;

	private static final String AC_WASH	= "WASH";
	private static final String AC_FILL	= "FILL";

	private static final float TIME_TO_FILL = 1f;

	private static final String TXT_STATUS	= "%d/%d";

	{
		image = ItemSpriteSheet.HOLYWATER;

		defaultAction = AC_WASH;

		unique = true;

	}

	private int volume = 2;

	private static final String VOLUME	= "volume";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( VOLUME, volume );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		volume	= bundle.getInt( VOLUME );
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (volume > 0) {
			actions.add( AC_WASH );
		}

		if (volume < 5) {
			actions.add( AC_FILL );
		}
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {
		super.execute( hero, action );

		//驱邪
		if (action.equals( AC_WASH )) {
			if (volume > 0) {
				volume -= 1;
				GameScene.selectItem(equipmentSelector);
				//照明
				Buff.affect( hero, Light.class ,5f);
				//清除负面buff
				Buff.detach( hero, Blindness.class );
				Buff.detach( hero, Burning.class );
				Buff.detach( hero, Charm.class );
				Buff.detach( hero, Cripple.class );
				Buff.detach( hero, Degrade.class );
				Buff.detach( hero, Vulnerable.class );
				Buff.detach( hero, Weakness.class );
				Buff.detach( hero, Ooze.class );
				//trimming off 0.01 drops helps with floating point errors
			} else {
				GLog.w( Messages.get(this, "runout") );
			}
		}

		//填充 先检测是否已知驱邪卷轴 Dungeon.hero.belongings.getItem(ScrollOfRemoveCurse.class) instanceof ScrollOfRemoveCurse &&
			if(action.equals( AC_FILL )){
				ScrollOfRemoveCurse scrollOfRemoveCurse = Dungeon.hero.belongings.getItem( ScrollOfRemoveCurse.class );
				if ( scrollOfRemoveCurse != null ) {

					Waterskin waterskin = hero.belongings.getItem(Waterskin.class);

					if( waterskin != null && volume >=5 ) {

						if (Catalog.isSeen(ScrollOfRemoveCurse.class) && scrollOfRemoveCurse.isKnown()) {

							ScrollOfRemoveCurse removeScroll = Dungeon.hero.belongings.getItem(ScrollOfRemoveCurse.class);

							removeScroll.detach(Dungeon.hero.belongings.backpack);
							volume += 1;
							volume -= 5;
							hero.spend(TIME_TO_FILL);
							GLog.w(Messages.get(this, "fill"));

						} else {
							GLog.w(Messages.get(this, "donotknown"));
						}
					} else {
						GLog.w(Messages.get(this, "dewdropnot_enough"));
					}
				} else {
					GLog.w(Messages.get(this, "cannotfill"));
				}
			}
	}

	protected static WndBag.ItemSelector equipmentSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return  Messages.get(BrokenSeal.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return (item instanceof EquipableItem || item instanceof Wand)
					&& ((!item.isIdentified() && !item.cursedKnown)
					|| item.cursed
					|| item instanceof Ring);
		}

		@Override
		public void onSelect(Item item) {

			new Flare( 6, 32 ).show( curUser.sprite, 2f );

			boolean procced = uncurse( curUser, item );

			if (curUser.buff(Degrade.class) != null) {
				Degrade.detach(curUser, Degrade.class);
				procced = true;
			}

			//戒指升级
			if ( item != null && item instanceof Ring){

				Holywater h = Dungeon.hero.belongings.getItem(Holywater.class);
				Ring r = (Ring)item;

				if ( r.HolywaterLevel >= 2 ){
					GLog.i(Messages.get(ArcaneResin.class, "level_too_high"));
					return;
				} else{
					int resinToUse = r.HolywaterLevel+1;



					if ( h.volume > resinToUse ){
						h.volume -= resinToUse ;
						r.upgrade( 1 );
						r.HolywaterLevel += 1;
						Item.updateQuickslot();

						GLog.w(Messages.get(this, "ring_upgrate"));

						curUser.sprite.operate(curUser.pos);
						Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
						curUser.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );

						curUser.spendAndNext(Actor.TICK);


					} else {
						GLog.i(Messages.get(ArcaneResin.class, "volume_notenough"));

					}

				}

			}


			if (procced) {
				GLog.p( Messages.get(this, "cleansed") );
			} else {
				GLog.i( Messages.get(this, "not_cleansed") );
			}
		}


	};


	public static boolean uncurse( Hero hero, Item... items ) {

		boolean procced = false;
		for (Item item : items) {
			if (item != null) {
				item.cursedKnown = true;
				if (item.cursed) {
					procced = true;
					item.cursed = false;
				}
			}
			if (item instanceof Weapon){
				Weapon w = (Weapon) item;
				if (w.hasCurseEnchant()){
					w.enchant(null);
					procced = true;
				}
			}
			if (item instanceof Armor){
				Armor a = (Armor) item;
				if (a.hasCurseGlyph()){
					a.inscribe(null);
					procced = true;
				}
			}
			if (item instanceof Wand){
				((Wand) item).updateLevel();
			}
		}

		if (procced && hero != null) {
			hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
			hero.updateHT( false ); //for ring of might
			updateQuickslot();
		}

		return procced;
	}

	@Override
	public String info() {
		String info = desc();

		if (volume == 0){
			info += "\n\n" + Messages.get(this, "desc_water");
		} else {
			info += "\n\n" + Messages.get(this, "desc_heal");
		}

		if (isFull()){
			info += "\n\n" + Messages.get(this, "desc_full");
		}

		return info;
	}

	public void empty() {
		volume = 0;
		updateQuickslot();
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	public boolean isFull() {
		return volume >= MAX_VOLUME;
	}


	public void fill() {
		volume = MAX_VOLUME;
		updateQuickslot();
	}

	@Override
	public String status() {
		return Messages.format( TXT_STATUS, volume, MAX_VOLUME );
	}

}
