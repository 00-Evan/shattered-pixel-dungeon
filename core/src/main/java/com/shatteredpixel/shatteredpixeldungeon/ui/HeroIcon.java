package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

//icons for hero subclasses and abilities atm, maybe add classes?
public class HeroIcon extends Image {

	private static TextureFilm film;
	private static final int SIZE = 16;

	//transparent icon
	public static final int NONE    = 31;

	//subclasses
	public static final int BERSERKER   = 0;
	public static final int GLADIATOR   = 1;
	public static final int BATTLEMAGE  = 2;
	public static final int WARLOCK     = 3;
	public static final int ASSASSIN    = 4;
	public static final int FREERUNNER  = 5;
	public static final int SNIPER      = 6;
	public static final int WARDEN      = 7;

	//abilities
	public static final int HEROIC_LEAP     = 8;
	public static final int SHOCKWAVE       = 9;
	public static final int ENDURE          = 10;
	public static final int ELEMENTAL_BLAST = 11;
	public static final int WILD_MAGIC      = 12;
	public static final int WARP_BEACON     = 13;
	public static final int SMOKE_BOMB      = 14;
	public static final int DEATH_MARK      = 15;
	public static final int SHADOW_CLONE    = 16;
	public static final int SPECTRAL_BLADES = 17;
	public static final int NATURES_POWER   = 18;
	public static final int SPIRIT_HAWK     = 19;
	public static final int RATMOGRIFY      = 20;

	public HeroIcon(HeroSubClass subCls){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(subCls.icon()));
	}

	public HeroIcon(ArmorAbility abil){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(abil.icon()));
	}

}
