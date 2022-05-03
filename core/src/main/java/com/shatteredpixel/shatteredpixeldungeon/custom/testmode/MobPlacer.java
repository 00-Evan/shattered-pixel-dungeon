package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredBrute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BlackHost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Brute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdMagicRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DemonSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FetidRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FlameB01;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollTrickster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreatCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.KagenoNusujin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MolotovHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OGPDLLS;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OGPDNQHZ;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OGPDZSLS;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RedNecromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RedSwarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotHeart;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDHBLR;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShieldHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpectralNecromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.XTG100;
import com.shatteredpixel.shatteredpixeldungeon.custom.dict.DictSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class MobPlacer extends TestItem{
    {
        image = ItemSpriteSheet.CANDY_CANE;
        defaultAction = AC_PLACE;
    }

    private static final String AC_PLACE = "place";
    private static final String AC_SET = "set";

    private int mobTier = 1;
    private int mobIndex = 0;
    private int elite = 0;
    private static final int MAX_ELITE = 7;

    private final ArrayList<Class<? extends ChampionEnemy>> eliteBuffs = new ArrayList<>();
    {
        eliteBuffs.add(ChampionEnemy.Blazing.class);
        eliteBuffs.add(ChampionEnemy.AntiMagic.class);
        eliteBuffs.add(ChampionEnemy.Blessed.class);
        eliteBuffs.add(ChampionEnemy.Giant.class);
        eliteBuffs.add(ChampionEnemy.Growing.class);
        eliteBuffs.add(ChampionEnemy.Projecting.class);
        eliteBuffs.add(ChampionEnemy.Halo.class);
    };

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_PLACE);
        actions.add(AC_SET);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_PLACE)) {
            GameScene.selectCell(new CellSelector.Listener() {
                @Override
                public void onSelect(final Integer cell) {
                    if(cell != null){
                        if (canPlaceMob(cell)) {
                            try {
                                Mob m = Reflection.newInstance(allData.get(dataThreshold(mobTier) + mobIndex).mobClass);
                                m.pos = cell;
                                GameScene.add(m);
                                if(elite>0){
                                    Collections.shuffle(eliteBuffs);
                                    for(int i=0;i<elite;++i){
                                        Buff.affect(m, eliteBuffs.get(i));
                                    }
                                }
                                ScrollOfTeleportation.appear(m, cell);
                                Dungeon.level.occupyCell(m);
                            } catch (Exception e) {
                                ShatteredPixelDungeon.reportException(e);
                            }
                        }else{
                            GLog.w(M.L(MobPlacer.class, "forbidden"));
                        }
                    }
                    curUser.next();
                }
                @Override
                public String prompt() {
                    return M.L(MobPlacer.class, "prompt");
                }
            });

        } else if (action.equals(AC_SET)) {
            GameScene.show(new WndSetMob());
        }
    }

    private boolean canPlaceMob(int cell){
        return Actor.findChar(cell) == null && (!Dungeon.level.solid[cell] || Dungeon.level.map[cell] == Terrain.DOOR || Dungeon.level.map[cell] == Terrain.OPEN_DOOR);
    }


    protected int maxMobIndex(int tier){
        switch (tier){
            case 1: return DataPack.GREAT_CRAB.ordinal();
            case 2: return DataPack.NEW_FIRE_ELE.ordinal() - DataPack.GREAT_CRAB.ordinal() - 1;
            case 3: return DataPack.DM201.ordinal() - DataPack.NEW_FIRE_ELE.ordinal() - 1;
            case 4: return DataPack.ELE_CHAOS.ordinal() - DataPack.DM201.ordinal() - 1;
            case 5: return DataPack.ACIDIC.ordinal() - DataPack.ELE_CHAOS.ordinal() - 1;
            case 6: return DataPack.PIRANHA.ordinal() - DataPack.ACIDIC.ordinal() - 1;
            case 7: return DataPack.NQHZ.ordinal() - DataPack.PIRANHA.ordinal() - 1;
            case 8: default: return DataPack.XTG.ordinal() - DataPack.NQHZ.ordinal() - 1;
        }
    }
    private int dataThreshold(int tier){
        switch (tier){
            case 1: default:
                return 0;
            case 2:
                return DataPack.GREAT_CRAB.ordinal()+1;
            case 3:
                return DataPack.NEW_FIRE_ELE.ordinal()+1;
            case 4:
                return DataPack.DM201.ordinal()+1;
            case 5:
                return DataPack.ELE_CHAOS.ordinal()+1;
            case 6:
                return DataPack.ACIDIC.ordinal()+1;
            case 7:
                return DataPack.PIRANHA.ordinal()+1;
            case 8:
                return DataPack.NQHZ.ordinal()+1;
        }
    }

    @Override
    public void storeInBundle(Bundle b){
        super.storeInBundle(b);
        b.put("mobTier", mobTier);
        b.put("mobIndex", mobIndex);
        b.put("eliteTags", elite);
    }

    @Override
    public void restoreFromBundle(Bundle b){
        super.restoreFromBundle(b);
        mobTier = b.getInt("mobTier");
        mobIndex = b.getInt("mobIndex");
        elite = b.getInt("eliteTags");
    }


    private class WndSetMob extends Window{

        private static final int WIDTH = 120;
        private static final int HEIGHT = 118;
        private static final int BTN_SIZE = 18;
        private static final int GAP = 2;

        private RenderedTextBlock selectedPage;
        private ArrayList<IconButton> mobButtons = new ArrayList<>();
        private RenderedTextBlock selectedMob;

        public WndSetMob(){
            super();

            resize(WIDTH, HEIGHT);

            RedButton lhs = new RedButton("上一页", 6){
                @Override
                public void onClick(){
                    mobTier--;
                    if(mobTier < 1 || mobTier>8){
                        mobTier = 8;
                    }
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier));
                    refreshImage();
                    updateSelectedMob();
                }
            };
            lhs.setRect(GAP, GAP, 24, 18);
            add(lhs);

            RedButton rhs = new RedButton("下一页", 6){
                @Override
                public void onClick(){
                    mobTier++;
                    if(mobTier < 1 || mobTier > 8){
                        mobTier = 1;
                    }
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier));
                    refreshImage();
                    updateSelectedMob();
                }
            };
            rhs.setRect(WIDTH - 24 - GAP,  GAP, 24, 18);
            add(rhs);

            selectedPage = PixelScene.renderTextBlock("", 9);
            PixelScene.align(selectedPage);
            add(selectedPage);

            selectedMob = PixelScene.renderTextBlock("", 9);
            selectedMob.hardlight(0xFFFF44);
            PixelScene.align(selectedMob);
            add(selectedMob);

            OptionSlider op = new OptionSlider
                    (M.L(MobPlacer.class, "elite"), "0", String.valueOf(MAX_ELITE), 0, MAX_ELITE) {
                @Override
                protected void onChange() {
                    elite = getSelectedValue();
                }
            };
            op.setRect(GAP, 92, WIDTH - 2*GAP, 24);
            op.setSelectedValue(elite);
            add(op);

            createMobImage();

            updateSelectedMob();
            layout();
        }

        private void updateSelectedMob(){
            int selected = mobTier;
            StringBuilder sb = new StringBuilder();
            for(int i=1;i<=8;++i){
                sb.append((i==selected? "_*_ ":"* "));
            }
            selectedPage.text(sb.toString());
            selectedPage.maxWidth(WIDTH / 2);
            selectedPage.setPos((WIDTH - selectedPage.width())/2, 5);
            updateMobText();
        }

        private void updateMobText(){
            selectedMob.text( M.L(allData.get(dataThreshold(mobTier) + mobIndex).mobClass, "name") );
        }

        private void layout(){
            selectedPage.maxWidth(WIDTH / 2);
            selectedPage.setPos((WIDTH - selectedPage.width())/2, 5);
            selectedMob.maxWidth(WIDTH);
            selectedMob.setPos((WIDTH - selectedMob.width())/2, 80);
        }

        private void createMobImage() {
            int maxNum = maxMobIndex(mobTier) + 1;
            //(N+1)/2
            int firstLine = (maxNum >> 1) + (maxNum & 1);
            float left1 = (WIDTH - (GAP + BTN_SIZE) * firstLine + GAP)/2f;
            float left2 = (WIDTH - (GAP + BTN_SIZE) * (maxNum - firstLine) + GAP)/2f;
            for (int i = 0; i < maxNum; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    public void onClick() {
                        super.onClick();
                        mobIndex = j;
                        updateMobText();
                    }
                };
                btn.icon( DictSpriteSheet.miscImages(allData.get(dataThreshold(mobTier)+i).imageId) );
                float max = Math.max(btn.icon().width(), btn.icon().height());
                btn.icon().scale = new PointF(BTN_SIZE/max, BTN_SIZE/max);
                if(i<firstLine){
                    btn.setRect(left1, 30f, BTN_SIZE, BTN_SIZE );
                    left1 += GAP + BTN_SIZE;
                }else{
                    btn.setRect(left2, 56f, BTN_SIZE, BTN_SIZE);
                    left2 += GAP + BTN_SIZE;
                }
                add(btn);
                mobButtons.add(btn);
            }
        }

        private void clearImage(){
            for(int i=0, len = mobButtons.size();i<len;++i){
                mobButtons.get(i).destroy();
            }
        }

        private void refreshImage(){
            clearImage();
            createMobImage();
        }
    }


    //packed with a linkedHashmap to find class by ordinal at O(1);
    private static LinkedHashMap<Integer, DataPack> allData = new LinkedHashMap<>();
    static {
        for(DataPack dp : DataPack.values()){
            allData.put(dp.ordinal(), dp);
        }
    }

    private enum DataPack{
        RAT(Rat.class, DictSpriteSheet.RAT),
        GNOLL(Gnoll.class, DictSpriteSheet.GNOLL),
        SNAKE(Snake.class, DictSpriteSheet.SNAKE),
        ALBINO(Albino.class, DictSpriteSheet.ALBINO),
        CRAB(Crab.class, DictSpriteSheet.CRAB),
        SWARM(Swarm.class, DictSpriteSheet.SWARM),
        SLIME(Slime.class, DictSpriteSheet.SLIME),
        F_RAT(FetidRat.class, DictSpriteSheet.F_RAT),
        GNOLL_DARTER(GnollTrickster.class, DictSpriteSheet.GNOLL_DARTER),
        GREAT_CRAB(GreatCrab.class, DictSpriteSheet.GREAT_CRAB),

        SKELETON(Skeleton.class, DictSpriteSheet.SKELETON),
        THIEF(Thief.class, DictSpriteSheet.THIEF),
        BANDIT(Bandit.class, DictSpriteSheet.BANDIT),
        DM100(DM100.class, DictSpriteSheet.DM100),
        GUARD(Guard.class, DictSpriteSheet.GUARD),
        NECRO(Necromancer.class, DictSpriteSheet.NECROMANCER),
        ROT_LASHER(RotLasher.class, DictSpriteSheet.ROT_LASHER),
        ROT_HEART(RotHeart.class, DictSpriteSheet.ROT_HEART),
        NEW_FIRE_ELE(Elemental.NewbornFireElemental.class, DictSpriteSheet.NEW_FIRE_ELE),

        BAT(Bat.class, DictSpriteSheet.BAT),
        BRUTE(Brute.class, DictSpriteSheet.BRUTE),
        ARMORED_BRUTE(ArmoredBrute.class, DictSpriteSheet.ARMORED_BRUTE),
        SHAMAN(Shaman.random(), DictSpriteSheet.SHAMAN),
        SPINNER(Spinner.class, DictSpriteSheet.SPINNER),
        DM200(DM200.class, DictSpriteSheet.DM200),
        DM201(DM201.class, DictSpriteSheet.DM201),

        GHOUL(Ghoul.class, DictSpriteSheet.GHOUL),
        WARLOCK(Warlock.class, DictSpriteSheet.WARLOCK),
        MONK(Monk.class, DictSpriteSheet.MONK),
        SENIOR(Senior.class, DictSpriteSheet.SENIOR),
        GOLEM(Golem.class, DictSpriteSheet.GOLEM),
        ELE_FIRE(Elemental.FireElemental.class, DictSpriteSheet.ELEMENTAL_FIRE),
        ELE_FROST(Elemental.FrostElemental.class, DictSpriteSheet.ELEMENTAL_FROST),
        ELE_LIGHTNING(Elemental.ShockElemental.class, DictSpriteSheet.ELEMENTAL_SHOCK),
        ELE_CHAOS(Elemental.ChaosElemental.class, DictSpriteSheet.ELEMENTAL_CHAOS),

        RIPPER(RipperDemon.class, DictSpriteSheet.RIPPER),
        SPAWNER(DemonSpawner.class, DictSpriteSheet.SPAWNER),
        EYE(Eye.class, DictSpriteSheet.EYE),
        SUCCUBUS(Succubus.class, DictSpriteSheet.SUCCUBUS),
        SCORPIO(Scorpio.class, DictSpriteSheet.SCORPIO),
        ACIDIC(Acidic.class, DictSpriteSheet.AICDIC),

        STATUE(Statue.class, DictSpriteSheet.STATUE),
        ARMORED_STATUE(ArmoredStatue.class, DictSpriteSheet.ARMORED_STATUE),
        WRAITH(Wraith.class, DictSpriteSheet.WRAITH),
        PIRANHA(Piranha.class, DictSpriteSheet.FISH),

        ZSLS(OGPDZSLS.class, DictSpriteSheet.OGPDZSLS),
        LLS(OGPDLLS.class, DictSpriteSheet.OGPDLLS),
        COLD(ColdMagicRat.class, DictSpriteSheet.COLD),
        RED(RedSwarm.class, DictSpriteSheet.RED),
        SHOW(KagenoNusujin.class, DictSpriteSheet.SHOW),
        BLACK(BlackHost.class, DictSpriteSheet.BLACK),
        HBLR(SRPDHBLR.class, DictSpriteSheet.HBLR),
        MOlO(MolotovHuntsman.class, DictSpriteSheet.MOLO),
        Flame(FlameB01.class, DictSpriteSheet.FLAME),
        NQHZ(OGPDNQHZ.class, DictSpriteSheet.OGPDNQHZ),

        REDBS(RedNecromancer.class, DictSpriteSheet.REDBS),
        SLXJ(SpectralNecromancer.class, DictSpriteSheet.SLXJ),
        BMHR(ShieldHuntsman.class, DictSpriteSheet.BMHR),
        XTG(XTG100.class, DictSpriteSheet.XTG);

        private Class<? extends Mob> mobClass;
        private int imageId;

        DataPack(Class<? extends Mob> cls, int image){
            this.imageId = image;
            this.mobClass = cls;
        }

        public int getImageId(){return imageId;}
        public Class<? extends Mob> getMobClass(){return mobClass;}
    }
}
