package uk.co.redfruit.gdx.experiments.box2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
public class Box2DExperimentsSimpleImage extends BaseScreen {

    public final static float WORLD_WIDTH = 20f;
    public final static float WORLD_HEIGHT = 10f;


    private static final String TAG = "Box2DExperimentsSimpleImage";

    private Sprite sprite;


    //Box
    private BodyDef boxBodyDef;
    private FixtureDef boxFixtureDef;
    private PolygonShape square;


    public Box2DExperimentsSimpleImage(Game game) {
        super(game);

        sprite = new Sprite(new Texture(Gdx.files.internal("badlogic.jpg")));

        // Shape for square
        square = new PolygonShape();
        // 1 meter-sided square
        square.setAsBox(0.5f, 0.5f);

        boxBodyDef = getDynamicBodyDef();
        // Fixture definition for our box
        boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = square;
        boxFixtureDef.density = 0.8f;
        boxFixtureDef.friction = 0.8f;
        boxFixtureDef.restitution = 0.55f;

        boxBodyDef.position.set(10f, 10f);
        Body body = world.createBody(boxBodyDef);
        body.createFixture(boxFixtureDef);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        batch.begin();

        for (Body currentBody : bodies) {
            if (!currentBody.equals(groundBody)) {
                Vector2 currentBodyPosition = currentBody.getPosition();
                if (camera.frustum.pointInFrustum(new Vector3(currentBodyPosition.x, currentBodyPosition.y, 0))) {
                    sprite.setBounds(currentBodyPosition.x - sprite.getWidth() / 2,
                            currentBodyPosition.y - sprite.getHeight() / 2, 1, 1);
                    sprite.setOriginCenter();
                    sprite.setRotation(MathUtils.radiansToDegrees * currentBody.getAngle());
                    sprite.draw(batch);
                } else {
                    world.destroyBody(currentBody);
                }
            }
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
        debugRenderer.dispose();
        world.dispose();
        square.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 target = camera.unproject(new Vector3(screenX, screenY, 0));
        float x = target.x;
        float y = target.y;
        //init(x, y);

        boxBodyDef.position.set(x, y);
        Body body = world.createBody(boxBodyDef);
        body.createFixture(boxFixtureDef);


        Gdx.app.log(TAG, "Bodies: " + world.getBodyCount());

        return true;
    }

}
