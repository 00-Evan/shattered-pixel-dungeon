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

package com.shatteredpixel.shatteredpixeldungeon.messages;

import java.util.Locale;

public enum Languages {
	ENGLISH("english",      "",   Status.REVIEWED,   null, null),
	KOREAN("한국어",        "ko", Status.UNREVIEWED, new String[]{"Cocoa", "Flameblast12", "GameConqueror", "Korean2017"}, new String[]{"WondarRabb1t", "ddojin0115", "eeeei", "hancyel", "linterpreteur", "lsiebnie"}),
	CHINESE("中文",         "zh", Status.UNREVIEWED, new String[]{"Jinkeloid(zdx00793)", "endlesssolitude"}, new String[]{"931451545", "Fishbone", "HoofBumpBlurryface", "Lery", "Lyn_0401", "ShatteredFlameBlast", "hmdzl001", "tempest102"}),
	RUSSIAN("русский",      "ru", Status.REVIEWED,   new String[]{"ConsideredHamster", "Inevielle", "apxwn", "yarikonline" }, new String[]{"AttHawk46", "BlueberryShortcake", "HerrGotlieb", "HoloTheWise", "Ilbko", "JleHuBbluKoT", "MrXantar", "Originalej0name", "Raymundo", "Shamahan", "kirusyaga", "perefrazz", "roman.yagodin", "un_logic", "Вoвa"}),
	POLISH("polski",        "pl", Status.REVIEWED,   new String[]{"Deksippos", "MrKukurykpl", "kuadziw", "szymex73"}, new String[]{"Chasseur", "Darden", "DarkKnightComes", "KarixDaii", "MJedi", "Odiihinia", "Peperos", "Scharnvirk", "VasteelXolotl", "bvader95", "dusakus", "michaub", "ozziezombie", "szczoteczka22", "transportowiec96"}),
	SPANISH("español",      "es", Status.UNREVIEWED, new String[]{"Kiroto", "Kohru", "airman12", "grayscales"}, new String[]{"AdventurerKilly", "Alesxanderk", "CorvosUtopy", "Dewstend", "Dyrran", "Fervoreking", "Illyatwo2", "JPCHZ", "LastCry", "STKmonoqui", "alfongad", "benzarr410", "chepe567.jc", "ctrijueque", "damc0616", "dhg121", "javifs", "jonismack1", "magmax", "tres.14159"}),
	GERMAN("deutsch",       "de", Status.REVIEWED,   new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Zap0", "apxwn", "bernhardreiter", "davedude"}, new String[]{"Abracadabra", "Ceeee", "DarkPixel", "ErichME", "Faquarl", "LenzB", "Ordoviz", "Sarius", "SirEddi", "Sorpl3x", "ThunfischGott", "Topicranger", "azrdev", "carrageen", "gekko303", "johannes.schobel", "karoshi42", "koryphea", "luciocarreras", "niemand", "oragothen", "spixi"}),
	FRENCH("français",      "fr", Status.UNREVIEWED, new String[]{"Emether", "TheKappaDuWeb", "Xalofar", "canc42", "kultissim", "minikrob"}, new String[]{"Alsydis", "Axce", "Az_zahr", "Bastien72", "Basttee", "Dekadisk", "Draal", "Neopolitan", "Nyrnx", "Petit_Chat", "RomTheMareep", "RunningColours", "SpeagleZNT", "Tronche2Cake", "VRad", "Ygdrazil", "antoine9298", "clexanis", "go11um", "levilbatard", "linterpreteur", "maeltur70", "marmous", "mluzarreta", "solthaar", "speagle", "vavavoum"}),
	PORTUGUESE("português", "pt", Status.UNREVIEWED, new String[]{"Chacal.Ex", "TDF2001", "matheus208"}, new String[]{"Bigode935", "ChainedFreaK", "Helen0903", "JST", "MadHorus", "Matie", "Tio_P_(Krampus)", "ancientorange", "danypr23", "denis.gnl", "ismael.henriques12", "mfcord", "owenreilly", "rafazago", "try31"}),
	ITALIAN("italiano",		"it", Status.INCOMPLETE, new String[]{"bizzolino", "funnydwarf"}, new String[]{"4est", "DaniMare", "Danzl", "andrearubbino00", "nessunluogo", "righi.a", "umby000"}),
	CZECH("čeština",        "cs", Status.REVIEWED,   new String[]{"ObisMike"}, new String[]{"AshenShugar", "Buba237", "JStrange", "RealBrofessor", "chuckjirka"}),
	FINNISH("suomi", 		"fi", Status.INCOMPLETE, new String[]{"TenguTheKnight"}, null ),
	TURKISH("türkçe",       "tr", Status.INCOMPLETE, new String[]{"LokiofMillenium", "emrebnk"}, new String[]{"AGORAAA", "AcuriousPotato", "alpekin98", "denizakalin", "erdemozdemir98", "immortalsamuraicn", "melezorus34", "mitux"}),
	HUNGARIAN("magyar",     "hu", Status.UNREVIEWED, new String[]{"dorheim", "szalaik"}, new String[]{"Navetelen", "clarovani", "dhialub", "nanometer", "nardomaa", "savarall"}),
	JAPANESE("日本語",       "ja", Status.INCOMPLETE, null, new String[]{"Gosamaru", "amama", "librada", "mocklike"}),
	INDONESIAN("indonésien","in", Status.INCOMPLETE, new String[]{"rakapratama"}, new String[]{"ZangieF347", "esprogarap"}),
	UKRANIAN("українська",  "uk", Status.UNREVIEWED, new String[]{"Oster"}, new String[]{"Sadsaltan1", "TheGuyBill", "Volkov", "ZverWolf", "ingvarfed", "oliolioxinfree", "romanokurg", "vlisivka"}),
	CATALAN("català",       "ca", Status.INCOMPLETE, new String[]{"Illyatwo2"}, new String[]{"Elosy", "n1ngu"}),
	BASQUE("euskara",       "eu", Status.INCOMPLETE, new String[]{"Deathrevenge", "Osoitz"}, null),
	ESPERANTO("esperanto",  "eo", Status.REVIEWED,   new String[]{"Verdulo"}, new String[]{"Raizin"});

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
