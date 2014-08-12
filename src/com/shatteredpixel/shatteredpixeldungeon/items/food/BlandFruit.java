package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.*;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant.Seed;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by debenhame on 12/08/2014.
 */
public class BlandFruit extends Food {

    public String message = "You eat the BlandFruit, bleugh!";
    public String info = "So dry and insubstantial, perhaps cooking could improve it.";
    public Potion potionAttrib = null;

    {
        name = "BlandFruit";
        stackable = false;
        image = ItemSpriteSheet.STEAK;
        energy = (Hunger.STARVING - Hunger.HUNGRY)/2;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (potionAttrib == null){

            super.execute(hero, action);

        } else if (action.equals( AC_EAT )){

            ((Hunger)hero.buff( Hunger.class )).satisfy( Hunger.HUNGRY );

            detach( hero.belongings.backpack );

            hero.spend( 1f );
            hero.busy();
            potionAttrib.apply(hero);

            Sample.INSTANCE.play( Assets.SND_EAT );

            hero.sprite.operate(hero.pos);

        } else if (action.equals( AC_THROW )){

            //need to finish this
            GameScene.selectCell(thrower);
            potionAttrib.splash(hero.pos);

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
        potionAttrib.image = 0;

        //need to finish this
        if (potionAttrib instanceof PotionOfHealing){
            name = "HealFruit";
            message = "";
            info = "";
        } else if (potionAttrib instanceof PotionOfStrength){

        } else if (potionAttrib instanceof PotionOfParalyticGas){

        } else if (potionAttrib instanceof PotionOfInvisibility){

        } else if (potionAttrib instanceof PotionOfLiquidFlame){

        } else if (potionAttrib instanceof PotionOfFrost){

        } else if (potionAttrib instanceof PotionOfMindVision){

        } else if (potionAttrib instanceof PotionOfToxicGas){

        } else {

        }
        return this;
    }

}
