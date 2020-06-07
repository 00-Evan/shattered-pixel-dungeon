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

package net.casiello.pixeldungeonrescue.items.scrolls;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.actors.buffs.Degrade;
import net.casiello.pixeldungeonrescue.actors.buffs.Invisibility;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.effects.Flare;
import net.casiello.pixeldungeonrescue.effects.particles.ShadowParticle;
import net.casiello.pixeldungeonrescue.items.EquipableItem;
import net.casiello.pixeldungeonrescue.items.Item;
import net.casiello.pixeldungeonrescue.items.armor.Armor;
import net.casiello.pixeldungeonrescue.items.wands.Wand;
import net.casiello.pixeldungeonrescue.items.weapon.Weapon;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.utils.GLog;
import net.casiello.pixeldungeonrescue.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRemoveCurse extends InventoryScroll {

	{
		initials = 7;
		mode = WndBag.Mode.UNCURSABLE;
	}
	
	@Override
	public void empoweredRead() {
		for (Item item : curUser.belongings){
			if (item.cursed){
				item.cursedKnown = true;
			}
		}
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		doRead();
	}
	
	@Override
	protected void onItemSelected(Item item) {
		new Flare( 6, 32 ).show( curUser.sprite, 2f ) ;

		boolean procced = uncurse( curUser, item );

		Degrade.detach( curUser, Degrade.class );

		if (procced) {
			GLog.p( Messages.get(this, "cleansed") );
		} else {
			GLog.i( Messages.get(this, "not_cleansed") );
		}
	}

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
	
	public static boolean uncursable( Item item ){
		if ((item instanceof EquipableItem || item instanceof Wand) && (!item.isIdentified() || item.cursed)){
			return true;
		} else if (item instanceof Weapon){
			return ((Weapon)item).hasCurseEnchant();
		} else if (item instanceof Armor){
			return ((Armor)item).hasCurseGlyph();
		} else {
			return false;
		}
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
