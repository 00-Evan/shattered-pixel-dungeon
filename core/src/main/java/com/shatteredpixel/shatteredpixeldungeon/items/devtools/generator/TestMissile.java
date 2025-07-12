package com.shatteredpixel.shatteredpixeldungeon.items.devtools.generator;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.TestGenerator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Bolas;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.FishingSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ForceCube;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Javelin;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Kunai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingClub;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Tomahawk;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Trident;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

public class TestMissile extends TestGenerator {
    {
        image = ItemSpriteSheet.MISSILE_HOLDER;
    }

    private int selected = 0;
    private int item_quantity = 1;
    private int levelToGen = 0;
    private boolean bowGenerated = false;

    private static final String AC_BOW = "bow";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (!bowGenerated) actions.add(AC_BOW);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_GIVE)) {
            GameScene.show(new SettingsWindow());
        } else if (action.equals(AC_BOW)) {
            SpiritBow bow = new SpiritBow();
            bow.identify().collect();
            bowGenerated = true;
        }
    }

    private void createMissiles(){
        MissileWeapon m = Reflection.newInstance(missileList.get(selected));
        m.level(levelToGen);
        m.quantity(item_quantity);
        if(m.collect()){
            GLog.i(Messages.get(this, "collect_success", m.name()));
        }else{
            m.doDrop(curUser);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("item_quantity", item_quantity);
        bundle.put("selected", selected);
        bundle.put("level_to_gen", levelToGen);
        bundle.put("bow_generated", bowGenerated);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        item_quantity = bundle.getInt("item_quantity");
        selected = bundle.getInt("selected");
        levelToGen = bundle.getInt("level_to_gen");
        bowGenerated = bundle.getBoolean("bow_generated");
    }

    private static ArrayList<Class<? extends MissileWeapon>> missileList = new ArrayList<>();

    private Class<? extends MissileWeapon> idToMissile(int id) {
        switch (id) {
            case 0:
                return Dart.class;
            case 1:
                return ThrowingStone.class;
            case 2:
                return ThrowingKnife.class;
            case 3:
                return FishingSpear.class;
            case 4:
                return Shuriken.class;
            case 5:
                return ThrowingClub.class;
            case 6:
                return ThrowingSpear.class;
            case 7:
                return Kunai.class;
            case 8:
                return Bolas.class;
            case 9:
                return Javelin.class;
            case 10:
                return HeavyBoomerang.class;
            case 11:
            default:
                return Tomahawk.class;
            case 12:
                return ThrowingHammer.class;
            case 13:
                return Trident.class;
            case 14:
                return ForceCube.class;
        }
    }

    private void buildList() {
        if (missileList.isEmpty()) {
            for (int i = 0; i < 15; ++i) {
                missileList.add(idToMissile(i));
            }
        }
    }

    private class SettingsWindow extends Window {
        private RenderedTextBlock t_select;
        private OptionSlider o_level;
        private OptionSlider o_quantity;
        private RedButton b_create;
        private ArrayList<IconButton> buttonList = new ArrayList<>();
        private static final int WIDTH = 120;
        private static final int BTN_SIZE = 19;
        private static final int GAP = 2;

        public SettingsWindow() {
            buildList();
            createImage();

            t_select = PixelScene.renderTextBlock("", 6);
            t_select.text();
            t_select.setPos(0, buttonList.get(buttonList.size() - 1).bottom() + GAP);
            add(t_select);

            o_level = new OptionSlider(Messages.get(this, "level"), "0", "12", 0, 12) {
                @Override
                protected void onChange() {
                    levelToGen = getSelectedValue();
                }
            };
            o_level.setSelectedValue(levelToGen);
            o_level.setRect(0, t_select.bottom() + 2 * GAP, WIDTH, 24);
            add(o_level);

            o_quantity = new OptionSlider(Messages.get(this, "quantity"), "1", "10", 1, 10) {
                @Override
                protected void onChange() {
                    item_quantity = getSelectedValue();
                }
            };
            o_quantity.setSelectedValue(item_quantity);
            o_quantity.setRect(0, t_select.bottom() + 2 * GAP, WIDTH, 24);
            add(o_quantity);

            b_create = new RedButton(Messages.get(this, "create_button")) {
                @Override
                protected void onClick() {
                    createMissiles();
                }
            };
            add(b_create);
            updateText();
        }

        private void updateText() {
            t_select.text(Messages.get(TestMissile.class, "select", Messages.get(missileList.get(selected), "name")));
            layout();
        }

        private void layout() {
            t_select.setPos(0, buttonList.get(buttonList.size() - 1).bottom() + GAP);
            o_level.setRect(0, t_select.bottom() + 2 * GAP, WIDTH, 24);
            o_quantity.setRect(0, o_level.bottom() + GAP, WIDTH, 24);
            b_create.setRect(0, o_quantity.bottom() + GAP, WIDTH, 16);
            resize(WIDTH, (int) b_create.bottom() );
        }

        private void createImage() {
            float left;
            float top = GAP;
            int placed = 0;
            int row = 1;
            int picPerRow = 6;
            int len = missileList.size();
            left = (WIDTH - BTN_SIZE * Math.min(len, picPerRow)) / 2f;
            for (int i = 0; i < len; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        selected = Math.min(j, 15);
                        super.onClick();
                        updateText();
                    }
                };
                Image im = new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(missileList.get(i))).image));
                im.scale.set(1.0f);
                btn.icon(im);
                btn.setRect(left + placed * BTN_SIZE, top + (row - 1) * (BTN_SIZE + GAP), BTN_SIZE, BTN_SIZE);
                add(btn);
                placed++;
                if (placed > 0 && placed % picPerRow == 0) {
                    placed = 0;
                    left = (WIDTH - BTN_SIZE * Math.min(len - row * picPerRow, picPerRow)) / 2f;
                    row++;
                }
                buttonList.add(btn);
            }
        }


    }
}
