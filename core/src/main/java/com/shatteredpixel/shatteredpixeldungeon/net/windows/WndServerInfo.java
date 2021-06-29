package com.shatteredpixel.shatteredpixeldungeon.net.windows;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.net.Net;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTabbed;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

public class WndServerInfo extends WndTabbed {
    private static final int WIDTH_P	    = 122;
    private static final int WIDTH_L	    = 223;

    private static final int BTN_HEIGHT	    = 16;
    private static final float GAP          = 2;

    public static int last_index = 0;

    public WndServerInfo(){
        super();

        float height;

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        InfoTab info = new InfoTab();
        info.setSize(width, 0);
        height = info.height();
        add( info );

        add( new IconTab(Icons.get(Icons.NET)){
            @Override
            protected void select(boolean value) {
                super.select(value);
                info.visible = info.active = value;
                if (value) last_index = 0;
            }
        });

        PlayersTab players = new PlayersTab();
        players.setSize(width, 0);
        height = Math.max(height, info.height());
        add( players );

        add( new IconTab(Icons.get(Icons.MAGE)){
            @Override
            protected void select(boolean value) {
                super.select(value);
                players.visible = players.active = value;
                if (value) last_index = 0;
            }
        });
        resize(width, (int)Math.ceil(height));

        layoutTabs();

        select(last_index);
    }


    private static class InfoTab extends Component {
        RenderedTextBlock title;
        ColorBlock sep1;
        LabeledText type;
        LabeledText host;
        LabeledText port;
        LabeledText status;
        RedButton connectBtn;

        Net net;
        @Override
        protected void createChildren() {
            net = ((ShatteredPixelDungeon) ShatteredPixelDungeon.instance).net();

            title = PixelScene.renderTextBlock("Server Info", 12);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            type = new LabeledText("Type", net.uri().getScheme());
            add(type);

            host = new LabeledText("Host", net.uri().getHost());
            add(host);

            port = new LabeledText("Port", String.valueOf(net.uri().getPort()));
            add(port);



            connectBtn = new RedButton("Connect"){
                @Override
                public synchronized void update() {
                    super.update();
                    text.text(net.connected() ? "Disconnect": "Connect");
                    setSize(net.connected() ? 50: 40,BTN_HEIGHT);
                    setPos( InfoTab.this.width - width, 0);
                }

                @Override
                protected void onClick() {
                    super.onClick();
                    net.toggle();
                }
            };
            add(connectBtn);

            status = new LabeledText("Status", net.connected() ? "Connected": "Disconnected"){
                @Override
                public synchronized void update() {
                    super.update();
                    text.text(net.connected() ? "Connected": "Disconnected");
                    text.hardlight(net.connected() ? 0x00FF00: 0xFF0000);
                }
            };
            add(status);

        }

        @Override
        protected void layout() {
            float bottom = y;

            title.setPos(GAP , bottom + 2*GAP);
            connectBtn.setSize(40,BTN_HEIGHT);
            connectBtn.setPos(width - (connectBtn.reqWidth() + 3* GAP), 0);

            sep1.size(width, 1);
            sep1.y = connectBtn.bottom() + GAP;

            bottom = sep1.y +1;

            float right = 0;

            type.setPos(right, bottom + GAP);
            bottom = type.bottom() + GAP;

            host.setPos(right + host.width(), bottom + GAP);
            bottom = host.bottom() + GAP;

            port.setPos(right + port.width(), bottom + GAP);
            bottom = port.bottom() + GAP;

            right = 0;

            status.setPos(right, bottom + GAP);

            height = status.bottom() + 4*GAP;
        }
    }

    private static class LabeledText extends Component {
        RenderedTextBlock label;
        RenderedTextBlock text;

        public LabeledText(String label, String text){
            this.label = PixelScene.renderTextBlock(label+": ", 9);
            add(this.label);

            this.text = PixelScene.renderTextBlock(text, 9);
            add(this.text);
        }

        @Override
        protected void layout() {
            float bottom = y;
            float right = x;

            label.setPos(right, bottom + GAP);

            right = label.right() + GAP;

            text.setPos(right, bottom + GAP);

            height = text.height();
            width = label.width() + text.width() + GAP;
        }
    }

    private static class PlayersTab extends Component {
        RenderedTextBlock title;
        ColorBlock sep1;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock("Players", 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);
        }

        @Override
        protected void layout() {
            float bottom = y;
            title.setPos((width - title.width())/2, bottom + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 2*GAP;

            height = sep1.y;
        }
    }
}
