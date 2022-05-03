/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RedDragon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfNukeCole;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BombGnollTricksterSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlameBoiSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkullShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

public class WndRedDragon extends Window {

    private static final int WIDTH		= 120;
    private static final int BTN_SIZE	= 24;
    private static final int BTN_GAP	= 5;
    private static final int GAP		= 2;

    public static boolean rewardObtained		= false;
    //SmuJB rewardObtained Intail == false

    RedDragon reddragon;

    public WndRedDragon( final RedDragon reddragon, final int type ) {

        super();

        this.reddragon = reddragon;

        IconTitle titlebar = new IconTitle();
        RenderedTextBlock message;
        switch (type){
            case 1:default:
                titlebar.icon( new BombGnollTricksterSprites() );
                titlebar.label( Messages.get(this, "gnollshied_title") );
                message = PixelScene.renderTextBlock( Messages.get(this, "gnollshied")+"\n\n"+Messages.get(this, "give_item"), 6 );
                break;
            case 2:
                titlebar.icon( new SkullShamanSprite() );
                titlebar.label( Messages.get(this, "skullgnoll_title") );
                message = PixelScene.renderTextBlock( Messages.get(this, "skullgnoll")+"\n\n"+Messages.get(this, "give_item"), 6 );
                break;
            case 3:
                titlebar.icon( new FlameBoiSprite());
                titlebar.label( Messages.get(this, "flamec02_title") );
                message = PixelScene.renderTextBlock( Messages.get(this, "flamec02")+"\n\n"+Messages.get(this, "give_item"), 6 );
                break;

        }

        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );

        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add( message );

        RewardButton btnRing = new RewardButton( RedDragon.Quest.weapon );
        btnRing.setRect( (WIDTH - BTN_GAP) / 2 - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE );
        add( btnRing );

        RewardButton btnFood = new RewardButton( RedDragon.Quest.armor );
        btnFood.setRect( btnRing.right() + BTN_GAP, btnRing.top(), BTN_SIZE, BTN_SIZE );

        add(btnFood);

        RewardButton btnArmor = new RewardButton( RedDragon.Quest.food );
        btnArmor.setRect( btnRing.left()-BTN_GAP + BTN_GAP, btnRing.bottom(), BTN_SIZE, BTN_SIZE );
        add(btnArmor);

        RewardButton btnScroll = new RewardButton( RedDragon.Quest.scrolls );
        btnScroll.setRect( btnArmor.right() + BTN_GAP, btnArmor.top(), BTN_SIZE, BTN_SIZE );
        add(btnScroll);

        resize(WIDTH, (int) btnArmor.bottom() + BTN_GAP);
    }

    private void selectReward( Item reward ) {

        hide();

        if (reward == null) return;

        if (reward instanceof Weapon && RedDragon.Quest.enchant != null){
            ((Weapon) reward).enchant(RedDragon.Quest.enchant);
        } else if (reward instanceof Armor && RedDragon.Quest.glyph != null){
            ((Armor) reward).inscribe(RedDragon.Quest.glyph);
        }

        reward.identify();
        if (reward.doPickUp( hero )) {
            GLog.i( Messages.get(hero, "you_now_have", reward.name()) );
            new ElixirOfNukeCole().quantity(5).identify().collect();
            rewardObtained = true;
        } else {
            Dungeon.level.drop( reward, reddragon.pos ).sprite.drop();
        }

        reddragon.yell( Messages.get(this, "farewell") );
        reddragon.die( null );

        RedDragon.Quest.complete();
    }

    private class RewardButton extends Component {

        protected NinePatch bg;
        protected ItemSlot slot;

        public RewardButton( Item item ){
            bg = Chrome.get( Chrome.Type.RED_BUTTON);
            add( bg );

            slot = new ItemSlot( item ){
                @Override
                protected void onPointerDown() {
                    bg.brightness( 1.2f );
                    Sample.INSTANCE.play( Assets.Sounds.CLICK );
                }
                @Override
                protected void onPointerUp() {
                    bg.resetColor();
                }
                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().addToFront(new RewardWindow(item));
                }
            };
            add(slot);
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size( width, height );

            slot.setRect( x + 2, y + 2, width - 4, height - 4 );
        }
    }
    public int pos = 0;
    private class RewardWindow extends WndInfoItem {

        public RewardWindow( Item item ) {
            super(item);

            RedButton btnConfirm = new RedButton(Messages.get(WndRedDragon.class, "confirm")){
                @Override
                protected void onClick() {
                    RewardWindow.this.hide();
                    if (rewardObtained == false) {
                        WndRedDragon.this.selectReward(item);
                    } else {
                        GLog.b(Messages.get(this, "why"));
                        Buff.affect(hero, Bleeding.class).set(8);
                        Buff.prolong(hero, Blindness.class, Degrade.DURATION);
                        Buff.prolong(hero, Cripple.class, Cripple.DURATION);
                        Music.INSTANCE.play(Assets.RUN, true);
                    }
                }

            };
            btnConfirm.setRect(0, height+2, width/2-1, 16);
            add(btnConfirm);

            RedButton btnCancel = new RedButton(Messages.get(WndSadGhost.class, "cancel")){
                @Override
                protected void onClick() {
                    RewardWindow.this.hide();
                }
            };
            btnCancel.setRect(btnConfirm.right()+2, height+2, btnConfirm.width(), 16);
            add(btnCancel);

            resize(width, (int)btnCancel.bottom());
        }
    }
}