/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPassage;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BeaconOfReturning extends Spell {
	
	{
		image = ItemSpriteSheet.RETURN_BEACON;

		talentChance = 1/(float)Recipe.OUT_QUANTITY;
	}

	//This class has a variety of code for compat with pre-v3.3.0 saves
	//Previously the destination was an item property, but in 3.3.0 this was changed to use a buff
	
	public int returnDepth	= -1;
	public int returnBranch	= 0;
	public int returnPos;
	
	@Override
	protected void onCast(final Hero hero) {

		BeaconTracker tracker = hero.buff(BeaconTracker.class);

		if (tracker == null && returnDepth == -1){
			setBeacon(hero);
		} else {
			GameScene.show(new WndOptions(new ItemSprite(this),
					Messages.titleCase(name()),
					Messages.get(BeaconOfReturning.class, "wnd_body"),
					Messages.get(BeaconOfReturning.class, "wnd_set"),
					Messages.get(BeaconOfReturning.class, "wnd_return")){
				@Override
				protected void onSelect(int index) {
					if (index == 0){
						setBeacon(hero);
					} else if (index == 1){
						returnBeacon(hero);
					}
				}
			});
			
		}
	}
	
	//we reset return depth when beacons are dropped to prevent
	//having two stacks of beacons with different return locations
	
	@Override
	protected void onThrow(int cell) {
		if (returnDepth != -1) {
			if (Dungeon.hero.belongings.getItem(getClass()) == null) {
				Notes.remove(Notes.Landmark.BEACON_LOCATION, returnDepth);
			}
			returnDepth = -1;
		}
		super.onThrow(cell);
	}
	
	@Override
	public void doDrop(Hero hero) {
		if (returnDepth != -1) {
			Notes.remove(Notes.Landmark.BEACON_LOCATION, returnDepth);
			returnDepth = -1;
		}
		super.doDrop(hero);
	}
	
	private void setBeacon(Hero hero ){
		if (returnDepth != -1){
			Notes.remove(Notes.Landmark.BEACON_LOCATION, returnDepth);
		}
		if (hero.buff(BeaconTracker.class) != null){
			Notes.remove(Notes.Landmark.BEACON_LOCATION,
					hero.buff(BeaconTracker.class).returnDepth);
		}

		BeaconTracker tracker = Buff.affect(hero, BeaconTracker.class);
		tracker.returnDepth = Dungeon.depth;
		tracker.returnBranch = Dungeon.branch;
		tracker.returnPos = hero.pos;

		Notes.add(Notes.Landmark.BEACON_LOCATION, tracker.returnDepth);
		
		hero.spend( 1f );
		hero.busy();
		
		GLog.i( Messages.get(this, "set") );
		
		hero.sprite.operate( hero.pos );
		Sample.INSTANCE.play( Assets.Sounds.BEACON );
		updateQuickslot();
	}
	
	private void returnBeacon( Hero hero ){

		BeaconTracker tracker = hero.buff(BeaconTracker.class);

		if (tracker == null){
			if (returnDepth == -1){
				return;
			}
			tracker = new BeaconTracker();
			tracker.returnDepth = returnDepth;
			tracker.returnBranch = returnBranch;
			tracker.returnPos = returnPos;
		}
		
		if (tracker.returnDepth == Dungeon.depth && tracker.returnBranch == Dungeon.branch) {

			Char existing = Actor.findChar(tracker.returnPos);
			if (existing != null && existing != hero){
				Char toPush = Char.hasProp(existing, Char.Property.IMMOVABLE) ? hero : existing;

				ArrayList<Integer> candidates = new ArrayList<>();
				for (int n : PathFinder.NEIGHBOURS8) {
					int cell = tracker.returnPos + n;
					if (!Dungeon.level.solid[cell] && Actor.findChar( cell ) == null
							&& (!Char.hasProp(toPush, Char.Property.LARGE) || Dungeon.level.openSpace[cell])) {
						candidates.add( cell );
					}
				}
				Random.shuffle(candidates);

				if (!candidates.isEmpty()){
					if (toPush == hero){
						tracker.returnPos = candidates.get(0);
					} else {
						Actor.add( new Pushing( toPush, toPush.pos, candidates.get(0) ) );
						toPush.pos = candidates.get(0);
						Dungeon.level.occupyCell(toPush);
					}
				} else {
					GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
					return;
				}
			}

			if (ScrollOfTeleportation.teleportToLocation(hero, tracker.returnPos)){
				hero.spendAndNext( 1f );
			} else {
				return;
			}

		} else {

			if (!Dungeon.interfloorTeleportAllowed()) {
				GLog.w( Messages.get(this, "preventing") );
				return;
			}

			//cannot return to mining level
			if (tracker.returnDepth >= 11 && tracker.returnDepth <= 14 && tracker.returnBranch == 1){
				GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
				return;
			}

			Level.beforeTransition();
			Invisibility.dispel();
			InterlevelScene.mode = InterlevelScene.Mode.RETURN;
			InterlevelScene.returnDepth = tracker.returnDepth;
			InterlevelScene.returnBranch = tracker.returnBranch;
			InterlevelScene.returnPos = tracker.returnPos;
			Game.switchScene( InterlevelScene.class );
		}
		if (quantity == 1){
			Notes.remove(Notes.Landmark.BEACON_LOCATION, tracker.returnDepth);
			if (tracker.target != null) tracker.detach();
		}
		detach(hero.belongings.backpack);
		Catalog.countUse(getClass());
		if (Random.Float() < talentChance){
			Talent.onScrollUsed(curUser, curUser.pos, talentFactor, getClass());
		}
	}
	
	@Override
	public String desc() {
		String desc = super.desc();
		if (Dungeon.hero != null) {
			BeaconTracker tracker = Dungeon.hero.buff(BeaconTracker.class);
			if (tracker != null){
				desc += "\n\n" + Messages.get(this, "desc_set", tracker.returnDepth);
			} else if (returnDepth != -1) {
				desc += "\n\n" + Messages.get(this, "desc_set", returnDepth);
			}
		}
		return desc;
	}
	
	private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );
	
	@Override
	public ItemSprite.Glowing glowing() {
		return returnDepth != -1 ? WHITE : null;
	}
	
	private static final String DEPTH	= "depth";
	private static final String BRANCH	= "branch";
	private static final String POS		= "pos";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DEPTH, returnDepth );
		bundle.put( BRANCH, returnBranch );
		if (returnDepth != -1) {
			bundle.put( POS, returnPos );
		}
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		returnDepth	= bundle.getInt( DEPTH );
		returnBranch = bundle.getInt( BRANCH );
		returnPos	= bundle.getInt( POS );
	}
	
	@Override
	public int value() {
		return (int)(60 * (quantity/(float)Recipe.OUT_QUANTITY));
	}

	@Override
	public int energyVal() {
		return (int)(12 * (quantity/(float)Recipe.OUT_QUANTITY));
	}
	
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		private static final int OUT_QUANTITY = 5;
		
		{
			inputs =  new Class[]{ScrollOfPassage.class};
			inQuantity = new int[]{1};
			
			cost = 12;
			
			output = BeaconOfReturning.class;
			outQuantity = OUT_QUANTITY;
		}
		
	}

	public static class BeaconTracker extends Buff {

		{
			revivePersists = true;
		}

		public int returnDepth	= -1;
		public int returnBranch	= 0;
		public int returnPos;

		private static final String DEPTH	= "depth";
		private static final String BRANCH	= "branch";
		private static final String POS		= "pos";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( DEPTH, returnDepth );
			bundle.put( BRANCH, returnBranch );
			if (returnDepth != -1) {
				bundle.put( POS, returnPos );
			}
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle(bundle);
			returnDepth	= bundle.getInt( DEPTH );
			returnBranch = bundle.getInt( BRANCH );
			returnPos	= bundle.getInt( POS );
		}
	}
}
