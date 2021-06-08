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

package com.elementalpixel.elementalpixeldungeon.items.armor;


import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.actors.Actor;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.Blob;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.Fire;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.ToxicGas;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.BlobImmunity;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Invisibility;
import com.elementalpixel.elementalpixeldungeon.actors.mobs.Mob;
import com.elementalpixel.elementalpixeldungeon.effects.CellEmitter;
import com.elementalpixel.elementalpixeldungeon.effects.particles.FlameParticle;
import com.elementalpixel.elementalpixeldungeon.scenes.GameScene;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.HashMap;

public class AlchemistArmor extends ClassArmor {


    {
        image = ItemSpriteSheet.ARMOR_ROGUE;
    }


    private int targetPos;
    public int damage = 0;

    @Override
    public void doSpecial() {

        if (curUser.justMoved) { targetPos = curUser.pos; }

        charge -= 0;
        updateQuickslot();

        Buff.affect(curUser, BlobImmunity.class, BlobImmunity.DURATION);

        for( int i : PathFinder.NEIGHBOURS9) {
            if (!Dungeon.level.solid[curUser.pos + i]) {
                GameScene.add( Blob.seed( curUser.pos+i, 2, Fire.class ) );
                CellEmitter.get( curUser.pos+i ).burst( FlameParticle.FACTORY, 5 );
            }
        }

        GameScene.add( Blob.seed( curUser.pos, 300 + 20 * Dungeon.depth, ToxicGas.class ) );
        Sample.INSTANCE.play(Assets.Sounds.GAS);


        curUser.sprite.zap( curUser.pos );
        curUser.busy();

        curUser.spend( Actor.TICK );
        curUser.sprite.operate( curUser.pos );
        Invisibility.dispel();
        curUser.busy();
    }

}