package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaReal;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.PointF;

import java.util.LinkedHashMap;

public class ScanningBeam extends Image {
    //all data
    private BeamData data;
    private PointF nowStart;

    //time control
    private float time = 0f;
    private float[] total;
    //beam properties
    private int type = 0;
    private float baseDiameter = 1f;
    //record attacked chars, or this would be DOT damage depending on FPS.
    private LinkedHashMap<Integer, Float> hasCollided = new LinkedHashMap<>();

    public ScanningBeam(Effects.Type asset, int beamType, BeamData dataPack){
        super( Effects.get( asset ) );
        data = dataPack;

        origin.set( 0, height / 2 );

        nowStart = new PointF(data.fromX, data.fromY);

        x = data.fromX* DungeonTilemap.SIZE - origin.x;
        y = data.fromY*DungeonTilemap.SIZE - origin.y;

        total = new float[3];
        total[0] = data.lightTime;
        total[1] = total[0]+data.moveTime;
        total[2] = total[1]+data.fadeTime;

        angle = data.startAngle;

        type = beamType;

        updateScale();
    }

    public static void setCollide(OnCollide collide) {
        ScanningBeam.collide = collide;
    }

    public ScanningBeam setDiameter(float scale){
        this.baseDiameter = scale;
        return this;
    }
    //build beam each flash
    @Override
    public void update(){
        super.update();
        time += Game.elapsed;

        if(time >= total[2]){
            killAndErase();
        }else if(time > total[1]){
            float p = (total[2]-time)/data.fadeTime * 0.9f;
            alpha(p);
            scale.y = p * baseDiameter;
            updateScale();
        }else if(time > total[0]){
            angle = (data.speedAngular * (time-total[0]) + data.startAngle);
            x += data.speedX * Game.elapsed * DungeonTilemap.SIZE;
            y += data.speedY * Game.elapsed * DungeonTilemap.SIZE;
            nowStart.offset(data.speedX * Game.elapsed, data.speedY * Game.elapsed);
            float randAlpha = (float) (0.9f + Math.sin(Game.timeTotal * 6 * Math.PI) * 0.1f);
            alpha(randAlpha);
            scale.y = baseDiameter * randAlpha;
            BallisticaReal ba = updateScale();
            judgeHit(ba);
        }else{
            float p = time/total[0] * 0.9f;
            alpha(p);
            scale.y = p * baseDiameter;
            updateScale();
        }
    }
    //May not safe if one cell/char is hit by multiple beams in one flash. Should avoid crossing.
    private /*synchronized*/ void judgeHit(BallisticaReal ba){
        for(int i: ba.subPath(1, ba.dist)){
            collide.cellProc(i);
            Char ch = Actor.findChar(i);
            if(ch != null){
                if(!hasCollided.containsKey(ch.id())){
                    if(collide.onHitProc(ch) > 0) {
                        hasCollided.put(ch.id(), angle);
                    }
                }
            }
        }
        //remove token if beam can't shoot that Char.
        //Normally Chars are sparse, so remove one each flash is just enough.
        if(!hasCollided.isEmpty()) {
            int removeFirst = hasCollided.keySet().iterator().next();
            if (Math.abs(angle - hasCollided.values().iterator().next()) > 180) {
                hasCollided.remove(removeFirst);
            }
        }
    }

    private BallisticaReal updateScale(){
        BallisticaReal ba = new BallisticaReal(nowStart, angle, data.range, type);
        float dx = ba.collisionF.x - nowStart.x;
        float dy = ba.collisionF.y - nowStart.y;
        float distance = (float) Math.sqrt(dx*dx + dy*dy);
        scale.x = Math.min(distance, data.range)*DungeonTilemap.SIZE/width;
        return ba;
    }

    @Override
    public void draw() {
        Blending.setLightMode();
        super.draw();
        Blending.setNormalMode();
    }

    private static OnCollide collide;

    public interface OnCollide {
        //when hit char. return >0 means hit, <=0 otherwise.
        int onHitProc(Char ch);
        //when hit cell
        int cellProc(int i);
    }

    //data pack. to avoid 10+ parameters in constructor.
    public static class BeamData{
        public float fromX = 0f;
        public float fromY = 0f;
        public float startAngle = 0f;
        public float range = 0f;

        public float speedX = 0f;
        public float speedY = 0f;
        public float speedAngular = 0f;

        public float lightTime = 0f;
        public float moveTime = 0f;
        public float fadeTime = 0f;

        public BeamData setPosition(float fx, float fy, float sa, float r){
            this.fromX = fx;
            this.fromY = fy;
            this.startAngle = sa;
            this.range = r;
            return this;
        }

        public BeamData setSpeed(float sx, float sy, float sa){
            this.speedX = sx;
            this.speedY = sy;
            this.speedAngular = sa;
            return this;
        }

        public BeamData setTime(float on, float move, float off){
            this.lightTime = on;
            this.moveTime = move;
            this.fadeTime = off;
            return this;
        }
    }

}

