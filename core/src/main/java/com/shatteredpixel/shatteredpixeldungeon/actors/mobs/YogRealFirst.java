package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.CorrosionParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.YogGodHardBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public abstract class YogRealFirst extends Mob {

    {
        HP = HT = 360;
        defenseSkill = 20;

        viewDistance = Light.DISTANCE;

        //for doomed resistance
        EXP = 25;
        maxLvl = -2;

        state = HUNTING;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
    }

    private float rangedCooldown;
    protected boolean canRangedInMelee = true;

    protected void incrementRangedCooldown(){
        rangedCooldown += Random.NormalFloat(6, 10);
    }

    @Override
    protected boolean act() {
        if (paralysed <= 0 && rangedCooldown > 0) rangedCooldown--;
        boolean beckon = false;
        if(enemy==null) beckon=true;
        else if(fieldOfView[enemy.pos]) beckon=true;
        if(beckon) beckon(Dungeon.hero.pos);
        return super.act();
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (rangedCooldown <= 0){
            return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
        } else {
            return super.canAttack(enemy);
        }
    }

    private boolean invulnWarned = false;

    protected boolean isNearYog(){
        int yogPos = YogGodHardBossLevel.CENTER;
        return Dungeon.level.distance(pos, yogPos) <= 4;
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        if (isNearYog() && !invulnWarned){
            invulnWarned = true;
            GLog.w(Messages.get(this, "invuln_warn"));
        }
        return isNearYog();
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos ) && (!canRangedInMelee || rangedCooldown > 0)) {

            return super.doAttack( enemy );

        } else {

            incrementRangedCooldown();
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    protected abstract void zap();

    public void onZapComplete(){
        zap();
        next();
    }

    @Override
    public int attackSkill( Char target ) {
        return 36;
    }

    @Override
    public int damageRoll() {
        return (Random.NormalIntRange( 20, 39 ) +  (isNearYog()?12:0)) ;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 15);
    }

    @Override
    public String description() {
        return Messages.get(YogFist.class, "desc") + "\n\n" + Messages.get(this, "desc");
    }

    public static final String RANGED_COOLDOWN = "ranged_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(RANGED_COOLDOWN, rangedCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        rangedCooldown = bundle.getFloat(RANGED_COOLDOWN);
    }

    public static class BurningFist extends YogRealFirst {

        {
            spriteClass = YogRealFistSprite.Burning.class;

            properties.add(Property.FIERY);
        }

        @Override
        public boolean act() {

            boolean result = super.act();

            for (int i : PathFinder.NEIGHBOURS9) {
                int vol = Fire.volumeAt(pos+i, Fire.class);
                if (vol < 4 && !Dungeon.level.water[pos + i] && !Dungeon.level.solid[pos + i]){
                    GameScene.add( Blob.seed( pos + i, 4 - vol, Fire.class ) );
                }
            }

            return result;
        }

        @Override
        protected void zap() {
            spend( 1f );

            if (Dungeon.level.map[enemy.pos] == Terrain.WATER){
                Level.set( enemy.pos, Terrain.EMPTY);
                GameScene.updateMap( enemy.pos );
                CellEmitter.get( enemy.pos ).burst( Speck.factory( Speck.STEAM ), 10 );
            } else {
                Buff.affect( enemy, Burning.class ).reignite( enemy );
            }

            for (int i : PathFinder.NEIGHBOURS9){
                if (!Dungeon.level.water[enemy.pos+i] && !Dungeon.level.solid[enemy.pos+i]){
                    int vol = Fire.volumeAt(enemy.pos+i, Fire.class);
                    if (vol < 4){
                        GameScene.add( Blob.seed( enemy.pos + i, 4 - vol, Fire.class ) );
                    }
                }
            }

        }

    }

    public static class SoiledFist extends YogRealFirst {

        {
            spriteClass = YogRealFistSprite.Soiled.class;
        }

        @Override
        public boolean act() {

            boolean result = super.act();

            int furrowedTiles = Random.chances(new float[]{0, 2, 3, 5});

            for (int i = 0; i < furrowedTiles; i++) {
                int cell = pos + PathFinder.NEIGHBOURS9[Random.Int(9)];
                if (Dungeon.level.map[cell] == Terrain.GRASS) {
                    Level.set(cell, Terrain.FURROWED_GRASS);
                    GameScene.updateMap(cell);
                    CellEmitter.get(cell).burst(LeafParticle.GENERAL, 10);
                }
            }

            Dungeon.observe();

            for (int i : PathFinder.NEIGHBOURS9) {
                int cell = pos + i;
                if (canSpreadGrass(cell)){
                    Level.set(pos+i, Terrain.GRASS);
                    GameScene.updateMap( pos + i );
                }
            }

            return result;
        }

        @Override
        public void damage(int dmg, Object src) {
            int grassCells = 0;
            for (int i : PathFinder.NEIGHBOURS9) {
                if (Dungeon.level.map[pos+i] == Terrain.FURROWED_GRASS
                        || Dungeon.level.map[pos+i] == Terrain.HIGH_GRASS){
                    grassCells++;
                }
            }
            if (grassCells > 0) dmg = Math.round(dmg * (6-grassCells)/6f);

            super.damage(dmg, src);
        }

        @Override
        protected void zap() {
            spend( 1f );

            if (hit( this, enemy, true )) {

                Buff.affect( enemy, Roots.class, 3f );

            } else {

                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

            for (int i : PathFinder.NEIGHBOURS9){
                int cell = enemy.pos + i;
                if (canSpreadGrass(cell)){
                    if (Random.Int(5) == 0){
                        Level.set(cell, Terrain.FURROWED_GRASS);
                        GameScene.updateMap( cell );
                    } else {
                        Level.set(cell, Terrain.GRASS);
                        GameScene.updateMap( cell );
                    }
                    CellEmitter.get( cell ).burst( LeafParticle.GENERAL, 10 );
                }
            }
            Dungeon.observe();

        }

        private boolean canSpreadGrass(int cell){
            int terrain = Dungeon.level.map[cell];
            return terrain != Terrain.EMPTY_SP && terrain != Terrain.WATER && terrain != Terrain.EMPTY_WELL &&  terrain != Terrain.PEDESTAL
                    && !Dungeon.level.solid[cell] && terrain != Terrain.GRASS && terrain != Terrain.FURROWED_GRASS
                    && !isNearYog();
        }

        {
            resistances.add(Burning.class);
        }


    }

    public static class RottingFist extends YogRealFirst {

        {
            spriteClass = YogRealFistSprite.Rotting.class;

            properties.add(Property.ACIDIC);
        }

        @Override
        protected boolean act() {
            //ensures toxic gas acts at the appropriate time when added
            GameScene.add(Blob.seed(pos, 0, ToxicGas.class));

            if (Dungeon.level.water[pos] && HP < HT) {
                sprite.emitter().burst( Speck.factory(Speck.HEALING), 3 );
                HP += HT/25;
            }

            return super.act();
        }

        @Override
        public void damage(int dmg, Object src) {
            if (!isInvulnerable(src.getClass()) && !(src instanceof Bleeding)){
                if (dmg < 0){
                    return;
                }
                Bleeding b = buff(Bleeding.class);
                if (b == null){
                    b = new Bleeding();
                }
                b.announced = false;
                b.set(dmg*.5f);
                b.attachTo(this);
                sprite.showStatus(CharSprite.WARNING, b.toString() + " " + (int)b.level());
            } else{
                super.damage(dmg, src);
            }
        }

        @Override
        protected void zap() {
            spend( 1f );
            GameScene.add(Blob.seed(enemy.pos, 100, ToxicGas.class));
        }

        @Override
        public int attackProc( Char enemy, int damage ) {
            damage = super.attackProc( enemy, damage );

            if (Random.Int( 2 ) == 0) {
                Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
                enemy.sprite.burst( 0xFF000000, 5 );
            }

            return damage;
        }

        {
            immunities.add(ToxicGas.class);
        }

    }

    public static class RustedFist extends YogRealFirst {

        {
            spriteClass = YogRealFistSprite.Rusted.class;

            properties.add(Property.LARGE);
            properties.add(Property.INORGANIC);
        }

        @Override
        public int damageRoll() {
            return super.damageRoll()+Random.NormalIntRange(5, 10);
        }

        @Override
        public void damage(int dmg, Object src) {
            if (!isInvulnerable(src.getClass()) && !(src instanceof Viscosity.DeferedDamage)){
                if (dmg >= 0) {
                    Buff.affect(this, Viscosity.DeferedDamage.class).prolong(dmg);
                    sprite.showStatus(CharSprite.WARNING, Messages.get(Viscosity.class, "deferred", dmg));
                }
            } else{
                super.damage(dmg, src);
            }
        }

        @Override
        protected void zap() {
            spend( 1f );
            Buff.affect(enemy, Cripple.class, 4f);
        }

    }

    public static class BrightFist extends YogRealFirst {

        {
            spriteClass = YogRealFistSprite.Bright.class;

            properties.add(Property.ELECTRIC);

            canRangedInMelee = false;
        }

        @Override
        protected void incrementRangedCooldown() {
            //ranged attack has no cooldown
        }

        public static class LightRay{};

        @Override
        protected void zap() {
            spend( 1f );

            if (hit( this, enemy, true )) {

                enemy.damage( Random.NormalIntRange(10, 20), new LightRay() );
                Buff.prolong( enemy, Blindness.class, Blindness.DURATION/2f );

                if (!enemy.isAlive() && enemy == Dungeon.hero) {
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(Char.class, "kill", name()) );
                }

            } else {

                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

        }

        @Override
        public void damage(int dmg, Object src) {
            int beforeHP = HP;
            super.damage(dmg, src);
            if (isAlive() && beforeHP > HT/2 && HP < HT/2){
                HP = HT/2;
                Buff.prolong( Dungeon.hero, Blindness.class, Blindness.DURATION*1.5f );
                int i;
                do {
                    i = Random.Int(Dungeon.level.length());
                } while (Dungeon.level.heroFOV[i]
                        || Dungeon.level.solid[i]
                        || Actor.findChar(i) != null
                        || PathFinder.getStep(i, Dungeon.level.exit, Dungeon.level.passable) == -1);
                ScrollOfTeleportation.appear(this, i);
                state = WANDERING;
                GameScene.flash(0xFFFFFF);
                GLog.w( Messages.get( this, "teleport" ));
            } else if (!isAlive()){
                Buff.prolong( Dungeon.hero, Blindness.class, Blindness.DURATION*3f );
                GameScene.flash(0xFFFFFF);
            }
        }

    }

    public static class DarkFist extends YogRealFirst {

        {
            spriteClass = YogRealFistSprite.Dark.class;

            canRangedInMelee = false;
        }

        @Override
        protected void incrementRangedCooldown() {
            //ranged attack has no cooldown
        }

        //used so resistances can differentiate between melee and magical attacks
        public static class DarkBolt{}

        @Override
        protected void zap() {
            spend( 1f );

            if (hit( this, enemy, true )) {

                enemy.damage( Random.NormalIntRange(10, 20), new DarkBolt() );

                Light l = enemy.buff(Light.class);
                if (l != null){
                    l.weaken(50);
                }

                if (!enemy.isAlive() && enemy == Dungeon.hero) {
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(Char.class, "kill", name()) );
                }

            } else {

                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

        }

        @Override
        public void damage(int dmg, Object src) {
            int beforeHP = HP;
            super.damage(dmg, src);
            if (isAlive() && beforeHP > HT/2 && HP < HT/2){
                HP = HT/2;
                Light l = Dungeon.hero.buff(Light.class);
                if (l != null){
                    l.detach();
                }
                int i;
                do {
                    i = Random.Int(Dungeon.level.length());
                } while (Dungeon.level.heroFOV[i]
                        || Dungeon.level.solid[i]
                        || Actor.findChar(i) != null
                        || PathFinder.getStep(i, Dungeon.level.exit, Dungeon.level.passable) == -1);
                ScrollOfTeleportation.appear(this, i);
                state = WANDERING;
                GameScene.flash(0, false);
                GLog.w( Messages.get( this, "teleport" ));
            } else if (!isAlive()){
                Light l = Dungeon.hero.buff(Light.class);
                if (l != null){
                    l.detach();
                }
                GameScene.flash(0, false);
            }
        }

    }


    public abstract static class YogRealFistSprite extends MobSprite {

        private static final float SLAM_TIME	= 0.33f;

        protected int boltType;

        protected abstract int texOffset();

        private Emitter particles;
        protected abstract Emitter createEmitter();

        public YogRealFistSprite() {
            super();

            int c = texOffset();

            texture( Assets.Sprites.FISTS );

            TextureFilm frames = new TextureFilm( texture, 24, 17 );

            idle = new MovieClip.Animation( 2, true );
            idle.frames( frames, c+0, c+0, c+1 );

            run = new MovieClip.Animation( 3, true );
            run.frames( frames, c+0, c+1 );

            attack = new MovieClip.Animation( Math.round(1 / SLAM_TIME), false );
            attack.frames( frames, c+0 );

            zap = new MovieClip.Animation( 8, false );
            zap.frames( frames, c+0, c+5, c+6 );

            die = new MovieClip.Animation( 10, false );
            die.frames( frames, c+0, c+2, c+3, c+4 );

            play( idle );
        }

        @Override
        public void link( Char ch ) {
            super.link( ch );

            if (particles == null) {
                particles = createEmitter();
            }
        }

        @Override
        public void update() {
            super.update();

            if (particles != null){
                particles.visible = visible;
            }
        }

        @Override
        public void die() {
            super.die();
            if (particles != null){
                particles.on = false;
            }
        }

        @Override
        public void kill() {
            super.kill();
            if (particles != null){
                particles.killAndErase();
            }
        }

        @Override
        public void attack( int cell ) {
            super.attack( cell );

            jump(ch.pos, ch.pos, null, 9, SLAM_TIME );
        }

        public void zap( int cell ) {

            turnTo( ch.pos , cell );
            play( zap );

            MagicMissile.boltFromChar( parent,
                    boltType,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ((YogRealFirst)ch).onZapComplete();
                        }
                    } );
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
        }

        @Override
        public void onComplete( MovieClip.Animation anim ) {
            super.onComplete( anim );
            if (anim == attack) {
                Camera.main.shake( 4, 0.2f );
            } else if (anim == zap) {
                idle();
            }
        }

        public static class Burning extends YogRealFistSprite {

            {
                boltType = MagicMissile.FIRE;
            }

            @Override
            protected int texOffset() {
                return 0;
            }

            @Override
            protected Emitter createEmitter() {
                Emitter emitter = emitter();
                emitter.pour( FlameParticle.FACTORY, 0.06f );
                return emitter;
            }

            @Override
            public int blood() {
                return 0xFFFFDD34;
            }

        }

        public static class Soiled extends YogRealFistSprite {

            {
                boltType = MagicMissile.FOLIAGE;
            }

            @Override
            protected int texOffset() {
                return 10;
            }

            @Override
            protected Emitter createEmitter() {
                Emitter emitter = emitter();
                emitter.pour( LeafParticle.GENERAL, 0.06f );
                return emitter;
            }

            @Override
            public int blood() {
                return 0xFF7F5424;
            }

        }

        public static class Rotting extends YogRealFistSprite {

            {
                boltType = MagicMissile.TOXIC_VENT;
            }

            @Override
            protected int texOffset() {
                return 20;
            }

            @Override
            protected Emitter createEmitter() {
                Emitter emitter = emitter();
                emitter.pour(Speck.factory(Speck.TOXIC), 0.25f );
                return emitter;
            }

            @Override
            public int blood() {
                return 0xFFB8BBA1;
            }

        }

        public static class Rusted extends YogRealFistSprite {

            {
                boltType = MagicMissile.CORROSION;
            }

            @Override
            protected int texOffset() {
                return 30;
            }

            @Override
            protected Emitter createEmitter() {
                Emitter emitter = emitter();
                emitter.pour(CorrosionParticle.MISSILE, 0.06f );
                return emitter;
            }

            @Override
            public int blood() {
                return 0xFF7F7F7F;
            }

        }

        public static class Bright extends YogRealFistSprite {

            {
                boltType = MagicMissile.RAINBOW;
            }

            @Override
            protected int texOffset() {
                return 40;
            }

            @Override
            protected Emitter createEmitter() {
                Emitter emitter = emitter();
                emitter.pour(SparkParticle.STATIC, 0.06f );
                return emitter;
            }

            @Override
            public void zap( int cell ) {
                turnTo( ch.pos , cell );
                play( zap );

                ((YogRealFirst)ch).onZapComplete();
                parent.add( new Beam.LightRay(center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
            }
            @Override
            public int blood() {
                return 0xFFFFFFFF;
            }

        }

        public static class Dark extends YogRealFistSprite {

            {
                boltType = MagicMissile.SHADOW;
            }

            @Override
            protected int texOffset() {
                return 50;
            }

            @Override
            protected Emitter createEmitter() {
                Emitter emitter = emitter();
                emitter.pour(ShadowParticle.MISSILE, 0.06f );
                return emitter;
            }

            @Override
            public int blood() {
                return 0xFF4A2F53;
            }

        }

    }
}

