package com.shatteredpixel.shatteredpixeldungeon.ai;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PlatformSupport;

/**
 * Inject programmable hero actions into the standard ShatteredPixelDungeon Game class. Future updates will make the
 * Hero agent parameterized so we can plug in different agents.
 */
public class AIPixelDungeon extends ShatteredPixelDungeon {
    public AIPixelDungeon(PlatformSupport platform) {
        super(platform);
        GamesInProgress.selectedClass = HeroClass.WARRIOR;
        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
        switchScene(InterlevelScene.class);

    }

    @Override
    public void update() {
        super.update();
        if (Dungeon.hero != null && Dungeon.hero.isAlive() && Dungeon.hero.ready && scene.active && scene.alive && scene.getClass().equals(GameScene.class) && Dungeon.hero.curAction == null && Dungeon.level != null) {
            HeroAction action = act();
            if (action != null) {
                Dungeon.hero.curAction = action;
                // stole this from CellSelector.moveFromActions function
                // I don't understand yet how calling next() progresses the game logic, but it seems to do the trick ¯\_(ツ)_/¯
                Dungeon.hero.next();
            }
        }

    }

    /**
     * Modify this code to make the AI hero do whatever you want. If it returns any subclass of HeroAction, it'll
     * perform that action. Otherwise does nothing. I don't know how robust this is, so give it a try and report any
     * errors found
     */
    protected HeroAction act() {
        // move towards the closest unexplored accessible cell
        int closestCell = 999999999;
        // iterate through the visited array to find cells we haven't seen yet
        // (I don't really know what "visited" means -- just that it's been in our POV before? We've walked on it?
        for (int target = 0; target < Dungeon.level.visited.length && closestCell == 999999999; target++) {
            if (!Dungeon.level.visited[target]) {
                int distance = Dungeon.level.distance(Dungeon.hero.pos, target);
                if (distance < closestCell) {
                    // I stole this from the Hero.getCloser function
                    int len = Dungeon.level.length();
                    boolean[] p = Dungeon.level.passable;
                    boolean[] v = Dungeon.level.visited;
                    boolean[] m = Dungeon.level.mapped;
                    boolean[] passable = new boolean[len];
                    for (int i = 0; i < len; i++) {
                        passable[i] = p[i] && (v[i] || m[i]);
                    }

                    // make sure the target cell is accessible (also stolen from Hero class)
                    PathFinder.Path newpath = Dungeon.findPath(Dungeon.hero, target, passable, Dungeon.hero.fieldOfView, true);
                    if (newpath != null) {
                        closestCell = target;
                    }
                }
            }
        }
        if (closestCell != 999999999) {
            return new HeroAction.Move(closestCell);
        }
        return null;
     }
}
