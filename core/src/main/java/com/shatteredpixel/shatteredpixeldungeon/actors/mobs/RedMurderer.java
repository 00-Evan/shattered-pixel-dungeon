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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MurdererSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class RedMurderer extends Mob {

    public Item item;
    public static int Money = 0;
    {
        spriteClass = MurdererSprite.RedMuderer.class;

        HP = HT = 70;
        defenseSkill = 24;

        EXP = 18;
        maxLvl = 29;

        loot = Random.oneOf(Generator.Category.RING, Generator.Category.ARTIFACT, Generator.Category.FOOD);
        lootChance = 0.03f; //initially, see rollToDropLoot

        WANDERING = new Wandering();
        FLEEING = new Fleeing();

        properties.add(Property.UNDEAD);
    }

    private static final String ITEM = "item";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ITEM, item );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        item = (Item)bundle.get( ITEM );
    }

    @Override
    public float speed() {
        if (item != null) return (5*super.speed())/6;
        else return super.speed();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 20 );
    }

    @Override
    public float attackDelay() {
        return super.attackDelay()*0.7f;
    }

    @Override
    public void rollToDropLoot() {
        if (item != null) {
            Dungeon.level.drop( item, pos ).sprite.drop();
            //updates position
            if (item instanceof Honeypot.ShatteredPot) ((Honeypot.ShatteredPot)item).dropPot( this, pos );
            item = null;
        }
        //each drop makes future drops 1/3 as likely
        // so loot chance looks like: 1/33, 1/100, 1/300, 1/900, etc.
        lootChance *= Math.pow(1/3f, Dungeon.LimitedDrops.THEIF_MISC.count);
        super.rollToDropLoot();
    }

    @Override
    protected Item createLoot() {
        Dungeon.LimitedDrops.THEIF_MISC.count++;
        return super.createLoot();
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 3);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        if (alignment == Alignment.ENEMY && item == null
                && enemy instanceof Hero && steal( (Hero)enemy )) {
            state = FLEEING;
        }

        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (state == FLEEING) {
            Dungeon.level.drop( new Gold(), pos ).sprite.drop();
        }

        return super.defenseProc(enemy, damage);
    }

    protected boolean steal( Hero hero ) {

        Item toSteal = hero.belongings.randomUnequipped();

        if (toSteal != null && !toSteal.unique && toSteal.level() < 1 ) {

            GLog.w( Messages.get(RedMurderer.class, "stole", toSteal.name()) );
            if (!toSteal.stackable) {
                Dungeon.quickslot.convertToPlaceholder(toSteal);
            }
            Item.updateQuickslot();
            TeleportationTrap run = new TeleportationTrap();
            run.pos = super.pos;
            run.activate();
            item = toSteal.detach( hero.belongings.backpack );
            if (item instanceof Honeypot){
                item = ((Honeypot)item).shatter(this, this.pos);
            } else if (item instanceof Honeypot.ShatteredPot) {
                ((Honeypot.ShatteredPot)item).pickupPot(this);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String description() {
        String desc = super.description();

        if (item != null) {
            desc += Messages.get(this, "carries", item.name() );
        }

        return desc;
    }

    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            super.act(enemyInFOV, justAlerted);

            //if an enemy is just noticed and the thief posses an item, run, don't fight.
            if (state == HUNTING && item != null){
                state = FLEEING;
            }

            return true;
        }
    }

    private class Fleeing extends Mob.Fleeing {
        @Override
        protected void nowhereToRun() {
            if (buff( Terror.class ) == null && buff( Corruption.class ) == null) {
                if (enemySeen) {
                    sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Mob.class, "rage"));
                    state = HUNTING;
                } else if (item != null
                        && !Dungeon.level.heroFOV[pos]
                        && Dungeon.level.distance(Dungeon.hero.pos, pos) >= 6) {

                    int count = 24;
                    int newPos;
                    do {
                        newPos = Dungeon.level.randomRespawnCell( RedMurderer.this );
                        if (count-- <= 0) {
                            break;
                        }
                    } while (newPos == -1 || Dungeon.level.heroFOV[newPos] || Dungeon.level.distance(newPos, pos) < (count/3));

                    if (newPos != -1) {

                        if (Dungeon.level.heroFOV[pos]) CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 3);
                        pos = newPos;
                        sprite.place( pos );
                        sprite.visible = Dungeon.level.heroFOV[pos];
                        if (Dungeon.level.heroFOV[pos]) CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 3);

                    }

                    if (item != null){
                        Money++;
                        System.out.println("测试"+Money);
                        GLog.n( Messages.get(RedMurderer.class, "escapes", item.name()));
                    }



                    item = null;
                    state = WANDERING;
                } else {
                    state = WANDERING;
                }
            } else {
                super.nowhereToRun();
            }
        }
    }
}
