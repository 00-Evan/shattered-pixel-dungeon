package com.right.helveticpixeldungeon.items

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestWeaponSpawner : Item() {

    companion object {
        val AC_SETTING = "SETTING"
        val AC_SPAWN = "SPAWN"
    }

    lateinit var weaponToSpaen: Weapon

    init {
        image = ItemSpriteSheet.WEAPON_HOLDER
        stackable = false
        defaultAction = AC_SPAWN
        bones = false
    }




    override fun actions(hero: Hero?): ArrayList<String?>? {
        val actions = super.actions(hero)
        actions?.add(AC_SETTING)
        actions?.add(AC_SPAWN)
        return actions
    }

    override fun execute(hero: Hero?, action: String?) {
        super.execute(hero, action)
        if (action == AC_SPAWN) {

        }
    }

    override fun isUpgradable(): Boolean = false
    override fun isIdentified(): Boolean = true
    override fun value(): Int = 0
}