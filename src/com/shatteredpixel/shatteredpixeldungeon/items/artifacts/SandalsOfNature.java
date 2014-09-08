package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Camera;

import java.util.ArrayList;

/**
 * Created by debenhame on 08/09/2014.
 */
public class SandalsOfNature extends Artifact {

    {
        name = "Sandals of Nature";
        image = ItemSpriteSheet.ARTIFACT_SANDALS;
        level = 0;
        levelCap = 3;
        charge = 0;
        //partialcharge, chargeCap and exp are unused
    }

    public static final String AC_FEED = "FEED";
    public static final String AC_ROOT = "ROOT";

    protected String inventoryTitle = "Select a seed";
    protected WndBag.Mode mode = WndBag.Mode.SEED;

    public ArrayList<String> seeds = new ArrayList<String>();

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && level < 3)
            actions.add(AC_FEED);
        if (isEquipped( hero ) && charge > 0)
            actions.add(AC_ROOT);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals(AC_FEED)){
            GameScene.selectItem(itemSelector, mode, inventoryTitle);
        } else if (action.equals(AC_ROOT)){
            if (charge > 0){
                Buff.prolong( hero, Roots.class, 5);
                Buff.affect( hero, Earthroot.Armor.class ).level( charge );
                CellEmitter.bottom(hero.pos).start( EarthParticle.FACTORY, 0.05f, 8 );
                Camera.main.shake( 1, 0.4f );
                charge = 0;
            }
        }
    }

    @Override
    public String status() {
        if (charge > 0)
            return Utils.format("%d", charge);
        else
            return null;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Naturalism();
    }

    @Override
    public String desc() {
        //TODO: add description
        return "";
    }

    //TODO: add bundle logic

    public class Naturalism extends ArtifactBuff{
        public int level() { return level; }
        public void charge() {
            if (charge < 25*level){
                charge++;

            }
        }
    }

    protected WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect( Item item ) {
            if (item != null && item instanceof Plant.Seed) {
                if (seeds.contains(item.name())){
                    GLog.w("The plant has already gained nutrients from that seed.");
                } else {
                    seeds.add(item.name());
                    if (seeds.size() >= 5+level){
                        seeds.clear();
                        //upgrade logic
                    }
                }
            }
        }
    };

}
