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

//TODO with install and review functionality, this service is less and less just about updates
// perhaps rename to PlatformService, StoreService, DistributionService, etc?
public abstract class UpdateService {

	public static abstract class UpdateResultCallback {
		public abstract void onUpdateAvailable( AvailableUpdateData update );
		public abstract void onNoUpdateFound();
		public abstract void onConnectionFailed();
	}

	//whether the app is updateable via an ingame prompt (e.g. not a demo or an android instant app)
	public abstract boolean isUpdateable();

	//whether the service supports an opt-in channel for betas
	public abstract boolean supportsBetaChannel();

	public abstract void checkForUpdate( boolean useMetered, boolean includeBetas, UpdateResultCallback callback );

	public abstract void initializeUpdate( AvailableUpdateData update );

	//whether the app installable via an ingame prompt (e.g. a demo, or an android instant app)
	public abstract boolean isInstallable();

	public abstract void initializeInstall();

	public static abstract class ReviewResultCallback {
		public abstract void onComplete();
	}

	public abstract boolean supportsReviews();

	public abstract void initializeReview( ReviewResultCallback callback );

	public abstract void openReviewURI();

}
