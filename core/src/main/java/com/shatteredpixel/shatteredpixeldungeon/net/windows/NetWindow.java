package com.shatteredpixel.shatteredpixeldungeon.net.windows;

import com.shatteredpixel.shatteredpixeldungeon.net.ui.UI;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class NetWindow extends Window {
        public NetWindow(int width, int height){
            super(width, height, UI.get(UI.Type.WINDOW));
        }
    public NetWindow(){
        super(0, 0, UI.get(UI.Type.WINDOW));
    }
}
