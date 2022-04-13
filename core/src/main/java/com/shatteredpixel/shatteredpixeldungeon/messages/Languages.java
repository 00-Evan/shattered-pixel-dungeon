/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
	CHINESE("中文",          "zh", Status.UNREVIEWED, new String[]{"Jinkeloid(zdx00793)", "endlesssolitude"}, new String[]{"931451545", "Chronie_Lynn_Iwa", "Fatir", "Fishbone", "HoofBumpBlurryface", "Lery", "Lyn_0401", "Ooooscar", "ShatteredFlameBlast", "SpaceAnchor", "hmdzl001", "leo", "tempest102"}),
	KOREAN("한국어",         "ko", Status.REVIEWED,   new String[]{"Cocoa", "Flameblast12", "GameConqueror", "Korean2017"}, new String[]{"N8fall", "WondarRabb1t", "chlrhwnstkd", "chlrhwnstkd", "ddojin0115", "eeeei", "enjuxx", "hancyel", "linterpreteur", "lsiebnie"}),
	RUSSIAN("русский",      "ru", Status.REVIEWED,   new String[]{"ConsideredHamster", "Inevielle", "apxwn", "yarikonline" }, new String[]{"AttHawk46", "BlueberryShortcake", "HerrGotlieb", "HoloTheWise", "Ilbko", "JleHuBbluKoT", "KirStaLong", "MrXantar", "Nikets", "Originalej0name", "Raymundo", "Shamahan", "Thomasg63", "Ya6lo4ko", "kirusyaga", "perefrazz", "roman.yagodin", "un_logic", "vivatimperia", "Вoвa"}),
	SPANISH("español",      "es", Status.REVIEWED,   new String[]{"KeyKai", "Kiroto", "Kohru", "airman12", "grayscales"}, new String[]{"AdventurerKilly", "Alesxanderk", "CorvosUtopy", "D0n.Kak0", "Dewstend", "Dyrran", "Enddox", "Fervoreking", "Illyatwo2", "JPCHZ", "LastCry", "Marquezo_577_284", "NAVI1237", "STKmonoqui", "Sh4rkill3r", "alfongad", "benzarr410", "chepe567.jc", "ctrijueque", "damc0616", "desen90", "dhg121", "javifs", "jonismack1", "magmax", "rechebeltran", "tres.14159"}),
	GERMAN("deutsch",       "de", Status.REVIEWED,   new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Zap0", "apxwn", "bernhardreiter", "davedude"}, new String[]{"2711chrissi", "Abracadabra", "Ceeee", "DarkPixel", "ErichME", "Faquarl", "LenzB", "MacMoff", "Micksha", "Niseko", "Ordoviz", "Sarius", "SirEddi", "Sorpl3x", "SurmanPP", "SwissQ", "ThunfischGott", "Topicranger", "azrdev", "carrageen", "dome.scheidler", "galactictrans", "gekko303", "jeinzi", "johannes.schobel", "karoshi42", "koryphea", "luciocarreras", "mklr", "niemand", "oragothen", "spixi", "unbekannterTyp"}),
	FRENCH("français",      "fr", Status.UNREVIEWED, new String[]{"Emether", "TheKappaDuWeb", "Xalofar", "canc42", "kultissim", "minikrob"}, new String[]{"Alsydis", "Axce", "Az_zahr", "Bastien72", "Basttee", "Dekadisk", "Draal", "Karnot", "Martin.Bellet", "Neopolitan", "NoGi", "Nyrnx", "Pandaman516", "Petit_Chat", "RomTheMareep", "RunningColours", "Soeiz", "SpeagleZNT", "Tronche2Cake", "VRad", "Weende_Bellet", "Ygdrazil", "_nim_", "adamch", "antoine9298", "clexanis", "go11um", "hydrasho", "jazzzz", "levilbatard", "linterpreteur", "maeltur70", "marmous", "mcbaba29000", "mluzarreta", "panopano", "solthaar", "speagle", "typhr80", "vavavoum", "whereisfelix", "zM_"}),
	POLISH("polski",        "pl", Status.UNREVIEWED, new String[]{"Deksippos", "MrKukurykpl", "kuadziw", "szymex73"}, new String[]{"Akmetari", "AntiTime", "Boguc", "Chasseur", "Ciechu", "Darden", "DarkKnightComes", "GRan0000", "I256I", "KarixDaii", "KrnąbrnyOlaf", "MJedi", "Odiihinia", "Peperos", "ProPolishGamer", "RolsoN", "Scharnvirk", "VasteelXolotl", "Voyteq", "Wiiiiiii", "bogumilg", "bvader95", "dusakus", "elchudy", "jajkoswinka", "michaub", "ozziezombie", "szczoteczka22", "taki1", "transportowiec96"}),
	PORTUGUESE("português", "pt", Status.UNREVIEWED, new String[]{"NicholasPainek", "TDF2001", "matheus208"}, new String[]{"14NGiestas", "Aetheryll", "Andrew_px1 ", "Arthur_Mastriaga", "Bigode935", "Bionic64", "Chacal.Ex", "ChainedFreaK", "DredgenVale", "ElefanteFome", "Helen0903", "JST", "MadHorus", "Maria_João", "MarkusCoisa", "Matie", "OtávioMoraes", "PingasOwner", "Piraldo", "Sr.BaconDelicioso", "Tete_Teli", "Tio_P_(Krampus)", "Zukkine", "ancientorange", "danypr23", "denis.gnl", "efverick", "gBiazon", "ismael.henriques12", "juniorsilve33", "mfcord", "nattlegal", "owenreilly", "rafazago", "renan408", "try31"}),
	ITALIAN("italiano",		"it", Status.UNREVIEWED, new String[]{"NeoAugustus", "bizzolino", "funnydwarf"}, new String[]{"4est", "Danelix", "DaniMare", "Danzl", "Eriliken", "Esse78", "Guiller124", "IoannesMaria", "Mat323", "Mister64", "MottledElm", "Noostale", "andreafaffo", "andrearubbino00", "cantarini", "carinellialessandro31", "dmytro.tokayev", "inkubo87", "mattiuw", "max1234ita", "nessunluogo", "righi.a", "umby000", "valerio.bozzolan"}),
	TURKISH("türkçe",       "tr", Status.UNREVIEWED, new String[]{"LokiofMillenium", "emrebnk", "gorkem_yılmaz"}, new String[]{"AGORAAA", "AcuriousPotato", "OzanAlkan", "alikeremozfidan", "alpekin98", "denizakalin", "eraysall402", "erdemozdemir98", "hasantahsin160", "immortalsamuraicn", "kayikyaki", "melezorus34", "mitux", "ryuga", "yasirckr85"}),
	CZECH("čeština",        "cs", Status.INCOMPLETE, new String[]{"ObisMike", "novotnyvaclav"}, new String[]{"16cnovotny", "AshenShugar", "Autony", "Buba237", "JStrange", "RealBrofessor", "Thorn_123", "kristanka", "chuckjirka"}),
	INDONESIAN("indonésien","in", Status.INCOMPLETE, new String[]{"rakapratama"}, new String[]{"Izulhaaq", "Taka31", "ZakyM313", "ZangieF347", "di9526985", "esprogarap", "kirimaja", "nicoalvito", "wisnugafur"}),
	JAPANESE("日本語",       "ja", Status.INCOMPLETE, null, new String[]{"Gosamaru", "Otogiri", "Siraore_Rou", "amama", "daingewuvzeevisiddfddd", "kiyofumimanabe", "librada", "mocklike", "tomofumikitano"}),
	HUNGARIAN("magyar",     "hu", Status.UNREVIEWED, new String[]{"dorheim", "szalaik"}, new String[]{"Navetelen", "acszoltan111", "clarovani", "dhialub", "nanometer", "nardomaa", "savarall", "summoner001"}),
	UKRANIAN("українська",  "uk", Status.INCOMPLETE, new String[]{"Oster"}, new String[]{"Lyttym", "Sadsaltan1", "TarasUA", "TheGuyBill", "Tomfire", "Volkov", "ZverWolf", "_bor_", "ddmaster3463", "ingvarfed", "iu0v1", "lezzen", "oliolioxinfree", "qweez", "romanokurg", "vlisivka"}),
	FINNISH("suomi", 		"fi", Status.INCOMPLETE, new String[]{"TenguKnight"}, new String[]{"Dakkus", "Oftox", "Sautari"} ),
	GREEK("ελληνικά",       "el", Status.REVIEWED,   new String[]{"Aeonius", "Saxy"}, new String[]{"DU_Clouds", "VasKyr", "YiorgosH", "fr3sh", "stefboi", "val.exe"}),
	//CATALAN("català",     "ca", Status.INCOMPLETE, new String[]{"Illyatwo2"}, new String[]{"Elosy", "n1ngu"})
	GALICIAN("galego",      "gl", Status.UNREVIEWED, new String[]{"xecarballido"}, null),
	BASQUE("euskara",       "eu", Status.INCOMPLETE, new String[]{"Deathrevenge", "Osoitz"}, null),
	ESPERANTO("esperanto",  "eo", Status.UNREVIEWED, new String[]{"Verdulo"}, new String[]{"Raizin"});

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
