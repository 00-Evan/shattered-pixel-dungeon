/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredpixeldungeon.scenes;

import android.content.Intent;
import android.net.Uri;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class AboutScene extends PixelScene {

    private static final String TTL_SHPX = "Shattered Pixel Dungeon";

    private static final String TXT_SHPX =
            "Design, Code, & Graphics: Evan\n\n" +
            "Shattered Pixel is Evan's online home, check it out:";

    private static final String LNK_SHPX = "ShatteredPixel.com";


    private static final String TTL_WATA = "Original Pixel Dungeon";

	private static final String TXT_WATA =
		    "Code & Graphics: Watabou\n" +
		    "Music: Cube_Code\n\n" +
		    "Visit Watabou for more info:";
	
	private static final String LNK_WATA = "pixeldungeon.watabou.ru";
	
	@Override
	public void create() {
		super.create();

        Image shpx = Icons.SHPX.get();
        shpx.x = align( (Camera.main.width - shpx.width()) / 2 );
        shpx.y = align( 30 );
        add( shpx );

        new Flare( 7, 64 ).color( 0x225511, true ).show( shpx, 0 ).angularSpeed = +20;

        BitmapTextMultiline shpxtitle = createMultiline( TTL_SHPX, 8 );
        shpxtitle.maxWidth = Math.min( Camera.main.width, 120 );
        shpxtitle.measure();
        shpxtitle.hardlight( Window.SHPX_COLOR );
        add( shpxtitle );

        shpxtitle.x = align( (Camera.main.width - shpxtitle.width()) / 2 );
        shpxtitle.y = align( shpx.y + shpx.height + 5 );

        BitmapTextMultiline shpxtext = createMultiline( TXT_SHPX, 8 );
        shpxtext.maxWidth = Math.min( Camera.main.width, 120 );
        shpxtext.measure();
        add( shpxtext );

        shpxtext.x = align( (Camera.main.width - shpxtext.width()) / 2 );
        shpxtext.y = align( shpxtitle.y + shpxtitle.height() + 12 );

        BitmapTextMultiline shpxlink = createMultiline( LNK_SHPX, 8 );
        shpxlink.maxWidth = Math.min( Camera.main.width, 120 );
        shpxlink.measure();
        shpxlink.hardlight( Window.SHPX_COLOR );
        add( shpxlink );

        shpxlink.x = shpxtext.x;
        shpxlink.y = shpxtext.y + shpxtext.height();

        TouchArea shpxhotArea = new TouchArea( shpxlink ) {
            @Override
            protected void onClick( Touch touch ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://shatteredpixel.tumblr.com") );
                Game.instance.startActivity( intent );
            }
        };
        add( shpxhotArea );

        Image wata = Icons.WATA.get();
        wata.x = align( (Camera.main.width - wata.width()) / 2 );
        wata.y = shpxlink.y + wata.height + 25;
        add( wata );

        new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +20;

        BitmapTextMultiline title = createMultiline( TTL_WATA, 8 );
        title.maxWidth = Math.min( Camera.main.width, 120 );
        title.measure();
        title.hardlight( Window.TITLE_COLOR );
        add( title );

        title.x = align( (Camera.main.width - title.width()) / 2 );
        title.y = align( wata.y + wata.height + 5 );

		BitmapTextMultiline text = createMultiline( TXT_WATA, 8 );
		text.maxWidth = Math.min( Camera.main.width, 120 );
		text.measure();
		add( text );
		
		text.x = align( (Camera.main.width - text.width()) / 2 );
		text.y = align( title.y + title.height() + 12 );
		
		BitmapTextMultiline link = createMultiline( LNK_WATA, 8 );
		link.maxWidth = Math.min( Camera.main.width, 120 );
		link.measure();
		link.hardlight( Window.TITLE_COLOR );
		add( link );
		
		link.x = text.x;
		link.y = text.y + text.height();
		
		TouchArea hotArea = new TouchArea( link ) {
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + LNK_WATA ) );
				Game.instance.startActivity( intent );
			}
		};
		add( hotArea );

		
		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}
}
