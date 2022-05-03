package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RandomBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyzSprites;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WndNyzShop extends Window {
    private static final int WIDTH		= 120;
    private static final int BTN_SIZE	= 16;
    private static final int BTN_GAP	= 3;
    private static final int GAP		= 3;
    public WndNyzShop(Callback callback) {
        IconTitle titlebar = new IconTitle();
        titlebar.setRect(0, 0, WIDTH, 0);
        titlebar.icon(new NyzSprites());
        titlebar.label(Messages.get(WndNyzShop.class,"nayazi"));
        add( titlebar );
        RenderedTextBlock message = PixelScene.renderTextBlock( (Messages.get(WndNyzShop.class,"nayaziwelcome")), 6 );
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add( message );

        WndNyzShop.RewardButton shop1 = new WndNyzShop.RewardButton( Nyz.shop1 );
        shop1.setRect( (WIDTH - BTN_GAP) / 6 - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE,
                BTN_SIZE );
        add( shop1 );

        WndNyzShop.RewardButton shop2 = new WndNyzShop.RewardButton( Nyz.shop2 );
        shop2.setRect( shop1.right() + BTN_GAP, shop1.top(), BTN_SIZE, BTN_SIZE );
        add(shop2);

        WndNyzShop.RewardButton shop3 = new WndNyzShop.RewardButton( Nyz.shop3 );
        shop3.setRect( shop2.right() + BTN_GAP, shop2.top(), BTN_SIZE, BTN_SIZE );
        add(shop3);

        WndNyzShop.RewardButton shop4 = new WndNyzShop.RewardButton( Nyz.shop4 );
        shop4.setRect( shop3.right() + BTN_GAP, shop3.top(), BTN_SIZE, BTN_SIZE );
        add(shop4);

        WndNyzShop.RewardButton shop5 = new WndNyzShop.RewardButton( Nyz.shop5 );
        shop5.setRect( shop4.right() + BTN_GAP, shop4.top(), BTN_SIZE, BTN_SIZE );
        add(shop5);

        WndNyzShop.RewardButton shop6 = new WndNyzShop.RewardButton( Nyz.shop6 );
        shop6.setRect( shop5.right() + BTN_GAP, shop5.top(), BTN_SIZE, BTN_SIZE );
        add(shop6);

        WndNyzShop.RewardButton2 bomb1 = new WndNyzShop.RewardButton2( Nyz.bomb1 );
        bomb1.setRect( shop1.left() , shop1.bottom(), BTN_SIZE, BTN_SIZE );
        add(bomb1);

        WndNyzShop.RewardButton2 bomb2 = new WndNyzShop.RewardButton2( Nyz.bomb2 );
        bomb2.setRect( bomb1.right()+ BTN_GAP , bomb1.top(), BTN_SIZE, BTN_SIZE );
        add(bomb2);

        WndNyzShop.RewardButton2 bomb3 = new WndNyzShop.RewardButton2( Nyz.bomb3 );
        bomb3.setRect( bomb2.right()+ BTN_GAP , bomb2.top(), BTN_SIZE, BTN_SIZE );
        add(bomb3);

        WndNyzShop.RewardButton2 bomb4 = new WndNyzShop.RewardButton2( Nyz.bomb4 );
        bomb4.setRect( bomb3.right()+ BTN_GAP , bomb3.top(), BTN_SIZE, BTN_SIZE );
        add(bomb4);

        WndNyzShop.RewardButton2 bomb5 = new WndNyzShop.RewardButton2( Nyz.bomb5 );
        bomb5.setRect( bomb4.right()+ BTN_GAP , bomb4.top(), BTN_SIZE, BTN_SIZE );
        add(bomb5);

        WndNyzShop.RewardButton2 bomb6 = new WndNyzShop.RewardButton2( Nyz.bomb6 );
        bomb6.setRect( bomb5.right() + BTN_GAP, bomb5.top(), BTN_SIZE, BTN_SIZE );
        add(bomb6);

        StyledButton btnSite = new StyledButton(Chrome.Type.WINDOW, "出售物品"){
            @Override
            protected void onClick() {
                super.onClick();
                GameScene.selectItem( Shopkeeper.itemSelector, WndBag.Mode.FOR_SALE, Messages.get(Shopkeeper.class, "sell"));
            }
        };
        btnSite.icon(Icons.get(Icons.GOLD));
        btnSite.textColor(Window.CYELLOW);
        btnSite.setRect(56,-2, 65, 20 );
        add(btnSite);

        resize(WIDTH, (int) bomb6.bottom());
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
    Nyz nyz;
    private void selectReward( Item reward ) {

        hide();

        reward.identify();
        if (reward.doPickUp( hero )) {
            GLog.i( Messages.get(hero, "you_now_have", reward.name()) );
        }

        //Ghost.Quest.complete();
    }

    private class RewardWindow extends WndInfoItem {

        public RewardWindow( Item item ) {
            super(item);

            RedButton btnConfirm = new RedButton(Messages.get(WndNyzShop.class, "buy")){
                @Override
                protected void onClick() {
                    if(Dungeon.hero.buff(RandomBuff.class) == null) {
                       tell(Messages.get(WndNyzShop.class,"maxbuy"));
                        for (Buff buff : hero.buffs()) {
                            if (buff instanceof RandomBuff) {
                                buff.detach();
                            }
                        }
                    } else if(Dungeon.gold > 300) {
                        Dungeon.gold-=300*Random.Int(2)+hero.lvl/5+10;
                        WndNyzShop.this.selectReward( item );
                        if (RandomBuff.level-- >= 0) {
                        }
                        Statistics.naiyaziCollected += 1;
                        WndNyzShop.RewardWindow.this.hide();
                        Badges.nyzvalidateGoldCollected();
                    } else {
                        tell(Messages.get(WndNyzShop.class,"nomoney"));
                        WndNyzShop.RewardWindow.this.hide();
                    }
                }
            };
            btnConfirm.setRect(0, height+2, width/2-1, 16);
            add(btnConfirm);

            RedButton btnCancel = new RedButton(Messages.get(WndNyzShop.class, "cancel")){
                @Override
                protected void onClick() {
                    hide();
                }
            };
            btnCancel.setRect(btnConfirm.right()+2, height+2, btnConfirm.width(), 16);
            add(btnCancel);

            resize(width, (int)btnCancel.bottom());
        }
    }

    private class RewardWindow2 extends WndInfoItem {

        public RewardWindow2( Item item ) {
            super(item);

            RedButton btnConfirm = new RedButton(Messages.get(WndNyzShop.class, "buy")){
                @Override
                protected void onClick() {
                    if(Dungeon.hero.buff(RandomBuff.class) == null){
                        tell(Messages.get(WndNyzShop.class,"maxbuy"));
                        for (Buff buff : hero.buffs()) {
                            if (buff instanceof RandomBuff) {
                                buff.detach();
                            }
                        }
                    } else if(Dungeon.gold > 270) {
                        Dungeon.gold-=270*Random.Int(3);
                        WndNyzShop.this.selectReward( item );
                        if (RandomBuff.level-- >= 0) {
                        }
                        Badges.nyzvalidateGoldCollected();
                        Statistics.naiyaziCollected += 1;
                        WndNyzShop.RewardWindow2.this.hide();
                    } else {
                        tell(Messages.get(WndNyzShop.class,"nomoney"));
                    }
                }
            };
            btnConfirm.setRect(0, height+2, width/2-1, 16);
            add(btnConfirm);

            RedButton btnCancel = new RedButton(Messages.get(WndNyzShop.class, "cancel")){
                @Override
                protected void onClick() {
                    hide();
                }
            };
            btnCancel.setRect(btnConfirm.right()+2, height+2, btnConfirm.width(), 16);
            add(btnCancel);

            resize(width, (int)btnCancel.bottom());
        }
    }

    public class RewardButton2 extends Component {

        protected NinePatch bg;
        protected ItemSlot slot;

        public RewardButton2(Item item) {
            bg = Chrome.get(Chrome.Type.RED_BUTTON);
            add(bg);

            slot = new ItemSlot(item) {
                @Override
                protected void onPointerDown() {
                    bg.brightness(1.2f);
                    Sample.INSTANCE.play(Assets.Sounds.CLICK);
                }

                @Override
                protected void onPointerUp() {
                    bg.resetColor();
                }

                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().addToFront(new WndNyzShop.RewardWindow2(item));
                }
            };
            add(slot);
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            slot.setRect(x + 2, y + 2, width - 4, height - 4);
        }
    }

    public class RewardButton extends Component {

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
                    ShatteredPixelDungeon.scene().addToFront(new WndNyzShop.RewardWindow(item));
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
}
