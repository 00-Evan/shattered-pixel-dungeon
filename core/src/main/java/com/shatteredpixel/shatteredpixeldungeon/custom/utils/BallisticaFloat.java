package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.List;

public class BallisticaFloat{

    //used for Integer path for gameLogic
    public ArrayList<Integer> pathI = new ArrayList<>();
    public Integer sourcePosI = null;
    public Integer collisionPosI = null;
    public Integer dist = 0;

    //used for Float points (true pos of ballistica), in VFX and special cases (like reflection)
    public ArrayList<PointF> pathF = new ArrayList<>();
    public PointF sourcePosF = null;
    public PointF collisionPosF = null;

    //parameters to specify the colliding cell
    public static final int STOP_TARGET = 1;    //ballistica will stop at the target cell
    public static final int STOP_CHARS = 2;     //ballistica will stop on first char hit
    public static final int STOP_SOLID = 4;     //ballistica will stop on solid terrain
    public static final int IGNORE_SOFT_SOLID = 8; //ballistica will ignore soft solid terrain, such as doors and webs

    public static final int PROJECTILE =  	STOP_TARGET	| STOP_CHARS	| STOP_SOLID;

    public static final int MAGIC_BOLT =    STOP_CHARS  | STOP_SOLID;

    public static final int WONT_STOP =     0;

    public BallisticaFloat(int from, int to, int params) {
        sourcePosI = from;
        sourcePosF = pointIntToFloat(cellToPoint(from));
        PointF t = pointIntToFloat(cellToPoint(to));
        buildTraceF(sourcePosF, t,
                (params & STOP_TARGET) > 0,
                (params & STOP_CHARS) > 0,
                (params & STOP_SOLID) > 0,
                (params & IGNORE_SOFT_SOLID) > 0);

        if (collisionPosI != null) {
            dist = pathI.indexOf(collisionPosI);
        } else if (!pathI.isEmpty()) {
            collisionPosI = pathI.get(dist = pathI.size() - 1);
            //collisionPosF = pointIntToFloat(cellToPoint(collisionPosI));
            collisionPosF = pathF.get(pathF.size()-1);
        } else {
            pathI.add(from);
            pathF.add(sourcePosF);
            collisionPosI = from;
            collisionPosF = sourcePosF;
            dist = 0;
        }
    }

    public BallisticaFloat(PointF from, PointF to, int params) {
        sourcePosI = pointToCell(pointFloatToInt(from, false));
        sourcePosF = from;
        buildTraceF(sourcePosF, to,
                (params & STOP_TARGET) > 0,
                (params & STOP_CHARS) > 0,
                (params & STOP_SOLID) > 0,
                (params & IGNORE_SOFT_SOLID) > 0);

        if (collisionPosI != null) {
            dist = pathI.indexOf(collisionPosI);
        } else if (!pathI.isEmpty()) {
            collisionPosI = pathI.get(dist = pathI.size() - 1);
            collisionPosF = pathF.get(pathF.size()-1);//collisionPosF = getCollisionPointWithBoundary(from, to);
        } else {
            pathI.add(sourcePosI);
            pathF.add(sourcePosF);
            collisionPosI = sourcePosI;
            collisionPosF = sourcePosF;
            dist = 0;
        }
    }

    public BallisticaFloat(PointF from, float angle, float range, int params) {
        PointF to = new PointF(from.x, from.y).offset(poleToPointF(angle, range));
        sourcePosI = pointToCell(pointFloatToInt(from, false));
        sourcePosF = from;
        buildTraceF(sourcePosF, to,
                (params & STOP_TARGET) > 0,
                (params & STOP_CHARS) > 0,
                (params & STOP_SOLID) > 0,
                (params & IGNORE_SOFT_SOLID) > 0);

        if (collisionPosI != null) {
            dist = pathI.indexOf(collisionPosI);
        } else if (!pathI.isEmpty()) {
            collisionPosI = pathI.get(dist = pathI.size() - 1);
            //collisionPosF = getCollisionPointWithBoundary(from, to);
            collisionPosF = pathF.get(pathF.size()-1);
        } else {
            pathI.add(sourcePosI);
            pathF.add(sourcePosF);
            collisionPosI = sourcePosI;
            collisionPosF = sourcePosF;
            dist = 0;
        }
    }

    public BallisticaFloat(int from, float angle, float range, int params){
        this(pointIntToFloat(cellToPoint(from)), angle, range, params);
    }

    private void reset(){
        pathI.clear();
        pathF.clear();
        sourcePosI = null;
        collisionPosI = null;
        dist = 0;
        sourcePosF = null;
        collisionPosF = null;
    }

    private void buildTraceF(PointF from, PointF to, boolean stopTarget, boolean stopChars, boolean stopTerrain, boolean ignoreSoftSolid){
        //we start from the upleft corner of one block, and then offset 0.5, 0.5 for VFX
        PointF vector = new PointF(to.x - from.x, to.y - from.y);

        float stepX;
        float stepY;
        float movX = Math.abs(vector.x);
        float movY = Math.abs(vector.y);

        if(movX < 0.001f && movY < 0.001f){
            int end = pointToCell(pointFloatToInt(from, false));
            pathI.add(end);
            pathF.add(from);
            cld(end);
            cldF(from);
            return;
        }

        if(movX>movY){
            stepX = (vector.x>0?1f:-1f);
            stepY = movY/movX*(vector.y>0?1f:-1f);
        }else{
            stepY = (vector.y>0?1f:-1f);
            stepX = movX/movY*(vector.x>0?1f:-1f);
        }

        PointF curPosF = new PointF(from.x, from.y);
        Point curPos = pointFloatToInt(from, true);

        while(isInsideMap(curPosF)){

            int cell = pointToCell(curPos);
            //if we're in a wall, collide with the previous cell along the path.
            //we don't use solid here because we don't want to stop short of closed doors
            //FIXME BEAM SHOULD NOT START IN WALLS
            if (stopTerrain && cell != sourcePosI && !Dungeon.level.passable[cell] && !Dungeon.level.avoid[cell]) {
                if(!pathI.isEmpty()) {
                    cld(pathI.get(pathI.size() - 1));
                    cldF(curPosF.clone().offset(-stepX, -stepY));
                    //cldF(curPosF.clone());
                    //cld(cell);
                }else{
                    break;
                }
            }

            pathI.add(cell);
            pathF.add(new PointF(curPosF));

            if (stopTerrain && cell != sourcePosI && Dungeon.level.solid[cell]) {
                if (ignoreSoftSolid && (Dungeon.level.passable[cell] || Dungeon.level.avoid[cell])) {
                    //do nothing
                } else {
                    cld(cell);
                    cldF(curPosF);
                }
            } else if (cell != sourcePosI && stopChars && Actor.findChar( cell ) != null) {
                cld(cell);
                cldF(curPosF);
            } else if  (curPos.equals(pointFloatToInt(to, true)) && stopTarget){
                cld(cell);
                cldF(curPosF);
            }

            curPosF.offset(stepX, stepY);
            curPos = pointFloatToInt(curPosF, true);
        }
    }

    //we only want to record the first position collision occurs at.
    private void cld(int cell){
        if (collisionPosI == null)
            collisionPosI = cell;
    }

    //using with new pointF
    private void cldF(PointF cell){
        if (collisionPosF == null) {
            collisionPosF = cell.clone();
        }
    }

    public List<Integer> subPath(int start, int end){
        try {
            end = Math.min( end, pathI.size()-1);
            return pathI.subList(start, end+1);
        } catch (Exception e){
            ShatteredPixelDungeon.reportException(e);
            return new ArrayList<>();
        }
    }

    // y positive is towards the ground...
    private PointF getCollisionPointWithBoundary(PointF from, PointF to){
        PointF downRight = new PointF(Dungeon.level.width()-1, 1);
        PointF downLeft = new PointF(1, 1);
        PointF upRight = new PointF(Dungeon.level.width()-1, Dungeon.level.height()-1);
        PointF upLeft = new PointF(1, Dungeon.level.height()-1);
        PointF up = getCrossPoint(from, to, upRight, upLeft);
        PointF down = getCrossPoint(from, to, downRight, downLeft);
        PointF left = getCrossPoint(from, to, upLeft, downLeft);
        PointF right = getCrossPoint(from, to, upRight, downRight);

        int available = 0;
        available += (up!=null? 1:0);
        available += (down!=null? 2:0);
        available += (left!=null? 4:0);
        available += (right!=null? 8:0);

        boolean direction = to.x - from.x > 0;
        float min_distance = 1000f;
        int min_distance_index = -1;
        for(int i=0; i<4; ++i){
            if((available&(1<<i))>0){
                PointF temp;
                switch(i){
                    case 0: temp = up.clone(); break;
                    case 1: temp = down.clone(); break;
                    case 2: temp = left.clone(); break;
                    case 3: default: temp = right.clone(); break;
                }
                if(direction == (temp.x - to.x > 0)){
                    if(Math.abs(temp.x - to.x) < min_distance){
                        min_distance_index = i;
                        min_distance = Math.abs(temp.x - to.x);
                    }
                }
            }
        }
        switch (min_distance_index){
            case 0: return up.clone();
            case 1: return down.clone();
            case 2: return left.clone();
            case 3: default: return right.clone();
        }
     }

    //library methods, static
    private static final float A2P = 0.0174533f;

    private static PointF poleToPointF(float angle, float range){
        return new PointF((float)(range*Math.cos(angle*A2P)), (float)(range*Math.sin(angle*A2P)));
    }

    private static boolean isInsideMap(PointF posF){
        return !(posF.x<1f || posF.x>Dungeon.level.width()-1f || posF.y<1f || posF.y>Dungeon.level.height()-1f);
    }

    private static Point cellToPoint( int cell ){
        return new Point(cell % Dungeon.level.width(), cell / Dungeon.level.width());
    }

    private static int pointToCell( Point p ){
        return p.x + p.y*Dungeon.level.width();
    }

    private static Point pointFloatToInt(PointF p, boolean round){
        if(round){
            return new Point(Math.round(p.x), Math.round(p.y));
        }
        return new Point((int)p.x, (int)p.y);
    }

    private static PointF pointIntToFloat(Point p){
        return new PointF(p.x, p.y);
    }

    public static PointF coordToScreen(PointF p){
        return p.clone().offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE);
    }

    public static PointF getCrossPoint(PointF A, PointF B, PointF M, PointF N){
        float A1=A.y-B.y;
        float B1=B.x-A.x;
        float C1=A1*A.x+B1*A.y;

        float A2=M.y-N.y;
        float B2=N.x-M.x;
        float C2=A2*M.x+B2*M.y;

        float det_k=A1*B2-A2*B1;

        if(Math.abs(det_k)<0.00001f){
            return null;
        }

        float a=B2/det_k;
        float b=-1*B1/det_k;
        float c=-1*A2/det_k;
        float d=A1/det_k;

        float x=a*C1+b*C2;
        float y=c*C1+d*C2;

        return  new PointF(x,y);
    }
}
