/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.messages;

import java.util.Locale;

public enum Languages {
	ENGLISH("english",      "", Status.REVIEWED, null, null),

	RUSSIAN("русский",      "ru", Status.REVIEWED, new String[]{"ConsideredHamster", "Inevielle", "yarikonline"}, new String[]{"AttHawk46", "HerrGotlieb", "Shamahan"}),
	KOREAN("한국어",         "ko", Status.REVIEWED, new String[]{"Flameblast12"}, new String[]{"Ddojin0115", "Eeeei", "lsiebnie", "WondarRabb1t"}),
	CHINESE("中文",          "zh", Status.REVIEWED, new String[]{"Jinkeloid(zdx00793)"}, new String[]{"931451545", "HoofBumpBlurryface", "Lery", "Lyn-0401", "ShatteredFlameBlast", "Hmdzl001", "Tempest102"}),

	GERMAN("deutsch",       "de", Status.UNREVIEWED, new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Davedude" }, new String[]{"DarkPixel", "ErichME", "Sarius", "Sorpl3x", "ThunfischGott", "Zap0", "Oragothen"}),
	FRENCH("français",      "fr", Status.UNREVIEWED, new String[]{"Emether", "Canc42", "Kultissim"}, new String[]{"Alsydis", "Basttee", "Go11um", "Minikrob", "Solthaar"}),
	SPANISH("español",      "es", Status.UNREVIEWED, new String[]{"Kiroto", "Grayscales"}, new String[]{"Alesxanderk", "CorvosUtopy", "Dewstend", "Dyrran", "Fervoreking", "Illyatwo2", "Kohru", "Alfongad", "Ctrijueque", "Dhg121", "Jonismack1"}),
	ITALIAN("italiano",		"it", Status.UNREVIEWED, new String[]{"Bizzolino", "Funnydwarf"}, new String[]{"4est", "DaniMare", "Danzl", "Nessunluogo", "Umby000"}),

	PORTUGUESE("português", "pt", Status.INCOMPLETE, new String[]{"Matheus208"}, new String[]{"ChainedFreaK", "JST", "TDF2001", "Try31"}),
	POLISH("polski",        "pl", Status.INCOMPLETE, null, new String[]{"Darden", "Deksippos", "MJedi", "Scharnvirk", "Shmilly", "Dusakus", "Kuadziw", "Michaub", "Ozziezombie", "Szymex73"}),
	HUNGARIAN("magyar",     "hu", Status.INCOMPLETE, new String[]{"Dorheim"}, new String[]{"Clarovani"}),
	FINNISH("suomi", 		"fi", Status.INCOMPLETE, null, new String[]{"TenguTheKnight"} );

	public enum Status{
		//below 60% complete languages are not added.
		INCOMPLETE, //60-99% complete
		UNREVIEWED, //100% complete
		REVIEWED    //100% reviewed
	}

	private String name;
	private String code;
	private Status status;
	private String[] reviewers;
	private String[] translators;

	Languages(String name, String code, Status status, String[] reviewers, String[] translators){
		this.name = name;
		this.code = code;
		this.status = status;
		this.reviewers = reviewers;
		this.translators = translators;
	}

	public String nativeName(){
		return name;
	}

	public String code(){
		return code;
	}

	public Status status(){
		return status;
	}

	public String[] reviewers() {
		if (reviewers == null) return new String[]{};
		else return reviewers.clone();
	}

	public String[] translators() {
		if (translators == null) return new String[]{};
		else return translators.clone();
	}

	public static Languages matchLocale(Locale locale){
		return matchCode(locale.getLanguage());
	}

	public static Languages matchCode(String code){
		for (Languages lang : Languages.values()){
			if (lang.code().equals(code))
				return lang;
		}
		return ENGLISH;
	}

}
