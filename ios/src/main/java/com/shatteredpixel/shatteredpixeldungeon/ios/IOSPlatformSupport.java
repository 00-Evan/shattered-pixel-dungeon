package com.shatteredpixel.shatteredpixeldungeon.ios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSGraphics;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.noosa.Game;
import com.watabou.utils.PlatformSupport;

import org.robovm.apple.audiotoolbox.AudioServices;
import org.robovm.apple.systemconfiguration.SCNetworkReachability;
import org.robovm.apple.systemconfiguration.SCNetworkReachabilityFlags;
import org.robovm.apple.uikit.UIApplication;

import java.util.HashMap;
import java.util.regex.Pattern;

public class IOSPlatformSupport extends PlatformSupport {
    @Override
    public void updateDisplaySize() {
        //non-zero safe insets on left/top/right means device has a notch, show status bar
        if (Gdx.graphics.getSafeInsetTop() != 0
                || Gdx.graphics.getSafeInsetLeft() != 0
                || Gdx.graphics.getSafeInsetRight() != 0){
            UIApplication.getSharedApplication().setStatusBarHidden(false);
        } else {
            UIApplication.getSharedApplication().setStatusBarHidden(true);
        }

        if (!SPDSettings.fullscreen()) {
            Game.bottomInset = Gdx.graphics.getSafeInsetBottom();
            Game.height -= Game.bottomInset;
            Game.dispHeight = Game.height;
        } else {
            Game.height += Game.bottomInset;
            Game.dispHeight = Game.height;
            Game.bottomInset = 0;
        }
        Gdx.gl.glViewport(0, Game.bottomInset, Game.width, Game.height);
    }

    @Override
    public void updateSystemUI() {
        updateDisplaySize();
        ShatteredPixelDungeon.seamlessResetScene();
    }

    @Override
    public boolean connectedToUnmeteredNetwork() {
        SCNetworkReachability test = new SCNetworkReachability("www.apple.com");
        return !test.getFlags().contains(SCNetworkReachabilityFlags.IsWWAN);
    }

    @Override
    public void promptTextInput(String title, String hintText, int maxLen, boolean multiLine, String posTxt, String negTxt, TextCallback callback) {
        //TODO need multiplat text input, this does nothing atm!
    }

    public void vibrate( int millis ){
        //gives a short vibrate on iPhone 6+, no vibration otherwise
        AudioServices.playSystemSound(1520);
    }

    private int pageSize;
    private PixmapPacker packer;
    private boolean systemfont;

    //custom pixel font, for use with Latin and Cyrillic languages
    private static FreeTypeFontGenerator basicFontGenerator;
    private static HashMap<Integer, BitmapFont> basicFonts = new HashMap<>();

    //droid sans fallback, for asian fonts
    private static FreeTypeFontGenerator asianFontGenerator;
    private static HashMap<Integer, BitmapFont> asianFonts = new HashMap<>();

    private static HashMap<FreeTypeFontGenerator, HashMap<Integer, BitmapFont>> fonts;

    @Override
    public void setupFontGenerators(int pageSize, boolean systemfont) {
        //don't bother doing anything if nothing has changed
        if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont){
            return;
        }
        this.pageSize = pageSize;
        this.systemfont = systemfont;

        if (fonts != null){
            for (FreeTypeFontGenerator generator : fonts.keySet()){
                for (BitmapFont f : fonts.get(generator).values()){
                    f.dispose();
                }
                fonts.get(generator).clear();
                generator.dispose();
            }
            fonts.clear();
            if (packer != null){
                for (PixmapPacker.Page p : packer.getPages()){
                    p.getTexture().dispose();
                }
                packer.dispose();
            }
        }
        fonts = new HashMap<>();

        if (systemfont) {
            basicFontGenerator = asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
        } else {
            basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
            asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
        }

        fonts.put(basicFontGenerator, basicFonts);
        fonts.put(asianFontGenerator, asianFonts);

        packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
    }

    @Override
    public void resetGenerators() {
        if (fonts != null) {
            for (FreeTypeFontGenerator generator : fonts.keySet()) {
                for (BitmapFont f : fonts.get(generator).values()) {
                    f.dispose();
                }
                fonts.get(generator).clear();
                generator.dispose();
            }
            fonts.clear();
            if (packer != null) {
                for (PixmapPacker.Page p : packer.getPages()) {
                    p.getTexture().dispose();
                }
                packer.dispose();
            }
            fonts = null;
        }
        setupFontGenerators(pageSize, systemfont);
    }

    @Override
    public void reloadGenerators() {
        if (packer != null) {
            for (PixmapPacker.Page p : packer.getPages()) {
                p.getTexture().dispose();
                p.updateTexture(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
            }
        }
    }

    private static Pattern asianMatcher = Pattern.compile("\\p{InHangul_Syllables}|" +
            "\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}|\\p{InHalfwidth_and_Fullwidth_Forms}|" +
            "\\p{InHiragana}|\\p{InKatakana}");

    private static FreeTypeFontGenerator getGeneratorForString( String input ){
        if (asianMatcher.matcher(input).find()){
            return asianFontGenerator;
        } else {
            return basicFontGenerator;
        }
    }


    @Override
    public BitmapFont getFont(int size, String text) {
        FreeTypeFontGenerator generator = getGeneratorForString(text);

        if (generator == null){
            return null;
        }

        if (!fonts.get(generator).containsKey(size)) {
            FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameters.size = size;
            parameters.flip = true;
            parameters.borderWidth = parameters.size / 10f;
            if (size >= 20){
                parameters.renderCount = 2;
            } else {
                parameters.renderCount = 3;
            }
            parameters.hinting = FreeTypeFontGenerator.Hinting.None;
            parameters.spaceX = -(int) parameters.borderWidth;
            parameters.incremental = true;
            if (generator == basicFontGenerator){
                //if we're using latin/cyrillic, we can safely pre-generate some common letters
                //(we define common as >4% frequency in english)
                parameters.characters = "�etaoinshrdl";
            } else {
                parameters.characters = "�";
            }
            parameters.packer = packer;

            try {
                BitmapFont font = generator.generateFont(parameters);
                font.getData().missingGlyph = font.getData().getGlyph('�');
                fonts.get(generator).put(size, font);
            } catch ( Exception e ){
                Game.reportException(e);
                return null;
            }
        }

        return fonts.get(generator).get(size);
    }

    //splits on newlines, underscores, and chinese/japaneses characters
    private Pattern regularsplitter = Pattern.compile(
            "(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
                    "(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
                    "(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
                    "(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
                    "(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})");

    //additionally splits on words, so that each word can be arranged individually
    private Pattern regularsplitterMultiline = Pattern.compile(
            "(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
                    "(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
                    "(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
                    "(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
                    "(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})");

    @Override
    public String[] splitforTextBlock(String text, boolean multiline) {
        if (multiline) {
            return regularsplitterMultiline.split(text);
        } else {
            return regularsplitter.split(text);
        }
    }
}
