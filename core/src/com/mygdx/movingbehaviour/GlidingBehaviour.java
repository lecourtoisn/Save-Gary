package com.mygdx.movingbehaviour;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.entity.WorldEntity;
import com.mygdx.util.Position;

public class GlidingBehaviour extends MovingBehaviour {

    private float velocity;

    /**
     * @param entity the moving entity
     * @param velocity in unit per second
     */
    public GlidingBehaviour(WorldEntity entity, float velocity) {
        super(entity);
        this.velocity = velocity;
    }

    @Override
    protected Position getNextPosition(float delta) {
        Position pos = new Position(entity.getPosition());
        Vector2 displacement = movement.getUnitVector();
        displacement.scl(delta * velocity);
        pos.add(displacement);
        return pos;
    }
}
