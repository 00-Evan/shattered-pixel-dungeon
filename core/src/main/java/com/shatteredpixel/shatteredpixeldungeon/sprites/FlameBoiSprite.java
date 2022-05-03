/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * Overgrown Pixel Dungeon
 * Copyright (C) 2018-2019 Anon
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

package  com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.watabou.noosa.TextureFilm;

public class FlameBoiSprite extends MobSprite {

    public FlameBoiSprite() {
        super();

        texture("sprites/dwarven_flameboi.png");

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1 );

        run = new Animation( 5, true );
        run.frames( frames, 2, 3, 4, 5 );

        attack = new Animation( 5, false );
        attack.frames( frames, 6, 7 );

        die = new Animation( 12, false );
        die.frames( frames, 8, 9, 10, 11 );

        play( idle );
    }

    @Override
    public void die() {
        super.die();
        emitter().start( FlameParticle.FACTORY, 0.3f, 3 );
    }
}
