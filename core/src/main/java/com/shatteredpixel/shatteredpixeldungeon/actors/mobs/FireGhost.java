//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireGhostSprite;
import com.watabou.utils.Random;

public class FireGhost extends Mob {
    public FireGhost() {
        this.spriteClass = FireGhostSprite.class;
        this.HT = 35;
        this.HP = 35;
        this.defenseSkill = 19;
        this.EXP = 6;
        this.maxLvl = 16;
        this.flying = true;
        this.loot = new PotionOfLiquidFlame();
        this.lootChance = 0.1f;
        this.properties.add(Char.Property.FIERY);
    }
    private int combo = 4;
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(0, 10) > 7) {
        }
        int damage2 = FireGhost.super.attackProc(enemy, this.combo + 2);
        if (Dungeon.level.flamable[enemy.pos]) {
            GameScene.add(Blob.seed(enemy.pos, 1, Fire.class));
        }
        if (enemy.buff(Burning.class) == null) {
            Buff.affect(enemy, Burning.class).reignite(enemy);
        }
        return damage2;
    }

    public int attackSkill(Char charR) {
        return 18;
    }

    public int damageRoll() {
        return Random.NormalIntRange(6, 19);
    }

    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }
}