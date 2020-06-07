/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package net.casiello.pixeldungeonrescue.items;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.effects.Enchanting;
import net.casiello.pixeldungeonrescue.effects.particles.PurpleParticle;
import net.casiello.pixeldungeonrescue.items.armor.Armor;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.scenes.GameScene;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.utils.GLog;
import net.casiello.pixeldungeonrescue.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Stylus extends Item {
	
	private static final float TIME_TO_INSCRIBE = 2;
	
	private static final String AC_INSCRIBE = "INSCRIBE";
	
	{
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

		super.execute( hero, action );

		if (action.equals(AC_INSCRIBE)) {

			curUser = hero;
			GameScene.selectItem( itemSelector, WndBag.Mode.ARMOR, Messages.get(this, "prompt") );
			
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

		if (!armor.isIdentified() ){
			GLog.w( Messages.get(this, "identify"));
			return;
		} else if (armor.cursed || armor.hasCurseGlyph()){
			GLog.w( Messages.get(this, "cursed"));
			return;
		}
		
		detach(curUser.belongings.backpack);

		GLog.w( Messages.get(this, "inscribed"));

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

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				Stylus.this.inscribe( (Armor)item );
			}
		}
	};
}
