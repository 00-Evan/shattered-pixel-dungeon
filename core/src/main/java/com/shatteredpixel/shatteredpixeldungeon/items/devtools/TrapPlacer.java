package com.shatteredpixel.shatteredpixeldungeon.items.devtools;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

public class TrapPlacer extends TestItem {
    {
        image = ItemSpriteSheet.RECLAIM_TRAP;
        defaultAction = AC_PLACE;
    }

    private static final String AC_PLACE = "place";
    private static final String AC_SET = "set";
    //used for trap selection.
    //Currently there are 7 rows and 8 cols of trap sprites
    //and none shares the same sprite.
    //the 9th column are inactive traps.
    private int row = 0;
    private int column = 0;
    protected Trap trap = null;
    private boolean triggerWhenPut = false;
    private boolean enableInvalidImage = false;
    //encoding for traps, column = color, row = shape;
    /*
    //trap colors
    public static final int RED     = 0;
    public static final int ORANGE  = 1;
    public static final int YELLOW  = 2;
    public static final int GREEN   = 3;
    public static final int TEAL    = 4;
    public static final int VIOLET  = 5;
    public static final int WHITE   = 6;
    public static final int GREY    = 7;
    public static final int BLACK   = 8;

    //trap shapes
    public static final int DOTS        = 0;
    public static final int WAVES       = 1;
    public static final int GRILL       = 2;
    public static final int STARS       = 3;
    public static final int DIAMOND     = 4;
    public static final int CROSSHAIR   = 5;
    public static final int LARGE_DOT   = 6;
     */

    //index = row * COLS + column, where COLS = 8;
    private static HashMap<Integer, Class<? extends Trap>> trapLib = new HashMap<>(40);
    static {
        trapLib.put(0, AlarmTrap.class);
        trapLib.put(48, DisarmingTrap.class);
        trapLib.put(24, GuardianTrap.class);
        trapLib.put(32, PitfallTrap.class);
        trapLib.put(25, BlazingTrap.class);
        trapLib.put(1, BurningTrap.class);
        trapLib.put(33, ExplosiveTrap.class);
        trapLib.put(2, ShockingTrap.class);
        trapLib.put(26, StormTrap.class);
        trapLib.put(3, OozeTrap.class);
        trapLib.put(43, PoisonDartTrap.class);
        trapLib.put(19, ToxicTrap.class);
        trapLib.put(11, WeakeningTrap.class);
        trapLib.put(20, ConfusionTrap.class);
        trapLib.put(52, DistortionTrap.class);
        trapLib.put(12, SummoningTrap.class);
        trapLib.put(4, TeleportationTrap.class);
        trapLib.put(28, WarpingTrap.class);
        trapLib.put(13, CursingTrap.class);
        trapLib.put(45, DisintegrationTrap.class);
        trapLib.put(6, ChillingTrap.class);
        trapLib.put(14, FlockTrap.class);
        trapLib.put(30, FrostTrap.class);
        trapLib.put(23, CorrosionTrap.class);
        trapLib.put(31, FlashingTrap.class);
        trapLib.put(55, GrimTrap.class);
        trapLib.put(7, GrippingTrap.class);
        trapLib.put(39, RockfallTrap.class);
        trapLib.put(47, WornDartTrap.class);
        trapLib.put(36, GeyserTrap.class);
        trapLib.put(44, GatewayTrap.class);
    }

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
            if (trap == null) {
                GLog.w(M.L(this, "null_trap"));
                return;
            }
            GameScene.selectCell(new CellSelector.Listener() {
                @Override
                public void onSelect(final Integer cell) {
                    if (cell != null) {
                        ((TrapPlacer) curItem).setTrap(cell);
                    }
                    curUser.next();
                }
                @Override
                public String prompt() {
                    return M.L(TrapPlacer.class, "prompt");
                }
            });

        } else if (action.equals(AC_SET)) {
            GameScene.show(new SettingsWindow());
        }
    }

    public void setTrap(Integer cell) {
        if (trap == null) {
            GLog.w(M.L(this, "null_trap"));
            return;
        }

        if (!canPlaceTrap(cell)) {
            {
                GLog.w(M.L(this, "invalid_tile"));
                return;
            }
        }

        Trap trapToCreate = trap;
        //create a new instance for trap
        Class<? extends Trap> tc = trapLib.get(index());
        if(tc == null){
            trap = new EmptyTrap();
            trap.color = enableInvalidImage ? color() : Trap.BLACK;
            trap.shape = shape();
            trapToCreate = trap;
        }else{
            trap = Reflection.newInstance(tc);
        }
        Dungeon.level.setTrap(trapToCreate.reveal(), cell);
        //logic for deciding if triggering trap
        //Dungeon.level.map[cell] = Terrain.TRAP;
        Level.set(cell, Terrain.TRAP, Dungeon.level);
        GameScene.updateMap(cell);
        //avoid new traps
        //Dungeon.level.avoid[cell] = true;
        //Dungeon.level.passable[cell] = false;

        if(triggerWhenPut) trapToCreate.trigger();
    }

    private boolean canPlaceTrap(int cell) {

        int x = Dungeon.level.map[cell];

        if (x == Terrain.EMPTY || x == Terrain.GRASS || x == Terrain.DOOR || x == Terrain.OPEN_DOOR || x == Terrain.EMBERS || x == Terrain.PEDESTAL || x == Terrain.EMPTY_SP ||
                x == Terrain.HIGH_GRASS || x == Terrain.FURROWED_GRASS || x == Terrain.TRAP || x == Terrain.INACTIVE_TRAP || x == Terrain.EMPTY_DECO || x == Terrain.WATER) {
            if (Dungeon.level.heroFOV[cell]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("row", row);
        bundle.put("column", column);
        bundle.put("trigger", triggerWhenPut);
        bundle.put("enableInvalid", enableInvalidImage);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        row = bundle.getInt("row");
        column = bundle.getInt("column");
        triggerWhenPut = bundle.getBoolean("trigger");
        enableInvalidImage = bundle.getBoolean("enableInvalid");
        trap = Reflection.newInstance(trapLib.get(index()));
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", (trap == null ? Messages.get(TrapPlacer.class, "no_trap_selected") : Messages.get(trap.getClass(), "name")));
    }

    private int index(){
        return row * 8 + column;
    }

    private int color(){ return column; }
    private int shape(){ return row; }

    private class SettingsWindow extends Window{
        private static final int WIDTH = 120;
        private static final int HEIGHT = 144;

        private static final int BTN_SIZE = 14;

        private static final int ROWS = 7;
        private static final int COLS = 8;

        private ArrayList<IconButton> trapButtons = new ArrayList<>();
        private RenderedTextBlock selected;



        public SettingsWindow(){
            super();

            resize(WIDTH, HEIGHT);

            RenderedTextBlock ttl = PixelScene.renderTextBlock(M.L(SettingsWindow.class, "title"), 9);
            PixelScene.align(ttl);
            add(ttl);
            ttl.setPos(1, 1);
            ttl.hardlight(0x44A8E4);

            CheckBox enableInvalid = new CheckBox(M.L(TrapPlacer.class, "invalid_trap_display")){
                @Override
                protected void onClick() {
                    super.onClick();
                    enableInvalidImage = checked();
                    refreshImage();
                }
            };
            enableInvalid.checked(enableInvalidImage);
            add(enableInvalid);
            enableInvalid.setRect(2, ttl.bottom() + 2, WIDTH - 4, 16);

            float left = (WIDTH - BTN_SIZE*COLS)/2f;
            float top = enableInvalid.bottom() + 4f;
            for(int i=0; i<ROWS; ++i){
                for(int j=0;j<COLS;++j){
                    final int index = i*COLS + j;
                    IconButton btn = new IconButton(){
                        @Override
                        public void onClick(){
                            trapButtons.get(index()).icon().resetColor();
                            row = index / COLS;
                            column = index % COLS;
                            trapButtons.get(index).icon().color(0xFFFF44);
                            updateText();
                        }
                    };
                    btn.setRect(left + BTN_SIZE * j, top + BTN_SIZE * i, BTN_SIZE, BTN_SIZE);
                    add(btn);
                    trapButtons.add(btn);
                }
            }
            refreshImage();
            trapButtons.get(row * COLS + column).icon().color(0xFFFF44);

            selected = PixelScene.renderTextBlock("", 6);
            PixelScene.align(selected);
            add(selected);

            CheckBox cb = new CheckBox(M.L(TrapPlacer.class, "trigger")){
                @Override
                protected void onClick(){
                    super.onClick();
                    triggerWhenPut = checked();
                }
            };
            cb.checked(triggerWhenPut);
            cb.setRect(1, top + 7 * BTN_SIZE + 4, WIDTH/2f, 16);
            add(cb);

            selected.setPos(WIDTH/2f + 6, top + 7 * BTN_SIZE + 4 + (cb.height()-7f)/2f);

            updateText();

            resize(WIDTH, (int) (cb.bottom() + 2));
        }

        private void updateText(){
            Class<? extends Trap> cls = trapLib.get(index());
            if(cls == null){
                trap = new EmptyTrap();
                trap.color = enableInvalidImage ? color() : Trap.BLACK;
                trap.shape = shape();
            }else {
                trap = Reflection.newInstance(cls);
            }
            selected.text(statDesc());
        }

        private String statDesc() {
            if(trap == null) return M.L(this, "null_trap");
            return M.L(trap.getClass(), "name");
        }

        private void refreshImage(){

            for(int i = 0, size = trapButtons.size(); i < size; ++i){
                if(trapLib.get(i)==null && !enableInvalidImage){
                    trapButtons.get(i).icon(new Image(Assets.Environment.TERRAIN_FEATURES, 128, 16*(i/COLS), 16, 16));
                }else{
                    trapButtons.get(i).icon(new Image(Assets.Environment.TERRAIN_FEATURES, 16*(i%COLS), 16*(i/COLS), 16, 16));;
                }
            }
        }
    }

    public static class EmptyTrap extends Trap{
        @Override
        public void activate() {

        }

        {
            active = true;
            canBeHidden = false;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put("trap_shape_val", shape);
            bundle.put("trap_color_val", color);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            shape = bundle.getInt("trap_shape_val");
            color = bundle.getInt("trap_color_val");
        }
    }
}