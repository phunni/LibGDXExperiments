package uk.co.redfruit.gdx.experiments.box2d;

import aurelienribon.bodyeditor.BodyEditorLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by paul on 15/09/15.
 */
public class Box2DExperimentsComplexShapes extends InputAdapter implements Screen {

    private static final float WORLD_WIDTH = 20f;
    private static final float WORLD_HEIGHT = 10f;

    private static final String TAG = "Box2DExperimentsComplexShapes";

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private TextureAtlas atlas;

    private Sprite box;
    private Sprite circle;
    private Sprite bottle;
    private Sprite jigsaw;
    private Sprite ship;

    private PolygonShape squareShape;
    private CircleShape circleShape;

    private FixtureDef boxFixtureDef;
    private FixtureDef circleFixtureDef;
    private FixtureDef bottleFixtureDef;
    private FixtureDef jigsawFixtureDef;
    private FixtureDef shipFixtureDef;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body groundBody;
    private BodyEditorLoader loader;

    private Array<Box2dObject> objects = new Array<Box2dObject>();

    public Box2DExperimentsComplexShapes() {
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();
        loader = new BodyEditorLoader(Gdx.files.internal("box2d/libgdx_experiments.json"));

        createGround();

        atlas = new TextureAtlas(Gdx.files.internal("images/libgdx_box2d_experiments.pack"));

        box = atlas.createSprite("badlogic");
        circle = atlas.createSprite("circle");
        bottle = atlas.createSprite("bottle");
        jigsaw = atlas.createSprite("jigsaw");
        ship = atlas.createSprite("ship");

        createShapesAndFixtureDefs();

    }

    private void createShapesAndFixtureDefs() {
        // Shape for square
        squareShape = new PolygonShape();
        // 1 meter-sided square
        squareShape.setAsBox(1f, 1f);

        // Shape for circles
        circleShape = new CircleShape();
        // 0.5 metres for radius
        circleShape.setRadius(0.9f);

        boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = squareShape;
        boxFixtureDef.density = 0.8f;
        boxFixtureDef.friction = 0.8f;
        boxFixtureDef.restitution = 0.55f;


        // Fixture definition for our circle
        circleFixtureDef = new FixtureDef();
        circleFixtureDef.shape = circleShape;
        circleFixtureDef.density = 0.5f;
        circleFixtureDef.friction = 5.4f;
        circleFixtureDef.restitution = 0.4f;

        //Fixture for bottle
        bottleFixtureDef = new FixtureDef();
        bottleFixtureDef.density = 0.5f;
        bottleFixtureDef.friction = 5.4f;
        bottleFixtureDef.restitution = 0.4f;

        //Fixture for Jigsaw
        jigsawFixtureDef = new FixtureDef();
        jigsawFixtureDef.density = 0.5f;
        jigsawFixtureDef.friction = 5.4f;
        jigsawFixtureDef.restitution = 0.6f;

        //Fixture for Ship
        shipFixtureDef = new FixtureDef();
        jigsawFixtureDef.density = 0.5f;
        jigsawFixtureDef.friction = 5.4f;
        jigsawFixtureDef.restitution = 0.8f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (Box2dObject currentObject : objects) {
            Vector2 currentBodyPosition = currentObject.body.getPosition();

            Sprite sprite = currentObject.sprite;
            if (currentObject.name.equals("box") || currentObject.name.equals("circle")){
                sprite.setOriginCenter();
                sprite.setBounds(currentBodyPosition.x - sprite.getWidth() / 2,
                        currentBodyPosition.y - sprite.getHeight() / 2, 2, 2);
            } else {
                Vector2 origin = loader.getOrigin(currentObject.name, 1f).cpy();
                sprite.setOrigin(origin.x, origin.y);
                sprite.setBounds(currentBodyPosition.x,  currentBodyPosition.y, 2, 2);
            }

            sprite.setRotation(MathUtils.radiansToDegrees * currentObject.body.getAngle());
            sprite.draw(batch);
        }


        batch.end();

        debugRenderer.render(world, camera.combined);

        world.step(1f / 60f, 6, 2);


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
        atlas.dispose();
        debugRenderer.dispose();
        world.dispose();
        squareShape.dispose();
        circleShape.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 target = camera.unproject(new Vector3(screenX, screenY, 0));
        float x = target.x;
        float y = target.y;

        //int objectType = MathUtils.random(4);
        int objectType = 2;

        Body body;

        switch (objectType) {

            case 0:
                body = createBoxBody(x, y);
                objects.add(new Box2dObject(body, box, "box"));
                break;
            case 1:
                body = createCircleBody(x, y);
                objects.add(new Box2dObject(body, circle, "circle"));
                break;
            case 2:
                body = createBottleBody(x, y);
                objects.add(new Box2dObject(body, bottle, "bottle"));
                break;
            case 3:
                body = createJigsawBody(x, y);
                objects.add(new Box2dObject(body, jigsaw, "jigsaw"));
                break;
            case 4:
                body = createShipBody(x, y);
                objects.add(new Box2dObject(body, ship, "ship"));
                break;
        }

        Gdx.app.log(TAG, "Bodies: " + world.getBodyCount());
        Gdx.app.log(TAG, "Objects: " + objects.size);

        return true;
    }

    private Body createShipBody(float x, float y) {
        Body body = getBody(x, y);
        loader.attachFixture(body, "ship", shipFixtureDef, 1);
        return body;
    }

    private Body createJigsawBody(float x, float y) {
        Body body = getBody(x, y);
        loader.attachFixture(body, "jigsaw", jigsawFixtureDef, 2);
        return body;
    }

    private Body createBottleBody(float x, float y) {
        Body body = getBody(x, y);
        loader.attachFixture(body, "bottle", bottleFixtureDef, 2);
        return body;
    }

    private Body createCircleBody(float x, float y) {
        Body body = getBody(x, y);
        body.createFixture(circleFixtureDef);
        return body;
    }

    private Body createBoxBody(float x, float y) {
        Body body = getBody(x, y);
        body.createFixture(boxFixtureDef);
        return body;
    }

    private Body getBody(float x, float y) {
        BodyDef bodyDef = getDynamicBodyDef();
        bodyDef.position.set(x, y);
        return world.createBody(bodyDef);
    }

    private void createGround() {

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

    private BodyDef getDynamicBodyDef() {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    private class Box2dObject {

        public Body body;
        public Sprite sprite;
        public String name;

        public Box2dObject(Body body, Sprite sprite, String name) {
            this.body = body;
            this.sprite = sprite;
            this.name = name;
        }


    }

}
