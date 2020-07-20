package com.kata.arcane.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kata.arcane.ArcaneGame;
import javafx.scene.input.MouseButton;

public class MenuState extends State {

    private Texture background;
    private Texture playbutton;

    public MenuState(GameStateManager gsm) {
        super(gsm, 4);

        background = new Texture("main-menu-background.png");
        playbutton = new Texture("playbutton.png");
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isButtonJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            gsm.setState(new LoadingWorldState(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, stateWidth, stateHeight);
        batch.draw(playbutton, stateWidth/2 - playbutton.getWidth()/2, stateHeight/2 - playbutton.getHeight()/2);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playbutton.dispose();
    }
}
