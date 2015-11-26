package com.mygdx.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Widget extends ClassicEntity {
    public Widget(Texture texture, Vector2 graphicSize) {
        super(texture, graphicSize);
    }

    @Override
    public void setGraphicSize(float x, float y) {
        Vector2 oldSize = graphicSize;
        graphicSize = new Vector2(x, y);
        origin.x *= graphicSize.x / oldSize.x;
        origin.y *= graphicSize.y / oldSize.y;
    }
}
