package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;

public class BooksButton extends IconButton {

    public BooksButton() {
        super(Icons.EXIT.get());

        width = 20;
        height = 20;
    }

    @Override
    protected void onClick() {
        if (Game.scene() instanceof GameScene) {
            Game.instance.finish();
        } else {
            ShatteredPixelDungeon.switchNoFade( GameScene.class );
        }
    }
}

