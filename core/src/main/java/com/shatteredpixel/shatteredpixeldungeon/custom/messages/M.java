package com.shatteredpixel.shatteredpixeldungeon.custom.messages;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class M {
    public static String L(Class<?> c, String k, Object...args){
        String s = Messages.get(c, k, args);
        if(s.contains("!!!NO TEXT FOUND!!!")){
            String key;
            if (c != null){
                key = c.getName();
                int lastPoint = key.lastIndexOf('.');
                key = key.substring(lastPoint);
                key += "." + k;
            } else
                key = k;

            return "m:" + key;
        }
        return s;
    }

    public static String L(Object o, String k, Object...args){
        return L(o.getClass(), k, args);
    }

    public static String L(String key, Object...args){
        return L(null, key, args);
    }

    public static String C(String string) {return Messages.capitalize(string);}
    public static String CL(Class<?> c, String k, Object...args){return C(L(c,k,args));}
    public static String CL(Object o, String k, Object...args){return C(L(o, k, args));}
    public static String CL(String key, Object...args){return C(L(key, args));}

    public static String T(String string){return Messages.titleCase(string);}
    public static String TL(Class<?> c, String k, Object...args){return T(L(c,k,args));}
    public static String TL(Object o, String k, Object...args){return T(L(o, k, args));}
    public static String TL(String key, Object...args){return T(L(key, args));}

    public static String F(String format, Object...args){return Messages.format(format, args);}
}
