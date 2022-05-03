package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

public class SeedUtil {

    //Here we regard 'A' as 0, 'Z' as 25.
    public static long directConvert(String code, char baseCode, int radix){
        long total = 0;
        for(char c: code.toCharArray()){
            c -= baseCode;
            total *= radix;
            total += c;
        }
        if(total < 0){
            total += Long.MAX_VALUE;
        }
        return total % 5429503678976L;
    }
}
