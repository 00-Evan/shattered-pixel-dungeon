package com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class YellowSunBooks extends Books {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.YELLOWBOOKS;
        unique= true;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals( Read )) {
            Sample.INSTANCE.play( Assets.Sounds.READ );
          switch (Random.Int(5)){
              case 0: case 1: case 2:
                  Buff.prolong(hero, Bless.class, Bless.DURATION*15f);
                  detach( hero.belongings.backpack );
                  GLog.b( Messages.get(this, "blees") );
                  break;
              case 3: case 4: case 5:
              Buff.affect(hero, ChampionHero.AntiMagic.class, ChampionHero.DURATION);
                  detach( hero.belongings.backpack );
                  GLog.b( Messages.get(this, "anmazing") );
                  break;
          }
        }
    }
}
