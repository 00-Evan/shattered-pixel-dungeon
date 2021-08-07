package com.shatteredpixel.shatteredpixeldungeon.ios;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.news.NewsImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.UpdateImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.Updates;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSException;
import org.robovm.apple.glkit.GLKViewDrawableColorFormat;
import org.robovm.apple.glkit.GLKViewDrawableDepthFormat;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate {
	@Override
	protected IOSApplication createApplication() {

		//ensures the app actually crashes if there's an error in the mobiVM runtime
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable ex) {
				new NSException(ex.getClass().getName(), ex.getMessage(), new NSDictionary()).raise();
			}
		});

		try {
			Game.version = NSBundle.getMainBundle().getInfoDictionaryObject("CFBundleVersionString").toString();
		} catch (Exception e) {
			Game.version = "???";
		}
		try {
			Game.versionCode = Integer.parseInt(NSBundle.getMainBundle().getInfoDictionaryObject("CFBundleVersion").toString());
		} catch (Exception e) {
			Game.versionCode = 0;
		}

		if (UpdateImpl.supportsUpdates()) {
			Updates.service = UpdateImpl.getUpdateService();
		}
		if (NewsImpl.supportsNews()) {
			News.service = NewsImpl.getNewsService();
		}

		FileUtils.setDefaultFileProperties(Files.FileType.Local, "");

		IOSApplicationConfiguration config = new IOSApplicationConfiguration();

		config.colorFormat = GLKViewDrawableColorFormat.RGBA8888;
		config.depthFormat = GLKViewDrawableDepthFormat.None;
		config.hdpiMode = HdpiMode.Pixels;

		CGRect statusBarFrame = UIApplication.getSharedApplication().getStatusBarFrame();
		double statusBarHeight = Math.min(statusBarFrame.getWidth(), statusBarFrame.getHeight());

		//if the application has a short status bar (no notch), then hide it
		//TODO we do this check elsewhere now, can this be removed?
		if (statusBarHeight <= 24) {
			UIApplication.getSharedApplication().setStatusBarHidden(true);
		}

		config.useAccelerometer = false;
		config.useCompass = false;

		//devices not currently listed in LibGDX's IOSDevice class
		config.addIosDevice("IPHONE_12_MINI", "iPhone13,1", 476);
		config.addIosDevice("IPHONE_12", "iPhone13,2", 460);
		config.addIosDevice("IPHONE_12_PRO", "iPhone13,3", 460);
		config.addIosDevice("IPHONE_12_PRO_MAX", "iPhone13,4", 458);

		config.addIosDevice("IPAD_7G_WIFI", "iPad7,11", 264);
		config.addIosDevice("IPAD_7G_WIFI_CELLULAR", "iPad7,12", 264);

		config.addIosDevice("IPAD_8G_WIFI", "iPad11,6", 264);
		config.addIosDevice("IPAD_8G_WIFI_CELLULAR", "iPad11,7", 264);
		config.addIosDevice("IPAD_AIR_4G_WIFI", "iPad13,1", 264);
		config.addIosDevice("IPAD_AIR_4G_WIFI_CELLULAR", "iPad13,2", 264);
		config.addIosDevice("IPAD_PRO_11_3G", "iPad13,4", 264);
		config.addIosDevice("IPAD_PRO_11_3G", "iPad13,5", 264);
		config.addIosDevice("IPAD_PRO_11_3G", "iPad13,6", 264);
		config.addIosDevice("IPAD_PRO_11_3G", "iPad13,7", 264);
		config.addIosDevice("IPAD_PRO_12.8_5G", "iPad13,8", 264);
		config.addIosDevice("IPAD_PRO_12.8_5G", "iPad13,9", 264);
		config.addIosDevice("IPAD_PRO_12.8_5G", "iPad13,10", 264);
		config.addIosDevice("IPAD_PRO_12.8_5G", "iPad13,11", 264);

		return new IOSApplication(new ShatteredPixelDungeon(new IOSPlatformSupport()), config);
	}

	public static void main(String[] argv) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, IOSLauncher.class);
		pool.close();
	}
}
