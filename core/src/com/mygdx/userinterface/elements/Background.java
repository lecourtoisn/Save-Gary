package com.mygdx.userinterface.elements;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.entity.Entity;
import com.mygdx.entity.EntityInfo;

public class Background extends Entity {

    public Background(float width, float height) {
        super(EntityInfo.BACKGROUND.getTexture(), new Vector2(width, height));
        setOrigin(0, 0);
    }
}
