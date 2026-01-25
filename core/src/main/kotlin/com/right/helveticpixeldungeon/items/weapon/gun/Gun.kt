package com.right.helveticpixeldungeon.items.weapon.gun

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon


abstract class Gun : Weapon() {
    companion object {
        const val AC_SHOOT = "shoot"
        const val AC_LOAD = "load"
    }

    var tier: Int = 0

    init {
        defaultAction = AC_SHOOT
    }

    override fun actions(hero: Hero): ArrayList<String> {
        val actions = super.actions(hero)
        actions.add(AC_LOAD)
        actions.add(AC_SHOOT)
        return actions
    }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)
        if (action == AC_LOAD) {
            onLoad(hero)
        } else if (action == AC_SHOOT) {
            onShoot(hero)
        }
    }

    abstract fun onLoad(hero: Hero)
    abstract fun onShoot(hero: Hero)

    override fun min(lvl: Int): Int = tier + 1
    override fun max(lvl: Int): Int = 5 * (tier + 1) + lvl * (tier + 1)
    override fun STRReq(lvl: Int): Int = STRReq(tier, lvl) - 2 // less than melee weapon


}