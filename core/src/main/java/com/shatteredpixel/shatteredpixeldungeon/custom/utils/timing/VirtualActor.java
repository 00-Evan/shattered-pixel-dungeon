package com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.utils.Callback;

import java.util.ArrayList;

//used to lock thread temporarily, to keep current status instantly
//so that we can stay at instant/fast visuals, or to custom freezing time.
public class VirtualActor extends Visual {

    private float lasting = 0.5f;
    private Callback callback;

    public VirtualActor() {
        super(0,0,0,0);
    }

    private static void addDelayerVisual(float delay, Callback callback){
        VirtualActor d = new VirtualActor();
        d.reset(delay, callback);
        Dungeon.hero.sprite.parent.add(d);
    }

    public void reset(float delay, Callback callback){
        revive();
        lasting = delay;
        this.callback = callback;
    }

    @Override
    public void update() {
        super.update();
        lasting -= Game.elapsed;
        if(lasting  <= 0){
            if(callback!=null) callback.call();
            killAndErase();
        }
    }
    //simply delay time, can't interrupt
    public static void delay(float time){
        delay(time, false, null, null);
    }
    //delay time and decide if delayer from the same class can parallel
    public static void delay(float time, final boolean parallel, Object obj){ delay(time, parallel, obj, null); }
    //simple delay with callback
    public static void delay(float time, Callback callback){
        delay(time, false, null, callback);
    }
    //delay time and decide if delayer from the same class can parallel with callback enabled
    public static void delay(float time, final boolean parallel, Object obj, Callback callback){
        if(!parallel) {
            Actor.addDelayed(new Actor() {
                final Actor toRemove = this;
                @Override
                protected boolean act() {
                    Actor.remove(toRemove);
                    VirtualActor.addDelayerVisual(time, () -> {
                        if (callback != null) callback.call();
                        toRemove.next();
                    });
                    return false;
                }
            }, -1);
        }else{
            //We need to distinguish different parallel effects, and hashcode for classname is a good idea.
            //Since we only have 100- effects to hash, the chance of conflict is small enough.
            final int hash = obj == null? 0:obj.getClass().getName().hashCode();
            Actor.addDelayed(new ClassSpecifiedDelayer() {
                final Actor toRemove = this;
                @Override
                protected boolean act() {
                    //remove this first to avoid deadlock when multiple events ends simultaneously.
                    Actor.remove(toRemove);
                    VirtualActor.addDelayerVisual(time, () -> {
                        if (callback != null) callback.call();
                        toRemove.next();
                    });
                    for (Actor a : Actor.all()) {
                        if (a instanceof ClassSpecifiedDelayer && a.cooldown() == 0) {
                            if(((ClassSpecifiedDelayer) a).targetHash == this.targetHash) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }.setHash(hash), -1);
        }
    }
    //simple delay that ANY DelayerEffect can interrupt
    public static void delaySoft(float time, Callback callback){
        Actor.addDelayed(new Delayer() {
            final Actor toRemove = this;
            @Override
            protected boolean act() {
                Actor.remove(toRemove);
                VirtualActor.addDelayerVisual(time, () -> {
                    if (callback != null) callback.call();
                    toRemove.next();
                });
                for(Actor a: Actor.all()){
                    if(a instanceof Delayer && a.cooldown() == 0){
                        return true;
                    }
                }
                return false;
            }
        }, -1);
    }
    //kill all the delayer
    public static void killAll(){
        ArrayList<Actor> toRemove = new ArrayList<>();
        for(Actor a: Actor.all()){
            if(a instanceof Delayer){
                toRemove.add(a);
            }
        }
        for(Actor a: toRemove){
            Actor.remove(a);
            a.next();
        }
    }

    public static abstract class Delayer extends Actor{
        {
            actPriority = VFX_PRIO;
        }
    }

    public static abstract class ClassSpecifiedDelayer extends Delayer{
        {
            actPriority = VFX_PRIO;
        }
        public int targetHash;
        public ClassSpecifiedDelayer setHash(int hash){this.targetHash = hash; return this;}
    }
}
