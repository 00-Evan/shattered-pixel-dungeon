package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogReal;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

import java.util.HashMap;

public class YogGodHardBossLevel extends Level {
    {
        color1 = 0x801500;
        color2 = 0xa68521;

        viewDistance = Math.min(4, viewDistance);
    }

    private static final int WIDTH = 41;
    private static final int HEIGHT = 41;

    public static final int CENTER = 20 * WIDTH + 20;
    private static final int EXIT = 3 * WIDTH + 20;
    private static final int ENTRANCE = 37 * WIDTH + 20;

    //portals. (from, to).

    //portals before fight ends
    private static final HashMap<Integer, Integer> MAIN_PORTAL = new HashMap<>(4);
    {
        MAIN_PORTAL.put(9+9*WIDTH, 12+28*WIDTH);
        MAIN_PORTAL.put(9+31*WIDTH, 28+28*WIDTH);
        MAIN_PORTAL.put(31+31*WIDTH, 28+12*WIDTH);
        MAIN_PORTAL.put(31+9*WIDTH, 12+12*WIDTH);
        MAIN_PORTAL.put(15+15*WIDTH, 32+20*WIDTH);
        MAIN_PORTAL.put(15+25*WIDTH, 20+8*WIDTH);
        MAIN_PORTAL.put(25+25*WIDTH, 8+20*WIDTH);
        MAIN_PORTAL.put(25+15*WIDTH, 20+32*WIDTH);
    }
    //portals after fight
    private static final HashMap<Integer, Integer> IF_MAIN_PORTAL = new HashMap<>(4);
    {
        IF_MAIN_PORTAL.put(9+9*WIDTH, 35+35*WIDTH);
        IF_MAIN_PORTAL.put(9+31*WIDTH, 35+5*WIDTH);
        IF_MAIN_PORTAL.put(31+31*WIDTH, 5+5*WIDTH);
        IF_MAIN_PORTAL.put(31+9*WIDTH, 5+35*WIDTH);
        //unchanged
        IF_MAIN_PORTAL.put(15+15*WIDTH, 32+20*WIDTH);
        IF_MAIN_PORTAL.put(15+25*WIDTH, 20+8*WIDTH);
        IF_MAIN_PORTAL.put(25+25*WIDTH, 8+20*WIDTH);
        IF_MAIN_PORTAL.put(25+15*WIDTH, 20+32*WIDTH);
    }
    //sub portals
    private static final HashMap<Integer, Integer> SUB_PORTAL = new HashMap<>(4);
    {
        SUB_PORTAL.put(34*WIDTH+34, 12*WIDTH + 28);
        SUB_PORTAL.put(6*WIDTH+34, 12*WIDTH+12);
        SUB_PORTAL.put( 6*WIDTH+6, 28*WIDTH+12);
        SUB_PORTAL.put( 34*WIDTH+6, 28*WIDTH+28);
    }

    public static final int[] summonPedestal = new int[]{12*WIDTH + 12, 12*WIDTH+28, 28*WIDTH+12, 28*WIDTH+28};

    /*
    public static final int[] destroy_a
            = new int[]{15+WIDTH*16, 16+WIDTH*15, 24+WIDTH*25, 25+WIDTH*24, 24+WIDTH*15, 25+WIDTH*16, 15+WIDTH*24, 16+WIDTH*25};
    public static final int[] destroy_b
            = new int[]{14+WIDTH*18, 14+WIDTH*22, 26+WIDTH*18, 26+WIDTH*22, 18+WIDTH*14, 22+WIDTH*14, 18+WIDTH*26, 22+WIDTH*26};
    public static final int[] destroy_c
            = new int[]{8+WIDTH*18, 8+WIDTH*22, 32+WIDTH*18, 32+WIDTH*22, 18+WIDTH*8, 18+WIDTH*32, 22+WIDTH*8, 22*WIDTH+32,};
*/

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HALLS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        for(int i= 0; i< HEIGHT * WIDTH; ++i){
            map[i] = codeToTerrain(codedMap[i]);
        }

        entrance=ENTRANCE;
        exit=EXIT;

        return true;
    }

    @Override
    public void create(){
        super.create();

        for(int well: MAIN_PORTAL.keySet()){
            avoid[well] = true;
            passable[well] = false;
        }

        for(int well: SUB_PORTAL.keySet()){
            avoid[well] = true;
            passable[well] = false;
        }
    }

    @Override
    protected void createMobs() {

    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Item item = Bones.get();
        if (item != null) {
            int pos;
            do {
                pos = randomRespawnCell(null);
            } while (pos % WIDTH == 20);
            drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
        }

        int[] plates = new int[]{4+WIDTH*4, 4+WIDTH*36, 36+WIDTH*36, 36+WIDTH*4};
        for(int i =0; i<4;++i){
            int prev = plates[i];
            int rand = Random.Int(4);
            plates[i] = plates[rand];
            plates[rand] = prev;
        }

        drop(new BrokenSeal(), plates[0]).type = Heap.Type.LOCKED_CHEST;
        drop(new MagesStaff(new WandOfMagicMissile()), plates[1]).type = Heap.Type.LOCKED_CHEST;
        drop(new CloakOfShadows(), plates[2]).type = Heap.Type.LOCKED_CHEST;
        drop(new SpiritBow(), plates[3]).type = Heap.Type.LOCKED_CHEST;

    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell( ch );

        if (map[entrance] == Terrain.ENTRANCE && map[exit] != Terrain.EXIT
                && ch == Dungeon.hero && Dungeon.level.distance(ch.pos, entrance) >= 2) {
            seal();
        }

        if(ch == Dungeon.hero){
            if(MAIN_PORTAL.containsKey(ch.pos)) {
                if (ch.buff(LockedFloor.class) != null) {
                    ScrollOfTeleportation.appear(ch, MAIN_PORTAL.get(ch.pos));
                }else{
                    ScrollOfTeleportation.appear(ch, IF_MAIN_PORTAL.get(ch.pos));
                }
                Dungeon.hero.interrupt();
                Dungeon.observe();
                GameScene.updateFog();
            }else if(SUB_PORTAL.containsKey(ch.pos)){
                ScrollOfTeleportation.appear(ch, SUB_PORTAL.get(ch.pos));
                Dungeon.hero.interrupt();
                Dungeon.observe();
                GameScene.updateFog();
            }
        }
    }

    @Override
    public void seal() {
        super.seal();
        set( entrance, Terrain.WALL );
        GameScene.updateMap( entrance );
        CellEmitter.get( entrance ).start( FlameParticle.FACTORY, 0.1f, 10 );

        Dungeon.observe();

        YogReal boss = new YogReal();
        boss.pos = CENTER;
        GameScene.add(boss);

    }

    @Override
    public void unseal() {
        super.unseal();
        set( entrance, Terrain.ENTRANCE );
        GameScene.updateMap( entrance );

        set( exit, Terrain.EXIT );
        GameScene.updateMap( exit );

        CellEmitter.get(CENTER-1).burst(ShadowParticle.UP, 25);
        CellEmitter.get(CENTER).burst(ShadowParticle.UP, 100);
        CellEmitter.get(CENTER+1).burst(ShadowParticle.UP, 25);

        Dungeon.observe();
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return M.L(HallsLevel.class, "water_name");
            case Terrain.GRASS:
                return M.L(HallsLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return M.L(HallsLevel.class, "high_grass_name");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return M.L(HallsLevel.class, "statue_name");
            case Terrain.EMPTY_WELL:
                return M.L(YogGodHardBossLevel.class, "well_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.WATER:
                return M.L(HallsLevel.class, "water_desc");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return M.L(HallsLevel.class, "statue_desc");
            case Terrain.BOOKSHELF:
                return M.L(HallsLevel.class, "bookshelf_desc");
            case Terrain.EMPTY_WELL:
                return M.L(YogGodHardBossLevel.class, "well_desc");
            default:
                return super.tileDesc( tile );
        }
    }


    private int codeToTerrain(int code){
        switch (code){
            case 1:
            default:
                return Terrain.EMPTY;
            case 65:
                return Terrain.WALL;
            case 5: case 11:
                return Terrain.EMPTY_SP;
            case 2:
                return Terrain.EMPTY_DECO;
            case 21:
                return Terrain.PEDESTAL;
            case 257:
                return Terrain.WATER;
            case 66:
                return Terrain.WALL_DECO;
            case 85:
                return Terrain.LOCKED_EXIT;
            case 17:
                return Terrain.ENTRANCE;
            case 98:
                return Terrain.STATUE;
            case 99:
                return Terrain.STATUE_SP;
            case 20:
                return Terrain.EMPTY_WELL;
        }
    }

    private static final int[] codedMap = {
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,66,66,85,66,66,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,
            65,65,65,65,21,11,11,11,11,66,65,65,65,98,98,98,1,1,1,2,5,2,1,1,1,98,98,98,65,65,65,66,5,5,5,5,21,65,65,65,65,
            65,65,65,65,11,2,2,2,66,65,65,65,1,1,1,1,1,1,1,2,5,2,1,1,1,1,1,1,1,65,65,65,66,2,2,2,5,65,65,65,65,
            65,65,65,65,11,2,20,66,65,65,66,1,1,1,257,257,257,257,98,2,5,2,98,257,257,257,257,1,1,1,66,65,65,66,20,2,5,65,65,65,65,
            65,65,65,65,11,2,66,65,65,66,1,1,1,257,257,99,99,257,98,2,5,2,98,257,99,99,257,257,1,1,1,66,65,65,66,2,5,65,65,65,65,
            65,65,65,65,11,66,65,65,66,98,1,1,257,257,5,5,99,257,98,2,21,2,98,257,99,11,11,257,257,1,1,98,66,65,65,66,5,65,65,65,65,
            65,65,65,65,66,65,65,66,98,20,1,257,257,99,5,5,5,257,7,2,5,2,7,257,11,11,11,99,257,257,1,20,98,66,65,65,66,65,65,65,65,
            65,65,65,65,65,65,66,1,1,1,257,257,5,5,5,5,5,257,7,2,5,2,7,257,11,11,11,11,11,257,257,1,1,1,66,65,65,65,65,65,65,
            65,65,65,65,65,65,1,1,1,257,257,99,99,99,5,5,99,257,7,2,5,2,7,257,99,5,5,99,99,99,257,257,1,1,1,65,65,65,65,65,65,
            65,65,65,65,65,1,1,1,257,257,5,99,21,5,5,99,99,257,1,2,5,2,1,257,99,99,11,11,21,99,11,257,257,1,1,1,65,65,65,65,65,
            65,65,65,65,98,1,1,257,257,99,5,99,5,5,5,5,5,257,1,2,5,2,1,257,11,11,11,5,11,99,11,99,257,257,1,1,98,65,65,65,65,
            65,65,65,65,98,1,257,257,5,5,5,5,5,5,257,257,257,257,98,2,5,2,98,257,257,257,257,11,11,5,11,11,11,257,257,1,98,65,65,65,65,
            65,65,65,65,98,1,257,99,5,5,5,5,99,5,257,20,99,5,5,2,5,2,5,5,99,20,257,11,99,5,11,11,11,99,257,1,98,65,65,65,65,
            65,65,65,65,1,1,257,99,99,5,5,99,99,5,257,99,2,2,2,2,5,2,2,2,2,99,257,11,99,99,11,11,99,99,257,1,1,65,65,65,65,
            65,65,65,65,1,1,257,257,257,257,257,257,257,257,257,5,2,5,5,5,5,5,5,5,2,5,257,257,257,257,257,257,257,257,257,1,1,65,65,65,65,
            65,65,65,66,1,1,98,98,98,7,7,7,1,1,98,5,2,5,257,257,5,257,257,5,2,5,98,1,1,7,7,7,98,98,98,1,1,66,65,65,65,
            65,65,65,66,2,2,2,2,2,2,2,2,2,2,2,2,2,5,257,5,5,5,257,5,2,2,2,2,2,2,2,2,2,2,2,2,2,66,65,65,65,
            65,65,65,66,5,5,5,5,21,5,5,5,5,5,5,5,5,5,5,5,1,5,5,5,5,5,5,5,5,5,5,5,21,5,5,5,5,66,65,65,65,
            65,65,65,66,2,2,2,2,2,2,2,2,2,2,2,2,2,5,257,5,5,5,257,5,2,2,2,2,2,2,2,2,2,2,2,2,2,66,65,65,65,
            65,65,65,66,1,1,98,98,98,7,7,7,1,1,98,5,2,5,257,257,5,257,257,5,2,5,98,1,1,7,7,7,98,98,98,1,1,66,65,65,65,
            65,65,65,65,1,1,257,257,257,257,257,257,257,257,257,5,2,5,5,5,5,5,5,5,2,5,257,257,257,257,257,257,257,257,257,1,1,65,65,65,65,
            65,65,65,65,1,1,257,99,99,11,11,99,99,11,257,99,2,2,2,2,5,2,2,2,2,99,257,5,99,99,5,5,99,99,257,1,1,65,65,65,65,
            65,65,65,65,98,1,257,99,11,11,11,5,99,11,257,20,99,5,5,2,5,2,5,5,99,20,257,5,99,5,5,5,5,99,257,1,98,65,65,65,65,
            65,65,65,65,98,1,257,257,11,11,11,11,11,11,257,257,257,257,98,2,5,2,98,257,257,257,257,5,5,5,5,5,5,257,257,1,98,65,65,65,65,
            65,65,65,65,98,1,1,257,257,99,11,99,11,5,11,11,11,257,1,2,5,2,1,257,5,5,5,5,5,99,5,99,257,257,1,1,98,65,65,65,65,
            65,65,65,65,65,1,1,1,257,257,11,99,21,11,11,99,99,257,1,2,5,2,1,257,99,99,5,5,21,99,5,257,257,1,1,1,65,65,65,65,65,
            65,65,65,65,65,65,1,1,1,257,257,99,99,99,11,11,99,257,7,2,5,2,7,257,99,5,5,99,99,99,257,257,1,1,1,65,65,65,65,65,65,
            65,65,65,65,65,65,66,1,1,1,257,257,11,11,11,11,11,257,7,2,5,2,7,257,5,5,5,5,5,257,257,1,1,1,66,65,65,65,65,65,65,
            65,65,65,65,66,65,65,66,98,20,1,257,257,99,11,11,11,257,7,2,5,2,7,257,5,5,5,99,257,257,1,20,98,66,65,65,66,65,65,65,65,
            65,65,65,65,5,66,65,65,66,98,1,1,257,257,11,11,99,257,98,2,21,2,98,257,99,5,5,257,257,1,1,98,66,65,65,66,11,65,65,65,65,
            65,65,65,65,5,2,66,65,65,66,1,1,1,257,257,99,99,257,98,2,5,2,98,257,99,99,257,257,1,1,1,66,65,65,66,2,11,65,65,65,65,
            65,65,65,65,5,2,20,66,65,65,66,1,1,1,257,257,257,257,98,2,5,2,98,257,257,257,257,1,1,1,66,65,65,66,20,2,11,65,65,65,65,
            65,65,65,65,5,2,2,2,66,65,65,65,1,1,1,1,1,1,1,2,5,2,1,1,1,1,1,1,1,65,65,65,66,2,2,2,11,65,65,65,65,
            65,65,65,65,21,5,5,5,5,66,65,65,65,98,98,98,1,1,1,2,5,2,1,1,1,98,98,98,65,65,65,66,11,11,11,11,21,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,66,66,17,66,66,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65
    };
}

