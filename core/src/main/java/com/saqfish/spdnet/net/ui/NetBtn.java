package com.saqfish.spdnet.net.ui;

import com.saqfish.spdnet.Chrome;
import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.net.windows.NetWindow;
import com.saqfish.spdnet.ui.StyledButton;

public class NetBtn extends StyledButton {
    public static final int HEIGHT = 24;
    public static final int MIN_WIDTH = 30;

    private ShatteredPixelDungeon instance = ((ShatteredPixelDungeon) ShatteredPixelDungeon.instance);

    public NetBtn() {
        super(Chrome.Type.GREY_BUTTON_TR, "");
        icon(NetIcons.get(NetIcons.GLOBE));
    }

    @Override
    public void update() {
        super.update();
        icon.brightness(instance.net.connected() ? 0.8f : 0.2f );
    }

    @Override
    protected void onClick() {
        super.onClick();
        NetWindow.showServerInfo();
    }
}
