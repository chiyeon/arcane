package com.kata.arcane.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import org.w3c.dom.css.Rect;

public abstract class Entity {
    public Vector3 position;
    public Texture texture;
    public float width, height;

    private Rectangle boundingBox;

    public Entity(Vector3 position, Texture texture, float width, float height) {
        this.position = position;
        this.texture = texture;
        this.width = width;
        this.height = height;

        boundingBox = new Rectangle(position.x, position.y, width, height);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void update(float dt) {
        boundingBox.set(position.x, position.y, width, height);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width/2, position.y - height/2, width, height);
    }
}
