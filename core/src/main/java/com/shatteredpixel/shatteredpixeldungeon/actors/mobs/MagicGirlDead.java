package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SpellCaster;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GME;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MissileSpriteCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfGodIce;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesGirlDeadLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MagicGirlDead extends Boss {
    {
        spriteClass = MagicGirlSprite.class;

        initProperty();
        initBaseStatus(16, 22, 28, 16, 400, 4, 8);
        initStatus(76);
        HP=600;
        HT=600;
        viewDistance = 18;
    }

    {
        immunities.add(Sleep.class);

        resistances.add(Terror.class);
        resistances.add(Charm.class);
        resistances.add(Vertigo.class);
        resistances.add(Cripple.class);
        resistances.add(Chill.class);
        resistances.add(Frost.class);
        resistances.add(Roots.class);
        resistances.add(Slow.class);

        immunities.add(Paralysis.class);
    }

    //0~7 phases. if health < threshold[phase], then go on.
    private static final int[] healthThreshold = new int[]{399, 330, 270, 210, 160, 120, 80, 40, -1000000};

    private int phase = 0;

    private float summonCD = 50f;

    private int lastTargeting = -1;


    @Override
    public String info(){
        return Messages.get(this, "desc", phase, HP - healthThreshold[phase]);
    }

    @Override
    public float speed(){
        return super.speed() * (0.6f + phase*0.05f);
    }

    protected void goOnPhase(){
        phase++;
        CellEmitter.center(pos).burst(SnowParticle.FACTORY, 30);
        Sample.INSTANCE.play( Assets.Sounds.CURSED );
        if(phase % 2 == 0){
            destroyAll();
            ArrayList<Integer> places = new ArrayList<>();
            places.add(5*Dungeon.level.width()+4);
            places.add(6*Dungeon.level.width()-5);
            places.add(17*Dungeon.level.width()+4);
            places.add(18*Dungeon.level.width()-5);
            Random.shuffle(places);
            for(int i=0;i<Math.min(phase/2, 4);++i){
                summonCaster(Random.Int(4), places.get(i),false);
            }
        }else{
            destroyAll();
            for(int i=0;i<phase/2+1;++i){
                summonCaster(Random.Int(6), findRandomPlaceForCaster(), false);
            }
        }

        activateAll();

        lastTargeting = -1;
        Buff.affect(this, RageAndFire.class, 1f*phase + 5f);

        yell(Messages.get(this, "damaged"));
    }

    @Override
    public boolean act(){
        if(paralysed>0){
            spend(TICK);
            summonCD -= 1/speed();
            return true;
        }
        for (Buff buff : hero.buffs()) {
            if (buff instanceof RoseShiled) {
                buff.detach();
                GLog.b("玫瑰结界的创始人是翼绫，你怎么敢用她的技能?/kill @e[type=RoseShiled] enemy!");
            }
            if (buff instanceof HaloFireImBlue ||buff instanceof FireImbue) {
                buff.detach();
                GLog.b("你想免疫火的伤害？在我这里，没有可能！/kill @e[type=FireImbue=All] enemy!");
            }
        }
        if(buff(RageAndFire.class)!=null){
            //if target is locked, fire, target = -1
            if(lastTargeting != -1){
                //no spend, execute next act
                fireProc(lastTargeting);
                return true;
                //else try to lock target
            }else if(findTargetLocation()) {
                //if success, spend and ready to fire
                return true;
            }//else, just act
        }
        if(summonCD<0f){
            summonCD += Math.max(60f - phase * 2f, 40f);
            summonCaster(Random.Int(4), findRandomPlaceForCaster(), phase>5);
        }
        summonCD -= 1/speed();
        return super.act();
    }


    @Override
    public void move(int step) {

        super.move(step);

        Camera.main.shake(  1, 0.25f );

        if (Dungeon.level.map[step] == Terrain.WATER && state == HUNTING) {

            if (Dungeon.level.heroFOV[step] && HP < 200) {
                if (buff(Haste.class) == null) {
                    Buff.affect(this, RoseShiled.class, 15f);
                    Buff.affect(this, Haste.class, 5f);
                    Buff.affect(this, ArcaneArmor.class).set(Dungeon.hero.lvl + 10, 10);
                    Buff.affect(this, Healing.class).setHeal(40, 0f, 6);
                    new SRPDICLRPRO().spawnAround(pos);
                    Buff.affect(this, Adrenaline.class, 20f);
                    yell( Messages.get(this, "arise2") );
                    GLog.b(Messages.get(this, "shield2"));
                    Music.INSTANCE.play(Assets.BGM_BOSSE3, true);
                    enemy.sprite.showStatus(0x00ffff, ("游戏开始！！！"));
                }
                sprite.emitter().start(SparkParticle.STATIC, 0.05f, 20);
            } else if (Dungeon.level.heroFOV[step]) {
                if (buff(Haste.class) == null) {
                    Buff.affect(this, Haste.class, 10f);
                    Buff.affect(this, Healing.class).setHeal(42, 0f, 6);
                    new SRPDICLRPRO().spawnAround(pos);
                    yell( Messages.get(this, "arise") );
                    GLog.b(Messages.get(this, "shield"));
                    enemy.sprite.showStatus(0x00ffff, ("不自量力！！！"));
                }
                sprite.emitter().start(SparkParticle.STATIC, 0.05f, 20);
            }



            if (Dungeon.level.water[pos] && HP < HT) {
                if (Dungeon.level.heroFOV[pos] ){
                    sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                }
                if (HP*2 == HT) {
                    BossHealthBar.bleed(false);
                }
                HP++;
            }

            summonCD -= 24f;

        }
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    @Override
    public void damage(int damage, Object src){
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
        }
        if (damage >= 30){
            damage = 30 + (int)(Math.sqrt(4*(damage - 14) + 1) - 1)/2;
        }

        if (HP <= 50){
            damage = 5;
        }

        if(buff(RageAndFire.class)!=null) damage = Math.round(damage*0.1f);

        int preHP = HP;
        super.damage(damage, src);
        int postHP = HP;
        if(preHP>healthThreshold[phase] && postHP<=healthThreshold[phase]){
            HP = healthThreshold[phase];
            goOnPhase();
        }

        if(phase>4) BossHealthBar.bleed(true);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(damage*2);
    }

    @Override
    public void die(Object src){

        super.die(src);

        yell(Messages.get(this, "die"));

        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(m instanceof SpellCaster){
                m.die(this);
                Dungeon.level.mobs.remove(m);
            }
        }

        Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
        GameScene.bossSlain();
        Badges.KILLMG();
        Badges.validateBossSlain();

        WandOfGodIce woc = new WandOfGodIce();
        woc.level(9);
        woc.identify();

        Dungeon.level.drop(woc, pos).sprite.drop();

        Dungeon.level.drop(new Gold().quantity(Random.Int(1800, 3200)), pos).sprite.drop();
        Dungeon.level.drop(new PotionOfHealing().quantity(Random.Int(4, 7)), pos).sprite.drop();
        Dungeon.level.drop(new ScrollOfMagicMapping().quantity(2).identify(), pos).sprite.drop();
        Dungeon.level.drop(new ScrollOfUpgrade().quantity(Random.Int(3, 5)).identify(), pos).sprite.drop();


    }

    @Override
    protected boolean canAttack(Char enemy){
        if(enemy!=null && enemySeen){
            if(Dungeon.level.distance(pos, enemy.pos)<3) return true;
        }
        return false;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put("phaseDM", phase);
        bundle.put("summonCD", summonCD);
        bundle.put("lastTargetingDM", lastTargeting);
        super.storeInBundle(bundle);

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt("phaseDM");
        summonCD = bundle.getFloat("summonCD");
        lastTargeting = bundle.getInt("lastTargetingDM");

        BossHealthBar.assignBoss(this);
        if (phase>4) BossHealthBar.bleed(true);

    }

//caster ability logic

    private static final int FROST = 0;
    private static final int EXPLODE = 1;
    private static final int LIGHT = 2;
    private static final int HALOFIRE = 3;
    private static final int BOUNCE = 4;
    private static final int FIRESE = 5;

    protected void fallingRockVisual(int pos){
        Camera.main.shake(0.4f, 2f);
        CellEmitter.get( pos - Dungeon.level.width() ).start(Speck.factory(Speck.RED_LIGHT), 0.08f, 10);
    }

    protected void activateVisual(int pos){
        CellEmitter.get( pos ).start(Speck.factory(Speck.STAR), 0.14f, 8);
    }

    protected void summonCaster(int category, int pos, boolean activate){
        if(pos != -1){
            SpellCaster caster;
            switch (category){
                case FROST:
                    caster = new SpellCaster.FrostCaster();
                    break;
                case EXPLODE:
                    caster = new SpellCaster.ExplosionCaster();
                    break;
                case LIGHT:
                    caster = new SpellCaster.LightCaster();
                    break;
                case HALOFIRE:
                    caster = new SpellCaster.HaloFireCaster();
                    break;
                case BOUNCE: default:
                    caster = new SpellCaster.BounceCaster();
            }
            caster.pos = pos;
            GameScene.add(caster, Random.Float(2f, 8f));
            Dungeon.level.mobs.add(caster);
            fallingRockVisual(pos);
            if(activate) caster.activate();
            Dungeon.level.passable[pos] = false;
        }
    }

    protected int findRandomPlaceForCaster(){

        int[] ceil = GME.rectBuilder(pos, 4, 4);

        //shuffle
        for (int i=0; i < ceil.length - 1; i++) {
            int j = Random.Int( i, ceil.length );
            if (j != i) {
                int t = ceil[i];
                ceil[i] = ceil[j];
                ceil[j] = t;
            }
        }

        boolean valid;
        for(int i: ceil){
            valid = true;
            for(int j: PathFinder.NEIGHBOURS4){
                if(findChar(j+i)!=null){
                    valid = false;break;
                }
            }
            if(!valid) continue;
            if(findChar(i) == null && !Dungeon.level.solid[i] && !(Dungeon.level.map[i]==Terrain.INACTIVE_TRAP)){

                //caster.spriteHardlight();
                return i;
            }
        }

        return -1;
    }

    protected void activateAll(){
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(m instanceof SpellCaster){
                if(m.alignment == Alignment.NEUTRAL) {
                    ((SpellCaster) m).activate();
                    activateVisual(m.pos);
                }
            }
        }
    }

    protected void destroyAll(){
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(m instanceof SpellCaster){
                if(m.alignment == Alignment.NEUTRAL) continue;
                Ballistica beam = new Ballistica(m.pos, hero.pos, Ballistica.WONT_STOP);
                m.sprite.parent.add(new BeamCustom(
                        DungeonTilemap.raisedTileCenterToWorld(m.pos),
                        DungeonTilemap.tileCenterToWorld(beam.collisionPos),
                        Effects.Type.DEATH_RAY).setLifespan(0.9f));
                for(int i: beam.path){
                    Char ch = findChar(i);
                    if(ch!=null){
                        if(ch.alignment != Alignment.ENEMY){
                            SpellCaster.zapDamage(ch, 20, 30, 0.85f, m);
                        }
                    }
                }
                m.die(this);
                Dungeon.level.mobs.remove(m);
            }
        }
    }

    //the first num is all nums, and the second is activated nums.
    protected int[] aliveCasters(){
        int[] count = new int[]{0, 0};
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])) {
            if (m instanceof SpellCaster) {
                ++count[0];
                if(m.alignment != Alignment.NEUTRAL){
                    ++count[1];
                }
            }
        }
        return count;
    }

    public void onZapComplete(){
        ventGas(enemy);
        next();
    }

    public void ventGas( Char target ){
        hero.interrupt();

        int gasVented = 0;

        Ballistica trajectory = new Ballistica(pos, target.pos, Ballistica.STOP_TARGET);

        int gasMulti = 2 ;

        for (int i : trajectory.subPath(0, trajectory.dist)){
            GameScene.add(Blob.seed(i, 20*gasMulti, ToxicGas.class));
            gasVented += 20*gasMulti;
        }

        GameScene.add(Blob.seed(trajectory.collisionPos, 100*gasMulti, ToxicGas.class));

        if (gasVented < 250*gasMulti){
            int toVentAround = (int)Math.ceil(((250*gasMulti) - gasVented)/8f);
            for (int i : PathFinder.NEIGHBOURS8){
                GameScene.add(Blob.seed(pos+i, toVentAround, ToxicGas.class));
            }

        }

    }
    public boolean supercharged = false;
    public boolean isSupercharged(){
        return supercharged;
    }

    public void dropRocks( Char target ) {

        hero.interrupt();
        final int rockCenter;

        if (Dungeon.level.adjacent(pos, target.pos)){
            int oppositeAdjacent = target.pos + (target.pos - pos);
            Ballistica trajectory = new Ballistica(target.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
            WandOfBlastWave.throwChar(target, trajectory, 2, false, false);
            if (target == hero){
                hero.interrupt();
            }
            rockCenter = trajectory.path.get(Math.min(trajectory.dist, 2));
        } else {
            rockCenter = target.pos;
        }

        int safeCell;
        do {
            safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (safeCell == pos
                || (Dungeon.level.solid[safeCell] && Random.Int(2) == 0)
                || (Blob.volumeAt(safeCell, CavesGirlDeadLevel.PylonEnergy.class) > 0 && Random.Int(2) == 0));

        ArrayList<Integer> rockCells = new ArrayList<>();

        int start = rockCenter - Dungeon.level.width() * 3 - 3;
        int pos;
        for (int y = 0; y < 7; y++) {
            pos = start + Dungeon.level.width() * y;
            for (int x = 0; x < 7; x++) {
                if (!Dungeon.level.insideMap(pos)) {
                    pos++;
                    continue;
                }
                //add rock cell to pos, if it is not solid, and isn't the safecell
                if (!Dungeon.level.solid[pos] && pos != safeCell && Random.Int(Dungeon.level.distance(rockCenter, pos)) == 0) {
                    //don't want to overly punish players with slow move or attack speed
                    rockCells.add(pos);
                }
                pos++;
            }
        }
        Buff.append(this, NewDM300.FallingRockBuff.class, Math.min(target.cooldown(), 3*TICK)).setRockPositions(rockCells);

    }

    public void onSlamComplete(){
        dropRocks(enemy);
        next();
    }
    public static class RageAndFire extends FlavourBuff {
        Emitter charge;
        @Override
        public void fx(boolean on){
            if(on) {
                charge = target.sprite.emitter();
                charge.autoKill = false;
                charge.pour(SparkParticle.STATIC, 0.05f);
                //charge.on = false;
            }else{
                if(charge != null) {
                    charge.on = false;
                    charge = null;
                }

            }
        }
    }

    protected void fireProc(int targetCell){
        Ballistica ballistica = new Ballistica(pos, targetCell, Ballistica.PROJECTILE);
        ((MissileSpriteCustom)sprite.parent.recycle(MissileSpriteCustom.class)).reset(
                sprite, ballistica.collisionPos, new Bomb(), 10f, 2.0f,
                new Callback() {
                    @Override
                    public void call() {
                        int[] cells = GME.NEIGHBOURS5();
                        for(int i: cells){
                            int c = i+ballistica.collisionPos;
                            Char ch = findChar(c);
                            if(ch!=null){
                                if(ch.alignment != Alignment.ENEMY){
                                    int damage = Random.Int(14, 24);
                                    damage -= ch.drRoll();
                                    ch.damage(damage, this);
                                    if(ch == hero && !ch.isAlive()){
                                        Dungeon.fail(this.getClass());
                                    }
                                }
                            }
                            CellEmitter.center(c).burst(SnowParticle.FACTORY, 15);
                        }

                    }
                }
        );
        lastTargeting = -1;
    }

    protected boolean findTargetLocation(){
        if(enemy!=null && enemySeen){
            lastTargeting = enemy.pos;
        }else{
            lastTargeting = hero.pos;
        }
        if(canHit(lastTargeting)) {
            sprite.parent.addToBack(new TargetedCell(lastTargeting, 0xFF0000));
            spend(TICK);
            return true;
        }else{
            lastTargeting = -1;
            return false;
        }
    }

    protected boolean canHit(int targetPos){
        Ballistica ba = new Ballistica(pos, targetPos, Ballistica.PROJECTILE);
        return Dungeon.level.distance(ba.collisionPos, targetPos) <= 1;
    }

    @Override
    public boolean isAlive(){
        return HP>0 || healthThreshold[phase]>0;
    }
}

