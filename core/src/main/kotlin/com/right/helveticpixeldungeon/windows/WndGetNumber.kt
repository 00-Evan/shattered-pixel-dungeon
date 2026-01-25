package com.right.helveticpixeldungeon.windows

import com.shatteredpixel.shatteredpixeldungeon.Chrome
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton
import com.shatteredpixel.shatteredpixeldungeon.ui.Window
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog
import com.watabou.input.PointerEvent
import com.watabou.noosa.TextInput

open class WndGetNumber(var num: Int? = null) : Window() {

    companion object {
        const val GAP: Int = 2
        const val MARGIN:Int=3
        const val WIDTH: Int = 73
        const val BTN_HEIGHT: Int = 21
        const val BTN_WIDTH: Int = 21
    }

    private var INSTANCE: WndGetNumber? = null

    val textBox: TextInput
    val numBtns = arrayOfNulls<RedButton>(10)
    val clearBtn: RedButton
    val confirmBtn: RedButton
    val cancelBtn: RedButton

    init {
        INSTANCE?.hide()
        INSTANCE = this



        textBox = object : TextInput(Chrome.get(Chrome.Type.TOAST_WHITE), false, (PixelScene.uiCamera.zoom*9).toInt()) {
            override fun enterPressed() {
                super.enterPressed()
                ensure()
            }
        }
        textBox.setText(num.toString())
        textBox.setMaxLength(9)
        textBox.active = true

        add(textBox)

        numBtns[0] = NumBtn(0)
        add(numBtns[0])

        var btnIndex = 1
        // from 1 - 9
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                numBtns[btnIndex] = NumBtn(btnIndex)
                add(numBtns[btnIndex])
                btnIndex++
            }
        }

        clearBtn = object : RedButton("CLEAR", 9) {
            override fun onClick() {
                textBox.text = ""
            }
        }
        add(clearBtn)

        confirmBtn = object : RedButton("CONFIRM", 9) {
            override fun onClick() {
                ensure()
            }
        }
        add(confirmBtn)

        cancelBtn = object : RedButton("CANCEL", 9) {
            override fun onClick() {
                this@WndGetNumber.hide()
            }
        }
        add(cancelBtn)

        PointerEvent.clearKeyboardThisPress = false
        layout()
    }

    fun layout() {

        var pos = 0f
        textBox.setRect(MARGIN + 0f, 0f, WIDTH - 2*MARGIN + 0f, 12f)

        pos = textBox.bottom() + GAP
        for (i in 0 until 3) {
            numBtns[3 * i + 1]!!.setRect(textBox.left(), pos, BTN_WIDTH.toFloat(), BTN_HEIGHT.toFloat())
            numBtns[3 * i + 2]!!.setRect(
                numBtns[3 * i + 1]!!.right() + 1f,
                pos,
                BTN_WIDTH.toFloat(),
                BTN_HEIGHT.toFloat()
            )
            numBtns[3 * i + 3]!!.setRect(
                numBtns[3 * i + 2]!!.right() + 1f,
                pos,
                BTN_WIDTH.toFloat(),
                BTN_HEIGHT.toFloat()
            )

            pos += BTN_HEIGHT + 1f
        }

        numBtns[0]!!.setRect(textBox.left(), pos, BTN_WIDTH.toFloat(), BTN_HEIGHT.toFloat())
        clearBtn.setRect(numBtns[0]!!.right() + 1f, pos, 2 * BTN_WIDTH + 1f, BTN_HEIGHT.toFloat())

        pos = clearBtn.bottom() + 2 * GAP
        confirmBtn.setRect(textBox.left(), pos, (textBox.width() - 2f) / 2f, 16f)
        cancelBtn.setRect(confirmBtn.right() + 2f, pos, confirmBtn.width(), confirmBtn.height())

        pos = confirmBtn.bottom() + 2 * GAP


        resize(WIDTH, pos.toInt())
        textBox.setRect(MARGIN + 0f, textBox.top(), WIDTH - 2f * MARGIN, 12f)
    }

    fun ensure() {
        num =  try {
            textBox.text.toString().toInt()
        } catch (e: NumberFormatException) {
            GLog.i(e.toString())
            null
        }
        onEnsure()
        this.hide()
    }
    open fun onEnsure(){

    }
    inner class NumBtn(val num: Int, label: String = num.toString(), size: Int = 9) : RedButton(label, size) {
        override fun onClick() {
            if (textBox.text.isEmpty() || textBox.text == "0") {
                textBox.text = num.toString()
            } else {
                textBox.text += num.toString()
            }

            // textBox.onChanged()
        }
    }
}