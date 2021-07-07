package com.shatteredpixel.shatteredpixeldungeon.net.ui;

import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Tag;
import com.watabou.input.GameAction;
import com.watabou.noosa.Image;

public class NetIndicator extends Tag {

    public static final int COLOR	= 0xFF4C4C;

    private Image icon;

    public NetIndicator() {
        super( 0xFF4C4C );
        setSize( 10, 10 );
        visible = true;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        icon = NetIcons.get(NetIcons.GLOBE);
        icon.scale.set(PixelScene.align(0.5f));
        add( icon );
    }

    @Override
    protected void layout() {
        super.layout();
        icon.x = right() - icon.width()-2;
        icon.y = y+2;
    }

    @Override
    public void update() {
        super.update();
        icon.alpha(ShatteredPixelDungeon.net().connected() ? 1f : 0.2f);
    }

    @Override
    protected void onClick() {
    }
}
