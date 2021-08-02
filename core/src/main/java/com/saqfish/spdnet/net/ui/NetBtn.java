package com.saqfish.spdnet.net.ui;

import com.saqfish.spdnet.Chrome;
import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.net.Settings;
import com.saqfish.spdnet.net.windows.NetWindow;
import com.saqfish.spdnet.ui.StyledButton;
import com.watabou.utils.PlatformSupport;

import java.net.URI;
import java.net.URISyntaxException;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;
import static com.watabou.noosa.Game.platform;

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

    @Override
    protected boolean onLongClick() {
        platform.promptTextInput("Enter host", Settings.uri().toString(), 40, false, "Set", "Cancel", new PlatformSupport.TextCallback() {
            @Override
            public void onSelect(boolean positive, String text) {
                if(positive){
                    URI url = null;
                    try {
                        url = new URI(text);
                        Settings.scheme(url.getScheme());
                        Settings.address(url.getHost());
                        Settings.port(url.getPort());
                        platform.promptTextInput("Enter key", Settings.auth_key(), 20, false, "Set", "Cancel", new PlatformSupport.TextCallback() {
                            @Override
                            public void onSelect(boolean positive, String text) {
                                if(positive){
                                    Settings.auth_key(text);
                                }
                            }
                        });
                    } catch (URISyntaxException e) {
                    }
                }
            }
        });
        return true;
    }
}
