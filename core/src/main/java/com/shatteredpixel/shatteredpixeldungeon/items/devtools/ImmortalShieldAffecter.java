package com.shatteredpixel.shatteredpixeldungeon.items.devtools;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class ImmortalShieldAffecter extends Item {
    {
        image = ItemSpriteSheet.ROUND_SHIELD;
        defaultAction = AC_SWITCH;
    }
    private static final String AC_SWITCH = "switch";

    public void execute(Hero hero, String str) {
        Class cls = MindVision.class;
        Class cls3 = Invulnerability.class;
        super.execute(hero, str);

        if (!str.equals(AC_SWITCH)) {
            return;
        }

        if (Dungeon.hero.buff(cls3) == null) {
            GLog.p(Messages.get(this, "godmode_on", new Object[0]), new Object[0]);
            Buff.prolong(hero, cls3, 3000000.0f);
            Buff.affect(hero, cls, 2.0E7f);
            // 开启 shielded 状态
            hero.sprite.add(CharSprite.State.SHIELDED);
        } else {
            GLog.n(Messages.get(this, "godmode_off", new Object[0]), new Object[0]);
            Buff.detach(hero, cls3);
            Buff.detach(hero, cls);
            // 关闭 shielded 状态
            hero.sprite.remove(CharSprite.State.SHIELDED);
        }
    }

    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.HP > 0) {
            actions.add(AC_SWITCH);
        }
        return actions;
    }

}