package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
//GAME MATH EXTENSION
public class GME {
    public static int gate(int min, int value, int max){
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    public static int[] NEIGHBOURS5(){
        int w = Dungeon.level.width();
        return new int[]{0, -1, 1, w, -w};
    }

    public static int[] NEIGHBOURS12(){
        int w = Dungeon.level.width();
        return new int[]{-1, -2, 1, 2, w, -w, -2*w, 2*w, w+1, w-1, -w+1, -w-1};
    }

    public static int[] NEIGHBOURS20(){
        int w = Dungeon.level.width();
        return new int[]{
                1, -1, w, -w, 1+w, 1-w, -1+w, -1-w, 2, -2, -2*w, 2*w,
                -2*w+1, -2*w-1, 2*w+1, 2*w-1, 2+w, 2-w, -2+w, -2-w
        };
    }

    public static float angle(int from, int to){
        float angle = PointF.angle(new PointF(pointToF(Dungeon.level.cellToPoint(from))),
                new PointF(pointToF(Dungeon.level.cellToPoint(to))));
        angle /= PointF.G2R;
        return angle;
    }
    public static float angle(float x, float y){
        float angle = PointF.angle(new PointF(x, y),
                new PointF(1f, 0));
        angle /= PointF.G2R;
        return angle;
    }
    public static float nAngle(float angle){
        if(angle<0f) angle += 360f;
        return angle;
    }
    protected static PointF pointToF(Point p){
        return new PointF(p.x, p.y);
    }

    //WARNING: return TRUE coords, NO need to +pos.
    public static int[] rectBuilder(int pos, int w, int h){
        if(w<0 || h<0) return null;
        int width = Dungeon.level.width();
        int height = Dungeon.level.height();
        ArrayList<Integer> cell = new ArrayList<>();

        int posX = pos%width;
        int posY = pos/width;
        for(int i=-w;i<w+1;++i){
            //if one tile is outside, then the column is outside
            if(posX + i <= 0 || posX + i >= width - 1) continue;
            for(int j=-h;j<h+1;++j){
                //exclude tile outside of map
                if(posY + j <= 0 || posY + j >= height - 1) continue;
                cell.add(pos + i * width + j);
            }
        }

        int le = cell.size();
        int[] result=new int[le];
        for(int i=0;i<le;++i) result[i]=cell.get(i);

        return result;
    }

    public static int accurateRound(float toRound){
        return Random.Float() < toRound - (int)toRound ? (int)(toRound + 1f) : (int)toRound;
    }

}
