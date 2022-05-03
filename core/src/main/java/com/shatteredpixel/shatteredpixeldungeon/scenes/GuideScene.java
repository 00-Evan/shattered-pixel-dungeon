package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.BooksButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class GuideScene extends PixelScene {

    private ArrayList<ListItem> items_ = new ArrayList<>();
    private RenderedTextBlock rtmInfo_ = null;
    private int currentIdx = -1;

    @Override
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

        // title
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 12);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos((w - title.width()) / 2 , 4);
        align(title);
        add(title);

        BooksButton btnExit = new BooksButton();
        btnExit.setPos(w - btnExit.width(), 0);
        add(btnExit);

        Archs archs = new Archs();
        archs.setSize(Camera.main.width, Camera.main.height);
        addToBack(archs);

        NinePatch panel = Chrome.get(Chrome.Type.WINDOW);
        {
            int pw = w - 6;
            int ph = h - 20;
            panel.size(pw, ph);

            panel.x = (w - pw) / 2;
            panel.y = 4 + title.height() + 2;
        }
        add(panel);

        ScrollPane sp = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                for (ListItem li : items_) {
                    if (li.onClick(x, y))
                        break;
                }
            }
        };
        add(sp);
        {
            float WIDTH_LISTITEM = landscape() ? 80f : 40f;
            float HEIGHT_LISTITEM = landscape() ? 12f : 20f;
            final float GAP_LISTITEM = 1;
            final int CNT_GUIDES = Integer.valueOf(Messages.get(this, "pages"));
            //12
            Component content = sp.content();
            content.clear();
            float libtm = 0f;
            for (int idx = 0; idx < CNT_GUIDES; ++idx) {
                final int index = idx;
                ListItem li = new ListItem(Messages.get(this, "title_" + index)) {
                    @Override
                    protected void onClick() {
                        showDescription(index);
                    }
                };
                li.setRect(0f, index * (HEIGHT_LISTITEM + GAP_LISTITEM),
                        WIDTH_LISTITEM, HEIGHT_LISTITEM);
                content.add(li);
                items_.add(li);
                libtm = li.bottom();
            }

            ColorBlock cb = new ColorBlock(1, panel.height, 0xFF000000);
            cb.x = WIDTH_LISTITEM + 1;
            cb.y = HEIGHT_LISTITEM;
            content.add(cb);
            rtmInfo_ = PixelScene.renderTextBlock(7);
            rtmInfo_.text("select to display");
            rtmInfo_.maxWidth((int) (panel.innerWidth() - WIDTH_LISTITEM - 2f));
            rtmInfo_.setPos(cb.x + 2, 2f);
            content.add(rtmInfo_);
            content.setSize(panel.innerWidth(), libtm);
        }
        sp.setRect(panel.x + panel.marginLeft(), panel.y + panel.marginTop(),
                panel.innerWidth(), panel.innerHeight());
        sp.scrollTo(0, 0);
        fadeIn();
        showDescription(0);
    }

    void showDescription(int idx) {
        if (idx == currentIdx)
            return;
        if (currentIdx >= 0)
            items_.get(currentIdx).highlight(false);
        currentIdx = idx;
        items_.get(currentIdx).highlight(true);
        rtmInfo_.text(Messages.get(this, "page_" + idx));
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(GameScene.class);
    }

    private static class ListItem extends Component {
        private RenderedTextBlock feature;
        private ColorBlock line;

        public ListItem(String text) {
            super();
            feature.text(text);
        }

        public void highlight(boolean hl) {
            if (hl)
                feature.hardlight(0x00ffff);
            else
                feature.hardlight(0xffffff);
        }

        @Override
        protected void createChildren() {
            feature = PixelScene.renderTextBlock(6);
            add(feature);
            line = new ColorBlock(1, 1, 0xFF222222);
            add(line);
        }

        @Override
        protected void layout() {
            line.size(width, 1);
            line.x = 0;
            line.y = y;
            feature.maxWidth((int) (width - 8 - 1));
            feature.setPos(4, y + 1 + (height() - 1 - feature.height()) / 2);
            PixelScene.align(feature);
        }

        protected boolean onClick(float x, float y) {
            if (inside(x, y)) {
                onClick();
                return true;
            } else {
                return false;
            }
        }

        protected void onClick() {
        }
    }
}