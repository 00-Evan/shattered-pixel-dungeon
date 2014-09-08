package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;

/**
 * Created by debenhame on 08/09/2014.
 */
public class TalismanOfForesight extends Artifact {
    //TODO: actual implementation, i'm going to need to test this one as I write it.

    {
        name = "Talisman of Foresight";
        image = ItemSpriteSheet.ARTIFACT_TALISMAN;
        level = 0;
        levelCap = 10;
        charge = 0;
        exp = 0;
        //partialcharge and chargeCap are unused
    }

    @Override
    public String status() {
        if (charge > 0)
            return Utils.format("%d", charge);
        else
            return null;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Foresight();
    }

    @Override
    public String desc() {
        //TODO: add description
        return "";
    }

    public class Foresight extends ArtifactBuff{

    }
}
