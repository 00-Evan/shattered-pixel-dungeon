/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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

import java.util.ArrayList;

import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Honeypot extends Item {
	
	public static final String AC_SHATTER	= "SHATTER";
	
	{
		name = "honeypot";
		image = ItemSpriteSheet.HONEYPOT;
		defaultAction = AC_THROW;
		stackable = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_SHATTER );
		return actions;
	}
	
	@Override
	public void execute( final Hero hero, String action ) {
		if (action.equals( AC_SHATTER )) {
			
			hero.sprite.zap( hero.pos );
			shatter( hero.pos );
			
			detach( hero.belongings.backpack );
			hero.spendAndNext( TIME_TO_THROW );
			
		} else {
			super.execute( hero, action );
		}
	}
	
	@Override
	protected void onThrow( int cell ) {
		if (Level.pit[cell]) {
			super.onThrow( cell );
		} else {
			shatter( cell );
		}
	}
	
	private void shatter( int pos ) {
		Sample.INSTANCE.play( Assets.SND_SHATTER );
		
		if (Dungeon.visible[pos]) {
			Splash.at( pos, 0xffd500, 5 );
		}
		
		int newPos = pos;
		if (Actor.findChar( pos ) != null) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			boolean[] passable = Level.passable;
			
			for (int n : Level.NEIGHBOURS4) {
				int c = pos + n;
				if (passable[c] && Actor.findChar( c ) == null) {
					candidates.add( c );
				}
			}
	
			newPos = candidates.size() > 0 ? Random.element( candidates ) : -1;
		}
		
		if (newPos != -1) {
			Bee bee = new Bee();
			bee.spawn( Dungeon.depth );
			bee.HP = bee.HT;
			bee.pos = newPos;
			
			GameScene.add( bee );
			Actor.addDelayed( new Pushing( bee, pos, newPos ), -1 );
			
			bee.sprite.alpha( 0 );
			bee.sprite.parent.add( new AlphaTweener( bee.sprite, 1, 0.15f ) );
			
			Sample.INSTANCE.play( Assets.SND_BEE );
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
	public int price() {
		return 50 * quantity;
	}
	
	@Override
	public String info() {
		return
			"There is not much honey in this small honeypot, but there is a golden bee there and it doesn't want to leave it.";
	}
}
