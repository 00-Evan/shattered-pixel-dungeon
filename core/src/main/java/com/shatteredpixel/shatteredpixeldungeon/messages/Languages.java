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

package com.shatteredpixel.shatteredpixeldungeon.messages;

import java.util.Locale;

public enum Languages {
	ENGLISH("english",      "",   Status.REVIEWED,   null, null),
	RUSSIAN("русский",      "ru", Status.INCOMPLETE, new String[]{"ConsideredHamster", "Inevielle", "apxwn", "yarikonline" }, new String[]{"AttHawk46", "BlueberryShortcake", "HerrGotlieb", "HoloTheWise", "Ilbko", "JleHuBbluKoT", "MrXantar", "Nikets", "Originalej0name", "Raymundo", "Shamahan", "Thomasg63", "Ya6lo4ko", "kirusyaga", "perefrazz", "roman.yagodin", "un_logic", "Вoвa"}),
	KOREAN("한국어",          "ko", Status.REVIEWED,   new String[]{"Cocoa", "Flameblast12", "GameConqueror", "Korean2017"}, new String[]{"N8fall", "WondarRabb1t", "ddojin0115", "eeeei", "enjuxx", "hancyel", "linterpreteur", "lsiebnie"}),
	CHINESE("中文",         "zh", Status.UNREVIEWED,  new String[]{"Jinkeloid(zdx00793)", "endlesssolitude"}, new String[]{"931451545", "Fatir", "Fishbone", "HoofBumpBlurryface", "Lery", "Lyn_0401", "ShatteredFlameBlast", "SpaceAnchor", "hmdzl001", "leo", "tempest102"}),
	POLISH("polski",        "pl", Status.INCOMPLETE, new String[]{"Deksippos", "MrKukurykpl", "kuadziw", "szymex73"}, new String[]{"Chasseur", "Darden", "DarkKnightComes", "KarixDaii", "KrnąbrnyOlaf", "MJedi", "Odiihinia", "Peperos", "RolsoN", "Scharnvirk", "VasteelXolotl", "Voyteq", "bogumilg", "bvader95", "dusakus", "elchudy", "michaub", "ozziezombie", "szczoteczka22", "transportowiec96"}),
	SPANISH("español",      "es", Status.UNREVIEWED, new String[]{"Kiroto", "Kohru", "airman12", "grayscales"}, new String[]{"AdventurerKilly", "Alesxanderk", "CorvosUtopy", "D0n.Kak0", "Dewstend", "Dyrran", "Enddox", "Fervoreking", "Illyatwo2", "JPCHZ", "LastCry", "Marquezo_577_284", "NAVI1237", "STKmonoqui", "Sh4rkill3r", "alfongad", "benzarr410", "chepe567.jc", "ctrijueque", "damc0616", "desen90", "dhg121", "javifs", "jonismack1", "magmax", "tres.14159"}),
	GERMAN("deutsch",       "de", Status.INCOMPLETE, new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Zap0", "apxwn", "bernhardreiter", "davedude"}, new String[]{"Abracadabra", "Ceeee", "DarkPixel", "ErichME", "Faquarl", "LenzB", "MacMoff", "Ordoviz", "Sarius", "SirEddi", "Sorpl3x", "SurmanPP", "ThunfischGott", "Topicranger", "azrdev", "carrageen", "gekko303", "jeinzi", "johannes.schobel", "karoshi42", "koryphea", "luciocarreras", "niemand", "oragothen", "spixi"}),
	FRENCH("français",      "fr", Status.UNREVIEWED, new String[]{"Emether", "TheKappaDuWeb", "Xalofar", "canc42", "kultissim", "minikrob"}, new String[]{"Alsydis", "Axce", "Az_zahr", "Bastien72", "Basttee", "Dekadisk", "Draal", "Martin.Bellet", "Neopolitan", "Nyrnx", "Petit_Chat", "RomTheMareep", "RunningColours", "SpeagleZNT", "Tronche2Cake", "VRad", "Ygdrazil", "_nim_", "antoine9298", "clexanis", "go11um", "hydrasho", "levilbatard", "linterpreteur", "maeltur70", "marmous", "mluzarreta", "solthaar", "speagle", "typhr80", "vavavoum", "zM_"}),
	PORTUGUESE("português", "pt", Status.INCOMPLETE, new String[]{"NicholasPainek", "TDF2001", "matheus208"}, new String[]{"Bigode935", "Chacal.Ex", "ChainedFreaK", "DredgenVale", "Helen0903", "JST", "MadHorus", "MarkusCoisa", "Matie", "Tio_P_(Krampus)", "ancientorange", "danypr23", "denis.gnl", "ismael.henriques12", "mfcord", "owenreilly", "rafazago", "try31"}),
	ITALIAN("italiano",		"it", Status.INCOMPLETE, new String[]{"bizzolino", "funnydwarf"}, new String[]{"4est", "Danelix", "DaniMare", "Danzl", "Guiller124", "Noostale", "andrearubbino00", "cantarini", "carinellialessandro31", "nessunluogo", "righi.a", "umby000"}),
	CZECH("čeština",        "cs", Status.INCOMPLETE, new String[]{"ObisMike"}, new String[]{"AshenShugar", "Autony", "Buba237", "JStrange", "RealBrofessor", "chuckjirka"}),
	FINNISH("suomi", 		"fi", Status.REVIEWED,   new String[]{"TenguKnight"}, new String[]{"Dakkus", "Sautari"} ),
	TURKISH("türkçe",       "tr", Status.INCOMPLETE, new String[]{"LokiofMillenium", "emrebnk"}, new String[]{"AGORAAA", "AcuriousPotato", "OzanAlkan", "alikeremozfidan", "alpekin98", "denizakalin", "erdemozdemir98", "gorkem_yılmaz", "hasantahsin160", "immortalsamuraicn", "kayikyaki", "melezorus34", "mitux"}),
	HUNGARIAN("magyar",     "hu", Status.INCOMPLETE, new String[]{"dorheim", "szalaik"}, new String[]{"Navetelen", "acszoltan111", "clarovani", "dhialub", "nanometer", "nardomaa", "savarall"}),
	JAPANESE("日本語",      "ja",  Status.INCOMPLETE, null, new String[]{"Gosamaru", "amama", "librada", "mocklike", "tomofumikitano"}),
	INDONESIAN("indonésien","in", Status.INCOMPLETE, new String[]{"rakapratama"}, new String[]{"Izulhaaq", "Taka31", "ZakyM313", "ZangieF347", "esprogarap"}),
	UKRANIAN("українська",  "uk", Status.INCOMPLETE, new String[]{"Oster"}, new String[]{"Sadsaltan1", "TheGuyBill", "Tomfire", "Volkov", "ZverWolf", "ingvarfed", "oliolioxinfree", "romanokurg", "vlisivka"}),
	CATALAN("català",       "ca", Status.INCOMPLETE, new String[]{"Illyatwo2"}, new String[]{"Elosy", "n1ngu"}),
	BASQUE("euskara",       "eu", Status.INCOMPLETE, new String[]{"Deathrevenge", "Osoitz"}, null),
	ESPERANTO("esperanto",  "eo", Status.INCOMPLETE, new String[]{"Verdulo"}, new String[]{"Raizin"});

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
