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

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PotionOfDragonKingBreath extends ExoticPotion {

    {
        icon = ItemSpriteSheet.Icons.POTION_DRAGONKING;
    }

    @Override
    //need to override drink so that time isn't spent right away
    protected void drink(final Hero hero) {
        curUser = hero;
        curItem = this;

        GameScene.selectCell(targeter);
    }

    private CellSelector.Listener targeter = new CellSelector.Listener() {
        @Override
        public void onSelect(final Integer cell) {

            if (cell == null && !isKnown()){
                identify();
                detach(curUser.belongings.backpack);
            } else if (cell != null) {
                identify();
                Sample.INSTANCE.play( Assets.Sounds.DRINK );
                curUser.sprite.operate(curUser.pos, new Callback() {
                    @Override
                    public void call() {

                        curItem.detach(curUser.belongings.backpack);

                        curUser.spend(1f);
                        curUser.sprite.idle();
                        curUser.sprite.zap(cell);
                        Sample.INSTANCE.play( Assets.Sounds.BURNING );

                        final Ballistica bolt = new Ballistica(curUser.pos, cell, Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);

                        int maxDist = 6;
                        int dist = Math.min(bolt.dist, maxDist);

                        final ConeAOE cone = new ConeAOE(bolt, 6, 60, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET | Ballistica.IGNORE_SOFT_SOLID);

                        //cast to cells at the tip, rather than all cells, better performance.
                        for (Ballistica ray : cone.rays){
                            ((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
                                    MagicMissile.FIRE_CONE,
                                    curUser.sprite,
                                    ray.path.get(ray.dist),
                                    null
                            );
                        }

                        MagicMissile.boltFromChar(curUser.sprite.parent,
                                MagicMissile.FIRE_CONE,
                                curUser.sprite,
                                bolt.path.get(dist / 2),
                                new Callback() {
                                    @Override
                                    public void call() {
                                        ArrayList<Integer> adjacentCells = new ArrayList<>();
                                        for (int cell : cone.cells){
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
                                                GameScene.add( Blob.seed( cell, 5, HalomethaneFire.class ) );
                                            }

                                            Char ch = Actor.findChar( cell );
                                            if (ch != null) {

                                                Buff.affect( ch, HalomethaneBurning.class ).reignite( ch );
                                                Buff.affect(ch, Poison.class).set(10.0f);
                                            }
                                        }

                                        //ignite cells that share a side with an adjacent cell, are flammable, and are further from the source pos
                                        //This prevents short-range casts not igniting barricades or bookshelves
                                        for (int cell : adjacentCells){
                                            for (int i : PathFinder.NEIGHBOURS4){
                                                if (Dungeon.level.trueDistance(cell+i, bolt.sourcePos) > Dungeon.level.trueDistance(cell, bolt.sourcePos)
                                                        && Dungeon.level.flamable[cell+i]
                                                        && HalomethaneFire.volumeAt(cell+i, HalomethaneFire.class) == 0){
                                                    GameScene.add( Blob.seed( cell+i, 5, HalomethaneFire.class ) );
                                                }
                                            }
                                        }

                                        curUser.next();
                                    }
                                });

                    }
                });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(PotionOfDragonsBreath.class, "prompt");
        }
    };
}
