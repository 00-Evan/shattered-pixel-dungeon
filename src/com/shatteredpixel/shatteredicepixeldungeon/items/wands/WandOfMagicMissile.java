/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.items.wands;

import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.Badges;
import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.ResultDescriptions;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredicepixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredicepixeldungeon.utils.Utils;
import com.shatteredpixel.shatteredicepixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfMagicMissile extends Wand {

	public static final String AC_DISENCHANT	= "DISENCHANT";
	
	private static final String TXT_SELECT_WAND	= "Select a wand to upgrade";
	
	private static final String TXT_DISENCHANTED = 
		"you disenchanted the Wand of Magic Missile and used its essence to upgrade your %s";
	
	private static final float TIME_TO_DISENCHANT	= 2f;
	
	private boolean disenchantEquipped;
	
	{
		name = "Wand of Magic Missile";
		image = ItemSpriteSheet.WAND_MAGIC_MISSILE;

        bones = false;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (level > 0) {
			actions.add( AC_DISENCHANT );
		}
		return actions;
	}
	
	@Override
	protected void onZap( int cell ) {
				
		Char ch = Actor.findChar( cell );
		if (ch != null) {	
			
			int level = level();
			
			ch.damage( Random.Int( 1, 6 + level * 2 ), this );
			
			ch.sprite.burst( 0xFF99CCFF, level / 2 + 2 );
			
			if (ch == curUser && !ch.isAlive()) {
				Dungeon.fail( Utils.format( ResultDescriptions.ITEM, name ) );
				GLog.n( "You killed yourself with your own Wand of Magic Missile..." );
			}
		}
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_DISENCHANT )) {
			
			if (hero.belongings.weapon == this) {
				disenchantEquipped = true;
				hero.belongings.weapon = null;
				updateQuickslot();
			} else {
				disenchantEquipped = false;
				detach( hero.belongings.backpack );
			}
			
			curUser = hero;
			GameScene.selectItem( itemSelector, WndBag.Mode.WAND, TXT_SELECT_WAND );
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
	
	@Override
	protected boolean isKnown() {
		return true;
	}
	
	@Override
	public void setKnown() {
	}
	
	protected int initialCharges() {
		return 3;
	}
	
	@Override
	public String desc() {
		return
			"This wand launches missiles of pure magical energy, dealing moderate damage to a target creature.";
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				
				Sample.INSTANCE.play( Assets.SND_EVOKE );
				ScrollOfUpgrade.upgrade( curUser );
				evoke( curUser );
				
				GLog.w( TXT_DISENCHANTED, item.name() );

                Dungeon.quickslot.clearItem(WandOfMagicMissile.this);
                WandOfMagicMissile.this.updateQuickslot();
				
				item.upgrade();
				curUser.spendAndNext( TIME_TO_DISENCHANT );
				
				Badges.validateItemLevelAquired( item );
				
			} else {
				if (disenchantEquipped) {
					curUser.belongings.weapon = WandOfMagicMissile.this;
					WandOfMagicMissile.this.updateQuickslot();
				} else {
					collect( curUser.belongings.backpack );
				}
			}
		}
	};
}
