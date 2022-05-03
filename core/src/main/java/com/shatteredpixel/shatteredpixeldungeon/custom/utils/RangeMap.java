package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

//This util class is used for describing range
//for example, returns a range within + directions <=2 , pos centered. WONT OUTSIDE OF MAP.
//Add new methods once necessary.
public class RangeMap {
    // M : distance for + directions
    // D : distance for x directions
    // C : distance for 8 directions

    private static int[] build(int pos, int[][]mapXY){
        int xOfs = pos % Dungeon.level.width();
        int yOfs = pos / Dungeon.level.width();
        for(int i=0;i<mapXY[0].length;++i){
            mapXY[0][i] += xOfs;
            mapXY[1][i] += yOfs;
        }
        return MapXY.ToLength(mapXY, true);
    }

    public static int[] arrayCopy(int[]... arrays){
        int arrayLength = 0;
        int startIndex = 0;
        for(int[] file : arrays){
            arrayLength = arrayLength + file.length;
        }
        int[] fileArray = new int[arrayLength];
        for(int i = 0; i < arrays.length; i++){
            if(i > 0){
                startIndex = startIndex + arrays[i-1].length;
            }
            System.arraycopy(arrays[i], 0, fileArray, startIndex, arrays[i].length);
        }
        return fileArray;
    }

    public static int[] M0(int pos){
        return new int[]{pos};
    }

    public static int[] M1(int pos){
        return build(pos,
                MapXY.buildXYMap(
                        new int[]{
                                -1, 0, 0, 1, 1, 0, 0, -1
                        }
                )
        );
    }

    public static int[] D1(int pos){
        return build(pos,
                MapXY.buildXYMap(
                        new int[]{
                                -1, -1, 1, 1, 1, -1, -1, 1
                        }
                )
        );
    }

    public static int[] C1(int pos){
        return arrayCopy(M1(pos), D1(pos));
    }

    public static int[] M2(int pos){
        return arrayCopy(C1(pos),
                build(pos,
                        MapXY.buildXYMap(
                                new int[]{
                                        -2, 0, 2, 0, 0, -2, 0, 2
                                }
                        )
                )
        );
    }

    public static int[] centeredRect(int center, int w, int h){
        if(w<0 || h<0) return new int[]{center};
        if(w*h>1000000) return new int[]{center}; //too large, might fail;
        int[] xyMap = new int[2*(2*w+1)*(2*h+1)];
        int count = 0;
        for(int i=-w;i<=w;++i){
            for(int j=-h;j<=h;++j){
                xyMap[2*count]=i;
                xyMap[2*count+1]=j;
                count ++;
            }
        }
        return build(center, MapXY.buildXYMap(xyMap));
    }

    public static int[] manhattanCircle(int center, int dist){
        if(dist<0 || dist>300) return null;
        if(dist==0){
            return new int[]{center};
        }
        int[] xyMap = new int[2*4*dist];
        //-0 = +0, so list by hand
        int index = 0;
        final int[] zeroTile = new int[]{dist, 0, -dist, 0, 0, dist, 0, -dist};
        for(int i: zeroTile){
            xyMap[index]=i;
            ++index;
        }

        final int[] signX = new int[]{-1, -1, 1, 1};
        final int[] signY = new int[]{-1, 1, -1, 1};
        for(int x = dist - 1; x > 0; --x){
            for(int i=0; i<4;++i){
                xyMap[index]=x*signX[i];
                xyMap[index+1]=(dist-x)*signY[i];
                index += 2;
            }
        }

        return build(center, MapXY.buildXYMap(xyMap));
    }

    public static int[] manhattanRing(int center, int minRange, int maxRange){
        minRange = Math.max(minRange, 0);
        if(maxRange > minRange){
            return arrayCopy(manhattanCircle(center, maxRange), manhattanRing(center, minRange, maxRange-1));
        }else if(maxRange == minRange){
            return manhattanCircle(center, maxRange);
        }
        return new int[]{center};
    }

    public static int manhattanDist(int a, int b){
        int w = Dungeon.level.width();
        return Math.abs(a/w-b/w)+Math.abs(a%w-b%w);
    }
}
