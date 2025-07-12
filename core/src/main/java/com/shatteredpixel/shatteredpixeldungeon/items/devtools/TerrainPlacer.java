package com.shatteredpixel.shatteredpixeldungeon.items.devtools;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.messages.M;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.Arrays;

public class TerrainPlacer extends TestItem {
    {
        image = ItemSpriteSheet.PICKAXE;
        defaultAction = AC_PLACE;
    }

    public int chosen;
    public RenderedTextBlock name;
    private static final String AC_PLACE = "place";
    private static final String AC_SET = "set";

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
                    if (chosen == 17 || chosen == 18 || chosen == 19) {
//                        GLog.i("不能放置此类型的地形");
                    } else if (cell != null) {
                        Level.set(cell,chosen);
                        GameScene.updateMap(cell);
                    }
                    curUser.next();
                }
                @Override
                public String prompt() {
                    return M.L(TerrainPlacer.class,"prompt");
                }
            });
        }

        if (action.equals(AC_SET)) {
            GameScene.show(new SettingsWindow());
        }
    }

//    @Override
//    public void storeInBundle(Bundle bundle) {
//        super.storeInBundle(bundle);
//        bundle.put("chosen", chosen);
//    }
//
//    @Override
//    public void restoreFromBundle(Bundle bundle) {
//        super.restoreFromBundle(bundle);
//        chosen = bundle.getInt("chosen");
//    }

    private class SettingsWindow extends Window {

        public SettingsWindow() {
            TerrainButton terrainButton;
            int x;
            int y;

            for (int i = 0;i < 32;i++) {
                terrainButton = new TerrainButton(i);
                x = i % 6 * 18 + 2;
                y = i / 6 * 18 + 2;
                terrainButton.setRect(x,y,16,16);
                add(terrainButton);
            }

            name = PixelScene.renderTextBlock("深渊",9);
            name.setPos((110 - name.width()) / 2,112);
            add(name);

            resize(110,114 + (int)name.height());
        }
    }

    private class TerrainButton extends IconButton {

        public int terrain = -1;

        public TerrainButton(int terrain) {
            this.terrain = terrain;
            switch (terrain) {
                case 17:
                case 18:
                case 19:
                case 23:
                    break;
                case 29:
                    icon(new Image(Dungeon.level.waterTex(),0, 0, DungeonTilemap.SIZE, DungeonTilemap.SIZE));
                    break;
                default:
                    int x = X.get(terrain) % 16;
                    int y = X.get(terrain) / 16;
                    icon(new Image(Dungeon.level.tilesTex(),x * 16, y * 16, DungeonTilemap.SIZE, DungeonTilemap.SIZE));
                    break;
            }
        }

        @Override
        public void onClick() {
            chosen = terrain;
            switch (terrain) {
                case 16:
                    name.text("隐藏门");
                    break;
                case 17:
                    name.text("隐藏陷阱");
                    break;
                case 18:
                    name.text("陷阱");
                    break;
                case 19:
                    name.text("已触发陷阱");
                    break;
                case 23:
                    name.text("被移除的告示牌");
                    break;
                default:
                    name.text(Dungeon.level.tileName(terrain));
                    break;
            }
            name.setPos((110 - name.width()) / 2,112);
        }
    }

    private final ArrayList<Integer> X = new ArrayList<>(Arrays.asList(DungeonTileSheet.CHASM,DungeonTileSheet.FLOOR,DungeonTileSheet.GRASS,DungeonTileSheet.EMPTY_WELL,
            DungeonTileSheet.FLAT_WALL,DungeonTileSheet.FLAT_DOOR,DungeonTileSheet.FLAT_DOOR_OPEN,DungeonTileSheet.ENTRANCE,DungeonTileSheet.EXIT,
            DungeonTileSheet.EMBERS,DungeonTileSheet.FLAT_DOOR_LOCKED,DungeonTileSheet.PEDESTAL,DungeonTileSheet.FLAT_WALL_DECO,DungeonTileSheet.FLAT_BARRICADE,
            DungeonTileSheet.FLOOR_SP,DungeonTileSheet.FLAT_HIGH_GRASS,DungeonTileSheet.FLAT_DOOR,-1,-1,-1,DungeonTileSheet.FLOOR_DECO,DungeonTileSheet.LOCKED_EXIT,
            DungeonTileSheet.UNLOCKED_EXIT,-1,DungeonTileSheet.WELL,DungeonTileSheet.FLAT_STATUE,DungeonTileSheet.FLAT_STATUE_SP,
            DungeonTileSheet.FLAT_BOOKSHELF,DungeonTileSheet.FLAT_ALCHEMY_POT,DungeonTileSheet.WATER,DungeonTileSheet.FLAT_FURROWED_GRASS,DungeonTileSheet.FLAT_DOOR_CRYSTAL));
}
