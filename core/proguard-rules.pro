# retain these to support class references and meaningful stack traces
-keepnames class com.shatteredpixel.** { *; }
-keepnames class com.watabou.** { *; }
-keep class com.shatteredpixel.shatteredpixeldungeon { *; }
-keep class com.watabou.noosa.game { *; }
-keepattributes SourceFile,LineNumberTable

# overrides default in proguard-android-optimize.txt, which is:
# -optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
# This is because code/simplification/arithmetic is safe to use on android 2.0+
-optimizations !code/simplification/cast,!field/*,!class/merging/*