package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by debenhame on 03/09/2014.
 */
public class MasterThievesArmband extends Artifact {

    {
        name = "Master Thieves' Armband";
        image = ItemSpriteSheet.ARTIFACT_ARMBAND;
        level = 0;
        levelCap = 10;
        charge = 0;
        chargeCap = 100;
        //partialcharge is unused
    }


    public class Thievery extends ArtifactBuff{

    }
}
