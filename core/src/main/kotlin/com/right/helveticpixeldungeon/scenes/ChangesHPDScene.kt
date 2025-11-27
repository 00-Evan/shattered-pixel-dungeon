/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.right.helveticpixeldungeon.scenes

import com.shatteredpixel.shatteredpixeldungeon.Assets
import com.shatteredpixel.shatteredpixeldungeon.Chrome
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene
import com.shatteredpixel.shatteredpixeldungeon.ui.*
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle
import com.watabou.noosa.Camera
import com.watabou.noosa.audio.Music
import com.watabou.noosa.ui.Component
import kotlin.math.ceil
import kotlin.math.max

class ChangesHPDScene : PixelScene() {
    public override fun create() {
        super.create()

        Music.INSTANCE.playTracks(
            arrayOf<String>(Assets.Music.THEME_1, Assets.Music.THEME_2),
            floatArrayOf(1f, 1f),
            false
        )

        val w = Camera.main.width
        val h = Camera.main.height

        val title = IconTitle(Icons.CHANGES.get(), Messages.get(ChangesScene::class.java, "title"))
        title.setSize(200f, 0f)
        title.setPos(
            (w - title.reqWidth()) / 2f,
            (20 - title.height()) / 2f
        )
        align(title)
        add(title)

        val btnExit = ExitButton()
        btnExit.setPos(Camera.main.width - btnExit.width(), 0f)
        add(btnExit)

        val panel = Chrome.get(Chrome.Type.TOAST)

        val pw = 135 + panel!!.marginLeft() + panel.marginRight() - 2
        val ph = h - 36

        if (h >= MIN_HEIGHT_FULL && w >= 300) {
            panel.size(pw.toFloat(), ph.toFloat())
            panel.x = (w - pw) / 2f - pw / 2 - 1
            panel.y = 20f

            val rightPanel = Chrome.get(Chrome.Type.TOAST)
            rightPanel!!.size(pw.toFloat(), ph.toFloat())
            rightPanel.x = (w - pw) / 2f + pw / 2 + 1
            rightPanel.y = 20f
            add(rightPanel)

            val rightScroll = ScrollPane(Component())
            add(rightScroll)
            rightScroll.setRect(
                rightPanel.x + rightPanel.marginLeft(),
                rightPanel.y + rightPanel.marginTop() - 1,
                rightPanel.innerWidth() + 2,
                rightPanel.innerHeight() + 2
            )
            rightScroll.scrollTo(0f, 0f)

            val changeTitle = IconTitle(Icons.get(Icons.CHANGES), Messages.get(ChangesScene::class.java, "right_title"))
            changeTitle.setPos(0f, 1f)
            changeTitle.setSize(pw.toFloat(), 20f)
            rightScroll.content().add(changeTitle)

            val body = Messages.get(ChangesScene::class.java, "right_body")

            val changeBody = renderTextBlock(body, 6)
            changeBody.maxWidth(pw - panel.marginHor())
            changeBody.setPos(0f, changeTitle.bottom() + 2)
            rightScroll.content().add(changeBody)
        } else {
            panel.size(pw.toFloat(), ph.toFloat())
            panel.x = (w - pw) / 2f
            panel.y = 20f
        }
        align(panel)
        add(panel)

        val changeInfos = ArrayList<ChangeInfo>()


        val switchSPDBtn =
            object : StyledButton(Chrome.Type.RED_BUTTON, Messages.get(this, "switch_spd")) {
                public override fun onClick() {
                    ChangesScene.changesSelected = 0
                    ShatteredPixelDungeon.switchNoFade(ChangesScene::class.java)
                }
            }

        val switchSPD = object : ChangeInfo("SPD", true, "") {
            override fun onClick(x: Float, y: Float): Boolean {
                if (switchSPDBtn!!.inside(x, y)) {
                    switchSPDBtn.onClick()
                    return true
                }
                return super.onClick(x, y)
            }

            public override fun layout() {
                super.layout()
                var posY = this.y + 12
                if (major) posY += 2f
                val posX = x
                switchSPDBtn!!.setRect(posX + 1, posY, width() - 2, switchSPDBtn.reqHeight())

                line.size(width(), 1f)

                line.x = posX
                line.y = switchSPDBtn.bottom() + 3
            }
        }

        switchSPD!!.add(switchSPDBtn)
        switchSPD.hardlight(Window.TITLE_COLOR)
        switchSPD.layout()
        changeInfos.add(switchSPD)

        val list: ScrollPane = object : ScrollPane(Component()) {
            override fun onClick(x: Float, y: Float) {
                for (info in changeInfos) {
                    if (info.onClick(x, y)) {
                        return
                    }
                }
            }
        }
        add(list)

        val content = list.content()
        content.clear()

        var posY = 0f
        var nextPosY = 0f
        var second = false
        for (info in changeInfos) {
            if (info.major) {
                posY = nextPosY
                second = false
                info.setRect(0f, posY, panel.innerWidth(), 0f)
                content.add(info)
                nextPosY = info.bottom()
                posY = nextPosY
            } else {
                if (!second) {
                    second = true
                    info.setRect(0f, posY, panel.innerWidth() / 2f, 0f)
                    content.add(info)
                    nextPosY = info.bottom()
                } else {
                    second = false
                    info.setRect(panel.innerWidth() / 2f, posY, panel.innerWidth() / 2f, 0f)
                    content.add(info)
                    nextPosY = max(info.bottom(), nextPosY)
                    posY = nextPosY
                }
            }
        }

        content.setSize(panel.innerWidth(), ceil(posY.toDouble()).toInt().toFloat())

        list.setRect(
            panel.x + panel.marginLeft(),
            panel.y + panel.marginTop() - 1,
            panel.innerWidth() + 2,
            panel.innerHeight() + 2
        )
        list.scrollTo(0f, 0f)


        val archs = Archs()
        archs.setSize(Camera.main.width.toFloat(), Camera.main.height.toFloat())
        addToBack(archs)

        fadeIn()
    }

    override fun onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene::class.java)
    }
}

