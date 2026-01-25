package com.right.helveticpixeldungeon.windows.test

import com.right.helveticpixeldungeon.items.test.TestItemSpawner
import com.right.helveticpixeldungeon.windows.WndGetNumber
import com.shatteredpixel.shatteredpixeldungeon.Assets
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.Item.BlessedType.*
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane
import com.watabou.noosa.Image
import com.watabou.utils.Reflection
import kotlin.math.abs

abstract class WndItemSpawnerSetting<T : Item>(val spawner: TestItemSpawner<T>) : WndTestToolSetting() {
    companion object {
        const val WIDTH: Int = 134
        const val MARGIN: Int = 3
        const val GAP: Int = 2
    }


    var title: RenderedTextBlock? = null
    var optTier: OptionSlider? = null

    var itemGrid: ScrollingGridPane? = null
    // TODO: use ArrayList to store these buttons when we need more buttons.
    var settingBtn0: SettingButton<T>? = null
    var settingBtn1: SettingButton<T>? = null
    var optBless: OptionSlider? = null

    var genBtn: RedButton? = null

    open fun initialize() {
        initTitle()
        initOptTier()
        initItemGrid()
        initSettingBtn()
        initOptBless()
        initGenBtn()
        layout()
    }

    fun initializeWithoutTier() {
        initTitle()
        initItemGrid()
        initSettingBtn()
        initOptBless()
        initGenBtn()
        layout()
    }

    open fun initOptBless() {
        optBless = object : OptionSlider(
            Messages.get(WndTestToolSetting::class.java, "optbless_title"), "cursed", "divine", -1, 2
        ) {
            override fun onChange() {
                spawner.itemBlessedType = when (this.selectedValue) {
                    -1 -> CURSED
                    0 -> NORMAL
                    1 -> BLESSED
                    2 -> DIVINE
                    else -> NORMAL
                }
            }
        }
        optBless!!.selectedValue = when (spawner.itemBlessedType) {
            CURSED -> -1
            NORMAL -> 0
            BLESSED -> 1
            DIVINE -> 2
        }
        add(optBless)
    }

    abstract fun initSettingBtn()
//    {
//        spawner.itemLvl?.let {
//            spawner.itemLvl = 0
//        }
//        quantityBtn = SettingQuantityButton<T>(spawner)
//        add(quantityBtn)
//    }

    open fun initItemGrid() {
        itemGrid = ScrollingGridPane()
        add(itemGrid)
    }

    open fun getItemGridHeight(): Float = 35f

    open fun initOptTier() {
        optTier = object : OptionSlider(
            Messages.get(WndTestToolSetting::class.java, "opttier_title"), "1", "5", 1, 5
        ) {
            override fun onChange() {
                spawner.tier = this.selectedValue
                updateItemGrid()
            }

        }
        optTier!!.selectedValue = if (spawner.tier in 0..5) spawner.tier else 1
        add(optTier)
    }

    open fun initTitle() {
        title = PixelScene.renderTextBlock(Messages.get(WndTestToolSetting::class.java, "setting_title"), 9)
        add(title)
    }

    open fun initGenBtn() {
        genBtn = object : RedButton(Messages.get(WndTestToolSetting::class.java, "spawn"), 9) {
            override fun onClick() {
                spawner.spawnItem()
                //this@WndItemSpawnerSetting.hide()

            }
        }
        add(genBtn)
    }


    var gridPos = 0f
    fun layout() {
        var left = 0f
        var pos = 0f
        val width = WIDTH - 2 * MARGIN

        title?.let {
            it.setPos(MARGIN + 0f, 0f)
            it.maxWidth(width)
            left = it.left()
            pos = it.bottom() + GAP
        }

        optTier?.let {
            it.setRect(left, pos, width.toFloat(), 20f)
            left = it.left()
            pos = it.bottom() + GAP
        }

        itemGrid?.let {
            gridPos = pos
            it.setRect(this.xOffset.toFloat() + MARGIN, pos, width.toFloat(), getItemGridHeight())

            left = it.left()
            pos = it.bottom() + GAP
        }


        settingBtn0?.let {
            it.setRect(left, pos, width.toFloat(), 16f)
            left = it.left()
            pos = it.bottom() + GAP

        }

        settingBtn1?.let {
            it.setRect(left, pos, width.toFloat(), 16f)
            left = it.left()
            pos = it.bottom() + GAP
        }

        optBless?.let {
            it.setRect(left, pos, width.toFloat(), 20f)
            left = it.left()
            pos = it.bottom() + GAP
        }

        genBtn?.let {
            it.setRect(left, pos, width.toFloat(), 16f)
            left = it.left()
            pos = it.bottom() + GAP
        }

        resize(WIDTH, pos.toInt())

        if (itemGrid != null) {
            updateItemGrid()
        }

    }

    open fun validItemClass(cls: Class<out Item>): Boolean {
        return true
    }

    abstract fun getItemClasses(): Iterable<Class<out Item>>

    open fun getGridItemIcon(item: Item): Image {
        val itemSprite = ItemSprite(item.image, item.glowing())
        return itemSprite
    }

    open fun getGridItemSecondIcon(gridItem: ScrollingGridPane.GridItem, item: Item): Image? {
        if (item.icon == -1) {
            return null
        }

        val secondIcon = Image(Assets.Sprites.ITEM_ICONS)
        secondIcon.frame(ItemSpriteSheet.Icons.film.get(item.icon))
        return secondIcon
    }

    fun updateItemGrid() {
        itemGrid!!.clear()
        val itemCls = getItemClasses()


        for (cls in itemCls) {
            if (!validItemClass(cls)) {
                continue
            }

            val item = Reflection.newInstance(cls) as Item
            val itemSprite = getGridItemIcon(item)
            val gridItem = object : ScrollingGridPane.GridItem(itemSprite) {
                override fun onClick(x: Float, y: Float): Boolean {

                    if (!inside(x, y)) {
                        return false
                    }

                    spawner.itemCls = cls

                    settingBtn0?.updateTxt()
                    settingBtn1?.updateTxt()
                    return true
                }
            }

            val secondIcon = getGridItemSecondIcon(gridItem, item)
            secondIcon?.let {
                gridItem.addSecondIcon(it)
            }
            gridItem.hardLightBG(1f, 1f, 1f)
            itemGrid!!.addItem(gridItem)
        }

        val width = WIDTH - 2 * MARGIN
        itemGrid!!.setRect(
            this.xOffset.toFloat() + MARGIN /*optTier.left()*/, gridPos, width.toFloat(), getItemGridHeight()
        )
        itemGrid!!.scrollTo(0f, 0f)

    }

    abstract class SettingButton<T : Item>(
        val spawner: TestItemSpawner<T>, label: String
    ) : RedButton(label) {
        abstract fun updateTxt()
    }

    open class SettingQuantityButton<T : Item>(spawner: TestItemSpawner<T>) :
        SettingButton<T>(spawner, Messages.get(spawner.itemCls, "name") + " x%d".format(spawner.itemQuantity)) {
        override fun onClick() {
            if (spawner.itemQuantity == null) return;
            val numWnd = object : WndGetNumber(spawner.itemQuantity!!) {
                override fun onEnsure() {
                    spawner.itemQuantity = abs(num?:1)
                    updateTxt()
                }
            }
            GameScene.show(numWnd)
        }

        override fun updateTxt() {
            this.text(Messages.get(spawner.itemCls, "name") + " x%d".format(spawner.itemQuantity))
        }
    }

    open class SettingLvlButton<T : Item>(
        spawner: TestItemSpawner<T>
    ) : SettingButton<T>(
        spawner, Messages.get(
            spawner.itemCls, "name"
        ) + " %+d".format(spawner.itemLvl)
    ) {
        override fun onClick() {
            if (spawner.itemLvl == null) return
            val numWnd = object : WndGetNumber(spawner.itemLvl!!) {
                override fun onEnsure() {
                    spawner.itemLvl = num?:0
                    updateTxt()
                }
            }
            GameScene.show(numWnd)
        }

        override fun updateTxt() {
            this.text(Messages.get(spawner.itemCls, "name") + " %+d".format(spawner.itemLvl))
        }
    }


}