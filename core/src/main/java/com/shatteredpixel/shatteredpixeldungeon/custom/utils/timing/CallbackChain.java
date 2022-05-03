package com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.utils.Callback;

import java.util.Collections;
import java.util.LinkedList;

//invoke several callbacks on key flashes.
public class CallbackChain extends Visual {
    public CallbackChain() {
        super(0, 0, 0, 0);
    }

    private LinkedList<CallbackTime> list;
    private float time = 0;
    private int count;

    public static void executeChain(float[] triggerTimes, Callback[] callbacks){
        CallbackChain cbc = new CallbackChain();
        cbc.initChain(triggerTimes, callbacks);
        Dungeon.hero.sprite.parent.add(cbc);
    }

    private void initChain(float[] triggerTimes, Callback[] callbacks){
        int length = Math.min(triggerTimes.length, callbacks.length);
        list = new LinkedList<>();
        for(int i=0;i<length; ++i){
            list.add(new CallbackTime(triggerTimes[i], callbacks[i]));
        }
        Collections.sort(list, (lhs, rhs) -> {
            float diff = lhs.time - rhs.time;
            return diff > 0 ? 1 : (diff < 0 ? -1 : 0);
        });
        count = list.size();
        VirtualActor.delaySoft(list.getFirst().time + Game.elapsed, null);
    }

    @Override
    public void update() {
        super.update();
        if(count <= 0){
            list = null;
            killAndErase();
            return;
        }
        time += Game.elapsed;
        CallbackTime current = list.getFirst();
        if (time > current.time) {
            if (current.callback != null) {
                current.callback.call();
            }
            list.removeFirst();
            --count;
        }
    }

    public static class CallbackTime{
        public float time;
        public Callback callback;
        public CallbackTime(float t, Callback c){time = t; callback = c;}
    }
}
