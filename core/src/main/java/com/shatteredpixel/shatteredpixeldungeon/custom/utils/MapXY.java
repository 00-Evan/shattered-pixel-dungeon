package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

public class MapXY {

    //Well currently linear maps can be handled by Dungeon.level.insideMap
    //Due to the method to read the linear map, x never go outside of the rect
    //And y can be compared with height
    //So no need for a inside check.
    public static int[][] ToXY(int[] map){
        int[][] mapXY = new int[2][map.length];
        int w = Dungeon.level.width();
        for(int i=0; i< map.length; ++i){
            mapXY[0][i] = map[i] % w;
            mapXY[1][i] = map[i] / w;
        }
        return mapXY;
    }

    //For a xy map, a check is necessary because (x,y) can access where linear maps can't
    public static int[] ToLength(int[][] mapXY, boolean check){
        int l = mapXY[0].length;
        if(check) l=insideTiles(mapXY);
        int[] map = new int[l];
        int w = Dungeon.level.width();

        int count=0;
        for(int i=0;i<mapXY[0].length;++i){
            if(check) {
                if(!insideMap( mapXY[0][i] , mapXY[1][i])){
                    continue;
                }
            }
            map[count] = mapXY[0][i] + mapXY[1][i] * w;
            ++count;
        }
        return map;
    }

    public static boolean insideMap(int x, int y){
        return (x>0 && x<Dungeon.level.width() && y>0 && y<Dungeon.level.height());
    }

    private static int insideTiles(int[][] mapXY){
        int count = 0;
        int l = mapXY[0].length;
        for(int i=0; i<l; ++i){
            if(insideMap(mapXY[0][i], mapXY[1][i]))
                ++count;
        }
        return count;
    }

    public static int[][] buildXYMap(int[] coordinates){
        int l = coordinates.length;
        if(l%2!=0) l--;
        int[][] mapXY = new int[2][l/2];
        for(int i=0; i<coordinates.length; ++i){
            mapXY[i%2][i/2] = coordinates[i];
        }
        return mapXY;
    }
}
