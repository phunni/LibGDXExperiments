package uk.co.redfruit.gdx.experiments.box2d;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import uk.co.redfruit.gdx.experiments.MenuScreen;

/**
 * Created by paul on 18/09/15.
 */
public abstract class BaseScreen extends InputAdapter implements Screen {

    protected static final float WORLD_WIDTH = 20f;
    protected static final float WORLD_HEIGHT = 10f;

    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    protected World world;
    protected Box2DDebugRenderer debugRenderer;
    protected Body groundBody;
    protected Game game;

    public BaseScreen(Game game) {
        Gdx.input.setInputProcessor(this);
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        createGround();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        float screenAR = width / (float) height;
        camera = new OrthographicCamera(20, 20 / screenAR);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        batch.dispose();
        debugRenderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            game.setScreen(new MenuScreen(game));
        } else if (keycode == Input.Keys.BACK) {
            game.setScreen(new MenuScreen(game));
        }

        return false;
    }

    protected void createGround() {

        float halfGroundWidth = WORLD_WIDTH;
        float halfGroundHeight = 0.0f;

        // Create a static body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        // Set the ground position
        groundBodyDef.position.set(halfGroundWidth * 0.5f, halfGroundHeight);

        // Create a body from the definition and add it to the world
        groundBody = world.createBody(groundBodyDef);

        // Create a rectangle shape which will fit the world_width and 1 meter high
        // (setAsBox takes half-width and half-height as arguments)
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(halfGroundWidth * 0.5f, halfGroundHeight);
        // Create a fixture from our rectangle shape and add it to our ground body
        groundBody.createFixture(groundBox, 0.0f);
        // Free resources
        groundBox.dispose();

    }

    protected BodyDef getDynamicBodyDef() {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }
}
