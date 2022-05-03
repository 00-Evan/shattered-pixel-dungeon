package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.watabou.utils.PointF;

public class Cone {
    public float baseX;
    public float baseY;
    public float start;
    public float width;
    public float radius;

    public Cone(float x, float y, float s, float w, float r){
        this.baseX = x;
        this.baseY = y;
        this.start = s;
        this.width = w>=0?w:-w;
        this.radius = r;
    }

    public PointF radToXY(float r, float theta){
        return new PointF((float)(r * Math.cos(theta)) + baseX, (float)(r * Math.sin(theta)) + baseY);
    }

    public boolean inCone(float x, float y){
        double adx = Math.cos(start);
        double ady = Math.sin(start);
        double bdx = Math.cos(start+width);
        double bdy = Math.sin(start+width);
        double cdx = x - baseX;
        double cdy = y - baseY;

        return (cdx * cdx + cdy * cdy <= radius*radius) && (adx * cdy - ady * cdx >= 0) && (bdx * cdy - bdy * cdx <= 0);
    }

    public float percent(float x, float y){
        return (GME.nAngle(GME.angle(x-baseX, y-baseY))-start)/width;
    }
}
