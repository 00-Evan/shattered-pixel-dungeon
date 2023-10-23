/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
	ENGLISH("english",      "",   Status._COMPLETE_, null, null),
	CHINESE("中文",          "zh", Status.UNREVIEWED, new String[]{"Chronie_Lynn_Iwa", "Jinkeloid(zdx00793)", "endlesssolitude"}, new String[]{"931451545", "Budding", "Fatir", "Fishbone", "Hcat", "HoofBumpBlurryface", "Horr_lski", "Lery", "Lyn_0401", "Lyx0527", "Ooooscar", "RainSlide", "ShatteredFlameBlast", "SpaceAnchor", "Teller", "hmdzl001", "leo", "tempest102", "户方狸奴"}),
	KOREAN("한국어",         "ko", Status.UNREVIEWED, new String[]{"Cocoa", "Flameblast12", "GameConqueror", "Korean2017"}, new String[]{"AFS", "N8fall", "WondarRabb1t", "chlrhwnstkd", "ddojin0115", "eeeei", "enjuxx", "hancyel", "linterpreteur", "lemonam", "lsiebnie", "sora0430"}),
	RUSSIAN("русский",      "ru", Status._COMPLETE_, new String[]{"AprilRain(Vadzim Navumaû)", "ConsideredHamster", "Dominowood371", "Inevielle", "apxwn", "yarikonline"}, new String[]{"AttHawk46", "BlueberryShortcake", "CatGirlSasha", "Enwviun", "HerrGotlieb", "HoloTheWise", "Ilbko", "JleHuBbluKoT", "KirStaLong", "MrXantar", "Nikets", "Originalej0name", "Raymundo", "Shamahan", "Thomasg63", "XAutumn", "Ya6lo4ko", "dasfan123", "ifritdiezel", "kirusyaga", "long_live_the_9", "pancreper1", "perefrazz", "roman.yagodin", "tibby", "un_logic", "vivatimperia", "Вoвa"}),
	SPANISH("español",      "es", Status._COMPLETE_, new String[]{"KeyKai", "Kiroto", "Kohru", "airman12", "grayscales"}, new String[]{"2001sergiobr", "AdventurerKilly", "Alesxanderk", "Bryan092", "CorvosUtopy", "D0n.Kak0", "Dewstend", "Dyrran", "Enddox", "Fervoreking", "Illyatwo2", "Fuwn", "JPCHZ", "LastCry", "Marquezo_577_284", "NAVI1237", "STKmonoqui", "Sh4rkill3r", "alfongad", "anauta", "benzarr410", "chepe567.jc", "ctrijueque", "damc0616", "desen90", "dhg121", "javifs", "jonismack1", "magmax", "rechebeltran", "tres.14159"}),
	PORTUGUESE("português", "pt", Status.UNREVIEWED, new String[]{"NicholasPainek", "TDF2001", "matheus208"}, new String[]{"14NGiestas", "Aetheryll", "Andrew_px1", "Arthur_Mastriaga", "Bigode935", "Bionic64", "Chacal.Ex", "ChainedFreaK", "DAVICCOSTA", "Derik", "DredgenVale", "ElefanteFome", "Helen0903", "JST", "Kotaroo05", "MadHorus", "Maria_João", "MarkusCoisa", "Matie", "OtávioMoraes", "PingasOwner", "Piraldo", "Sr.BaconDelicioso", "Tete_Teli", "Tio_P_(Krampus)", "Zukkine", "ancientorange", "danypr23", "denis.gnl", "efverick", "gBiazon", "ismael.henriques12", "juniorsilve33", "mfcord", "nattlegal", "owenreilly", "rafazago", "renan408", "try31"}),
	GERMAN("deutsch",       "de", Status._COMPLETE_, new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Zap0", "apxwn", "bernhardreiter", "davedude"}, new String[]{"2711chrissi", "Abracadabra", "Ceeee", "DarkPixel", "EmilKevinManuel", "ErichME", "Faquarl", "LenzB", "MacMoff", "Micksha", "Niseko", "Ordoviz", "Sarius", "Shtynow", "SirEddi", "Sorpl3x", "SurmanPP", "SwissQ", "ThunfischGott", "Timo_S", "Topicranger", "azrdev", "carrageen", "dome.scheidler", "galactictrans", "gekko303", "jeinzi", "johannes.schobel", "karoshi42", "koryphea", "luciocarreras", "mklr", "niemand", "oragothen", "spixi", "unbekannterTyp", "wunst"}),
	FRENCH("français",      "fr", Status.UNREVIEWED, new String[]{"Emether", "TheKappaDuWeb", "Weende_Bellet", "Xalofar", "canc42", "kultissim", "minikrob"}, new String[]{"3raven", "Alsydis", "Axce", "Az_zahr", "Bastien72", "Basttee", "Coco_EC", "Dekadisk", "Draal", "Eragem", "Karnot", "Lama", "Le_Valla", "Louson", "Lucasgstar", "Martin.Bellet", "Neopolitan", "NoGi", "Nyrnx", "Opidox", "Pandaman516", "Petit_Chat", "RomTheMareep", "RunningColours", "Soeiz", "SpeagleZNT", "Teddywestside", "Tronche2Cake", "VRad", "Ygdrazil", "_nim_", "adamch", "adeb", "antoine9298", "clexanis", "eloiseflo", "go11um", "hydrasho", "jan.", "jazzzz", "levilbatard", "linterpreteur", "luffah", "maeltur70", "marmous", "mcbaba29000", "mluzarreta", "panopano", "solthaar", "speagle", "typhr80", "vavavoum", "whereisfelix", "willi3725", "zM_"}),
	POLISH("polski",        "pl", Status.UNREVIEWED, new String[]{"Deksippos", "MrKukurykpl", "ProPolishGamer", "chronon", "kuadziw", "szymex73"}, new String[]{"Akmetari", "AntiTime", "Boguc", "Chasseur", "Ciechu", "Darden", "DarkKnightComes", "GRan0000", "Hammil", "I256I", "KarixDaii", "KrnąbrnyOlaf", "Lufix", "MJedi", "MrCommander", "Odiihinia", "Ostsee0912", "Peperos", "RolsoN", "Scharnvirk", "Tangens", "VasteelXolotl", "Voyteq", "Wiiiiiii", "bogumilg", "bvader95", "dusakus", "elchudy", "jajkoswinka", "michaub", "ozziezombie", "szczoteczka22", "taki1", "transportowiec96"}),
	ITALIAN("italiano",		"it", Status._COMPLETE_, new String[]{"MottledElm", "NeoAugustus", "bizzolino", "funnydwarf", "inkubo87"}, new String[]{"4est", "Danelix", "DaniMare", "Danzl", "Eriliken", "Esse78", "Guiller124", "IoannesMaria", "LN_90", "Mat323", "Mister64", "Noostale", "PicchiSeba", "Tugamer89", "andreafaffo", "andrearubbino00", "cantarini", "carinellialessandro31", "dmytro.tokayev", "mattiuw", "max1234ita", "nessunluogo", "righi.a", "umby000", "valerio.bozzolan"}),
	TURKISH("türkçe",       "tr", Status.UNREVIEWED, new String[]{"LokiofMillenium", "T3kin5iZ", "emrebnk", "gorkem_yılmaz"}, new String[]{"AGORAAA", "AchernarPrime", "AcuriousPotato", "BurningDaylight", "MuratEfeYilmaz", "Mustafa.10", "OzanAlkan", "TR_Muhittin", "Talha_0_0", "ahmetbakicakir", "akkaya.mustafa", "alikeremozfidan", "alpekin98", "denizakalin", "eraysall402", "erdemozdemir98", "hasantahsin160", "immortalsamuraicn", "kayikyaki", "melezorus34", "mitux", "mustafadoslu", "ryuga", "yasirckr85", "yukete"}),
	JAPANESE("日本語",       "ja", Status._COMPLETE_, new String[]{"daingewuvzeevisiddfddd", "oz51199"}, new String[]{"Gosamaru", "NickZhrbin", "Otogiri", "Siraore_Rou", "amama", "grassedge", "kiyofumimanabe", "librada", "mocklike", "tomofumikitano"}),
	UKRANIAN("українська",  "uk", Status.UNFINISHED, new String[]{"Oster", "Snikewin"}, new String[]{"AlexFenixUA", "Dotsent", "Lyttym", "Mops", "Sadsaltan1", "TarasUA", "TheGuyBill", "Tomfire", "Volkov", "ZverWolf", "_bor_", "alexfenixva", "ddmaster3463", "filalex77", "holuydadko", "ingvarfed", "iu0v1", "lezzen", "myshokoleksander05", "oliolioxinfree", "qweez", "romanokurg", "so1der", "sterenkevicsasa", "vlisivka", "xojltoh", "yukete", "zhawty",  "Мальвочка"}),
	CZECH("čeština",        "cs", Status.UNREVIEWED, new String[]{"ObisMike", "novotnyvaclav"}, new String[]{"16cnovotny", "AshenShugar", "Autony", "Block_Vader", "Buba237", "Nerdiniel", "JStrange", "RealBrofessor", "Thorn_123", "chuckjirka", "emteckos2", "kristanka"}),
	INDONESIAN("indonésien","in", Status.UNFINISHED, new String[]{"rakapratama"}, new String[]{"An_Ironstone", "INDRA_SYAHPUTRA", "Izulhaaq", "Karanh", "M.Bintang.K", "PineFirebloom", "QiuQiuQi", "Taka31", "ZakyM313", "ZangieF347", "aachunemiku", "atmorojo", "di9526985", "esprogarap", "hatsunnimiku", "icebearwand", "kirimaja", "lupar21", "nicoalvito", "noeldycreator", "oolek", "wisnugafur"}),
	DUTCH("nederlands",     "nl", Status.UNFINISHED, new String[]{"AlbertBrand"}, new String[]{"AvanLieshout", "Blokheck011", "Frankwert", "Gehenna", "Mvharen", "Valco", "ZephyrZodiac", "link200023", "rmw", "th3f4llenh0rr0r"}),
	VIETNAMESE("tiếng việt","vi", Status.UNREVIEWED, new String[]{"Chuseko", "The_Hood", "nguyenanhkhoapythus"}, new String[]{"BlueSheepAlgodoo", "Phuc2401", "Teh_boi", "Threyja", "Toluu", "bruhwut", "buicongminh_t63", "deadlevel13", "h4ndy_c4ndy", "hniV", "khangxyz3g", "ngolamaz3", "nkhhu", "vdgiapp", "vtvinh24"}),
	HUNGARIAN("magyar",     "hu", Status.UNREVIEWED, new String[]{"dorheim", "szalaik"}, new String[]{"Csanevox", "Navetelen", "acszoltan111", "clarovani", "dhialub", "nanometer", "nardomaa", "savarall", "summoner001", "szemetvodor"}),
	//FINNISH("suomi", 		"fi", Status.UNFINISHED, new String[]{"TenguKnight"}, new String[]{"Allugaattori10", "Dakkus", "Jaskas123", "MailBoxGod", "Oftox", "Sautari", "Tikkari"} ),
	GREEK("ελληνικά",       "el", Status._COMPLETE_, new String[]{"Aeonius", "Saxy"}, new String[]{"DU_Clouds", "VasKyr", "YiorgosH", "fr3sh", "stefboi", "toumbo", "val.exe"}),
	BELARUSIAN("беларуская","be", Status.UNREVIEWED, new String[]{"AprilRain(Vadzim Navumaû)"}, new String[]{"4ebotar"});
	//CATALAN("català",     "ca", Status.UNFINISHED, new String[]{"Illyatwo2"}, new String[]{"Elosy", "n1ngu"})
	//GALICIAN("galego",    "gl", Status.UNFINISHED, new String[]{"xecarballido"}, null),
	//BASQUE("euskara",     "eu", Status.UNFINISHED, new String[]{"Deathrevenge", "Osoitz"}, null),
	//ESPERANTO("esperanto","eo", Status.UNFINISHED, new String[]{"Verdulo"}, new String[]{"Raizin"});

	public enum Status{
		//below 80% translated languages are not added or removed
		UNFINISHED, //80-99% translated
		UNREVIEWED, //100% translated
		_COMPLETE_  //100% reviewed
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
