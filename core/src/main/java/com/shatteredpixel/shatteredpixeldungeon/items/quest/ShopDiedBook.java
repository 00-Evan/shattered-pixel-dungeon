package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FlameC01;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShopGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShopDiedBook extends Item {

    {
        image = ItemSpriteSheet.DG20;

        cursed = true;
        cursedKnown = true;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        return new ArrayList<>(); //yup, no dropping this one
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
    protected void onDetach() {
        CorpseDust.DustGhostSpawner spawner = Dungeon.hero.buff(CorpseDust.DustGhostSpawner.class);
        if (spawner != null){
            spawner.dispel();
        }
    }

    public static class ShopSpawner extends Buff {

        int spawnPower = 0;

        @Override
        public boolean act() {
            spawnPower++;
            int wraiths = 1; //we include the wraith we're trying to spawn
            int shopguard = 1; //we include the wraith we're trying to spawn
            for (Mob mob : Dungeon.level.mobs){
                if (mob instanceof Wraith){
                    wraiths++;
                }
                if (mob instanceof ShopGuard){
                    shopguard++;
                }
            }

            int powerNeeded = Math.min(25, wraiths*wraiths);
            int powerNeeded2 = Math.min(25, shopguard*shopguard);

            if (powerNeeded <= spawnPower){
                spawnPower -= powerNeeded;
                int pos = 0;
                int tries = 20;
                do{
                    pos = Random.Int(Dungeon.level.length());
                    tries --;
                } while (tries > 0 && (!Dungeon.level.heroFOV[pos] || Dungeon.level.solid[pos] || Actor.findChar( pos ) != null));
                if (tries > 0) {
                    Wraith.spawnAt(pos);
                    Sample.INSTANCE.play(Assets.Sounds.CURSED);
                }
            }

            if (powerNeeded2 <= spawnPower){
                spawnPower -= powerNeeded2;
                int pos = 0;
                int tries = 20;
                do{
                    pos = Random.Int(Dungeon.level.length());
                    tries --;
                } while (tries > 0 && (!Dungeon.level.heroFOV[pos] || Dungeon.level.solid[pos] || Actor.findChar( pos ) != null));
                if (tries > 0 && tries < 5) {
                    FlameC01.spawnAt(pos);
                    Sample.INSTANCE.play(Assets.Sounds.CURSED);
                }
            }

            spend(TICK);
            return true;
        }

        public void dispel(){
            detach();
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof Wraith){


                    mob.die(null);
                }
            }
        }

        private static String SPAWNPOWER = "spawnpower";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( SPAWNPOWER, spawnPower );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            spawnPower = bundle.getInt( SPAWNPOWER );
        }
    }

}

