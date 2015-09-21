package uk.co.redfruit.gdx.experiments.box2d;

import aurelienribon.bodyeditor.BodyEditorLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
public class Box2DExperimentsComplexShapes extends BaseScreen {

    private static final String TAG = "Box2DExperimentsComplexShapes";

    private TextureAtlas atlas;

    private float BOTTLE_WIDTH = 2f;
    private float JIGSAW_WIDTH = 2f;
    private float SHIP_WIDTH = 1.5f;

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

    private BodyEditorLoader loader;

    private Array<Box2dObject> objects = new Array<Box2dObject>();

    public Box2DExperimentsComplexShapes() {
        super();

        loader = new BodyEditorLoader(Gdx.files.internal("box2d/libgdx_experiments.json"));

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
        // 0.9 metres for radius
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
        bottleFixtureDef.density = 1f;
        bottleFixtureDef.friction = 0.5f;
        bottleFixtureDef.restitution = 0.3f;

        //Fixture for Jigsaw
        jigsawFixtureDef = new FixtureDef();
        jigsawFixtureDef.density = 0.5f;
        jigsawFixtureDef.friction = 5.4f;
        jigsawFixtureDef.restitution = 0.4f;

        //Fixture for Ship
        shipFixtureDef = new FixtureDef();
        shipFixtureDef.density = 1f;
        shipFixtureDef.friction = 0.5f;
        shipFixtureDef.restitution = 0.4f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (Box2dObject object : objects) {
            Vector2 position = object.body.getPosition().sub(object.origin);
            object.sprite.setPosition(position.x , position.y);
            if ("box".equals(object.name)) {
                position = object.body.getPosition();
                object.sprite.setOriginCenter();
                object.sprite.setBounds(position.x - object.sprite.getWidth() / 2,
                        position.y - object.sprite.getHeight() / 2, 2, 2);
            } else if ("circle".equals(object.name)) {
                position = object.body.getPosition();
                object.sprite.setOriginCenter();
                object.sprite.setBounds(position.x - object.sprite.getWidth() / 2,
                        position.y - object.sprite.getHeight() / 2, 2, 2);
            } else if ("bottle".equals(object.name)) {
                object.sprite.setPosition(position.x , position.y);
                object.sprite.setOrigin(object.origin.x, object.origin.y);
                object.sprite.setBounds(position.x, position.y, BOTTLE_WIDTH, BOTTLE_WIDTH);
            } else if("jigsaw".equals(object.name)) {
                object.sprite.setPosition(position.x , position.y);
                object.sprite.setOrigin(object.origin.x, object.origin.y);
                object.sprite.setBounds(position.x, position.y, JIGSAW_WIDTH, JIGSAW_WIDTH);
            } else if("ship".equals(object.name)) {
                object.sprite.setPosition(position.x , position.y);
                object.sprite.setOrigin(object.origin.x, object.origin.y);
                object.sprite.setBounds(position.x, position.y, SHIP_WIDTH,
                        SHIP_WIDTH * object.sprite.getHeight()/object.sprite.getWidth());
            }
            object.sprite.setRotation(object.body.getAngle() * MathUtils.radiansToDegrees);
            object.sprite.draw(batch);
        }


        batch.end();

        debugRenderer.render(world, camera.combined);

        world.step(1f / 60f, 6, 2);


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
        super.dispose();
        atlas.dispose();
        squareShape.dispose();
        circleShape.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 target = camera.unproject(new Vector3(screenX, screenY, 0));
        float x = target.x;
        float y = target.y;

        int objectType = MathUtils.random(4);
        //int objectType = 1;

        Body body;

        switch (objectType) {

            case 0:
                body = createBoxBody(x, y);
                objects.add(new Box2dObject(body, box, "box", new Vector2(box.getOriginX(), box.getOriginY())));
                break;
            case 1:
                body = createCircleBody(x, y);
                objects.add(new Box2dObject(body, circle, "circle", new Vector2(circle.getOriginX(), circle.getOriginY())));
                break;
            case 2:
                body = createBottleBody(x, y);
                objects.add(new Box2dObject(body, bottle, "bottle", loader.getOrigin("bottle", BOTTLE_WIDTH)));
                break;
            case 3:
                body = createJigsawBody(x, y);
                objects.add(new Box2dObject(body, jigsaw, "jigsaw",loader.getOrigin("jigsaw", JIGSAW_WIDTH)));
                break;
            case 4:
                body = createShipBody(x, y);
                objects.add(new Box2dObject(body, ship, "ship", loader.getOrigin("ship", SHIP_WIDTH)));
                break;
        }

        Gdx.app.log(TAG, "Bodies: " + world.getBodyCount());
        //Gdx.app.log(TAG, "Objects: " + objects.size);

        return true;
    }

    private Body createShipBody(float x, float y) {
        Body body = getBody(x, y);
        loader.attachFixture(body, "ship", shipFixtureDef, SHIP_WIDTH);
        return body;
    }

    private Body createJigsawBody(float x, float y) {
        Body body = getBody(x, y);
        loader.attachFixture(body, "jigsaw", jigsawFixtureDef, JIGSAW_WIDTH);
        return body;
    }

    private Body createBottleBody(float x, float y) {
        Body body = getBody(x, y);
        loader.attachFixture(body, "bottle", bottleFixtureDef, BOTTLE_WIDTH);
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



    private class Box2dObject {

        public Body body;
        public Sprite sprite;
        public String name;
        public Vector2 origin;

        public Box2dObject(Body body, Sprite sprite, String name, Vector2 origin) {
            this.body = body;
            this.sprite = sprite;
            this.name = name;
            this.origin = origin;
        }


    }

}
