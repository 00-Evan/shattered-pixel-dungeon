/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NxhyNpc;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.REN;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Slyl;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.obSir;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;

public class ZeroLevel extends Level {
    private static final int SIZE = 5;
    private static final int[] pre_map = {190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 161, 4, 4, 4, 0, 0, 4, 0, 0, 0, 2, 0, 161, 190, 0, 0, 0, 0, 0, 0, 0, 190, 161, 0, 2, 0, 0, 0, 0, 4, 0, 0, 0, 0, 161, 190, 190, 0, 4, 16, 4, 0, 0, 4, 0, 0, 0, 2, 0, 0, 190, 0, 0, 0, 0, 0, 0, 0, 190, 0, 2, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 0, 4, 0, 4, 0, 0, 4, 0, 2, 0, 2, 0, 2, 190, 0, 0, 4, 4, 0, 0, 0, 190, 2, 2, 2, 2, 2, 2, 2, 4, 0, 0, 0, 0, 0, 190, 190, 0, 4, 0, 4, 0, 0, 4, 0, 0, 2, 2, 2, 0, 190, 0, 4, 0, 1, 4, 0, 0, 190, 0, 2, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 0, 4, 0, 4, 0, 0, 4, 0, 0, 0, 2, 0, 0, 190, 0, 4, 2, 3, 4, 0, 0, 190, 0, 0, 2, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 0, 4, 0, 4, 0, 0, 4, 4, 4, 4, 4, 4, 4, 80, 4, 4, 4, 4, 4, 4, 4, 80, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 190, 190, 0, 4, 0, 4, 0, 0, 4, 0, 1, 4, 0, 0, 0, 190, 0, 4, 0, 1, 4, 0, 0, 190, 0, 0, 0, 4, 0, 0, 0, 1, 4, 4, 4, 4, 4, 190, 190, 0, 4, 4, 4, 0, 0, 4, 2, 3, 4, 0, 0, 0, 190, 0, 4, 2, 3, 4, 0, 0, 190, 0, 0, 0, 4, 0, 1, 1, 1, 4, 0, 0, 0, 0, 190, 190, 4, 4, 4, 2, 4, 4, 4, 4, 4, 4, 0, 0, 0, 190, 0, 0, 4, 4, 0, 0, 0, 190, 0, 0, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 190, 190, 0, 0, 0, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 190, 0, 0, 0, 4, 0, 0, 0, 190, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 2, 2, 2, 2, 2, 2, 4, 0, 0, 0, 0, 0, 0, 190, 0, 0, 4, 0, 4, 0, 0, 190, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 0, 0, 0, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 190, 0, 4, 0, 4, 0, 4, 0, 190, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 161, 0, 0, 2, 0, 0, 4, 0, 0, 0, 0, 0, 161, 190, 4, 0, 0, 0, 0, 0, 4, 190, 161, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 161, 190, 190, 190, 190, 190, 190, 190, 190, 80, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 80, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 80, 190, 190, 190, 190, 190, 190, 190, 0, 0, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 4, 190, 123, 123, 123, 123, 123, 123, 123, 190, 4, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 0, 0, 4, 0, 1, 4, 4, 0, 0, 0, 0, 4, 0, 190, 123, 98, 4, 10, 4, 98, 123, 190, 0, 4, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 0, 0, 4, 2, 3, 4, 4, 0, 0, 0, 4, 0, 0, 190, 123, 4, 0, 85, 0, 4, 123, 190, 0, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4, 0, 80, 123, 10, 85, 17, 85, 10, 123, 80, 0, 4, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 190, 190, 0, 1, 4, 0, 0, 0, 4, 0, 0, 0, 4, 0, 0, 190, 123, 4, 0, 85, 0, 4, 123, 190, 0, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 2, 3, 4, 0, 0, 0, 4, 0, 0, 0, 0, 4, 0, 190, 123, 98, 4, 10, 4, 98, 123, 190, 0, 4, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 4, 4, 4, 0, 0, 0, 4, 0, 0, 0, 0, 0, 4, 190, 123, 123, 123, 123, 123, 123, 123, 190, 4, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 190, 190, 190, 190, 190, 190, 80, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 80, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 80, 190, 190, 190, 190, 190, 190, 190, 124, 124, 124, 124, 124, 124, 4, 124, 124, 124, 124, 124, 124, 190, 4, 0, 0, 0, 0, 0, 4, 190, 161, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 161, 190, 190, 124, 124, 124, 124, 124, 124, 4, 124, 124, 124, 124, 124, 124, 190, 0, 4, 0, 4, 0, 4, 0, 190, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 124, 124, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 190, 0, 0, 4, 0, 4, 0, 0, 190, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 0, 0, 190, 190, 124, 124, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 190, 0, 0, 0, 4, 0, 0, 0, 190, 0, 0, 0, 0, 4, 0, 1, 4, 0, 1, 4, 0, 0, 190, 190, 124, 124, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 190, 0, 0, 0, 4, 0, 0, 0, 190, 0, 0, 0, 0, 4, 2, 3, 4, 2, 3, 4, 0, 0, 190, 190, 124, 124, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 80, 4, 4, 4, 4, 4, 4, 4, 80, 4, 4, 4, 4, 4, 4, 4, 19, 4, 4, 4, 4, 4, 190, 190, 124, 124, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 190, 0, 0, 0, 4, 0, 0, 0, 190, 0, 0, 2, 0, 4, 0, 1, 4, 0, 1, 4, 0, 0, 190, 190, 124, 124, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 190, 4, 4, 4, 4, 4, 4, 4, 190, 0, 0, 2, 0, 4, 2, 3, 4, 2, 3, 4, 0, 0, 190, 190, 124, 124, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 190, 4, 0, 1, 4, 0, 1, 4, 190, 0, 0, 2, 0, 4, 4, 4, 4, 4, 4, 4, 0, 0, 190, 190, 124, 124, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 190, 4, 2, 3, 4, 2, 3, 4, 190, 0, 0, 2, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 124, 124, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 190, 4, 4, 4, 4, 4, 4, 4, 190, 2, 0, 2, 0, 2, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 190, 0, 0, 0, 4, 0, 0, 0, 190, 0, 2, 2, 2, 0, 0, 0, 4, 0, 0, 0, 0, 0, 190, 190, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 190, 0, 0, 0, 4, 0, 0, 0, 190, 161, 0, 2, 0, 0, 0, 0, 4, 0, 0, 0, 0, 161, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190, 190};

    {
        color1 = 5459774;
        color2 = 12179041;
    }

    public ZeroLevel() {
        Dungeon.isChallenged(32);
        this.viewDistance = 15;
    }

    private int mapToTerrain(int var1) {
        if (var1 == 1 || var1 == 2 || var1 == 3) {
            return 29;
        }
        if (var1 != 4) {
            if (var1 == 16) {
                return 7;
            }
            if (var1 == 17) {
                return 8;
            }
            switch (var1) {
                case -2147483644:
                    break;
                case -2147483584:
                case 64:
                case 190:
                    return 4;
                case -2147483550:
                case 98:
                    return 25;
                case -2147483524:
                case 124:
                case 140:
                    return 27;
                case 4:
                    return 14;
                case 69:
                    return 12;
                case 80:
                    return 5;
                case 85:
                    return 11;
                case 96:
                    return 23;
                case 120:
                    return 20;
                case 123:
                    return 29;
                case 161:
                    return 12;
                default:
                    return 1;
            }
        }
        return 14;
    }

    protected boolean build() {
        setSize(37, 37);
        this.exit = (this.width * 18 + 18);
        this.entrance = (this.width * 2) + 3;
        for (int var1 = 0; var1 < this.map.length; var1++) {
            this.map[var1] = mapToTerrain(pre_map[var1]);
        }
        return true;
    }

    protected void createItems() {
        drop( ( Generator.randomUsingDefaults( Generator.Category.POTION ) ), this.width * 17 + 16 );
        drop( ( Generator.randomUsingDefaults( Generator.Category.POTION ) ), this.width * 16 + 17 );

        drop( ( Generator.randomUsingDefaults( Generator.Category.SCROLL ) ), this.width * 20 + 17 );
        drop( ( Generator.randomUsingDefaults( Generator.Category.SCROLL ) ), this.width * 19 + 16 );


        drop( new Ankh(), this.width * 17 + 20  );
        drop( new Stylus(), this.width * 19 + 20  );

        drop( ( Generator.randomUsingDefaults( Generator.Category.STONE ) ), this.width * 16 + 19 );
        drop( ( Generator.randomUsingDefaults( Generator.Category.FOOD ) ), this.width * 20 + 19 );

        if ( Badges.isUnlocked(Badges.Badge.GODD_MAKE)){
            drop( ( Generator.randomUsingDefaults( Generator.Category.RING ) ), this.width * 17 + 18 );
        }

        if ( Badges.isUnlocked(Badges.Badge.BIG_X)){
            drop( ( Generator.randomUsingDefaults( Generator.Category.ARMOR ) ), this.width * 19 + 18 );
        }
        if ( Badges.isUnlocked(Badges.Badge.KILL_SLMKING)||Badges.isUnlocked(Badges.Badge.KILL_MG)  ){
            drop(new Ankh(), this.width * 18 + 17  );
        }
        if ( Badges.isUnlocked(Badges.Badge.RLPT)){
            drop( ( Generator.randomUsingDefaults( Generator.Category.ARTIFACT ) ), this.width * 18 + 19 );
        }

        //旧版支离破碎的神器获取方案已经不再使用
    }

    public Mob createMob() {
        return null;
    }

    protected void createMobs() {
        REN n = new REN();
        n.pos = (this.width * 18 + 16);
        mobs.add(n);

        Slyl npc1 = new Slyl();
        npc1.pos = (this.width * 16 + 18);
        mobs.add(npc1);

        obSir npc2= new obSir();
        npc2.pos = (this.width * 20 + 18);
        mobs.add(npc2);

        NxhyNpc npc3= new NxhyNpc();
        npc3.pos = (this.width * 18 + 20);
        mobs.add(npc3);



        if ( Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
            Nyz npc4= new Nyz();
            npc4.pos = (this.width * 28 + 7);
            mobs.add(npc4);
       }
    }

    public int randomRespawnCell() {
        return this.entrance - width();
    }

    public Actor respawner() {
        return null;
    }

    public String tilesTex() {
        return Assets.Environment.TILES_SEWERS;
    }

    public String waterTex() {
        return Assets.Environment.WATER_COLD;
    }

}
