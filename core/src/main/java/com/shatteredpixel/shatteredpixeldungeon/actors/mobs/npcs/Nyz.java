package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.BrokenBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.DeepBloodBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.IceCityBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.MagicGirlBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.NoKingMobBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.YellowSunBooks;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyzSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndNyzShop;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;

public class Nyz extends NPC {

    {

        spriteClass = NyzSprites.class;

        properties.add(Property.IMMOVABLE);
    }
    private boolean seenBefore = false;

    protected boolean act() {
        if (!seenBefore && Dungeon.level.heroFOV[pos]) {
            GLog.p(Messages.get(this, "greetings", Dungeon.hero.name()));
            //TODO 诡异奈亚子 早上好 中午好 晚上好
            Music.INSTANCE.play(Assets.NYZSHOP, true);
            seenBefore = true;
        } else if (seenBefore && !Dungeon.level.heroFOV[pos] && Dungeon.depth == 0) {
            Music.INSTANCE.play(Assets.TOWN, true);
            seenBefore = false;
        } else if (seenBefore && !Dungeon.level.heroFOV[pos] && Dungeon.depth == 12) {
            Music.INSTANCE.play(Assets.BGM_2,true);
            seenBefore = false;
        }
        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );

        shop6 = new YellowSunBooks();
        shop5 = new BrokenBooks();
        shop4 = new IceCityBooks();
        shop3 = new NoKingMobBooks();
        shop2 = new DeepBloodBooks();
        shop1 = new MagicGirlBooks();
        bomb1 = (Bomb) new Flashbang().quantity(1);
        bomb2 = (Bomb) new Noisemaker().quantity(1);
        bomb3 = (Bomb) new RegrowthBomb().quantity(1);
        bomb4 = (Bomb) new HolyBomb().quantity(1);
        bomb5 = (Bomb) new Firebomb().quantity(1);
        bomb6 = (Bomb) new FrostBomb().quantity(1);
        throwItem();
        return Nyz.super.act();
    }

    public static Books shop1;
    public static Books shop2;
    public static Books shop3;
    public static Books shop4;
    public static Books shop5;
    public static Books shop6;

    public static Bomb bomb1;
    public static Bomb bomb2;
    public static Bomb bomb3;
    public static Bomb bomb4;
    public static Bomb bomb5;
    public static Bomb bomb6;
    public int defenseSkill(Char enemy) {
        return 1000;
    }

    public void damage(int dmg, Object src) {
    }

    public void add(Buff buff) {
    }

    private void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                                   @Override
                                   public void call() {
                                       GameScene.show(new WndQuest(new Nyz(), text));
                                   }
                               }
        );
    }

    public boolean reset() {
        return true;
    }

    public boolean interact(Char c) {
        this.sprite.turnTo(this.pos, Dungeon.hero.pos);
        if (seenBefore && Dungeon.level.heroFOV[pos]) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndNyzShop(this));
                }
            });
        }
        return true;
    }
}
