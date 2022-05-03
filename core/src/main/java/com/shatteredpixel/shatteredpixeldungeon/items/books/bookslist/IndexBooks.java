package com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GuideScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;


public class IndexBooks extends Books {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.MOBBOOKS;
        unique= true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public void execute(final Hero hero, String action) {

        super.execute(hero, action);
        if (action.equals( Read )) {
            ShatteredPixelDungeon.switchNoFade(GuideScene.class);
            Sample.INSTANCE.play( Assets.Sounds.READ );
        }
    }
}