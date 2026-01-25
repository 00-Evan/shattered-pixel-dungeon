package com.right.helveticpixeldungeon.items.weapon.melee

import com.shatteredpixel.shatteredpixeldungeon.Assets
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import java.util.ArrayList

class SteelHandGun : MeleeWeapon() {
    init {
        image= ItemSpriteSheet.STEEL_HANDGUN
        hitSound= Assets.Sounds.HIT
        tier=1
        bones=false

        defaultAction = AC_HIT
    }
    companion object{
        const val AC_HIT = "hit"
    }

    override fun actions(hero: Hero): ArrayList<String> {
         val actions= super.actions(hero)
        actions.add(AC_HIT)
        return actions
    }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)
        if(action == AC_HIT){
            hero.sprite.showStatusWithIconWithScale(0xFFFFFF,"TEST", FloatingText.EXCLAMATION,false)
        }
    }
}