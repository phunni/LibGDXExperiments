package uk.co.redfruit.gdx.experiments.box2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Box2DExperimentsBasicScreen implements Screen {

    public final static float SCALE = 32f;
	public final static float INV_SCALE = 1f/SCALE;
	public final static float VP_WIDTH = 800f * INV_SCALE;
	public final static float VP_HEIGHT = 480f * INV_SCALE;

    private static final String TAG = "Box2DExperimentsBasicScreen";

    private OrthographicCamera camera;
    private Viewport viewport;

    private World world;
    private Box2DDebugRenderer debugRenderer;


    //Box
    private Body box;
    private FixtureDef boxFixtureDef;
    private PolygonShape square;

    public Box2DExperimentsBasicScreen() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VP_WIDTH, VP_HEIGHT, camera);
        camera.position.set(viewport.getCamera().position.x + VP_WIDTH * 0.5f,
                viewport.getCamera().position.y + VP_HEIGHT * 0.5f, 0);
        camera.update();

        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        createGround();

        // Shape for square
        square = new PolygonShape();
        // 1 meter-sided square
        square.setAsBox(0.5f, 0.5f);

        BodyDef boxBodyDef = getDynamicBodyDef();

        // Fixture definition for our box
        boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = square;
        boxFixtureDef.density = 0.8f;
        boxFixtureDef.friction = 0.8f;
        boxFixtureDef.restitution = 0.55f;

        //boxBodyDef.position.set(viewport.getWorldWidth() / 2 * INV_SCALE,
         //       (viewport.getWorldHeight() - 0.25f) * INV_SCALE);
        boxBodyDef.position.set(VP_WIDTH / 2, (float) (VP_HEIGHT - 0.25));
        box = world.createBody(boxBodyDef);
        box.createFixture(boxFixtureDef);

        Gdx.app.log(TAG, "Bodies: " + world.getBodyCount());
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1f / 60f, 6, 4);

        debugRenderer.render(world, camera.combined);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
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
        debugRenderer.dispose();
        world.dispose();

    }

    private void createGround() {

        float halfGroundWidth = VP_WIDTH;
        float halfGroundHeight = 0.0f; // 1 meter high

        // Create a static body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        // Set the ground position
        groundBodyDef.position.set(halfGroundWidth*0.5f, halfGroundHeight);

        // Create a body from the definition and add it to the world
        Body groundBody = world.createBody(groundBodyDef);

        // Create a rectangle shape which will fit the world_width and 1 meter high
        // (setAsBox takes half-width and half-height as arguments)
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(halfGroundWidth * 0.5f, halfGroundHeight);
        // Create a fixture from our rectangle shape and add it to our ground body
        groundBody.createFixture(groundBox, 0.0f);
        // Free resources
        groundBox.dispose();

    }

    private BodyDef getDynamicBodyDef(){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }
}
