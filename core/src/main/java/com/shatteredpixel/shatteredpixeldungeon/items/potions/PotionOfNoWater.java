package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PotionOfNoWater extends Item {

    public static final float TIME_TO_LIGHT = 1;

    {
        image = ItemSpriteSheet.DG18;

        stackable = true;

        defaultAction = AC_THROW;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        return super.actions(hero);
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
    }

    @Override
    protected void onThrow(int cell) {
        if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {

            super.onThrow(cell);

        } else {

            Dungeon.level.pressCell(cell);
            shatter(cell);

        }
    }

    public void shatter(int cell) {

        if (Dungeon.level.heroFOV[cell]) {
            splash(cell);
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }

        for (int offset : PathFinder.NEIGHBOURS9) {
            if (Dungeon.level.water[cell + offset]) {
                Level.set(cell + offset, Terrain.EMPTY);
                DEM(cell + offset);
                //GameScene.add(Blob.seed(cell + offset, 1, Fire.class));
                GameScene.updateMap(cell + offset);
            }
        }
    }

    protected void splash(int cell) {

        Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
        if (fire != null)
            fire.clear(cell);

        DEM(cell);
    }

    protected void DEM(int cell) {
        final int color = splashColor();
        Char ch = Actor.findChar(cell);
        if (ch != null) {
            Buff.affect(ch, Burning.class).reignite(ch, 8f);
            Buff.affect(ch, Blindness.class, 8f);
            Splash.at(ch.sprite.center(), color, 5);
        } else {
            Splash.at(cell, color, 5);
        }
    }

    protected int splashColor() {
        return ItemSprite.pick(image, 5, 9);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{Firebloom.Seed.class, Blindweed.Seed.class};
            inQuantity = new int[]{1, 1};

            cost = 5;

            output = PotionOfNoWater.class;
            outQuantity = 6;
        }

    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public int price() {
        return 100 * quantity;
    }
}
