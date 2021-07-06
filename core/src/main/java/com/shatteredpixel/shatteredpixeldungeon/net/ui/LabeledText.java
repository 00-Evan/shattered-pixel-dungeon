package com.shatteredpixel.shatteredpixeldungeon.net.ui;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
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
