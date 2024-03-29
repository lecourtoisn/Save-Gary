package com.mygdx.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.mygdx.entity.*;
import com.mygdx.event.IEvent;
import com.mygdx.userinterface.elements.Background;
import com.mygdx.util.GenericHolder;

import java.util.HashSet;
import java.util.Set;

public class World {
    private Background background;
    private Grid grid;
    private Garry garry;

    private GenericHolder<WorldEntity> entities;
    private GenericHolder<IEvent> events;

    public World(float width, float height) {
        float GRIDSIZE = 80;
        this.background = new Background(width, height);
        this.grid = new Grid(GRIDSIZE, width, height);
        this.garry = new Garry(this);

        this.entities = new GenericHolder<WorldEntity>();
        this.events = new GenericHolder<IEvent>();

        entities.add(garry);
    }

    public void update(float delta) {
        for (IEvent event : events.getElements()) {
            event.process(delta);
        }

        for (IEntity entity : entities.getElements()) {
            entity.update(delta);
        }
    }

    public void render(Batch batch, Camera cam) {
        Rectangle scissors = new Rectangle();
        Rectangle clipBounds = grid.getBoundaries();
        ScissorStack.calculateScissors(cam, batch.getTransformMatrix(), clipBounds, scissors);

        background.draw(batch);
        grid.draw(batch);
        garry.draw(batch);

        for (IEntity entity : entities.getElements(Arrow.class)) {
            entity.draw(batch);
        }

        batch.flush();
        ScissorStack.pushScissors(scissors);
        for (IEntity entity : entities.getElements(Enemy.class)) {
            entity.draw(batch);
        }
        batch.flush();
        ScissorStack.popScissors();
    }

    public Garry getGarry() {
        return garry;
    }

    public Grid getGrid() {
        return grid;
    }

    public GenericHolder<WorldEntity> getEntities() {
        return entities;
    }

    public GenericHolder<IEvent> getEvents() {
        return events;
    }

    public Set<WorldEntity> getCollisions(WorldEntity entity) {
        Set<WorldEntity> colliding = new HashSet<WorldEntity>();
        for (WorldEntity other : entities.getElements()) {
            Rectangle entityHitbox = entity.getHitbox();
            Rectangle otherHitbox = other.getHitbox();
            if (!entity.equals(other) && entityHitbox.overlaps(otherHitbox)) {
                colliding.add(other);
            }
        }
        return colliding;
    }

    public boolean enemyInPath(Rectangle pathRectangle) {
        for (WorldEntity enemy : entities.getElements(Enemy.class)) {
            if (enemy.getHitbox().overlaps(pathRectangle)) {
                return true;
            }
        }
        return false;
    }
}
