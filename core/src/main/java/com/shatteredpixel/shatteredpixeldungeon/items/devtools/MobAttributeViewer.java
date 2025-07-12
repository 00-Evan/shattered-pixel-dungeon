package com.shatteredpixel.shatteredpixeldungeon.items.devtools;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class MobAttributeViewer extends TestItem{
    {
        image = ItemSpriteSheet.WAND_MAGIC_MISSILE;
        defaultAction = AC_INSPECT;
    }

    private static final String AC_INSPECT = "inspect";

    private CellSelector.Listener selector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if(cell == null) return;
            Char ch = Actor.findChar(cell);
            if(ch == null){
                GLog.w(M.L(MobAttributeViewer.class, "no_char"));
            }else{
                String desc = "";
                if(ch instanceof Mob){
                    desc += ((Mob) ch).description();
                }
                desc += "\n\n";
                desc += M.L(MobAttributeViewer.class, "health", ch.HP, ch.HT);
                desc += "\n";

                int tries = 500;

                int[] damage = new int[tries];
                for (int i = 0; i < tries; ++i) {
                    damage[i] = ch.damageRoll();
                }
                float variance = 0f;
                float average = 0f;
                for (int i = 0; i < tries; ++i) {
                    average += damage[i];
                }
                average /= tries;
                for (int i = 0; i < tries; ++i) {
                    variance += (damage[i] - average) * (damage[i] - average);
                }
                variance = (float) Math.sqrt(variance/tries);
                desc += M.L(MobAttributeViewer.class, "damage", average, variance);
                desc += "\n";

                int[] defense = new int[tries];
                for (int i = 0; i < tries; ++i) {
                    defense[i] = ch.drRoll();
                }
                variance = 0f;
                average = 0f;
                for (int i = 0; i < tries; ++i) {
                    average += defense[i];
                }
                average /= tries;
                for (int i = 0; i < tries; ++i) {
                    variance += (defense[i] - average) * (defense[i] - average);
                }
                variance = (float) Math.sqrt(variance/tries);
                desc += M.L(MobAttributeViewer.class, "defense", average, variance);
                desc += "\n";

                try {
                    desc += M.L(MobAttributeViewer.class, "accuracy", ch.attackSkill(null));
                    desc += "\n";
                }catch (NullPointerException ignored){

                }
                try {
                    desc += M.L(MobAttributeViewer.class, "evasion", ch.defenseSkill(null));
                    desc += "\n";
                }catch (NullPointerException ignored){

                }
                desc += M.L(MobAttributeViewer.class, "move_speed", ch.speed());
                desc += "\n";
                if (ch instanceof Mob) {
                    desc += M.L(MobAttributeViewer.class, "attack_delay", ((Mob) ch).attackDelay());
                    desc += "\n";
                }else if(ch instanceof Hero){
                    desc += M.L(MobAttributeViewer.class, "attack_delay", ((Hero) ch).attackDelay());
                    desc += "\n";
                }
                desc += M.L(MobAttributeViewer.class, "view_distance", ch.viewDistance);
                desc += "\n";
                if (ch instanceof Mob) {
                    desc += M.L(MobAttributeViewer.class, "exp", ((Mob) ch).EXP, ((Mob) ch).maxLvl);
                    desc += "\n";
                }
                GameScene.show(new WndMobInfo(ch, desc));
            }
        }

        @Override
        public String prompt() {
            return M.L(MobAttributeViewer.class, "select");
        }
    };

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_INSPECT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if(action.equals(AC_INSPECT)){
            GameScene.selectCell(selector);
        }
    }

    public static class WndMobInfo extends WndTitledMessage {
        public WndMobInfo(){
            super(new CharTitle(new Rat()), null);
        }

        public WndMobInfo(Char ch, String message){
            super(new CharTitle(ch), message);
        }
    }

    public static class CharTitle extends Component {

        private static final int GAP = 2;

        private RenderedTextBlock name;
        private HealthBar health;
        private BuffIndicator buffs;

        public CharTitle(Char ch) {

            name = PixelScene.renderTextBlock(Messages.titleCase(ch.name()), 9);
            name.hardlight(0xFFFF00);
            add(name);

            health = new HealthBar();
            health.level(ch);
            add(health);

            buffs = new BuffIndicator(ch, false);
            add(buffs);
        }

        @Override
        protected void layout() {

            name.maxWidth((int) width - GAP*2);
            name.setPos( GAP, GAP);

            health.setRect( GAP, name.bottom() + GAP, width - GAP * 2, health.height());

            buffs.setPos(
                name.right() + GAP - 1,
                name.bottom() - BuffIndicator.SIZE_SMALL - 2
            );

            height = health.bottom();
        }
    }
}
