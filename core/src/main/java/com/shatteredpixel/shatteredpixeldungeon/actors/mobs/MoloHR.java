//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.GooWarn;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class MoloHR extends Mob {
    private static final String COMBO = "combo";
    private String[] attackCurse;
    private int combo;
    private String[] deathCurse;

    public MoloHR() {
        this.spriteClass = MolotovHuntsmanSprite.class;
        this.HT = 180;
        this.HP = 180;
        HUNTING = new Hunting();
        state = HUNTING;
        this.defenseSkill = 10;
        flying = true;
        this.EXP = 15;
        this.baseSpeed = 0.5625F;
        this.deathCurse = new String[]{"！！！"};
        this.attackCurse = new String[]{"就你？还想打我？", "让火焰净化一切！", "那个幽灵太看得起你了！", "我说，为什么要让我承担？",
                "这都是你的错！",
                "扬了你的骨灰！", "啊！", "烧死你"};
        this.combo = 0;
        properties.add(Property.MINIBOSS);
        properties.add(Property.FIERY);
    }

    public int attackProc(Char var1, int var2) {
        byte var3 = 0;
        int var4;
        if (Random.Int(0, 8) > 7) {
            var4 = Random.Int(this.attackCurse.length);
            this.sprite.showStatus(16711680, this.attackCurse[var4], new Object[0]);
        }

        int var5 = super.attackProc(var1, var2);
        var4 = var1.pos;
        CellEmitter.center(var4).burst(BlastParticle.FACTORY, 30);
        GameScene.add(Blob.seed(var4, 4, GooWarn.class));
        int[] var7 = PathFinder.NEIGHBOURS9;
        int var6 = var7.length;

        for(var2 = var3; var2 < var6; ++var2) {
            int var8 = var7[var2];
            if (!Dungeon.level.solid[var4 + var8]) {
                GameScene.add(Blob.seed(var4 + var8, 2, GooWarn.class));
            }
        }

        ++this.combo;
        return var5;
    }

    public int attackSkill(Char var1) {
        return 56;
    }

    protected boolean canAttack(Char var1) {
        Ballistica var2 = new Ballistica(this.pos, var1.pos, 7);
        boolean var3;
        if (!Dungeon.level.adjacent(this.pos, var1.pos) && var2.collisionPos == var1.pos) {
            var3 = true;
        } else {
            var3 = false;
        }

        return var3;
    }

    public int damageRoll() {
        return Random.NormalIntRange(40, 49);
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        Dungeon.level.unseal();
    }

    protected boolean getCloser(int var1) {
        boolean var2 = false;
        this.combo = 0;
        if (this.state == this.HUNTING) {
            boolean var3 = var2;
            if (this.enemySeen) {
                var3 = var2;
                if (this.getFurther(var1)) {
                    var3 = true;
                }
            }

            return var3;
        } else {
            return super.getCloser(var1);
        }
    }

    public void storeInBundle(Bundle var1) {
        super.storeInBundle(var1);
        var1.put("combo", this.combo);
    }

    @Override
    public void notice() {

        super.notice();
    }

    {
        immunities.add( ToxicGas.class );
        immunities.add( Terror.class );
    }
}
