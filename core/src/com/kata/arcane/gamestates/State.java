package com.kata.arcane.gamestates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kata.arcane.ArcaneGame;

public abstract class State {
    protected OrthographicCamera camera;
    protected GameStateManager gsm;
    protected float stateScale;
    protected int stateWidth, stateHeight;

    protected State(GameStateManager gsm, float stateScale) {
        this.gsm = gsm;
        this.stateScale = stateScale;
        camera = new OrthographicCamera();
        stateWidth = (int)(ArcaneGame.WIDTH / this.stateScale);
        stateHeight = (int)(ArcaneGame.HEIGHT / this.stateScale);
        camera.setToOrtho(false, stateWidth, stateHeight);
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();
}
