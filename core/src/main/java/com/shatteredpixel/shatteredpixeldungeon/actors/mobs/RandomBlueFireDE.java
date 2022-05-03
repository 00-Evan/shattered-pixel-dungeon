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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireBallMobSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

public class RandomBlueFireDE extends Mob {
    private static final String COMBO = "combo";
    private String[] attackCurse = {"……","不能违背神的意志","无知者无罪"};
    private int combo = 0;
    private String[] deathCurse = {"没有任何的悲哀可以给我，你彻底激怒我了！"};
    public RandomBlueFireDE() {
        this.spriteClass = FireBallMobSprite.class;
        this.HT = 200;
        this.HP = 200;
        this.defenseSkill = 4;
        this.EXP = 7;
        this.maxLvl = 15;
        this.state = this.SLEEPING;
    }

    @Override
    public boolean act() {

        if (Dungeon.level.water[pos] && HP < HT) {
            if (Dungeon.level.heroFOV[pos] ){
                sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
            }

            HP++;
            Music.INSTANCE.play(Assets.BGM_BOSSA, true);
        }

        if (state != SLEEPING){
            Dungeon.level.seal();
        }

        return super.act();
    }

    @Override
	public void damage( int dmg, Object src ) {
		//半血狂暴
		super.damage( dmg, src );
		if (HP <= HT / 2) {
			destroy();
			Mob mob = new RandomBlueFire();
			mob.HP = mob.HT / 2;
			mob.pos = pos;
			GameScene.add(mob);
            new FlameC01().spawnAround(pos);
            Music.INSTANCE.play(Assets.BGM_BOSSD, true);
            GLog.n( Messages.get(this, "fuck") );
		}
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 2 );
    }

    public int attackProc(Char enemy, int damage) {
        if (Random.Int(0, 10) > 7) {
            this.sprite.showStatus(16711680, this.attackCurse[Random.Int(this.attackCurse.length)], new Object[0]);
        }
        int damage2 = RandomBlueFireDE.super.attackProc(enemy, this.combo + damage);
        this.combo++;
        return damage2;
    }

    public void die(Object cause) {
        RandomBlueFireDE.super.die(cause);
        if (cause != Chasm.class) {
            this.sprite.showStatus(16711680, this.deathCurse[Random.Int(this.deathCurse.length)], new Object[0]);

        }
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }
}

