package com.shatteredpixel.shatteredpixeldungeon.items.books;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.MainBooks;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

public class Books extends MainBooks {

    {
        defaultAction = Read;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }


    public String title = Messages.get(this, "title");
    public String author = Messages.get(this, "author");
    public Integer age;
    public String desc = Messages.get(this, "desc");
    public Integer icon = null;
    private static final String Read	= "Read";
    public Emitter emitter() { return null; }

    public ItemSprite.Glowing glowing() {
        return null;
    }
    @Override
    public int value() {
        return quantity * 12;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.HP > 0) {
            actions.add(Read);
        }
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action ) {

        super.execute(hero, action);
        if (action.equals( Read )) {
        }
    }
}
