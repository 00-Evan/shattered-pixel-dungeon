These sources are provided for FOSS completeness, but compiling them is optional.

The folder jniLibs already contains compiled versions of FroyoGLES20Fix.c, and makes compiling
The sources here an optional step in building Shattered Pixel Dungeon. This is done so that
The Android NDK is not required for project compilation, but those who want to ensure they are 100%
building from source can still do so.

There is no functional difference between using the provided .so files, and compiling your own.

These sources can be compiled through the gradle task SPD-classes:ndkBuild on a system with the
Android NDK installed and configured.