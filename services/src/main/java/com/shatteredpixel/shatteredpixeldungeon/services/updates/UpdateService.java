/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

//TODO with review functionality, this service is about more than just updates
// perhaps rename to PlatformService, StoreService, DistributionService, etc?
public abstract class UpdateService {

	public static abstract class UpdateResultCallback {
		public abstract void onUpdateAvailable( AvailableUpdateData update );
		public abstract void onNoUpdateFound();
		public abstract void onConnectionFailed();
	}

	//whether the service supports offering update notifications via an ingame prompt
	public abstract boolean supportsUpdatePrompts();

	//whether the service supports an opt-in channel for betas
	public abstract boolean supportsBetaChannel();

	public abstract void checkForUpdate( boolean useMetered, boolean includeBetas, UpdateResultCallback callback );

	public abstract void initializeUpdate( AvailableUpdateData update );

	public static abstract class ReviewResultCallback {
		public abstract void onComplete();
	}

	//whether the service supports prompts to review the game via and ingame prompt
	public abstract boolean supportsReviews();

	public abstract void initializeReview( ReviewResultCallback callback );

	public abstract void openReviewURI();

}
