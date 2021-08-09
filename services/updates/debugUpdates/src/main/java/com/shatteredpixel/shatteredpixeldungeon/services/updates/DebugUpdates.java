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

package com.shatteredpixel.shatteredpixeldungeon.services.updates;


import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;

public class DebugUpdates extends UpdateService {

	private static AvailableUpdateData debugUpdateInfo;

	@Override
	public boolean isUpdateable() {
		return false; //turn on to debug update prompts
	}

	@Override
	public boolean supportsBetaChannel() {
		return true;
	}

	@Override
	public void checkForUpdate(boolean useMetered, boolean includeBetas, UpdateResultCallback callback) {

		if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()){
			callback.onConnectionFailed();
			return;
		}

		debugUpdateInfo = new AvailableUpdateData();
		debugUpdateInfo.versionCode = Game.versionCode+1;
		debugUpdateInfo.URL = "http://www.google.com";

		callback.onUpdateAvailable(debugUpdateInfo);

	}

	@Override
	public void initializeUpdate(AvailableUpdateData update) {
		DeviceCompat.openURI( update.URL );
	}

	@Override
	public boolean isInstallable() {
		return false; //turn on to test install prompts
	}

	@Override
	public void initializeInstall() {
		//does nothing
	}

	@Override
	public boolean supportsReviews() {
		return true;
	}

	@Override
	public void initializeReview(ReviewResultCallback callback) {
		//does nothing
		callback.onComplete();
	}

	@Override
	public void openReviewURI() {
		DeviceCompat.openURI("https://www.google.com/");
	}
}
