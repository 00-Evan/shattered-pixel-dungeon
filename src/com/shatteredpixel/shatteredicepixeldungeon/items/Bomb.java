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
package com.shatteredpixel.shatteredicepixeldungeon.items;

import com.shatteredpixel.shatteredicepixeldungeon.ResultDescriptions;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredicepixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredicepixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Bomb extends Item {
	
	{
		name = "bomb";
		image = ItemSpriteSheet.BOMB;
		defaultAction = AC_LIGHTTHROW;
		stackable = true;
	}

	public Fuse fuse;

    //FIXME using a static variable for this is kinda gross, should be a better way
    private static boolean lightingFuse = false;

	private static final String AC_LIGHTTHROW = "Light & Throw";

    @Override
    public boolean isSimilar(Item item) {
        return item instanceof Bomb && this.fuse == ((Bomb) item).fuse;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        actions.add ( AC_LIGHTTHROW );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals( AC_LIGHTTHROW )){
            lightingFuse = true;
            action = AC_THROW;
        } else
            lightingFuse = false;

        super.execute(hero, action);
    }

    @Override
	protected void onThrow( int cell ) {
        if (!Level.pit[ cell ] && lightingFuse) {
            Actor.addDelayed(fuse = new Fuse().ignite(this), 2);
        }
        if (Actor.findChar( cell ) != null && !(Actor.findChar( cell ) instanceof Hero) ){
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int i : Level.NEIGHBOURS8)
                if (Level.passable[cell + i])
                    candidates.add(cell + i);
            int newCell = candidates.isEmpty() ? cell : Random.element(candidates);
            Dungeon.level.drop( this, newCell ).sprite.drop( cell );
        } else
            super.onThrow( cell );
	}

	@Override
	public boolean doPickUp(Hero hero) {
		if (fuse != null) {
			GLog.w("You quickly snuff the bomb's fuse.");
			fuse = null;
		}
		return super.doPickUp(hero);
	}

	public void explode(int cell){
		//We're blowing up, so no need for a fuse anymore.
		this.fuse = null;

		Sample.INSTANCE.play( Assets.SND_BLAST, 2 );

		if (Dungeon.visible[cell]) {
			CellEmitter.center( cell ).burst( BlastParticle.FACTORY, 30 );
		}

		boolean terrainAffected = false;
		for (int n : Level.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Level.LENGTH) {
				if (Dungeon.visible[c]) {
					CellEmitter.get( c ).burst( SmokeParticle.FACTORY, 4 );
				}

				if (Level.flamable[c]) {
					Level.set( c, Terrain.EMBERS );
					GameScene.updateMap( c );
					terrainAffected = true;
				}

				//destroys items / triggers bombs caught in the blast.
				Heap heap = Dungeon.level.heaps.get( c );
				if(heap != null)
					heap.explode();

				Char ch = Actor.findChar( c );
				if (ch != null) {
					//those not at the center of the blast take damage less consistently.
                    int minDamage = c == cell ? Dungeon.depth+5 : 1;
                    int maxDamage = 10 + Dungeon.depth * 2;

					int dmg = Random.NormalIntRange( minDamage, maxDamage ) - Random.Int( ch.dr() );
					if (dmg > 0) {
						ch.damage( dmg, this );
					}

					if (ch == Dungeon.hero && !ch.isAlive())
						//constant is used here in the rare instance a player is killed by a double bomb.
						Dungeon.fail(Utils.format(ResultDescriptions.ITEM, "bomb"));
				}
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
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
	public Item random() {
		switch(Random.Int( 2 )){
			case 0:
			default:
				return this;
			case 1:
				return new DoubleBomb();
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return fuse != null ? new ItemSprite.Glowing( 0xFF0000, 0.6f) : null;
	}

	@Override
	public int price() {
		return 20 * quantity;
	}
	
	@Override
	public String info() {
		return
			"A fairly hefty black powder bomb. An explosion from this would certainly do damage to anything nearby." +
				(fuse != null ? "\n\nThe bomb's fuse is burning away, keep your distance or put it out!" :
					"\n\nIt looks like the fuse will take a couple rounds to burn down once it is lit.");
	}

	private static final String FUSE = "fuse";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( FUSE, fuse );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains( FUSE ))
			Actor.add( fuse = ((Fuse)bundle.get(FUSE)).ignite(this) );
	}


	public static class Fuse extends Actor{

		private Bomb bomb;

		public Fuse ignite(Bomb bomb){
			this.bomb = bomb;
			return this;
		}

		@Override
		protected boolean act() {

			//something caused our bomb to explode early, or be defused. Do nothing.
			if (bomb.fuse != this){
				Actor.remove( this );
				return true;
			}

			//look for our bomb, remove it from its heap, and blow it up.
			for (Heap heap : Dungeon.level.heaps.values()) {
				if (heap.items.contains(bomb)) {
					heap.items.remove(bomb);

					bomb.explode(heap.pos);

					Actor.remove(this);
					return true;
				}
			}

			//can't find our bomb, this should never happen, throw an exception.
			throw new RuntimeException("Something caused a lit bomb to not be present in a heap on the level!");
		}
	}


	public static class DoubleBomb extends Bomb{

		{
			name = "two bombs";
			image = ItemSpriteSheet.DBL_BOMB;
			stackable = false;
		}

		@Override
		public String info() {
			return
				"A stack of two hefty black powder bombs, looks like you get one free!";
		}

		@Override
		public boolean doPickUp(Hero hero) {
			Bomb bomb = new Bomb();
			bomb.quantity(2);
			if (bomb.doPickUp(hero)) {
				//isaaaaac....
				hero.sprite.showStatus(CharSprite.NEUTRAL, "1+1 free!");
				return true;
			}
			return false;
		}
	}
}
