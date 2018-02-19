/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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
	
	CZECH("čeština",        "cs", Status.REVIEWED, new String[]{"ObisMike"}, new String[]{"AshenShugar", "Buba237", "JStrange", "RealBrofessor", "chuckjirka"}),
	TURKISH("türkçe",       "tr", Status.REVIEWED, new String[]{"LokiofMillenium", "emrebnk"}, new String[]{"AcuriousPotato", "alpekin98", "denizakalin", "melezorus34"}),
	CATALAN("català",       "ca", Status.REVIEWED, new String[]{"Illyatwo2"}, null),

	KOREAN("한국어",         "ko", Status.UNREVIEWED, new String[]{"Flameblast12"}, new String[]{"Korean2017", "WondarRabb1t", "ddojin0115", "eeeei", "hancyel", "linterpreteur", "lsiebnie" }),
	RUSSIAN("русский",      "ru", Status.UNREVIEWED, new String[]{"ConsideredHamster", "Inevielle", "yarikonline"}, new String[]{"AttHawk46", "HerrGotlieb", "HoloTheWise", "MrXantar", "Shamahan", "roman.yagodin", "un_logic", " Вoвa"}),
	POLISH("polski",        "pl", Status.UNREVIEWED, new String[]{"Deksippos", "kuadziw"}, new String[]{"Chasseur", "Darden", "MJedi", "MrKukurykpl", "Peperos", "Scharnvirk", "Shmilly", "dusakus", "michaub", "ozziezombie", "szczoteczka22", "szymex73"}),
	ITALIAN("italiano",		"it", Status.UNREVIEWED, new String[]{"bizzolino", "funnydwarf"}, new String[]{"4est", "DaniMare", "Danzl", "andrearubbino00", "nessunluogo", "righi.a", "umby000"}),
	CHINESE("中文",          "zh", Status.UNREVIEWED, new String[]{"Jinkeloid(zdx00793)"}, new String[]{"931451545", "HoofBumpBlurryface", "Lery", "Lyn-0401", "ShatteredFlameBlast", "hmdzl001", "tempest102"}),
	ESPERANTO("esperanto",  "eo", Status.UNREVIEWED, new String[]{"Verdulo"}, new String[]{"Raizin"}),

	GERMAN("deutsch",       "de", Status.INCOMPLETE, new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Zap0", "davedude" }, new String[]{"DarkPixel", "ErichME", "LenzB", "Sarius", "Sorpl3x", "ThunfischGott", "Topicranger", "oragothen"}),
	FRENCH("français",      "fr", Status.INCOMPLETE, new String[]{"Emether", "canc42", "kultissim", "minikrob"}, new String[]{"Alsydis", "Basttee", "Dekadisk", "Draal", "antoine9298", "go11um", "linterpreteur", "solthaar"}),
	SPANISH("español",      "es", Status.INCOMPLETE, new String[]{"Kiroto", "Kohru", "grayscales"}, new String[]{"Alesxanderk", "CorvosUtopy", "Dewstend", "Dyrran", "Fervoreking", "Illyatwo2", "airman12", "alfongad", "benzarr410", "ctrijueque", "dhg121", "javifs", "jonismack1"}),
	PORTUGUESE("português", "pt", Status.INCOMPLETE, new String[]{"TDF2001", "matheus208"}, new String[]{"ChainedFreaK", "JST", "MadHorus", "Tio_P_", "ancientorange", "danypr23", "ismael.henriques12", "owenreilly", "try31"}),
	FINNISH("suomi", 		"fi", Status.INCOMPLETE, new String[]{"TenguTheKnight"}, null ),
	HUNGARIAN("magyar",     "hu", Status.INCOMPLETE, new String[]{"dorheim"}, new String[]{"Navetelen", "clarovani", "dhialub", "nanometer", "nardomaa"}),
	INDONESIAN("indonésien","in", Status.INCOMPLETE, new String[]{"rakapratama"}, null);

	public enum Status{
		//below 80% complete languages are not added.
		INCOMPLETE, //80-99% complete
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
