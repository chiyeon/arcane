package com.kata.arcane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class Control extends InputAdapter implements InputProcessor {
    public boolean up, down, left, right;
    public boolean jump;
    public boolean debug;

    public boolean LMB, RMB;
    public Vector2 mouseClickPosition = new Vector2();

    @Override
    public boolean keyDown(int keyCode) {
        switch (keyCode) {
            case Keys.DOWN:
                down = true;
                break;
            case Keys.UP:
                up = true;
                break;
            case Keys.LEFT:
                left = true;
                break;
            case Keys.RIGHT:
                right = true;
                break;
            case Keys.W:
                up = true;
                break;
            case Keys.A:
                left = true;
                break;
            case Keys.S:
                down = true;
                break;
            case Keys.D:
                right = true;
                break;
            case Keys.B:
                debug = !debug;
                break;
            case Keys.SPACE:
                jump = true;
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.DOWN:
                down = false;
                break;
            case Keys.UP:
                up = false;
                break;
            case Keys.LEFT:
                left = false;
                break;
            case Keys.RIGHT:
                right = false;
                break;
            case Keys.W:
                up = false;
                break;
            case Keys.A:
                left = false;
                break;
            case Keys.S:
                down = false;
                break;
            case Keys.D:
                right = false;
                break;
            case Keys.ESCAPE:
                Gdx.app.exit();
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer == 0 && button == 0){
            LMB = true;
        } else if (pointer == 0 && button == 0){
            RMB = true;
        }

        setMouseClickedPos(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer == 0 && button == 0){
            LMB = false;
        } else if (pointer == 0 && button == 0){
            RMB = false;
        }

        setMouseClickedPos(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        setMouseClickedPos(screenX, screenY);
        return false;
    }

    private void setMouseClickedPos(int screenX, int screenY) {
        mouseClickPosition.set(screenX, screenY);
    }
}
