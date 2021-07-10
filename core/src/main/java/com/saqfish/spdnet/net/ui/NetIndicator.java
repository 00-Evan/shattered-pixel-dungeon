package com.saqfish.spdnet.net.ui;

import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.ui.Tag;
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
