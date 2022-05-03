package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpectralNecromancerSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class SpectralNecromancer extends Necromancer {

    {
        HP = HT = 80;
        spriteClass = SpectralNecromancerSprite.class;
    }

    private ArrayList<Integer> wraithIDs = new ArrayList<>();

    @Override
    protected boolean act() {
        if (summoning && state != HUNTING){
            summoning = false;
            if (sprite instanceof SpectralNecromancerSprite) {
                ((SpectralNecromancerSprite) sprite).cancelSummoning();
            }
        }
        return super.act();
    }

    @Override
    public void rollToDropLoot() {
        if (Dungeon.hero.lvl > maxLvl + 2) return;

        super.rollToDropLoot();

        int ofs;
        do {
            ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (Dungeon.level.solid[pos + ofs] && !Dungeon.level.passable[pos + ofs]);
        Dungeon.level.drop( new ScrollOfRemoveCurse(), pos + ofs ).sprite.drop( pos );
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (	mob instanceof FireGhostDead||
                    mob instanceof FireGhost) {
                mob.die( cause );
            }
        }
    }

    private static final String WRAITH_IDS = "wraith_ids";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        int[] wraithIDArr = new int[wraithIDs.size()];
        int i = 0; for (Integer val : wraithIDs){ wraithIDArr[i] = val; i++; }
        bundle.put(WRAITH_IDS, wraithIDArr);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        Collections.addAll(wraithIDs, bundle.getInt(WRAITH_IDS));
    }

    @Override
    public void summonMinion() {
        if (Actor.findChar(summoningPos) != null) {
            int pushPos = pos;
            for (int c : PathFinder.NEIGHBOURS8) {
                if (Actor.findChar(summoningPos + c) == null
                        && Dungeon.level.passable[summoningPos + c]
                        && (Dungeon.level.openSpace[summoningPos + c] || !hasProp(Actor.findChar(summoningPos), Property.LARGE))
                        && Dungeon.level.trueDistance(pos, summoningPos + c) > Dungeon.level.trueDistance(pos, pushPos)) {
                    pushPos = summoningPos + c;
                }
            }

            //push enemy, or wait a turn if there is no valid pushing position
            if (pushPos != pos) {
                Char ch = Actor.findChar(summoningPos);
                Actor.addDelayed( new Pushing( ch, ch.pos, pushPos ), -1 );

                ch.pos = pushPos;
                Dungeon.level.occupyCell(ch );

            } else {

                Char blocker = Actor.findChar(summoningPos);
                if (blocker.alignment != alignment){
                    blocker.damage( Random.NormalIntRange(2, 10), this );
                }

                spend(TICK);
                return;
            }
        }

        summoning = firstSummon = false;

        FireGhostDead wraith = FireGhostDead.spawnAt(summoningPos);

        if (buff(Corruption.class) != null){
            Buff.affect(wraith, Corruption.class);
        }
        for (Buff b : buffs(ChampionEnemy.class)){
            Buff.affect( wraith, b.getClass());
        }
    }
}
