/*
 * Pixel Dungeon
 * Copyright (C) 2021 saqfish
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

package com.saqfish.spdnet.net.ui;

import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.watabou.noosa.ui.Component;

public class LabeledText extends Component {
    RenderedTextBlock label;
    RenderedTextBlock text;

    private float GAP = 2;
    private String labelText;
    private String textText;
    private int labelSize;
    private int textSize;


    public LabeledText(String label, String text, int labelSize, int textSize){
        this.labelText = label;
        this.textText = text;
        this.labelSize = labelSize;
        this.textSize = textSize;
    }

    public LabeledText(String label, String text){
        this.labelText = label;
        this.textText = text;
        this.labelSize = 9;
        this.textSize = 9;
    }
    @Override
    protected void layout() {
        float bottom = y;
        float right = x;

        label = PixelScene.renderTextBlock(labelText+": ", labelSize);
        add(this.label);

        text = PixelScene.renderTextBlock(textText, textSize);
        add(this.text);

        label.setPos(right, bottom + GAP);

        right = label.right() + GAP;

        text.setPos(right, bottom + GAP);

        height = text.height();
        width = label.width() + text.width() + GAP;
    }

    public RenderedTextBlock text(){
        return this.text;
    }
}
