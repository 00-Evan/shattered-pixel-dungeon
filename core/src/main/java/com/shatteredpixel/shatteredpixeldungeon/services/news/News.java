/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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

import java.util.ArrayList;
import java.util.Date;

public class News {

	public static NewsService service;

	public static boolean supportsNews(){
		return service != null;
	}

	private static Date lastCheck = null;
	private static final long CHECK_DELAY = 1000*60*60; //1 hour

	public static void checkForNews(){
		if (!supportsNews()) return;
		if (lastCheck != null && (new Date().getTime() - lastCheck.getTime()) < CHECK_DELAY) return;

		service.checkForArticles(new NewsService.NewsResultCallback() {
			@Override
			public void onArticlesFound(ArrayList<NewsArticle> articles) {
				lastCheck = new Date();
				News.articles = articles;
			}

			@Override
			public void onConnectionFailed() {
				lastCheck = null;
				News.articles = null;
			}
		});

	}

	private static ArrayList<NewsArticle> articles;

	public static boolean articlesAvailable(){
		return articles != null;
	}

	public static ArrayList<NewsArticle> articles(){
		return articles;
	}

	public static int unreadArticles(Date lastRead){
		int unread = 0;
		for (NewsArticle article : articles){
			if (article.date.after(lastRead)) unread++;
		}
		return unread;
	}



}
