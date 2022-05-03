package com.shatteredpixel.shatteredpixeldungeon;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaFloat;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GME;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualTimer;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrstalSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class SpellCaster extends Mob {
    {
        spriteClass = CrstalSprite.class;

        HP = HT = 45;

        maxLvl = -99;

        EXP = 10;

        properties.add(Property.BOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.IMMOVABLE);

        state = PASSIVE;
        alignment = Alignment.NEUTRAL;
        viewDistance = 9;
    }

    protected int lastTargeting=-1;
    protected int count=0;

    protected enum State{
        PREPARING, AIMING, SHOOTING
    }

    @Override
    public int drRoll(){
        return Random.NormalIntRange(3, 6);
    }

    @Override
    protected boolean act(){
        spend(TICK);
        if(alignment == Alignment.NEUTRAL) return true;
        State s = countDown();
        if(s == State.SHOOTING) {
            zapProc();
        }else{
            findTarget();
        }
        return true;
    }

    protected int maxCount(){
        return 6;
    }

    public void setCount(int count) {
        this.count = count;
    }

    protected State countDown(){
        if(maxCount() - count > 0 && maxCount() - count < 3) warn(maxCount()-count);
        if(count >= maxCount()){
            count = Random.Int(0, 3) - 3; return State.SHOOTING;
        }else if(count == maxCount() - 1){
            count ++;
            //sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "!") );
            //sprite.showStatus( CharSprite.NEGATIVE, "!!!" );
            return State.AIMING;
        }else{
            count ++;
            return State.PREPARING;
        }
    }

    protected boolean findTarget(){
        if(enemy != null){
            //record last pos of enemy. Not update if out of FOV.
            if(enemySeen){
                lastTargeting = enemy.pos;
                return true;
            }else{
                lastTargeting = Dungeon.hero.pos;
            }
        }else{
            lastTargeting = Dungeon.hero.pos;
        }
        return false;
    }

    protected abstract void zapProc();

    protected abstract void warn(int num);

    public void activate(){
        alignment = Alignment.ENEMY;
        ((CrstalSprite) sprite).activate();
        yell("水晶已经激活，请注意摧毁！");
    }

    @Override
    public CharSprite sprite() {
        CrstalSprite p = (CrstalSprite) super.sprite();
        if (alignment != Alignment.NEUTRAL) p.activate();
        return p;
    }

    @Override
    public void notice() {
        //do nothing
    }

    public abstract void spriteHardlight();

    @Override
    public void add(Buff buff) {
        //immune to all buffs/debuffs when inactive
        if (alignment != Alignment.NEUTRAL) {
            super.add(buff);
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        //immune to damage when inactive
        if (alignment == Alignment.NEUTRAL){
            return;
        }
        //prevents one-hit kill
        if (dmg >= 20){
            dmg = 20 + (int)(Math.sqrt(8*(dmg - 14) + 1) - 1)/2;
        }
        super.damage(dmg, src);
    }

    @Override
    public void die(Object cause){
        Dungeon.level.passable[pos] = true;
        super.die(cause);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("ALIGNMENT", alignment);
        bundle.put("lastPos", lastTargeting);
        bundle.put("countDown", count);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        alignment = bundle.getEnum("ALIGNMENT", Char.Alignment.class);
        lastTargeting = bundle.getInt("lastPos");
        count = bundle.getInt("countDown");
    }

    {
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( ToxicGas.class );
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
    }
    /*
        protected void checkDeath(Char ch, Object src){
            if(ch == Dungeon.hero && !ch.isAlive()){
                Dungeon.fail(src.getClass());
            }
        }

        protected void showWarn(int color, String text, Object... args){
            if (args.length > 0) {
                text = Messages.format( text, args );
            }
            float x = sprite.destinationCenter().x;
            float y = sprite.destinationCenter().y - sprite.height()/2f;
            if (sprite.ch != null) {
                FloatingText.show( x, y, sprite.ch.pos, text, color );
            } else {
                FloatingText.show( x, y, text, color );
            }
        }
     */
    //modifier works 115% efficiently to adjust difficulty
    public static void zapDamage(Char ch, int min, int max, float modifier, Object src){
        int damage = Random.IntRange(min, max);
        if(ch.buff(Marked.class)!=null){
            damage = Math.round(damage*(1f+modifier*1.15f));
        }
        ch.damage(damage, src);
        if(ch == Dungeon.hero && !ch.isAlive()){
            Dungeon.fail(src.getClass());
        }
    }

    public static class FrostCaster extends SpellCaster{

        @Override
        protected void zapProc() {
            //the collision pos of MAGIC_BOLT MUST be the pos of target, so find chars at collisionPos
            Ballistica ba = new Ballistica(pos, lastTargeting, Ballistica.PROJECTILE);

            sprite.parent.add(new BeamCustom(
                    sprite.center(),
                    DungeonTilemap.tileCenterToWorld(ba.collisionPos),
                    Effects.Type.DEATH_RAY).setLifespan(0.45f).setColor(0x6060FF));

            hitProc(ba);

        }

        @Override
        protected void warn(int num) {
            //if(num==2) sprite.showStatus( 0x5050FF, "!" );
            if(num==1) {sprite.showStatus(0x5050FF, "傲慢");
                new Flare( 6, 32 ).color( 0x5050FF, true ).show(sprite, 3f );
            }
        }

        @Override
        public void spriteHardlight() {
            sprite.hardlight(0x2222FF);
        }

        protected void hitProc(Ballistica ba){
            Char ch = findChar(ba.collisionPos);
            if(ch != null){
                ch.sprite.burst( 0xFF99CCFF, 5 );
                if(ch.alignment != Alignment.ENEMY){
                    Buff.affect(ch, Chill.class, 5f);
                    zapDamage(ch, 8, 12, 0.45f, this);
                }
            }
            Buff.affect(this, FrostPostShoot.class).setSource(ba.collisionPos);
        }

        public static class FrostPostShoot extends Buff{
            private int source=-1;
            public void setSource(int pos){
                source = pos;
            }
            @Override
            public void storeInBundle(Bundle b){
                b.put("sourcePosFrost", source);
                super.storeInBundle(b);
            }
            @Override
            public void restoreFromBundle(Bundle b){
                super.restoreFromBundle(b);
                source = b.getInt("sourcePosFrost");
            }
            @Override
            public boolean attachTo(Char target){
                spend(TICK);
                return super.attachTo(target);
            }
            @Override
            public boolean act(){
                if(source != -1){
                    for(int i: PathFinder.NEIGHBOURS4){
                        Ballistica ballistica = new Ballistica(source, source+i, Ballistica.MAGIC_BOLT);
                        MagicMissile m = MagicMissile.boltFromChar(target.sprite.parent, MagicMissile.FROST, target.sprite,
                                ballistica.collisionPos, () -> {
                                    //hitProc(ballistica);
                                });
                        m.reset(MagicMissile.FROST,
                                DungeonTilemap.tileCenterToWorld(source+i),
                                DungeonTilemap.tileCenterToWorld(ballistica.collisionPos),
                                new Callback() {
                                    @Override
                                    public void call() {
                                        hitProc(ballistica);
                                    }
                                } );
                        m.setSpeed(400f);
                        VirtualTimer.countTime(10f, m::destroy);
                    }
                }
                detach();
                return true;
            }
            protected void hitProc(Ballistica ballistica){
                Char ch = findChar(ballistica.collisionPos);
                if(ch != null){
                    ch.sprite.burst( 0xFF99CCFF,  3 );
                    if(ch.alignment != Alignment.ENEMY){
                        Buff.affect(ch, Chill.class, 3f);
                        zapDamage(ch, 5, 8, 0.9f, FrostCaster.class);
                    }
                }
            }
        }
    }

    public static class ExplosionCaster extends SpellCaster{

        @Override
        protected void zapProc() {
            Ballistica ba = new Ballistica(pos, lastTargeting, Ballistica.PROJECTILE);

            sprite.parent.add(new BeamCustom(
                    sprite.center(),
                    DungeonTilemap.tileCenterToWorld(ba.collisionPos),
                    Effects.Type.DEATH_RAY).setLifespan(0.45f).setColor(0xFF4040));

            hitProc(ba);

            Buff.affect(this, ExplosivePostShoot.class).setSource(ba.collisionPos);

        }

        @Override
        public void spriteHardlight() {
            sprite.hardlight(0xFF2222);
        }

        protected void hitProc(Ballistica ba){
            ArrayList<Char> affected = new ArrayList<>();

            sprite.parent.add(new BeamCustom(
                    DungeonTilemap.tileCenterToWorld(ba.collisionPos-1),
                    DungeonTilemap.tileCenterToWorld(ba.collisionPos+1),
                    Effects.Type.DEATH_RAY).setLifespan(0.4f).setColor(0x00FFFFFF));

            sprite.parent.add(new BeamCustom(
                    DungeonTilemap.tileCenterToWorld(ba.collisionPos-Dungeon.level.width()),
                    DungeonTilemap.tileCenterToWorld(ba.collisionPos+Dungeon.level.width()),
                    Effects.Type.DEATH_RAY).setLifespan(0.4f).setColor(0x00FFFFFF));

            boolean terrainAffected = false;
            int[] cells = GME.NEIGHBOURS5();
            for (int n : cells) {
                int c = ba.collisionPos + n;
                if (c >= 0 && c < Dungeon.level.length()) {

                    if (Dungeon.level.flamable[c]) {
                        Dungeon.level.destroy(c);
                        GameScene.updateMap(c);
                        terrainAffected = true;
                    }


                    Char ch = Actor.findChar(c);
                    if (ch != null) {
                        affected.add(ch);
                    }
                }
            }

            for (Char ch : affected){

                //if they have already been killed by another bomb
                if(!ch.isAlive()){
                    continue;
                }

                ch.sprite.centerEmitter().burst( PurpleParticle.BURST, 2 );
                ch.sprite.flash();

                if(ch.alignment == Alignment.ENEMY) continue;

                Buff.affect(ch, Marked.class, 6f);

                zapDamage(ch, 7, 12, 0.45f, this);
            }

            if (terrainAffected) {
                Dungeon.observe();
            }
        }


        @Override
        protected void warn(int num) {
            //if(num==2) sprite.showStatus( 0xFF7070, "!" );
            if(num==1) {
                sprite.showStatus(0xFF7070, "妒忌");
                new Flare( 6, 32 ).color( 0xFF7070, true ).show(sprite, 3f );
            }
        }

        public static class ExplosivePostShoot extends Buff{
            private int source=-1;
            public void setSource(int pos){
                source = pos;
            }
            @Override
            public void storeInBundle(Bundle b){
                b.put("sourcePosExplosive", source);
                super.storeInBundle(b);
            }
            @Override
            public void restoreFromBundle(Bundle b){
                super.restoreFromBundle(b);
                source = b.getInt("sourcePosExplosive");
            }
            @Override
            public boolean attachTo(Char target){
                spend(TICK);
                return super.attachTo(target);
            }
            @Override
            public boolean act(){
                if(source != -1){
                    final int[] directions = { -1 - Dungeon.level.width(), - 1 + Dungeon.level.width(),
                            1 - Dungeon.level.width(), 1 + Dungeon.level.width()};
                    for(int i: directions){
                        Ballistica ballistica = new Ballistica(source, source+i, Ballistica.STOP_SOLID);
                        hitProc(ballistica);
                        target.sprite.parent.add(new BeamCustom(DungeonTilemap.tileCenterToWorld(source),
                                DungeonTilemap.tileCenterToWorld(ballistica.collisionPos),
                                Effects.Type.DEATH_RAY).setLifespan(0.45f).setColor(0x00FFFFFF));
                    }
                }
                detach();
                return true;
            }
            protected void hitProc(Ballistica ballistica){
                for(int i: ballistica.subPath(1, ballistica.dist)) {
                    Char ch = findChar(i);
                    if (ch != null) {
                        ch.sprite.centerEmitter().burst( PurpleParticle.BURST, 1 );
                        ch.sprite.flash();
                        if (ch.alignment != Alignment.ENEMY) {
                            zapDamage(ch, 5, 7, 0.9f, ExplosionCaster.class);
                        }
                    }
                }
            }
        }
    }

    public static class LightCaster extends SpellCaster{

        @Override
        protected void zapProc() {
            Ballistica ba = new Ballistica(pos, lastTargeting, Ballistica.STOP_SOLID);

            sprite.parent.add(new BeamCustom(
                    sprite.center(),
                    DungeonTilemap.tileCenterToWorld(ba.collisionPos),
                    Effects.Type.LIGHT_RAY).setLifespan(0.7f).setColor(0x00FFFFFF));

            hitProc(ba);

            findTarget();

            Buff.affect(this, LightProcShot.class).setSource(lastTargeting);

        }

        @Override
        protected void warn(int num) {
            //if(num==2) sprite.showStatus( 0xFFFFFF, "!" );
            if(num==1){ sprite.showStatus(0xFFFFFF, "暴怒");
                new Flare( 6, 32 ).color( 0xFFFFFF, true ).show(sprite, 3f );}

        }

        @Override
        public void spriteHardlight() {
            sprite.hardlight(0xFFFFFF);
        }

        protected void hitProc(Ballistica ba){
            for(int i: ba.subPath(1, ba.dist)) {
                Char ch = findChar(i);
                if (ch != null) {
                    ch.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 6 );
                    if (ch.alignment != Alignment.ENEMY) {
                        Buff.affect(ch, Blindness.class, 4f);
                        zapDamage(ch, 9, 12, 0.6f, this);
                    }
                }
            }
        }

        public static class LightProcShot extends Buff{
            private int source=-1;
            public void setSource(int pos){
                source = pos;
            }
            @Override
            public void storeInBundle(Bundle b){
                b.put("sourcePosLight", source);
                super.storeInBundle(b);
            }
            @Override
            public void restoreFromBundle(Bundle b){
                super.restoreFromBundle(b);
                source = b.getInt("sourcePosLight");
            }
            @Override
            public boolean attachTo(Char target){
                spend(TICK);
                return super.attachTo(target);
            }
            @Override
            public boolean act(){
                if(source != -1){
                    final float[] angles = {-23f, 0f, 23f};
                    final float sourceAngle = GME.angle(target.pos, source);
                    for(float a: angles){
                        BallisticaFloat bf = new BallisticaFloat(target.pos, sourceAngle + a, 25, BallisticaFloat.STOP_SOLID);
                        Ballistica ballistica = new Ballistica(target.pos, bf.collisionPosI, Ballistica.STOP_SOLID);
                        hitProc(ballistica);
                        target.sprite.parent.add(new BeamCustom(DungeonTilemap.tileCenterToWorld(target.pos),
                                DungeonTilemap.tileCenterToWorld(ballistica.collisionPos),
                                Effects.Type.LIGHT_RAY).setLifespan(0.6f).setColor(0x00FFFFFF));
                    }
                }
                detach();
                return true;
            }
            protected void hitProc(Ballistica ballistica){
                for(int i: ballistica.subPath(1, ballistica.dist)) {
                    Char ch = findChar(i);
                    if (ch != null) {
                        ch.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 4 );
                        if (ch.alignment != Alignment.ENEMY) {
                            Buff.affect(ch, Blindness.class, 2f);
                            zapDamage(ch, 6, 9, 1.2f, LightCaster.class);
                        }
                    }
                }
            }
        }
    }

    public static class BounceCaster extends SpellCaster{

        @Override
        protected void zapProc() {
            Ballistica ba = new Ballistica(pos, lastTargeting, Ballistica.MAGIC_BOLT);

            sprite.parent.add(new BeamCustom(
                    sprite.center(),
                    DungeonTilemap.tileCenterToWorld(ba.collisionPos),
                    Effects.Type.DEATH_RAY).setLifespan(0.65f).setColor(0x30FF30));

            if(ba.collisionPos != lastTargeting) {
                findTarget();
                Buff.affect(this, BouncePostShoot.class).setTrace(ba.collisionPos, lastTargeting);
            }
            hitProc(ba);
        }

        @Override
        protected void warn(int num) {
            //if(num==2) sprite.showStatus( 0x30FF30, "!" );
            if(num==1) {sprite.showStatus(0x30FF30, "懒惰");
                new Flare( 6, 32 ).color( 0x30FF30, true ).show(sprite, 3f );}
        }

        @Override
        public void spriteHardlight() {
            sprite.hardlight(0x30FF30);
        }

        protected void hitProc(Ballistica ba){
            Char ch = findChar(ba.collisionPos);
            if (ch != null) {
                CellEmitter.center(ch.pos).burst( BloodParticle.BURST, 2 );
                if (ch.alignment != Alignment.ENEMY) {
                    Buff.affect(ch, Marked.class, 5f);
                    zapDamage(ch, 6, 9, 0.45f, this);
                }
            }
        }

        public static class BouncePostShoot extends Buff{
            private int source=-1;
            private int to = -1;
            public void setTrace(int pos, int nextPos){
                source = pos;
                to = nextPos;
            }
            @Override
            public void storeInBundle(Bundle b){
                b.put("sourcePosBounce", source);
                b.put("toPosBounce", to);
                super.storeInBundle(b);
            }
            @Override
            public void restoreFromBundle(Bundle b){
                super.restoreFromBundle(b);
                source = b.getInt("sourcePosBounce");
                to = b.getInt("toPosBounce");
            }
            @Override
            public boolean attachTo(Char target){
                spend(TICK);
                return super.attachTo(target);
            }
            @Override
            public boolean act(){
                if(source != -1){
                    Ballistica ballistica = new Ballistica(source, to, Ballistica.STOP_SOLID);
                    hitProc(ballistica);
                    target.sprite.parent.add(new BeamCustom(DungeonTilemap.tileCenterToWorld(source),
                            DungeonTilemap.tileCenterToWorld(ballistica.collisionPos),
                            Effects.Type.DEATH_RAY).setLifespan(0.55f).setColor(0x30FF30));
                }
                detach();
                return true;
            }
            protected void hitProc(Ballistica ballistica){
                for(int i: ballistica.subPath(1, ballistica.dist)) {
                    Char ch = findChar(i);
                    if (ch != null) {
                        CellEmitter.center(ch.pos).burst( BloodParticle.BURST, 1 );
                        if (ch.alignment != Alignment.ENEMY) {
                            Buff.affect(ch, Marked.class, 3f);
                            zapDamage(ch, 3, 6, 0.9f, BounceCaster.class);
                        }
                    }
                }
            }
        }
    }

    public static class HaloFireCaster extends SpellCaster{

        @Override
        protected void zapProc() {
            Ballistica ba = new Ballistica(pos, lastTargeting, Ballistica.MAGIC_BOLT);

            sprite.parent.add(new BeamCustom(
                    sprite.center(),
                    DungeonTilemap.tileCenterToWorld(ba.collisionPos),
                    Effects.Type.DEATH_RAY).setLifespan(0.65f).setColor(0x30FF30));

            if(ba.collisionPos != lastTargeting) {
                findTarget();
                Buff.affect(this, BouncePostShoot.class).setTrace(ba.collisionPos, lastTargeting);
            }
            hitProc(ba);
        }

        @Override
        protected void warn(int num) {
            //if(num==2) sprite.showStatus( 0x30FF30, "!" );
            if(num==1) {sprite.showStatus(0xff0000, "暴食");
                new Flare( 6, 32 ).color( 0x30FF30, true ).show(sprite, 3f );}
        }

        @Override
        public void spriteHardlight() {
            sprite.hardlight(0xff0000);
        }

        protected void hitProc(Ballistica ba){
            Char ch = findChar(ba.collisionPos);
            if (ch != null) {
                CellEmitter.center(ch.pos).burst( BloodParticle.BURST, 2 );
                if (ch.alignment != Alignment.ENEMY) {
                    Buff.affect( hero, HalomethaneBurning.class ).reignite( hero, 7f );
                    zapDamage(ch, 6, 9, 0.45f, this);
                }
            }
        }

        public static class BouncePostShoot extends Buff{
            private int source=-1;
            private int to = -1;
            public void setTrace(int pos, int nextPos){
                source = pos;
                to = nextPos;
            }
            @Override
            public void storeInBundle(Bundle b){
                b.put("sourcePosBounce", source);
                b.put("toPosBounce", to);
                super.storeInBundle(b);
            }
            @Override
            public void restoreFromBundle(Bundle b){
                super.restoreFromBundle(b);
                source = b.getInt("sourcePosBounce");
                to = b.getInt("toPosBounce");
            }
            @Override
            public boolean attachTo(Char target){
                spend(TICK);
                return super.attachTo(target);
            }
            @Override
            public boolean act(){
                if(source != -1){
                    Ballistica ballistica = new Ballistica(source, to, Ballistica.STOP_SOLID);
                    hitProc(ballistica);
                    target.sprite.parent.add(new BeamCustom(DungeonTilemap.tileCenterToWorld(source),
                            DungeonTilemap.tileCenterToWorld(ballistica.collisionPos),
                            Effects.Type.DEATH_RAY).setLifespan(0.55f).setColor(0xff0000));
                }
                detach();
                return true;
            }
            protected void hitProc(Ballistica ballistica){
                for(int i: ballistica.subPath(1, ballistica.dist)) {
                    Char ch = findChar(i);
                    if (ch != null) {
                        CellEmitter.center(ch.pos).burst( BloodParticle.BURST, 1 );
                        if (ch.alignment != Alignment.ENEMY) {
                            Buff.affect( hero, Burning.class ).reignite( hero, 4f );
                            zapDamage(ch, 3, 6, 0.9f, HaloFireCaster.class);
                        }
                    }
                }
            }
        }
    }

    public static class Marked extends FlavourBuff {
        @Override
        public boolean attachTo( Char target ){
            if(target.sprite!=null) target.sprite.showStatus(0xB040B0, Messages.get(SpellCaster.class, "marked"));
            return super.attachTo(target);
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.MARKED);
            else target.sprite.remove(CharSprite.State.MARKED);
        }
    }

}