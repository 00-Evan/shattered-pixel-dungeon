package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.*;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant.Seed;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

/**
 * Created by debenhame on 12/08/2014.
 */
public class Blandfruit extends Food {

    public String message = "You eat the Blandfruit, bleugh!";
    public String info = "So dry and insubstantial, perhaps cooking could improve it.";
    public Potion potionAttrib = null;

    {
        name = "Blandfruit";
        stackable = false;
        image = ItemSpriteSheet.BLANDFRUIT;
        energy = (Hunger.STARVING - Hunger.HUNGRY)/2;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (potionAttrib == null){

            detach( hero.belongings.backpack );

            ((Hunger)hero.buff( Hunger.class )).satisfy( energy );
            GLog.i(message);

            hero.sprite.operate( hero.pos );
            hero.busy();
            SpellSprite.show(hero, SpellSprite.FOOD);
            Sample.INSTANCE.play( Assets.SND_EAT );

            hero.spend( 1f );

            Statistics.foodEaten++;
            Badges.validateFoodEaten();

        } else if (action.equals( AC_EAT )){

            ((Hunger)hero.buff( Hunger.class )).satisfy( Hunger.HUNGRY );

            detach( hero.belongings.backpack );

            hero.spend( 1f );
            hero.busy();
            potionAttrib.apply(hero);

            Sample.INSTANCE.play( Assets.SND_EAT );

            hero.sprite.operate(hero.pos);

            switch (hero.heroClass) {
                case WARRIOR:
                    if (hero.HP < hero.HT) {
                        hero.HP = Math.min( hero.HP + 5, hero.HT );
                        hero.sprite.emitter().burst( Speck.factory(Speck.HEALING), 1 );
                    }
                    break;
                case MAGE:
                    hero.belongings.charge( false );
                    ScrollOfRecharging.charge(hero);
                    break;
                case ROGUE:
                case HUNTRESS:
                    break;
            }

        } else if (action.equals( AC_THROW )){

            //need to finish this
            GameScene.selectCell(thrower);
            //potionAttrib.splash(hero.pos);

        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public String info() {
        return info;
    }

    @Override
    public int price() {
        return 20 * quantity;
    }

    public Item cook(Seed seed){
        Class<? extends Item> plant = seed.alchemyClass;


        try {
            potionAttrib = (Potion)plant.newInstance();
        } catch (Exception e) {
            return null;
        }

        //implement pixmap
        potionAttrib.image = ItemSpriteSheet.BLANDFRUIT;

        //need to finish this
        if (potionAttrib instanceof PotionOfHealing){

            name = "Healthfruit";
            //message = "You eat the HealthFruit, your whole body tingles.";
            info = "The fruit has plumped up from its time soaking in the pot and has even absorbed the properties "+
                    "of the Sungrass seed it was cooked with. It looks delicious and hearty, ready to be eaten!";


        } else if (potionAttrib instanceof PotionOfStrength){

            name = "Powerfruit";
            //message = "";
            info = "The fruit has plumped up from its time soaking in the pot and has even absorbed the properties "+
                    "of the Rotberry seed it was cooked with. It looks delicious and powerful, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfParalyticGas){

            name = "Paralyzefruit";
            //message = "";
            info = "The fruit has plumped up from its time soaking in the pot and has even absorbed the properties "+
                    "of the Earthroot seed it was cooked with. It looks delicious and firm, but eating it is probably a bad idea.";

        } else if (potionAttrib instanceof PotionOfInvisibility){

            name = "Invisifruit";
            //message = "";
            info = "The fruit has plumped up from its time soaking in the pot and has even absorbed the properties "+
                    "of the Blindweed seed it was cooked with. It looks delicious and shiny, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfLiquidFlame){

            name = "Flamefruit";
            //message = "";
            info = "The fruit has plumped up from its time soaking in the pot and has even absorbed the properties "+
                    "of the Firebloom seed it was cooked with. It looks delicious and spicy, but eating it is probably a bad idea.";

        } else if (potionAttrib instanceof PotionOfFrost){

            name = "Frostfruit";
            //message = "";
            info = "The fruit has plumped up from its time soaking in the pot and has even absorbed the properties "+
                    "of the Icecap seed it was cooked with. It looks delicious and refreshing, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfMindVision){

            name = "Visionfruit";
            //message = "";
            info = "The fruit has plumped up from its time soaking in the pot and has even absorbed the properties "+
                    "of the Fadeleaf seed it was cooked with. It looks delicious and shadowy, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfToxicGas){

            name = "Toxicfruit";
            //message = "";
            info = "The fruit has plumped up from its time soaking in the pot and has even absorbed the properties "+
                    "of the Sorrowmoss seed it was cooked with. It looks delicious and crisp, but eating it is probably a bad idea.";

        } else {

        }

        return this;
    }

    private static final ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x2EE62E );

    @Override
    public ItemSprite.Glowing glowing() {
        return (potionAttrib != null) ? GREEN : null;
    }

}
