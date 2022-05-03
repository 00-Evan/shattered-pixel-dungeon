package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HalomethaneFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.TomeOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GoldBAo;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlueFuck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.levels.DimandKingLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;

public class DimandKing extends Boss {
    {
        spriteClass = MimicSprite.Dimand.class;
        initProperty();
        initBaseStatus(14, 23, 33, 22, 200, 5, 12);
        initStatus(120);
        properties.add(Property.UNDEAD);
        HP=250;
        HT=250;
    }

    private int phase = 1;
    private int drop = Random.Int(5);
    private int summonsMade = 0;

    private float summonCooldown = 0;
    private float abilityCooldown = 6;
    private static final int MIN_COOLDOWN = 7;
    private static final int MAX_COOLDOWN = 11;

    private static float[] chanceMap = {0f, 100f, 100f, 100f, 100f, 100f, 100f};
    private int wave=0;

    private int lastAbility = 0;
    private static final int NONE = 0;
    private static final int LINK = 1;
    private static final int TELE = 2;
    private static final int ENRAGE = 3;
    private static final int DEATHRATTLE = 4;
    private static final int SACRIFICE = 5;
    private static final int SUMMON = 6;

    private static final String PHASE = "phase";
    private static final String SUMMONS_MADE = "summons_made";

    private static final String SUMMON_CD = "summon_cd";
    private static final String ABILITY_CD = "ability_cd";
    private static final String LAST_ABILITY = "last_ability";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
        bundle.put( SUMMONS_MADE, summonsMade );
        bundle.put( SUMMON_CD, summonCooldown );
        bundle.put( ABILITY_CD, abilityCooldown );
        bundle.put( LAST_ABILITY, lastAbility );
        bundle.put("wavePhase2", wave);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt( PHASE );
        summonsMade = bundle.getInt( SUMMONS_MADE );
        summonCooldown = bundle.getFloat( SUMMON_CD );
        abilityCooldown = bundle.getFloat( ABILITY_CD );
        lastAbility = bundle.getInt( LAST_ABILITY );
        wave = bundle.getInt("wavePhase2");

        if (phase == 2) properties.add(Property.IMMOVABLE);
    }

    private void resetChanceMap(){
        for(int i=1;i<chanceMap.length;++i){
            chanceMap[i]=100f;
        }
        chanceMap[0]=0f;
    }

    private void rollForAbility(){
        lastAbility = Random.chances(chanceMap);
        chanceMap[lastAbility] /= 4f;
        if(chanceMap[lastAbility] < 0.0001f) resetChanceMap();
    }

    private int aliveSummons(){
        int count = 0;
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(isSummonedByDK(m)){
                count++;
            }
        }
        return count;
    }

    @Override
    protected boolean act() {
        if (phase == 1) {

            if (summonCooldown <= 0 && summonSubject(3)){
                summonsMade++;
                summonCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN)-1f;
            } else if (summonCooldown > 0){
                summonCooldown-=1f/speed();
            }

            if (paralysed > 0){
                spend(TICK);
                return true;
            }

            if (abilityCooldown <= 0){
                int alive = aliveSummons();
                //NEVER use skills if no mobs alive.
                if(alive == 0) {
                    lastAbility = NONE;
                    //summon faster when no ability is available.
                    summonCooldown -= 2f;
                }
                else {
                    do {
                        rollForAbility();
                    } while ((lastAbility == ENRAGE || lastAbility == SACRIFICE) && alive < 2);
                }
                if(buff(SacrificeSubjectListener.class)!= null){
                    lastAbility = NONE;
                    //cd faster while prepare to sacrifice
                    abilityCooldown --;
                }

                if (lastAbility == LINK && lifeLinkSubject()){
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                } else if (lastAbility == TELE && teleportSubject()) {
                    lastAbility = TELE;
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                }else if(lastAbility == ENRAGE){
                    enrageSubject();
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                }else if(lastAbility == DEATHRATTLE){
                    deathRattleSubject();
                    lastAbility = DEATHRATTLE;
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                }else if(lastAbility == SACRIFICE){
                    sacrificeSubject();
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                }else if(lastAbility == SUMMON){
                    extraSummonSubject();
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                }

            } else {
                abilityCooldown--;
            }

        } else if (phase == 2){
            actPhaseTwoSummon();
            return true;
        } else if (phase == 3 && buffs(DimandKing.Summoning.class).size() < 4){
            if (summonSubject(2)) summonsMade++;
        }

        return super.act();
    }

    private boolean summonSubject( int delay ){

        //4th summon is always a monk or warlock, otherwise ghoul
        //4 1n 13 are monks/warlocks
        if (summonsMade % 13 == 7 || summonsMade % 13 == 11) {
            return summonSubject(delay, DKMonk.class );
        } else if(summonsMade % 13 == 5 || summonsMade % 13 == 12) {
            return summonSubject(delay, DKWarlock.class);
        }else{
            return summonSubject(delay, DKGhoul.class);
        }

    }

    private boolean summonSubject( int delay, Class<?extends Mob> type ){
        Summoning s = new Summoning();
        s.pos = ((DimandKingLevel)Dungeon.level).getSummoningPos();
        if (s.pos == -1) return false;
        s.summon = type;
        s.delay = delay;
        s.attachTo(this);
        return true;
    }

    private HashSet<Mob> getSubjects(){
        HashSet<Mob> subjects = new HashSet<>();
        for (Mob m : Dungeon.level.mobs){
            if (m.alignment == alignment && (m instanceof Ghoul || m instanceof Monk || m instanceof Warlock)){
                subjects.add(m);
            }
        }
        return subjects;
    }

    private boolean lifeLinkSubject(){
        Mob furthest = null;

        for (Mob m : getSubjects()){
            boolean alreadyLinked = false;
            for (LifeLink l : m.buffs(LifeLink.class)){
                if (l.object == id()) alreadyLinked = true;
            }
            if (!alreadyLinked) {
                if (furthest == null || Dungeon.level.distance(pos, furthest.pos) < Dungeon.level.distance(pos, m.pos)){
                    furthest = m;
                }
            }
        }

        if (furthest != null) {
            Buff.append(furthest, LifeLink.class, 100f).object = id();
            Buff.append(this, LifeLink.class, 100f).object = furthest.id();
            yell(Messages.get(this, "lifelink_" + Random.IntRange(1, 2)));
            sprite.parent.add(new Beam.HealthRay(sprite.destinationCenter(), furthest.sprite.destinationCenter()));
            return true;

        }
        return false;
    }

    private boolean teleportSubject(){
        if (enemy == null) return false;

        Mob furthest = null;

        for (Mob m : getSubjects()){
            if (furthest == null || Dungeon.level.distance(pos, furthest.pos) < Dungeon.level.distance(pos, m.pos)){
                furthest = m;
            }
        }

        if (furthest != null){

            float bestDist;
            int bestPos = pos;

            Ballistica trajectory = new Ballistica(enemy.pos, pos, Ballistica.STOP_TARGET);
            int targetCell = trajectory.path.get(trajectory.dist+1);
            //if the position opposite the direction of the hero is open, go there
            if (Actor.findChar(targetCell) == null && !Dungeon.level.solid[targetCell]){
                bestPos = targetCell;

                //Otherwise go to the neighbour cell that's open and is furthest
            } else {
                bestDist = Dungeon.level.trueDistance(pos, enemy.pos);

                for (int i : PathFinder.NEIGHBOURS8){
                    if (Actor.findChar(pos+i) == null
                            && !Dungeon.level.solid[pos+i]
                            && Dungeon.level.trueDistance(pos+i, enemy.pos) > bestDist){
                        bestPos = pos+i;
                        bestDist = Dungeon.level.trueDistance(pos+i, enemy.pos);
                    }
                }
            }

            Actor.add(new Pushing(this, pos, bestPos));
            pos = bestPos;

            //find closest cell that's adjacent to enemy, place subject there
            bestDist = Dungeon.level.trueDistance(enemy.pos, pos);
            bestPos = enemy.pos;
            for (int i : PathFinder.NEIGHBOURS8){
                if (Actor.findChar(enemy.pos+i) == null
                        && !Dungeon.level.solid[enemy.pos+i]
                        && Dungeon.level.trueDistance(enemy.pos+i, pos) < bestDist){
                    bestPos = enemy.pos+i;
                    bestDist = Dungeon.level.trueDistance(enemy.pos+i, pos);
                }
            }

            if (bestPos != enemy.pos) ScrollOfTeleportation.appear(furthest, bestPos);
            yell(Messages.get(this, "teleport_" + Random.IntRange(1, 2)));
            return true;
        }
        return false;
    }

    private void enrageSubject(){
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(isSummonedByDK(m)){
                Buff.affect(m, Healing.class).setHeal(42, 0f, 6);
                Buff.affect(m, StrengthEmpower.class, 10f);
            }
        }
        new Flare(5, 32).color(0xFF6060, false).show(sprite, 1.5f);
        yell(Messages.get(this,"buff_all"));
    }

    private void sacrificeSubject(){
        Buff.affect(this,  SacrificeSubjectListener.class, 3f);
        new Flare(6, 32).color(0xFF22FF, false).show(sprite, 1.5f);
        yell(Messages.get(this, "sacrifice"));
    }

    private void deathRattleSubject(){
        int count = 0;
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(isSummonedByDK(m)){
                //theoretically multiple death rattles is ok but,
                //problems: 1. vfx  2. mobs at one tile
                if(m.buff(DeathRattleSummon.class)==null) {
                    Buff.affect(m, DeathRattleSummon.class).setId(Random.chances(new float[]{9f, 4f, 4f}));
                    count++;
                }
            }
            if(count>=4) break;
        }
        new Flare(7, 32).color(0x303030, false).show(sprite, 1.5f);
        yell(Messages.get(this,"death_rattle"));
    }

    private void extraSummonSubject(){
        summonSubject(2);
        summonsMade++;
        summonSubject(3);
        summonsMade++;
        yell(Messages.get(this, "more_summon"));
        new Flare(4, 32).color(0x4040FF, false).show(sprite, 1.5f);
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
    public boolean isInvulnerable(Class effect) {
        return phase == 2 && effect != KingDamager.class;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (isInvulnerable(src.getClass())){
            super.damage(dmg, src);
            return;
        } else if (phase == 3 && !(src instanceof Viscosity.DeferedDamage)){

            if(src instanceof KingDamager) dmg = 1;

            if (dmg >= 0) {
                Viscosity.DeferedDamage deferred = Buff.affect( this, Viscosity.DeferedDamage.class );
                deferred.prolong( dmg );

                sprite.showStatus( CharSprite.WARNING, Messages.get(Viscosity.class, "deferred", dmg) );
            }
            return;
        }
        int preHP = HP;
        super.damage(dmg, src);

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg/3);


        if (phase == 1) {
            int dmgTaken = preHP - HP;
            abilityCooldown -= dmgTaken/8f;
            summonCooldown -= dmgTaken/8f;
            if (HP <= 120) {
                HP = 120;
                sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
                ScrollOfTeleportation.appear(this, DimandKingLevel.throne);
                properties.add(Property.IMMOVABLE);
                phase = 2;
                summonsMade = 0;
                sprite.idle();
                Buff.affect(this, DKBarrior.class).setShield(25*24);
                for (Summoning s : buffs(Summoning.class)) {
                    s.detach();
                }
                for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (m instanceof Ghoul || m instanceof Monk || m instanceof Warlock) {
                        m.die(null);
                    }
                }
                for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (m instanceof Ghoul || m instanceof Monk || m instanceof Warlock) {
                        m.die(null);
                    }
                }
                Buff.detach(this, SacrificeSubjectListener.class);
            }
        } else if (phase == 2 && shielding() == 0) {
            properties.remove(Property.IMMOVABLE);
            phase = 3;
            summonsMade = 3;
            sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            yell(  Messages.get(this, "enraged", Dungeon.hero.name()) );
            Buff.affect(this, Healing.class).setHeal(42, 0f, 6);

        } else if (phase == 3 && preHP > 30 && HP <= 30){
            yell( Messages.get(this, "losing") );
        }
    }

    @Override
    public boolean isAlive() {
        return super.isAlive() || phase != 3;
    }

    @Override
    public void die(Object cause) {

        GameScene.bossSlain();

        super.die( cause );
        if (Dungeon.hero.subClass == HeroSubClass.NONE) {
            Dungeon.level.drop( new TomeOfMastery(), pos ).sprite.drop();
        }
        int dropPos = this.pos;
        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (	mob instanceof DKMonk||
                    mob instanceof DKGhoul||
                    mob instanceof DKWarlock) {
                mob.die( cause );
            }
        }
        if (Dungeon.level.solid[pos]){
            Heap h = Dungeon.level.heaps.get(pos);
            if (h != null) {
                for (Item i : h.items) {
                    Dungeon.level.drop(i, pos + Dungeon.level.width());
                }
                h.destroy();
            }
            dropPos = pos + Dungeon.level.width();
        }

        WandOfFireblast woc = new WandOfFireblast();
        woc.level(3);
        woc.identify();

        WandOfBlueFuck fire = new WandOfBlueFuck();
        fire.level(1);
        fire.identify();
        if(drop<5) {
            Dungeon.level.drop(woc, dropPos).sprite.drop();
        }else {
            Dungeon.level.drop(fire, dropPos).sprite.drop();
         }
        Dungeon.level.drop(new Gold().quantity(Random.Int(1800, 3200)), pos).sprite.drop();
        //Dungeon.level.drop(new KingsCrown(), dropPos).sprite.drop();
        Dungeon.level.drop(new ScrollOfRecharging().quantity(3),  dropPos).sprite.drop(pos);
        Ankh ankh = new Ankh();
        ankh.bless();
        Dungeon.level.drop(new Ankh(), dropPos).sprite.drop(pos);
        Dungeon.level.drop(new Gold().quantity(Random.Int(1800, 3200)), pos).sprite.drop();
        Dungeon.level.drop(new GoldBAo().quantity(1),  dropPos).sprite.drop(pos);
        Badges.validateBossSlain();

        Dungeon.level.unseal();

        for (Mob m : getSubjects()){
            m.die(null);
        }

        yell( Messages.get(this, "defeated") );
    }

    @Override
    public boolean isImmune(Class effect) {
        //immune to damage amplification from doomed in 2nd phase or later, but it can still be applied
        if (phase > 1 && effect == Doom.class && buff(Doom.class) != null ){
            return true;
        }
        return super.isImmune(effect);
    }

    public static class DKGhoul extends OGPDLLS {
        {
            state = HUNTING;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;
        }

        @Override
        protected boolean act() {
            return super.act();
        }

        @Override
        public int damageRoll(){
            boolean str = buff(StrengthEmpower.class)!=null;
            return Math.round(super.damageRoll()*(str? 1.5f:1f));
        }
    }

    public static class DKMonk extends Guard {
        {
            state = HUNTING;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;
        }
        @Override
        public int damageRoll(){
            boolean str = buff(StrengthEmpower.class)!=null;
            return Math.round(super.damageRoll()*(str? 1.5f:1f));
        }
    }

    public static class DKWarlock extends FireGhostDead {
        {
            state = HUNTING;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;
        }
        @Override
        public int attackProc(Char enemy, int damage){
            if(Random.Int(10)==0) {
                Buff.affect(enemy, Degrade.class, 2f);
            }
            return super.attackProc(enemy, damage);
        }
        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 10, 15 );
        }
    }

    public static class Summoning extends Buff {

        private int delay;
        private int pos;
        private Class<?extends Mob> summon;

        private Emitter particles;

        public int getPos() {
            return pos;
        }

        @Override
        public boolean act() {
            delay--;

            if (delay <= 0){

                if (summon == DKWarlock.class){
                    particles.burst(ShadowParticle.CURSE, 10);
                    Sample.INSTANCE.play(Assets.Sounds.CURSED);
                } else if (summon == DKMonk.class){
                    particles.burst( ShadowParticle.MISSILE, 10 );
                    Sample.INSTANCE.play(Assets.Sounds.BURNING);
                } else {
                    particles.burst(EnergyParticle.FACTORY, 10);
                   Sample.INSTANCE.play(Assets.Sounds.READ);
                }
                particles = null;

                if (Actor.findChar(pos) != null){
                    ArrayList<Integer> candidates = new ArrayList<>();
                    for (int i : PathFinder.NEIGHBOURS8){
                        if (Dungeon.level.passable[pos+i] && Actor.findChar(pos+i) == null){
                            candidates.add(pos+i);
                        }
                    }
                    if (!candidates.isEmpty()){
                        pos = Random.element(candidates);
                    }
                }

                if (Actor.findChar(pos) == null) {
                    Mob m = Reflection.newInstance(summon);
                    m.pos = pos;
                    m.maxLvl = -2;
                    GameScene.add(m);
                    m.state = m.HUNTING;
                    if (((DimandKing)target).phase == 2){
                        Buff.affect(m, KingDamager.class);
                    }
                } else {
                    Char ch = Actor.findChar(pos);
                    ch.damage(Random.NormalIntRange(20, 40), summon);
                    if (((DimandKing)target).phase == 2){
                        target.damage(24, new KingDamager());
                    }
                }

                detach();
            }

            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on && particles == null) {
                particles = CellEmitter.get(pos);

                if (summon == DKWarlock.class){
                    particles.pour(ShadowParticle.UP, 0.1f);
                } else if (summon == DKMonk.class){
                    particles.pour(ElmoParticle.FACTORY, 0.1f);
                } else {
                    particles.pour(Speck.factory(Speck.RATTLE), 0.1f);
                }

            } else if (!on && particles != null) {
                particles.on = false;
            }
        }

        private static final String DELAY = "delay";
        private static final String POS = "pos";
        private static final String SUMMON = "summon";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DELAY, delay);
            bundle.put(POS, pos);
            bundle.put(SUMMON, summon);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            delay = bundle.getInt(DELAY);
            pos = bundle.getInt(POS);
            summon = bundle.getClass(SUMMON);
        }
    }

    public static class KingDamager extends Buff {

        @Override
        public boolean act() {
            if (target.alignment != Alignment.ENEMY){
                detach();
            }
            spend( TICK );
            return true;
        }

        @Override
        public void detach() {
            super.detach();
            for (Mob m : Dungeon.level.mobs){
                if (m instanceof DimandKing){
                    m.damage(24, this);
                }
            }
        }
    }

    public static class DKBarrior extends Barrier {

        @Override
        public boolean act() {
            incShield();
            return super.act();
        }

        @Override
        public int icon() {
            return BuffIndicator.NONE;
        }
    }


    public static class DeathRattleSummon extends Buff{
        private Emitter charge;
        private int summonCate=-1;
        public void setId(int id){
            summonCate = id;
        }
        @Override
        public void storeInBundle(Bundle b){
            b.put("summonCategory", summonCate);
            super.storeInBundle(b);
        }
        @Override
        public void restoreFromBundle(Bundle b){
            super.restoreFromBundle(b);
            summonCate = b.getInt("summonCategory");
        }
        public Mob idToMob(){
            switch (summonCate){
                case 0: return Reflection.newInstance(DKGhoul.class);
                case 1: return Reflection.newInstance(DKWarlock.class);
                case 2: return Reflection.newInstance(DKMonk.class);
                case 3: return Reflection.newInstance(Skeleton.class);
                case 4: return Reflection.newInstance(SRPDHBLR.class);
                case 5: return Reflection.newInstance(DM201.class);
                case 6: default: return Reflection.newInstance(OGPDZSLS.class);
            }
        }
        public boolean summon(int pos){
            Mob m = idToMob();
            if(m == null) return false;
            m.pos = pos;
            m.HP = m.HT * 3 / 5;
            GameScene.add(m);
            return true;
        }
        @Override
        public void detach(){
            summon(target.pos);
            if(summonCate == 0) new Flare(6, 25).color(0xCCCCCC, false).show(target.sprite, 1f);
            else if(summonCate == 1) new Flare(6, 25).color(0x303030, false).show(target.sprite, 1f);
            else if(summonCate == 2) new Flare(6, 25).color(0x4040FF, false).show(target.sprite, 1f);
            super.detach();
        }
        @Override
        public void fx(boolean on){
            if (on && charge == null &&summonCate!=-1) {
                charge = target.sprite.emitter();

                if (summonCate == 1){
                    charge.pour(ShadowParticle.UP, 0.3f);
                } else if (summonCate == 2){
                    charge.pour(ElmoParticle.FACTORY, 0.3f);
                } else {
                    charge.pour(Speck.factory(Speck.RATTLE), 0.3f);
                }

            } else {
                if(charge != null) {
                    charge.on = false;
                    charge = null;
                }
            }
        }
        @Override
        public boolean act(){
            fx(true);
            spend(TICK*9999f);
            return true;
        }
    }

    public static class SacrificeSubjectListener extends FlavourBuff {
        Emitter charge;
        @Override
        public void fx(boolean on){
            if(on) {
                charge = target.sprite.emitter();
                charge.autoKill = false;
                charge.pour( HalomethaneFlameParticle.FACTORY, 0.06f );
                //charge.on = false;
            }else{
                if(charge != null) {
                    charge.on = false;
                    charge = null;
                }

            }
        }
        @Override
        public void detach(){
            int[] area = PathFinder.NEIGHBOURS8;
            for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
                if(isSummonedByDK(m)){
                    m.die(DimandKing.class);
                    for(int i: area) {
                        CellEmitter.center(i+m.pos).burst(Speck.factory(Speck.BONE), 3);
                        Char ch = findChar(i+m.pos);
                        if(ch != null){
                            if(ch.alignment != Alignment.ENEMY){
                                ch.damage(Random.IntRange(25, 36), m);
                                if(ch == Dungeon.hero && !ch.isAlive()){
                                    Dungeon.fail(getClass());
                                }
                            }
                        }
                    }
                    CellEmitter.center(m.pos).burst(Speck.factory(Speck.BONE), 6);
                }
            }
            super.detach();
        }
    }

    protected static boolean isSummonedByDK(Mob m){
        return (m instanceof DKGhoul || m instanceof DKWarlock || m instanceof DKMonk);
    }

    private void actPhaseTwoSummon(){
        if(wave == 0){
            yell(Messages.get(this, "wave_1"));
            summonSubject(2, DKGhoul.class);
            summonSubject(3, DKGhoul.class);
            ++wave;
            spend(TICK*9);
        }else if(wave == 1){
            summonSubject(1, DKGhoul.class);
            summonSubject(5, DKMonk.class);
            ++wave;
            spend(TICK*12);
        }else if(wave == 2){
            summonSubject(1, DKGhoul.class);
            summonSubject(2, DKWarlock.class);
            summonSubject(6, DKGhoul.class);
            summonSubject(6, DKGhoul.class);
            ++wave;
            spend(TICK*15);
        }else if(wave == 3){
            yell(Messages.get(this, "wave_2"));
            summonSubject(1, DKGhoul.class);
            summonSubject(2, DKWarlock.class);
            summonSubject(2, DKGhoul.class);
            summonSubject(11, DKMonk.class);
            summonSubject(5, OGPDZSLS.class);
            summonSubject(7, SRPDHBLR.class);
            ++wave;
            spend(TICK*15);
        }else if(wave == 4){
            summonSubject(2, DKGhoul.class);
            summonSubject(5, DKWarlock.class);
            summonSubject(5, DKMonk.class);
            summonSubject(2, DKGhoul.class);
            summonSubject(5, OGPDZSLS.class);
            summonSubject(7, SRPDHBLR.class);
            summonSubject(5, DM100.class);
            ++wave;
            spend(TICK*14);
        }else if(wave == 5){
            yell(Messages.get(this,"wave_3"));
            summonSubject(2, DKGhoul.class);
            summonSubject(4, DKWarlock.class);
            summonSubject(4, DKMonk.class);
            summonSubject(8, DKMonk.class);
            summonSubject(2, DKGhoul.class);
            summonSubject(5, OGPDZSLS.class);
            summonSubject(7, SRPDHBLR.class);
            summonSubject(5, DM100.class);
            ++wave;
            spend(TICK*13);
        }else if(wave == 6){
            summonSubject(3, DKWarlock.class);
            summonSubject(3, DKMonk.class);
            summonSubject(3, DKMonk.class);
            summonSubject(3, DKWarlock.class);
            summonSubject(2, DKGhoul.class);
            summonSubject(5, OGPDZSLS.class);
            summonSubject(7, SRPDHBLR.class);
            summonSubject(5, DM100.class);
            summonSubject(2, DM201.class);
            summonSubject(5, DM200.class);
            summonSubject(7, MolotovHuntsman.class);
            summonSubject(5, Skeleton.class);
            summonSubject(5, Necromancer.class);
            summonSubject(5, RedNecromancer.class);
            ++wave;
            spend(TICK*12);
        }else{
            //only need to kill one.
            summonSubject(3, DKWarlock.class);
            spend(TICK);
        }
    }

    public static class StrengthEmpower extends FlavourBuff{
        Emitter charge;
        @Override
        public void fx(boolean on){
            if (on && charge == null) {
                charge = target.sprite.emitter();

                charge.pour(Speck.factory(Speck.UP), 0.7f);

            } else {
                if(charge != null) {
                    charge.on = false;
                    charge = null;
                }
            }
        }
        @Override
        public boolean attachTo(Char target){
            target.sprite.showStatus(0x00FF00, Messages.get(DimandKing.class, "str_empower"));
            return super.attachTo(target);
        }
    }
}
