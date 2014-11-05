package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ui.Component;

/**
 * Created by debenhame on 14/10/2014.
 */
//TODO: this is completely broken at the moment. Implement soon if it's needed!
public class WndChanges extends Window {

    private static final int WIDTH	= 112;
    private static final int HEIGHT	= 160;

    private static final String TXT_TITLE	= "Changes";

    private static final String TXT_CHANGES	= "test test test test test test test test test test test test test test test" +
            "\n\n test test test test test test test test test test test test test test test\ntest test test test " +
            "test test test test test test\n\n\ntest test test test testtest test test test test";

    private BitmapText txtTitle;
    private ScrollPane text;

    public WndChanges() {
        super();
        resize( WIDTH, HEIGHT );

        txtTitle = PixelScene.createText(TXT_TITLE, 9);
        txtTitle.hardlight( Window.SHPX_COLOR );
        txtTitle.measure();
        txtTitle.x = PixelScene.align( PixelScene.uiCamera, (WIDTH - txtTitle.width()) / 2 );
        add( txtTitle );

        BitmapTextMultiline txtChanges = PixelScene.createMultiline(TXT_CHANGES, 9);

        Component content = new Component();

        content.add(txtChanges);

        text = new ScrollPane( content ) {

        };
        add( text );
        text.setRect( 0, txtTitle.height(), WIDTH, HEIGHT - txtTitle.height() );
    }
}
