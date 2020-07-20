package com.kata.arcane.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kata.arcane.ArcaneGame;

public class LoadingWorldState extends State {

    private BitmapFont font;
    private boolean shouldBeginLoad = true;

    protected LoadingWorldState(GameStateManager gsm) {
        super(gsm, 1);

        font = new BitmapFont();
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        //show loading screen for one frame before attempting to load, will freeze in this state
        if(shouldBeginLoad) {
            shouldBeginLoad = false;
            gsm.setState(new PlayState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Loading world. . .", stateWidth/2 - 53, stateHeight/2);
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
