/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.shatteredpixel.shatteredpixeldungeon.items.Item.BlessedType.CURSED;

public class Torch extends Item {

    public static final String AC_LIGHT = "LIGHT";

    public static final float TIME_TO_LIGHT = 1;

    {
        image = ItemSpriteSheet.TORCH;

        stackable = true;

        levelKnown = blessedTypeKnown = true;

        defaultAction = AC_LIGHT;
    }

    @Override
    public ArrayList<String> actions(@NotNull Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_LIGHT);
        return actions;
    }

    @Override
    public void execute(@NotNull Hero hero, @NotNull String action) {

        super.execute(hero, action);

        if (action.equals(AC_LIGHT)) {

            hero.spend(TIME_TO_LIGHT);
            hero.busy();

            hero.sprite.operate(hero.pos);

            detach(hero.belongings.backpack);
            Catalog.countUse(getClass());

            if (blessedType == CURSED && Random.Int(5) == 0) {
                Buff.detach(hero, Light.class);
                Buff.affect(hero, Blindness.class, Blindness.DURATION);
                Sample.INSTANCE.play(Assets.Sounds.CURSED);
                Emitter emitter = hero.sprite.centerEmitter();
                emitter.start(ShadowParticle.CURSE, 0.2f, 3);
            } else {
                float duration = Light.DURATION;
                duration *= switch (blessedType) {
                    case DIVINE -> 2.0f;
                    case BLESSED -> 1.4f;
                    case NORMAL -> 1.0f;
                    case CURSED -> 0.8f;
                };
                Buff.affect(hero, Light.class, duration);
                Sample.INSTANCE.play(Assets.Sounds.BURNING);
                Emitter emitter = hero.sprite.centerEmitter();
                emitter.start(FlameParticle.FACTORY, 0.2f, 3);

                if (blessedType.isHoly() && Random.Int(5) == 0) {
                    Buff.affect(hero, MindVision.class, MindVision.DURATION);
                    Buff.detach(hero, Blindness.class);
                }

            }

        }
    }

    @Override
    public String info() {
        String info = super.info();
        info += switch (blessedType) {
            default -> "";
            case DIVINE -> "\n\n" + Messages.get(Torch.class, "divine");
            case BLESSED -> "\n\n" + Messages.get(Torch.class, "blessed");
            case CURSED -> "\n\n" + Messages.get(Torch.class, "cursed");
        };
        return info;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        int value = 8 * quantity;
        value = (int) (value * switch (blessedType) {
            case DIVINE -> 2;
            case BLESSED -> 1.3f;
            case NORMAL -> 1;
            case CURSED -> 0.5f;
        });
        return value;
    }

}
