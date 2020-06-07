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

package net.casiello.pixeldungeonrescue.items.quest;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.actors.mobs.Bat;
import net.casiello.pixeldungeonrescue.effects.CellEmitter;
import net.casiello.pixeldungeonrescue.effects.Speck;
import net.casiello.pixeldungeonrescue.items.weapon.Weapon;
import net.casiello.pixeldungeonrescue.levels.Level;
import net.casiello.pixeldungeonrescue.levels.Terrain;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.scenes.GameScene;
import net.casiello.pixeldungeonrescue.sprites.ItemSprite.Glowing;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Pickaxe extends Weapon {
	
	public static final String AC_MINE	= "MINE";
	
	public static final float TIME_TO_MINE = 2;
	
	private static final Glowing BLOODY = new Glowing( 0x550000 );
	
	{
		image = ItemSpriteSheet.PICKAXE;
		
		unique = true;
		bones = false;
		
		defaultAction = AC_MINE;

	}
	
	public boolean bloodStained = false;

	@Override
	public int min(int lvl) {
		return 2;   //tier 2
	}

	@Override
	public int max(int lvl) {
		return 15;  //tier 2
	}

	@Override
	public int STRReq(int lvl) {
		return 14;  //tier 3
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_MINE );
		return actions;
	}
	
	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );
		
		if (action.equals(AC_MINE)) {
			
			if (Dungeon.depth < 11 || Dungeon.depth > 15) {
				GLog.w( Messages.get(this, "no_vein") );
				return;
			}
			
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				
				final int pos = hero.pos + PathFinder.NEIGHBOURS8[i];
				if (Dungeon.level.map[pos] == Terrain.WALL_DECO) {
				
					hero.spend( TIME_TO_MINE );
					hero.busy();
					
					hero.sprite.attack( pos, new Callback() {
						
						@Override
						public void call() {

							CellEmitter.center( pos ).burst( Speck.factory( Speck.STAR ), 7 );
							Sample.INSTANCE.play( Assets.SND_EVOKE );
							
							Level.set( pos, Terrain.WALL );
							GameScene.updateMap( pos );
							
							DarkGold gold = new DarkGold();
							if (gold.doPickUp( Dungeon.hero )) {
								GLog.i( Messages.get(Dungeon.hero, "you_now_have", gold.name()) );
							} else {
								Dungeon.level.drop( gold, hero.pos ).sprite.drop();
							}
							
							hero.onOperateComplete();
						}
					} );
					
					return;
				}
			}
			
			GLog.w( Messages.get(this, "no_vein") );
			
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
	
	@Override
	public int proc( Char attacker, Char defender, int damage ) {
		if (!bloodStained && defender instanceof Bat && (defender.HP <= damage)) {
			bloodStained = true;
			updateQuickslot();
		}
		return damage;
	}
	
	private static final String BLOODSTAINED = "bloodStained";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		
		bundle.put( BLOODSTAINED, bloodStained );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		bloodStained = bundle.getBoolean( BLOODSTAINED );
	}
	
	@Override
	public Glowing glowing() {
		return bloodStained ? BLOODY : null;
	}

}
