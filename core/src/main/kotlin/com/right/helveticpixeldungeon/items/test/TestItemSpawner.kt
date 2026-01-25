package com.right.helveticpixeldungeon.items.test

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog
import com.watabou.noosa.Game
import com.watabou.utils.Reflection
import java.util.ArrayList

abstract class TestItemSpawner<T : Item>(var itemCls: Class<*>, itemImage: Int = ItemSpriteSheet.HOLDER) : TestTool() {

    companion object {
        const val AC_SETTING = "SETTING"
        const val AC_SPAWN = "SPAWN"
    }


    var tier: Int = 1
    var itemBlessedType: BlessedType = BlessedType.NORMAL
    var itemLvl: Int? = null
    var itemQuantity: Int? = 1

    init {
        image = itemImage
        defaultAction = AC_SPAWN

    }

    override fun actions(hero: Hero): ArrayList<String> {
        val actions = super.actions(hero)
        actions?.add(AC_SETTING)
        actions?.add(AC_SPAWN)
        return actions
    }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)
        if (action == AC_SPAWN) {
            spawnItem()
        } else if (action == AC_SETTING) {

            Game.runOnRenderThread {
                showSetting()
            }
        }
    }

    abstract fun showSetting()

    open fun spawnItem(): T {
        val newItem = Reflection.newInstance(itemCls) as T
        itemLvl?.let { newItem.safeUpgradeOrDegrade(it) }
        itemQuantity?.let { newItem.quantity(it) }

        newItem.blessedType(itemBlessedType)
        newItem.collect()
        newItem.identify()

        GLog.newLine()
        GLog.n("You got ${newItem.name()}${if (itemLvl != null) " +" + itemLvl!! else ""}${if (itemQuantity != null) " x" + itemQuantity!! else ""}.")

        return newItem
    }
}