package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;

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

        defaultAction = AC_ROOT;
    }

    public static final String[] NAMES = {"Sandals of Nature", "Shoes of Nature",
                                        "Boots of Nature", "Greaves of Nature"};

    public static final String AC_FEED = "FEED";
    public static final String AC_ROOT = "ROOT";

    protected String inventoryTitle = "Select a seed";
    protected WndBag.Mode mode = WndBag.Mode.SEED;

    public ArrayList<String> seeds = new ArrayList<String>();

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && level < 3 && !cursed)
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
        } else if (action.equals(AC_ROOT) && level > 0){

            if (!isEquipped( hero )) GLog.i("You need to equip them to do that.");
            else if (charge == 0)    GLog.i("They have no energy right now.");
            else {
                Buff.prolong(hero, Roots.class, 5);
                Buff.affect(hero, Earthroot.Armor.class).level(charge);
                CellEmitter.bottom(hero.pos).start(EarthParticle.FACTORY, 0.05f, 8);
                Camera.main.shake(1, 0.4f);
                charge = 0;
            }
        }
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Naturalism();
    }

    @Override
    public String desc() {
        String desc = "";
        if (level == 0)
            desc += "What initially seem like sandals made of twine are actually two plants! The footwear moves ever " +
                  "so slightly when being held. They seem very weak and pale, perhaps they need to be given nutrients?";
        else if (level == 1)
            desc += "The footwear has grown and now more closely resemble two tailored shoes. They seem to match the " +
                "contours of your feet exactly. Some colour has returned to them, perhaps they can still grow further?";
        else if (level == 2)
            desc += "The plants have grown again and now resembles a pair of solid tall boots. They appear to be made" +
                    " of solid bark more than vine now, yet are still very flexible. The plants seem to have " +
                    "regained their strength, but perhaps they can still grow further";
        else
            desc += "Now almost tall enough to make full pants, the bark-mesh artifact seems to have reached its " +
                    "maximum size. Perhaps the two plants don't want to merge together? The greaves are a deep brown " +
                    "and resemble a very sturdy tree.";

        if ( isEquipped ( Dungeon.hero ) ){
            desc += "\n\n";
            if (level == 0) {
                if (!cursed)
                    desc += "The sandals wrap snugly around your feet, they seem happy to be worn.";
                else
                    desc += "The cursed sandals wrap tightly around your feet.";
            }
            else if (level == 1)
                desc += "The shoes fit on loosely but quickly tighten to make a perfect fit.";
            else if (level == 2)
                desc += "The boots fit snugly and add a nice heft to your step.";
            else
                desc += "The greaves are thick and weighty, but very easy to move in, as if they are moving with you.";

            if (!cursed)
                desc += " You feel more attuned with nature while wearing them.";
            else
                desc += " They are blocking any attunement with nature.";

            if (level > 0)
                desc += "\n\nThe footwear has gained the ability to form up into a sort of immobile natural armour, " +
                        "but will need to charge up for it.";
        }

        if (!seeds.isEmpty()){
            desc += "\n\nRecently Fed Seeds:";
            String[] seedsArray = seeds.toArray(new String[seeds.size()]);

            for (int i = 0; i < seedsArray.length-1; i++)
                desc += " " + seedsArray[i].substring(8) + ",";

            desc += " " + seedsArray[seedsArray.length-1].substring(8) + ".";
        }

        return desc;
    }

    @Override
    public Item upgrade() {
        if (level < 0)
            image = ItemSpriteSheet.ARTIFACT_SANDALS;
        else if (level == 0)
            image = ItemSpriteSheet.ARTIFACT_SHOES;
        else if (level == 1)
            image = ItemSpriteSheet.ARTIFACT_BOOTS;
        else if (level >= 2)
            image = ItemSpriteSheet.ARTIFACT_GREAVES;
        name = NAMES[level+1];
        return super.upgrade();
    }


    private static final String SEEDS = "seeds";
    private static final String NAME = "name";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(NAME, name);
        bundle.put(SEEDS, seeds.toArray(new String[seeds.size()]));
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        name = bundle.getString( NAME );
        if (bundle.contains(SEEDS))
            Collections.addAll(seeds , bundle.getStringArray(SEEDS));
    }

    public class Naturalism extends ArtifactBuff{
        public void charge() {
            if (charge < target.HT){
                //gain 1+(1*level)% of the difference between current charge and max HP.
                charge+= (Math.round( (target.HT-charge) * (.01+ level*0.01) ));
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
                    Sample.INSTANCE.play( Assets.SND_PLANT );
                    hero.busy();
                    hero.spend( 2f );
                    if (seeds.size() >= 5+(level*2)){
                        seeds.clear();
                        upgrade();
                        if (level >= 1 && level <= 3) {
                            GLog.p("Your " + NAMES[level-1] + " surge in size, they are now " + NAMES[level] + "!");
                        }

                    } else {
                        GLog.i("Your " + name + " absorb the seed, they seem healthier.");
                    }
                    item.detach(hero.belongings.backpack);
                }
            }
        }
    };

}
