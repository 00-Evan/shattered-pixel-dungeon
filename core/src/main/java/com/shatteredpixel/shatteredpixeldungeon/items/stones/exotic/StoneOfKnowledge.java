package com.shatteredpixel.shatteredpixeldungeon.items.stones.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.InventoryStone;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;

public class StoneOfKnowledge extends InventoryStone {

    {
        image = ItemSpriteSheet.STONE_KNOWLEDGE;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        if (item instanceof Ring || item instanceof Scroll || item instanceof Potion){
            return true;
        }
        return false;
    }

    @Override
    protected void onItemSelected(Item item) {
        curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );

        Sample.INSTANCE.play( Assets.Sounds.READ );

        HashSet<Class<? extends Potion>> potions = Potion.getUnknown();
        HashSet<Class<? extends Scroll>> scrolls = Scroll.getUnknown();
        HashSet<Class<? extends Ring>> rings = Ring.getUnknown();

        int total = potions.size() + scrolls.size() + rings.size();

        ArrayList<Item> IDed = new ArrayList<>();

        if (item instanceof Potion && !Potion.allKnown()) {
            Potion p = Reflection.newInstance(Random.element(potions));
            p.identify();
            IDed.add(p);
        } else if (item instanceof Scroll && !Scroll.allKnown()) {
            Scroll s = Reflection.newInstance(Random.element(scrolls));
            s.identify();
            IDed.add(s);
        } else if (item instanceof Ring && !Ring.allKnown()) {
            Ring r = Reflection.newInstance(Random.element(rings));
            r.identify();
            IDed.add(r);
        } else {
            new StoneOfKnowledge().collect();
        }

        GameScene.show(new WndDivination(IDed));
        identify();

    }

    private class WndDivination extends Window {

        private static final int WIDTH = 120;

        WndDivination(ArrayList<Item> IDed ){
            IconTitle cur = new IconTitle(new ItemSprite(StoneOfKnowledge.this),
            Messages.titleCase(Messages.get(StoneOfKnowledge.class, "name")));
            cur.setRect(0, 0, WIDTH, 0);
            add(cur);

            RenderedTextBlock msg = PixelScene.renderTextBlock(Messages.get(this, "desc"), 6);
            msg.maxWidth(120);
            msg.setPos(0, cur.bottom() + 2);
            add(msg);

            float pos = msg.bottom() + 10;

            for (Item i : IDed){

                cur = new IconTitle(i);
                cur.setRect(0, pos, WIDTH, 0);
                add(cur);
                pos = cur.bottom() + 2;

            }

            resize(WIDTH, (int)pos);
        }
    }
}
