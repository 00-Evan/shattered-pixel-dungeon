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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.WelcomeScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PlatformSupport;

public class ShatteredPixelDungeon extends Game {

	//variable constants for specific older versions of shattered, used for data conversion
	//versions older than v0.8.0b are no longer supported, and data from them is ignored
	public static final int v0_8_0b = 414;
	public static final int v0_8_1a = 422;
	public static final int v0_8_2d = 463;

	public static final int v0_9_0b  = 489;
	public static final int v0_9_1d  = 511;
	public static final int v0_9_2b  = 532;
	public static final int v0_9_3   = 544;
	
	public ShatteredPixelDungeon( PlatformSupport platform ) {
		super( sceneClass == null ? WelcomeScene.class : sceneClass, platform );

		//v0.9.3
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu.class,
				"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewTengu" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.PrisonBossLevel.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewPrisonBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.PrisonBossLevel.ExitVisual.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewPrisonBossLevel$exitVisual" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.PrisonBossLevel.ExitVisualWalls.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewPrisonBossLevel$exitVisualWalls" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300.class,
				"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM300" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel.PylonEnergy.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel$PylonEnergy" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel.ArenaVisuals.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel$ArenaVisuals" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel.CityEntrance.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel$CityEntrance" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel.EntranceOverhang.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel$EntranceOverhang" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.CityBossLevel.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewCityBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.CityBossLevel.CustomGroundVisuals.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewCityBossLevel$CustomGroundVisuals" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.CityBossLevel.CustomWallVisuals.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewCityBossLevel$CustomWallVisuals" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.HallsBossLevel.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewHallsBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.HallsBossLevel.CenterPieceVisuals.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewHallsBossLevel$CenterPieceWalls" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.levels.HallsBossLevel.CenterPieceWalls.class,
				"com.shatteredpixel.shatteredpixeldungeon.levels.NewHallsBossLevel$CenterPieceWalls" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.items.Waterskin.class,
				"com.shatteredpixel.shatteredpixeldungeon.items.DewVial" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.items.TengusMask.class,
				"com.shatteredpixel.shatteredpixeldungeon.items.TomeOfMastery" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown.class,
				"com.shatteredpixel.shatteredpixeldungeon.items.ArmorKit" );
		
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