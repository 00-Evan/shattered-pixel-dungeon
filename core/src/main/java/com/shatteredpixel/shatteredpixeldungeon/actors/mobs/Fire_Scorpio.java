package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireAcidicSprite;
import com.watabou.utils.Random;

public class Fire_Scorpio extends Scorpio{
    {
        spriteClass = FireAcidicSprite.class;

        HP = HT = 80;
        defenseSkill = 24;
        viewDistance = Light.DISTANCE;

        EXP = 14;
        maxLvl = 27;

        loot = Generator.Category.FOOD;
        lootChance = 0.8f;

        properties.add(Property.DEMONIC);
    }

    @Override
    public int attackProc(Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 2 ) == 0) {
            Buff.affect( enemy, Burning.class ).reignite( enemy, 4f );
        }

        return damage;
    }
}

