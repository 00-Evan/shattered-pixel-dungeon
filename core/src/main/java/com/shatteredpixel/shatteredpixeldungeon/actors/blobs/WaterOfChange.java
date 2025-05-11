/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes.Landmark;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;


public class WaterOfChange extends WellWater {

    @Override
    protected boolean affectHero( Hero hero ) {

        if (!hero.isAlive()) return false;

        ArrayList<LinkedHashMap<Talent, Integer>> talents = Hero.Polished.getTalents();
        int tiersAvailable = Hero.Polished.tiersUnlocked();

        int tier_1 = Random.Int(tiersAvailable)+1;
        Talent replace_1 = Random.element(talents.get(tier_1-1).keySet());
        Talent newTalent_1 = Random.element(ScrollOfMetamorphosis.getOptions(tier_1, replace_1).keySet());

        ScrollOfMetamorphosis.replaceTalent(replace_1, newTalent_1);


        int tier_2;
        do {
            tier_2 = Random.Int(tiersAvailable)+1;
        } while(tier_2 == tier_1 && tiersAvailable > 1);

        Set<Talent> set = talents.get(tier_2-1).keySet();
        set.remove(replace_1);
        Talent replace_2 = Random.element(set);

        Talent newTalent_2 = Random.element(ScrollOfMetamorphosis.getOptions(tier_2, replace_2).keySet());

        ScrollOfMetamorphosis.replaceTalent(replace_2, newTalent_2);


        Sample.INSTANCE.play( Assets.Sounds.DRINK );
        CellEmitter.get( hero.pos ).start( Speck.factory( Speck.CHANGE ), 0.2f, 8 );

        Callback callback_2 = new Callback() {
            @Override
            public void call() {
                Transmuting.show(Dungeon.hero, replace_2, newTalent_2);
            }
        };
        Callback callback_1 = new Callback() {
            @Override
            public void call() {
                Transmuting animation = Transmuting.show(Dungeon.hero, replace_1, newTalent_1);
                if(animation != null) animation.callback=callback_2;
            }
        };
        Dungeon.hero.sprite.doAfterAnim(callback_1);

        Dungeon.hero.interrupt();
        GLog.p( Messages.get(this, "procced") );
        return true;
    }

    @Override
    protected Item affectItem(Item item, int pos ) {
        Item newItem = ScrollOfTransmutation.changeItem(item);

        if(newItem != null) {
            CellEmitter.get( pos ).start( Speck.factory( Speck.CHANGE ), 0.2f, 8 );
            Sample.INSTANCE.play( Assets.Sounds.DRINK );

            Transmuting.show(Dungeon.hero, item, newItem);
        }
        return newItem;
    }

    @Override
    public Landmark landmark() {
        return Landmark.WELL_OF_CHANGE;
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.start( Speck.factory( Speck.CHANGE ), 0.35f, 0 );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
