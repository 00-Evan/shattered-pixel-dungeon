package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestSeedSpawner : TestItemSpawner<Plant.Seed>(Sungrass.Seed::class.java, ItemSpriteSheet.SEED_HOLDER) {

    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }

    class WndSetting(val that: TestSeedSpawner) : WndItemSpawnerSetting<Plant.Seed>(that) {
        override fun getItemClasses(): Iterable<Class<out Plant.Seed>> {
            return Generator.Category.SEED.classes.asIterable() as Iterable<Class<out Plant.Seed>>
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