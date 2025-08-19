package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.news.NewsImpl;
import com.sofiastation.sofiastation.services.updates.UpdateImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.Updates;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Point;

import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

public class DesktopLauncher {

    public static void main(String[] args) {

        if (!DesktopLaunchValidator.verifyValidJVMState(args)) {
            return;
        }

        // FreeBSD detection patch
        if (System.getProperty("os.name").contains("FreeBSD")) {
            SharedLibraryLoader.isLinux = true;
            SharedLibraryLoader.isIos = false;
            SharedLibraryLoader.is64Bit =
                    System.getProperty("os.arch").contains("64")
                    || System.getProperty("os.arch").startsWith("armv8");
        }

        // Ensure Game.version is set early
        Game.version = DesktopLauncher.class.getPackage().getSpecificationVersion();
        if (Game.version == null) {
            Game.version = System.getProperty("Specification-Version", "1.0.0");
        }

        // Ensure Game.versionCode is set
        try {
            String implVersion = DesktopLauncher.class.getPackage().getImplementationVersion();
            if (implVersion == null) {
                implVersion = System.getProperty("Implementation-Version", "1");
            }
            Game.versionCode = Integer.parseInt(implVersion);
        } catch (NumberFormatException e) {
            Game.versionCode = 1;
        }

        // Title
        final String title = (DesktopLauncher.class.getPackage().getSpecificationTitle() != null)
                ? DesktopLauncher.class.getPackage().getSpecificationTitle()
                : System.getProperty("Specification-Title", "Sofia Station");

        // Vendor
        String vendor = DesktopLauncher.class.getPackage().getImplementationTitle();
        if (vendor == null) {
            vendor = System.getProperty("Implementation-Title", "default.vendor");
        }
        String[] vendorParts = vendor.split("\\.");
        vendor = vendorParts.length > 1 ? vendorParts[1] : vendorParts[0];

        // Debug logging
        System.out.println("=== DEBUG LAUNCH INFO ===");
        System.out.println("Title: " + title);
        System.out.println("Version: " + Game.version);
        System.out.println("VersionCode: " + Game.versionCode);
        System.out.println("Vendor: " + vendor);
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        System.out.println("=========================");

        // Global exception handler
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Game.reportException(throwable);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            pw.flush();
            String exceptionMsg = sw.toString()
                    .replaceAll("\\(.*:([0-9]*)\\)", "($1)")
                    .replace("com.shatteredpixel.shatteredpixeldungeon.", "")
                    .replace("com.watabou.", "")
                    .replace("com.badlogic.gdx.", "")
                    .replace("\t", "  ")
                    .replace('\'', '’')
                    .replace('"', '”');

            if (exceptionMsg.length() > 1000) {
                exceptionMsg = exceptionMsg.substring(0, 1000) + "...";
            }

            String header = title + " Has Crashed!";
            String details;
            if (exceptionMsg.contains("Couldn’t create window")) {
                details = title + " was not able to initialize its graphics display, sorry about that!\n\n" +
                        "This usually happens when your graphics card has misconfigured drivers or does not support OpenGL 2.0+.\n\n" +
                        "If you are certain the game should work on your computer, please message the developer (support@sofiastation.com)\n\n" +
                        "version: " + Game.version + "\n" + exceptionMsg;
            } else {
                details = title + " has run into an error it cannot recover from and has crashed, sorry about that!\n\n" +
                        "If you could, please email this error message to the developer (support@sofiastation.com):\n\n" +
                        "version: " + Game.version + "\n" + exceptionMsg;
            }
            TinyFileDialogs.tinyfd_messageBox(header, details, "ok", "error", false);
            System.exit(1);
        });

        if (UpdateImpl.supportsUpdates()) {
            Updates.service = UpdateImpl.getUpdateService();
        }
        if (NewsImpl.supportsNews()) {
            News.service = NewsImpl.getNewsService();
        }

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle(title);

        String basePath = "";
        Files.FileType baseFileType = null;

        if (SharedLibraryLoader.isWindows) {
            if (System.getProperty("os.name").equals("Windows XP")) {
                basePath = "Application Data/." + vendor + "/" + title + "/";
            } else {
                basePath = "AppData/Roaming/." + vendor + "/" + title + "/";
            }
            baseFileType = Files.FileType.External;
        } else if (SharedLibraryLoader.isMac) {
            basePath = "Library/Application Support/" + title + "/";
            baseFileType = Files.FileType.External;
        } else if (SharedLibraryLoader.isLinux) {
            String XDGHome = System.getenv("XDG_DATA_HOME");
            if (XDGHome == null) {
                XDGHome = System.getProperty("user.home") + "/.local/share";
            }
            String titleLinux = title.toLowerCase(Locale.ROOT).replace(" ", "-");
            basePath = XDGHome + "/." + vendor + "/" + titleLinux + "/";
            baseFileType = Files.FileType.Absolute;
        }

        config.setPreferencesConfig(basePath, baseFileType);
        SPDSettings.set(new Lwjgl3Preferences(
                new Lwjgl3FileHandle(basePath + SPDSettings.DEFAULT_PREFS_FILE, baseFileType)));
        FileUtils.setDefaultFileProperties(baseFileType, basePath);

        config.setWindowSizeLimits(720, 400, -1, -1);
        Point p = SPDSettings.windowResolution();
        config.setWindowedMode(p.x, p.y);
        config.setMaximized(SPDSettings.windowMaximized());

        DesktopWindowListener listener = new DesktopWindowListener();
        config.setWindowListener(listener);

        config.setWindowIcon("icons/icon_16.png", "icons/icon_32.png", "icons/icon_48.png",
                "icons/icon_64.png", "icons/icon_128.png", "icons/icon_256.png");

        new Lwjgl3Application(new ShatteredPixelDungeon(new DesktopPlatformSupport()), config);
    }
}
