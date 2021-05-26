/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.elementalpixel.elementalpixeldungeon;


import com.elementalpixel.elementalpixeldungeon.scenes.GameScene;
import com.elementalpixel.elementalpixeldungeon.scenes.PixelScene;
import com.elementalpixel.elementalpixeldungeon.scenes.WelcomeScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PlatformSupport;

public class ShatteredPixelDungeon extends Game {

	//variable constants for specific older versions of shattered, used for data conversion
	//versions older than v0.7.5e are no longer supported, and data from them is ignored
	public static final int v0_0_1b = 1;

	public ShatteredPixelDungeon( PlatformSupport platform ) {
		super( sceneClass == null ? WelcomeScene.class : sceneClass, platform );

		//v0.8.0
		com.watabou.utils.Bundle.addAlias(
				com.elementalpixel.elementalpixeldungeon.actors.mobs.ArmoredBrute.class,
				"com.elementalpixel.elementalpixeldungeon.actors.mobs.Shielded");
		com.watabou.utils.Bundle.addAlias(
				com.elementalpixel.elementalpixeldungeon.actors.mobs.DM100.class,
				"com.elementalpixel.elementalpixeldungeon.actors.mobs.Shaman");
		com.watabou.utils.Bundle.addAlias(
				com.elementalpixel.elementalpixeldungeon.actors.mobs.Elemental.FireElemental.class,
				"com.elementalpixel.elementalpixeldungeon.actors.mobs.Elemental");
		com.watabou.utils.Bundle.addAlias(
				com.elementalpixel.elementalpixeldungeon.actors.mobs.Elemental.NewbornFireElemental.class,
				"com.elementalpixel.elementalpixeldungeon.actors.mobs.NewbornElemental");
		com.watabou.utils.Bundle.addAlias(
				com.elementalpixel.elementalpixeldungeon.actors.mobs.OldDM300.class,
				"com.elementalpixel.elementalpixeldungeon.actors.mobs.DM300");
		com.watabou.utils.Bundle.addAlias(
				com.elementalpixel.elementalpixeldungeon.levels.OldCavesBossLevel.class,
				"com.elementalpixel.elementalpixeldungeon.levels.CavesBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.elementalpixel.elementalpixeldungeon.levels.OldCityBossLevel.class,
				"com.elementalpixel.elementalpixeldungeon.levels.CityBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.elementalpixel.elementalpixeldungeon.levels.OldHallsBossLevel.class,
				"com.elementalpixel.elementalpixeldungeon.levels.HallsBossLevel" );
		
	}
	
	@Override
	public void create() {
		super.create();

		updateSystemUI();
		SPDAction.loadBindings();
		
		Music.INSTANCE.enable( SPDSettings.music() );
		Music.INSTANCE.volume( SPDSettings.musicVol()*SPDSettings.musicVol()/100f );
		Sample.INSTANCE.enable( SPDSettings.soundFx() );
		Sample.INSTANCE.volume( SPDSettings.SFXVol()*SPDSettings.SFXVol()/100f );

		Sample.INSTANCE.load( Assets.Sounds.all );
		
	}

	public static void switchNoFade(Class<? extends PixelScene> c){
		switchNoFade(c, null);
	}

	public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
		PixelScene.noFade = true;
		switchScene( c, callback );
	}
	
	public static void seamlessResetScene(SceneChangeCallback callback) {
		if (scene() instanceof PixelScene){
			((PixelScene) scene()).saveWindows();
			switchNoFade((Class<? extends PixelScene>) sceneClass, callback );
		} else {
			resetScene();
		}
	}
	
	public static void seamlessResetScene(){
		seamlessResetScene(null);
	}
	
	@Override
	protected void switchScene() {
		super.switchScene();
		if (scene instanceof PixelScene){
			((PixelScene) scene).restoreWindows();
		}
	}
	
	@Override
	public void resize( int width, int height ) {
		if (width == 0 || height == 0){
			return;
		}

		if (scene instanceof PixelScene &&
				(height != Game.height || width != Game.width)) {
			PixelScene.noFade = true;
			((PixelScene) scene).saveWindows();
		}

		super.resize( width, height );

		updateDisplaySize();

	}
	
	@Override
	public void destroy(){
		super.destroy();
		GameScene.endActorThread();
	}
	
	public void updateDisplaySize(){
		platform.updateDisplaySize();
	}

	public static void updateSystemUI() {
		platform.updateSystemUI();
	}
}