package com.shatteredpixel.shatteredpixeldungeon.items.devtools.generator;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.*;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.*;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.TestGenerator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class TestArmor extends TestGenerator {
    {
        image = ItemSpriteSheet.ARMOR_HOLDER;
    }

    private int tier = 1;
    private boolean cursed = false;
    private int levelToGen = 0;
    private int enchant_id = 0;
    private int enchant_rarity = 0;

    @Override
    public ArrayList<String> actions(Hero hero) {
        return super.actions(hero);
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_GIVE)) {
            GameScene.show(new SettingsWindow());
        }
    }

    private void createArmor() {
        Armor armor = new ClothArmor();
        if (Challenges.isItemBlocked(armor)) {
            return;
        }
        switch (tier) {
            case 5:
                armor = new PlateArmor();
                break;
            case 4:
                armor = new ScaleArmor();
                break;
            case 3:
                armor = new MailArmor();
                break;
            case 2:
                armor = new LeatherArmor();
                break;
            default:
                armor = new ClothArmor();
        }
        armor = modifyArmor(armor);
        armor.identify();
        if (armor.collect()) {
            GLog.i(Messages.get(this, "collect_success", armor.name()));
        } else {
            armor.doDrop(curUser);
        }
    }

    private Armor modifyArmor(Armor armor) {
        if (levelToGen >= 0) armor.level(levelToGen);
        armor.cursed = cursed;
        if (generateEnchant(enchant_rarity, enchant_id) == null) {
            armor.inscribe(null);
        } else {
            armor.inscribe(Reflection.newInstance(generateEnchant(enchant_rarity, enchant_id)));
        }
        return armor;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("tier", tier);
        bundle.put("is_cursed", cursed);
        bundle.put("level_to_gen", levelToGen);
        bundle.put("enchant_rarity", enchant_rarity);
        bundle.put("enchant_id", enchant_id);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        tier = bundle.getInt("tier");
        cursed = bundle.getBoolean("is_cursed");
        levelToGen = bundle.getInt("level_to_gen");
        enchant_rarity = bundle.getInt("enchant_rarity");
        enchant_id = bundle.getInt("enchant_id");
    }

    private String currentGlyphName(Class<? extends Armor.Glyph> glyph) {
        if (enchant_rarity < 4)
            return currentGlyphName(glyph, Messages.get(Armor.Glyph.class, "glyph"));
        else
            return currentGlyphName(glyph, Messages.get(Item.class, "curse"));
    }

    private String currentGlyphName(Class<? extends Armor.Glyph> glyph, String armorName) {
        return Messages.get(glyph, "name", armorName);
    }

    private Class<? extends Armor.Glyph> generateEnchant(int enc_type, int enc_id) {
        if (enc_type == 1) switch (enc_id) {
            case 0:
                return Obfuscation.class;
            case 1:
                return Swiftness.class;
            case 2:
                return Viscosity.class;
            case 3:
                return Potential.class;
            default:
                return null;
        }
        else if (enc_type == 2) switch (enc_id) {
            case 0:
                return Stone.class;
            case 1:
                return Brimstone.class;
            case 2:
                return Entanglement.class;
            case 3:
                return Repulsion.class;
            case 4:
                return Camouflage.class;
            case 5:
                return Flow.class;
            default:
                return null;
        }
        else if (enc_type == 3) switch (enc_id) {
            case 0:
                return AntiMagic.class;
            case 1:
                return Thorns.class;
            case 2:
                return Affection.class;
            default:
                return null;
        }
        else if (enc_type == 4) switch (enc_id) {
            case 0:
                return AntiEntropy.class;
            case 1:
                return Bulk.class;
            case 2:
                return Corrosion.class;
            case 3:
                return Displacement.class;
            case 4:
                return Metabolism.class;
            case 5:
                return Multiplicity.class;
            case 6:
                return Overgrowth.class;
            case 7:
                return Stench.class;
        }

        return null;
    }

    private class SettingsWindow extends Window {
        private static final int WIDTH = 120;
        private static final int GAP = 2;
        private CheckBox c_curse;
        private RenderedTextBlock t_enchantInfo;
        private OptionSlider o_level;
        private OptionSlider o_enchantId;
        private OptionSlider o_enchantRarity;
        private OptionSlider o_tier;
        private RedButton b_create;

        public SettingsWindow() {
            super();

            o_tier = new OptionSlider(Messages.get(this, "tier"), "1", "5", 1, 5) {
                @Override
                protected void onChange() {
                    tier = getSelectedValue();
                }
            };
            o_tier.setSelectedValue(tier);
            add(o_tier);

            o_level = new OptionSlider(Messages.get(this, "level"), "0", "12", 0, 12) {
                @Override
                protected void onChange() {
                    levelToGen = getSelectedValue();
                }
            };
            o_level.setSelectedValue(levelToGen);
            add(o_level);

            t_enchantInfo = PixelScene.renderTextBlock("", 6);
            t_enchantInfo.text(infoBuilder());
            t_enchantInfo.visible = true;
            t_enchantInfo.maxWidth(WIDTH);
            add(t_enchantInfo);

            o_enchantRarity = new OptionSlider(Messages.get(this, "enchant_rarity"), "0", "4", 0, 4) {
                @Override
                protected void onChange() {
                    enchant_rarity = getSelectedValue();
                    updateText();
                }
            };
            o_enchantRarity.setSelectedValue(enchant_rarity);
            add(o_enchantRarity);

            o_enchantId = new OptionSlider(Messages.get(this, "enchant_id"), "0", "7", 0, 7) {
                @Override
                protected void onChange() {
                    enchant_id = getSelectedValue();
                    updateText();
                }
            };
            o_enchantId.setSelectedValue(enchant_id);
            add(o_enchantId);

            c_curse = new CheckBox(Messages.get(this, "curse")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    cursed = checked();
                }
            };
            c_curse.checked(cursed);
            add(c_curse);

            b_create = new RedButton(Messages.get(this, "create_button")) {
                @Override
                protected void onClick() {
                    createArmor();
                }
            };
            add(b_create);

            layout();
        }

        private void layout() {
            o_tier.setRect(0, GAP, WIDTH, 24);
            o_level.setRect(0, GAP + o_tier.top() + o_tier.height(), WIDTH, 24);
            t_enchantInfo.setPos(0, GAP + o_level.top() + o_level.height());
            o_enchantRarity.setRect(0, GAP + t_enchantInfo.bottom(), WIDTH, 24);
            o_enchantId.setRect(0, GAP + o_enchantRarity.top() + o_enchantRarity.height(), WIDTH, 24);
            c_curse.setRect(0, GAP + o_enchantId.top() + o_enchantId.height(), WIDTH / 2f - GAP / 2f, 16);
            b_create.setRect(WIDTH / 2f + GAP / 2f, o_enchantId.bottom() + GAP, WIDTH / 2f - GAP / 2f, 16);
            resize(WIDTH, (int) b_create.bottom());
        }

        private String infoBuilder() {
            //String desc = Messages.get(BossRushArmor.class, "enchant_id_pre", enchant_rarity);
            String desc = "";
            String key = "enchant_id_g" + String.valueOf(enchant_rarity);
            Class<? extends Armor.Glyph> glyph = generateEnchant(enchant_rarity, enchant_id);
            desc += Messages.get(TestArmor.class, key, (glyph == null ? Messages.get(TestArmor.class, "null_glyph") : currentGlyphName(glyph)));
            return desc;
        }

        private void updateText() {
            t_enchantInfo.text(infoBuilder());
            layout();
        }
    }
}