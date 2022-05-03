package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.levels.DM920BossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM150Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM275Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300AttackSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300SpiderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM75Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

//DM920 隐藏Boss
public class DM920 extends Mob
{
    public static class DM150 extends Mob
    {

        public int attackSkill(Char char1)
        {
            return 14;
        }

        @Override
        public void damage(int dmg, Object src) {
            super.damage(dmg, src);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
        }
        public int damageRoll()
        {
            return Random.NormalIntRange(10, 13);
        }

        public int drRoll() {
            return Random.NormalIntRange(0, 10);
        }

        public void move(int i)
        {
            super.move(i);
            if(Dungeon.level.map[i] == 19 && HP < HT)
            {
                HP = HP + Random.Int(1, HT - HP);
                sprite.emitter().burst(ElmoParticle.FACTORY, 3);
                if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                    GLog.n(Messages.get(this, "repair", new Object[0]), new Object[0]);
            }
        }

        public void notice()
        {
            super.notice();
            yell(Messages.get(this, "notice"));
        }

        public DM150()
        {
            spriteClass = DM150Sprite.class;
            HT = 50;
            HP = 50;
            EXP = 15;
            defenseSkill = 9;
            loot = new StoneOfEnchantment();
            lootChance = 0.333F;
        }
    }

    public static class DM300AttackMode extends Mob implements Callback
    {

        public int attackSkill(Char char1)
        {
            return 28;
        }

        @Override
        public void damage(int dmg, Object src) {
            super.damage(dmg, src);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
        }

        public int damageRoll()
        {
            return Random.NormalIntRange(20, 25);
        }

        public void die(Object obj)
        {
            super.die(obj);
            obj = new DM300SpiderMode();
            ((DM300SpiderMode) obj).pos = pos;
            GameScene.add(((Mob) (obj)));
            Actor.addDelayed(new Pushing(((Char) (obj)), pos, ((DM300SpiderMode) (obj)).pos), -1F);
            yell( Messages.get(this, "defeated") );
        }

        public void ventGas( Char target ){
            hero.interrupt();

            int gasVented = 0;

            Ballistica trajectory = new Ballistica(pos, target.pos, Ballistica.STOP_TARGET);

            for (int i : trajectory.subPath(0, trajectory.dist)){
                GameScene.add(Blob.seed(i, 20, ToxicGas.class));
                gasVented += 20;
            }

            GameScene.add(Blob.seed(trajectory.collisionPos, 100, ToxicGas.class));

            if (gasVented < 250){
                int toVentAround = (int)Math.ceil((250 - gasVented)/8f);
                for (int i : PathFinder.NEIGHBOURS8){
                    GameScene.add(Blob.seed(pos+i, toVentAround, ToxicGas.class));
                }

            }

        }

        public int drRoll()
        {
            return Random.NormalIntRange(0, 10);
        }

        public void move(int i)
        {
            super.move(i);
            if(Dungeon.level.map[i] == 19 && HP < HT)
            {
                HP = HP + Random.Int(1, HT - HP);
                sprite.emitter().burst(ElmoParticle.FACTORY, 5);
                if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                    GLog.n(Messages.get(this, "repair", new Object[0]), new Object[0]);
            }
            if(Dungeon.level.heroFOV[i])
            {
                CellEmitter.get(i).start(Speck.factory(8), 0.07F, 10);
                Camera.main.shake(3F, 0.7F);
                Sample.INSTANCE.play("sound/snd_rocks.mp3");
                if(Dungeon.level.water[i])
                    GameScene.ripple(i);
                else
                if(Dungeon.level.map[i] == 1)
                {
                    Level.set(i, 20);
                    GameScene.updateMap(i);
                }
            }
        }

        public void notice()
        {
            super.notice();
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
        }

        public void restoreFromBundle(Bundle bundle)
        {
            super.restoreFromBundle(bundle);
            BossHealthBar.assignBoss(this);
        }

        public DM300AttackMode()
        {
            spriteClass = DM300AttackSprite.class;
            HT = 350;
            HP = 350;
            EXP = 30;
            defenseSkill = 18;
        }

        protected boolean doAttack( Char enemy ) {

            if (Dungeon.level.adjacent( pos, enemy.pos )) {

                return super.doAttack( enemy );

            } else {

                if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                    sprite.zap(enemy.pos);
                    return false;
                } else {
                    ventGas(enemy);
                    Sample.INSTANCE.play(Assets.Sounds.GAS);
                    return true;
                }
            }
        }

        //used so resistances can differentiate between melee and magical attacks
        public static class DarkBolt{}

        private static final float TIME_TO_ZAP	= 4f;
        private void zap() {
            spend( TIME_TO_ZAP );

            if (hit( this, enemy, true )) {
                //TODO would be nice for this to work on ghost/statues too
                if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                    Buff.prolong( enemy, Blindness.class, Degrade.DURATION );
                    Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
                }

                int dmg = Random.NormalIntRange( 10, 25 );
                enemy.damage( dmg, new DM300AttackMode.DarkBolt() );

                if (enemy == Dungeon.hero && !enemy.isAlive()) {
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(this, "frost_kill") );
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }
        }

        public void onZapComplete() {
            zap();
            next();
        }

        @Override
        public void call() {
            next();
        }
    }

    public static class DM300DeathBall extends Mob
    {

        public int attackSkill(Char char1)
        {
            return 15;
        }

        public void damage(int i, Object obj)
        {
            super.damage(i, obj);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        }

        public int damageRoll()
        {
            return Random.NormalIntRange(20, 25);
        }

        public void die(Object obj)
        {
            super.die(obj);
            GameScene.bossSlain();
            Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
            Badges.validateBossSlain();
            yell( Messages.get(this, "defeated") );
        }

        public int drRoll()
        {
            return Random.NormalIntRange(0, 8);
        }

        public void move(int i)
        {
            super.move(i);
            if(Dungeon.level.map[i] == 19 && HP < HT)
            {
                HP = HP + Random.Int(1, HT - HP);
                sprite.emitter().burst(ElmoParticle.FACTORY, 5);
                if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                    GLog.n(Messages.get(this, "repair", new Object[0]), new Object[0]);
            }
            int ai[] = new int[8];
            int j = i - 1;
            ai[0] = j;
            int k = i + 1;
            ai[1] = k;
            ai[2] = i - Dungeon.level.width();
            ai[3] = i + Dungeon.level.width();
            ai[4] = j - Dungeon.level.width();
            ai[5] = j + Dungeon.level.width();
            ai[6] = k - Dungeon.level.width();
            ai[7] = k + Dungeon.level.width();
            i = ai[Random.Int(ai.length)];
            if(Dungeon.level.heroFOV[i])
            {
                CellEmitter.get(i).start(Speck.factory(8), 0.07F, 10);
                Camera.main.shake(3F, 0.7F);
                Sample.INSTANCE.play("sound/snd_rocks.mp3");
                if(Dungeon.level.water[i])
                    GameScene.ripple(i);
                else
                if(Dungeon.level.map[i] == 1)
                {
                    Level.set(i, 20);
                    GameScene.updateMap(i);
                }
            }
            Char char1 = Actor.findChar(i);
            if(char1 != null && char1 != this)
                Buff.prolong( char1, Paralysis.class, 2 );
        }

        public void notice()
        {
            super.notice();
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
        }

        public void restoreFromBundle(Bundle bundle)
        {
            super.restoreFromBundle(bundle);
            BossHealthBar.assignBoss(this);
        }

        public DM300DeathBall()
        {
            spriteClass = DM275Sprite.class;
            HT = 1000;
            HP = 1000;
            EXP = 10;
            defenseSkill = 25;
        }
    }

    //国度
    public static class Country extends ToxicGas{

    }

    public static class DM300SpiderMode extends Spinner
    {

        public int attackSkill(Char char1)
        {
            return 26;
        }

        public boolean act()
        {
            GameScene.add(Blob.seed(pos, 30, CorrosiveGas.class));
            return super.act();
        }

        @Override
        public void damage(int dmg, Object src) {
            super.damage(dmg, src);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
        }

        public int damageRoll()
        {
            return Random.NormalIntRange(18, 23);
        }

        public void die(Object obj)
        {
            super.die(obj);
            obj = new DM300DeathBall();
            ((DM300DeathBall) obj).pos = pos;
            GameScene.add(((Mob) (obj)));
            Actor.addDelayed(new Pushing(((Char) (obj)), pos, ((DM300DeathBall) (obj)).pos), -1F);
            yell( Messages.get(this, "defeated") );
        }

        public int drRoll()
        {
            return Random.NormalIntRange(0, 8);
        }

        public void move(int i)
        {
            super.move(i);
            if(Dungeon.level.map[i] == 19 && HP < HT)
            {
                HP = HP + Random.Int(1, HT - HP);
                sprite.emitter().burst(ElmoParticle.FACTORY, 5);
                if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                    GLog.n(Messages.get(this, "repair", new Object[0]), new Object[0]);
            }
            int ai[] = new int[8];
            int j = i - 1;
            ai[0] = j;
            int k = i + 1;
            ai[1] = k;
            ai[2] = i - Dungeon.level.width();
            ai[3] = i + Dungeon.level.width();
            ai[4] = j - Dungeon.level.width();
            ai[5] = j + Dungeon.level.width();
            ai[6] = k - Dungeon.level.width();
            ai[7] = k + Dungeon.level.width();
            i = ai[Random.Int(ai.length)];
            if(Dungeon.level.heroFOV[i])
            {
                CellEmitter.get(i).start(Speck.factory(8), 0.07F, 10);
                Camera.main.shake(3F, 0.7F);
                Sample.INSTANCE.play("sound/snd_rocks.mp3");
                if(Dungeon.level.water[i])
                    GameScene.ripple(i);
                else
                if(Dungeon.level.map[i] == 1)
                {
                    Level.set(i, 20);
                    GameScene.updateMap(i);
                }
            }
            Char char1 = Actor.findChar(i);
            if(char1 != null && char1 != this)
                Buff.prolong( char1, Paralysis.class, 2 );
        }

        public void notice()
        {
            DM920BossLevel level = (DM920BossLevel) Dungeon.level;
            super.notice();
            //Dungeon.level.seal();
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
        }

        public void restoreFromBundle(Bundle bundle)
        {
            super.restoreFromBundle(bundle);
            BossHealthBar.assignBoss(this);
        }

        public DM300SpiderMode ()
        {
            spriteClass = DM300SpiderSprite.class;
            HT = 600;
            HP = 600;
            EXP = 10;
            defenseSkill = 23;
            maxLvl = -1;
        }
    }

    public static class DM75 extends Mob
    {

        public int attackSkill(Char char1)
        {
            return 7;
        }

        @Override
        public void damage(int dmg, Object src) {
            super.damage(dmg, src);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
        }

        public int damageRoll()
        {
            return Random.NormalIntRange(5, 6);
        }

        public void die(Object obj)
        {
            super.die(obj);
            obj = new ArrayList();
            for(int i = 0; i < PathFinder.NEIGHBOURS8.length; i++)
                ((ArrayList) (obj)).add(Integer.valueOf(pos + PathFinder.NEIGHBOURS8[i]));

            if(!Dungeon.level.mobs.isEmpty())
            {
                for(obj = ((ArrayList) (obj)).iterator(); ((Iterator) (obj)).hasNext();)
                {
                    int j = ((Integer)((Iterator) (obj)).next()).intValue();
                    if(Actor.findChar(j) instanceof DM300SpiderMode)
                    {
                        Actor.findChar(j).die(this);
                        obj = new DM300SpiderMode();
                        ((DM300SpiderMode) obj).pos = pos;
                        GameScene.add(((Mob) (obj)));
                        Actor.addDelayed(new Pushing(((Char) (obj)), pos, ((DM300SpiderMode) (obj)).pos), -1F);
                        return;
                    }
                }

                for(obj = Dungeon.level.mobs.iterator(); ((Iterator) (obj)).hasNext(); Actor.findChar(((Mob)((Iterator) (obj)).next()).pos).die(this));
                GameScene.bossSlain();
                Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
                Badges.validateBossSlain();
                LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
                if(obj != null)
                    ((LloydsBeacon) (obj)).upgrade();
                yell( Messages.get(this, "defeated") );
            }
        }

        public int drRoll()
        {
            return Random.NormalIntRange(0, 3);
        }

        public void move(int i)
        {
            super.move(i);

            if (Dungeon.level.map[i] == Terrain.INACTIVE_TRAP && HP < HT) {

                HP += Random.Int( 1, HT - HP );
                sprite.emitter().burst( ElmoParticle.FACTORY, 5 );

                if (Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive()) {
                    GLog.n( Messages.get(this, "repair") );
                }
            }

            if(Dungeon.level.map[i] == 19 && HP < HT)
            {
                HP = HP + Random.Int(1, HT - HP);
                sprite.emitter().burst(ElmoParticle.FACTORY, 1);
                if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                    GLog.n(Messages.get(this, "repair", new Object[0]), new Object[0]);
            }
        }

        public void notice()
        {
            super.notice();
            yell(Messages.get(this, "notice", new Object[0]));
        }

        public DM75()
        {
            spriteClass = DM75Sprite.class;
            HT = 25;
            HP = 25;
            EXP = 7;
            defenseSkill = 4;
            loot = new StoneOfEnchantment();
            lootChance = 0.333F;
        }
    }


    public DM920()
    {
        spriteClass = DM300Sprite.class;

        HP = HT = 280;
        EXP = 30;
        defenseSkill = 18;


        properties.add(Property.BOSS);
        properties.add(Property.INORGANIC);
    }

    public boolean act()
    {
        GameScene.add(Blob.seed(pos, 30, ToxicGas.class));
        return super.act();
    }

    public int attackSkill(Char char1)
    {
        return 20;
    }

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
    }

    public int damageRoll()
    {
        return Random.NormalIntRange(15, 20);
    }

    public void die(Object obj)
    {
        super.die(obj);
        split(pos, enemy);
        //split(pos, enemy);
        int i = 0;
        yell( Messages.get(this, "defeated") );
    }

    public int drRoll()
    {
        return Random.NormalIntRange(0, 8);
    }

    public void move(int i)
    {
        super.move(i);
        if(Dungeon.level.map[i] == 19 && HP < HT)
        {
            HP = HP + Random.Int(1, HT - HP);
            sprite.emitter().burst(ElmoParticle.FACTORY, 5);
            if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                GLog.n(Messages.get(this, "repair", new Object[0]), new Object[0]);
        }
        int ai[] = new int[8];
        int j = i - 1;
        ai[0] = j;
        int k = i + 1;
        ai[1] = k;
        ai[2] = i - Dungeon.level.width();
        ai[3] = i + Dungeon.level.width();
        ai[4] = j - Dungeon.level.width();
        ai[5] = j + Dungeon.level.width();
        ai[6] = k - Dungeon.level.width();
        ai[7] = k + Dungeon.level.width();
        i = ai[Random.Int(ai.length)];
        if(Dungeon.level.heroFOV[i])
        {
            CellEmitter.get(i).start(Speck.factory(8), 0.07F, 10);
            Camera.main.shake(3F, 0.7F);
            Sample.INSTANCE.play("sound/snd_rocks.mp3");
            if(Dungeon.level.water[i])
                GameScene.ripple(i);
            else
            if(Dungeon.level.map[i] == 1)
            {
                Level.set(i, 20);
                GameScene.updateMap(i);
            }
        }
        Char char1 = Actor.findChar(i);
        if(char1 != null && char1 != this)
            Buff.prolong( char1, Paralysis.class, 2 );
    }

    public void notice()
    {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell(Messages.get(this, "notice", new Object[0]));
    }

    public void restoreFromBundle(Bundle bundle)
    {
        super.restoreFromBundle(bundle);
        BossHealthBar.assignBoss(this);
    }

    public void split(int i, Char char1)
    {
        ArrayList arraylist = new ArrayList();
        for(int j = 0; j < PathFinder.NEIGHBOURS8.length; j++)
        {
            int k = PathFinder.NEIGHBOURS8[j] + i;
            if(Actor.findChar(k) == null && (Dungeon.level.passable[k] || Dungeon.level.avoid[k]))
                arraylist.add(Integer.valueOf(k));
        }

        if(arraylist.size() > 0)
        {
            DM300AttackMode dm75 = new   DM300AttackMode();
            dm75.pos = ((Integer)Random.element(arraylist)).intValue();
            GameScene.add(dm75);
            Actor.addDelayed(new Pushing(dm75, i, dm75.pos), -1F);
        }
        Iterator iterator = Dungeon.level.mobs.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Mob mob = (Mob)iterator.next();
            if(mob instanceof DM150)
                mob.aggro(char1);
        } while(true);
    }
}