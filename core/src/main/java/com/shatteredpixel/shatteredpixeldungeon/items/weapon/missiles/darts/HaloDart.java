package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class HaloDart extends TippedDart {

    {
        image = ItemSpriteSheet.HALO_DART;
    }

    @Override
    protected void onThrow( int cell ) {
        Char enemy = Actor.findChar( cell );
        if ((enemy == null || enemy == curUser) && Dungeon.level.flamable[cell]) {
            GameScene.add( Blob.seed( cell, 7, HalomethaneFire.class ) );
            Dungeon.level.drop(new Dart(), cell).sprite.drop();
        } else{
            super.onThrow(cell);
        }
    }

    @Override
    public int proc( Char attacker, Char defender, int damage ) {
        Buff.affect( defender, HalomethaneBurning.class ).reignite( defender );
        return super.proc( attacker, defender, damage );
    }

}


