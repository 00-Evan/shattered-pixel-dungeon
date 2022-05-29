package com.shatteredpixel.shatteredpixeldungeon.items.stones.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class StoneOfAffection extends Runestone {

    {
        image = ItemSpriteSheet.STONE_AFFECTION;
    }

    @Override
    protected void activate(int cell) {

        for (int i : PathFinder.NEIGHBOURS9){

            CellEmitter.center(cell + i).start( Speck.factory( Speck.HEART ), 0.2f, 5 );


            Char ch = Actor.findChar( cell + i );

            if (ch != null && ch.alignment == Char.Alignment.ENEMY){
                Buff.prolong(ch, Charm.class, Charm.DURATION).object = curUser.id();
            }
        }

        Sample.INSTANCE.play( Assets.Sounds.CHARMS );

    }

}
