package com.shatteredpixel.shatteredpixeldungeon.items.spells.debug;

        import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
        import com.shatteredpixel.shatteredpixeldungeon.items.spells.Spell;
        import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
        import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
        import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
        import com.watabou.noosa.Game;

public class Descend extends Spell {
    {
        image = ItemSpriteSheet.REWIND;
    }

    @Override
    protected void onCast(Hero hero) {
        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
        Game.switchScene( InterlevelScene.class );
    }
}
