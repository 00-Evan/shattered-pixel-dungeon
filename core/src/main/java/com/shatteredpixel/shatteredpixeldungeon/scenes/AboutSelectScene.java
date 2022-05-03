package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class AboutSelectScene extends PixelScene {

    private static final int SLOT_WIDTH = 120;
    private static final int SLOT_HEIGHT = 30;

    @Override
    public void create() {
        super.create();
        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize( w, h );
        add( archs );

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( w - btnExit.width(), 0 );
        add( btnExit );

        RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (w - title.width()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        int slotGap = landscape() ? 5 : 10;
        int slotCount = 2;
        int slotsHeight = slotCount*SLOT_HEIGHT + (slotCount - 1) * slotGap;

        float yPos = (h - slotsHeight)/2f;
        if (landscape()) yPos += 8;

        ChangesButton MLPDChanges = new ChangesButton() {
            @Override
            protected void onClick() {
                MLPDChangesScene.changesSelected = 0;
                ShatteredPixelDungeon.switchNoFade(MLPDAboutScene.class );
            }
        };
        MLPDChanges.text(Messages.get(ChangesScene.class, "mlpd"));
        MLPDChanges.setRect((w - SLOT_WIDTH) / 2f, yPos, SLOT_WIDTH, SLOT_HEIGHT);
        yPos += SLOT_HEIGHT + slotGap;
        align(MLPDChanges);
        add(MLPDChanges);

        ChangesButton SPDChanges = new ChangesButton() {
            @Override
            protected void onClick() {
                SPDChangesScene.changesSelected = 0;
                ShatteredPixelDungeon.switchNoFade( SPDAboutScene.class );
            }
        };
        SPDChanges.text(Messages.get(ChangesScene.class, "spd"));
        SPDChanges.setRect((w - SLOT_WIDTH) / 2f, yPos, SLOT_WIDTH, SLOT_HEIGHT);
        yPos += SLOT_HEIGHT + slotGap;
        align(SPDChanges);
        add(SPDChanges);

        fadeIn();

    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade( TitleScene.class );
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

