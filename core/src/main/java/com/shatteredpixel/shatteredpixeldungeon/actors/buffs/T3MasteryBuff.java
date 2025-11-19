package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class T3MasteryBuff extends Buff {

    // 定義穿透係數 (0.5 = 50% 穿透)
    public static final float CRIT_PENETRATION = 0.5f;

    // [NEW] 自己定義一個變數來存剩餘時間
    public float left;

    @Override
    public String toString() {
        return "Controlled Fury";
    }

    @Override
    public String desc() {
        return "Your attacks are precise and deadly, penetrating enemy armor.\n\n" +
                "Duration: " + Math.ceil(left) + " turns.";
    }

    // [NEW] 設定圖示 (這會讓它在左上角顯示一個預設圖示，你可以之後再改)
    @Override
    public int icon() {
        return BuffIndicator.VULNERABLE; // 借用碎甲的圖示，比較符合穿透的意象
    }

    // 當 Buff 被施加時設定持續時間
    public void setup(Hero hero, int masteryLevel) {
        int bonusDuration = Math.max(0, masteryLevel - 4);
        // 設定剩餘時間 (例如 3.0f 代表 3 回合)
        this.left = 3f + bonusDuration;
    }

    // [重要] 這是 SPD 的「Update」方法
    @Override
    public boolean act() {
        if (target.isAlive()) {

            // 扣除時間 (TICK 代表 1 個標準回合的時間單位)
            left -= TICK;

            // 如果時間到了，移除 Buff
            if (left <= 0) {
                detach();
            }

            // 告訴系統：我還活著，請在 TICK (1回合) 後再次呼叫我
            spend( TICK );
        } else {
            // 如果宿主死了，Buff 也消失
            detach();
        }

        return true;
    }
}