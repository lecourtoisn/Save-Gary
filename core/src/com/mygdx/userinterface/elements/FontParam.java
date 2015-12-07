package com.mygdx.userinterface.elements;

import com.badlogic.gdx.graphics.Color;

import static com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

public class FontParam {

    public static float ratio = 1;

    public static FreeTypeFontLoaderParameter build(String font, int size, Color color) {
        System.out.println(size + " =>" + Math.round(size/ratio));
        //float ratio = 1;

        //System.out.println(18 * ratio);
        FreeTypeFontLoaderParameter param = new FreeTypeFontLoaderParameter();
        param.fontFileName = font;
        param.fontParameters.size = (int) (ratio * size);
        param.fontParameters.color = color;
        return param;
    }
}
