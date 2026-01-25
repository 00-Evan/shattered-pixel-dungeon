package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestRingSpawner : TestItemSpawner<Ring>(RingOfWealth::class.java, ItemSpriteSheet.RING_HOLDER) {
    init {
        itemQuantity=null
        itemLvl = 0
    }

    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }

    class WndSetting(val that: TestRingSpawner) : WndItemSpawnerSetting<Ring>(that) {
        override fun getItemClasses(): Iterable<Class<out Ring>> {
            return Generator.Category.RING.classes.asIterable() as Iterable<Class<out Ring>>
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