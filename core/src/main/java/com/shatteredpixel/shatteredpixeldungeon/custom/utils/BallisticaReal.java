package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.List;

//REAL ballistica means works like real;
//Normally ballistica in game can go through corners when the beam go through a wall
//but the tile of wall is not built in trace. (Because there is only one tile for each tile in main direction)
//However, REAL ballistica can't go through corners.
//It considers all the points the beam cross with each x and y integer
//So complex actions like reflection can work correctly.
//Walls, target cells, chars are considered as a whole tile. Even 0.001 tile collision would stop.
//Yeah it would be quite odd when this happens, but it works for now.
//Integer pos is considered at the center of tile.
public class BallisticaReal {
    //Why here we do NOT record FULL path?
    //first, introduction of range makes real collision quite messy if we add end point to path.
    //second, real beam collides with boundary, which means it has already considered the next cell.
    //if we want to control the full path, just build a never-stop one and go by tiles.
    //third, it would cost much more if go through full path. There are cases when it updates each flash.
    public ArrayList<Integer> pathI = new ArrayList<>();
    public Integer sourceI = null;
    public Integer collisionI = null;
    public Integer dist = 0;

    public ArrayList<PointF> pathF = new ArrayList<>();
    public PointF sourceF = null;
    public PointF collisionF = null;

    public static final int STOP_TARGET = 1;    //ballistica will stop at the target cell
    public static final int STOP_CHARS = 2;     //ballistica will stop on first char hit
    public static final int STOP_SOLID = 4;     //ballistica will stop on solid terrain
    public static final int IGNORE_SOFT_SOLID = 8; //ballistica will ignore soft solid terrain, such as doors and webs

    public static final int PROJECTILE =  	STOP_TARGET	| STOP_CHARS	| STOP_SOLID;

    public static final int MAGIC_BOLT =    STOP_CHARS  | STOP_SOLID;

    public static final int WONT_STOP =     0;



    public BallisticaReal(PointF from, PointF to, int params) {
        //sourcepos need to offset a bit to judge cases the from is on the line
        //for example, from = "30, 20.5", to = "20, 0.5", x>=30 is wall
        //if we do not offset, the source will be 30, 20, and when it collides with y=20,
        //the wall judgement is centered at (30, 20), not (29, 20). First judgement will look at (30, 19) instead of (29,19)
        //When it collides, it regards the upper ceil, wall, not empty. And the ballistica will end here.

        //if the point is on the edge of cell, give it a tiny offset.
        if(from.x-(int)from.x==0f || from.y-(int)from.y==0f) {
            sourceI = pointToCell(pointFloatToInt(from.clone().offset(to.x - from.x > 0 ? 0.001f : -0.001f, to.y - from.y > 0 ? 0.001f : -0.001f), false));
        }else{
            sourceI = pointToCell(pointFloatToInt(from.clone(), false));
        }
        sourceF = from;
        buildTrace(sourceF, to,
                (params & STOP_TARGET) > 0,
                (params & STOP_CHARS) > 0,
                (params & STOP_SOLID) > 0,
                (params & IGNORE_SOFT_SOLID) > 0);

        if (collisionI != null) {
            dist = pathI.indexOf(collisionI);
        } else if (!pathI.isEmpty()) {
            collisionI = pathI.get(dist = pathI.size() - 1);
            collisionF = pathF.get(pathF.size()-1);
        } else {
            dist = 0;
        }
    }

    public BallisticaReal(int from, int to, int params){
        this(pointIntToFloat(cellToPoint(from)).offset(0.5f, 0.45f),
                pointIntToFloat(cellToPoint(to)).offset(0.5f, 0.45f),
                params);
    }

    public BallisticaReal(PointF from, float angle, float range, int params){
        this(from, new PointF(from.x, from.y).offset(poleToPointF(angle, range)), params);
    }

    public BallisticaReal(int from, float angle, float range, int params){
        this(pointIntToFloat(cellToPoint(from)).offset(0.5f, 0.45f),angle,range,params);
    }

    private void buildTrace(PointF from, PointF to, boolean stopTarget, boolean stopChars, boolean stopTerrain, boolean ignoreSoftSolid){
        PointF vector = new PointF(to.x - from.x, to.y - from.y);

        float dx;
        float dy;
        float movX = Math.abs(vector.x);
        float movY = Math.abs(vector.y);
        //too short move ,return
        if(movX < 0.001f && movY < 0.001f){
            int end = pointToCell(pointFloatToInt(from, false));
            pathI.add(end);
            pathF.add(from);
            cld(end);
            cldF(from);
            return;
        }
        //actually it is abandoned
        if(movX>movY){
            dx = (vector.x>0?1f:-1f);
            dy = movY/movX*(vector.y>0?1f:-1f);
        }else{
            dy = (vector.y>0?1f:-1f);
            dx = movX/movY*(vector.x>0?1f:-1f);
        }
        //which direction?
        boolean up = dy>0;
        boolean right = dx>0;
        boolean vertical = Math.abs(dx)<0.001f;
        boolean horizontal = Math.abs(dy)<0.001f;
        //record current point
        PointF curPosF = new PointF(from.x, from.y);
        Point curPos = cellToPoint(sourceI);//pointFloatToInt(from, false);
        //we shot into one tile, and meet one border. passable caches whether beam can pass this border.
        boolean[] canPass = new boolean[4];
        final int[] neigh = new int[]{-1, 1, Dungeon.level.width(), -Dungeon.level.width()};

        pathI.add(sourceI);
        pathF.add(sourceF);
        /*
        if (stopTerrain  && Dungeon.level.solid[sourceI]) {
            if (ignoreSoftSolid && (Dungeon.level.passable[sourceI] || Dungeon.level.avoid[sourceI])) {
                //do nothing
            } else {
                cld(sourceI);
                cldF(sourceF);
                return;
            }
        } else if (stopChars && Actor.findChar( sourceI ) != null) {
            cld(sourceI);
            cldF(sourceF);
            return;
        } else if  (sourceF.equals(pointFloatToInt(to, false)) && stopTarget){
            cld(sourceI);
            cldF(sourceF);
            return;
        }
         */

        while(isInsideMap(curPosF)){
            int cell= pointToCell(curPos);
            boolean passable;
            //build passable
            for(int i=0;i<4;++i){
                int target = cell + neigh[i];
                passable = true;
                if (stopTerrain  && Dungeon.level.solid[target]) {
                    if (ignoreSoftSolid && (Dungeon.level.passable[target] || Dungeon.level.avoid[target])) {
                        //do nothing
                    } else {
                        passable = false;
                    }
                } else if (stopChars && Actor.findChar( target ) != null) {
                    passable = false;
                } else if  (curPos.equals(pointFloatToInt(to, false)) && stopTarget){
                    passable = false;
                }

                canPass[i]=passable;
            }
            //will meet which border
            boolean xOnLine = curPosF.x-(int)curPosF.x==0;
            boolean yOnLine = curPosF.y-(int)curPosF.y==0;
            float tx = (float) (xOnLine?(Math.floor(curPosF.x) + (right?1f:-1f)):(Math.floor(curPosF.x) + (right?1f:0f)));
            float ty = (float) (yOnLine?(Math.floor(curPosF.y) + (up?1f:-1f)):(Math.floor(curPosF.y) + (up?1f:0f)));
            //meet x border, y border, or both? And where?
            PointF nx, ny;
            if(vertical){
                ny = new PointF(curPosF.x, ty);
                nx = null;
            }else if(horizontal){
                nx = new PointF(tx, curPosF.y);
                ny = null;
            }else{
                nx = new PointF(tx, (tx-curPosF.x)*dy/dx+curPosF.y);
                ny = new PointF((ty-curPosF.y)*dx/dy+curPosF.x, ty);

                if(nx.y==ny.y){
                    //nx==ny means reaching xy cross
                    //do nothing
                }else if((up && nx.y>ny.y) || (!up && nx.y<ny.y)){
                    nx = null;
                }else{
                    ny = null;
                }
            }

            //out of range, stop
            if(stopTarget){
                if((nx != null && Math.abs(nx.x  - from.x)>=movX) ||
                        (ny != null && Math.abs(ny.y  - from.y)>=movY))
                {
                    cldF(to);
                    cld(pointToCell(pointFloatToInt(to, false)));
                    pathI.add(collisionI);
                    pathF.add(collisionF);
                    return;
                }
            }

            //next cell, next pointF. Handle xy logic first.
            int nextCell=cell;
            if(ny==null){
                nextCell += dx>0? 1:-1;
            }else if(nx==null){
                nextCell += dy>0?Dungeon.level.width():-Dungeon.level.width();
            }else{
                //if reach the xy cross, judge x and y
                nextCell += dx>0? 1:-1;
                for(int i=0; i<4; ++i){
                    if(neigh[i]+cell!=nextCell) continue;
                    if(!canPass[i]){
                        cld(nextCell+dy>0?Dungeon.level.width():-Dungeon.level.width());
                        cldF(nx);
                        return;
                    }
                }
                nextCell += dy>0?Dungeon.level.width():-Dungeon.level.width();
                for(int i=0; i<4; ++i){
                    if(neigh[i]+cell!=nextCell) continue;
                    if(!canPass[i]){
                        cld(nextCell);
                        cldF(ny);
                        return;
                    }
                }
                //if x and y let it pass, the go on
            }

            //judge whether can pass, and collide. For x or y logic
            pathI.add(nextCell);
            pathF.add(ny==null?nx.clone():ny.clone());
            //has judged xy cross
            if(nx == null || ny == null) {
                for (int i = 0; i < 4; ++i) {
                    if (neigh[i] + cell != nextCell) continue;
                    if (!canPass[i]) {
                        cld(nextCell);
                        cldF(ny == null ? nx : ny);
                        return;
                    }
                }
            }
            //go on one point, and continue
            curPosF=(ny==null?nx.clone():ny.clone());
            curPos=cellToPoint(nextCell);

        }

    }

    private void cld(int cell){
        if (collisionI == null)
            collisionI = cell;
    }

    //using with new pointF
    private void cldF(PointF cell){
        if (collisionF == null) {
            collisionF = cell.clone();
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

    public static PointF pointToScreen(PointF p){
        return p.clone().scale(DungeonTilemap.SIZE);
    }

    public static PointF raisedPointToScreen(PointF p){
        return p.clone().scale(DungeonTilemap.SIZE);
    }



    private static final float A2P = 0.0174533f;

    //We assume up is positive y but in game it is opposite.
    private static PointF poleToPointF(float angle, float range){
        return new PointF((float)(range*Math.cos(angle*A2P)), (float)(range*Math.sin(angle*A2P)));
    }

    private static boolean isInsideMap(PointF posF){
        return !(posF.x<1f || posF.x> Dungeon.level.width()-1f || posF.y<1f || posF.y>Dungeon.level.height()-1f);
    }

    private static Point cellToPoint(int cell ){
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
}
