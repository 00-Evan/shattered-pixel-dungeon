package com.right.helveticpixeldungeon.windows

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane
import com.shatteredpixel.shatteredpixeldungeon.ui.Window

abstract class WndGetEmlement(val titleTxt: String = "", val descTxt: String = "") : Window() {

    companion object {
        const val MARGINS = 3
        const val WIDTH = 123
        const val GAP = 1
    }

    var title: RenderedTextBlock? = null
    var desc: RenderedTextBlock? = null
    val list: ScrollingListPane

    val confirmBtn: RedButton
    val cancelBtn: RedButton

    private var INSTANCE: WndGetEmlement? = null

    init {
        INSTANCE?.let {
            it.hide()
            INSTANCE = this
        }

        if (titleTxt.isNotEmpty()) {
            title = PixelScene.renderTextBlock(titleTxt, 9)
            add(title)
        }
        if (descTxt.isNotEmpty()) {
            desc = PixelScene.renderTextBlock(descTxt, 6)
            add(desc)
        }


        list = ScrollingListPane()
        add(list)

        confirmBtn = object : RedButton("CONFIRM", 6) {
            override fun onClick() {
                onEnsure()
                this@WndGetEmlement.hide()
            }
        }
        add(confirmBtn)

        cancelBtn = object : RedButton("CANCEL", 6) {
            override fun onClick() {
                this@WndGetEmlement.hide()
            }
        }
        add(cancelBtn)

        layout()
    }

    open fun onEnsure() {}

    var listPos = 0f
    fun layout() {
        var pos = 0f
        var left = MARGINS.toFloat()
        val width = WIDTH - 2 * MARGINS.toFloat()
        title?.let {
            it.setPos(left, pos)
            it.setSize(width,16f)
            it.maxWidth(width.toInt())

            pos = it.bottom() + GAP
        }
        desc?.let {
            it.setPos(left, pos)
            it.maxWidth(width.toInt())
            pos = it.bottom() + GAP
        }
        listPos = pos
        list.setRect(this.xOffset.toFloat() + MARGINS, listPos, WIDTH - 2 * MARGINS.toFloat(), 90f)
        pos = list.bottom() + GAP

        confirmBtn.setRect(left, pos, (width - 1f) / 2f, 16f);
        cancelBtn.setRect(confirmBtn.right() + 1, pos, (width - 1f) / 2f, 16f);
        pos = cancelBtn.bottom() + GAP

        this.resize(WIDTH, pos.toInt())

        updateElements()
    }

    open fun scrollY() = 0f
    open fun updateElements() {
        //list.addTitle("TODO")
        addElement(list)

        list.setRect(this.xOffset.toFloat() + MARGINS, listPos, WIDTH - 2 * MARGINS.toFloat(), 90f)
        list.scrollTo(0f, scrollY())
    }

    abstract fun addElement(list: ScrollingListPane)


    override fun offset(xOffset: Int, yOffset: Int) {
        super.offset(xOffset, yOffset)
         list.setRect(this.xOffset.toFloat() + MARGINS, listPos, WIDTH - 2 * MARGINS.toFloat(), 90f)
    }

}