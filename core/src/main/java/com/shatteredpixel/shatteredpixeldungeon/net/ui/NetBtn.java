package com.shatteredpixel.shatteredpixeldungeon.net.ui;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.net.Util;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;

public class NetBtn extends StyledButton {
    public static final int HEIGHT = 24;
    public static final int MIN_WIDTH = 30;

    private ShatteredPixelDungeon instance = ((ShatteredPixelDungeon) ShatteredPixelDungeon.instance);

    public NetBtn() {
        super(Chrome.Type.GREY_BUTTON_TR, "");
        icon(Icons.get(Icons.GLOBE));
    }

    @Override
    public void update() {
        super.update();
        icon.brightness(instance.net.connected() ? 0.8f : 0.2f );
    }

    @Override
    protected void onClick() {
        super.onClick();
        Util.showServerInfo();
    }
}
