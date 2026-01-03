package com.right.helveticpixeldungeon.sprites.facilities


import com.right.helveticpixeldungeon.HAssets
import com.shatteredpixel.shatteredpixeldungeon.Assets
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.watabou.noosa.TextureFilm

class WorkTableSprite: FacilitySprite() {
     init{

         //texture(HAssets.Sprites.FACILITY)
         texture(Assets.Sprites.ITEMS)
         //val textureFim= TextureFilm(texture,16,16)
         idle= Animation(15,true)
         //idle?.frames(textureFim,1)
         idle?.frames(ItemSpriteSheet.film, ItemSpriteSheet.WORKTABLE)
         play(idle)
    }
}