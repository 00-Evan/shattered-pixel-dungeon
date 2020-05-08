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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Honeypot extends Item {
	
	public static final String AC_SHATTER	= "SHATTER";
	
	{
		image = ItemSpriteSheet.HONEYPOT;

		defaultAction = AC_THROW;
		usesTargeting = true;

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

		super.execute( hero, action );

		if (action.equals( AC_SHATTER )) {
			
			hero.sprite.zap( hero.pos );
			
			detach( hero.belongings.backpack );

			Item item = shatter( hero, hero.pos );
			if (!item.collect()){
				Dungeon.level.drop(item, hero.pos);
				if (item instanceof ShatteredPot){
					((ShatteredPot) item).dropPot(hero, hero.pos);
				}
			}

			hero.next();

		}
	}
	
	@Override
	protected void onThrow( int cell ) {
		if (Dungeon.level.pit[cell]) {
			super.onThrow( cell );
		} else {
			Dungeon.level.drop(shatter( null, cell ), cell);
		}
	}
	
	public Item shatter( Char owner, int pos ) {
		
		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			Splash.at( pos, 0xffd500, 5 );
		}
		
		int newPos = pos;
		if (Actor.findChar( pos ) != null) {
			ArrayList<Integer> candidates = new ArrayList<>();
			boolean[] passable = Dungeon.level.passable;
			
			for (int n : PathFinder.NEIGHBOURS4) {
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
			bee.setPotInfo( pos, owner );
			bee.HP = bee.HT;
			bee.pos = newPos;
			
			GameScene.add( bee );
			Actor.addDelayed( new Pushing( bee, pos, newPos ), -1f );
			
			bee.sprite.alpha( 0 );
			bee.sprite.parent.add( new AlphaTweener( bee.sprite, 1, 0.15f ) );
			
			Sample.INSTANCE.play( Assets.Sounds.BEE );
			return new ShatteredPot();
		} else {
			return this;
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
		return 30 * quantity;
	}

	//The bee's broken 'home', all this item does is let its bee know where it is, and who owns it (if anyone).
	public static class ShatteredPot extends Item {

		{
			image = ItemSpriteSheet.SHATTPOT;
			stackable = true;
		}

		@Override
		public boolean doPickUp(Hero hero) {
			if ( super.doPickUp(hero) ){
				pickupPot( hero );
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void doDrop(Hero hero) {
			super.doDrop(hero);
			dropPot(hero, hero.pos);
		}

		@Override
		protected void onThrow(int cell) {
			super.onThrow(cell);
			dropPot(curUser, cell);
		}

		public void pickupPot(Char holder){
			for (Bee bee : findBees(holder.pos)){
				updateBee(bee, -1, holder);
			}
		}
		
		public void dropPot( Char holder, int dropPos ){
			for (Bee bee : findBees(holder)){
				updateBee(bee, dropPos, null);
			}
		}

		public void destroyPot( int potPos ){
			for (Bee bee : findBees(potPos)){
				updateBee(bee, -1, null);
			}
		}

		private void updateBee( Bee bee, int cell, Char holder ){
			if (bee != null && bee.alignment == Char.Alignment.ENEMY)
				bee.setPotInfo( cell, holder );
		}
		
		//returns up to quantity bees which match the current pot Pos
		private ArrayList<Bee> findBees( int potPos ){
			ArrayList<Bee> bees = new ArrayList<>();
			for (Char c : Actor.chars()){
				if (c instanceof Bee && ((Bee) c).potPos() == potPos){
					bees.add((Bee) c);
					if (bees.size() >= quantity) {
						break;
					}
				}
			}
			
			return bees;
		}
		
		//returns up to quantity bees which match the current pot holder
		private ArrayList<Bee> findBees( Char potHolder ){
			ArrayList<Bee> bees = new ArrayList<>();
			for (Char c : Actor.chars()){
				if (c instanceof Bee && ((Bee) c).potHolderID() == potHolder.id()){
					bees.add((Bee) c);
					if (bees.size() >= quantity) {
						break;
					}
				}
			}
			
			return bees;
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
			return 5 * quantity;
		}
	}
}
