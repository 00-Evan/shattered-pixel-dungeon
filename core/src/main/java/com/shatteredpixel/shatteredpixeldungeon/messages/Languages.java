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
	CHINESE("中文",          "zh", Status.REVIEWED, new String[]{"Jinkeloid(zdx00793)", "endlesssolitude"}, new String[]{"931451545", "Budding", "Chronie_Lynn_Iwa", "Fatir", "Fishbone", "Hcat", "HoofBumpBlurryface", "Lery", "Lyn_0401", "Ooooscar", "ShatteredFlameBlast", "SpaceAnchor", "hmdzl001", "leo", "tempest102", "户方狸奴"}),
	KOREAN("한국어",         "ko", Status.REVIEWED, new String[]{"Cocoa", "Flameblast12", "GameConqueror", "Korean2017"}, new String[]{"AFS", "N8fall", "WondarRabb1t", "chlrhwnstkd", "ddojin0115", "eeeei", "enjuxx", "hancyel", "linterpreteur", "lsiebnie", "sora0430"}),
	RUSSIAN("русский",      "ru", Status.REVIEWED, new String[]{"ConsideredHamster", "Inevielle", "apxwn", "yarikonline" }, new String[]{"AttHawk46", "BlueberryShortcake", "Dominowood371", "Enwviun", "HerrGotlieb", "HoloTheWise", "Ilbko", "JleHuBbluKoT", "KirStaLong", "MrXantar", "Nikets", "Originalej0name", "Raymundo", "Shamahan", "Thomasg63", "XAutumn", "Ya6lo4ko", "dasfan123", "ifritdiezel", "kirusyaga", "long_live_the_9", "perefrazz", "roman.yagodin", "un_logic", "vivatimperia", "Вoвa"}),
	SPANISH("español",      "es", Status.REVIEWED, new String[]{"KeyKai", "Kiroto", "Kohru", "airman12", "grayscales"}, new String[]{"2001sergiobr", "AdventurerKilly", "Alesxanderk", "Bryan092", "CorvosUtopy", "D0n.Kak0", "Dewstend", "Dyrran", "Enddox", "Fervoreking", "Illyatwo2", "JPCHZ", "LastCry", "Marquezo_577_284", "NAVI1237", "STKmonoqui", "Sh4rkill3r", "alfongad", "anauta", "benzarr410", "chepe567.jc", "ctrijueque", "damc0616", "desen90", "dhg121", "javifs", "jonismack1", "magmax", "rechebeltran", "tres.14159"}),
	GERMAN("deutsch",       "de", Status.REVIEWED, new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Zap0", "apxwn", "bernhardreiter", "davedude"}, new String[]{"2711chrissi", "Abracadabra", "Ceeee", "DarkPixel", "ErichME", "Faquarl", "LenzB", "MacMoff", "Micksha", "Niseko", "Ordoviz", "Sarius", "SirEddi", "Sorpl3x", "SurmanPP", "SwissQ", "ThunfischGott", "Timo_S", "Topicranger", "azrdev", "carrageen", "dome.scheidler", "galactictrans", "gekko303", "jeinzi", "johannes.schobel", "karoshi42", "koryphea", "luciocarreras", "mklr", "niemand", "oragothen", "spixi", "unbekannterTyp"}),
	POLISH("polski",        "pl", Status.UNREVIEWED, new String[]{"Deksippos", "MrKukurykpl", "kuadziw", "szymex73"}, new String[]{"Akmetari", "AntiTime", "Boguc", "Chasseur", "Ciechu", "Darden", "DarkKnightComes", "GRan0000", "I256I", "KarixDaii", "KrnąbrnyOlaf", "Lufix", "MJedi", "Odiihinia", "Ostsee0912", "Peperos", "ProPolishGamer", "RolsoN", "Scharnvirk", "VasteelXolotl", "Voyteq", "Wiiiiiii", "bogumilg", "bvader95", "chronon", "dusakus", "elchudy", "jajkoswinka", "michaub", "ozziezombie", "szczoteczka22", "taki1", "transportowiec96"}),
	FRENCH("français",      "fr", Status.UNREVIEWED, new String[]{"Emether", "TheKappaDuWeb", "Xalofar", "canc42", "kultissim", "minikrob"}, new String[]{"3raven", "Alsydis", "Axce", "Az_zahr", "Bastien72", "Basttee", "Dekadisk", "Draal", "Eragem", "Karnot", "Le_Valla", "Louson", "Martin.Bellet", "Neopolitan", "NoGi", "Nyrnx", "Pandaman516", "Petit_Chat", "RomTheMareep", "RunningColours", "Soeiz", "SpeagleZNT", "Tronche2Cake", "VRad", "Weende_Bellet", "Ygdrazil", "_nim_", "adamch", "antoine9298", "clexanis", "eloiseflo", "go11um", "hydrasho", "jazzzz", "levilbatard", "linterpreteur", "maeltur70", "marmous", "mcbaba29000", "mluzarreta", "panopano", "solthaar", "speagle", "typhr80", "vavavoum", "whereisfelix", "willi3725", "zM_"}),
	PORTUGUESE("português", "pt", Status.UNREVIEWED, new String[]{"NicholasPainek", "TDF2001", "matheus208"}, new String[]{"14NGiestas", "Aetheryll", "Andrew_px1", "Arthur_Mastriaga", "Bigode935", "Bionic64", "Chacal.Ex", "ChainedFreaK", "Derik", "DredgenVale", "ElefanteFome", "Helen0903", "JST", "MadHorus", "Maria_João", "MarkusCoisa", "Matie", "OtávioMoraes", "PingasOwner", "Piraldo", "Sr.BaconDelicioso", "Tete_Teli", "Tio_P_(Krampus)", "Zukkine", "ancientorange", "danypr23", "denis.gnl", "efverick", "gBiazon", "ismael.henriques12", "juniorsilve33", "mfcord", "nattlegal", "owenreilly", "rafazago", "renan408", "try31"}),
	ITALIAN("italiano",		"it", Status.REVIEWED, new String[]{"MottledElm", "NeoAugustus", "bizzolino", "funnydwarf", "inkubo87"}, new String[]{"4est", "Danelix", "DaniMare", "Danzl", "Eriliken", "Esse78", "Guiller124", "IoannesMaria", "Mat323", "Mister64", "Noostale", "andreafaffo", "andrearubbino00", "cantarini", "carinellialessandro31", "dmytro.tokayev", "mattiuw", "max1234ita", "nessunluogo", "righi.a", "umby000", "valerio.bozzolan"}),
	TURKISH("türkçe",       "tr", Status.INCOMPLETE, new String[]{"LokiofMillenium", "emrebnk", "gorkem_yılmaz"}, new String[]{"AGORAAA", "AchernarPrime", "AcuriousPotato", "MuratEfeYilmaz", "OzanAlkan", "T3kin5iZ", "TR_Muhittin", "ahmetbakicakir", "akkaya.mustafa", "alikeremozfidan", "alpekin98", "denizakalin", "eraysall402", "erdemozdemir98", "hasantahsin160", "immortalsamuraicn", "kayikyaki", "melezorus34", "mitux", "ryuga", "yasirckr85", "yukete"}),
	CZECH("čeština",        "cs", Status.UNREVIEWED, new String[]{"ObisMike", "novotnyvaclav"}, new String[]{"16cnovotny", "AshenShugar", "Autony", "Block_Vader", "Buba237", "Nerdiniel", "JStrange", "RealBrofessor", "Thorn_123", "chuckjirka", "emteckos2", "kristanka"}),
	INDONESIAN("indonésien","in", Status.INCOMPLETE, new String[]{"rakapratama"}, new String[]{"INDRA_SYAHPUTRA", "Izulhaaq", "Taka31", "ZakyM313", "ZangieF347", "atmorojo", "di9526985", "esprogarap", "kirimaja", "nicoalvito", "oolek", "wisnugafur"}),
	JAPANESE("日本語",       "ja", Status.INCOMPLETE, null, new String[]{"Gosamaru", "Otogiri", "Siraore_Rou", "amama", "daingewuvzeevisiddfddd", "grassedge", "kiyofumimanabe", "librada", "mocklike", "oz51199", "tomofumikitano"}),
	UKRANIAN("українська",  "uk", Status.INCOMPLETE, new String[]{"Oster"}, new String[]{"AlexFenixUA", "Dotsent", "Lyttym", "Sadsaltan1", "TarasUA", "TheGuyBill", "Tomfire", "Volkov", "ZverWolf", "_bor_", "alexfenixva", "ddmaster3463", "filalex77", "ingvarfed", "iu0v1", "lezzen", "oliolioxinfree", "so1der", "qweez", "romanokurg", "vlisivka", "yukete", "zhawty"}),
	DUTCH("nederlands",     "nl", Status.UNREVIEWED, new String[]{"AlbertBrand"}, new String[]{"AvanLieshout", "Blokheck011", "Frankwert", "Gehenna", "Mvharen", "Valco", "ZephyrZodiac", "link200023", "rmw", "th3f4llenh0rr0r"}),
	HUNGARIAN("magyar",     "hu", Status.UNREVIEWED, new String[]{"dorheim", "szalaik"}, new String[]{"Navetelen", "acszoltan111", "clarovani", "dhialub", "nanometer", "nardomaa", "savarall", "summoner001"}),
	FINNISH("suomi", 		"fi", Status.INCOMPLETE, new String[]{"TenguKnight"}, new String[]{"Allugaattori10", "Dakkus", "Jaskas123", "MailBoxGod", "Oftox", "Sautari", "Tikkari"} ),
	GREEK("ελληνικά",       "el", Status.REVIEWED,   new String[]{"Aeonius", "Saxy"}, new String[]{"DU_Clouds", "VasKyr", "YiorgosH", "fr3sh", "stefboi", "toumbo", "val.exe"}),
	//CATALAN("català",     "ca", Status.INCOMPLETE, new String[]{"Illyatwo2"}, new String[]{"Elosy", "n1ngu"})
	GALICIAN("galego",      "gl", Status.INCOMPLETE, new String[]{"xecarballido"}, null),
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
