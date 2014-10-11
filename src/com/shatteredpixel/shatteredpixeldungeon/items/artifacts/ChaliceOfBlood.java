package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ResultDescriptions;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by debenhame on 27/08/2014.
 */
public class ChaliceOfBlood extends Artifact {

    private static final String TXT_CHALICE	= "Chalice of Blood";
    private static final String TXT_YES		= "Yes, I know what I'm doing";
    private static final String TXT_NO		= "No, I changed my mind";
    private static final String TXT_PRICK 	=
            "Each time you use the chalice it will drain more life energy, "+
            "if you are not careful this draining effect can easily kill you.\n\n"+
            "Are you sure you want to offer it more life energy?";


    {
        name = "Chalice of Blood";
        image = ItemSpriteSheet.ARTIFACT_CHALICE1;
        level = 0;
        levelCap = 10;
        //charge & chargecap are unused
    }

    public static final String AC_PRICK = "PRICK";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && level < levelCap)
            actions.add(AC_PRICK);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals(AC_PRICK)){

            int damage = 3*(level*level);

            if (damage > hero.HP*0.75) {

                GameScene.show(
                    new WndOptions(TXT_CHALICE, TXT_PRICK, TXT_YES, TXT_NO) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0)
                                prick(Dungeon.hero);
                        };
                    }
                );

            } else {
                prick(hero);
            }
        }
    }

    private void prick(Hero hero){
        int damage = 3*(level*level);

        Earthroot.Armor armor = hero.buff(Earthroot.Armor.class);
        if (armor != null) {
            damage = armor.absorb(damage);
        }

        damage -= Random.IntRange(0, hero.dr());

        hero.sprite.operate( hero.pos );
        hero.busy();
        hero.spend(3f);
        if (damage <= 0){
            GLog.i("You prick yourself, and your blood drips into the chalice.");
        } else if (damage < 25){
            GLog.w("You prick yourself and the chalice feeds on you.");
            Sample.INSTANCE.play(Assets.SND_CURSED);
            hero.sprite.emitter().burst( ShadowParticle.CURSE, 6 );
        } else if (damage < 100){
            GLog.w("Your life essence drains into the chalice.");
            Sample.INSTANCE.play(Assets.SND_CURSED);
            hero.sprite.emitter().burst( ShadowParticle.CURSE, 12 );
        } else {
            GLog.w("The chalice devours your life energy.");
            Sample.INSTANCE.play(Assets.SND_CURSED);
            hero.sprite.emitter().burst( ShadowParticle.CURSE, 18 );
        }

        if (damage > 0)
            hero.damage(damage, this);

        if (!hero.isAlive()) {
            Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name, Dungeon.depth));
            GLog.n("The Chalice sucks your life essence dry...");
        } else {
            upgrade();
            if (level >= 7)
                image = ItemSpriteSheet.ARTIFACT_CHALICE3;
            else if (level >= 3)
                image = ItemSpriteSheet.ARTIFACT_CHALICE2;
        }
    }


    @Override
    protected ArtifactBuff passiveBuff() {
        return new chaliceRegen();
    }

    @Override
    public String desc() {
        String desc = "This shining silver chalice is oddly adorned with sharp gems at the rim. ";
        if (level < levelCap)
            desc += "The chalice is pulling your attention strangely, you feel like it wants something from you.";
        else
            desc += "The chalice is full and radiating energy.";

        if (isEquipped (Dungeon.hero)){
            desc += "\n\n";
            if (level == 0)
                desc += "As you hold the chalice, you feel oddly compelled to prick yourself on the sharp gems.";
            else if (level < 3)
                desc += "Some of your blood is pooled into the chalice, you can subtly feel the chalice feeding life " +
                   "energy into you. You still want to cut yourself on the chalice, even though you know it will hurt.";
            else if (level < 7)
                desc += "The chalice is about half full of your blood and you can feel it feeding life energy " +
                   "into you. you still want to hurt yourself, the chalice needs your energy, it's your friend.";
            else if (level < levelCap)
                desc += "The chalice is getting pretty full, and the life force it's feeding you is stronger than " +
                   "ever. You should give it more energy, you need too, your friend needs your energy, it needs " +
                   "your help. Your friend knows you have limits though, it doesn't want you to die, just bleed.";
            else
                desc += "The chalice is filled to the brim with your life essence. You can feel the chalice pouring " +
                   "life energy back into you. It's your best friend. It's happy with you. So happy. " +
                   "You've done well. So well. You're being rewarded. You don't need to touch the sharp gems anymore.";
        }

        return desc;
    }

    public class chaliceRegen extends ArtifactBuff {
        public int level() {
            return level;
        }
    }

}
