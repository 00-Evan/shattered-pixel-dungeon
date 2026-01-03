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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.right.helveticpixeldungeon.scenes.ChangesHPDScene;
import com.shatteredpixel.shatteredpixeldungeon.*;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndVictoryCongrats;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.*;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;

public class TitleScene extends PixelScene {

    @Override
    public void create() {

        super.create();

        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.THEME_1, Assets.Music.THEME_2},
                new float[]{1, 1},
                false);

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        Image title = BannerSprites.get(landscape() ? BannerSprites.Type.TITLE_LAND : BannerSprites.Type.TITLE_PORT);
        add(title);

        float topRegion = Math.max(title.height - 6, h * 0.45f);

        title.x = (w - title.width()) / 2f;
        title.y = 2 + (topRegion - title.height()) / 2f;

        align(title);

        if (landscape()) {
            placeTorch(title.x + 30, title.y + 35);
            placeTorch(title.x + title.width - 30, title.y + 35);
        } else {
            placeTorch(title.x + 16, title.y + 70);
            placeTorch(title.x + title.width - 16, title.y + 70);
        }

        Image signs = new Image(BannerSprites.get(landscape() ? BannerSprites.Type.TITLE_GLOW_LAND : BannerSprites.Type.TITLE_GLOW_PORT)) {
            private float time = 0;

            @Override
            public void update() {
                super.update();
                am = Math.max(0f, (float) Math.sin(time += Game.elapsed));
                if (time >= 1.5f * Math.PI) time = 0;
            }

            @Override
            public void draw() {
                Blending.setLightMode();
                super.draw();
                Blending.setNormalMode();
            }
        };
        signs.x = title.x + (title.width() - signs.width()) / 2f;
        signs.y = title.y;
        add(signs);

        final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;

        StyledButton btnPlay = new StyledButton(GREY_TR, Messages.get(this, "enter")) {
            @Override
            protected void onClick() {
                if (GamesInProgress.checkAll().isEmpty()) {
                    GamesInProgress.selectedClass = null;
                    GamesInProgress.curSlot = 1;
                    ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
                } else {
                    ShatteredPixelDungeon.switchNoFade(StartScene.class);
                }
            }

            @Override
            protected boolean onLongClick() {
                //making it easier to start runs quickly while debugging
                if (DeviceCompat.isDebug()) {
                    GamesInProgress.selectedClass = null;
                    GamesInProgress.curSlot = 1;
                    ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
                    return true;
                }
                return super.onLongClick();
            }
        };
        btnPlay.icon(Icons.get(Icons.ENTER));
        add(btnPlay);

        // support button deleted here.
        // StyledButton btnSupport = new SupportButton(GREY_TR, Messages.get(this, "support"));
        // add(btnSupport);

        StyledButton btnRankings = new StyledButton(GREY_TR, Messages.get(this, "rankings")) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.switchNoFade(RankingsScene.class);
            }
        };
        btnRankings.icon(Icons.get(Icons.RANKINGS));
        add(btnRankings);
        Dungeon.daily = Dungeon.dailyReplay = false;

        StyledButton btnBadges = new StyledButton(GREY_TR, Messages.get(this, "journal")) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.switchNoFade(JournalScene.class);
            }
        };
        btnBadges.icon(Icons.get(Icons.JOURNAL));
        add(btnBadges);

        //StyledButton btnNews = new NewsButton(GREY_TR, Messages.get(this, "news"));
        //btnNews.icon(Icons.get(Icons.NEWS));
        //add(btnNews);

        StyledButton btnChanges = new ChangesButton(GREY_TR, Messages.get(this, "changes"));
        btnChanges.icon(Icons.get(Icons.CHANGES));
        add(btnChanges);

        StyledButton btnSettings = new SettingsButton(GREY_TR, Messages.get(this, "settings"));
        add(btnSettings);

        StyledButton btnAbout = new AboutButton(GREY_TR, Messages.get(this, "about"));

        add(btnAbout);

        final int BTN_HEIGHT = 20;
        int GAP = (int) (h - topRegion - (landscape() ? 3 : 4) * BTN_HEIGHT) / 3;
        GAP /= landscape() ? 3 : 5;
        GAP = Math.max(GAP, 2);

        float buttonAreaWidth = landscape() ? PixelScene.MIN_WIDTH_L - 6 : PixelScene.MIN_WIDTH_P - 2;
        float btnAreaLeft = (Camera.main.width - buttonAreaWidth) / 2f;
        if (landscape()) {
            btnPlay.setRect(btnAreaLeft, topRegion + GAP, (buttonAreaWidth / 2) - 1, BTN_HEIGHT);
            align(btnPlay);
            //btnSupport.setRect(btnPlay.right() + 2, btnPlay.top(), btnPlay.width(), BTN_HEIGHT);
            btnRankings.setRect(btnPlay.right() + 2, btnPlay.top(), btnPlay.width() /*(float) (Math.floor(buttonAreaWidth / 3f) - 1)*/, BTN_HEIGHT);
            btnBadges.setRect(btnPlay.left(), btnRankings.bottom() + GAP, btnPlay.width(), BTN_HEIGHT);
            // btnNews.setRect(btnBadges.right() + 2, btnBadges.top(), btnRankings.width(), BTN_HEIGHT);
            btnSettings.setRect(btnBadges.right() + 2, btnBadges.top(), btnRankings.width(), BTN_HEIGHT);
            btnChanges.setRect(btnBadges.left(), btnSettings.bottom() + GAP, btnPlay.width(), BTN_HEIGHT);
            btnAbout.setRect(btnBadges.right() + 2, btnSettings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
        } else {
            btnPlay.setRect(btnAreaLeft, topRegion + GAP, buttonAreaWidth, BTN_HEIGHT);
            align(btnPlay);
            //btnSupport.setRect(btnPlay.left(), btnPlay.bottom() + GAP, btnPlay.width(), BTN_HEIGHT);
            btnRankings.setRect(btnPlay.left(), btnPlay.bottom() + GAP,btnPlay.width() , BTN_HEIGHT);
            btnBadges.setRect(btnRankings.left(), btnRankings.bottom()+ GAP, (btnPlay.width() / 2) - 1 , BTN_HEIGHT);
            //btnNews.setRect(btnRankings.left(), btnRankings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
            btnChanges.setRect(btnBadges.right()+2, btnBadges.top(), btnBadges.width(), BTN_HEIGHT);
            btnSettings.setRect(btnBadges.left(), btnChanges.bottom() + GAP, btnBadges.width(), BTN_HEIGHT);
            btnAbout.setRect(btnSettings.right() + 2, btnSettings.top(), btnSettings.width(), BTN_HEIGHT);
        }

        BitmapText version = new BitmapText("v" + Game.getVersion(), pixelFont);
        version.measure();
        version.hardlight(0x888888);
        version.x = w - version.width() - 4;
        version.y = h - version.height() - 2;
        add(version);

        if (DeviceCompat.isDesktop()) {
            ExitButton btnExit = new ExitButton();
            btnExit.setPos(w - btnExit.width(), 0);
            add(btnExit);
        }

        Badges.loadGlobal();
        if (Badges.isUnlocked(Badges.Badge.VICTORY) && !SPDSettings.victoryNagged()) {
            SPDSettings.victoryNagged(true);
            add(new WndVictoryCongrats());
        }

        fadeIn();
    }

    private void placeTorch(float x, float y) {
        Fireball fb = new Fireball();
        fb.x = x - fb.width() / 2f;
        fb.y = y - fb.height();

        align(fb);
        add(fb);
    }

    private static class ChangesButton extends StyledButton {

        boolean updateShown = false;

        public ChangesButton(Chrome.Type type, String label) {
            super(type, label);
            //if (SPDSettings.updates()) Updates.checkForUpdate();
        }


        @Override
        protected void onClick() {


            // ShatteredPixelDungeon.switchNoFade(ChangesHPDScene.class);
            ShatteredPixelDungeon.switchNoFade(ChangesHPDScene.class);

        }

    }

    private static class SettingsButton extends StyledButton {

        public SettingsButton(Chrome.Type type, String label) {
            super(type, label);
            if (Messages.lang().status() == Languages.Status.X_UNFINISH) {
                icon(Icons.get(Icons.LANGS));
                icon.hardlight(1.5f, 0, 0);
            } else {
                icon(Icons.get(Icons.PREFS));
            }
        }

        @Override
        public void update() {
            super.update();

            if (Messages.lang().status() == Languages.Status.X_UNFINISH) {
                textColor(ColorMath.interpolate(0xFFFFFF, CharSprite.NEGATIVE, 0.5f + (float) Math.sin(Game.timeTotal * 5) / 2f));
            }
        }

        @Override
        protected void onClick() {
            if (Messages.lang().status() == Languages.Status.X_UNFINISH) {
                WndSettings.last_index = 5;
            }
            ShatteredPixelDungeon.scene().add(new WndSettings());
        }
    }

    private static class AboutButton extends StyledButton {
        public AboutButton(Chrome.Type type, String label) {
            super(type, label);
            this.icon(Icons.get(Icons.DISPLAY));


        }

        @Override
        protected void onClick() {
            ShatteredPixelDungeon.switchScene(AboutScene.class);
        }
    }
}
