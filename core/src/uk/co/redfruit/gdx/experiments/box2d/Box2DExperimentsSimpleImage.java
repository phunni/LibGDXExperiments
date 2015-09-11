package uk.co.redfruit.gdx.experiments.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by paul on 11/09/15.
 */
public class Box2DExperimentsSimpleImage extends InputAdapter implements Screen {

    public final static float SCALE = 32f;
    public final static float INV_SCALE = 1f/SCALE;
    public final static float VP_WIDTH = 800f * INV_SCALE;
    public final static float VP_HEIGHT = 480f * INV_SCALE;

    private static final String TAG = "Box2DExperimentsSimpleImage";

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private World world;
    private Box2DDebugRenderer debugRenderer;


    //Box
    private Body box;
    private BodyDef boxBodyDef;
    private FixtureDef boxFixtureDef;
    private PolygonShape square;

    private Array<Box> boxes = new Array<Box>();

    public Box2DExperimentsSimpleImage() {
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VP_WIDTH, VP_HEIGHT, camera);
        camera.position.set(viewport.getCamera().position.x + VP_WIDTH * 0.5f,
                viewport.getCamera().position.y + VP_HEIGHT * 0.5f, 0);
        camera.update();

        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        createGround();


        init(VP_WIDTH / 2, (float) (VP_HEIGHT - 0.25));
    }

    private void init(float x, float y) {
        boxBodyDef = getDynamicBodyDef();
        Texture badlogic = new Texture(Gdx.files.internal("badlogic.jpg"));

        // Shape for square
        square = new PolygonShape();
        // 1 meter-sided square
        square.setAsBox(badlogic.getWidth() * INV_SCALE, badlogic.getHeight() * INV_SCALE);

        // Fixture definition for our box
        boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = square;
        boxFixtureDef.density = 0.8f;
        boxFixtureDef.friction = 0.8f;
        boxFixtureDef.restitution = 0.55f;

        //boxBodyDef.position.set(viewport.getWorldWidth() / 2 * INV_SCALE,
        //       (viewport.getWorldHeight() - 0.25f) * INV_SCALE);
        boxBodyDef.position.set(x, y);
        box = world.createBody(boxBodyDef);
        box.createFixture(boxFixtureDef);
        Box newBox = new Box(x, y, 0 , badlogic );
        newBox.body = box;
        Vector2 origin = box.getLocalCenter();
        newBox.ox = origin.x;
        newBox.oy = origin.y;
        boxes.add(newBox);

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
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Box b : boxes) {
            b.draw(batch);
        }
        batch.end();

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
        square.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 target = camera.unproject(new Vector3(screenX, screenY, 0));
        float x = target.x;
        float y = target.y;
        init(x, y);
        return true;
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

    private class Box {

        public Body body;
        public Texture texture;
        public float x, y;
        public float rotation;
        private float width;
        private float height;
        private int srcWidth;
        private int srcHeight;
        public float ox, oy;

        public Box(float x, float y, float rotation, Texture texture) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
            this.texture = texture;
            srcWidth = texture.getWidth();
            width = srcWidth * INV_SCALE;
            srcHeight = texture.getHeight();
            height = srcHeight * INV_SCALE;
        }

        public void update() {
            Vector2 position = body.getPosition();
            x = position.x;
            y = position.y;
            rotation = body.getAngle() * MathUtils.radiansToDegrees;
        }

        public void draw(Batch batch) {
            batch.draw(texture, x - ox, y - oy, ox, oy, width, height, 1, 1, rotation, 0, 0, srcWidth,
                    srcHeight, false, false);
        }

    }

}
