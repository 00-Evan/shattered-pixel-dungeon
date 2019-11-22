/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

public class Updates {

	public static UpdateService service;

	public static boolean supportsUpdates(){
		return service != null;
	}

	private static boolean updateChecked = false;

	public static void checkForUpdate(){
		if (!supportsUpdates() || updateChecked) return;
		service.checkForUpdate(new UpdateService.UpdateResultCallback() {
			@Override
			public void onUpdateAvailable(AvailableUpdateData update) {
				updateChecked = true;
				updateData = update;
			}

			@Override
			public void onNoUpdateFound() {
				updateChecked = true;
			}

			@Override
			public void onConnectionFailed() {
				updateChecked = false;
			}
		});
	}

	public static void launchUpdate( AvailableUpdateData data ){
		service.initializeUpdate( data );
	}

	private static AvailableUpdateData updateData = null;

	public static boolean updateAvailable(){
		return updateData != null;
	}

	public static AvailableUpdateData updateData(){
		return updateData;
	}

}
