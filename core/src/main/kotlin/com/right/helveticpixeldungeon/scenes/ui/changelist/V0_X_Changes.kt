package com.right.helveticpixeldungeon.scenes.ui.changelist

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons
import com.shatteredpixel.shatteredpixeldungeon.ui.Window
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo

@Suppress("ClassName")
object V0_X_Changes {

    fun addAllChanges(changeInfos: ArrayList<ChangeInfo>) {
        add_v0_0(changeInfos)
    }
    fun add_v0_0(changeInfos: ArrayList<ChangeInfo>) {
        var changes = ChangeInfo("v0.0", true, "")
        changes.hardlight(Window.TITLE_COLOR)
        changeInfos.add(changes)

//        changes = ChangeInfo("", false, null)
//        changes.hardlight(Window.TITLE_COLOR)
//        changeInfos.add(changes)

        changes = ChangeInfo("v0.0.1", false, null)
        changes.hardlight(Window.TITLE_COLOR)
        changeInfos.add(changes)
        changes.addButton(ChangeHPDButton(Icons.PREFS.get(),"基础设施",""" 
            **-**  基本支持kotlin开发
            **-**  整理一下TitleScene,将HPD与SPD的更新记录分离
            **-**  删除了决斗家
         
        """.trimIndent()))

    }
}