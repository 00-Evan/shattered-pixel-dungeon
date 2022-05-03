package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GoldBAo;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DimandMimic extends Mimic {
    private boolean chainsUsed = false;
    {
        spriteClass = MimicSprite.Dimand.class;
        loot = GoldBAo.class;
        lootChance = 1f;
        properties.add( Property.ICY );
    }

    public DimandMimic() {
        super();

        HP = HT = 150;
        defenseSkill = 10;

        HUNTING = new DimandMimic.Hunting();
    }

    @Override
    public String name() {
        if (alignment == Alignment.NEUTRAL){
            return Messages.get(Heap.class, "locked_chest");
        } else {
            return super.name();
        }
    }
    public boolean supercharged = false;
    @Override
    public void move(int step) {
        super.move(step);

        Camera.main.shake( supercharged ? 4 : 1, 0.75f );

        if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && state == HUNTING) {

            //don't gain energy from cells that are energized
            if (NewCavesBossLevel.PylonEnergy.volumeAt(pos, NewCavesBossLevel.PylonEnergy.class) > 0){
                return;
            }

            if (Dungeon.level.heroFOV[step]) {
                if (buff(Barrier.class) == null) {
                    yell(Messages.get(this, "shield"));
                }
                Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
                sprite.emitter().start(SparkParticle.STATIC, 0.05f, 6);
            }

            if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP < HT) {

                HP += Random.Int( 2, HT - HP );
                sprite.emitter().burst( ElmoParticle.FACTORY, 5 );

                if (Dungeon.level.heroFOV[step] && Dungeon.hero.isAlive()) {
                    GLog.n(Messages.get(this, "repair"));
                }
            }

            Buff.affect(this, Barrier.class).setShield( 10 + (HT - HP)/10);

        }
    }

    @Override
    public String description() {
        if (alignment == Alignment.NEUTRAL){
            return Messages.get(Heap.class, "locked_chest_desc") + "\n\n" + Messages.get(this, "hidden_hint");
        } else {
            return super.description();
        }
    }

    public void stopHiding(){
        state = HUNTING;
        if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
            enemy = Dungeon.hero;
            target = Dungeon.hero.pos;
            enemySeen = true;
            GLog.w(Messages.get(this, "reveal") );
            CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
            Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 0.85f);
        }
    }

    @Override
    public int attackProc(Char var1, int var2) {
        var2 = super.attackProc(var1, var2 / 2);
        if (Random.Int(2) == 0) {
            ((Bleeding)Buff.affect(var1, Bleeding.class)).set((float)(var2 * 1));
            ((Weakness)Buff.affect(var1, Weakness.class)).set((float)(var2 * 1));
        }

        return var2;
    }

    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;

            if (!chainsUsed
                    && enemyInFOV
                    && !isCharmedBy( enemy )
                    && !canAttack( enemy )
                    && Dungeon.level.distance( pos, enemy.pos ) < 5
                    && Random.Int(3) == 0

                    && chain(enemy.pos)){
                return false;
            } else {
                return super.act( enemyInFOV, justAlerted );
            }

        }
    }

    private boolean chain(int target){
        if (chainsUsed || enemy.properties().contains(Property.IMMOVABLE))
            return false;

        Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

        if (chain.collisionPos != enemy.pos
                || chain.path.size() < 2
                || Dungeon.level.pit[chain.path.get(1)])
            return false;
        else {
            int newPos = -1;
            for (int i : chain.subPath(1, chain.dist)){
                if (!Dungeon.level.solid[i] && Actor.findChar(i) == null){
                    newPos = i;
                    break;
                }
            }

            if (newPos == -8){
                return false;
            } else {
                final int newPosFinal = newPos;
                this.target = newPos;
                yell( Messages.get(this, "scorpion") );
                new Item().throwSound();
                Sample.INSTANCE.play( Assets.Sounds.CHAINS );
                sprite.parent.add(new Chains(sprite.center(), enemy.sprite.center(), new Callback() {
                    public void call() {
                        Actor.addDelayed(new Pushing(enemy, enemy.pos, newPosFinal, new Callback(){
                            public void call() {
                                enemy.pos = newPosFinal;
                                Dungeon.level.occupyCell(enemy);
                                Cripple.prolong(enemy, Cripple.class, 4f);
                                if (enemy == Dungeon.hero) {
                                    Dungeon.hero.interrupt();
                                    Dungeon.observe();
                                    GameScene.updateFog();
                                }
                            }
                        }), -1);
                        next();
                    }
                }));
            }
        }
        chainsUsed = true;
        return true;
    }

    @Override
    public void setLevel(int level) {
        super.setLevel(Math.round(level*1.33f));
    }

    private static Char throwingChar;
    public static boolean throwBomb(final Char thrower, final Char target){

        int targetCell = -1;

        //Targets closest cell which is adjacent to target, and at least 3 tiles away
        for (int i : PathFinder.NEIGHBOURS8){
            int cell = target.pos + i;
            if (Dungeon.level.distance(cell, thrower.pos) >= 3 && !Dungeon.level.solid[cell]){
                if (targetCell == -1 ||
                        Dungeon.level.trueDistance(cell, thrower.pos) < Dungeon.level.trueDistance(targetCell, thrower.pos)){
                    targetCell = cell;
                }
            }
        }

        if (targetCell == -1){
            return false;
        }

        final int finalTargetCell = targetCell;
        throwingChar = thrower;
        final NewTengu.BombAbility.BombItem item = new NewTengu.BombAbility.BombItem();
        thrower.sprite.zap(finalTargetCell);
        ((MissileSprite) thrower.sprite.parent.recycle(MissileSprite.class)).
                reset(thrower.sprite,
                        finalTargetCell,
                        item,
                        new Callback() {
                            @Override
                            public void call() {
                                item.onThrow(finalTargetCell);
                                thrower.next();
                            }
                        });
        return true;
    }

    @Override
    protected void generatePrize() {
        super.generatePrize();
        //all existing prize items are guaranteed uncursed, and have a 50% chance to be +1 if they were +0
        for (Item i : items){
            if (i instanceof EquipableItem || i instanceof Wand){
                i.cursed = false;
                i.cursedKnown = true;
                if (i instanceof Weapon && ((Weapon) i).hasCurseEnchant()){
                    ((Weapon) i).enchant(null);
                }
                if (i instanceof Armor && ((Armor) i).hasCurseGlyph()){
                    ((Armor) i).inscribe(null);
                }
                if (!(i instanceof MissileWeapon) && i.level() == 6 && Random.Int(4) == 0){
                    i.upgrade();
                }
            }
        }
    }
}

