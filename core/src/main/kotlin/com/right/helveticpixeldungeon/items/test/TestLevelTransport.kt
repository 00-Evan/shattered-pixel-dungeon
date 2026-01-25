package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.WndGetNumber
import com.shatteredpixel.shatteredpixeldungeon.Dungeon
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation.teleportToLocation
import com.shatteredpixel.shatteredpixeldungeon.levels.Level
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene
import com.watabou.noosa.Game
import kotlin.math.max
import kotlin.math.min

class TestLevelTransport : TestTool() {
    companion object {
        const val AC_UPSTAIR = "upstair"
        const val AC_TRANSLVL = "translvl"
        const val AC_DOWNSTAIR = "downstair"
        const val AC_SETLVL = "setlvl"
        const val AC_TRANSPORT = "transport"

    }

    init {
        defaultAction = AC_TRANSPORT
    }

    var transTo: Int? = null
    override fun actions(hero: Hero): ArrayList<String?>? {
        val actions = super.actions(hero)
        actions.add(AC_UPSTAIR)
        actions.add(AC_TRANSLVL)
        actions.add(AC_DOWNSTAIR)
        actions.add(AC_SETLVL)
        actions.add(AC_TRANSPORT)
        return actions
    }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)
        when (action) {
            AC_UPSTAIR -> {
                transLevel(max(1, Dungeon.depth - 1))
            }

            AC_SETLVL -> {
                val getNumWnd = object : WndGetNumber() {
                    init {
                        num = transTo ?: Dungeon.depth
                        textBox.setText(num.toString())
                    }

                    override fun onEnsure() {
                        num?.let {
                            transTo = it
                            transLevel(it)
                        }
                    }
                }
                Game.runOnRenderThread {
                    GameScene.show(getNumWnd)
                }

            }

            AC_DOWNSTAIR -> {
                transLevel(min(26, Dungeon.depth + 1))
            }

            AC_TRANSLVL -> {
                transTo?.let {
                    transLevel(it)
                }
            }

            AC_TRANSPORT -> {
                GameScene.selectCell(object : CellSelector.Listener() {
                    override fun onSelect(cell: Int?) {
                        transport(hero, cell)
                    }

                    override fun prompt(): String = "Select a cell to transport."
                })
            }

            else -> {}
        }
    }

    fun transport(hero: Hero, to: Int?) {
        if (to == null) return
        teleportToLocation(hero, to, false)
    }

    fun transLevel(to: Int) {
        val transMode = InterlevelScene.Mode.RETURN
        Level.beforeTransition()
        InterlevelScene.mode = transMode
        InterlevelScene.returnDepth = to
        InterlevelScene.returnBranch = 0
        InterlevelScene.returnPos = -1
        Game.switchScene(InterlevelScene::class.java)
    }
}