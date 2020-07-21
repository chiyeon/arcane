package com.kata.arcane.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.kata.arcane.ArcaneGame;
import com.kata.arcane.Control;
import com.kata.arcane.Enums;
import com.kata.arcane.entities.Player;
import com.kata.arcane.physics.Box2DWorld;
import com.kata.arcane.tiles.GameWorld;
import com.kata.arcane.tiles.Tile;

public class PlayState extends State {
    private Box2DWorld boxWorld;

    private Control control;
    private BitmapFont font;
    private SpriteBatch hudBatch;

    private Player player;

    private GameWorld gameWorld;

    Tile tile;

    public PlayState(GameStateManager gsm) {
        super(gsm, 4);

        boxWorld = new Box2DWorld();

        control = new Control();
        font = new BitmapFont();
        hudBatch = new SpriteBatch();

        player = new Player(new Vector3(0, 30 * 8, 0), boxWorld.world);
        camera.position.y = player.position.y - 5;
        //camera.zoom = 4f;

        gameWorld = new GameWorld(boxWorld);
        Gdx.input.setInputProcessor(control);
        tile = new Tile(Enums.TileType.STONE);

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        gameWorld.UpdateWorld((int)player.position.x, camera, control, player);
        handleInput();
        player.dirX = 0;

        if(control.right) {
            player.dirX = dt * player.movementSpeed;
        } else if(control.left) {
            player.dirX = -dt * player.movementSpeed;
        }

        if(control.jump) {
            control.jump = false;
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, 77);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            gameWorld.UpdateVisibleChunkCollisionBoxes();
        }

        boxWorld.tick();
        player.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {

        camera.position.x = player.position.x;
        camera.position.y = player.position.y + 16;
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(135f/255f, 206f/255f, 250f/255f, 1);
        batch.begin();

        gameWorld.RenderWorld(batch);
        player.render(batch);


        batch.end();

        hudBatch.begin();
        if(player.inventory.showInventory) {

            player.inventory.RenderInventory(font, hudBatch, 20, ArcaneGame.HEIGHT - 128);
            player.inventory.RenderSelectItem(font, hudBatch, 160, ArcaneGame.HEIGHT - 128);
        }
        font.draw(hudBatch,
                "FPS: " + Gdx.graphics.getFramesPerSecond() +
                "\nShow Hitboxes: " + control.debug +
                "\nPlayer Position: " + "(" + (int)player.position.x + ", " + (int)player.position.y + ")",
                20,
                ArcaneGame.HEIGHT - 20);
        hudBatch.end();
        boxWorld.render(camera, control);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
