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
package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Stylus extends Item {
	
	private static final String TXT_SELECT_ARMOR	= "Select an armor to inscribe";
	private static final String TXT_INSCRIBED		= "you inscribed your %s with the stylus";
	
	private static final float TIME_TO_INSCRIBE = 2;
	
	private static final String AC_INSCRIBE = "INSCRIBE";
	
	{
		name = "arcane stylus";
		image = ItemSpriteSheet.STYLUS;
		
		stackable = true;

		bones = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_INSCRIBE );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action == AC_INSCRIBE) {

			curUser = hero;
			GameScene.selectItem( itemSelector, WndBag.Mode.ARMOR, TXT_SELECT_ARMOR );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private void inscribe( Armor armor ) {
		
		detach(curUser.belongings.backpack);

		GLog.w(TXT_INSCRIBED, armor.name());
		
		armor.inscribe();
		
		curUser.sprite.operate(curUser.pos);
		curUser.sprite.centerEmitter().start(PurpleParticle.BURST, 0.05f, 10);
		Enchanting.show(curUser, armor);
		Sample.INSTANCE.play(Assets.SND_BURNING);
		
		curUser.spend(TIME_TO_INSCRIBE);
		curUser.busy();
	}
	
	@Override
	public int price() {
		return 30 * quantity;
	}
	
	@Override
	public String info() {
		return
			"This arcane stylus is made of some dark, very hard stone. Using it you can inscribe " +
			"a magical glyph on your armor, but you have no power over choosing what glyph it will be, " +
			"the stylus will decide it for you.";
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				Stylus.this.inscribe( (Armor)item );
			}
		}
	};
}
