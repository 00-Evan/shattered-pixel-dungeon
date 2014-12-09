package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by debenhame on 21/11/2014.
 */
public class DriedRose extends Artifact {

    {
        name = "dried rose";
        image = ItemSpriteSheet.ARTIFACT_ROSE1;
        level = 0;
        levelCap = 10;
        charge = 100;
        chargeCap = 100;
        defaultAction = AC_SUMMON;
    }

    protected boolean talkedTo = false;
    protected boolean firstSummon = false;

    public int droppedPetals = 0;

    public static final String AC_SUMMON = "SUMMON";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && charge == chargeCap)
            actions.add(AC_SUMMON);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action.equals(AC_SUMMON)) {

            ArrayList<Integer> spawnPoints = new ArrayList<Integer>();
            for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
                int p = hero.pos + Level.NEIGHBOURS8[i];
                if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
                    spawnPoints.add( p );
                }
            }

            if (spawnPoints.size() > 0) {
                GhostHero ghost = new GhostHero();
                ghost.pos = Random.element(spawnPoints);


                GameScene.add( ghost, 1f );
                CellEmitter.get(ghost.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

                hero.spend( 1f );
                hero.busy();
                hero.sprite.operate(hero.pos);

            }

        } else{
            super.execute(hero, action);
        }
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new roseRecharge();
    }

    public class roseRecharge extends ArtifactBuff {

    }

    public static class Petal extends Item {

        {
            name = "dried petal";
            stackable = true;
            image = ItemSpriteSheet.PETAL;
        }

        @Override
        public boolean doPickUp( Hero hero ) {
            DriedRose rose = hero.belongings.getItem( DriedRose.class );



            if (rose != null && rose.level < rose.levelCap){
                rose.upgrade();
                if (rose.level == rose.levelCap) {
                    GLog.p("The rose is completed!");
                    Sample.INSTANCE.play( Assets.SND_GHOST );
                    GLog.n("sad ghost: Thank you...");
                } else
                    GLog.i("You add the petal to the rose.");
                hero.spendAndNext(TIME_TO_PICK_UP);
                return true;
            } else {
                GLog.w("You have no rose to add this petal to.");
                return false;
            }
        }

        @Override
        public String info() {
            return "A frail dried up petal, which has somehow survived this far into the dungeon.";
        }

    }

    //TODO: needs to:
    //have combat stats
    //attack only nearby enemies
    //Be tethered to the player
    //Enemies must be able/want to attack it
    //Must be lost on level transition.
    public static class GhostHero extends NPC {

        {
            name = "sad ghost";
            spriteClass = WraithSprite.class;

            flying = true;

            state = WANDERING;
            enemy = DUMMY;

        }

        private static final String TXT_WELCOME = "My spirit is bound to this rose, it was very precious to me, a gift " +
                "from my love whom I left on the surface.\n\nI cannot return to him, but thanks to you I have a second " +
                "chance to complete my journey. When I am able I will respond to your call and fight with you.\n\n" +
                "hopefully you may succeed where I failed...";

        @Override
        public String defenseVerb() {
            return "evaded";
        }

        @Override
        protected boolean getCloser( int target ) {
            if (state == WANDERING)
                this.target = target = Dungeon.hero.pos;
            return super.getCloser( target );
        }

        @Override
        protected Char chooseEnemy() {
            if (enemy == DUMMY || !enemy.isAlive()) {

                HashSet<Mob> enemies = new HashSet<Mob>();
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob.hostile && Level.fieldOfView[mob.pos]) {
                        enemies.add(mob);
                    }
                }
                enemy = enemies.size() > 0 ? Random.element( enemies ) : DUMMY;
            }
            return enemy;
        }

        @Override
        public void damage( int dmg, Object src ) {
        }

        @Override
        public void add( Buff buff ) {
        }

        @Override
        public void interact() {
            //if (!talkedTo){
            //    talkedTo = true;
            //    GameScene.show(new WndQuest(this, TXT_WELCOME));
            //} else {
                int curPos = pos;

                moveSprite( pos, Dungeon.hero.pos );
                move( Dungeon.hero.pos );

                Dungeon.hero.sprite.move( Dungeon.hero.pos, curPos );
                Dungeon.hero.move( curPos );

                Dungeon.hero.spend( 1 / Dungeon.hero.speed() );
                Dungeon.hero.busy();
            //}
        }
    }

}
