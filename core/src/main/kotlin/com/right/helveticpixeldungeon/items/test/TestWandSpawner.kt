package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestWandSpawner : TestItemSpawner<Wand>(WandOfFireblast::class.java, ItemSpriteSheet.WAND_HOLDER) {
    init {
        itemQuantity=null
        itemLvl = 0
    }

    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }

    class WndSetting(val that: TestWandSpawner) : WndItemSpawnerSetting<Wand>(that) {
        override fun getItemClasses(): Iterable<Class<out Wand>> {
            return Generator.Category.WAND.classes.asIterable() as Iterable<Class<out Wand>>
        }

        init {
            initializeWithoutTier()
        }

        override fun initSettingBtn() {
            settingBtn0 = SettingLvlButton(spawner)
            add(settingBtn0)
        }

    }
}