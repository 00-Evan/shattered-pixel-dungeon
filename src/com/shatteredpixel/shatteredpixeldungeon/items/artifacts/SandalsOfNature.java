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
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by debenhame on 08/09/2014.
 */
public class SandalsOfNature extends Artifact {
    //todo: test, add sprite switching, number tweaking.

    {
        name = "Sandals of Nature";
        image = ItemSpriteSheet.ARTIFACT_SANDALS;
        level = 0;
        levelCap = 3;
        charge = 0;
        //partialcharge, chargeCap and exp are unused
    }

    public static final String[] NAMES = {"Sandals of Nature", "Shoes of Nature",
                                        "Boots of Nature", "Leggings of Nature"};

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
                CellEmitter.bottom(hero.pos).start(EarthParticle.FACTORY, 0.05f, 8);
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


    private static final String SEEDS = "seeds";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(SEEDS, seeds.toArray(new String[seeds.size()]));
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(SEEDS))
            Collections.addAll(seeds , bundle.getStringArray(SEEDS));
    }

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
                    GLog.w("Your " + name + " have already gained nutrients from that seed recently.");
                } else {
                    seeds.add(item.name());
                    Hero hero = Dungeon.hero;
                    hero.sprite.operate( hero.pos );
                    hero.busy();
                    hero.spend( 2f );
                    if (seeds.size() >= 5+level){
                        seeds.clear();
                        upgrade();
                        if (level >= 1 && level <= 3) {
                            GLog.p("Your " + name + " surge in size, they are now " + NAMES[level] + "!");
                            name = NAMES[level];
                        }
                        if (level <= 0)
                            image = ItemSpriteSheet.ARTIFACT_SANDALS;
                        else if (level == 1)
                            image = ItemSpriteSheet.ARTIFACT_SHOES;
                        else if (level == 2)
                            image = ItemSpriteSheet.ARTIFACT_BOOTS;
                        else if (level >= 3)
                            image = ItemSpriteSheet.ARTIFACT_LEGGINGS;
                    } else {
                        GLog.i("Your " + name + " absorb the seed, they seem healthier.");
                    }
                }
            }
        }
    };

}
