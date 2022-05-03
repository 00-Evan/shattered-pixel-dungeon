package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton3;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

public class SPSLScene extends PixelScene {
    private static final int SLOT_WIDTH = 120;
    private static final int SLOT_HEIGHT = 30;
    private static String TTL_MLPD() { return "";}

    @Override
    public void create() {
        super.create();
        Music.INSTANCE.play( Assets.Music.SHOP, true );
        final float colWidth = Camera.main.width / (landscape() ? 2 : 1);
        final float colTop = (Camera.main.height / 2) - (landscape() ? 30 : 90);
        final float colOffset = landscape() ? colWidth : 0;
        int w = Camera.main.width;
        int h = Camera.main.height;
        Image ling = (new SlimeKingSprite());
        ling.x = (colWidth - ling.width()) / 2;
        ling.y = colTop;
        align( ling );
        add( ling );

        Flare flare = new Flare( 7, 125 ) {
            private float time = 0;
            @Override
            public void update() {
                super.update();
                am = Math.max(0f, (float)Math.sin(time += Game.elapsed));
                if (time >= 1.5f * Math.PI) time = 0;
            }
        };
        flare.color( Window.MLPD_COLOR, true ).show( ling, 0 ).angularSpeed = +35;

        RenderedTextBlock mlpdtitle = renderTextBlock( TTL_MLPD(), 8 );
        mlpdtitle.hardlight( Window.MLPD_COLOR );
        add( mlpdtitle );

        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (w - title.width()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        ExitButton3 btnExit = new ExitButton3();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        int slotGap = landscape() ? 5 : 10;
        int slotCount = 2;
        int slotsHeight = slotCount*SLOT_HEIGHT + (slotCount - 1) * slotGap;

        float yPos = (h - slotsHeight)/2f;
        if (landscape()) yPos += 8;

        SPSLScene.ChangesButton MLPDChanges = new SPSLScene.ChangesButton() {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.switchNoFade( TSSupporterScene.class );
            }
        };
        MLPDChanges.text(Messages.get(this, "ts"));
        MLPDChanges.setRect((w - SLOT_WIDTH) / 2f, yPos, SLOT_WIDTH, SLOT_HEIGHT);
        yPos += SLOT_HEIGHT + slotGap;
        align(MLPDChanges);
        add(MLPDChanges);

        SPSLScene.ChangesButton SPDChanges = new SPSLScene.ChangesButton() {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.switchNoFade( LingSupporterScene.class );
            }
        };
        SPDChanges.text(Messages.get(this, "ling"));
        SPDChanges.setRect((w - SLOT_WIDTH) / 2f, yPos, SLOT_WIDTH, SLOT_HEIGHT);
        yPos += SLOT_HEIGHT + slotGap;
        align(SPDChanges);
        add(SPDChanges);

        SPSLScene.ChangesButton THANKSChanges = new SPSLScene.ChangesButton() {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.switchNoFade( ThanksScene.class );
            }
        };
        THANKSChanges.text(Messages.get(this, "zxz"));
        THANKSChanges.setRect((w - SLOT_WIDTH) / 2f, yPos, SLOT_WIDTH, SLOT_HEIGHT);
        yPos += SLOT_HEIGHT + slotGap;
        align(THANKSChanges);
        add(THANKSChanges);

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade( TitleScene.class );
    }


    private static class LinkBlock extends Component {

        RenderedTextBlock link;
        ColorBlock linkUnderline;
        PointerArea linkButton;

        private LinkBlock(int highlight, String linkText, String linkUrl, int maxWidth, int textSize) {
            super();

            if (linkText != null && linkUrl != null){

                int color = 0xFFFFFFFF;
                if (highlight != -1) color = 0xFF000000 | highlight;
                this.linkUnderline = new ColorBlock(1, 1, color);
                add(this.linkUnderline);

                this.link = PixelScene.renderTextBlock(linkText, textSize);
                this.link.maxWidth(maxWidth);
                if (highlight != -1) this.link.hardlight(highlight);
                add(this.link);

                linkButton = new PointerArea(0, 0, 0, 0){
                    @Override
                    protected void onClick( PointerEvent event ) {
                        DeviceCompat.openURI( linkUrl );
                    }
                };
                add(linkButton);
            }

        }

        @Override
        protected void layout() {
            super.layout();

            float topY = top();

            if (link != null) {
                //link.maxWidth((int)width());
                link.setPos( x + (width() - link.width())/2f, topY);
                topY += link.height() + 2;

                linkButton.x = link.left()-1;
                linkButton.y = link.top()-1;
                linkButton.width = link.width()+2;
                linkButton.height = link.height()+2;

                linkUnderline.size(link.width(), PixelScene.align(0.49f));
                linkUnderline.x = link.left();
                linkUnderline.y = link.bottom()+1;

            }

            topY -= 2;

            height = Math.max(height, topY - top());
        }

    }
    private static class ChangesButton extends Button {

        private NinePatch bg;
        private RenderedTextBlock name;

        @Override
        protected void createChildren() {
            super.createChildren();

            bg = Chrome.get(Chrome.Type.GEM);
            add( bg );

            name = PixelScene.renderTextBlock(9);
            add(name);
        }

        public void hardlight(int color) {
            name.hardlight(color);
        }

        public void text(String text){
            name.text(text);
            layout();
        }

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
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size( width, height );

            name.setPos(
                    x + (width - name.width()) / 2f,
                    y + (height - name.height()) / 2f
            );
            align(name);

        }
    }
}
