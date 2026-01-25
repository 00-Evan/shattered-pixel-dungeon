package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestStoneSpawner : TestItemSpawner<Runestone>(StoneOfEnchantment::class.java, ItemSpriteSheet.STONE_HOLDER) {
    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }
    class WndSetting(val that: TestStoneSpawner) : WndItemSpawnerSetting<Runestone>(that) {
        override fun getItemClasses(): Iterable<Class<out Runestone>> {
            return Generator.Category.STONE.classes.asIterable() as Iterable<Class<out Runestone>>
        }

        init {
            initializeWithoutTier()
        }

        override fun initSettingBtn() {
            settingBtn0 = SettingQuantityButton(spawner)
            add(settingBtn0)
        }

    }
}

