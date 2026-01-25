package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestArtifactSpawner : TestItemSpawner<Artifact>(HornOfPlenty::class.java, ItemSpriteSheet.ARTIFACT_HOLDER) {
    init {
        itemQuantity=null
        itemLvl = 0
    }

    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }

    class WndSetting(val that: TestArtifactSpawner) : WndItemSpawnerSetting<Artifact>(that) {
        override fun getItemClasses(): Iterable<Class<out Artifact>> {
            return Generator.Category.ARTIFACT.classes.asIterable() as Iterable<Class<out Artifact>>
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

