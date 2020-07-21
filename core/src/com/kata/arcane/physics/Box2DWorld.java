package com.kata.arcane.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.kata.arcane.Control;

import java.util.ArrayList;

public class Box2DWorld {
    public World world;
    private Box2DDebugRenderer debugRenderer;
    private ArrayList<Body> bodiesToDestroy;

    public Box2DWorld() {
        world = new World(new Vector2(0, -300), true);
        debugRenderer = new Box2DDebugRenderer();
        bodiesToDestroy = new ArrayList<>();
    }

    public void tick(){
        world.step(Gdx.app.getGraphics().getDeltaTime(), 8, 3);
        world.clearForces();

        RemoveBodies();

    }

    public void RemoveBodies() {
        for(int i = 0; i < bodiesToDestroy.size(); i++) {
            world.destroyBody(bodiesToDestroy.get(i));
        }
        bodiesToDestroy.clear();
    }

    public void render(OrthographicCamera camera, Control control) {
        if (control.debug) debugRenderer.render(world, camera.combined);
    }

    public void RemoveBody(Body body) {
        bodiesToDestroy.add(body);
    }
}
