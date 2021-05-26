package com.elementalpixel.elementalpixeldungeon.items.fragments;

import com.elementalpixel.elementalpixeldungeon.Badges;
import com.elementalpixel.elementalpixeldungeon.Challenges;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.ShatteredPixelDungeon;
import com.elementalpixel.elementalpixeldungeon.Statistics;
import com.elementalpixel.elementalpixeldungeon.actors.Actor;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.scenes.AmuletScene;
import com.elementalpixel.elementalpixeldungeon.scenes.FireFragmentScene;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;

import java.io.IOException;

public class FireFragment extends Fragment {
    {
        image = ItemSpriteSheet.FIRE_FRAGMENT;
    }

    public FireFragment(int depth) {
        super();
    }

    @Override
    public boolean doPickUp( Hero hero ) {
        if (super.doPickUp( hero )) {

            if (!Statistics.amuletObtained) {
                Statistics.amuletObtained = true;
                hero.spend(-TIME_TO_PICK_UP);

                //add a delayed actor here so pickup behaviour can fully process.
                Actor.addDelayed(new Actor(){
                    @Override
                    protected boolean act() {
                        Actor.remove(this);
                        showFireFragmentScene( true );
                        return false;
                    }
                }, -5);
            }

            return true;
        } else {
            return false;
        }
    }

    private void showFireFragmentScene( boolean showText ) {
        try {
            Dungeon.saveAll();
            FireFragmentScene.noText = !showText;
            Game.switchScene( FireFragmentScene.class, new Game.SceneChangeCallback() {
                @Override
                public void beforeCreate() {

                }

                @Override
                public void afterCreate() {

                }
            });
        } catch (IOException e) {
            ShatteredPixelDungeon.reportException(e);
        }
    }
}