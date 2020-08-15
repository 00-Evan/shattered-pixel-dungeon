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

package com.shatteredpixel.shatteredpixeldungeon.services.news;

import com.watabou.noosa.Game;

import java.util.ArrayList;
import java.util.Date;

public class DebugNews extends NewsService {

	@Override
	public void checkForArticles(boolean useMetered, boolean forceHTTPS, NewsResultCallback callback) {

		if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()){
			callback.onConnectionFailed();
			return;
		}

		//turn on to test connection failure
		if (false){
			callback.onConnectionFailed();
			return;
		}

		boolean testUnread = false;
		//start placing articles either at the current time (if testing unread count)
		// or 10 days after 1st jan 1970
		long startTime = testUnread ? Game.realTime : 10*1000*60*60*24;

		ArrayList<NewsArticle> articles = new ArrayList<>();
		for (int i = 0; i < 10; i++){
			NewsArticle article = new NewsArticle();
			article.title = "TEST ARTICLE " + i;
			article.summary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
					"eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
					"veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea " +
					"commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit " +
					"esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat " +
					"non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
			// 10 to 1 days after Jan 1st 1970
			article.date = new Date(startTime - (i)*1000*60*60*24);

			article.URL = "http://www.google.com";

			//debug icon!
			article.icon = "sprites/spinner.png, 144, 0, 16, 16";

			articles.add(article);
		}

		callback.onArticlesFound(articles);

	}
}
