package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfNoWater;
import com.shatteredpixel.shatteredpixeldungeon.levels.CaveTwoBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PylonCSSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
public class PylonCS extends Mob {

    {
        spriteClass = PylonCSSprite.class;

        HP = HT = 270;

        maxLvl = 324;

        properties.add(Property.MINIBOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.ELECTRIC);
        properties.add(Property.IMMOVABLE);

        state = PASSIVE;
        alignment = Alignment.NEUTRAL;
    }

    private int targetNeighbor = Random.Int(8);

    @Override
    protected boolean act() {
        spend(TICK);

        Heap heap = Dungeon.level.heaps.get( pos );
        if (heap != null) {
            int n;
            do {
                n = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
            } while (!Dungeon.level.passable[n] && !Dungeon.level.avoid[n]);
            Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
        }

        if (alignment == Alignment.NEUTRAL){
            return true;
        }

        int cell1 = pos + PathFinder.CIRCLE8[targetNeighbor];
        int cell2 = pos + PathFinder.CIRCLE8[(targetNeighbor+4)%8];

        sprite.flash();

        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[cell1] || Dungeon.level.heroFOV[cell2]) {
            sprite.parent.add(new Lightning(DungeonTilemap.raisedTileCenterToWorld(cell1),
                    DungeonTilemap.raisedTileCenterToWorld(cell2), null));
            CellEmitter.get(cell1).burst(SparkParticle.FACTORY, 3);
            CellEmitter.get(cell2).burst(SparkParticle.FACTORY, 3);
            Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
        }

        shockChar(Actor.findChar(cell1));
        shockChar(Actor.findChar(cell2));

        targetNeighbor = (targetNeighbor+1)%8;

        return true;
    }

    private void shockChar( Char ch ){
        if (ch != null && !(ch instanceof NewDM300)){
            ch.sprite.flash();
            ch.damage(Random.NormalIntRange(10, 20), new Electricity());

            if (ch == Dungeon.hero && !ch.isAlive()){
                Dungeon.fail(NewDM300.class);
                GLog.n( Messages.get(Electricity.class, "ondeath") );
            }
        }
    }

    public void activate(){
        alignment = Alignment.ENEMY;
        ((PylonCSSprite) sprite).activate();
    }

    @Override
    public CharSprite sprite() {
        PylonCSSprite p = (PylonCSSprite) super.sprite();
        if (alignment != Alignment.NEUTRAL) p.activate();
        return p;
    }

    @Override
    public void notice() {
        //do nothing
    }

    @Override
    public String description() {
        if (alignment == Alignment.NEUTRAL){
            return Messages.get(this, "desc_inactive");
        } else {
            return Messages.get(this, "desc_active");
        }
    }

    @Override
    public boolean interact(Char c) {
        return true;
    }

    @Override
    public void add(Buff buff) {
        //immune to all buffs/debuffs when inactive
        if (alignment != Alignment.NEUTRAL) {
            super.add(buff);
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        //immune to damage when inactive
        if (alignment == Alignment.NEUTRAL){
            return;
        }
        super.damage(dmg, src);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        ((CaveTwoBossLevel)Dungeon.level).eliminatePylon();

        int blobs = Random.chances(new float[]{4, 5, 6, 5, 4});
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new PotionOfNoWater(), pos + ofs ).sprite.drop( pos );
        }
    }

    private static final String ALIGNMENT = "alignment";
    private static final String TARGET_NEIGHBOUR = "target_neighbour";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ALIGNMENT, alignment);
        bundle.put(TARGET_NEIGHBOUR, targetNeighbor);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        alignment = bundle.getEnum(ALIGNMENT, Alignment.class);
        targetNeighbor = bundle.getInt(TARGET_NEIGHBOUR);
    }

    {
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( ToxicGas.class );
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
    }

}