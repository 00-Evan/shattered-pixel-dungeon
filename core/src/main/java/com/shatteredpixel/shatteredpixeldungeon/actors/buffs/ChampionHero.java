
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public abstract class ChampionHero extends FlavourBuff {
    public static final float DURATION	= 200f;
    public static final float shopDURATION	= 2000000000f;
    {
        type = buffType.POSITIVE;
    }

    protected int color;

    @Override
    public int icon() {
        return BuffIndicator.UPGRADE;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }
    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(color);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.aura( color );
        else target.sprite.clearAura();
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc",DURATION);
    }


    public void onAttackProc(Char hero ){

    }

    public boolean canAttackWithExtraReach( Char hero ){
        return false;
    }

    public float meleeDamageFactor(){
        return 1f;
    }

    public float damageTakenFactor(){
        return 1f;
    }

    public float evasionAndAccuracyFactor(){
        return 1f;
    }

    {
        immunities.add(Corruption.class);
    }

    public static void rollForChampion(Mob m){
        if (Dungeon.mobsToChampion <= 0) Dungeon.mobsToChampion = 8;

        Dungeon.mobsToChampion--;

        if (Dungeon.mobsToChampion <= 0){
            switch (Random.Int(7)){
                case 0: default:    Buff.affect(m, ChampionHero.Blazing.class);      break;
                case 1:             Buff.affect(m, ChampionHero.Projecting.class);   break;
                case 2:             Buff.affect(m, ChampionHero.AntiMagic.class);    break;
                case 3:             Buff.affect(m, ChampionHero.Giant.class);        break;
                case 4:             Buff.affect(m, ChampionHero.Blessed.class);      break;
                case 5:             Buff.affect(m, ChampionHero.Growing.class);      break;
                case 6:             Buff.affect(m, ChampionHero.Halo.class);      break;
            }
            m.state = m.WANDERING;
        }
    }

    public static class Blazing extends ChampionHero {

        {
            color = 0xFF8800;
        }
        @Override
        public void fx(boolean on) {
            //
        }
        @Override
        public void onAttackProc(Char hero) {
            Buff.affect(hero, Burning.class).reignite(hero);
        }

        @Override
        public float meleeDamageFactor() {
            return 1.25f;
        }

        {
            immunities.add(Burning.class);
        }
    }

    public static class Halo extends ChampionHero {
        @Override
        public void fx(boolean on) {
            //
        }
        {
            color = 0x00FFFF;
        }

        @Override
        public void onAttackProc(Char hero) {
            Buff.affect(hero, HalomethaneBurning.class).reignite(hero);
        }

        @Override
        public float meleeDamageFactor() {
            return 1.55f;
        }

        {
            immunities.add(HalomethaneBurning.class);
            immunities.add(Burning.class);
        }
    }

    public static class Projecting extends ChampionHero {
        @Override
        public void fx(boolean on) {
            //
        }
        {
            color = 0x8800FF;
        }

        @Override
        public float meleeDamageFactor() {
            return 1.25f;
        }

        @Override
        public boolean canAttackWithExtraReach( Char hero ) {
            return target.fieldOfView[hero.pos]; //if it can see it, it can attack it.
        }
    }

    public static class AntiMagic extends ChampionHero {
        @Override
        public void fx(boolean on) {
            //
        }
        {
            color = 0x00FF00;
        }

        @Override
        public float damageTakenFactor() {
            return 0.75f;
        }

        {
            immunities.addAll(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic.RESISTS);
        }

    }

    //Also makes target large, see Char.properties()
    public static class Giant extends ChampionHero {
        @Override
        public void fx(boolean on) {
            //
        }
        {
            color = 0x0088FF;
        }

        @Override
        public float damageTakenFactor() {
            return 0.25f;
        }

        @Override
        public boolean canAttackWithExtraReach(Char hero) {
            //attack range of 2
            return target.fieldOfView[hero.pos] && Dungeon.level.distance(target.pos, hero.pos) <= 2;
        }
    }

    public static class Blessed extends ChampionHero {
        @Override
        public void fx(boolean on) {
            //
        }
        {
            color = 0xFFFF00;
        }

        @Override
        public float evasionAndAccuracyFactor() {
            return 3f;
        }
    }

    public static class Growing extends ChampionHero {
        @Override
        public void fx(boolean on) {
            //
        }
        {
            color = 0xFF0000;
        }

        private float multiplier = 1.09f;

        @Override
        public boolean act() {
            detach();
            multiplier += 0.01f;
            spend(3*TICK);
            return true;
        }

        @Override
        public float meleeDamageFactor() {
            return multiplier;
        }

        @Override
        public float damageTakenFactor() {
            return 1f/multiplier;
        }

        @Override
        public float evasionAndAccuracyFactor() {

            return multiplier;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", (int)(100*(multiplier-1)), (int)(100*(1 - 1f/multiplier)));
        }
    }

}
