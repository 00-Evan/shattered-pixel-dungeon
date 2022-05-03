/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlueNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RedLunar extends Mob {

    {
        HP =250;
        HT= 250;
        EXP = 20;
        defenseSkill = 12;
        spriteClass = BlueNecromancerSprite.class;
        HUNTING = new RedLunar.Hunting();
        maxLvl=-999;
        flying=true;
        properties.add(Property.BOSS);
        properties.add(Property.FIERY);
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


    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 5, 8 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(5, 2);
    }

    private int delay = 0;

    private boolean canTryToSummon() {

        int ratCount = 0;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){

            if (mob instanceof Rat){
                ratCount++;
            }
        }
        if (ratCount < 3 && delay <= 0) {
            return true;
        } else {
            return false;
        }
    }

    private int combo = 0;

    @Override
    public boolean attack( Char enemy ) {
        if (canAttack(enemy) && canTryToSummon()) {
            return true;
        } else if(canAttack(enemy)){
            spend( attackDelay()*7f );
            return super.attack(enemy);
        }else if(canAttack(enemy)) {
            return super.attack(enemy);
        }
        else return false;
    }

    private boolean chainsUsed = false;
    private boolean chain(int target){
        if (chainsUsed || enemy.properties().contains(Property.IMMOVABLE))
            return false;

        Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

        if (chain.collisionPos != enemy.pos
                || chain.path.size() < 2
                || Dungeon.level.pit[chain.path.get(1)])
            return false;
        else {
            int newPos = -1;
            for (int i : chain.subPath(1, chain.dist)){
                if (!Dungeon.level.solid[i] && Actor.findChar(i) == null){
                    newPos = i;
                    break;
                }
            }

            if (newPos == -1){
                return false;
            } else {
                final int newPosFinal = newPos;
                this.target = newPos;

                if (sprite.visible) {
                    yell(Messages.get(this, "scorpion"));
                    summon();
                    new Item().throwSound();
                    Sample.INSTANCE.play(Assets.Sounds.CHAINS);
                    sprite.parent.add(new Chains(sprite.center(), enemy.sprite.center(), new Callback() {
                        public void call() {
                            Actor.addDelayed(new Pushing(enemy, enemy.pos, newPosFinal, new Callback() {
                                public void call() {
                                    pullEnemy(enemy, newPosFinal);
                                }
                            }), -1);
                            next();
                        }
                    }));
                } else {
                    pullEnemy(enemy, newPos);
                }
            }
        }
        //chainsUsed = true;
        return true;
    }

    private void pullEnemy( Char enemy, int pullPos ){
        enemy.pos = pullPos;
        Dungeon.level.occupyCell(enemy);
        Cripple.prolong(enemy, Cripple.class, 4f);
        if (enemy == hero) {
            hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }

    @Override
    public int attackProc(Char var1, int var2) {
        var2 = super.attackProc(var1, var2 / 2);
        if (Random.Int(2) == 0) {
            ((Bleeding)Buff.affect(var1, Bleeding.class)).set((float)(var2 * 1));
            ((Poison)Buff.affect(var1, Poison.class)).set((float)(var2 * 1));
        }
        return var2;
    }

    private int level;
    public void adjustStats( int level ) {
        this.level = level;
        defenseSkill = attackSkill( null ) * 5;
        enemySeen = true;
    }
    private int summonCooldown = 10;
    private static final float SPAWN_DELAY	= 2f;
    public static RedLunar spawnAt(int pos ) {
        if (!Dungeon.level.solid[pos] && Actor.findChar( pos ) == null) {

            RedLunar w = new RedLunar();
            w.adjustStats( Dungeon.depth );
            w.pos = pos;
            w.state = w.HUNTING;
            GameScene.add( w, SPAWN_DELAY );

            w.sprite.alpha( 0 );
            w.sprite.parent.add( new AlphaTweener( w.sprite, 1, 0.5f ) );

            w.sprite.emitter().burst( ShadowParticle.CURSE, 5 );

            return w;
        } else {
            return null;
        }
    }

    public void spawnAround( int pos ) {
        for (int n : PathFinder.NEIGHBOURS1) {
            int cell = pos + n;

            if (Dungeon.level.passable[pos+1] && Actor.findChar( cell ) == null) {
                spawnAt( cell );
            }
        }
    }

    private void summon() {

        delay = (int) (summonCooldown - (5 - 2 * (float) HP / HT));

        sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
        Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
        for (int i = 0; i <= (1 + (1 - (float) HP / HT)); i++) {
            int newPos;
            do {
                newPos = Random.Int(Dungeon.level.length());
            } while (
                    Dungeon.level.solid[newPos] ||
                            Dungeon.level.distance(newPos, enemy.pos) < 3 ||
                            Actor.findChar(newPos) != null);
            if (Random.Int(1000) <= 500) {
                SRPDICLR  rat = new SRPDICLR();
                rat.state = rat.WANDERING;
                rat.pos = newPos;
                GameScene.add(rat);
                rat.beckon(pos);
            } else if (Random.Int(500) <= 250) {
                MoloHR rat = new MoloHR();
                rat.state = rat.WANDERING;
                rat.pos = newPos;
                GameScene.add(rat);
                rat.beckon(pos);
            } else if (Random.Int(250) <= 125) {
                SkullShaman rat = new SkullShaman();
                rat.state = rat.WANDERING;
                rat.pos = newPos;
                GameScene.add(rat);
                rat.beckon(pos);
            } else if (Random.Int(125) <= 75) {
                SRPDHBLR rat = new SRPDHBLR();
                rat.state = rat.WANDERING;
                rat.pos = newPos;
                GameScene.add(rat);
                rat.beckon(pos);
            } else if (Random.Int(75) <= 37) {
                KagenoNusujin rat = new KagenoNusujin();
                rat.state = rat.WANDERING;
                rat.pos = newPos;
                GameScene.add(rat);
                rat.beckon(pos);
            } else if (Random.Int(37) <= 18) {
                GnollTrickster rat = new GnollTrickster();
                rat.state = rat.WANDERING;
                rat.pos = newPos;
                GameScene.add(rat);
                rat.beckon(pos);
            } else {
                DM200 rat = new DM200();
                rat.state = rat.WANDERING;
                rat.pos = newPos;
                GameScene.add(rat);
                rat.beckon(pos);
            }
        }
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Dungeon.level.unseal();
        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();

        //60% chance of 2 blobs, 30% chance of 3, 10% chance for 4. Average of 2.5
        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (	mob instanceof OldDM300) {
                mob.die( cause );
            }
        }
    }


    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;
            if (!chainsUsed
                    && enemyInFOV
                    && !isCharmedBy( enemy )
                    && !canAttack( enemy )
                    && Dungeon.level.distance( pos, enemy.pos ) < 3
                    && Random.Int(3) == 0

                    && chain(enemy.pos)){
                return !(sprite.visible || enemy.sprite.visible);
            } else {
                return super.act( enemyInFOV, justAlerted );
            }

        }
    }
}
