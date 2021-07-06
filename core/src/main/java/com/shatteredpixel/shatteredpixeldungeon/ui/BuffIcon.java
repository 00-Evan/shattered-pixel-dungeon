package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class BuffIcon extends Image {

	private static TextureFilm smallFilm;
	private static final int SML_SIZE = 7;

	private static TextureFilm largeFilm;
	private static final int LRG_SIZE = 16;

	//TODO maybe roll fading behaviour into this too?
	public BuffIcon(Buff buff, boolean large){
		this(buff.icon(), large);
		buff.tintIcon(this);
	}

	public BuffIcon(int icon, boolean large){
		super( large ? Assets.Interfaces.BUFFS_LARGE : Assets.Interfaces.BUFFS_SMALL );

		if (large){
			if (largeFilm == null) largeFilm = new TextureFilm(texture, LRG_SIZE, LRG_SIZE);
			frame(largeFilm.get(icon));
		} else {
			if (smallFilm == null ) smallFilm = new TextureFilm(texture, SML_SIZE, SML_SIZE);
			frame(smallFilm.get(icon));
		}
	}

}
