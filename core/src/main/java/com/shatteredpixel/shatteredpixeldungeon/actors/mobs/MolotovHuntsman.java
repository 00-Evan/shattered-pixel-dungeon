//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RedBloodMoon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class MolotovHuntsman extends Mob {
    private static final String COMBO = "combo";
    private String[] attackCurse;
    private int combo;
    private String[] deathCurse;
    public float lifespan;
    public MolotovHuntsman() {
        this.spriteClass = MolotovHuntsmanSprite.class;
        this.HT = 25;
        this.HP = 25;
        this.defenseSkill = 10;
        this.EXP = 15;
        this.state = this.SLEEPING;
        this.baseSpeed = 0.5625F;
        this.loot = new RedBloodMoon();
        this.lootChance = 0.005F;
        this.deathCurse = new String[]{"快停下...", "啊......", "啊.......你这个怪物", "你这怪物...", "神啊，帮帮我吧...", "最后…啊…终于能...见到家人了...", "愚蠢的人类", "为了她..."};
        this.attackCurse = new String[]{"烧起来吧，吃人的野兽！", "让火焰净化一切！", "快滚！", "我说，为什么要让我承担？", "这都是你的错！", "扬了你的骨灰！", "你们，都是贱人！", "去死，你这嗜血的人!", "啊！", "烧死你"};
        this.combo = 0;
    }

    public int attackProc(Char var1, int var2) {
        byte var3 = 0;
        int var4;
        if (Random.Int(0, 10) > 7) {
            var4 = Random.Int(this.attackCurse.length);
            this.sprite.showStatus(16711680, this.attackCurse[var4], new Object[0]);
        }

        int var5 = super.attackProc(var1, var2);
        var4 = var1.pos;
        CellEmitter.center(var4).burst(BlastParticle.FACTORY, 30);
        GameScene.add(Blob.seed(var4, 2, Fire.class));
        int[] var7 = PathFinder.NEIGHBOURS9;
        int var6 = var7.length;

        for(var2 = var3; var2 < var6; ++var2) {
            int var8 = var7[var2];
            if (!Dungeon.level.solid[var4 + var8]) {
                GameScene.add(Blob.seed(var4 + var8, 2, Fire.class));
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
        return Random.NormalIntRange(7, 12);
    }

    public void die(Object var1) {
        super.die(var1);
        if (var1 != Chasm.class) {
            int var2 = Random.Int(this.deathCurse.length);
            this.sprite.showStatus(16711680, this.deathCurse[var2], new Object[0]);
        }

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

    public void restoreFromBundle(Bundle var1) {
        super.restoreFromBundle(var1);
        this.combo = var1.getInt("combo");
    }

    public void storeInBundle(Bundle var1) {
        super.storeInBundle(var1);
        var1.put("combo", this.combo);
    }
}
