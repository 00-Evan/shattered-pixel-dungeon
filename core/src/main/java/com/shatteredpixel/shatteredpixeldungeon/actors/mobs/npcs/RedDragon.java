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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FlameC02;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SkullShaman;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAffection;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfConfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRedDragon;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class RedDragon extends NPC {

    {
        spriteClass = RedDragonSprite.class;
        HP = HT =100;
        flying = true;

        state = WANDERING;
    }

    public RedDragon() {
        super();
        WndRedDragon.rewardObtained = false;
    }


    @Override
    protected boolean act() {
        if (RedDragon.Quest.processed()) {
            target = Dungeon.hero.pos;
        }
        if (Dungeon.level.heroFOV[pos] && !RedDragon.Quest.completed()){
            Notes.add( Notes.Landmark.REDAGRON );
        }
        return super.act();
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public float speed() {
        return Quest.processed() ? 2f : 0.5f;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }



    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );

        //Sample.INSTANCE.play( Assets.Sounds.GHOST );

        if (c != hero){
            return super.interact(c);
        }

        if (Quest.given) {
            if (Quest.weapon != null) {
                if (Quest.processed) {
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndRedDragon(RedDragon.this, Quest.type));
                        }
                    });
                } else {
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            switch (Quest.type) {
                                //豺狼炼药长老
                                //豺狼萨满长老
                                //FLAME-C02
                                case 1:
                                default:
                                    GameScene.show(new WndQuest(RedDragon.this, Messages.get(RedDragon.this,
                                            "gnollshiled_2")));
                                    break;
                                case 2:
                                    GameScene.show(new WndQuest(RedDragon.this, Messages.get(RedDragon.this,
                                            "skullshaman_2")));
                                    break;
                                case 3:
                                    GameScene.show(new WndQuest(RedDragon.this, Messages.get(RedDragon.this,
                                            "flamec01_2")));
                                    break;
                            }
                        }
                    });

                    int newPos = -1;
                    for (int i = 0; i < 10; i++) {
                        int Click = 1;
                        if (3 == 3) {
                            Click = 1 + 10;
                            AlarmTrap Waring = new AlarmTrap();
                            Waring.pos = super.pos;
                            Waring.activate();
                        }
                        newPos = Dungeon.level.randomRespawnCell( this );
                        if (newPos != -1) {
                            break;
                        }
                    }
                    if (newPos != -1) {

                        CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
                        pos = newPos;
                        sprite.place(pos);
                        sprite.visible = Dungeon.level.heroFOV[pos];
                    }
                }
            }
        } else {
            Mob questBoss;
            String txt_quest;

            switch (Quest.type){
                case 1: default:
                    questBoss = new GnollShiled();
                    txt_quest = Messages.get(this, "gnollshiled_1", hero.name()); break;
                case 2:
                    questBoss = new SkullShaman();
                    txt_quest = Messages.get(this, "skullshaman_1", hero.name());
                    break;
                case 3:
                    questBoss = new FlameC02();
                    txt_quest = Messages.get(this, "flamec01_1", hero.name());
                    break;

            }

            questBoss.pos = Dungeon.level.randomRespawnCell( this );

            if (questBoss.pos != -1) {
                GameScene.add(questBoss);
                Quest.given = true;
                Notes.add( Notes.Landmark.REDAGRON );
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show( new WndQuest( RedDragon.this, txt_quest ) );
                    }
                });
            }

        }

        return true;
    }

    public static class Quest {

        private static boolean spawned;

        private static int type;

        private static boolean given;
        private static boolean processed;

        private static int depth;

        public static Ring weapon;
        public static Artifact armor;
        public static Armor food;
        public static ExoticScroll scrolls;

        public static Weapon.Enchantment enchant;
        public static Armor.Glyph glyph;

        public static void reset() {
            spawned = false;

            weapon = null;
            armor = null;
            enchant = null;
            glyph = null;
            food =null;
            scrolls =null;
        }

        private static final String NODE		= "sads";

        private static final String SPAWNED		= "spawned";
        private static final String TYPE        = "type";
        private static final String GIVEN		= "given";
        private static final String PROCESSED	= "processed";
        private static final String DEPTH		= "depth";
        private static final String WEAPON		= "weapon";
        private static final String ARMOR		= "armor";
        private static final String FOOD		= "food";
        private static final String SCROLLS		= "scrolls";
        private static final String ENCHANT		= "enchant";
        private static final String GLYPH		= "glyph";

        public static void storeInBundle( Bundle bundle ) {

            Bundle node = new Bundle();

            node.put( SPAWNED, spawned );

            if (spawned) {

                node.put( TYPE, type );

                node.put( GIVEN, given );
                node.put( DEPTH, depth );
                node.put( PROCESSED, processed );

                node.put( WEAPON, weapon );
                node.put( ARMOR, armor );
                node.put( FOOD, food );
                node.put( SCROLLS, scrolls);

                if (enchant != null) {
                    node.put(ENCHANT, enchant);
                    node.put(GLYPH, glyph);
                }
            }

            bundle.put( NODE, node );
        }

        public static void restoreFromBundle( Bundle bundle ) {

            Bundle node = bundle.getBundle( NODE );

            if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

                type = node.getInt(TYPE);
                given	= node.getBoolean( GIVEN );
                processed = node.getBoolean( PROCESSED );

                depth	= node.getInt( DEPTH );

                weapon	= (Ring)node.get( WEAPON );
                armor	= (Artifact) node.get( ARMOR );
                food	= (Armor) node.get( FOOD );
                scrolls	= (ExoticScroll) node.get( SCROLLS );

                if (node.contains(ENCHANT)) {
                    enchant = (Weapon.Enchantment) node.get(ENCHANT);
                    glyph   = (Armor.Glyph) node.get(GLYPH);
                }
            } else {
                reset();
            }
        }

        public static void spawn( CavesLevel level ) {
            if (!spawned && Dungeon.depth > 11 && Random.Int( 15 - Dungeon.depth) == 0) {

                RedDragon reddragon = new RedDragon();
                do {
                    reddragon.pos = level.randomRespawnCell( reddragon );
                } while (reddragon.pos == -1);
                level.mobs.add( reddragon );

                spawned = true;
                //dungeon depth determines type of quest.d
                //depth12=fetid rat, 13=gnoll trickster, 14=great crab
                type = Dungeon.depth-11;
                //3-1=2

                given = false;
                processed = false;
                depth = Dungeon.depth;
                //5
                switch (Random.chances(new float[]{0, 0, 10, 6, 3, 1})){
                    default:
                    case 2: food = new LeatherArmor(); break;
                    case 3: food = new MailArmor();    break;
                    case 4: food = new ScaleArmor();   break;
                    case 5: food = new PlateArmor();   break;
                }
                //50%:tier2, 30%:tier3, 15%:tier4, 5%:tier5
                armor = (Artifact)Generator.random( Generator.Category.ARTIFACT );

                switch (Random.chances(new float[]{0, 0, 10, 6, 3, 1})){
                    default:
                    case 2: scrolls = new ScrollOfAffection(); break;
                    case 3: scrolls = new ScrollOfAntiMagic();    break;
                    case 4: scrolls = new ScrollOfConfusion();   break;
                    case 5: scrolls = new ScrollOfPsionicBlast();   break;
                }

                //50%:tier2, 30%:tier3, 15%:tier4, 5%:tier5
                int wepTier = Random.chances(new float[]{0, 0, 10, 6, 3, 1});
                Generator.Category c = Generator.wepTiers[wepTier - 1];
                weapon = (Ring)Generator.random( Generator.Category.RING );

                //50%:+0, 30%:+1, 15%:+2, 5%:+3
                float itemLevelRoll = Random.Float();
                int itemLevel;
                if (itemLevelRoll < 0.5f){
                    itemLevel = 0;
                } else if (itemLevelRoll < 0.8f){
                    itemLevel = 1;
                } else if (itemLevelRoll < 0.95f){
                    itemLevel = 2;
                } else {
                    itemLevel = 3;
                }
                weapon.upgrade(itemLevel);
                armor.upgrade(itemLevel);

                //10% to be enchanted. We store it separately so enchant status isn't revealed early
                if (Random.Int(10) == 0){
                    enchant = Weapon.Enchantment.random();
                    glyph = Armor.Glyph.random();
                }

            }
        }

        public static void process() {
            if (spawned && given && !processed && (depth == Dungeon.depth)) {
                GLog.b( Messages.get(RedDragon.class, "find_me") );
                //Sample.INSTANCE.play( Assets.Sounds.GHOST );
                processed = true;
            }
        }

        public static void complete() {
            weapon = null;
            armor = null;

            Notes.remove( Notes.Landmark.REDAGRON );
        }

        public static boolean processed(){
            return spawned && processed;
        }

        public static boolean completed(){
            return processed() && weapon == null && armor == null && food == null && scrolls == null;
        }
    }
}
