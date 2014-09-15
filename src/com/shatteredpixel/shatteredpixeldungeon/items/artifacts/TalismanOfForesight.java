package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by debenhame on 08/09/2014.
 */
public class TalismanOfForesight extends Artifact {
    //TODO: shade sprite, polish.

    {
        name = "Talisman of Foresight";
        image = ItemSpriteSheet.ARTIFACT_TALISMAN;
        level = 0;
        levelCap = 10;
        charge = 0;
        exp = 0;
        partialCharge = 0;
        chargeCap = 100;
    }

    public static final String AC_SCRY = "SCRY";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && charge == 100)
            actions.add(AC_SCRY);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals(AC_SCRY)){
            hero.sprite.operate( hero.pos );
            hero.busy();
            Sample.INSTANCE.play( Assets.SND_BEACON );
            charge = 0;
            for (int i=0; i < Level.LENGTH; i++) {

                int terr = Dungeon.level.map[i];
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                    //Level.set( i, Terrain.discover( terr ) );
                    GameScene.updateMap( i );

                    if (Dungeon.visible[i]) {
                        GameScene.discoverTile( i, terr );
                    }
                }
            }

            Buff.affect(hero, Awareness.class, Awareness.DURATION);
            Dungeon.observe();

        }
    }

    @Override
    public String status() {
            return Utils.format("%d%%", charge);
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Foresight();
    }

    @Override
    public String desc() {
        //TODO: add description
        return "";
    }

    public class Foresight extends ArtifactBuff{
        private int warn = 0;

        @Override
        public boolean act() {
            spend( TICK );

            boolean smthFound = false;

            int distance = 3;

            int cx = target.pos % Level.WIDTH;
            int cy = target.pos / Level.WIDTH;
            int ax = cx - distance;
            if (ax < 0) {
                ax = 0;
            }
            int bx = cx + distance;
            if (bx >= Level.WIDTH) {
                bx = Level.WIDTH - 1;
            }
            int ay = cy - distance;
            if (ay < 0) {
                ay = 0;
            }
            int by = cy + distance;
            if (by >= Level.HEIGHT) {
                by = Level.HEIGHT - 1;
            }

            for (int y = ay; y <= by; y++) {
                for (int x = ax, p = ax + y * Level.WIDTH; x <= bx; x++, p++) {

                    if (Dungeon.visible[p] && Level.secret[p] && Dungeon.level.map[p] != Terrain.SECRET_DOOR)
                            smthFound = true;
                }
            }

            if (smthFound == true){
                if (warn == 0){
                    GLog.w("You feel uneasy.");
                    if (target instanceof Hero){
                        ((Hero)target).interrupt();
                    }
                }
                warn = 3;
            } else {
                if (warn > 0){
                    warn --;
                }
            }
            BuffIndicator.refreshHero();

            //fully charges in 2400 turns at lvl=0, scaling to 800 turns at lvl = 10.
            partialCharge += (1f/24) + (((float)level)/80);


            if (partialCharge > 1 && charge < 100){
                partialCharge--;
                charge++;
            } else if (charge >= 100)
                partialCharge = 0;

            return true;
        }

        public void charge(){
            charge = Math.min(charge+4, chargeCap);
            exp++;
            if (exp >= 5) {
                upgrade();
                GLog.p("Your Talisman grows stronger!");
                exp -= 5;
            }
        }

        @Override
        public String toString() {
            return "Foresight";
        }

        @Override
        public int icon() {
            if (warn == 0)
                return BuffIndicator.NONE;
            else
                return BuffIndicator.FORESIGHT;
        }
    }
}
