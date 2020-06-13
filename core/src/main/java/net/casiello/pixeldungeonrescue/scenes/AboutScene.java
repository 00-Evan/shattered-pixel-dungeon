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

package net.casiello.pixeldungeonrescue.scenes;

import net.casiello.pixeldungeonrescue.ShatteredPixelDungeon;
import net.casiello.pixeldungeonrescue.effects.Flare;
import net.casiello.pixeldungeonrescue.ui.Archs;
import net.casiello.pixeldungeonrescue.ui.ExitButton;
import net.casiello.pixeldungeonrescue.ui.Icons;
import net.casiello.pixeldungeonrescue.ui.RenderedTextBlock;
import net.casiello.pixeldungeonrescue.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.DeviceCompat;

public class AboutScene extends PixelScene {

	public static final int PDR_COLOR = 0x33BB33;

	private static final String TTL_PDR = "Pixel Dungeon Rescue";
	private static final String TXT_PDR = "bcasiello@gmail.com";
	private static final String LNK_PDR = "github.com/bcasiello/pixel-dungeon-rescue";

	private static final String TTL_SHPX = "based on Shattered Pixel Dungeon by Evan";
	private static final String LNK_SHPX = "ShatteredPixel.com";

	@Override
	public void create() {
		super.create();

		final float colWidth = Camera.main.width;
		final float colTop = 60;

		Image pdr = Icons.WATA.get();
		pdr.x = (colWidth - pdr.width()) / 2;
		pdr.y = colTop;
		align(pdr);
		add( pdr );

		new Flare( 7, 64 ).color( 0x112233, true ).show( pdr, 0 ).angularSpeed = +20;

		RenderedTextBlock pdrtitle = renderTextBlock( TTL_PDR, 8 );
		pdrtitle.hardlight( PDR_COLOR );
		add( pdrtitle );

		pdrtitle.setPos((colWidth - pdrtitle.width()) / 2, pdr.y + pdr.height + 5 );
		align(pdrtitle);

		RenderedTextBlock pdrtext = renderTextBlock( TXT_PDR, 8 );
		pdrtext.maxWidth((int)Math.min(colWidth, 120));
		add( pdrtext );

		pdrtext.setPos((colWidth - pdrtext.width()) / 2, pdrtitle.bottom() + 6);
		align(pdrtext);

		RenderedTextBlock pdrlink = renderTextBlock( LNK_PDR, 8 );
		pdrlink.maxWidth(pdrtext.maxWidth());
		pdrlink.hardlight( PDR_COLOR );
		add( pdrlink );

		pdrlink.setPos((colWidth - pdrlink.width()) / 2, pdrtext.bottom() + 4);
		align(pdrlink);

		PointerArea pdrhotArea = new PointerArea( pdrlink.left(), pdrlink.top(), pdrlink.width(), pdrlink.height() ) {
			@Override
			protected void onClick( PointerEvent event ) {
				DeviceCompat.openURI( "https://" + LNK_PDR );
			}
		};
		add( pdrhotArea );

//		Image shpx = Icons.WATA.get();
//		shpx.x = (colWidth - shpx.width()) / 2;
//		shpx.y = pdrlink.top() + shpx.height + 20;
//		align(shpx);
//		add( shpx );
//
//		new Flare( 7, 64 ).color( 0x225511, true ).show( shpx, 0 ).angularSpeed = +20;

		RenderedTextBlock shpxTitle = renderTextBlock( TTL_SHPX, 8 );
		shpxTitle.hardlight(Window.TITLE_COLOR);
		add( shpxTitle );

		shpxTitle.setPos(
				(colWidth - shpxTitle.width()) / 2,
				pdrlink.bottom() + 11
		);
		align(shpxTitle);

		RenderedTextBlock shpxLink = renderTextBlock( LNK_SHPX, 8 );
		shpxLink.maxWidth((int)Math.min(colWidth, 120));
		shpxLink.hardlight(Window.TITLE_COLOR);
		add(shpxLink);
		
		shpxLink.setPos((colWidth - shpxLink.width()) / 2 , shpxTitle.bottom() + 6);
		align(shpxLink);
		
		PointerArea hotArea = new PointerArea( shpxLink.left(), shpxLink.top(), shpxLink.width(), shpxLink.height() ) {
			@Override
			protected void onClick( PointerEvent event ) {
				DeviceCompat.openURI( "http://" + LNK_SHPX );
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
