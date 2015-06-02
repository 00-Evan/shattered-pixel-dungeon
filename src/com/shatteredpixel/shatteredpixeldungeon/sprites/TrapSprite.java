package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class TrapSprite extends Image {

	private static TextureFilm frames;

	private int pos = -1;

	public TrapSprite() {
		super( Assets.TRAPS );

		if (frames == null) {
			frames = new TextureFilm( texture, 16, 16 );
		}

		origin.set( 8, 12 );
	}

	public TrapSprite( int image ) {
		this();
		reset( image );
	}

	public void reset( Trap trap ) {

		revive();

		reset( trap.image + (((Dungeon.depth-1) / 5) * 8) );
		alpha( 1f );

		pos = trap.pos;
		x = (pos % Level.WIDTH) * DungeonTilemap.SIZE;
		y = (pos / Level.WIDTH) * DungeonTilemap.SIZE;

	}

	public void reset( int image ) {
		frame( frames.get( image ) );
	}

}
