package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

public class MLPDAboutScene extends PixelScene {

    private static String TTL_MLPD() { return Messages.get(ChangesScene.class, "mlpd");}

    private static String TXT_MLPD() { return Messages.get(MLPDAboutScene.class, "txt_mlpd");}

    private static String TTL_ANSDO() { return Messages.get(MLPDAboutScene.class, "ttl_ansdo");}

    private static String TXT_ANSDO() { return Messages.get(MLPDAboutScene.class, "txt_ansdo") + "\n冷群怪 & 奈亚子 & 被子";}

    private static String LNK_MLPD_SOURCE() { return "https://github.com/AnsdoShip/Magic-Ling-Pixel-Dungeon-Ling";}
    private static String TXT_MLPD_SOURCE() { return Messages.get(MLPDAboutScene.class,"github");}

    private static String LNK_QQDiscord() { return Messages.get(MLPDAboutScene.class,"joinlink");}
    private static String TXT_QQDiscord() { return Messages.get(MLPDAboutScene.class,"join");}

    @Override
    public void create() {
        super.create();
        final float colWidth = Camera.main.width / (landscape() ? 2 : 1);
        final float colTop = (Camera.main.height / 2) - (landscape() ? 30 : 90);
        final float colOffset = landscape() ? colWidth : 0;

        Image ling = Icons.LING.get();
        ling.x = (colWidth - ling.width()) / 2;
        ling.y = colTop;
        align( ling );
        add( ling );

        new Flare( 12, 64 ).color( Window.SKYBULE_COLOR, true ).show( ling, 0 ).angularSpeed = +20;

        RenderedTextBlock mlpdtitle = renderTextBlock( TTL_MLPD(), 8 );
        mlpdtitle.hardlight( Window.MLPD_COLOR );
        add( mlpdtitle );

        mlpdtitle.x = (colWidth - mlpdtitle.width()) / 2;
        mlpdtitle.y = ling.y + ling.height + 5;
        align(mlpdtitle);

        RenderedTextBlock mlpdtext = renderTextBlock( TXT_MLPD(), 8 );
        mlpdtext.maxWidth((int)Math.min(colWidth, 120));
        add( mlpdtext );

        mlpdtext.setPos((colWidth - mlpdtext.width()) / 2, mlpdtitle.y + mlpdtitle.height() + 12);
        align(mlpdtext);

        LinkBlock mlpdLink = new LinkBlock(Window.TITLE_COLOR, TXT_MLPD_SOURCE(), LNK_MLPD_SOURCE(),
                mlpdtext.maxWidth(), 8);
        add(mlpdLink);

        mlpdLink.setPos((colWidth - mlpdLink.width()) / 2, mlpdtext.bottom() + 12);
        align(mlpdLink);

        LinkBlock qqLink = new LinkBlock(Window.TITLE_COLOR, TXT_QQDiscord(), LNK_QQDiscord(), (int)Math.min(colWidth, 120), 8);
        add(qqLink);

        qqLink.setPos((colWidth - qqLink.width()) / 2 , mlpdLink.bottom() + 12);
        align(qqLink);

        RenderedTextBlock ansdotitle = renderTextBlock( TTL_ANSDO(), 8 );
        ansdotitle.hardlight( Window.ANSDO_COLOR );
        add( ansdotitle );

        ansdotitle.x = colOffset + (colWidth - ansdotitle.width()) / 2;
        ansdotitle.y = landscape() ? colTop : qqLink.bottom() + 12;
        align( ansdotitle );

        RenderedTextBlock ansdotext = renderTextBlock( TXT_ANSDO(), 8 );
        ansdotext.maxWidth((int)Math.min(colWidth, 120));
        add( ansdotext );

        ansdotext.setPos(colOffset + (colWidth - ansdotext.width()) / 2, ansdotitle.y + ansdotitle.height() + 12);
        align( ansdotext );


        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        ExitButton btnExit = new ExitButton() {
            @Override
            protected void onClick() {
                if (Game.scene() instanceof TitleScene) {
                    Game.instance.finish();
                } else {
                    ShatteredPixelDungeon.switchNoFade( AboutSelectScene.class );
                }
            }
        };
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade( AboutSelectScene.class );
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

}
