package com.shatteredpixel.shatteredpixeldungeon.items.devtools.generator;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.TestGenerator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
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

public class TestArtifact extends TestGenerator {
    {
        image = ItemSpriteSheet.ARTIFACT_HOLDER;
    }
    private int level;
    private int selected;
    private boolean cursed;
    @Override
    public ArrayList<String> actions(Hero hero) {
        return super.actions(hero);
    }
    @Override
    public void execute(Hero hero, String action ) {
        super.execute( hero, action );
        if(action.equals(AC_GIVE)){
            GameScene.show(new SettingsWindow());
        }
    }

    private void modifyArtifact(Artifact a){
        int max = Math.min(level, maxLevel(selected));
        for(int i=0;i<max; ++i){
            a.upgrade();
        }
        a.cursed = cursed;
    }
    private void createArtifact(){
        Artifact a = Reflection.newInstance(idToArtifact(selected));
        if(a != null){
            modifyArtifact(a);
            if(Challenges.isItemBlocked(a)) return;
            a.identify();
            if(a.collect()){
                GLog.i(Messages.get(this, "collect_success", a.name()));
            }else{
                a.doDrop(curUser);
            }
        }
    }
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("selected", selected);
        bundle.put("is_cursed", cursed);
        bundle.put("level", level);
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        selected = bundle.getInt("selected");
        cursed = bundle.getBoolean("is_cursed");
        level = bundle.getInt("level");
    }

    private Class<? extends Artifact> idToArtifact(int sel){
        switch(sel){
            case 0: return AlchemistsToolkit.class;
            case 1: return CapeOfThorns.class;
            case 2: return ChaliceOfBlood.class;
            case 3: return CloakOfShadows.class;
            case 4: return DriedRose.class;
            case 5: return EtherealChains.class;
            case 6: return HornOfPlenty.class;
            case 7: return LloydsBeacon.class;
            case 8: return MasterThievesArmband.class;
            case 9: return SandalsOfNature.class;
            case 10: return TalismanOfForesight.class;
            case 11: return TimekeepersHourglass.class;
            case 12: default: return UnstableSpellbook.class;
        }
    }

    private int maxLevel(int id){
        switch (id){
            case 0: case 1: case 2: case 3: case 4: case 6: case 8: case 10: case 12: default: return 10;
            case 5: case 11: return 5;
            case 7: case 9: return 3;
        }
    }

    private static ArrayList<Class<? extends Artifact>> artifactList = new ArrayList<Class<? extends Artifact>>();
    private void buildArtifactArray(){
        if(!artifactList.isEmpty()) return;
        for(int i=0;i<13;++i){
            artifactList.add(idToArtifact(i));
        }
    }



    private class SettingsWindow extends Window {
        private static final int WIDTH = 120;
        private static final int BTN_SIZE = 17;
        private static final int GAP = 2;
        private RenderedTextBlock t_selected;
        private OptionSlider o_level;
        private CheckBox c_curse;
        private RedButton b_create;
        private ArrayList<IconButton> artifactSprites = new ArrayList<>();

        public SettingsWindow(){
            buildArtifactArray();
            createArtifactImage();
            t_selected = PixelScene.renderTextBlock("", 6);
            t_selected.text();
            add((t_selected));

            o_level = new OptionSlider(Messages.get(this, "level"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    level = getSelectedValue();
                }
            };
            o_level.setSelectedValue(level);
            add(o_level);

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
                    createArtifact();
                }
            };
            add(b_create);

            updateText();
        }

        private void layout(){
            t_selected.setPos(0, 3*GAP + BTN_SIZE *2);
            o_level.setRect(0, t_selected.bottom() + GAP, WIDTH, 24);
            c_curse.setRect(0, o_level.bottom() + GAP, WIDTH, 18);
            b_create.setRect(0, c_curse.bottom() + GAP, WIDTH, 16);
            resize(WIDTH, (int)b_create.bottom() + GAP);
        }

        private void createArtifactImage(){
            float left;
            float top = GAP;
            int placed = 0;
            int length = artifactList.size();
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        selected = j;
                        updateText();
                        super.onClick();
                    }
                };
                Image im = new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(artifactList.get(i))).image));
                im.scale.set(1f);
                btn.icon(im);
                if(i<7) {
                    left = (WIDTH - BTN_SIZE * 7) / 2f;
                    btn.setRect(left + placed * BTN_SIZE, top, BTN_SIZE, BTN_SIZE);
                }
                else {
                    left = (WIDTH - BTN_SIZE * 6) / 2f;
                    btn.setRect(left + (placed-7) * BTN_SIZE, top + GAP + BTN_SIZE, BTN_SIZE, BTN_SIZE);
                }
                add(btn);
                placed++;
                artifactSprites.add(btn);
            }
        }

        private void updateText(){
            t_selected.text(Messages.get(TestArtifact.class, "selected", Messages.get(idToArtifact(selected), "name")));
            layout();
        }
    }
}
