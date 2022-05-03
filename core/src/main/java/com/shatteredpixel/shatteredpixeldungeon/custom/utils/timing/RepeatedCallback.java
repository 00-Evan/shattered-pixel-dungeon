package com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.utils.Callback;

import java.util.Arrays;
import java.util.LinkedList;

public class RepeatedCallback extends Visual {
    public RepeatedCallback() {
        super(0, 0, 0, 0);
    }

    private LinkedList<Float> times;
    private float time = 0;
    private int count;
    private float step;
    private boolean simple = false;
    private Callback callback;

    public static void executeChain(float[] triggerTimes, Callback callback){
        RepeatedCallback rc = new RepeatedCallback();
        rc.initChain(triggerTimes, callback);
        Dungeon.hero.sprite.parent.add(rc);
    }

    public static void executeChain(float step, int repeats, Callback callback){
        RepeatedCallback rc = new RepeatedCallback();
        rc.initSimple(step, repeats, callback);
        Dungeon.hero.sprite.parent.add(rc);
    }

    private void initChain(float[] triggerTimes, Callback callback){
        Arrays.sort(triggerTimes);
        times = new LinkedList<>();
        for(float t: triggerTimes){
            times.add(t);
        }
        count = triggerTimes.length;
        simple = false;
        this.callback = callback;
        VirtualActor.delaySoft(triggerTimes[0] + Game.elapsed, null);
    }

    private void initSimple(float step, int repeats, Callback callback){
        this.step = step;
        this.count = repeats;
        this.callback = callback;
        this.simple = true;
        this.time = step;
    }

    @Override
    public void update() {
        super.update();
        time += Game.elapsed;
        if(count <= 0){
            times = null;
            killAndErase();
            return;
        }
        if(simple){
            if(time > step){
                time -= step;
                --count;
                if(callback != null) callback.call();
            }
        }else{
            time += Game.elapsed;
            float current = times.getFirst();
            if(time>current){
                --count;
                if(callback != null) callback.call();
            }
        }
    }
}
