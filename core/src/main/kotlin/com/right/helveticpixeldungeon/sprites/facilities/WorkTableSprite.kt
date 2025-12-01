package com.right.helveticpixeldungeon.sprites.facilities


import com.right.helveticpixeldungeon.Assets
import com.watabou.noosa.TextureFilm

class WorkTableSprite: FacilitySprite() {
     init{

         texture(Assets.Sprites.FACILITY)
         val textureFim= TextureFilm(texture,16,16)
         idle= Animation(15,true)
         idle?.frames(textureFim,0)
         play(idle)
    }
}