/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
	ENGLISH("english",      "en", Status.O_COMPLETE, null, null),
	CHINESE("中文",          "zh", Status.__UNREVIEW, new String[]{"Chronie_Lynn_Iwa", "Jinkeloid(zdx00793)", "endlesssolitude"}, new String[]{"931451545", "Budding", "Fatir", "Fishbone", "Hcat", "HoofBumpBlurryface", "Horr_lski", "Lery", "Lyn_0401", "Lyx0527", "Ooooscar", "Oxide111", "RainSlide", "ShatteredFlameBlast", "SpaceAnchor", "SunsetGlowTheDOGE", "Teller", "hmdzl001", "leo", "tempest102", "户方狸奴"}),
	KOREAN("한국어",         "ko", Status.O_COMPLETE, new String[]{"Cocoa", "Flameblast12", "GameConqueror", "Korean2017"}, new String[]{"AFS", "N8fall", "WondarRabb1t", "benepaper", "chlrhwnstkd", "ddojin0115", "eeeei", "enjuxx", "hancyel", "linterpreteur", "lemonam", "lsiebnie", "sora0430"}),
	RUSSIAN("русский",      "ru", Status.X_UNFINISH, new String[]{"AprilRain(Vadzim Navumaû)", "ConsideredHamster", "Dominowood371", "Inevielle", "apxwn", "yarikonline"}, new String[]{"5r", "AngryPotato", "AttHawk46", "BlueberryShortcake", "CatGirlSasha", "Enwviun", "HerrGotlieb", "HoloTheWise", "Ilbko", "JleHuBbluKoT", "KirStaLong", "MrXantar", "Nikets", "OneDuo", "Originalej0name", "Raymundo", "Roycce", "Shamahan", "Thomasg63", "XAutumn", "Ya6lo4ko", "chelikchelik", "dasfan123", "ifritdiezel", "kirusyaga", "kptmx", "long_live_the_9", "pancreper1", "perefrazz", "roman.yagodin", "tibby", "un_logic", "vivatimperia", "wntrau", "Вoвa"}),
	SPANISH("español",      "es", Status.__UNREVIEW, new String[]{"KeyKai", "Kiroto", "Kohru", "airman12", "grayscales"}, new String[]{"2001sergiobr", "AdventurerKilly", "Alesxanderk", "Bryan092", "CorvosUtopy", "D0n.Kak0", "Dewstend", "Dyrran", "Enddox", "Fervoreking", "Illyatwo2", "Fuwn", "JPCHZ", "LastCry", "Marquezo_577_284", "NAVI1237", "No_se145", "STKmonoqui", "Sh4rkill3r", "Uri2523", "alfongad", "anauta", "benzarr410", "chepe567.jc", "ctrijueque", "damc0616", "desen90", "dhg121", "javifs", "jonismack1", "magmax", "rechebeltran", "tres.14159"}),
	PORTUGUESE("português", "pt", Status.__UNREVIEW, new String[]{"NicholasPainek", "TDF2001", "matheus208"}, new String[]{"14NGiestas", "Aetheryll", "Andrew_px1", "Arthur_Mastriaga", "Bigode935", "Bionic64", "Chacal.Ex", "ChainedFreaK", "DAVICCOSTA", "Derik", "DredgenVale", "ElefanteFome", "Helen0903", "JST", "Kotaroo05", "MadHorus", "Maria_João", "MarkusCoisa", "Matie", "OtávioMoraes", "PingasOwner", "Piraldo", "Sr.BaconDelicioso", "Tete_Teli", "Tio_P_(Krampus)", "Zukkine", "ancientorange", "danypr23", "denis.gnl", "efverick", "gBiazon", "ismael.henriques12", "juniorsilve33", "mfcord", "nattlegal", "owenreilly", "phobos445", "rafazago", "renan408", "try31"}),
	GERMAN("deutsch",       "de", Status.X_UNFINISH, new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Zap0", "apxwn", "bernhardreiter", "davedude"}, new String[]{"2711chrissi", "Abracadabra", "Anaklysmos", "Ceeee", "DarkPixel", "David.transifex", "EmilKevinManuel", "ErichME", "Faquarl", "LenzB", "MacMoff", "Micksha", "Niseko", "Ordoviz", "Sarius", "Shtynow", "SirEddi", "Sorpl3x", "SurmanPP", "SwissQ", "ThunfischGott", "Timo_S", "Topicranger", "azrdev", "carrageen", "dome.scheidler", "galactictrans", "gekko303", "jeinzi", "johannes.schobel", "karoshi42", "koryphea", "luciocarreras", "mklr", "niemand", "oragothen", "spixi", "tanjay", "unbekannterTyp", "wunst"}),
	FRENCH("français",      "fr", Status.O_COMPLETE, new String[]{"Emether", "TheKappaDuWeb", "Weende_Bellet", "Xalofar", "canc42", "kultissim", "minikrob", "Lucasgstar"}, new String[]{"3raven", "Alsydis", "Anonyme48", "Axce", "Az_zahr", "Bastien72", "Basttee", "Coco_EC", "Dekadisk", "Draal", "Eragem", "Karnot", "L.E.V.", "Lama", "Le_Valla", "Leandre", "Louson", "Martin.Bellet", "Neopolitan", "NoGi", "Nyrnx", "Opidox", "Pandaman516", "Petit_Chat", "RomTheMareep", "RunningColours", "STPayoube", "Soeiz", "SpeagleZNT", "Teddywestside", "Tronche2Cake", "VRad", "Ygdrazil", "_nim_", "adamch", "adeb", "antoine9298", "clexanis", "eloiseflo", "fricht", "go11um", "hydrasho", "jan.", "jazzzz", "levilbatard", "linterpreteur", "luffah", "maeltur70", "marmous", "mcbaba29000", "mluzarreta", "panopano", "solthaar", "speagle", "tkf_", "typhr80", "vavavoum", "whereisfelix", "willi3725", "zM_"}),
	JAPANESE("日本語",       "ja", Status.O_COMPLETE, new String[]{"daingewuvzeevisiddfddd", "oz51199"}, new String[]{"Gosamaru", "NickZhrbin", "Otogiri", "Siraore_Rou", "amama", "grassedge", "kiyofumimanabe", "librada", "mocklike", "tomofumikitano"}),
	POLISH("polski",        "pl", Status.__UNREVIEW, new String[]{"Daniel Witański", "Deksippos", "MrKukurykpl", "chronon", "kuadziw", "szymex73"}, new String[]{"Akmetari", "AntiTime", "Boguc", "Chasseur", "Ciechu", "Darden", "DarkKnightComes", "DogeseleQ", "GRan0000", "Hammil", "I256I", "KarixDaii", "KrnąbrnyOlaf", "Lufix", "MJedi", "MrCommander", "Odiihinia", "Ostsee0912", "Peperos", "RolsoN", "Scharnvirk", "Serpens13", "Tangens", "VasteelXolotl", "Voyteq", "Wiiiiiii", "bobas10", "bogumilg", "bvader95", "dusakus", "elchudy", "jajkoswinka", "michaub", "mikolka9144", "ozziezombie", "szczoteczka22", "taki1", "transportowiec96"}),
	VIETNAMESE("tiếng việt","vi", Status.__UNREVIEW, new String[]{"Chuseko", "The_Hood", "nguyenanhkhoapythus"}, new String[]{"BlueSheepAlgodoo", "Phuc2401", "SpaceMetropolis", "Teh_boi", "Threyja", "Toluu", "bruhwut", "buicongminh_t63", "deadlevel13", "duongfg250", "h4ndy_c4ndy", "hniV", "khangxyz3g", "ngolamaz3", "nkhhu", "vdgiapp", "vtvinh24"}),
	TURKISH("türkçe",       "tr", Status.X_UNFINISH, new String[]{"LokiofMillenium", "Mustafa.10", "T3kin5iZ", "emrebnk", "gorkem_yılmaz"}, new String[]{"AGORAAA", "AchernarPrime", "AcuriousPotato", "BurningDaylight", "Helgon", "Koga", "Mehmet_Emin_21", "MuratEfeYilmaz", "OzanAlkan", "TR_Muhittin", "Talha_0_0", "TheMBDsvs", "Yllcare", "YORGANSIZMTAV", "ahmetbakicakir", "akkaya.mustafa", "alikeremozfidan", "alpekin98", "denizakalin", "eraysall402", "erdemozdemir98", "hasantahsin160", "immortalsamuraicn", "kayikyaki", "kempilbey", "melezorus34", "mitux", "mustafadoslu", "ryuga", "yasirckr85", "yukete"}),
	INDONESIAN("indonésien","in", Status.__UNREVIEW, new String[]{"RF_4R4F1_03", "rakapratama"}, new String[]{"An_Ironstone", "Flasherx", "INDRA_SYAHPUTRA", "Izulhaaq", "Karanh", "M.Bintang.K", "PineFirebloom", "QiuQiuQi", "Taka31", "ZakyM313", "ZangieF347", "aachunemiku", "anagakenny24", "atmorojo", "di9526985", "esprogarap", "hatsunnimiku", "icebearwand", "kirimaja", "lupar21", "luthfidzaky_ldzy", "mkakhsan301", "nicoalvito", "noeldycreator", "oolek", "wisnugafur"}),
	ITALIAN("italiano",		"it", Status.X_UNFINISH, new String[]{"MottledElm", "NeoAugustus", "bizzolino", "funnydwarf", "inkubo87"}, new String[]{"4est", "Danelix", "DaniMare", "Danzl", "Eriliken", "Esse78", "Guiller124", "Hydr46605", "IoannesMaria", "LN_90", "Mat323", "Mister64", "Noostale", "PicchiSeba", "Tugamer89", "andrea049ita", "andreafaffo", "andrearubbino00", "cantarini", "carinellialessandro31", "dmytro.tokayev", "mamon68596", "mattiuw", "max1234ita", "maxifire32", "nessunluogo", "righi.a", "umby000", "unknown888", "valerio.bozzolan"}),
	UKRANIAN("українська",  "uk", Status.O_COMPLETE, new String[]{"Oster", "Snikewin", "zhushman00"}, new String[]{"AlexFenixUA", "Buster54", "Doodlinka", "Dotsent", "Lyttym", "MaxQuiet", "Mops", "Sadsaltan1", "TarasUA", "TheGuyBill", "Tomfire", "Volkov", "ZverWolf", "_bor_", "alexfenixva", "ddmaster3463", "filalex77", "holuydadko", "ingvarfed", "iu0v1", "jesternotricks", "lezzen", "myshokoleksander05", "oliolioxinfree", "qweez", "romanokurg", "so1der", "sterenkevicsasa", "vlisivka", "xojltoh", "yukete", "zhawty", "Мальвочка"}),
	CZECH("čeština",        "cs", Status.__UNREVIEW, new String[]{"ObisMike", "novotnyvaclav"}, new String[]{"16cnovotny", "AshenShugar", "Autony", "Block_Vader", "Buba237", "JStrange", "Nerdiniel", "Patrik123", "RealBrofessor", "Thorn_123", "chuckjirka", "emteckos2", "kristanka"}),
	HUNGARIAN("magyar",     "hu", Status.O_COMPLETE, new String[]{"dorheim", "summoner001", "szalaik"}, new String[]{"Csanevox", "Navetelen", "acszoltan111", "balazsszalab", "clarovani", "dhialub", "nanometer", "nardomaa", "savarall", "szemetvodor"}),
	DUTCH("nederlands",     "nl", Status.O_COMPLETE, new String[]{"AlbertBrand", "Mvharen"}, new String[]{"AvanLieshout", "Blokheck011", "Frankwert", "Gehenna", "Valco", "ZephyrZodiac", "link200023", "ojppe", "rmw", "th3f4llenh0rr0r"}),
	//FINNISH("suomi", 		"fi", Status.X_UNFIN, new String[]{"TenguKnight"}, new String[]{"Allugaattori10", "Dakkus", "Jaskas123", "MailBoxGod", "Oftox", "Sautari", "Tikkari"} ),
	GREEK("ελληνικά",       "el", Status.X_UNFINISH, new String[]{"Aeonius", "Saxy"}, new String[]{"DU_Clouds", "VasKyr", "YiorgosH", "fr3sh", "nikolaoskelirakis", "stefboi", "toumbo", "val.exe"}),
	BELARUSIAN("беларуская","be", Status.X_UNFINISH, new String[]{"AprilRain(Vadzim Navumaû)"}, new String[]{"4ebotar"}),
	//CATALAN("català",     "ca", Status.X_UNFIN, new String[]{"Illyatwo2"}, new String[]{"Elosy", "n1ngu"})
	//GALICIAN("galego",    "gl", Status.X_UNFIN, new String[]{"xecarballido"}, null),
	//BASQUE("euskara",     "eu", Status.X_UNFIN, new String[]{"Deathrevenge", "Osoitz"}, null),
	ESPERANTO("esperanto",  "eo", Status.__UNREVIEW, new String[]{"Verdulo"}, new String[]{"Raizin", "Rwelean", "kameluloj"});

	public enum Status{
		//below 80% translated languages are not added or removed
		X_UNFINISH, //unfinished, ~80-99% translated
		__UNREVIEW, //unreviewed, but 100% translated
		O_COMPLETE, //complete, 100% reviewed
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
