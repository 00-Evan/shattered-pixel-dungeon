/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
	RUSSIAN("русский",      "ru", Status.REVIEWED, new String[]{"ConsideredHamster", "Inevielle", "yarikonline"}, new String[]{"HerrGotlieb", "Shamahan"}),
	KOREAN("한국어",         "ko", Status.REVIEWED, new String[]{"Flameblast12"}, new String[]{"Ddojin0115", "Eeeei", "lsiebnie", "WondarRabb1t"}),

	//Simplified
	CHINESE("中文",          "zh", Status.UNREVIEWED, new String[]{"Jinkeloid(zdx00793)"}, new String[]{"931451545", "HoofBumpBlurryface", "Lyn-0401", "ShatteredFlameBlast", "Tempest102"}),
	//Brazillian
	PORTUGUESE("português", "pt", Status.UNREVIEWED, new String[]{"Matheus208"}, new String[]{"JST", "Try31"}),
	GERMAN("deutsch",       "de", Status.UNREVIEWED, new String[]{"Davedude", "KrystalCroft"}, new String[]{"Dallukas", "DarkPixel", "ErichME", "Sarius", "ThunfischGott", "Zap0", "Oragothen"}),
	FRENCH("français",      "fr", Status.UNREVIEWED, null, new String[]{"Alsydis", "Basttee", "Canc42", "Go11um", "Kultissim", "Minikrob", "Solthaar"}),

	POLISH("polski",        "pl", Status.INCOMPLETE, null, new String[]{"Darden", "Deksippos", "Scharnvirk", "Wawrzyn", "Dusakus", "Michaub"}),
	SPANISH("español",      "es", Status.INCOMPLETE, null, new String[]{"CorvosUtopy", "LucasCamilo", "Luuciano96", "Pecheneg", "Prancer", "Talruin", "Alfongad", "Ctrijueque", "Grayscales", "Jonismack1", "Pixeled4life"}),
	ITALIAN("italiano",		"it", Status.INCOMPLETE, null, new String[]{"4est", "Bizzolino", "Funnydwarf", "Nessunluogo", "Umby000"});

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
