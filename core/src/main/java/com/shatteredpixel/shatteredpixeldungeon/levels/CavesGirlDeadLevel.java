package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MagicGirlDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pylon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class CavesGirlDeadLevel extends Level {

    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;

        viewDistance = Math.max(viewDistance, 12);
    }

    private int status = 0;
    private static final int START = 0;
    private static final int FIGHTING = 1;
    private static final int WON = 2;
    private static final int ERROR = 9999999;

    private void progress(){
        if(status == START){
            status = FIGHTING;
        }else if(status == FIGHTING){
            status = WON;
        }else{
            status = ERROR;
        }
    }

    public int getStatus(){return status;}

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( "level_status", status );
    }



    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        status = bundle.getInt("level_status");
    }


    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_COLD;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_COLD;
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.GRASS:
                return Messages.get(CavesLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_name");
            case Terrain.WATER:
                return Messages.get(CavesLevel.class, "water_name");
            case Terrain.STATUE:
                //city statues are used
                return Messages.get(CityLevel.class, "statue_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return super.tileDesc( tile ) + "\n\n" + Messages.get(CavesGirlDeadLevel.class, "water_desc");
            case Terrain.ENTRANCE:
                return Messages.get(CavesLevel.class, "entrance_desc");
            case Terrain.EXIT:
                //city exit is used
                return Messages.get(CityLevel.class, "exit_desc");
            case Terrain.HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_desc");
            case Terrain.WALL_DECO:
                return Messages.get(CavesLevel.class, "wall_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(CavesLevel.class, "bookshelf_desc");
            //city statues are used
            case Terrain.STATUE:
                return Messages.get(CityLevel.class, "statue_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    private static final int WIDTH = 21;
    private static final int HEIGHT = 23;

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = MAP.clone();

        buildFlagMaps();
        cleanWalls();

        for(int i=4*WIDTH;i<length-4*WIDTH;++i){
            if(!solid[i]) {
                if (Random.Int(100) < 6) {
                }
            }
        }

        entrance = 10+19*21;
        exit = 10 + 3*21;

        map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    @Override
    public void seal(){
        super.seal();

        set( entrance, Terrain.WALL );

        Heap heap = Dungeon.level.heaps.get( entrance );
        if (heap != null) {
            int n;
            do {
                n = entrance + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
            } while (!Dungeon.level.passable[n]);
            Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( entrance );
        }

        Char ch = Actor.findChar( entrance );
        if (ch != null) {
            int n;
            do {
                n = entrance + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
            } while (!Dungeon.level.passable[n]);
            ch.pos = n;
            ch.sprite.place(n);
        }

        GameScene.updateMap( entrance );
        Dungeon.observe();
    }

    @Override
    public void unseal(){
        super.unseal();

        set( entrance, Terrain.ENTRANCE );

        GameScene.updateMap();

        Dungeon.observe();
    }

    @Override
    public boolean setCellToWater(boolean includeTraps, int cell) {
        return super.setCellToWater(false, cell);
    }

    @Override
    protected void createMobs() {
    }

    @Override
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
            } while (pos == entrance || pos == exit);
            drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
        }
    }

    private static final int W = Terrain.WALL;
    public static final int D = Terrain.WATER;
    private static final int e = Terrain.EMPTY;

    private static final int T = Terrain.WATER;

    private static final int E = Terrain.ENTRANCE;
    private static final int X = Terrain.EXIT;



    private static final int[] MAP = {
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, X, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W,
            W, W, W, W, T, e, e, e, e, e, e, e, e, e, e, e, T, W, W, W, W,
            W, W, W, e, e, T, e, e, e, e, e, e, e, e, e, T, e, e, W, W, W,
            W, W, W, e, e, e, T, e, e, e, W, e, e, e, T, e, e, e, W, W, W,
            W, W, W, e, e, e, e, T, e, e, W, e, e, T, e, e, e, e, W, W, W,
            W, W, W, e, e, e, e, e, T, e, W, e, T, e, e, e, e, e, W, W, W,
            W, W, W, e, e, e, e, e, e, T, T, T, e, e, e, e, e, e, W, W, W,
            W, W, W, e, e, e, W, W, W, T, D, T, W, W, W, e, e, e, W, W, W,
            W, W, W, e, e, e, e, e, e, T, T, T, e, e, e, e, e, e, W, W, W,
            W, W, W, e, e, e, e, e, T, e, W, e, T, e, e, e, e, e, W, W, W,
            W, W, W, e, e, e, e, T, e, e, W, e, e, T, e, e, e, e, W, W, W,
            W, W, W, e, e, e, T, e, e, e, W, e, e, e, T, e, e, e, W, W, W,
            W, W, W, e, e, T, e, e, e, e, e, e, e, e, e, T, e, e, W, W, W,
            W, W, W, W, T, e, e, e, e, e, e, e, e, e, e, e, T, W, W, W, W,
            W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, E, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
    };
    @Override
    public Group addVisuals() {
        super.addVisuals();
        CavesLevel.addCavesVisuals(this, visuals);
        return visuals;
    }

    @Override
    public void occupyCell( Char ch ) {

        super.occupyCell( ch );

        if (status == START && ch == Dungeon.hero) {

            progress();
            seal();

            MagicGirlDead boss = new MagicGirlDead();
            boss.state = boss.WANDERING;
            boss.pos = exit + WIDTH*2;
            GameScene.add( boss );

            Dungeon.observe();

            Camera.main.shake( 3, 0.7f );
            Sample.INSTANCE.play( Assets.Sounds.ROCKS );
        }
    }

    @Override
    public Heap drop(Item item, int cell ) {

        if (status < WON && item instanceof SkeletonKey) {

            progress();
            unseal();

            Dungeon.observe();
        }

        return super.drop( item, cell );
    }

    public static class PylonEnergy extends Blob {

        @Override
        protected void evolve() {
            for (int cell = 0; cell < Dungeon.level.length(); cell++) {
                if (Dungeon.level.insideMap(cell)) {
                    off[cell] = cur[cell];

                    //instantly spreads to water cells
                    if (off[cell] == 0 && Dungeon.level.water[cell]){
                        off[cell]++;
                    }

                    volume += off[cell];

                    if (off[cell] > 0){

                        Char ch = Actor.findChar(cell);
                        if (ch != null && !(ch instanceof MagicGirlDead)) {
                            Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
                            ch.damage( Random.NormalIntRange(6, 12), Electricity.class);
                            ch.sprite.flash();

                            if (ch == Dungeon.hero && !ch.isAlive()) {
                                Dungeon.fail(MagicGirlDead.class);
                                GLog.n( Messages.get(Electricity.class, "ondeath") );
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void fullyClear() {
            super.fullyClear();
            energySourceSprite = null;
        }

        private static CharSprite energySourceSprite = null;

        private static Emitter.Factory DIRECTED_SPARKS = new Emitter.Factory() {
            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                if (energySourceSprite == null){
                    for (Char c : Actor.chars()){
                        if (c instanceof Pylon && c.alignment != Char.Alignment.NEUTRAL){
                            energySourceSprite = c.sprite;
                            break;
                        } else if (c instanceof MagicGirlDead){
                            energySourceSprite = c.sprite;
                        }
                    }
                    if (energySourceSprite == null){
                        return;
                    }
                }

                SparkParticle s = ((SparkParticle) emitter.recycle(SparkParticle.class));
                s.resetStatic(x, y);
                s.speed.set((energySourceSprite.x + energySourceSprite.width/2f) - x,
                        (energySourceSprite.y + energySourceSprite.height/2f) - y);
                s.speed.normalize().scale(DungeonTilemap.SIZE*2f);

                //offset the particles slightly so they don't go too far outside of the cell
                s.x -= s.speed.x/8f;
                s.y -= s.speed.y/8f;
            }

            @Override
            public boolean lightMode() {
                return true;
            }
        };

        @Override
        public String tileDesc() {
            return Messages.get(CavesGirlDeadLevel.class, "energy_desc");
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );
            energySourceSprite = null;
            emitter.pour(DIRECTED_SPARKS, 0.125f);
        }

    }
}
