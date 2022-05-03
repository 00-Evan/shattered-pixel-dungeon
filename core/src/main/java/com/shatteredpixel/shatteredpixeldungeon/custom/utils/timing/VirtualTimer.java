package com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.utils.Callback;

//Yeah its only job is to count a period of time, do something, and die.
public class VirtualTimer extends Visual {
    private float time;
    private Callback callback;
    public VirtualTimer(){
        super(0,0,0,0);
    }
    private void initTimer(float t, Callback c){time = t; callback = c;}
    @Override
    public void update() {
        time-= Game.elapsed;
        if(time<=0){
            killAndErase();
            if(callback!=null){
                callback.call();
            }
        }
    }

    public static void countTime(float time, Callback callback){
        VirtualTimer tv = new VirtualTimer();
        Dungeon.hero.sprite.parent.add(tv);
        tv.initTimer(time, callback);
    }
}
