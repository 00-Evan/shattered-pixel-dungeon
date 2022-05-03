package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.EXSG;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.RLPT;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.SBSG;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaReal;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.HitBack;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualActor;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ScanningBeam;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SpreadWave;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.YogGodHardBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LarvaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class YogReal extends Boss {
    {
        initProperty();
        initBaseStatus(0, 0, 1, 0, 1200, 0, 0);
        initStatus(500);
        HP=1200;
        HT=1200;
        spriteClass = YogSprite.class;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.DEMONIC);

        //see all the map
        viewDistance = 16;
        state=HUNTING;
    }

    {
        immunities.add( Terror.class );
        immunities.add( Amok.class );
        immunities.add( Charm.class );
        immunities.add( Sleep.class );
        immunities.add( Vertigo.class );
        immunities.add( Frost.class );
        immunities.add( Paralysis.class );
    }

    private int phase = 0;
    private int destroyed = 0;

    private float summonCD = 15f;
    private int beamCD = 23;
    private float[] skillBalance = new float[]{100f, 100f, 100f};

    private static final int SUMMON_DECK_SIZE = 4;
    private ArrayList<Class> regularSummons = new ArrayList<>();
    {
        for (int i = 0; i < SUMMON_DECK_SIZE; i++){
            if (i >= Statistics.spawnersAlive){
                regularSummons.add(Larva.class);
            } else {
                regularSummons.add(YogRealRipper.class);
            }
        }
        Random.shuffle(regularSummons);
    }

    private ArrayList<Class> fistSummons = new ArrayList<>();

    {
        Random.pushGenerator(Dungeon.seedCurDepth());

        fistSummons.add(YogRealFirst.BurningFist.class);
        fistSummons.add(YogRealFirst.SoiledFist.class);
        fistSummons.add(YogRealFirst.RottingFist.class);
        fistSummons.add(YogRealFirst.RustedFist.class);
        fistSummons.add(YogRealFirst.BrightFist.class);
        fistSummons.add(YogRealFirst.DarkFist.class);

        Random.shuffle(fistSummons);

        Random.popGenerator();
    }

    private void actSummon(){
        summonCD -= 1f;

        while (summonCD <= 0){

            boolean success = false;

            Class<?extends Mob> cls = regularSummons.remove(0);
            Mob summon = Reflection.newInstance(cls);
            regularSummons.add(cls);

            int spawnPos = -1;
            for (int i : PathFinder.NEIGHBOURS8){
                if (Actor.findChar(pos+i) == null){
                    if (spawnPos == -1 || Dungeon.level.trueDistance(Dungeon.hero.pos, spawnPos) > Dungeon.level.trueDistance(Dungeon.hero.pos, pos+i)){
                        spawnPos = pos + i;
                    }
                }
            }

            if (spawnPos != -1) {
                summon.pos = spawnPos;
                GameScene.add( summon );
                Actor.addDelayed( new Pushing( summon, pos, summon.pos ), -1 );
                summon.beckon(Dungeon.hero.pos);
                success = true;
            }

            //repeat
            cls = regularSummons.remove(0);
            summon = Reflection.newInstance(cls);
            regularSummons.add(cls);

            spawnPos = -1;
            int[] candidates = YogGodHardBossLevel.summonPedestal.clone();
            for(int i = 0; i<4; ++i){
                int p = candidates[i];
                int r = Random.Int(4);
                candidates[i] = candidates[r];
                candidates[r] = p;
            }
            //find nearest
            for (int i : candidates){
                if (Actor.findChar(i) == null){
                    if (spawnPos == -1 || Dungeon.level.trueDistance(Dungeon.hero.pos, spawnPos) > Dungeon.level.trueDistance(Dungeon.hero.pos, i)){
                        spawnPos = i;
                    }
                }
            }

            if (spawnPos != -1) {
                summon.pos = spawnPos;
                GameScene.add( summon );
                CellEmitter.get(spawnPos).start(ElmoParticle.FACTORY, 0.05f, 20);
                summon.beckon(Dungeon.hero.pos);
                success = true;
            }

            if(success) {
                summonCD += standardSummonCD();
            }else{
                break;
            }
        }
    }

    private float standardSummonCD(){
        if(phase >= 5) return 5.25f;
        return Random.NormalFloat(22f, 30f) - phase*2;
    }

    private void actScanning(){
        if(phase>0) {
            --beamCD;
            if (beamCD <= 0) {
                Buff.detach(this, YogScanHalf.class);
                Buff.detach(this, YogScanRound.class);
                int skill  = Random.chances(skillBalance);
                if (skill == 0) {
                    Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
                    int w = Dungeon.level.width();
                    int dx = enemy.pos % w - pos % w;
                    int dy = enemy.pos / w - pos / w;
                    int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
                    direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
                    Buff.affect(this, YogScanHalf.class).setPos(pos, direction);
                    skillBalance[skill] /= 1.75f;
                    beamCD = 20 + 8 - (phase == 5?19:0);
                }else if(skill == 1){
                    Buff.affect(this, YogScanRound.class).setPos(pos);
                    skillBalance[skill] /= 2f;
                    beamCD = 20 + 10 - (phase == 5?19:0);
                }else if(skill == 2){
                    int count = 4 + (phase == 5 ? 3 : 0);
                    YogContinuousBeam b = Buff.affect(this, YogContinuousBeam.class);
                    b.setLeft(count);
                    b.setRage(phase == 5);
                    beamCD = 20 + count - (phase == 5?19:0);
                    skillBalance[skill] /= 2.25f;
                }
                Dungeon.hero.interrupt();
                if(skillBalance[0] < 0.1f){
                    skillBalance[0] = skillBalance[1] = skillBalance[2] = 100f;
                }
            }
        }
    }

    private void actSummonFist(){
        //vfx
        Dungeon.level.viewDistance = Math.max(1, Dungeon.level.viewDistance-1);
        if (Dungeon.hero.buff(Light.class) == null){
            Dungeon.hero.viewDistance = Dungeon.level.viewDistance;
        }
        Dungeon.observe();
        GLog.n(Messages.get(this, "darkness"));
        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
        //push back for spaces
        for(Char ch: Actor.chars()){
            int dist = Dungeon.level.distance(pos, ch.pos);
            if(dist <= 4 && dist > 0){
                Ballistica ba = HitBack.bounceBack(ch, this);
                WandOfBlastWave.throwChar(ch, ba, 10 - 2 * dist);
            }
        }

        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        //summon fists
        int[] candidates = PathFinder.NEIGHBOURS8.clone();
        for(int i = 0; i<8; ++i){
            int prev = candidates[i];
            int n = Random.Int(8);
            candidates[i]=candidates[n];
            candidates[n]=prev;
        }

        for(int i=0;i<phase-1;++i) {
            YogRealFirst fist = (YogRealFirst) Reflection.newInstance(fistSummons.remove(0));
            fist.pos = pos + candidates[i];
            GameScene.add(fist, 4f);

            Actor.addDelayed(new Pushing(fist, pos, fist.pos), -1);
        }

    }
    // 6, 9, 12 enemies on destroy.
    private static final int[] destroySummon_1 = {3, 2, 1};
    private static final int[] destroySummon_2 = {3, 3, 3};
    private static final int[] destroySummon_3 = {0, 6, 6};

    private void actDestroy(){
        if(findFist() == null && phase > destroyed + 1 && phase <= 4) {
            LinkedList<Integer> toDestroy = new LinkedList<>();
            int seed = Random.Int(4 - destroyed);
            int L = Dungeon.level.length();
            for (int i = 0; i < L; ++i) {
                if (Dungeon.level.map[i] == Terrain.STATUE || Dungeon.level.map[i] == Terrain.STATUE_SP) {
                    --seed;
                    if (seed <= 0) {
                        seed = 4 - destroyed;
                        if (destroySkeleton(i)) {
                            toDestroy.add(i);
                        }
                    }
                }
            }
            Random.shuffle(toDestroy);
            int[] toSummon = phase <= 2? destroySummon_1 : (phase <=3 ? destroySummon_2 : destroySummon_3);
            int willSummon = 0;
            for(int i:toSummon){
                willSummon += i;
            }
            int total = toDestroy.size();
            for (int i : toDestroy) {
                if (Random.Float()<(float)(willSummon/total)) {
                    int roll = Random.Int(willSummon);
                    int cat = 0;
                    for(int j=0;j<toSummon.length;++j){
                        if(roll>=toSummon[j]){
                            roll -= toSummon[j];
                            ++cat;
                        }else{
                            break;
                        }
                    }
                    Mob summoning = cat == 0? new YogRealRipper(): (cat == 1? new YogRealSuccubus(): new YogRealScorpio());
                    summoning.pos = i;
                    summoning.state = summoning.HUNTING;
                    GameScene.add(summoning, 2f);
                    CellEmitter.get(i).start(ElmoParticle.FACTORY, 0.08f, 40);
                    --willSummon;
                    --toSummon[Math.min(cat, toSummon.length-1)];
                }
                CellEmitter.get(i).burst(ElmoParticle.FACTORY, 10);
                --total;
            }
            GLog.w(M.L(this, "destroy_tile"));
            Camera.main.shake(2f, 1f);
            destroyed = Math.min(3, ++destroyed);
        }
    }

    private boolean destroySkeleton(int cell){
        if(Dungeon.level.map[cell] == Terrain.STATUE){
            Level.set(cell, Terrain.EMBERS);
            GameScene.updateMap(cell);
            return true;
        }
        if(Dungeon.level.map[cell] == Terrain.STATUE_SP){
            Level.set(cell, Terrain.EMPTY_SP);
            GameScene.updateMap(cell);
            return true;
        }
        return false;
    }

    @Override
    protected boolean act() {
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
        //char logic
        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
            fieldOfView = new boolean[Dungeon.level.length()];
        }
        Dungeon.level.updateFieldOfView( this, fieldOfView );

        throwItems();

        //mob logic
        enemy = chooseEnemy();

        if(enemy == null) enemy = Dungeon.hero;

        enemySeen = enemy != null && enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;
        //end of char/mob logic

        actScanning();
        actSummon();
        actDestroy();

        if (phase == 0){
            if (Dungeon.hero.viewDistance >= Dungeon.level.distance(pos, Dungeon.hero.pos)) {
                Dungeon.observe();
            }
            if (Dungeon.level.heroFOV[pos]) {
                notice();
            }
        }

        if(phase == 4 && findFist() == null){
            yell(Messages.get(this, "hope"));
            summonCD = -20;
            phase = 5;
            regularSummons.add(YogRealRipper.class);
        }

        spend(TICK);
        return true;
    }

    @Override
    public void damage(int damage, Object src){
        if(phase >= 5){
            if(damage > 25) {
                damage = 25;
            }
        }
        if (damage >= 380){
            damage = 380 + (int)(Math.sqrt(4*(damage - 14) + 1) - 1)/2;
            hero.HT = 25;
            Buff.affect(hero, Degrade.class, 12f);
        }


        if(src instanceof Buff || src instanceof Blob){
            damage = Math.max(0, damage-1);
        }

        int preHP = HP;
        super.damage(damage, src);
        int postHP = HP;
        int threshold = 1200-300*phase;
        if(preHP > threshold && postHP<=threshold){
            HP = threshold;
            ++phase;
            actSummonFist();
        }
        int dmgTaken = preHP - HP;

        if (dmgTaken > 0) {
            summonCD -= dmgTaken / 8f + 1f;
        }

        if(HP<=600){
            BossHealthBar.bleed(true);
        }

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmgTaken);

    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return phase == 0 || findFist() != null;
    }

    private YogRealFirst findFist(){
        for ( Char c : Actor.chars() ){
            if (c instanceof YogRealFirst){
                return (YogRealFirst) c;
            }
        }
        return null;
    }

    public void beckon( int cell ) {
    }

    @Override
    public void aggro(Char ch) {
        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (Dungeon.level.distance(pos, mob.pos) <= 8 &&
                    (mob instanceof Larva || mob instanceof RipperDemon)) {
                mob.aggro(ch);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void die( Object cause ) {
        GameScene.flash(0x80FFFFFF);

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof Larva
                    || mob instanceof RipperDemon
                    ||mob instanceof Scorpio
                    ||mob instanceof Succubus) {
                mob.die( cause );
            }
        }

        if(Dungeon.isChallenged(RLPT)){
            Badges.GOODRLPT();
        }

        if(Dungeon.isChallenged(SBSG)){
            Badges.BIGX();
        }

        if(Dungeon.isChallenged(EXSG)){
            Badges.EXSG();
        }

        Dungeon.level.viewDistance = 4;
        if (Dungeon.hero.buff(Light.class) == null){
            Dungeon.hero.viewDistance = Dungeon.level.viewDistance;
        }

        for(int i=0;i<4;++i){
            Dungeon.level.drop(new GoldenKey(Dungeon.depth), pos).sprite.drop();
        }

        GameScene.bossSlain();
        Dungeon.level.unseal();
        super.die( cause );

        yell( Messages.get(this, "defeated") );
    }


    @Override
    public void notice() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
            if (phase == 0) {
                phase = 1;
            }
        }
    }


    @Override
    public boolean isAlive() {
        return phase < 5 || HP > 0;
    }

    @Override
    public void storeInBundle(Bundle b){
        super.storeInBundle(b);
        b.put("phase", phase);
        b.put("count", destroyed);
        b.put("halfScanningCD", beamCD);
        b.put("FIST_SUMMONS", fistSummons.toArray(new Class[0]));
        b.put("REGULAR_SUMMONS", regularSummons.toArray(new Class[0]));
    }

    @Override
    public void restoreFromBundle(Bundle b){
        super.restoreFromBundle(b);
        phase = b.getInt("phase");
        destroyed = b.getInt("count");
        beamCD = b.getInt("halfScanningCD");
        fistSummons.clear();
        Collections.addAll(fistSummons, b.getClassArray("FIST_SUMMONS"));
        regularSummons.clear();
        Collections.addAll(regularSummons, b.getClassArray("REGULAR_SUMMONS"));
        if(phase>0) BossHealthBar.assignBoss(this);
        if (HP < 600) BossHealthBar.bleed(true);
    }



    public static class Larva extends Mob {

        {
            spriteClass = LarvaSprite.class;

            HP = HT = 30;
            defenseSkill = 16;
            viewDistance = Light.DISTANCE;

            EXP = 5;
            maxLvl = -2;

            properties.add(Property.DEMONIC);

            immunities.add(Corruption.class);
        }

        @Override
        public int attackSkill( Char target ) {
            return 50;
        }

        @Override
        public int damageRoll(){
            return Random.NormalIntRange(22, 34);
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 6);
        }

        @Override
        public void damage(int dmg, Object src){
            if(Dungeon.level.distance(pos, YogGodHardBossLevel.CENTER)<=4){
                dmg = Math.max(dmg/6, 2);
            }
            super.damage(dmg, src);
        }

    }

    //used so death to yog's ripper demons have their own rankings description and are more aggro
    public static class YogRealRipper extends Ice_Scorpio {
        {
            maxLvl = -999;
            viewDistance = 8;
        }
        @Override
        public void damage(int dmg, Object src){
            if(Dungeon.level.distance(pos, YogGodHardBossLevel.CENTER)<=4){
                dmg = Math.max(dmg/6, 2);
            }
            super.damage(dmg, src);
        }
    }

    public static class YogRealScorpio extends Scorpio {
        {
            maxLvl = -999;
            viewDistance = 8;
        }
        @Override
        public void damage(int dmg, Object src){
            if(Dungeon.level.distance(pos, YogGodHardBossLevel.CENTER)<=4){
                dmg = Math.max(dmg/3, 2);
            }
            super.damage(dmg, src);
        }
    }

    public static class YogRealSuccubus extends Succubus {
        {
            maxLvl = -999;
            viewDistance = 10;
        }
        @Override
        public void damage(int dmg, Object src){
            if(Dungeon.level.distance(pos, YogGodHardBossLevel.CENTER)<=4){
                dmg = Math.max(dmg/6, 2);
            }
            super.damage(dmg, src);
        }
    }


    public static class YogScanHalf extends Buff implements ScanningBeam.OnCollide{
        private int left = 8;
        //00:x- 01:x+ 10:y- 11:y+
        private int direction = 0;
        private int center = -1;

        public YogScanHalf setPos(int c, int d){
            this.center = c;
            this.direction = d;
            return this;
        }

        @Override
        public void storeInBundle(Bundle b){
            super.storeInBundle(b);
            b.put("centerPos", center);
            b.put("fourDirections", direction);
            b.put("leftTime", left);
        }

        @Override
        public void restoreFromBundle(Bundle b){
            super.restoreFromBundle(b);
            center = b.getInt("centerPos");
            direction = b.getInt("fourDirections");
            left = b.getInt("leftTime");
        }

        @Override
        public boolean act(){
            spend(TICK);

            if(left > 0){
                renderWarning((direction & 2) == 0, (direction & 1) != 0);
                --left;
            }else {
                renderSkill((direction & 2) == 0, (direction & 1) != 0);
                diactivate();
            }

            return true;
        }
        //warning
        protected void renderWarning(boolean isx, boolean positive){
            int w = Dungeon.level.width();
            int h = Dungeon.level.height();
            int xOfs = center % w;
            int yOfs = center / w;
            int startX; int startY;
            int endX; int endY;
            if(isx){
                startX = xOfs + (8 - left) * (positive ? 1: -1) * 2;
                endX = startX;
                startY = 1;
                endY = h - 1;
            }else{
                startY = yOfs + (8 - left) * (positive ? 1: -1) * 2;
                endY = startY;
                startX = 1;
                endX = w - 1;
            }
            target.sprite.parent.add(new BeamCustom(
                    new PointF(startX, startY).offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE),
                    new PointF(endX, endY).offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE),
                    Effects.Type.DEATH_RAY)
                    .setLifespan(0.7f).setColor(0xD471FF)
            );
        }
        //damage
        protected void renderSkill(boolean isx, boolean positive){
            int w = Dungeon.level.width();
            int xOfs = center % w;
            int yOfs = center / w;
            float startX; float startY;
            float xsp = 0; float ysp = 0;
            float ang;
            float r;
            if(isx){
                startX = xOfs;
                startY = 3;
                xsp = 10f * (positive ? 1f : -1f);
                ang = 90f;
                r = w - 6;
            }else{
                startY = yOfs;
                startX = 3;
                ysp = 10f * (positive ? 1f : -1f);
                ang = 0f;
                r = Dungeon.level.height() - 6;
            }

            ScanningBeam.setCollide(this);
            target.sprite.parent.add(new ScanningBeam(Effects.Type.DEATH_RAY, BallisticaReal.STOP_TARGET, new ScanningBeam.BeamData()
                            .setPosition(startX+0.5f, startY + 0.5f, ang, r)
                            .setSpeed(xsp, ysp, 0f)
                            .setTime(0.3f, 1.5f, 0.5f)
                    ).setDiameter(3f)
            );
            VirtualActor.delay(1.8f, ()->{
                detach();
                Camera.main.shake(2f, 0.3f);
            });

            Camera.main.shake(2f, 100f);

        }

        @Override
        public int onHitProc(Char ch) {
            if(ch.alignment == Alignment.ENEMY) return 0;
            ch.damage( Random.Int(50, 80), YogReal.class );
            ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 5, 10 ) );
            ch.sprite.flash();
            if(ch == Dungeon.hero){
                Sample.INSTANCE.play(Assets.Sounds.BLAST, Random.Float(1.1f, 1.5f));
                Buff.affect(ch, Degrade.class, 50f);
                if(!ch.isAlive()) Dungeon.fail(getClass());
            }
            return 1;
        }

        @Override
        public int cellProc(int i) {
            if(Dungeon.level.flamable[i]){
                Dungeon.level.destroy(i);
                GameScene.updateMap( i );
            }
            return 0;
        }
    }

    public static class YogScanRound extends Buff implements ScanningBeam.OnCollide {
        private int left = 10;
        private int center = -1;

        public YogScanRound setPos(int c) {
            this.center = c;
            return this;
        }

        @Override
        public void storeInBundle(Bundle b) {
            super.storeInBundle(b);
            b.put("centerPos", center);
            b.put("leftTime", left);
        }

        @Override
        public void restoreFromBundle(Bundle b) {
            super.restoreFromBundle(b);
            center = b.getInt("centerPos");
            left = b.getInt("leftTime");
        }

        @Override
        public boolean act(){
            spend(TICK);
            if(left>0){
                if(left % 3 == 1) renderWarn();
                --left;
            }else{
                renderSkill();
                diactivate();
            }
            return true;
        }

        public void renderWarn(){
            //SpreadRingImage.blast(10, 0.2f, 0.4f, 0xCCCCCC, 1f,
            //       DungeonTilemap.tileCenterToWorld(center), null);
            SpreadWave.blast(target.sprite.center(), 32, 1f, 0xCCCCCC, null);
            //target.sprite.parent.add(new RingImage(5*DungeonTilemap.SIZE, 0.5f, 1f, 0xAAAAAA).setPoint(target.sprite.center()));
        }

        public void renderSkill(){
            ScanningBeam.setCollide(this);
            target.sprite.parent.add(new ScanningBeam(Effects.Type.LIGHT_RAY, BallisticaReal.STOP_TARGET|BallisticaReal.STOP_SOLID, new ScanningBeam.BeamData()
                    .setPosition((center%Dungeon.level.width())+0.5f, (center/Dungeon.level.width())+0.5f, Random.Float(360f), 18)
                    .setSpeed(0, 0, 180f)
                    .setTime(0.3f, 2.0f, 0.5f))
                    .setDiameter(2.0f)
            );

            VirtualActor.delay(2.3f, () -> {
                detach();
                Camera.main.shake(2f, 0.3f);
            });

            Camera.main.shake(2f, 100f);
        }

        @Override
        public int onHitProc(Char ch) {
            if (ch.alignment == Alignment.ENEMY) return 0;
            ch.damage(Random.Int(40, 70), YogReal.class);
            ch.sprite.centerEmitter().burst(RainbowParticle.BURST, Random.Int(20, 35));
            ch.sprite.flash();
            Buff.affect(ch, Blindness.class, 50f);
            if (ch == Dungeon.hero) {
                Sample.INSTANCE.play(Assets.Sounds.BLAST, Random.Float(1.1f, 1.5f));
                if (!ch.isAlive()) Dungeon.fail(getClass());
            }
            return 1;
        }

        @Override
        public int cellProc(int i) {
            return 0;
        }
    }

    public static class YogContinuousBeam extends Buff{
        private int left = 10;
        private int aim = -1;
        private int lastAim = -1;
        private boolean raged = false;
        boolean started = false;

        public YogContinuousBeam setLeft(int left){
            this.left = left;
            return this;
        }
        public YogContinuousBeam setRage(boolean rage){
            this.raged = rage;
            return this;
        }

        @Override
        public boolean act(){
            spend(TICK);
            findTarget();
            if(left>1 && !started){
                renderWarn();
                started = true;
            }else{
                renderSkill();
                if(left <= 0) detach();
            }
            --left;
            return true;
        }

        private void renderWarn(){
            Ballistica ba = new Ballistica(target.pos, aim, Ballistica.WONT_STOP);
            target.sprite.parent.add(new BeamCustom(
                    target.sprite.center(), DungeonTilemap.tileCenterToWorld(ba.collisionPos), Effects.Type.LIGHT_RAY,null)
                    .setLifespan(0.5f).setDiameter(0.3f).setColor(0xFFE9B0));
        }

        private void renderSkill(){
            if(lastAim == -1) return;
            Ballistica ba = new Ballistica(target.pos, lastAim, Ballistica.WONT_STOP);
            target.sprite.parent.add(new BeamCustom(
                    target.sprite.center(), DungeonTilemap.tileCenterToWorld(ba.collisionPos), Effects.Type.LIGHT_RAY, null)
                    .setTime(0.4f, 0.6f, 0.3f).setDiameter(2.2f).setColor(left >= 2 ? 0xFFE9B0 : 0xA9C7FF));
            VirtualActor.delay(0.4f, ()->{
                Sample.INSTANCE.play(Assets.Sounds.RAY, Random.Float(0.8f, 1.25f));
                Camera.main.shake(3f, 0.5f);
                onHitProc(ba);
            });
        }

        private void onHitProc(Ballistica ba){
            for(Integer cell: ba.subPath(1, ba.dist)) {
                Char ch = findChar(cell);
                if (ch != null) {
                    if (ch.alignment == Alignment.ENEMY) continue;
                    ch.damage(Random.Int(30, 50), YogReal.class);
                    Buff.affect(ch, Blindness.class, 5f);
                    ch.sprite.flash();
                    if (ch == Dungeon.hero) {
                        Sample.INSTANCE.play(Assets.Sounds.BLAST, Random.Float(0.9f, 1.1f));
                        if (!ch.isAlive()) Dungeon.fail(getClass());
                    }
                }
                if (raged) {
                    int position;
                    for (int p : PathFinder.NEIGHBOURS4) {
                        position = cell + p;
                        if (Dungeon.level.map[position] == Terrain.STATUE) {
                            Level.set(position, Terrain.EMBERS);
                            GameScene.updateMap(position);
                        }
                        if (Dungeon.level.map[position] == Terrain.STATUE_SP) {
                            Level.set(position, Terrain.EMPTY_SP);
                            GameScene.updateMap(position);
                        }
                    }
                }
            }
        }

        private void findTarget(){
            lastAim = aim;
            //I can't get char.enemy from target, it's protected!
            aim = Dungeon.hero.pos;
        }

        @Override
        public void storeInBundle(Bundle b) {
            super.storeInBundle(b);
            b.put("thisAim", aim);
            b.put("lastAim", lastAim);
            b.put("leftTime", left);
            b.put("isRage", raged);
        }

        @Override
        public void restoreFromBundle(Bundle b) {
            super.restoreFromBundle(b);
            left = b.getInt("leftTime");
            aim = b.getInt("thisAim");
            lastAim = b.getInt("lastAim");
            raged = b.getBoolean("isRage");
        }
    }

}
