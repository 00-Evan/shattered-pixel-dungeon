package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RedDragon;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkullShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class SkullShaman extends Mob implements Callback {
    private static final float TIME_TO_ZAP = 1.0F;

    public SkullShaman() {
        this.spriteClass = SkullShamanSprite.class;
        int var1 = Dungeon.depth * Random.NormalIntRange(1, 3) + 80;
        this.HT = var1;
        this.HP = var1;
        this.defenseSkill = 8;
        this.EXP = 6;
        this.maxLvl = 19;
        state = PASSIVE;
        this.properties.add(Property.ELECTRIC);
    }

    public int attackProc(Char var1, int var2) {
        var2 = super.attackProc(var1, var2);
        if (var1 instanceof Hero) {
            ((Hero)var1).damageSanity(3);
        }

        int var3 = Random.Int(4) + 5;
        if (var3 > 2) {
            if (var3 >= 6 && var1.buff(Burning.class) == null) {
                if (Dungeon.level.flamable[var1.pos]) {
                    GameScene.add(Blob.seed(var1.pos, 4, Fire.class));
                }

                ((Burning) Buff.affect(var1, Burning.class)).reignite(var1);
            } else {
                ((Poison)Buff.affect(var1, Poison.class)).set((float)(var3 - 2));
            }
        }

        return var2;
    }

    public int attackSkill(Char var1) {
        return 22;
    }

    public void call() {
        this.next();
    }

    protected boolean canAttack(Char var1) {
        boolean var2;
        if ((new Ballistica(this.pos, var1.pos, 6)).collisionPos == var1.pos) {
            var2 = true;
        } else {
            var2 = false;
        }

        return var2;
    }

    public void die(Object cause) {
        SkullShaman.super.die(cause);
        RedDragon.Quest.process();
        //赋予红龙权限
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos]) {
            Notes.add( Notes.Landmark.STATUE );
        }
        return super.act();
    }

    @Override
    public void destroy() {
        Notes.remove( Notes.Landmark.STATUE );
        super.destroy();
    }

    public int damageRoll() {
        return Random.NormalIntRange(8, 24);
    }

    protected boolean doAttack(Char var1) {
        int var2 = Dungeon.level.distance(this.pos, var1.pos);
        boolean var3 = true;
        if (var2 <= 1) {
            return super.doAttack(var1);
        } else {
            boolean var6;
            if (!this.fieldOfView[this.pos] && !this.fieldOfView[var1.pos]) {
                var6 = false;
            } else {
                var6 = true;
            }

            if (var6) {
                this.sprite.zap(var1.pos);
            }

            this.spend(1.0F);
            if (hit(this, var1, true)) {
                int var4 = Random.NormalIntRange(3, 10);
                int var5 = var4;
                if (Dungeon.level.water[var1.pos]) {
                    var5 = var4;
                    if (!var1.flying) {
                        var5 = (int)((float)var4 * 1.5F);
                    }
                }

                var1.damage(var5, this);
                var1.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                var1.sprite.flash();
                if (var1 == Dungeon.hero) {
                    Camera.main.shake(2.0F, 0.3F);
                    if (!var1.isAlive()) {
                        Dungeon.fail(this.getClass());
                        GLog.n(Messages.get(this, "zap_kill", new Object[0]), new Object[0]);
                    }
                }
            } else {
                var1.sprite.showStatus(16776960, var1.defenseVerb(), new Object[0]);
            }

            if (var6) {
                var3 = false;
            }

            return var3;
        }
    }

    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    public void damage( int dmg, Object src ) {

        if (state == PASSIVE) {
            state = HUNTING;
        }

        super.damage( dmg, src );
    }
}
