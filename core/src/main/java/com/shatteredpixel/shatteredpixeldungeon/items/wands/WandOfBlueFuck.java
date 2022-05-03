/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfFlameCursed;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class WandOfBlueFuck extends DamageWand {

    {
        image = ItemSpriteSheet.DG6;

        collisionProperties = Ballistica.MAGIC_BOLT | Ballistica.MAGIC_BOLT;
    }
    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{PotionOfLiquidFlameX.class, ScrollOfFlameCursed.class, Stylus.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 7+Dungeon.depth/2;

            output = WandOfBlueFuck.class;
            outQuantity = 1;
        }

    }


    //1x/2x/3x damage
    public int min(int lvl){
        return (1+lvl+Dungeon.depth/5) * chargesPerCast();
    }

    //1x/2x/3x damage
    public int max(int lvl){
        return (4*lvl+Dungeon.depth/5) * chargesPerCast();
    }

    ConeAOE cone;

    @Override
    protected void onZap( Ballistica bolt ) {

        ArrayList<Char> affectedChars = new ArrayList<>();
        ArrayList<Integer> adjacentCells = new ArrayList<>();
        for( int cell : cone.cells ){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            //knock doors open
            if (Dungeon.level.map[cell] == Terrain.DOOR){
                Level.set(cell, Terrain.OPEN_DOOR);
                GameScene.updateMap(cell);
            }

            //only ignite cells directly near caster if they are flammable
            if (Dungeon.level.adjacent(bolt.sourcePos, cell) && !Dungeon.level.flamable[cell]){
                adjacentCells.add(cell);
            } else {
                GameScene.add( Blob.seed( cell, 1+chargesPerCast(), HalomethaneFire.class ) );
            }

            Char ch = Actor.findChar( cell );
            if (ch != null) {
                affectedChars.add(ch);
            }
        }

        //ignite cells that share a side with an adjacent cell, are flammable, and are further from the source pos
        //This prevents short-range casts not igniting barricades or bookshelves
        for (int cell : adjacentCells){
            for (int i : PathFinder.CIRCLE6){
                if (Dungeon.level.trueDistance(cell+i, bolt.sourcePos) > Dungeon.level.trueDistance(cell, bolt.sourcePos)
                        && Dungeon.level.flamable[cell+i]
                        && HalomethaneFire.volumeAt(cell+i, HalomethaneFire.class) == 0){
                    GameScene.add( Blob.seed( cell+i, 6+chargesPerCast(), HalomethaneFire.class ) );
                }
            }
        }

        for ( Char ch : affectedChars ){
            processSoulMark(ch, chargesPerCast());
            ch.damage(damageRoll(), this);
            if (ch.isAlive()) {
                Buff.affect(ch, Burning.class).reignite(ch);
                switch (chargesPerCast()) {
                    case 1:
                        break; //no effects
                    case 2:
                        Buff.affect(ch, Blindness.class, 4f);
                        break;
                    case 3:
                        Buff.affect(ch, Haste.class, 4f);
                        break;
                }
            }
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //acts like blazing enchantment
        new Blazing().proc( staff, attacker, defender, damage);
    }

    @Override
    protected void fx( Ballistica bolt, Callback callback ) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.

        // 5/7/9 distance
        int maxDist = 3 + 2*chargesPerCast();
        int dist = Math.min(bolt.dist, maxDist);

        cone = new ConeAOE( bolt,
                maxDist,
                30 + 20*chargesPerCast(),
                collisionProperties | Ballistica.STOP_TARGET);

        //cast to cells at the tip, rather than all cells, better performance.
        for (Ballistica ray : cone.rays){
            ((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.EARTH,
                    curUser.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }

        //final zap at half distance, for timing of the actual wand effect
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.EARTH,
                curUser.sprite,
                bolt.path.get(dist/4),
                callback );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
        Sample.INSTANCE.play( Assets.Sounds.BURNING );
    }

    @Override
    protected int chargesPerCast() {
        //consumes 30% of current charges, rounded up, with
        // a minimum of one.
        return Math.max(1, (int)Math.ceil(curCharges*0.3f));
    }

    @Override
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc", chargesPerCast(), min(), max());
        else
            return Messages.get(this, "stats_desc", chargesPerCast(), min(0), max(0));
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( 0xEE7722 );
        particle.am = 0.5f;
        particle.setLifespan(0.6f);
        particle.acc.set(0, -20);
        particle.setSize( 0f, 1.5f);
        particle.shuffleXY( 0.4f );
    }

}
