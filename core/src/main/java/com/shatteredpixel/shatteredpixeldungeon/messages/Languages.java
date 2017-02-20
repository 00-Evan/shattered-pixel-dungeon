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

	RUSSIAN("русский",      "ru", Status.REVIEWED, new String[]{"ConsideredHamster", "Inevielle", "yarikonline"}, new String[]{"AttHawk46", "HerrGotlieb", "Shamahan", "un_logic"}),
	KOREAN("한국어",         "ko", Status.REVIEWED, new String[]{"Flameblast12"}, new String[]{"WondarRabb1t", "ddojin0115", "eeeei", "linterpreteur", "lsiebnie" }),
	CHINESE("中文",          "zh", Status.REVIEWED, new String[]{"Jinkeloid(zdx00793)"}, new String[]{"931451545", "HoofBumpBlurryface", "Lery", "Lyn-0401", "ShatteredFlameBlast", "hmdzl001", "tempest102"}),
	FINNISH("suomi", 		"fi", Status.REVIEWED, new String[]{"TenguTheKnight"}, null ),
	POLISH("polski",        "pl", Status.REVIEWED, new String[]{"Deksippos", "kuadziw"}, new String[]{"Chasseur", "Darden", "MJedi", "Scharnvirk", "Shmilly", "dusakus", "michaub", "ozziezombie", "szczoteczka22", "szymex73"}),

	GERMAN("deutsch",       "de", Status.UNREVIEWED, new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Zap0", "davedude" }, new String[]{"DarkPixel", "ErichME", "Sarius", "Sorpl3x", "ThunfischGott", "oragothen"}),
	FRENCH("français",      "fr", Status.UNREVIEWED, new String[]{"Emether", "canc42", "kultissim", "minikrob"}, new String[]{"Alsydis", "Basttee", "Draal", "go11um", "linterpreteur", "solthaar"}),
	ITALIAN("italiano",		"it", Status.UNREVIEWED, new String[]{"bizzolino", "funnydwarf"}, new String[]{"4est", "DaniMare", "Danzl", "andrearubbino00", "nessunluogo", "umby000"}),
	HUNGARIAN("magyar",     "hu", Status.UNREVIEWED, new String[]{"dorheim"}, new String[]{"Navetelen", "clarovani", "dhialub", "nanometer", "nardomaa"}),
	SPANISH("español",      "es", Status.UNREVIEWED, new String[]{"Kiroto", "Kohru", "grayscales"}, new String[]{"Alesxanderk", "CorvosUtopy", "Dewstend", "Dyrran", "Fervoreking", "Illyatwo2", "alfongad", "benzarr410", "ctrijueque", "dhg121", "javifs", "jonismack1"}),
	PORTUGUESE("português", "pt", Status.UNREVIEWED, new String[]{"TDF2001", "matheus208"}, new String[]{"ChainedFreaK", "JST", "MadHorus", "danypr23", "ismael.henriques12", "try31"}),
	ESPERANTO("esperanto",  "eo", Status.UNREVIEWED, new String[]{"Verdulo"}, null),
	INDONESIAN("indonésien","in", Status.UNREVIEWED, new String[]{"rakapratama"}, null);

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
