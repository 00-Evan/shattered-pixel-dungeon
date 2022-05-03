package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RedDragon;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BombGnollTricksterSprites;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GnollShiled extends Gnoll {
    private static final String COMBO = "combo";
    private int combo = 0;

    public GnollShiled() {
        this.spriteClass = BombGnollTricksterSprites.class;
        this.HT = HP = Random.Int(50,210);
        this.defenseSkill = 5;
        this.EXP = 8;
        this.state = this.WANDERING;
        this.loot = AlchemicalCatalyst.class;
        this.lootChance = 0.3f;
        this.properties.add(Char.Property.MINIBOSS);
    }

    public void spawnAround( int pos ) {
        for (int n : PathFinder.NEIGHBOURS4) {
            int cell = pos + n;
            if (Dungeon.level.passable[pos+1] && Actor.findChar( cell ) == null) {
                spawnAt( cell );
            }
        }
    }
    private int level;
    public void adjustStats( int level ) {
        this.level = level;
        defenseSkill = attackSkill( null ) * 5;
        enemySeen = true;
    }

    private static final float SPAWN_DELAY	= 2f;
    public static GnollShiled spawnAt( int pos ) {
        if (!Dungeon.level.solid[pos] && Actor.findChar( pos ) == null) {

            GnollShiled w = new GnollShiled();
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

    public int attackSkill(Char target) {
        return 12;
    }

    protected boolean canAttack(Char enemy) {
        return !Dungeon.level.adjacent(this.pos, enemy.pos) && new Ballistica(this.pos, enemy.pos, 7).collisionPos.intValue() == enemy.pos;
    }

    public int attackProc(Char enemy, int damage) {
        int damage2 = GnollShiled.super.attackProc(enemy, damage);
        this.combo++;
        int effect = Random.Int(4) + this.combo;
        if (effect > 2) {
            switch (Random.Int(3)) {
                case 0:
                    Buff.affect(enemy, Blindness.class).set((float) (effect - 2));
                    break;
                case 1:
                    Buff.affect( enemy, Poison.class ).set( enemy.HT / 10 );
                    break;
                case 2:
                    Buff.affect(enemy, Bleeding.class).set((float) (effect - 2));
                    break;
                default:
                    Buff.affect(enemy, Poison.class).set((float) (effect - 8));
                    break;
            }

        }
        return damage2;
    }

    protected boolean getCloser(int target) {
        this.combo = 0;
        if (this.state != this.HUNTING) {
            return GnollShiled.super.getCloser(target);
        }
        if (!this.enemySeen || !getFurther(target)) {
            return false;
        }
        return true;
    }

    public void die(Object cause) {
        GnollShiled.super.die(cause);
        RedDragon.Quest.process();
        //赋予红龙权限
    }

    public void storeInBundle(Bundle bundle) {
        GnollShiled.super.storeInBundle(bundle);
        bundle.put("combo", this.combo);
    }

    public void restoreFromBundle(Bundle bundle) {
        GnollShiled.super.restoreFromBundle(bundle);
        this.combo = bundle.getInt("combo");
    }
}
