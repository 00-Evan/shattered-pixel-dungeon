package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfGodIce extends DamageWand {

    {
        image = ItemSpriteSheet.DG19;

        collisionProperties = Ballistica.MAGIC_BOLT;
    }

    //1x/2x/3x damage
    public int min(int lvl){
        return (9+lvl) * chargesPerCast();
    }

    //1x/2x/3x damage
    public int max(int lvl){
        return (15*lvl) * chargesPerCast();
    }

    @Override
    protected void onZap(Ballistica beam) {
        affectMap(beam);

        if (Dungeon.level.viewDistance < 6 ){
            if (Dungeon.isChallenged(Challenges.DARKNESS)){
                Buff.prolong( curUser, Chill.class, 2f + buffedLvl());
            } else {
                Buff.prolong( curUser, Chill.class, 10f+buffedLvl()*5);
            }
        }

        Char ch = Actor.findChar(beam.collisionPos);
        if (ch != null){
            processSoulMark(ch, chargesPerCast());
            affectTarget(ch);
        }
    }

    private void affectTarget(Char ch){
        int dmg = damageRoll();

        //three in (5+lvl) chance of failing
        if (Random.Int(5+buffedLvl()) >= 3) {
            Buff.prolong(ch, Chill.class, 2f + (buffedLvl() * 0.333f));
            ch.sprite.emitter().burst(Speck.factory(Speck.STAR), 12 );
        }

        if (ch.properties().contains(Char.Property.BOSS)){
            ch.sprite.emitter().start( ShadowParticle.CURSE, 0.05f, 10+buffedLvl() );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);

            ch.damage(Math.round(dmg*2f), this);
        } else {
            ch.sprite.centerEmitter().burst( RainbowParticle.BURST, 10+buffedLvl() );

            ch.damage(dmg, this);
        }

    }

    private void affectMap(Ballistica beam){
        boolean noticed = false;
        for (int c : beam.subPath(0, beam.dist)){
            if (!Dungeon.level.insideMap(c)){
                continue;
            }
            for (int n : PathFinder.NEIGHBOURS9){
                int cell = c+n;

                if (Dungeon.level.discoverable[cell])
                    Dungeon.level.mapped[cell] = true;

                int terr = Dungeon.level.map[cell];
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                    Dungeon.level.discover( cell );

                    GameScene.discoverTile( cell, terr );
                    ScrollOfMagicMapping.discover(cell);

                    noticed = true;
                }
            }

            CellEmitter.center(c).burst( RainbowParticle.BURST, Random.IntRange( 12, 25 ) );
        }
        if (noticed)
            Sample.INSTANCE.play( Assets.Sounds.SECRET );

        GameScene.updateFog();
    }

    @Override
    protected void fx( Ballistica beam, Callback callback ) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.SHAMAN_PURPLE,
                curUser.sprite,
                beam.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //cripples enemy
        Buff.affect(defender, Bleeding.class).set( 4+staff.buffedLvl());
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( Random.Int( 0x00fffff ) );
        particle.am = 0.5f;
        particle.setLifespan(1f);
        particle.speed.polar(Random.Float(PointF.G2R), 2f);
        particle.setSize( 1f, 2f);
        particle.radiateXY( 0.5f);
    }

}