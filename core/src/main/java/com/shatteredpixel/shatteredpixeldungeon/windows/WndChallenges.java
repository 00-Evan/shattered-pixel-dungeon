/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.*;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndChallenges extends Window {


    private ChallengesScrollPane list;

    public WndChallenges(long checked, boolean editable) {

        super();

        this.list = new ChallengesScrollPane(checked, editable);
        add(this.list);
        list.setRect(0, 0, width, height);
        list.scrollTo(0, 0);
    }

    @Override
    public void onBackPressed() {
        list.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public void offset(int xOffset, int yOffset) {
        super.offset(xOffset, yOffset);

        list.scrollTo(0, yOffset);
    }

    public static class ChallengesIconButton extends IconButton {
        String challenge;

        public ChallengesIconButton(Image icon, String challenge) {
            super(icon);
            this.challenge = challenge;
        }

        @Override
        public void onClick() {
            super.onClick();

            ShatteredPixelDungeon.scene().add(new WndMessage(Messages.get(Challenges.class, challenge + "_desc")));
        }
    }

    public class ChallengesScrollPane extends ScrollPane {

        private static final int WIDTH = 120;
        private static final int TTL_HEIGHT = 16;
        private static final int BTN_HEIGHT = 16;
        private static final int GAP = 1;
        private final ArrayList<CheckBox> boxes;
        private final ArrayList<Button> infos;
        private RenderedTextBlock title;
        private final boolean editable;

        public ChallengesScrollPane(long checked, boolean editable) {
            super(new Component());
            disableThumb();

            this.editable = editable;

            title = PixelScene.renderTextBlock(Messages.get(WndChallenges.this, "title"), 12);
            title.hardlight(TITLE_COLOR);
            title.setPos((WIDTH - title.width()) / 2, (TTL_HEIGHT - title.height()) / 2);
            PixelScene.align(title);
            content.add(title);

            boxes = new ArrayList<>();
            infos = new ArrayList<>();

            float pos = TTL_HEIGHT;
            for (int i = 0; i < Challenges.NAME_IDS.length; i++) {

                final String challenge = Challenges.NAME_IDS[i];

                CheckBox cb = new CheckBox(Messages.titleCase(Messages.get(Challenges.class, challenge)));


                cb.checked((checked & Challenges.MASKS[i]) != 0);
                cb.active = false;

                if (i > 0) {
                    pos += GAP;
                }
                cb.setRect(0, pos, WIDTH - 16, BTN_HEIGHT);

                content.add(cb);
                boxes.add(cb);

                ChallengesIconButton info = new ChallengesIconButton(Icons.get(Icons.INFO), challenge);
                info.setRect(cb.right(), pos, 16, BTN_HEIGHT);
                content.add(info);
                infos.add(info);

                pos = cb.bottom();
            }
            content.setSize(width, pos + 16);
            WndChallenges.this.resize(WIDTH, TTL_HEIGHT + BTN_HEIGHT * 9 + GAP * 8);

        }

        public void onBackPressed() {
            if (editable) {
                long value = 0;
                for (int i = 0; i < boxes.size(); i++) {
                    if (boxes.get(i).checked()) {
                        value |= Challenges.MASKS[i];
                    }
                }
                SPDSettings.challenges(value);
            }
        }

        @Override
        protected void layout() {
            super.layout();
            float pos = TTL_HEIGHT;
            for (var info : infos) {
                pos += info.height();
            }
            content.setSize(width, pos + 16);
        }


        @Override
        public void onClick(float x, float y) {
            for (var box : boxes) {
                if (this.editable && box.inside(x, y)) {
                    box.checked(!box.checked());
                    return;
                }
            }
            for (var info : infos) {
                if ((info instanceof ChallengesIconButton btn) && info.inside(x, y)) {
                    btn.onClick();
                    return;
                }
            }
        }
    }


}