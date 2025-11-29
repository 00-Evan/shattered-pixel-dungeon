package com.right.helveticpixeldungeon.scenes.ui.changelist

import com.right.helveticpixeldungeon.scenes.ChangesHPDScene
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton
import com.watabou.noosa.Image

class ChangeHPDButton (icon: Image, title: String?, vararg messages: String?): ChangeButton(icon, title, *messages) {
    override fun onClick() {
        ChangesHPDScene.showChangeInfo(Image(icon), title, *messages)
    }
}